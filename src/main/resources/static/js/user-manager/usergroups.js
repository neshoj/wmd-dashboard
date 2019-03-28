(function ($, window) {
    'use strict';
    let Site = window.Site,
        token = $("meta[name='_csrf']").attr("content"),
        header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
        xhr.setRequestHeader("Accept", "application/json");
    });

    $(function () {

        $(".table-active thead tr th").each(function () {
            console.log($(this).text());
        });

        let ui = $('.edit-view'),

            initForm = function (o) {
                $("#permissionsEl_leftAll").trigger("click");
                let current = $("[name='baseType']", ui).val();

                console.log(current);

                $("#permissionsEl > option, optgroup").each(function () {

                    if ($(this).data('basetype').indexOf(current) === -1) {
                        console.log("hidden");
                        $(this).addClass("hidden");
                    }

                    //Show this record if it were previously hidden
                    else {
                        console.log("unhidden");
                        $(this).toggleClass("hidden", false);
                    }
                });

                $("#permissionsEl_to").find("option, optgroup").each(function () {

                    if ($(this).data('basetype').indexOf(current) === -1) {
                        $(this).addClass("hidden");
                    }

                    //Show this record if it were previously hidden
                    else {
                        $(this).toggleClass("hidden", false);
                    }
                });

                if (typeof o !== "undefined") {
                    console.log(o.permissions);
                    $.each(o.permissions, (k, v) => {

                        $("#permissionsEl").find("option[value=" + v + "]").prop("selected", true);
                        $("#permissionsEl_rightSelected").trigger("click");

                    });
                }
            },
            fnEdit = function (o) {
                $('input[name="name"]', ui).val(o.name);
                $('input[name="id"]', ui).val(o.id);
                $('input[name="description"]', ui).val(o.description);
                $('select[name="baseType"]', ui).val(o.baseType);

                initForm(o);
                //
                // $('select[name="roles"]', ui).selectpicker('val', o.permissions);
                // $('select[name="roles"]', ui).selectpicker('refresh');

                $('input[name="action"]', ui).val("edit");
                ui.modal("show");
            },
            initFormV2 = function () {
                let current = $("[name='baseType']", ui).val();
                $(".permissions > option", ui).each(function () {
                    if ($(this).data('basetype').indexOf(current) === -1) {
                        $(this).addClass("hide");
                    }

                    //Show this record if it were previously hidden
                    else {
                        $(this).toggleClass("hide", false);
                    }
                });
                $(".permissions").val(null).selectpicker("refresh");
            };

        utils.makerchecker.initTables(fnEdit);
        let refreshTables = function () {
            utils.makerchecker.refreshMkTables();
        };

        //When to refresh table info
        $('[data-action="refresh"]').click(function () {
            setTimeout(function () {
                refreshTables();
            });
        });

        //When submitting the form
        $('form', ui)

            .find("#permissionsEl").multiselect({
            keepRenderingSort: true
        }).end()


            .find("[name='baseType']").change(function () {
            initForm();
        }).end()

            .formValidation({
                framework: 'bootstrap',
                excluded: ':disabled',
                fields: {
                    name: {validators: {notEmpty: {message: 'Name is required'}}},
                    description: {validators: {notEmpty: {message: 'Description is required'}}},

                    roles: {
                        validators: {
                            blank: {},
                            callback: {
                                message: 'Please select at least one permission',
                                callback: function (value, validator, $field) {
                                    let options = validator.getFieldElements('roles').val();
                                    console.log("options:", options);
                                    return (options !== null);
                                }
                            }
                        }
                    }
                }
            }).on('success.form.fv', function (e) {
            e.preventDefault();

            let selected = [];
            $(".permissions > option", ui).each(function () {
                if (!$(this).hasClass("hidden") && $(this).is(':selected')) selected.push($(this).val());
            });
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

