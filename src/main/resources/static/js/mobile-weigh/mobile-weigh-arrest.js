(function($, window){
    'use strict';
    let token = $("meta[name='_csrf']").attr("content"),
        header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
        xhr.setRequestHeader("Accept", "application/json");
    });

    $(function(){
        let ui = $(".edit-view"), panel = $(".wrapper");
        function fnEdit( o ){
            $('input[name="id"]', ui).val( o.id );
            $('input[name="transactionDate"]', ui).val( o.transactionDate );
            $('input[name="vehicleNo"]', ui).val( o.vehicleNo );
            $('input[name="transporter"]', ui).val( o.transporter );
            $('input[name="excessGVW"]', ui).val( o.excessGVW );
            $('input[name="excessAxleWeight"]', ui).val( o.excessAxleWeight );
            $('input[name="cargo"]', ui).val( o.cargo );
            $('input[name="origin"]', ui).val( o.origin );
            $('input[name="destination"]', ui).val( o.destination );
            $('textarea[name="action"]', ui).val( o.action );
            $('input[name="action"]', ui).val("edit");
        ui.modal('show');
    }

        //When handling a standard table
        let customStandardTableDt = null;

        //When handling a standard table
        const customStandardTable = {

            // fnStandardActions: (recordId) => {
            //     return  '<div class="xs-mb-0">\n' +
            //         '        <div class="btn-toolbar">\n' +
            //         '            <div class="btn-group btn-space">\n' +
            //         '                <button type="button" class="btn btn-info btn-sm edit" data-toggle="tooltip" data-placement="top" title="Edit" data-index=' +recordId+ '><i class="icon s7-pen"></i></button>\n' +
            //         '                <button type="button" class="btn btn-danger btn-sm delete" data-toggle="tooltip" data-placement="top" title="Delete" data-index=' + recordId + ' ><i class="icon s7-close-circle"></i></button>\n' +
            //         '            </div>\n' +
            //         '        </div>\n' +
            //         '    </div>'
            // },
            //
            // //Fetch a record from the server side, and pass the result to
            // //a callback that handles the edit event
            // fnEdit: (recordId, callback) => {
            //     const request = {action: 'fetch-record', index: recordId};
            //     //Maker sure the submit button is always visible
            //     const ui = options.$editView;
            //     $('button[type="submit"]', ui).toggleClass('d-none', false);
            //     http
            //         .jsonRequest(e, request)
            //         .done((o) => {
            //             if ('00' === o.status) {
            //                 if ('function' === typeof callback) {
            //                     callback(o);
            //                 }
            //             } else alert.error(o.message);
            //         });
            // },


            fnInitTable: (fnCallback) => {
                const $table = $('.table-records');
                const cols = $table.find('thead > tr > th').length;
                const idIndex = cols - 1;

                return $table.ajaxTable({
                    table: $table,
                    fnRowCallback: (nRow, aData) => {
                        //Render Template in the actions column
                        // const recordId = aData[idIndex];
                        // const $actionsTemplate = customStandardTable.fnStandardActions(recordId);
                        // $(nRow).children('td:eq( -1 )').html($actionsTemplate);
                        // //Edit events
                        // $('.edit', nRow).click(function () {
                        //     standardTable.fnEdit(recordId, fnCallback);
                        // });

                    }
                });
            },

            init: (callback) => (customStandardTableDt = customStandardTable.fnInitTable(callback)),
            refreshTable: () => customStandardTableDt.fnDraw()
        };

        customStandardTable.init( fnEdit );

        //When to refresh table info
        $('[data-action="refresh"]').click( function(){
            setTimeout(function () {
                utils.standardTable.refreshTable();
            });
        });

        let $form = $('form', ui);
        $form.find('[name="vehicleNo"]').bind("keyup change", function () {
            $(this).val(function (i, v) {
                return v.replace(/ /g, "").toUpperCase();
            });
        });

        $form.find('[name="transporter"]').bind("keyup change", function () {
            $(this).val(function (i, v) {
                return v.replace(/ /g, "").toUpperCase();
            });
        });

        $form.find('[name="cargo"]').bind("keyup change", function () {
            $(this).val(function (i, v) {
                return v.toUpperCase();
            });
        });

        $form.find('[name="origin"]').bind("keyup change", function () {
            $(this).val(function (i, v) {
                return v.toUpperCase();
            });
        });

        $form.find('[name="destination"]').bind("keyup change", function () {
            $(this).val(function (i, v) {
                return v.toUpperCase();
            });
        });

        $('form', ui).formValidation({
            framework: 'bootstrap',
            excluded: ':disabled',
            fields: {
                transactionDate: {validators: {notEmpty: {message: 'Transaction Date is required'}}},
                vehicleNo: {validators: {notEmpty: {message: 'Vehicle No is required'}}},
                transporter: {validators: {notEmpty: {message: 'Transporter is required'}}},
                excessGVW: {validators: {notEmpty: {message: 'Excess GVW is required'}}},
                excessAxleWeight: {validators: {notEmpty: {message: 'Excess Axle Weight is required'}}},
                cargo: {validators: {notEmpty: {message: 'Cargo is required'}}},
                origin: {validators: {notEmpty: {message: 'Origin is required'}}},
                destination: {validators: {notEmpty: {message: 'Destination is required'}}},
                description: {validators: {notEmpty: {message: 'Description is required'}}}
            }
        }).on('success.form.fv', function (e) {
            e.preventDefault();
            $("form", ui).data('formValidation').resetForm();
            ui.hide();
            utils.http.jsonRequest(panel, $(this).serializeArray())
                .done( function( o ){
                    if( o.status === "00"){
                        utils.alert.success( o.message );
                        customStandardTable.refreshTable();
                        ui.modal("hide");
                    }
                    else{
                        utils.alert.error(o.message, function(){
                            ui.show();
                        });
                    }
                });
        });

        //House keep
        ui.on('hidden.bs.modal', function(){
            utils.houseKeep( ui );
            $("form", ui).data('formValidation').resetForm();
            //always have this default action
            $('input[name="action"]', ui).val('new');
            $('input[name="id"]', ui).val('');
        });

    });
})(jQuery, window);