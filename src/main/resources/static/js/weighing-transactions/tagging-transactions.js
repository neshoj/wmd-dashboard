(function (document, window, $) {
    'use strict';
    var token = $("meta[name='_csrf']").attr("content"),
        header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
        xhr.setRequestHeader("Accept", "application/json");
    });

    $(document).ready(function ($) {

        let ui = $(".edit-view"), oTable,
            fnEdit = function (o) {
                // $('[name="id"]').val( o.id );
                ui.modal("show");
            },
            actions = function( o ){

                return '<div class="xs-mb-0">\n' +
                    '        <div class="btn-toolbar">\n' +
                    '            <div class="btn-group btn-space">\n' +
                    '                <button type="button" class="btn btn-info  btn-rounded  btn-sm view" data-toggle="tooltip" data-placement="top" title="View Ticket" data-index=' + o + ' ><i class="fa fa-eye"></i></button>\n' +
                    '            </div>\n' +
                    '        </div>\n' +
                    '    </div>'


            };

        oTable = utils.dataTable({
            table : $(".table"),
            fnRowCallback: function(nRow, aData){
                //Process data
                let k = 0;
                $(aData).each(function (i) {
                    k++;
                    if (aData.length === k)
                        $(nRow).children('td:eq(-1)').html( actions(aData[i]));
                     });

                //Handle table events
                $(".view", nRow).click( function( e ){
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

    });
})(document, window, jQuery);