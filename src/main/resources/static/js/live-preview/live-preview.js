(function (document, window, $) {
    'use strict';
    var token = $("meta[name='_csrf']").attr("content"),
        header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
        xhr.setRequestHeader("Accept", "application/json");
    });

    $(document).ready(function ($) {
        let stompClient = null;
        let oTable = utils.dataTable({
            table: $(".table"),
            fnRowCallback: function (nRow, aData) {
                //Process data
                let k = 0, WEIGH_STATUS=1;
                $(aData).each(function () {
                    k++;
                    if (k === WEIGH_STATUS) {
                        aData[k] === "2" ? $(nRow).find('td:eq(' + k + ')').html('<i class="fa fa-car fa-2x red"></i>') :
                            $(nRow).find('td:eq(' + k + ')').html('<i class="fa fa-car fa-2x green"></i>');
                    }
                });
            }
        });


        //When to refresh table info
        $('[data-action="refresh"]').click(function () {
            setTimeout(function () {
                oTable.fnDraw();
            });
        });

        // Connect to back office via web socket
        // let connect = function () {
        //     console.log('Attempting connection');
        //     let socket = new SockJS('/gs-guide-websocket');
        //     stompClient = Stomp.over(socket);
        //     stompClient.connect({}, function (frame) {
        //         connectBackOffice();
        //         stompClient.subscribe('/topic/weighing-transactions', function (response) {
        //             console.log(JSON.parse(response.body));
        //             oTable.fnDraw();
        //         });
        //     });
        // }

        // let connectBackOffice = function () {
        //     stompClient.send("/app/connect", {}, JSON.stringify({'action': 'connect'}));
        // }

        // Connect to back office
        // connect();

        let timerId = setInterval(() => oTable.fnDraw(), 120000);


    });
})(document, window, jQuery);