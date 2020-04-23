(function (document, window, $) {
    'use strict';
    var token = $("meta[name='_csrf']").attr("content"),
        header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
        xhr.setRequestHeader("Accept", "application/json");
    });

    $(document).ready(function ($) {

        // let oTable = utils.dataTable({
        //     table : $(".table")
        // });

       let oTable = utils.dataTable({
            table: $(".table"),
            fnRowCallback: function (nRow, aData) {
                //Process data
                let k = 0;
                $(aData).each(function (i) {
                    k++;
                    if (k === 3)
                         $(nRow).find('td:eq(' + k + ')').html('<i class="fa fa-signal fa-2x green"></i>');
                });
            }
        });

        //When to refresh table info
        $('[data-action="refresh"]').click( function(){
            setTimeout(function () {
                oTable.fnDraw();
            });
        });
    });

})(document, window, jQuery);