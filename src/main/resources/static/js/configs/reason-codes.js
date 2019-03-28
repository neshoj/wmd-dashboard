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
            $('input[name="name"]', ui).val( o.name );
            $('textarea[name="description"]', ui).val( o.description );
            $('input[name="action"]', ui).val("edit");
            ui.modal('show');
        }


        utils.standardTable.init( fnEdit );

        //When to refresh table info
        $('[data-action="refresh"]').click( function(){
            setTimeout(function () {
                utils.standardTable.refreshTable();
            });
        });

        $('form', ui).formValidation({
            framework: 'bootstrap',
            excluded: ':disabled',
            fields: {
                name: {validators: {notEmpty: {message: 'Name is required'}}},
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
                        utils.standardTable.refreshTable();
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