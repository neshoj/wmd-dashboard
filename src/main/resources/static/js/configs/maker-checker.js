(function (document, window, $) {
    'use strict';
    let token = $("meta[name='_csrf']").attr("content"),
        header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
        xhr.setRequestHeader("Accept", "application/json");
    });

    $(document).ready(function ($) {

        let ui = $(".edit-view"), oTable,
            fnEdit = function (o) {
                $('[name="id"]').val( o.id );
                $('[name="module"]').val( o.code );
                $('[name="enabled"]', ui).val( o.enabled );
                $('[name="status"]', ui).prop('checked', o.enabled);
                $('[name="action"]', ui).val( "edit" );
                ui.modal("show");
            },
            actions = function( o ){
                // return '<a href="javascript:void(0)" class="pr-10 edit" data-toggle="tooltip" title="edit" data-toggle="tooltip" data-placement="top" title="Edit" data-index='+ o + '><i class="fa fa-edit txt-primary"></i></a>';

                return '<div class="xs-mb-0">\n' +
                    '        <div class="btn-toolbar">\n' +
                    '            <div class="btn-group btn-space">\n' +
                    '                <button type="button" class="btn btn-info btn-sm edit" data-toggle="tooltip" data-placement="top" title="Edit" data-index=' + o + ' ><i class="icon s7-pen"></i></button>\n' +
                    '            </div>\n' +
                    '        </div>\n' +
                    '    </div>'


            };

        oTable = utils.dataTable({
            table : $(".table"),
            fnRowCallback: function(nRow, aData){
                //Process data
                var k = 0;
                $(aData).each(function (i) {
                    k++;
                    if (aData.length === k)
                        $(nRow).children('td:eq(-1)').html( actions(aData[i]));
                    else
                        aData[k] === true ? $(nRow).find('td:eq(' + k + ')').text('Yes') : $(nRow).find('td:eq(' + k + ')').text('No');
                });

                //Handle table events
                $(".edit", nRow).click( function( e ){
                    let index = $(this).data("index");
                    utils.http
                        .jsonRequest(".panel", {'action':'fetch-record', 'index':index })
                        .done( function( o ){
                            if("00" === o.status ) fnEdit( o );
                            else utils.alert.error( o.message );
                        });
                });
            }
        });

        function reloadTables(){
            oTable.fnDraw();
        };
        //Refresh call within helpers
        utils.makerchecker.options.refreshFn = reloadTables;

        //When to refresh table info
        $('[data-action="refresh"]').click( function(){
            setTimeout(function () {
                reloadTables();
            });
        });

        $("form", ui).submit( function(e){
            e.preventDefault();
            $('[name="enabled"]', ui).val( $('[name="status"]', ui).is(":checked") );
            utils.submitForm($(this).serializeArray(), ui);
        });

    });

})(document, window, jQuery);





