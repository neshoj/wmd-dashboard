package atlas.web.clients.forms;

import atlas.core.template.forms.Form;
import atlas.web.clients.entities.Clients;
import org.springframework.stereotype.Component;

@Component
public class ClientsForm extends Form<Clients> {
    public ClientsForm(){
        setMapping( Clients.class );
        setRole("ROLE_CLIENTS", "Clients", "super-admin");
    }
}
