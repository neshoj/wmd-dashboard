(function ($, window) {
    //Global ajax setup
    'use strict';
    let token = $("meta[name='_csrf']").attr("content"),
        header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
        xhr.setRequestHeader("Accept", "application/json");
    });

    $(function () {
        //Init custom validators
        FormValidation.Validator.mobile = utils.mobileValidator;
        let ui = $('.edit-view'),
            fnEdit = function (o) {
                $('input[name="axleCode"]', ui).val(o.axleCode);
                $('input[name="axleCount"]', ui).val(o.axleCount);
                $('input[name="axleOne"]', ui).val(o.axleOne);
                $('input[name="axleTwo"]', ui).val(o.axleTwo);
                $('input[name="axleThree"]', ui).val(o.axleThree);
                $('input[name="axleFour"]', ui).val(o.axleFour);
                $('input[name="axleFive"]', ui).val(o.axleFive);
                $('input[name="axleSix"]', ui).val(o.axleSix);
                $('input[name="axleSeven"]', ui).val(o.axleSeven);

                $('input[name="action"]', ui).val('edit');
                $('input[name="id"]', ui).val(o.id);
                ui.modal('show');
            };
        utils.makerchecker.initTables(fnEdit);

        //When to refresh table info
        $('[data-action="refresh"]').click(function () {
            setTimeout(function () {
                utils.makerchecker.refreshMkTables();
            });
        });

        $('form', ui)
            .find('[name="axleCode"]').bind("keyup change", function () {
            $(this).val(function (i, v) {
                return v.replace(/ /g, "").toUpperCase();
            });
        });

        //Form validation
        $('form', ui)
            .formValidation({
                framework: 'bootstrap',
                excluded: ':disabled',
                fields: {
                    axleCode: {validators: {notEmpty: {message: 'Code is required'}}},
                    axleCount: {validators: {notEmpty: {message: 'Axle Count is required'}, integer: {
                                message: 'The value is not a valid integer number'}}},
                    axleOne: {validators: {notEmpty: {message: 'Axle Input is required'}, integer: {
                                message: 'The value is not a valid integer number'}}},
                    axleTwo: {validators: {notEmpty: {message: 'Axle Input is required'}}},
                }
            })
            .on('success.fv.form', function (e) {
                e.preventDefault();
                $("form", ui).data('formValidation').resetForm();
                utils.submitForm($(this).serializeArray(), ui);
            });

        //House keep
        ui.on('hidden.bs.modal', function () {
            utils.houseKeep(ui);
            //always have this default action
            $('input[name="action"]', ui).val('new');
        });

    });

})(jQuery, window);