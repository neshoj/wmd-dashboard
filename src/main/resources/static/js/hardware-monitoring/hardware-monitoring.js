(function (document, window, $) {
    'use strict';
    var token = $("meta[name='_csrf']").attr("content"),
        header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
        xhr.setRequestHeader("Accept", "application/json");
    });

    $(document).ready(function ($) {

        let oTable = utils.dataTable({
            table : $(".table")
        });

        //When to refresh table info
        $('[data-action="refresh"]').click( function(){
            setTimeout(function () {
                oTable.fnDraw();
            });
        });
    });

})(document, window, jQuery);