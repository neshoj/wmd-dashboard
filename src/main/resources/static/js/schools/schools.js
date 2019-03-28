(function($, window){
    //Global ajax setup
    'use strict';
    let token = $("meta[name='_csrf']").attr("content"),
        header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
        xhr.setRequestHeader("Accept", "application/json");
    });

    $(function(){
        //Init custom validators
        FormValidation.Validator.mobile = utils.mobileValidator;
        let ui = $('.edit-view'),
            fnEdit = function( o ){
                $('input[name="name"]', ui).val( o.name );
                $('input[name="location"]', ui).val( o.location );
                $('input[name="contactPhone"]', ui).val( o.contactPhone );
                $('input[name="contactEmail"]', ui).val( o.contactEmail );
                $('input[name="costPerSMS"]', ui).val( o.costPerSMS );

                $('input[name="action"]', ui).val('edit' );
                $('input[name="id"]', ui).val( o.id );
                ui.modal('show');
            };
        utils.makerchecker.initTables( fnEdit );

        //When to refresh table info
        $('[data-action="refresh"]').click( function(){
            setTimeout(function () {
                utils.makerchecker.refreshMkTables();
            });
        });

        //Form validation
        $('form', ui)
            .formValidation({
                framework: 'bootstrap',
                excluded: ':disabled',
                fields:{
                    name: { validators:{ notEmpty :{ message: 'Name is required'}}},
                    location: { validators:{ notEmpty :{ message: 'Location is required'}}},
                    contactPhone:{ validators:{ mobile:{ message: 'Invalid mobile number'},notEmpty :{ message: 'Phone is required'}}},
                    costPerSMS:{ validators:{notEmpty :{ message: 'Cost Per SMS is required'}}},
                    contactEmail:{
                        verbose: false,
                        validators:{
                            notEmpty:{ message:"Email is required"},
                            emailAddress: { message: 'The email address is not valid'},
                            stringLength: { max: 512, message: 'Cannot exceed 512 characters'}
                        }
                    }
                }
            })
            .on('success.fv.form', function(e){
                e.preventDefault();
                $("form", ui).data('formValidation').resetForm();
                utils.submitForm( $(this).serializeArray(), ui);
            });

        //House keep
        ui.on('hidden.bs.modal', function(){
            utils.houseKeep( ui );
            //always have this default action
            $('input[name="action"]', ui).val('new');
        });

    });

})(jQuery, window);