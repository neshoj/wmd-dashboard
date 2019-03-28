(function (document, window, $) {
    'use strict';
    let token = $("meta[name='_csrf']").attr("content"),
        header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
        xhr.setRequestHeader("Accept", "application/json");
    });

    $(document).ready(function ($) {
        let ui = $(".edit-view"), panel = $(".portlet"), oTable;
        function fnEdit( o ){
            $('input[name="id"]', ui).val( o.id );
            $('input[name="name"]', ui).val( o.name );
            $('input[name="value"]', ui).val( o.value );
            $('textarea[name="description"]', ui).val( o.description );
            ui.modal('show');
        }

        oTable = utils.dataTable({
            table : $(".table"),
            fnRowCallback: function(nRow, aData){
                $(nRow).children('td:eq(-1)').html('<a href="javascript:void(0)" class="pr-10 edit" data-toggle="tooltip" title="edit" data-toggle="tooltip" data-placement="top" title="Edit" data-index='+ aData[3] + '><i class="fa fa-edit txt-primary"></i></a>');

                $(".edit", nRow).click( function(){
                    let index = $(this).data("index");

                    utils.http
                        .jsonRequest( panel, {'action':'fetch-record', 'index':index })
                        .done( function( o ){
                            if("00" === o.status ){
                                fnEdit( o );
                            }
                            else utils.alert.error( o.message );
                        });
                });
            }
        });

        //When to refresh table info
        $('[data-action="refresh"]').click( function(){
            setTimeout(function () {
                oTable.fnDraw();
            });
        });

        $('form', ui).formValidation({
            framework: 'bootstrap',
            fields: {
                name: {validators: {notEmpty: {message: 'Name is required'}}},
                description: {validators: {notEmpty: {message: 'Description is required'}}},
                value:{ validators: { notEmpty: { message: 'Value is required'}}}
            }
        }).on('success.form.fv', function (e) {
            e.preventDefault();
            ui.hide();
            utils.http.jsonRequest(panel, $(this).serializeArray())
                .done( function( o ){
                    if( o.status === "00"){
                        utils.alert.success( o.message );
                        oTable.fnDraw();
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
            $('input[name="action"]', ui).val('edit');
        });

    });
})(document, window, jQuery);