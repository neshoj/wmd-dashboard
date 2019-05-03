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
                $('#ticket-no').text(o.ticketNo);
                $('#ticket-date').text(moment(o.transactionDate).format("dddd, MMMM Do YYYY HH:mm A"));

                $('#vehicle-no').text(o.vehicleNo);
                $('#axle-class').text(o.axleConfiguration);
                $('#gvw').text(o.vehicleGVM);
                $('#operator').text(o.operator === null ? 'N/A' : o.operator);
                $('#shift').text(o.operatorShift);
                $('#cargo').text(o.cargo);
                $('#origin').text(o.origin);
                $('#destination').text(o.destination);
                $('#weighbridge').text(o.weighbridge);

                $('#action').text(o.actionTaken === null ? 'N/A' : o.operator);

                let GVW = 0, tableRows ='';
                $("#tblAxleWeights tbody").empty();

                // Populate table data
                 tableRows = `<tr>` +
                    `<td>` + o.firstAxleType + `</td>` +
                    `<td class="number">` + o.firstAxleLegalWeight + `</td>` +
                    `<td class="number">` + o.firstAxleWeight + `</td>` +
                    `<td class="number">` + o.firstAxleWeightExceededValue + `</td>` +
                    `</tr>`;
                GVW +=o.firstAxleLegalWeight;

                // second axle
                tableRows += `<tr>` +
                    `<td>` + o.secondAxleType + `</td>` +
                    `<td class="number">` + o.secondAxleLegalWeight + `</td>` +
                    `<td class="number">` + o.secondAxleWeight + `</td>` +
                    `<td class="number">` + o.secondAxleWeightExceededValue + `</td>` +
                    `</tr>`;
                GVW +=o.secondAxleLegalWeight;

                if(o.thirdAxleWeight){
                    tableRows += `<tr>` +
                        `<td>` + o.thirdAxleType + `</td>` +
                        `<td class="number">` + o.thirdAxleLegalWeight + `</td>` +
                        `<td class="number">` + o.thirdAxleWeight + `</td>` +
                        `<td class="number">` + o.thirdAxleWeightExceededValue + `</td>` +
                        `</tr>`;
                    GVW +=o.thirdAxleLegalWeight;
                }

                if(o.fourthAxleWeight){
                    tableRows += `<tr>` +
                        `<td>` + o.fourthAxleType + `</td>` +
                        `<td class="number">` + o.fourthAxleLegalWeight + `</td>` +
                        `<td class="number">` + o.fourthAxleWeight + `</td>` +
                        `<td class="number">` + o.fourthAxleWeightExceededValue + `</td>` +
                        `</tr>`;
                    GVW +=o.fourthAxleLegalWeight;
                }

                if(o.fifthAxleWeight){
                    tableRows += `<tr>` +
                        `<td>` + o.fifthAxleType + `</td>` +
                        `<td class="number">` + o.fifthAxleLegalWeight + `</td>` +
                        `<td class="number">` + o.fifthAxleWeight + `</td>` +
                        `<td class="number">` + o.fifthAxleWeightExceededValue + `</td>` +
                        `</tr>`;
                    GVW +=o.fifthAxleLegalWeight;
                }

                if(o.sixthAxleWeight){
                    tableRows += `<tr>` +
                        `<td>` + o.sixthAxleType + `</td>` +
                        `<td class="number">` + o.sixthAxleLegalWeight + `</td>` +
                        `<td class="number">` + o.sixthAxleWeight + `</td>` +
                        `<td class="number">` + o.sixthAxleWeightExceededValue + `</td>` +
                        `</tr>`;
                    GVW +=o.sixthAxleLegalWeight;
                }

                if(o.seventhAxleWeight){
                    tableRows += `<tr>` +
                        `<td>` + o.seventhAxleType + `</td>` +
                        `<td class="number">` + o.seventhAxleLegalWeight + `</td>` +
                        `<td class="number">` + o.seventhAxleWeight + `</td>` +
                        `<td class="number">` + o.seventhAxleWeightExceededValue + `</td>` +
                        `</tr>`;
                    GVW +=o.seventhAxleLegalWeight;
                }


                tableRows += `<tr>` +
                    `<td>GVW</td>` +
                    `<td class="number">`+GVW+`</td>` +
                    `<td class="number">` + o.vehicleGVM + `</td>` +
                    `<td class="number">` + o.gvwExceededWeight + `</td>` +
                    `</tr>`;

                $("#tblAxleWeights tbody").append(tableRows);

                $('#print-btn').html(
                    '<a class="btn btn-danger btn-space" target="_blank" href="weighing-transactions/' + o.id + '" ><i class="fa fa-print"></i> Print Ticket </a>'
                );

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
                        aData[k] === 2 ?
                            $(nRow).find('td:eq(' + k + ')').html('<i class="fa fa-car fa-2x red"></i>') :
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