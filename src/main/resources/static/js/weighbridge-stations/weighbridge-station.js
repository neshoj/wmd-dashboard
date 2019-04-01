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

        let fnEdit = o => {
            $('input[name="name"]', ui).val(o.name);
            $('input[name="stationCode"]', ui).val(o.stationCode);
            $('input[name="location"]', ui).val(o.location);
            $('input[name="mobileNo"]', ui).val(o.mobileNo);
            $('select[name="weighbridgeTypeNo"]', ui).val(o.weighbridgeTypeNo);

            $('input[name="action"]', ui).val('edit');
            $('input[name="id"]', ui).val(o.id);

            // fnUpdateAccounts( o.merchantNo, o.accountNo );
            ui.modal('show');
        };
        utils.makerchecker.initTables(fnEdit);

        //When to refresh table info
        $('[data-action="refresh"]').click(function () {
            setTimeout(function () {
                utils.makerchecker.refreshMkTables();
            });
        });


        //Form validation
        let $form = $('form', ui);
        $form
            .formValidation({
                framework: 'bootstrap',
                excluded: ':disabled',
                fields: {
                    name: {validators: {notEmpty: {message: 'Name is required'}}},
                    stationCode: {validators: {notEmpty: {message: 'Station code is required'}}},
                    mobileNo: {validators: {mobile: {message: 'Invalid mobile number'}}},
                    location: {validators: {notEmpty: {message: 'Location is required '}}},
                    weighbridgeTypeNo: {validators: {notEmpty: {message: 'Weighbridge type is required '}}}
                }
            })
            .on('success.fv.form', function (e) {
                e.preventDefault();

                utils.submitForm($(this).serializeArray(), ui);
            });

        //House keep
        ui.on('hidden.bs.modal', function () {
            utils.houseKeep(ui);
            $("form", ui).data('formValidation').resetForm();
            //always have this default action
            $('input[name="action"]', ui).val('new');
        });
    }

}