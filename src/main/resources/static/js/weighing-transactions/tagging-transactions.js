(function (document, window, $) {
    'use strict';
    var token = $("meta[name='_csrf']").attr("content"),
        header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
        xhr.setRequestHeader("Accept", "application/json");
    });

    $(document).ready(function ($) {

        let ui = $(".edit-view"), clearTagUI= $(".clear-tags"), oTable,
            fnEdit = function (o) {
                console.log(o.vehicleNo)
                $('#ticketNo').text(o.tagReference);
                $('#ticketDate').text(moment(o.transactionDate).format("dddd, MMMM Do YYYY HH:mm A"));

                $('#plateNo').text(o.vehicleNo);
                $('#taggingSystem').text(o.taggingSystem);

                $('#transgression').text(o.transgression);
                $('#taggingStation').text(o.weighbridge);
                ui.modal("show");
            },
            fnClear = function(o){

            let tagInformation = 'You are about to clear the tag on vehicle '+ o.vehicleNo +
                ' Tagged at '+ o.weighbridge + ' by '+ o.taggingSystem +
                ' for '+ o.transgression +
                ' tag reference '+ o.tagReference

                $('#tagInformation').text(tagInformation);

                $('input[name="tagReference"]', clearTagUI).val(o.tagReference);
                $('input[name="taggingTransactionsNo"]', clearTagUI).val(o.id);
                $('input[name="action"]', clearTagUI).val('clear-tag');

                clearTagUI.modal("show");
            },
            actions = function (o) {

                return '<div class="xs-mb-0">\n' +
                    '        <div class="btn-toolbar">\n' +
                    '            <div class="btn-group btn-space">\n' +
                    '                <button type="button" class="btn btn-info  btn-rounded  btn-sm view" data-toggle="tooltip" data-placement="top" title="View Ticket" data-index=' + o + ' ><i class="fa fa-eye"></i></button>\n' +
                    '            </div>\n' +
                    '        </div>\n' +
                    '    </div>' +
                    '<div class="xs-mb-0">\n' +
                    '        <div class="btn-toolbar">\n' +
                    '            <div class="btn-group btn-space">\n' +
                    '                <button type="button" class="btn btn-success  btn-rounded  btn-sm clear" data-toggle="tooltip" data-placement="top" title="Clear Ticket" data-index=' + o + ' ><i class="fa fa-eraser"></i></button>\n' +
                    '            </div>\n' +
                    '        </div>\n' +
                    '    </div>'

            };

        oTable = utils.dataTable({
            table: $(".table"),
            fnRowCallback: function (nRow, aData) {
                //Process data
                let k = 0;
                $(aData).each(function (i) {
                    k++;
                    if (aData.length === k)
                        $(nRow).children('td:eq(-1)').html(actions(aData[i]));
                });

                //Handle table events
                $(".view", nRow).click(function (e) {
                    let index = $(this).data("index");
                    utils.http
                        .jsonRequest(".panel", {'action': 'fetch-record', 'index': index})
                        .done(function (o) {
                            if ("00" === o.status) fnEdit(o.data);
                            else utils.alert.error(o.message);
                        });
                });
                $(".clear", nRow).click(function (e) {
                    let index = $(this).data("index");
                    utils.http
                        .jsonRequest(".panel", {'action': 'fetch-record', 'index': index})
                        .done(function (o) {
                            if ("00" === o.status) fnClear(o.data);
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

        let $form = $('form', clearTagUI);
        $form
            .formValidation({
                framework: 'bootstrap',
                excluded: ':disabled',
                fields: {
                    narration: {validators: {notEmpty: {message: 'Narration is required'}}}
                }
            })
            .on('success.fv.form', function (e) {
                e.preventDefault();
                utils.submitForm($(this).serializeArray(), clearTagUI);
            });

        //House keep
        clearTagUI.on('hidden.bs.modal', function () {
            utils.houseKeep(clearTagUI);
            $("form", clearTagUI).data('formValidation').resetForm();
            //always have this default action
            $('input[name="action"]', clearTagUI).val('');
        });

    });
})(document, window, jQuery);