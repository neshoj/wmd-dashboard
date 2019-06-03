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
            toggleImageDisplayBasedOnImagePresence = function (base64ImageInput) {
                if (base64ImageInput)
                    return '<img class=\'img-responsive\' src=\'data:image/jpg;base64, ' + base64ImageInput + '\' />';
                else
                    return '<span class="fa fa-truck-moving fa-4x"></span>';
            },
            fnEdit = function (o) {
                $('#ticket-no').text(o.virtualStationTransactionId);
                $('#ticket-date').text(moment(o.dateTime).format("dddd, MMMM Do YYYY HH:mm A"));

                $('#vehicle-no').text(o.frontPlate);
                $('#axle-class').text(o.axleDescription);
                $('#gvw').text(o.totalWeight);
                $('#speed').text(o.velocity);
                $('#verdict').text(o.flag);

                $('#action').text(o.virtualStation);

                let GVW = 0, tableRows = '';
                $("#tblAxleWeights tbody").empty();

                // Populate table data
                tableRows = `<tr>` +
                    `<td class="number">` + o.firstAxleLoad + `</td>` +
                    `<td class="number">` + o.secondAxleLoad + `</td>` +
                    `<td class="number">` + o.thirdAxleLoad + `</td>` +
                    `<td class="number">` + o.fourthAxleLoad + `</td>` +
                    `<td class="number">` + o.fifthAxleLoad + `</td>` +
                    `<td class="number">` + o.sixthAxleLoad + `</td>` +
                    `<td class="number">` + o.seventhAxleLoad + `</td>` +
                    `<td class="number">` + o.eighthAxleLoad + `</td>` +
                    `<td class="number">` + o.ninthAxleLoad + `</td>` +
                    `</tr>`;


                $("#tblAxleWeights tbody").append(tableRows);

                if (o.frontPlateBinaryImage)
                    $('.front-lp-image').html(toggleImageDisplayBasedOnImagePresence(o.frontPlateBinaryImage));

                if (o.backPlateBinaryImage)
                    $('.back-lp-image').html(toggleImageDisplayBasedOnImagePresence(o.backPlateBinaryImage));

                if (o.detailImage)
                    $('.detail-image').html(toggleImageDisplayBasedOnImagePresence(o.detailImage));

                if (o.detailImageBack)
                    $('.detail-image-back').html(toggleImageDisplayBasedOnImagePresence(o.detailImageBack));

                // $('#print-btn').html(
                //     '<a class="btn btn-danger btn-space" target="_blank" href="weighing-transactions/' + o.id + '" ><i class="fa fa-print"></i> Print Ticket </a>'
                // );

                ui.modal("show");
            },
            actions = function (o) {

                return '<div class="xs-mb-0">\n' +
                    '        <div class="btn-toolbar">\n' +
                    '            <div class="btn-group btn-space">\n' +
                    '                <button type="button" class="btn btn-info  btn-rounded  btn-sm view" data-toggle="tooltip" data-placement="top" title="View Ticket" data-index=' + o + ' ><i class="fa fa-eye"></i></button>\n' +
                    '            </div>\n' +
                    '        </div>\n' +
                    '    </div>'


            };


        oTable = utils.dataTable({
            table: $(".table-active"),
            fnRowCallback: function (nRow, aData) {
                //Process data
                let k = 0;
                $(aData).each(function (i) {
                    k++;
                    if (aData.length === k)
                        $(nRow).children('td:eq(-1)').html(actions(aData[i]));
                    else if (k === 1)
                        aData[k] === 'ABOVE TOLERABLE OVERLOAD' ? $(nRow).find('td:eq(' + k + ')').html('<i class="fa fa-car fa-2x red"></i>') :
                            $(nRow).find('td:eq(' + k + ')').html('<i class="fa fa-car fa-2x green"></i>');
                });

                //Handle table events
                $(".view", nRow).click(function (e) {
                    let index = $(this).data("index");
                    utils.http
                        .jsonRequest(".panel", {'action': 'fetch-record', 'index': index})
                        .done(function (o) {
                            if ("00" === o.status) fnEdit(o);
                            else utils.alert.error(o.message);
                        });
                });
            }
        });

        function reloadTables() {
            oTable.fnDraw();
        };
        //Refresh call within helpers
        utils.makerchecker.options.refreshFn = reloadTables;

        //When to refresh table info
        $('[data-action="refresh"]').click(function () {
            setTimeout(function () {
                reloadTables();
            });
        });

    });
})(document, window, jQuery);