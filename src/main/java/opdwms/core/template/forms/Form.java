package opdwms.core.template.forms;

import opdwms.core.template.AppConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import opdwms.web.configs.entities.AuditTrail;
import opdwms.web.configs.entities.MakerChecker;
import opdwms.web.configs.repository.MakerCheckerRepository;
import opdwms.web.usermanager.auth.SecurityUtils;
import opdwms.web.usermanager.entities.AppRoles;
import opdwms.web.usermanager.entities.Permissions;
import opdwms.web.usermanager.entities.Users;
import opdwms.web.usermanager.repository.UserRepository;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Wraps a bean from a given set of params instead of doing it all
 * over again for every bean in this application
 *
 * @param <T>
 * @category    Forms
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Component
@Scope("prototype")
public class Form<T>{

    private T _entity;
    private Class<T> _mapping;
    private Errors _errors;
    private AuditTrail log = new AuditTrail();
    private final AuditData _auditData = new AuditData();
    private String entityRole;
    private String roleReference;
    private String accessLevels;
    private String clientResponse;

    @Autowired
    protected Validator validator;
    @PersistenceContext private EntityManager entityManager;

    @Autowired
    private MakerCheckerRepository makerCheckerRepository;
    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(Form.class);

    public final Class<T> getMapping(){ return _mapping; }
    public final void setMapping(Class<T> mapping){
        _mapping = mapping;
    }

    public void setRole(String entityRole, String roleReference, String accessLevels){
        this.entityRole = entityRole;
        this.roleReference = roleReference;
        this.accessLevels = accessLevels;
    }

    public final T getEntity(){
        return this._entity;
    }

    /**
     * Method exposed to other beans to retrieve audit log
     *
     * @return AuditTrail
     */
    public AuditTrail getLog(){ return this.log; }
    public AuditData auditData(){ return _auditData; }

    /**
     * Retrieve an instance of hibernate session from EntityManager
     * @return
     */
    protected Session getSession(){
        return entityManager.unwrap( Session.class );
    }

    /**
     * Binds an instance of a request(HttpServletRequest) to an entity
     *
     * @param request Current request
     * @return T an instance of the current entity
     */
    public T handleRequests(HttpServletRequest request ){
        _entity = genericBinder( request );

        //Perform validation
        _errors = new BeanPropertyBindingResult(_entity, "target");
        validator.validate(_entity, _errors);
        //Suppose we want to 'auto-validate' our request?
        return _entity;
    }

    /**
     * Check if any changes were submitted; if true, update the entity,
     * else do-nothing and notify calling method
     *
     * @param request
     * @return boolean
     * <code>true</code> The entity was modified
     * <code>false</code> No changes were made
     */
    public boolean handleEditRequest(HttpServletRequest request) {
        //Get an instance of the entity with new values
        Map<String, Object> map ;
        T t = genericBinder( request );
        Long index = 0L;
        boolean isModified = false;
        String editWrapperField = null;

        try {
            if( !ObjectUtils.isEmpty( t ) ){
                //Check if entity has been modified
                final Field[] allFields = t.getClass().getDeclaredFields();
                for(Field field : allFields )
                {
                    //Allow field accessibility: fields will always have private access
                    field.setAccessible( true );
                    if( field.isAnnotationPresent(Id.class)){
                        index = (Long)field.get( t );
                    }
                    else if( field.isAnnotationPresent( EditDataWrapper.class)){
                        editWrapperField = field.getName();
                    }

                    //TODO: preserve resources(it might also be an overhead)
                    if( index != 0L && !StringUtils.isEmpty( editWrapperField )) break;
                }

                if( index != 0L){
                    _entity = _mapping.newInstance();
                    _entity = getSession().get(_mapping, index);

                    //Compare values
                    map = fetchChanges(_entity, t);
                    List<Object> result = (List<Object>)map.get("data");
                    isModified = (result.size() > 0) ;

                    //If there are changes, store the new entity values
                    if( isModified ){
                        if( !StringUtils.isEmpty( editWrapperField ) ) {
                            BeanWrapper wrapper = new BeanWrapperImpl( _entity );
                            ObjectMapper mapper = new ObjectMapper();
                            String data = mapper.writeValueAsString( t );

                            wrapper.setPropertyValue(editWrapperField, data);
                            _entity = (T) wrapper.getWrappedInstance();
                        }

                        System.err.println("======================================================");
                        System.err.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(_entity));
                        System.err.println("======================================================");
                    }
                }
            }
        }
        catch (Exception ex) {
            System.err.println("==================== ERROR =================");
            System.err.println("Error >>    "+ex.getMessage() );
            System.err.println("Cause >>    "+ex.getCause() );
            ex.printStackTrace();
            System.err.println("==================== ERROR =================");
        }
        return isModified;
    }

    /**
     * Bind all matching parameter keys to the current entity
     *
     * @param request
     * @return T
     */
    public T genericBinder(HttpServletRequest request){
        BeanWrapper wrapper = new BeanWrapperImpl( _mapping );
        return binder( wrapper, request);
    }

    /**
     * When handling an existing entity
     *
     * @param request
     * @return T
     */
    public T binderWithIndex(HttpServletRequest request){
        String index = request.getParameter("index");
        _entity = (T)getSession().get(_mapping, Long.valueOf( index ));
        BeanWrapper wrapper = new BeanWrapperImpl( _entity );
        return binder( wrapper, request);

    }

    /**
     * Where the actual work of binding occurs
     *
     * @param wrapper
     * @param request
     * @return T
     */
    public T binder(BeanWrapper wrapper, HttpServletRequest request){
        Map<String, String[]> map = request.getParameterMap();
        Iterator it = map.entrySet().iterator();

        while( it.hasNext() ){
            Map.Entry<String, String[]> entry = ( Map.Entry<String, String[]> )it.next();
            String key = entry.getKey();
            String[] values = entry.getValue();

            //Register custom date editor
            wrapper.registerCustomEditor(Date.class, new CustomDateEditor( new SimpleDateFormat("yyyy-MM-dd"), true));

            //Retrieve form data
            if( false != wrapper.isReadableProperty(key)){

                //When handling a collection of indices
                if( values.length > 1 ){ }

                //When the key has a single value
                else{
                    String value = values[ 0 ];

                    //Remove space characters
                    if( !StringUtils.isEmpty( value ) ) value.trim();
                    wrapper.setPropertyValue(key, values[0]);
                }
            }
        }

        return (T)wrapper.getWrappedInstance();
    }

    /**
     * Generates an entity from request parameters for updating the
     * state of this entity
     *
     * @param request Current request
     * @return T an instance of the entity in question
     */
    public T flagRequests(HttpServletRequest request){
        return null;
    }

    /**
     * Generates an entity and updates the fields than contain deactivation details
     *
     * @param request Current request
     * @return T an instance of the entity in question
     */
    public T deactivateRequest(HttpServletRequest request){
        return binderWithIndex( request );
    }

    /**
     * Fetch the changes yet to be persisted to an object
     *
     * @param index Unique index of the current object to be modified
     * @return Map<String, Object>
     */
    public Map<String, Object> fetchChanges(String index){
        T t = (T)getSession().get(_mapping, Long.valueOf(index));

        final Field[] allFields = t.getClass().getDeclaredFields();
        String data = null;
        try{
            for(Field field : allFields ){
                if( !field.isAccessible() ) field.setAccessible( true );
                if( field.isAnnotationPresent( EditDataWrapper.class)){
                    data = (String)field.get( t );
                }
            }

            if( null != data ){
                //Serialize object
                ObjectMapper mapper = new ObjectMapper();
                T newbean = mapper.readValue(data, _mapping);
                return fetchChanges(t, newbean);
            }
        }
        catch(IOException | IllegalArgumentException |IllegalAccessException e){
            System.err.println("==================== ERROR =================");
            System.err.println("Error >>"+e.getMessage() );
            System.err.println("Cause >>"+e.getCause() );
            e.printStackTrace();
            System.err.println("==================== ERROR =================");
        }
        return null;
    }

    /**
     * Update a persisted object with new values previously persisted into a
     * a column of this entity
     *
     * @param t Entity to be updated
     * @return T Updated entity
     */
    public T mergeChanges(T t){
        final Field[] allFields = t.getClass().getDeclaredFields();
        String data = null, editWrapperField=null;
        try{
            for(Field field : allFields ){

               if( !field.isAccessible() ) field.setAccessible( true );
               if( field.isAnnotationPresent( EditDataWrapper.class)){
                   data = (String)field.get( t );
                   editWrapperField = field.getName();
                   break;
               }
            }

            if( null != data ){
                //Serialize object
                ObjectMapper mapper = new ObjectMapper();
                T newbean = mapper.readValue(data, _mapping);
                BeanWrapper wrapper = new BeanWrapperImpl( t );

                for(Field field : allFields ){
                    if( !field.isAccessible() ) field.setAccessible( true );
                    if( field.isAnnotationPresent( MutableField.class)){

                        //When handling optional values
                        Object value = field.get( newbean );
                        if( field.getAnnotation( MutableField.class).optional() && ObjectUtils.isEmpty( value ) )
                            continue;

                        //Default handling
                        wrapper.setPropertyValue(field.getName(), value);
                    }
                }
                //Reset this column
                wrapper.setPropertyValue(editWrapperField, null);

                return (T)wrapper.getWrappedInstance();
            }
        }
        catch(IOException |IllegalArgumentException |IllegalAccessException e){
            System.err.println("==================== ERROR =================");
            System.err.println("Error >>"+e.getMessage() );
            System.err.println("Cause >>"+e.getCause() );
            e.printStackTrace();
            System.err.println("==================== ERROR =================");
        }
        return null;
    }

    /**
     * Transform entity into a Map object
     * @param index
     * @return Map<String, Object>
     */
    public Map<String, Object> transformEntity(String index){
        _entity = (T)getSession().get(_mapping, Long.valueOf( index ));
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.convertValue(_entity, Map.class);
        map.put("status", "00");
        return map;
    }

    /**
     * Transform entity into a Map object
     *
     * @param entity
     * @return Map<String, Object>
     */
    public Map<String, Object> transformEntity(T entity){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Map map = mapper.convertValue(entity, Map.class);
        map.put("status", "00");
        return map;
    }

    public Errors fetchErrors(){
        return _errors;
    }

    /**
     * Compare the 'modifiable' fields of two beans
     *
     * @param oldbean A persisted instance of this class
     * @param newbean A new bean with new values yet to be persisted
     * @return Map<String, Object> array containing the field name, old value, and the new value
     */
    public Map<String, Object> fetchChanges(T oldbean, T newbean){
        Map<String, Object> map = new HashMap<>();
        try
        {

            if( newbean.getClass() != oldbean.getClass() ){
                throw new IllegalArgumentException("The beans must be of the same class");
            }

            StringBuilder keys = new StringBuilder();
            StringBuilder oldValues = new StringBuilder();
            StringBuilder newValues = new StringBuilder();

            List<Object> changes = new ArrayList<>();
            final Field[] allFields = oldbean.getClass().getDeclaredFields();

            //Define Property Editor placeholders to handle the beans in question
            BeanWrapper oldWrapper = new BeanWrapperImpl( oldbean );
            BeanWrapper newWrapper = new BeanWrapperImpl( newbean );

            for(Field field : allFields ){

                //Manage the fields that we need only
                if( field.isAnnotationPresent( MutableField.class)){
                    Map<String, Object> node = new HashMap<>();
                    String fieldName = field.getAnnotation( MutableField.class ).name();
                    //Enable access of this field
                    if( !field.isAccessible() ) field.setAccessible( true );

                    Object oldValue = oldWrapper.getPropertyValue( field.getName() );
                    Object newValue = newWrapper.getPropertyValue( field.getName() );

                    //TODO: When working with boolean values


                    //When the new value is null, and the field is optional, skip comparison
                    //TODO: Test this snippet until no undesirable results are noted
                    if( field.getAnnotation( MutableField.class).optional() && ObjectUtils.isEmpty( newValue ) )
                        continue;

                    //Compare these two objects
                    if( isModified( oldValue, newValue )){
                        //When handling entities with foreign keys
                        String referenceEntity = field.getAnnotation( MutableField.class ).entity() ;
                        String referenceName = field.getAnnotation( MutableField.class ).reference();
                        if( !StringUtils.isEmpty( referenceEntity) && !StringUtils.isEmpty( referenceName )){

                            oldValue =  getSession().createQuery("SELECT "+referenceName + " FROM "+ referenceEntity + " WHERE id = :id")
                                    .setParameter( "id", oldValue).uniqueResult();

                            newValue =  getSession().createQuery("SELECT "+referenceName + " FROM "+ referenceEntity + " WHERE id = :id")
                                    .setParameter( "id", newValue).uniqueResult();
                        }

                        if( ObjectUtils.isEmpty( oldValue )) oldValue = "N/A";
                        if( ObjectUtils.isEmpty( newValue )) newValue = "N/A";

                        node.put("oldvalue", oldValue);
                        node.put("newvalue", newValue);
                        node.put("field",  fieldName);
                        changes.add( node );

                        if (keys.length() > 0) keys.append(", ");
                        keys.append( fieldName );

                        if (oldValues.length() > 0) oldValues.append(", ");
                        oldValues.append(oldValue);

                        if (newValues.length() > 0) newValues.append(", ");
                        newValues.append(newValue);

                    }
                }
            }
            map.put("data", changes);

            //When to update log entry
            if( changes.size() > 0 ) {
                _auditData
                        .setDescription(keys.toString())
                        .setOldValue(oldValues.toString()).setNewValue(newValues.toString());
            }
        }
        catch(IllegalArgumentException e){
            System.err.println("==================== ERROR =================");
            System.err.println("Error >>"+e.getMessage() );
            System.err.println("Cause >>"+e.getCause() );
            e.printStackTrace();
            System.err.println("==================== ERROR =================");
        }
        return map;
    }

    /**
     * Check if a field value has been modified
     *
     * @param object1
     * @param object2
     * @return boolean
     */
    private boolean isModified(Object object1, Object object2){
        return !ObjectUtils.nullSafeEquals( object1, object2);
    }

    /**
     * Method expose to other beans to client response
     *
     * @return String
     */
    public String getResponse(){
        return this.clientResponse;
    }

    public boolean applyMakerChecker(T t, String action) {
        System.err.println("==========================================================");
        log = new AuditTrail();
        String entityClass = t.getClass().getSimpleName();
        Optional<MakerChecker> oChecker = makerCheckerRepository.findByModule( entityClass );

        boolean executeAuth = false, isAllowed = false;
        boolean authMaker = false, authChecker = false;
        BeanWrapper beanWrapper = new BeanWrapperImpl( t );

        String currentUserEmail = SecurityUtils.getCurrentUserLogin();
        Optional<Users> oUser = userRepository.findByEmail( currentUserEmail );
        Users currentUser = oUser.get();
        Long userNo = currentUser.getId();

        if ( oChecker.isPresent() ) {
            MakerChecker checker = oChecker.get();
            executeAuth = checker.isEnabled();

            System.err.println("executeAuth :" + executeAuth );

            if ( executeAuth ) {
                try {

                    String createdBy = userRepository.findById((Long)beanWrapper.getPropertyValue( "createdBy" )).get().getEmail();
                    String updatedBy = userRepository.findById((Long)beanWrapper.getPropertyValue( "updatedBy" )).get().getEmail();

                    System.err.println("createdBy :" + createdBy);
                    System.err.println("updatedBy :" + updatedBy);

                    switch (action) {
                        case AppConstants.ACTION_ACTIVATE:
                        case AppConstants.ACTION_DELETE:
                        case AppConstants.ACTION_APPROVE_DEACTIVATION:
                        case AppConstants.ACTION_DECLINE_DEACTIVATION:
                        case AppConstants.ACTION_APRROVE_EDIT:
                        case AppConstants.ACTION_DECLINE_EDIT:
                            authChecker = true;
                            break;
                        case AppConstants.ACTION_APRROVE_NEW:
                        case AppConstants.ACTION_DECLINE_NEW:
                            authMaker = true;
                            break;
                    }

                    //Check if maker-checker applies for this user
                    isAllowed = authChecker ? !updatedBy.equals( currentUserEmail ) : ( authMaker ? !createdBy.equals( currentUserEmail ) : false );

                    //Check if the user has the appropriate permissions to execute this action
                    if( isAllowed ) {

//                        boolean hasRole = hasRole(action, currentUser );
//
//                        //Depending on the action, update the record accordingly
//                        if( hasRole ){
//                            //Always, update audit fields
                            beanWrapper.setPropertyValue("updatedBy", userNo );
                            beanWrapper.setPropertyValue("updatedOn", new Date(System.currentTimeMillis()));
                            beanWrapper.setPropertyValue("flag", getStatus( action ));
//
                            _entity = (T)beanWrapper.getWrappedInstance();
//
                            //When handling edit changes
                            if( AppConstants.ACTION_APRROVE_EDIT.equals( action )){
                                System.err.println("Updating entity---------------------------------------");
                                _entity = mergeChanges( _entity );

                                try {
                                    System.err.println("============================ Edited object ==========================");
                                    System.err.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(_entity));
                                    System.err.println("======================================================");
                                    System.err.println("Updating entity---------------------------------------");
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
//
//                        }
                        return  true;
                    }
                    else generateFailedLog( action );

                } catch ( SecurityException | IllegalArgumentException ex) {
                    logger.error("Error occurred while applying maker checker:", ex);
                }
            } else {

                beanWrapper.setPropertyValue("updatedBy", userNo);
                beanWrapper.setPropertyValue("updatedOn", new Date(System.currentTimeMillis()));
                beanWrapper.setPropertyValue("flag", getStatus( action ));

                _entity = (T)beanWrapper.getWrappedInstance();

                //When handling edit changes
                if( AppConstants.ACTION_APRROVE_EDIT.equals( action )){
                    System.err.println("Updating entity---------------------------------------");
                    _entity = mergeChanges( _entity );

                    try {
                        System.err.println("============================ Edited object ==========================");
                        System.err.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(_entity));
                        System.err.println("======================================================");
                        System.err.println("Updating entity---------------------------------------");
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }

                try {
                    System.err.println("======================================================");
                    System.err.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(_entity));
                    System.err.println("======================================================");
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                return true;
            }
        } else {
            beanWrapper.setPropertyValue("updatedBy", userNo);
            beanWrapper.setPropertyValue("updatedOn", new Date(System.currentTimeMillis()));
            beanWrapper.setPropertyValue("flag", getStatus( action ));

            _entity = (T)beanWrapper.getWrappedInstance();

            //When handling edit changes
            if( AppConstants.ACTION_APRROVE_EDIT.equals( action )){
                System.err.println("Updating entity---------------------------------------");
                _entity = mergeChanges( _entity );

                try {
                    System.err.println("============================ Edited object ==========================");
                    System.err.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(_entity));
                    System.err.println("======================================================");
                    System.err.println("Updating entity---------------------------------------");
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            try {
                System.err.println("======================================================");
                System.err.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(_entity));
                System.err.println("======================================================");
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return true;
        }

        System.err.println("==========================================================");
        return false;
//        return true;
    }

    private T updateEntity(T t, String action){



        return t;
    }

    /**
     * Get the new status of the record, given the current action
     * Update AuditTrails
     *
     * @param action
     * @return Character
     */
    private String getStatus(String action){
        String status = " ";
        log
                .setLogType( AuditTrail.USER_GENERATED )
                .setStatus("Success");
        switch (action) {
            case AppConstants.ACTION_ACTIVATE:
                status = AppConstants.STATUS_ACTIVERECORD;
                clientResponse = "Record activated successfully.";
                log.setActivity("Activated an inactive record successfully. Resource: " + roleReference)
                        .setNewValues( "Active" )
                        .setOldValues("Pending Approval");
                break;
            case AppConstants.ACTION_APRROVE_NEW:
                status = AppConstants.STATUS_ACTIVERECORD;
                clientResponse = "Record approved successfully.";
                log.setActivity("Approved a new record successfully. Resource: " + roleReference)
                        .setNewValues( "Active" )
                        .setOldValues("Pending Approval");
                break;
            case AppConstants.ACTION_APRROVE_EDIT:
                status = AppConstants.STATUS_ACTIVERECORD;
                clientResponse = "Record approved successfully.";
                log.setActivity("Approved an edit request successfully. Resource: " + roleReference)
                        .setNewValues( "Active" )
                        .setOldValues("Pending Approval");
                break;
            case AppConstants.ACTION_DECLINE_EDIT:
                clientResponse = "Record declined successfully.";
                status = AppConstants.STATUS_ACTIVERECORD;
                log.setActivity("Declined an edit request successfully. Resource: " + roleReference)
                        .setNewValues( "Active" )
                        .setOldValues("Pending Approval");
                break;
            case AppConstants.ACTION_DECLINE_DEACTIVATION:
                clientResponse = "Record activated successfully.";
                status = AppConstants.STATUS_ACTIVERECORD;
                log.setActivity("Declined a deactivation request successfully. Resource: " + roleReference)
                        .setNewValues( "Active" )
                        .setOldValues("Pending Approval");
                break;
            case AppConstants.ACTION_DECLINE_NEW:
                clientResponse = "Record deleted successfully.";
                status = AppConstants.STATUS_SOFTDELETED;
                log
                        .setActivity("Deleted a new record successfully. Resource: " + roleReference)
                        .setNewValues( "Deleted" )
                        .setOldValues("Pending Approval");
                break;
            case AppConstants.ACTION_DELETE:
                clientResponse = "Record deleted successfully.";
                status = AppConstants.STATUS_SOFTDELETED;
                log.setActivity("Deleted a record successfully. Resource: " + roleReference)
                        .setNewValues( "N/A" )
                        .setOldValues("N/A");
                break;
            case AppConstants.ACTION_APPROVE_DEACTIVATION:
                clientResponse = "Record deactivated successfully.";
                status = AppConstants.STATUS_INACTIVERECORD;
                log.setActivity("Approved a deactivation request successfully. Resource: " + roleReference)
                        .setNewValues( "Inactive" )
                        .setOldValues("Pending Approval");
                break;
        }

        return status;
    }

    /**
     * Generate Audit logs for the corresponding failed action
     *
     * @param action
     * @return Character
     */
    private void generateFailedLog(String action){
        log
                .setLogType( AuditTrail.USER_GENERATED )
                .setStatus("Failed");
        switch (action) {
            case AppConstants.ACTION_ACTIVATE:
                clientResponse = "You can not activate a record you deactivated.";
                log.setActivity("Attempt to activate an inactive record failed -> Maker-Checker failed. Resource: " + roleReference)
                        .setNewValues( "N/A" )
                        .setOldValues("N/A");
                break;
            case AppConstants.ACTION_APRROVE_NEW:
                clientResponse = "You can not approve a record you created.";
                log.setActivity("Attempt to approve a new record failed -> Maker-Checker failed. Resource: " + roleReference)
                        .setNewValues( "N/A" )
                        .setOldValues("N/A");
                break;
            case AppConstants.ACTION_APRROVE_EDIT:
                clientResponse = "You can not approve a record you edited.";
                log.setActivity("Attempt to approve an edit request failed -> Maker-Checker failed. Resource: " + roleReference)
                        .setNewValues( "N/A" )
                        .setOldValues("N/A");
                break;
            case AppConstants.ACTION_DECLINE_EDIT:
                clientResponse = "You can not decline a record you edited.";
                log.setActivity("Attempt to decline an edit request failed -> Maker-Checker failed. Resource: " + roleReference)
                        .setNewValues( "N/A" )
                        .setOldValues("N/A");
                break;
            case AppConstants.ACTION_DECLINE_DEACTIVATION:
                clientResponse = "You can not decline a request you placed.";
                log.setActivity("Attempt to decline a deactivation request failed -> Maker-Checker failed. Resource: " + roleReference)
                        .setNewValues( "N/A" )
                        .setOldValues("N/A");
                break;
            case AppConstants.ACTION_DECLINE_NEW:
                clientResponse = "You can not approve a record you created.";
                log
                        .setActivity("Attempt to delete a new record failed -> Maker-Checker failed. Resource: " + roleReference)
                        .setNewValues( "N/A" )
                        .setOldValues("N/A");
                break;
            case AppConstants.ACTION_DELETE:
                clientResponse = "You can not delete a record you deactivated.";
                log.setActivity("Attempt to delete a record failed -> Maker-Checker failed. Resource: " + roleReference)
                        .setNewValues( "N/A" )
                        .setOldValues("N/A");
                break;
            case AppConstants.ACTION_APPROVE_DEACTIVATION:
                clientResponse = "You can not approve a request you placed.";
                log.setActivity("Attempt to approve a deactivation request failed. Resource: " + roleReference)
                        .setNewValues( "N/A" )
                        .setOldValues("N/A");
                break;
        }

    }

    public boolean hasRole(String action, Users user){
        boolean hasRole = false;
        //Entity permissions
        Session session = getSession();
        Permissions entityPermission = (Permissions) session.createQuery("FROM Permissions WHERE roleLink.name = :name AND appCode = :action ")
                .setParameter("name", entityRole).setParameter("action", action).setMaxResults( 1 ).uniqueResult();

        //If entity permission is null, create this permission
        if( null == entityPermission ){
            insertRole( action );
            //End method execution here
            return false;
        }


        //Current user permissions
        Set<Permissions> userPermissions = new HashSet<>();
        if( null != user.getUserGroupLink() ){
            userPermissions = user.getUserGroupLink().getPermissions();
        }

        if( !userPermissions.isEmpty() ){
            //Fetch
        }



        return hasRole;
    }

    private boolean insertRole(String action ){
        //Check if a role corresponding to this entity has been created
        Session session = getSession();
        List<AppRoles> _role = session.createQuery("FROM AppRoles WHERE appCode = :role").setParameter("role", entityRole).list();

        //Create a set of roles for this entity
        if( _role.isEmpty() ){
            AppRoles role = new AppRoles();
            role
                    .setAppCode( entityRole ).setName( roleReference ).setAppFunction( accessLevels );
            session.persist( role );

            //Insert a set of default permissions
            List<Permissions> permissions = generateMakerCheckerPermissions( role.getId() );
            //Persist to permanent storage
            for ( int i = 0; i < permissions.size(); i++) {
                session.save(permissions.get(i));

                if (i % 20 == 0) {/*Upload batches of 20*/
                    //Write changes to the db and release memory:
                    session.flush();
                    session.clear();
                }
            }

        }

        //When the role is found, check if the permission is available
        else{
            Permissions entityPermission = (Permissions)session.createQuery("FROM Permissions WHERE appCode = :action ")
                    .setParameter("action", action).setMaxResults( 1 ).uniqueResult();

            if( null == entityPermission ){
                Map<String, String> map = permissionsHolder();
                String actionHolder = map.get( action );
                AppRoles role = _role.get( 0 );

                //At the moment, handle a permission within the maker-checker actions
                if( !StringUtils.isEmpty( actionHolder ) ){
                    Permissions permission = new Permissions();
                    permission
                            .setName( actionHolder + roleReference )
                            .setAppCode( action )
                            .setRoleNo( role.getId() );

                    session.persist( permission );
                }
            }
        }

        return false;
    }

    private List<Permissions> generateMakerCheckerPermissions(Long roleId){
        List<Permissions> permissions = new ArrayList<>();

        Map<String, String> map = permissionsHolder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Permissions permission = new Permissions();
            permission.setName( entry.getKey() + roleReference )
                    .setAppCode( entry.getValue() )
                    .setRoleNo( roleId );

            permissions.add( permission );
        }

        return permissions;
    }

    private Map<String, String> permissionsHolder(){
        Map<String, String> map = new LinkedHashMap<>();
        map.put("View ", "default");
        map.put("View Active ", "vactive");
        map.put("View New ", "vnew");
        map.put("View Edited ", "vedit");
        map.put("View Deactivated ", "vdeactivated");
        map.put("View Inactive ", "vinactive");
        map.put("View Archived ", "varchived");
        map.put("Create ", "new");
        map.put("Edit ", "edit");
        map.put("Approve New  ", "approve-new");
        map.put("Decline New ", "decline-new");
        map.put("Approve Edited ", "approve-edit");
        map.put("Decline Edited ", "decline-edit");
        map.put("Approve Deactivated ", "approve-deactivation");
        map.put("Decline Deactivated ", "decline-deactivation");
        map.put("Delete ", "delete");
        map.put("Activate ", "activate");
        return map;
    }
}
