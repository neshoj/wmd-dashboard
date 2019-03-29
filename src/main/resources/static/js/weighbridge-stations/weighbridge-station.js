{
    //Global ajax setup
    'use strict';
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
        xhr.setRequestHeader("Accept", "application/json");
    });

    {
        //Init custom validators
        // FormValidation.Validator.mobile = utils.mobileValidator;
        let ui = $('.edit-view');

        let fnEdit =  o => {
            $('input[name="name"]', ui).val( o.name );
            $('input[name="stationCode"]', ui).val( o.stationCode );
            $('input[name="mobileNo"]', ui).val( o.mobileNo );
            $('input[name="location"]', ui).val( o.location );
            $('input[name="region"]', ui).val( o.region );
            $('select[name="country"]', ui).val( o.country );
            $('select[name="merchantNo"]', ui).val( o.merchantNo );
            $('select[name="axleConfigurationGroupNo"]', ui).val( o.axleConfigurationGroupNo );
            $('select[name="weighbridgeTypeNo"]', ui).val( o.weighbridgeTypeNo );

            $('input[name="action"]', ui).val('edit' );
            $('input[name="id"]', ui).val( o.id );

            // fnUpdateAccounts( o.merchantNo, o.accountNo );
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
        let $form = $('form', ui);
        $form

            //When merchant list is update
            .find( '[name="merchantNo"]').change( function(){
                let recordId = this.value;
                // fnUpdateAccounts( recordId );
            }).end()

            .formValidation({
                framework: 'bootstrap',
                excluded: ':disabled',
                fields:{
                    name: { validators:{ notEmpty :{ message: 'Name is required'}}},
                    stationCode:{ validators:{ notEmpty:{ message: 'Station code is required'}}},
                    mobileNo:{ validators:{ mobile:{ message: 'Invalid mobile number'}}},
                    location :{ validators:{ notEmpty:{ message: 'Location is required '}}},
                    region :{ validators:{ notEmpty:{ message: 'Region is required '}}},
                    country :{ validators:{ notEmpty:{ message: 'Country is required '}}},
                    merchantNo :{ validators:{ notEmpty:{ message: 'Merchant is required '}}},
                    axleConfigurationGroupNo :{ validators:{ notEmpty:{ message: 'Axle Configuration Group is required '}}}
                }
            })
            .on('success.fv.form', function(e){
                e.preventDefault();

                utils.submitForm( $(this).serializeArray(), ui);
            });

        //House keep
        ui.on('hidden.bs.modal', function(){
            utils.houseKeep( ui );
            $("form", ui).data('formValidation').resetForm();
            //always have this default action
            $('input[name="action"]', ui).val('new');
        });

        //Handle checkboxes
        // $("#printPinCheck").change(function(e){
        //     let checked = $(this).prop("checked");
        //     $(".pin-view", ui).toggleClass("d-none", !checked );
        // });
        //
        // $("#printVatCheck").change( function(e){
        //     let checked = $(this).prop("checked");
        //     $(".vat-view", ui).toggleClass("d-none", !checked );
        // });

    }

}