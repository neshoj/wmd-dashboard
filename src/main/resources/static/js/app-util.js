var utils = (function (p) {
    'use strict';

    //The parent element that wraps all the elements to be
    //manipulated in this file
    const e = $('.am-wrapper');

    //Data-table placeholders for maker-checker tables
    let activeTableDt = null;
    let inactiveTableDt = null;
    let deactivatedTableDt = null;
    let newTableDt = null;
    let editedTableDt = null;

    //Maker-Checker Parameters
    let isMakerCheckerEnabled = false;

    //When handling a standard table
    let standardTableDt = null;


    //Global options
    //TODO: Might there be need to modify this object?
    let options = {
        //The current resource to serve AJAX requests
        url: window.location.pathname,
        module: null,

        //Maker-Checker Tables
        $activeTable: $('.table-active', e),
        $newTable: $('.table-new', e),
        $editedTable: $('.table-edited', e),
        $inactiveTable: $('.table-inactive', e),
        $deactivatedTable: $('.table-deactivated', e),

        //Maker-Checker Actionable Views
        $editView: $('.edit-view'),
        $deactivateView: $('.deactivate-view'),
        $viewChanges: $('.view-changes'),
        $viewReasons: $('.view-reasons')
    };

    //CSRF Parameters
    const $csrfToken = $(`meta[name='_csrf']`).attr('content');
    const $csrfHeader = $(`meta[name='_csrf_header']`).attr('content');

    const $deactivationForm = $('form', options.$deactivateView); //Displayed when deactivating a record
    const $editChangesForm = $('form', options.$viewChanges);//Displayed when approving/declining edit changes
    const $deactivationRnsForm = $('form', options.$viewReasons);//Displayed when approving/declining deactivation requests

    //Initialize CSRF parameters for our AJAX requests
    const $$ = $(document);
    $$.ajaxSend((e, xhr, options) => {
        xhr.setRequestHeader($csrfToken, $csrfHeader);
        xhr.setRequestHeader('Accept', 'application/json');
    });

    //Sweet alerts
    const alert = {
        error: (o, callback) => {
            swal({title: '', text: o, type: 'warning'}, function () {
                if (typeof callback === 'function') callback();
            });
        },
        success: function (o, callback) {
            swal({title: '', text: o, type: 'success'}, function () {
                if (typeof callback === 'function') callback();
            });
        },
        confirm: function (o, type, callback) {
            swal({title: '', text: o, type: type, showCancelButton: true, closeOnConfirm: true}, function () {
                if (typeof callback === 'function') callback();
            });
        }
    };

    //Notify user of the progress of background tasks
    //@$element - current jquery object
    //@state - whether to showcase progress or hide the animation view
    const toggleLoading = ($element, state) => {
        // If the state is not set, end here
        if (typeof (state) === 'undefined') return false;

        // If we are showing the loading progress item
        if (state === "show") {
            $($element).block({
                message: '<span class="semibold"> Loading...</span>',
                overlayCSS: {backgroundColor: '#fff', opacity: 0.8, cursor: 'wait'},
                css: {border: 0, padding: '10px 15px', color: '#fff', width: 'auto', backgroundColor: '#333'}
            });
        }
        // If we are hiding the loading progress
        else if (state === "hide") {
            $($element).unblock();
        }
    };

    //When to clean-up forms
    let houseKeep = function (ui) {
        $('form', ui)[0].reset();
    };

    //User Module Permissions
    const modulePermissions = (module) => {
        console.log('module', module);
        if (module) {
            const url = `/permissions/${module}`;
            const request = {
                action: 'module-permissions',
                module: module
            };
            $.post(url, request);
        }
    };

    //DataTable init plugin
    $.fn.ajaxTable = function (fn) {
        //Define default options
        let defaults = {
            sAjaxSource: window.location.pathname,
            table: $('table'),
            sorting: [[0, 'desc']]
        };
        //Merge default and user params
        fn = $.extend(
            // {}, //Empty object to prevent overriding of default options
            defaults, //Default options for the plugin
            fn //User defined options
        );
        let oTable = {};
        let sortOrder = (typeof defaults.sorting === undefined) ?
            [[0, 'desc']] : defaults.sorting;

        //Initialize a dataTable object
        oTable = {
            'lengthMenu': [ 10, 25, 50, 100, 500, 1000 ],
            'bProcessing': true,
            'sAjaxSource': fn.sAjaxSource,
            'aaSorting': sortOrder,
            'bServerSide': true,
            'fnRowCallback': function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                if (typeof fn.fnRowCallback === 'function')
                    fn.fnRowCallback(nRow, aData);
            },
            'fnServerParams': function (aoData) {
                if (typeof fn.fnServerParams === 'function')
                    fn.fnServerParams(aoData);
            },
            "fnDrawCallback": function (oSettings) {
                // Remove the portlet loader
                toggleLoading(e, "hide");

                // Run the callback
                if (typeof (fn.fnDrawCallback) !== 'undefined')
                    return fn.fnDrawCallback(oSettings);
                return true;
            },
            "fnPreDrawCallback": function (oSettings) {
                // Show the portlet loader
                toggleLoading(e, "show");

                // Run the callback
                if (typeof (fn.fnPreDrawCallback) !== 'undefined')
                    return fn.fnPreDrawCallback(oSettings);
                return true;
            }
        };
        //Allow chaining
        return fn.table.dataTable(oTable);
    };

    //HTTP Ajax Helper
    const http = {
        jsonRequest: (el, input) => {
            if (undefined === typeof el) el = e;

            const $this =
                $.ajax({
                    type: 'post',
                    data: input,
                    dataType: 'json',
                    beforeSend: () => {
                        //Notify user of the progress of execution
                        toggleLoading(el, 'show');
                    },
                    complete: () => {
                        //Notify user of end of execution
                        toggleLoading(el, 'hide');
                    },
                    fail: () => {
                    },
                    statusCode: {
                        //404: http._404, 500: http._500, 403: http._403
                    }
                });

            //What about this sugary syntax?
            // const $$this = $.post( options.url, input );
            return $this;
        },
        endRequest: function (input, view) {
            //Pre-check to ensure an element is currently being handled
            if ('undefined' === typeof view) {
                view = options.$editView;
            }

            //Hide the modal
            view.hide();

            //Send the request to the backend
            http.jsonRequest(e, input).done((o) => {
                //When all went well
                if (o.status === '00') {
                    alert.success(o.message, () => {
                        makerChecker.refreshTables(); //Refresh tables
                        houseKeep(view); //Clean up the form
                        view.modal('hide'); //Destroy the modal from the 'dom'
                    });
                }
                //When the request ended with an error
                else {
                    alert.error(o.message, function () {
                        view.show(); //Display the current modal
                    });
                }
            });
        },
        simpleReq: (input) => http.jsonRequest(e, input)
    };

    //TODO: What does this function actually perform?
    const dataTable = function (fn) {
        function dt(o) {
            let oTable = {},
                table,
                url,
                sortOrder;
            table = (typeof o.table === 'undefined') ? $("table") : o.table;
            url = (typeof o.sAjaxSource === 'undefined') ? window.location.pathname : o.sAjaxSource;
            sortOrder = (typeof o.sortOptions === 'undefined') ? [[0, "desc"]] : o.sortOptions;
            //When refreshing a table
            this.fnDraw = function () {
                oTable.fnDraw();
            };

            //When destroying this table
            this.fnDestroy = function () {
                oTable.fnDestroy();
            };

            oTable = {
                "bProcessing": true,
                "bServerSide": true,
                "sAjaxSource": url,
                "aaSorting": sortOrder,
                "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
                    if (typeof o.fnRowCallback === "function") o.fnRowCallback(nRow, aData);
                },
                "fnServerParams": function (aoData) {
                    if (typeof o.fnServerParams === "function") o.fnServerParams(aoData);
                },
                "fnDrawCallback": function (oSettings) {
                    // Remove the portlet loader
                    toggleLoading(e, "hide");

                    // Run the callback
                    if (typeof (o.fnDrawCallback) !== 'undefined')
                        return o.fnDrawCallback(oSettings);
                    return true;
                },
                "fnPreDrawCallback": function (oSettings) {
                    // Show the portlet loader
                    toggleLoading(e, "show");

                    // Run the callback
                    if (typeof (o.fnPreDrawCallback) !== 'undefined')
                        return o.fnPreDrawCallback(oSettings);
                    return true;
                }
            };

            oTable = table.dataTable(oTable);
        }

        return new dt(fn);
    };


    //When handling a standard table
    const standardTable = {

        fnStandardActions: (recordId) => {
           return  '<div class="xs-mb-0">\n' +
            '        <div class="btn-toolbar">\n' +
            '            <div class="btn-group btn-space">\n' +
            '                <button type="button" class="btn btn-info btn-sm edit" data-toggle="tooltip" data-placement="top" title="Edit" data-index=' +recordId+ '><i class="icon s7-pen"></i></button>\n' +
            '                <button type="button" class="btn btn-danger btn-sm delete" data-toggle="tooltip" data-placement="top" title="Delete" data-index=' + recordId + ' ><i class="icon s7-close-circle"></i></button>\n' +
            '            </div>\n' +
            '        </div>\n' +
            '    </div>'
        },

        //Fetch a record from the server side, and pass the result to
        //a callback that handles the edit event
        fnEdit: (recordId, callback) => {
            const request = {action: 'fetch-record', index: recordId};
            //Maker sure the submit button is always visible
            const ui = options.$editView;
            $('button[type="submit"]', ui).toggleClass('d-none', false);
            http
                .jsonRequest(e, request)
                .done((o) => {
                    if ('00' === o.status) {
                        if ('function' === typeof callback) {
                            callback(o);
                        }
                    } else alert.error(o.message);
                });
        },

        deleteRecord: (recordId) => {
            swal({
                title: '',
                text: "Are you sure you want to delete this record?",
                type: 'warning',
                showCancelButton: true,
                closeOnConfirm: false,
                showLoaderOnConfirm: true
            }, function (isConfirm) {
                if (isConfirm) {
                    //Package the server-side request
                    const request = {action: 'delete', index: recordId};

                    setTimeout(() => {
                        $.post(window.location.pathname, request)
                            .done((o) => {
                                if ('00' === o.status) {
                                    alert.success(o.message);
                                    standardTable.refreshTable();
                                } else alert.success(o.message);
                            });
                    }, 200);
                }
            });
        },

        fnInitTable: (fnCallback) => {
            const $table = $('.table-records');
            const cols = $table.find('thead > tr > th').length;
            const idIndex = cols - 1;

            return $table.ajaxTable({
                table: $table,
                fnRowCallback: (nRow, aData) => {
                    //Render Template in the actions column
                    const recordId = aData[idIndex];
                    const $actionsTemplate = standardTable.fnStandardActions(recordId);
                    $(nRow).children('td:eq( -1 )').html($actionsTemplate);

                    //Edit events
                    $('.edit', nRow).click(function () {
                        standardTable.fnEdit(recordId, fnCallback);
                    });

                    //Delete event
                    $('.delete', nRow).click(function () {
                        standardTable.deleteRecord(recordId);
                    });
                }
            });
        },

        init: (callback) => (standardTableDt = standardTable.fnInitTable(callback)),
        refreshTable: () => standardTableDt.fnDraw()
    };


    //Maker-Checker handler
    const makerChecker = {

        options: {
            refreshFn: null,
            sorting: undefined,
            referenceColumn: undefined
        },

        fnRowActions: function (o, ident, state) {
            let actions = '';

            if(state === 0){
                actions += '<div class="xs-mb-0">\n' +
                    '        <div class="btn-toolbar">\n' +
                    '            <div class="btn-group btn-space">\n' +
                    '                <button type="button" class="btn btn-success btn-sm approve-new" data-toggle="tooltip" data-placement="top" title="Approve" data-index=' + o + ' data-name=' + ident + '><i class="icon s7-check"></i></button>\n' +
                    '                <button type="button" class="btn btn-primary btn-sm decline-new" data-toggle="tooltip" data-placement="top" title="Decline" data-index=' + o + ' data-name=' + ident + '><i class="icon s7-trash"></i></button>\n' +
                    '            </div>\n' +
                    '        </div>\n' +
                    '    </div>';

            }else if(state === 1){
                actions += '<div class="xs-mb-0">\n' +
                    '        <div class="btn-toolbar">\n' +
                    '            <div class="btn-group btn-space">\n' +
                    '                <button type="button" class="btn btn-info btn-sm edit" data-toggle="tooltip" data-placement="top" title="Edit" data-index=' + o + ' data-name=' + ident + '><i class="icon s7-pen"></i></button>\n' +
                    '                <button type="button" class="btn btn-danger btn-sm deactivate" data-toggle="tooltip" data-placement="top" title="Deactivate" data-index=' + o + ' data-name=' + ident + '><i class="icon s7-close-circle"></i></button>\n' +
                    '            </div>\n' +
                    '        </div>\n' +
                    '    </div>';
            } else if (state === 2) {
                actions += '<div class="xs-mb-0">\n' +
                    '        <div class="btn-toolbar">\n' +
                    '            <div class="btn-group btn-space">\n' +
                    '                <button type="button" class="btn btn-info btn-sm vedit" data-toggle="tooltip" data-placement="top" title="View Changes" data-index=' + o + ' data-name=' + ident + '><i class="icon s7-look"></i></button>\n' +
                    '            </div>\n' +
                    '        </div>\n' +
                    '    </div>';

            }else if (state === 3) {
                actions += '<div class="xs-mb-0">\n' +
                    '        <div class="btn-toolbar">\n' +
                    '            <div class="btn-group btn-space">\n' +
                    '                <button type="button" class="btn btn-info btn-sm vdeactivation" data-toggle="tooltip" data-placement="top" title="View Changes" data-index=' + o + ' data-name=' + ident + '><i class="icon s7-look"></i></button>\n' +
                    '            </div>\n' +
                    '        </div>\n' +
                    '    </div>';

            } else if (state === 4) {
                actions += '<div class="xs-mb-0">\n' +
                    '        <div class="btn-toolbar">\n' +
                    '            <div class="btn-group btn-space">\n' +
                    '                <button type="button" class="btn btn-success btn-sm activate" data-toggle="tooltip" data-placement="top" title="Activate" data-index=' + o + ' data-name=' + ident + '><i class="icon s7-check"></i></button>\n' +
                    '                <button type="button" class="btn btn-primary btn-sm delete" data-toggle="tooltip" data-placement="top" title="Delete" data-index=' + o + ' data-name=' + ident + '><i class="icon s7-trash"></i></button>\n' +
                    '            </div>\n' +
                    '        </div>\n' +
                    '    </div>';

            }
            return actions;
        },

        flagRecords: function (ident, recordId, status, type) {
            swal({
                title: '',
                text: $('.lang-pack > ._ajax_question_' + status, e).html(),
                type: (typeof type === 'undefined') ? 'info' : type,
                showCancelButton: true,
                closeOnConfirm: false,
                showLoaderOnConfirm: true
            }, function (isConfirm) {
                if (isConfirm) {
                    //Package the server-side request
                    const request = {action: status, index: recordId};

                    setTimeout(() => {
                        $.ajax({
                            type: 'post', data: request, dataType: 'json',
                            success: function (o) {

                                //Handle a backend error returning 01 when status should be 00
                                let status = o.status;
                                let message = o.message;

                                if (message.indexOf('success') !== -1) status = '00';

                                if (status === '00') {
                                    alert.success(message);
                                    makerChecker.refreshTables();
                                } else alert.error(message);
                            },
                            statusCode: {
                                // 404: http._404, 500: _http._500, 403:_http._403
                            }
                        });
                    }, 200);
                }
            });
        },

        //Fetch a record from the server side, and pass the result to
        //a callback that handles the edit event
        fnEdit: (recordId, callback) => {
            const request = {action: 'fetch-record', index: recordId};
            http
                .jsonRequest(e, request)
                .done((o) => {
                    if ('00' === o.status) {
                        if ('function' === typeof callback) {
                            callback(o);
                        }
                    } else alert.error(o.message);
                });
        },

        //Display edit changes to enable a user make a decision
        //on whether to approve or decline these changes
        fnViewChanges: (recordId) => {
            const $this = options.$viewChanges;
            const request = {action: 'vedit', index: recordId};

            //Submit a request to the server-side
            http
                .jsonRequest(e, request)
                .done(function (o) {
                    let changes = '<table class="table table-bordered table-condensed"><tr><th>Field</th><th>Old Value</th><th>New Value</th></tr>';
                    let rows = [];
                    $.each(o.data, function (i, val) {
                        rows.push(`<tr><td> ${val.field} </td><td> ${val.oldvalue}</td><td> ${val.newvalue}</td></tr>`);
                    });
                    changes += rows.join('') + '</table>';
                    $this.find('[name="index"]').val(recordId).end()
                        .find('.modal-body').html(changes).end();
                    $this.modal('show');
                });
        },

        //Pass the recordId of the current record and display the
        //deactivation view
        fnDeactivate: (recordId, reference) => {
            const $view = options.$deactivateView;
            $view.find('[name="index"]').val(recordId);
            $view.find('.identity').html(reference);
            $view.modal('show');
        },

        //Display deactivation reasons to enable a user make a decision
        //on whether to approve or decline these changes
        fnViewReasons: (recordId) => {
            const $this = options.$viewReasons;
            const request = {action: 'vdeactivation', index: recordId};

            http
                .jsonRequest(e, request)
                .done((o) => {
                    if ('00' === o.status) {
                        $('.user', $this).html(o.editor);
                        $('.reason', $this).html(o.reason);
                        $('.description', $this).html(o.description);
                        $('input[name="index"]', $this).val(recordId);
                        $this.modal('show');
                    }
                });
        },

        fnActiveTable: (fnCallback) => {
            const $table = options.$activeTable;
            const cols = $table.find('thead > tr > th').length;
            const idIndex = cols - 1;
            const tableState = 1;

            return $table.ajaxTable({
                table: $table,
                sorting: makerChecker.options.sorting,
                fnServerParams: (aoData) => {
                    aoData.push({name: 'fetch-table', value: '1'});
                },
                fnRowCallback: (nRow, aData) => {
                    //Append action buttons
                    const recordId = aData[idIndex];
                    const recordReference = aData[0];
                    $(nRow).children('td:eq( -1 )').html(
                        makerChecker.fnRowActions(recordId, recordReference, tableState)
                    );

                    //Edit events
                    $('.edit', nRow).click(function () {
                        makerChecker.fnEdit($(this).data('index'), fnCallback);
                    });

                    //Deactivate events
                    $('.deactivate', nRow).click(function () {
                        makerChecker.fnDeactivate($(this).data('index'), $(this).data('name'));
                    });
                }
            });
        },

        //Initialize the new table
        fnNewTable: () => {
            const $table = options.$newTable;
            const cols = $table.find('thead > tr > th').length;
            const idIndex = cols - 1;
            const tableState = 0;

            return $table.ajaxTable({
                table: $table,
                sorting: makerChecker.options.sorting,
                fnServerParams: (aoData) => {
                    aoData.push({'name': 'fetch-table', 'value': '0'});
                },
                fnRowCallback: (nRow, aData) => {
                    //Append action buttons
                    const recordId = aData[idIndex];
                    $(nRow).children(`td:eq( -1 )`).html(
                        makerChecker.fnRowActions(recordId, aData[0], tableState)
                    );

                    //Handle events
                    $('.approve-new', nRow).click(function () {
                        makerChecker.flagRecords($(this).data('name'), $(this).data('index'), "approve-new");
                    });
                    $('.decline-new', nRow).click(function () {
                        makerChecker.flagRecords($(this).data('name'), $(this).data('index'), "decline-new", "warning");
                    });
                }
            });
        },

        //Initialize edited table
        fnEditedTable: () => {
            const $table = options.$editedTable;
            const cols = $table.find('thead > tr > th').length;
            const idIndex = cols - 1;
            const tableState = 2;

            return $table.ajaxTable({
                table: $table,
                sorting: makerChecker.options.sorting,
                fnServerParams: (aoData) => {
                    aoData.push({'name': 'fetch-table', 'value': '2'});
                },
                fnRowCallback: (nRow, aData) => {
                    //Append action buttons
                    const recordId = aData[idIndex];
                    $(nRow).children('td:eq( -1 )').html(
                        makerChecker.fnRowActions(recordId, undefined, tableState)
                    );

                    //Handle events
                    $('.vedit', nRow).click(function () {
                        makerChecker.fnViewChanges(recordId);
                    });
                }
            });
        },

        //Initialize deactivated table
        fnDeactivatedTable: () => {
            const $table = options.$deactivatedTable;
            const cols = $table.find('thead > tr > th').length;
            const idIndex = cols - 1;
            const tableState = 3;

            return $table.ajaxTable({
                table: $table,
                sorting: makerChecker.options.sorting,
                fnServerParams: (aoData) => {
                    aoData.push({name: 'fetch-table', value: tableState});
                },
                fnRowCallback: (nRow, aData) => {
                    //Append action buttons
                    const recordId = aData[idIndex];
                    $(nRow).children('td:eq( -1 )').html(
                        makerChecker.fnRowActions(recordId, undefined, tableState)
                    );

                    //Handle events
                    $('.vdeactivation', nRow).click(function () {
                        makerChecker.fnViewReasons(recordId);
                    });
                }
            });
        },

        //Initialize inactive table
        fnInactiveTable: () => {
            const $table = options.$inactiveTable;
            const cols = $table.find('thead > tr > th').length;
            const idIndex = cols - 1;
            const tableState = 4;

            return $table.ajaxTable({
                table: $table,
                sorting: makerChecker.options.sorting,
                "fnServerParams": function (aoData) {
                    aoData.push({name: 'fetch-table', value: tableState});
                },
                "fnRowCallback": function (nRow, aData) {
                    //Append action buttons
                    const recordId = aData[idIndex];
                    $(nRow).children('td:eq(' + (cols - 1) + ')').html(
                        makerChecker.fnRowActions(recordId, undefined, tableState)
                    );

                    //Handle events
                    $('.delete', nRow).click(function () {
                        makerChecker.flagRecords($(this).data('name'), recordId, "delete", "warning");
                    });
                    $('.activate', nRow).click(function () {
                        makerChecker.flagRecords($(this).data('name'), recordId, "activate");
                    });
                }
            });
        },

        //Initialize all maker-checker tables
        initTables: (callback, module) => {

            //Fetch module permissions
            modulePermissions(module);

            activeTableDt = makerChecker.fnActiveTable(callback);
            newTableDt = makerChecker.fnNewTable();
            editedTableDt = makerChecker.fnEditedTable();
            deactivatedTableDt = makerChecker.fnDeactivatedTable();
            inactiveTableDt = makerChecker.fnInactiveTable();
        },

        //Update all maker-checker tables' content
        refreshMkTables: () => {
            //Legacy Call to refresh tables
            activeTableDt.fnDraw();
            newTableDt.fnDraw();
            editedTableDt.fnDraw();
            deactivatedTableDt.fnDraw();
            inactiveTableDt.fnDraw();
        },

        //Allow overriding of the refresh function
        refreshTables: () => {
            if ('function' === typeof makerChecker.options.refreshFn) {
                makerChecker.options.refreshFn();
            } else makerChecker.refreshMkTables();
        }
    };

    //When approving/declining edit changes
    $editChangesForm.on("submit", function (event) {
        event.preventDefault();
        const $form = $(this);
        const action = $form.find('[type=submit]:focus').data('action');

        $form.find(`[name='action']`).val(action);
        http.endRequest($(this).serializeArray(), options.$viewChanges);
        return false;
    });

    //When deactivating a record
    $deactivationForm.formValidation().on('success.fv.form', function (e) {
        e.preventDefault();
        const $view = options.$deactivateView;
        $view.hide();

        const request = http.jsonRequest($view, $(e.target).serializeArray());
        request.done(function (o) {
            if (o.status === "00") {
                alert.success(o.message);
                makerChecker.refreshTables();
                houseKeep($view);

                //Default value
                $view.find(`[name='action']`).val('deactivate');
                $view.modal('hide');
            } else alert.error(o.message, function () {
                $view.show();
            });
        });
        return false;
    });

    //When approving/declining a deactivation request
    $deactivationRnsForm.on('submit', function (event) {
        event.preventDefault();
        const $view = options.$viewReasons;

        const action = $view.find('[type=submit]:focus').data('action');
        $view.find(`[name='action']`).val(action);
        http.endRequest($(event.target).serializeArray(), $view);
        houseKeep($view);
        return false;
    });

    //Custom validation plugins
    //1. Mobile Number Validation
    const mobileValidator = {
        init: (validator, $field, options) => {

            //Initialize field with intlTelInput options
            $field.intlTelInput({
                geoIpLookup: function (callback) {
                    $.get('https://ipinfo.io', function () {
                    }, "jsonp").always(function (resp) {
                        const countryCode = (resp && resp.country) ? resp.country : "";
                        callback(countryCode);
                    });
                },
                utilsScript: '/theme/lib/intelutil/js/utils.js',
                autoPlaceholder: true,
                initialCountry: "auto",
                separateDialCode: true
            });

            //Revalidate field when country is changed
            const $form = validator.getForm();
            const fieldname = $field.attr('data-fv-field');
            $form.on('click.country.intphonenumber', '.country-list', () => {
                $form.formValidation('revalidateField', fieldname);
            });
        },
        destroy: (validator, $field, options) => {
            $field.intlTelInput('destroy');
            //Turn off the event
            validator.getForm().off('click.country.intphonenumber');
        },
        validate: (validator, $field, options) => {
            return $field.val() === '' || $field.intlTelInput('isValidNumber');
        }
    };

    //2. Password validator
    const passwordValidator = {
        validate: (validator, $field, options) => {
            const value = $field.val();
            if ('' === value) return true;

            //Check the password strength
            if (value.length < 7) {
                return {valid: false, message: 'Password should be at least 7 characters long'};
            }

            //If password contains any uppercase letter
            if (value.toLowerCase() === value) {
                return {valid: false, message: 'Password must contain at least one uppercase character'};
            }

            //If password contains any lowercase letter
            if (value.toUpperCase() === value) {
                return {valid: false, message: 'Password must contain at least one lowercase character'};
            }

            //If password contains any digit
            if (value.search(/[0-9]/) < 0) {
                return {valid: false, message: 'Password must contain at least one digit'};
            }

            return true;
        }
    };

    let reportDateRange = function (e, fnDraw, ranges) {
        let yr = new Date().getFullYear();
        let startDate = moment(yr + "-01-01");
        let endDate = moment(yr + "-12-31");
        if (typeof (ranges) === 'undefined') {
            ranges = {
                'Today': [moment(), moment()],
                'Last 7 Days': [moment().subtract(6, 'days'), new Date()],
                'Last 30 Days': [moment().subtract(29, 'days'), new Date()],
                'This Month': [moment().startOf('month'), moment().endOf('month')],
                'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
                'Current Year': [moment(yr + "-01-01"), moment(yr + "-12-31")],
                'Previous Year': [moment(yr + "-01-01").subtract(1, 'year'), moment(yr + "-12-31").subtract(1, 'year')]
            };
        }

        $('.date-range', e).daterangepicker({
            ranges: ranges,
            opens: 'left',
            locale: {
                format: 'YYYY-MM-DD',
                startDate: startDate.format('YYYY-MM-DD'),
                endDate: endDate.format('YYYY-MM-DD')
            }
        }, function (start, end) {
            $('.date-range span', e)
                .html(start.format('MMM D, YYYY') + ' - ' + end.format('MMM D, YYYY'))
                .data('start-date', start.format('YYYY-MM-DD'))
                .data('end-date', end.format('YYYY-MM-DD'));
            fnDraw(start, end);
        });

        $('.date-range span', e)
            .html(startDate.format('MMM D, YYYY') + ' - ' + endDate.format('MMM D, YYYY'))
            .data('start-date', startDate.format('YYYY-MM-DD'))
            .data('end-date', endDate.format('YYYY-MM-DD'));
    };

    //Expose methods to the public
    return {
        makerchecker: makerChecker,
        dataTable: dataTable,
        http: http,
        submitForm: http.endRequest,
        alert: alert,
        houseKeep: houseKeep,
        options: options,
        mobileValidator: mobileValidator,
        passwordValidator: passwordValidator,
        initReportDateRange: reportDateRange,
        standardTable: standardTable,
        toggleLoading: toggleLoading,
    };

}(utils || {}));

