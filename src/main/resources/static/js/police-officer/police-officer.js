{
    //Global ajax setup
    'use strict';
    let token = $("meta[name='_csrf']").attr("content"),
        header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
        xhr.setRequestHeader("Accept", "application/json");
    });

    {
        //Init custom validators
        FormValidation.Validator.mobile = utils.mobileValidator;
        let ui = $('.edit-view'),
            fnEdit = function (o) {
                $('input[name="firstName"]', ui).val(o.firstName);
                $('input[name="surname"]', ui).val(o.surname);
                $('input[name="policeNo"]', ui).val(o.policeNo);
                $('select[name="gender"]', ui).val(o.gender);
                $('input[name="action"]', ui).val('edit');
                $('input[name="id"]', ui).val(o.id);
                $('.user-type', ui).hide();
                ui.modal('show');
            };
        utils.makerchecker.initTables(fnEdit);

        //Extra table
        let lockedTable = utils.dataTable({
            table: $('.table-locked'),
            "fnServerParams": function (aoData) {
                aoData.push({"name": "fetch-table", "value": "5"});
            },
            "fnRowCallback": function (nRow, aData) {
                $(nRow).children('td:eq(-1)')
                    .html('<button class="btn btn-danger btn-xs unlock" data-index="' + aData[4] + '" >Unlock</button>');
                $('.unlock', nRow).click(function () {
                    utils.makerchecker.flagRecords("undefined", $(this).data('index'), "unlock");
                });
            }
        });

        let reloadTables = () => {
            utils.makerchecker.refreshMkTables();
            lockedTable.fnDraw();
        };

        //Refresh call within helpers
        utils.makerchecker.options.refreshFn = reloadTables;

        //When to refresh table info
        $('[data-action="refresh"]').click(function () {
            setTimeout(function () {
                reloadTables();
            });
        });

        let fnUpdateGroups = userType => {
            //Handle merchant attributes conflicts
            switch (userType) {
                case 'kenha-admin':
                    userType = 'kenha-admin'
                    break;
                case 'kenha-axle-load-control-officer':
                    userType = 'kenha-axle-load-control-officer'
                    break;
                case 'aea-admin':
                    userType = 'aea-admin'
                    break;
                case 'aea-weighbridge-managers':
                    userType = 'aea-weighbridge-managers'
                    break;
                case 'aea-operations-managers':
                    userType = 'aea-operations-managers'
                    break;
                default:
                    userType = 'super-admin'
            }

            let userGroupCursor = 0;
            $("[name='userGroupNo']", ui).children('option').each(function () {
                let $this = $(this);
                let dataAttribute = $this.data('type');
                console.log('==========',dataAttribute,'===================', userType)
                if (dataAttribute && dataAttribute.indexOf(userType) === -1) {
                    $this.addClass('d-none');
                }
                //Show this record if it were previously hidden
                else {
                    if ($this.hasClass("d-none")) $this.removeClass('d-none', false);
                }
                userGroupCursor++;
            });
        };

        let $form = $('form', ui);
        $form.find('[name="firstName"]').bind("keyup change", function () {
            $(this).val(function (i, v) {
                return v.toUpperCase();
            });
        });
        $form.find('[name="surname"]').bind("keyup change", function () {
            $(this).val(function (i, v) {
                return v.toUpperCase();
            });
        });
        $form.find('[name="policeNo"]').bind("keyup change", function () {
            $(this).val(function (i, v) {
                return v.toUpperCase();
            });
        });

        //Form validation
        $('form', ui)
            .formValidation({
                framework: 'bootstrap',
                fields: {
                    firstName: {validators: {notEmpty: {message: 'First name is required'}}},
                    surname: {validators: {notEmpty: {message: 'Last name is required'}}},
                    gender: {validators: {notEmpty: {message: 'Gender is required'}}},
                    policeNo: {validators: {number: {message: 'Police Number is required'}}}
                }
            })
            .on('success.fv.form', function (e) {
                e.preventDefault();
                utils.submitForm($(this).serializeArray(), ui);
            });

        //House keep
        ui.on('hidden.bs.modal', function () {
            utils.houseKeep(ui);
            //always have this default action
            $('input[name="action"]', ui).val('new');
        });

    }

}