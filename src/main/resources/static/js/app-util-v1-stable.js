var utils = (function ( p ){
    "use strict";

    var e = $('.content-body'), _activeTable, _inactiveTable, _deactivatedTable, _newTable, _editedTable;
    var _options = {
            _url : window.location.pathname,
            _tactive :$('.table-active', e),
            _tnew : $('.table-new', e),
            _tedited : $('.table-edited', e),
            _tinactive :$('.table-inactive', e),
            _tdeactivated : $('.table-deactivated', e),
            _token : $("meta[name='_csrf']").attr("content"),
            _header : $("meta[name='_csrf_header']").attr("content"),
            _editview :$('.edit-view'),
            _deactivateView : $('.deactivate-view'),
            _viewChanges: $('.view-changes'),
            _viewReasons: $('.view-reasons')
        },

        // Called to set/unset the loading bg in an element
//     _toggleLoading = function(portlet, state) {
//         // If the state is not set, end here
//         if ( typeof(state) === 'undefined' ) return false;
//
//         // If we are showing the loading progress item
//         if ( state === "show" ){
//             var loader = '<div class="loader-wrapper active"><div class="loader-layer loader-blue-only"><div class="loader-circle-left"><div class="circle"></div></div>';
//             loader += '<div class="loader-circle-gap"></div><div class="loader-circle-right"><div class="circle"></div></div></div></div>';
//             $( portlet ).block({
//                 message : loader,
//                 overlayCSS: {
//                     backgroundColor: '#fff',
//                     opacity: 0.8,
//                     cursor: 'wait',
//                     'box-shadow': '0 0 0 1px #ddd'
//                 },
//                 css: {
//                     border: 0,
//                     padding: 0,
//                     backgroundColor: 'none'
//                 }
//             });
//         }
//         // If we are hiding the loading progress
//         else if ( state === "hide" ) {
// //            var pd = portlet.find('.panel-disabled');
// //            pd.remove();
//             $( portlet ).unblock();
//         }
//     },

        // Called to set/unset the loading bg in an element
        _toggleLoading = function(portlet, state) {
            // If the state is not set, end here
            if ( typeof(state) === 'undefined' ) return false;

            // If we are showing the loading progress item
            if ( state === "show" )
                portlet.append('<div class="card bg-dark panel-disabled"><div class="loader-1"></div></div>');

            // If we are hiding the loading progress
            else if ( state === "hide" ) {
                var pd = portlet.find('.panel-disabled');
                pd.remove();
            }
        },

        // Use this to set the portlet loading background on a portlet/panel
        _panelLoading = function(el, state) {
            var ui;
            if( "undefined" === el || "" === el || null === el) ui = $(".panel");
            else ui = el;
            _toggleLoading(ui, state);
        },

        // Called to toggle the readonly status of all elements within a given form
        _toggleReadonly = function (ui, state) {
            $('input, textarea', ui).prop('readonly', state);
            $('input[type="checkbox"]', ui).prop('disabled', state);
        },

        //Clean form inputs
        _houseKeep = function( ui ){
            // Clear the previous values
            $(":checkbox, :radio", ui).prop('checked', false);
            $('textarea', ui).val('');
            $('select', ui).prop('selectedIndex', 1);
            $(':input', ui).not(':button, :submit, :checkbox, :radio').val('');

            //Reset form validation errors
            $('form *', ui)
                .filter('.form-group').each(function( e ){
                if( $(this).hasClass("has-error") || $(this).hasClass("has-success") ){
                    $(this).find("[data-fv-result]").value = "NOT_VALIDATED";
                    $(this).find(".help-block").attr("style", "display: none");

                    $(this).toggleClass("has-error", false);
                    $(this).toggleClass("has-success", false);
                }
            }).end()
                .filter(":submit").each( function(e){
                $(this).prop('disabled', false);
            });

            // Ensure that one can edit the content
            _toggleReadonly(ui, false);
            ui.formValidation('resetForm', true);
        },

        //Sweet alerts
        _alert = {
            error: function(o, callback){
                swal({ title:"", text: o, type:'warning'}, function(){
                    if( typeof callback === 'function') callback();
                });

                // swal( { title:"", text: o, type:'warning'} )
                // .then( function(){
                //     if( typeof callback === 'function') callback();
                // });
            },
            success: function(o, callback){
                swal({ title:"", text:o, type:'success'}, function(){
                    if( typeof callback === 'function') callback();
                });
            },
            confirm:function(o, type, callback){
                swal({'title':'', text:o, type: type, showCancelButton:true, closeOnConfirm:true }, function(){
                    if( typeof callback === 'function' ) callback();
                });
            }
        },
        _dataTable = function( fn ){
            // fn.table.ajaxTable( fn );
            function dt( o ){
                var oTable ={}, table, url;
                table = ( typeof o.table === 'undefined') ? $("table") : o.table;
                url = ( typeof o.sAjaxSource === 'undefined')? window.location.pathname : o.sAjaxSource;
                //When refreshing a table
                this.fnDraw= function(){
                    oTable.fnDraw();
                };

                //When destroying this table
                this.fnDestroy = function(){
                    oTable.fnDestroy();
                };

                oTable = {
                    "bProcessing": true,
                    "bServerSide":true,
                    "sAjaxSource" : url,
                    "aaSorting":[[0, "desc"]],
                    "fnRowCallback": function(nRow, aData, iDisplayIndex, iDisplayIndexFull){
                        if( typeof o.fnRowCallback === "function") o.fnRowCallback(nRow, aData);
                    },
                    "fnServerParams":function( aoData){
                        if( typeof o.fnServerParams === "function") o.fnServerParams( aoData );
                    },
                    "fnDrawCallback": function (oSettings) {
                        // Remove the portlet loader
                        _toggleLoading(e, "hide");

                        // Run the callback
                        if (typeof (o.fnDrawCallback) !== 'undefined')
                            return o.fnDrawCallback(oSettings);
                        return true;
                    },
                    "fnPreDrawCallback": function (oSettings) {
                        // Show the portlet loader
                        _toggleLoading(e, "show");

                        // Run the callback
                        if (typeof (o.fnPreDrawCallback) !== 'undefined')
                            return o.fnPreDrawCallback(oSettings);
                        return true;
                    }
                };

                oTable = table.dataTable( oTable );
            }
            return new dt( fn );
        };

    //JSON form serializer plugin
    $.fn.serializeObject = function ()
    {
        var o = {};
        var a = p.serializeArray();
        $.each(a, function () {
            if (o[p.name]) {
                if (!o[p.name].push) {
                    o[p.name] = [o[p.name]];
                }
                o[p.name].push(p.value || '');
            } else {
                o[p.name] = p.value || '';
            }
        });
        return o;
    };

    //DataTable init plugin
    $.fn.ajaxTable = function( fn ){
        //Define default options
        var defaults ={
            sAjaxSource : window.location.pathname,
            table: $('table')
        };
        //Merge default and user params
        fn = $.extend(
            // {}, //Empty object to prevent overriding of default options
            defaults, //Default options for the plugin
            fn //User defined options
        );
        let oTable ={};
        let sortIndex = fn.table.find('th:contains("Created On")').index();
        if( sortIndex === -1 ) sortIndex = 0;

        //Initialize a dataTable object
        oTable = {
            'bProcessing': true,
            'sAjaxSource': fn.sAjaxSource,
            'aaSorting':[[sortIndex, 'desc']],
            'bServerSide':true,
            'fnRowCallback': function(nRow, aData, iDisplayIndex, iDisplayIndexFull){
                if( typeof fn.fnRowCallback === 'function')
                    fn.fnRowCallback(nRow, aData);
            },
            'fnServerParams':function( aoData){
                if( typeof fn.fnServerParams === 'function')
                    fn.fnServerParams( aoData );
            },
            "fnDrawCallback": function (oSettings) {
                // Remove the portlet loader
                _toggleLoading(e, "hide");

                // Run the callback
                if (typeof ( fn.fnDrawCallback) !== 'undefined')
                    return fn.fnDrawCallback(oSettings);
                return true;
            },
            "fnPreDrawCallback": function (oSettings) {
                // Show the portlet loader
                _toggleLoading(e, "show");

                // Run the callback
                if (typeof ( fn.fnPreDrawCallback) !== 'undefined')
                    return fn.fnPreDrawCallback(oSettings);
                return true;
            }
        };
        //Allow chaining
        return fn.table.dataTable( oTable );
    };

    //Mobile phone validator: include formValidation dependecies
    let _mobileValidator = {
            /**
             * @param {FormValidation.Base} validator The validator plugin instance
             * @param {jQuery} $field The jQuery object represents the field element
             * @param {Object} options The validator options
             * @returns {Boolean}
             */
            init: function(validator, $field, options){
                //Initialize field with intlTelInput options
                $field.intlTelInput({
                    geoIpLookup: function (callback) {
                        $.get('https://ipinfo.io', function () {}, "jsonp").always(function (resp) {
                            var countryCode = (resp && resp.country ) ? resp.country : "";
                            callback( countryCode );
                        });
                    },
                    utilsScript: '/plugins/intelutil/js/utils.js',
                    autoPlaceholder: true,
                    initialCountry: "auto",
                    separateDialCode: true
                });
                //Revalidate field when country is changed
                var $form = validator.getForm();
                var fieldName = $field.attr('data-fv-field');
                $form.on('click.country.intphonenumber', '.country-list', function() {
                    $form.formValidation('revalidateField', fieldName);
                });
            },
            destroy: function(validator, $field, options){
                $field.intlTelInput('destroy');
                //Turn off the event
                validator.getForm().off('click.country.intphonenumber');
            },
            validate:function(validator, $field, options){
                return $field.val() === '' || $field.intlTelInput('isValidNumber');
            }
        },
        _passwordValidator ={
            validate : function(validator, $field, options){
                var value = $field.val();
                if('' === value ) return true;
                //Check the password strenght
                if(value.length < 7 ){ return{ valid: false, message: 'Password should be at least 7 characters long'};}
                //If password contains any uppercase letter
                if( value.toLowerCase() === value ){ return{ valid:false, message:'Password must contain at least one uppercase character'};}
                //If password contains any lowercase letter
                if( value.toUpperCase() === value ){ return{ valid:false, message:'Password must contain at least one lowercase character'};}
                //If password contains any digit
                if( value.search(/[0-9]/) < 0){ return { valid:false, message:'Password must contain at least one digit'};}

                return true;
            }
        };


    //Ajax helpers for making requests
    //Global ajax setup
    var _http = {
        _options:{

        },
        _404 : function(){
            _alert.error( $('.lang-pack > ._ajax_error_404', e).html() );
        },
        _500 :function(){
            _alert.error( $('.lang-pack > ._ajax_error_500', e).html() );
        },
        _403: function(){
            _alert.confirm( $('.lang-pack > ._ajax_error_403', e).html(), 'warning', function(){
                window.location.reload();
            });
        },
        entityRequest:function(input, fnCallback){

        },
        jsonRequest:function(e, input){
            if( 'undefined' === typeof e) e = p.e;
            var $this  =
                $.ajax({
                    type : 'post', data: input, dataType: 'json',
                    beforeSend: function(){
                        //Notify user of the progress of execution
                        _toggleLoading(e, 'show');
                    },
                    complete: function(){
                        //Notify user of end of execution
                        _toggleLoading(e, 'hide');
                    },
                    error: function(){

                    },
                    statusCode:{
                        404: _http._404, 500: _http._500, 403:_http._403
                    }
                });
            return $this;
        },
        endRequest : function(input, view){
            if('undefined' === typeof view) view = _options._editview;
            view.hide();
            // view.modal('toggle');
            _http.jsonRequest(e, input).done( function(o){
                if( o.status === "00"){
                    _alert.success( o.message );
                    makerchecker.refreshTables();
                    _houseKeep( view );
                    view.modal('hide');
                    // view.modal('toggle');
                }
                else{
                    _alert.error( o.message, function(){
                        console.log("executing me!");
                        view.show();
                    });
                }
            });
        },
        simpleReq: function( input ){
            return _http.jsonRequest(e, input);
        }
    };

    /*Handling tables */
    p._tableAux = function() {
        $.extend($.fn.dataTable.defaults, {
            autoWidth: false,
            columnDefs: [{
                orderable: false,
                width: '100px',
                targets: [5]
            }],
            dom: '<"datatable-header"fl><"datatable-scroll"t><"datatable-footer"ip>',
            language: {
                search: '<span>Filter:</span> _INPUT_',
                lengthMenu: '<span>Show:</span> _MENU_',
                paginate: {'first': 'First', 'last': 'Last', 'next': '&rarr;', 'previous': '&larr;'}
            },
            drawCallback: function () {
                $(this).find('tbody tr').slice(-3).find('.dropdown, .btn-group').addClass('dropup');
            },
            preDrawCallback: function () {
                $(this).find('tbody tr').slice(-3).find('.dropdown, .btn-group').removeClass('dropup');
            }
        });
        // Add placeholder to the datatable filter option
        $('.dataTables_filter input[type=search]', e).attr('placeholder', 'Type to filter...');

        //Enable Select2 select for the length option
        $('.dataTables_length select', e).select2({
            minimumResultsForSearch: Infinity,
            width: 'auto'
        });
    };

    //Create a dynamic table and append to a tab
    p._createTable = function(label){
        //Create the tab
        $('<li class="closeme"><a href="#random-tab" data-toggle="tab">'+label+'</a></li>').appendTo('#myTab');

        //Create the tab content
        var table = '<table class="table table-striped table-bordered">';
        table += "<thead><tr><th>"+Array.prototype.slice.call( arguments, 1).join("</th><th>")+"</th></tr></thead></table>";
        $('<div class="tab-pane closeme table-tab" id="random-tab" role="tabpanel">' + table + '</div>').appendTo('.tab-content');

        // make the new tab active
        $('#myTab a:last').tab('show');
        //When to destroy this tab
        $(document).on('hide.bs.tab', 'a[data-toggle="tab"]', function (e) {
            var tab = $(e.target);
            if (tab.parent().hasClass("closeme"))
            {
                tab.parent().remove();
                $('div.tab-content div.table-tab').remove();
            }
        });
    };

    var makerchecker = {
        // fnRowActions:function( o, ident, state ){
        //     var actions = "";
        //     if (state === 0) {
        //         actions = '<button type="button" class="btn btn-success btn-xs approve-new" data-index="' + o + '" data-name="' + ident + '">Approve</button>';
        //         actions += '&nbsp;&nbsp;<button type="button" class="btn btn-danger btn-xs decline-new" data-index="' + o + '" data-name="' + ident + '">Decline</button>';
        //     } else if (state === 1) {
        //         actions = '<button type="button" class="btn btn-success btn-xs edit" data-index="' + o + '" > Edit </button>';
        //         actions += '&nbsp;&nbsp;<button type="button" class="btn btn-danger btn-xs deactivate" data-index="' + o + '" data-name="' + ident + '">Deactivate</button>';
        //     } else if (state === 2) {
        //         actions = '<button type="button" class="btn btn-success btn-xs vedit" data-index="' + o + '" >View Changes</button>';
        //     } else if (state === 3) {
        //         actions = '<button type="button" class="btn btn-success btn-xs vdeactivation" data-index="' + o + '" >View Changes</button>';
        //     } else if (state === 4) {
        //         actions = '<button type="button" class="btn  btn-success btn-xs activate" data-index="' + o + '" data-name="' + ident + '">Activate</button>';
        //         actions += '&nbsp;&nbsp;<button type="button" class="btn btn-danger btn-xs delete" data-index="' + o + '" data-name="' + o + '">Delete</button>';
        //     }
        //     return actions;
        // },

        fnRowActions:function( o, ident, state ){
            var actions = '<div class="btn-group"><button type="button" class="btn btn-primary btn-square btn-sm">Options</button>';
            actions += '<button type="button" class="btn btn-danger dropdown-toggle dropdown-toggle-split" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"></button>';
            actions += '<div class="dropdown-menu">';
            if (state === 0) {
                actions += '<a href="javascript:void(0)" class="dropdown-item approve-new" data-index="' + o + '" data-name="' + ident + '"><i class="icon-checkmark4"></i> Approve </a>';
                actions += '<a href="javascript:void(0)" class="dropdown-item decline-new" data-index="' + o + '" data-name="' + ident + '"><i class="icon-cross2"></i> Decline </a>';
            } else if (state === 1) {
                actions += '<a href="javascript:void(0)" class="dropdown-item edit" data-index="' + o + '" data-name="' + ident + '"><i class="icon-pencil"></i> Edit </a>';
                actions += '<li><a href="javascript:void(0)" class="dropdown-item deactivate" data-index="' + o + '" data-name="' + ident + '"><i class="icon-cross2"></i> Deactivate </a>';
            } else if (state === 2) {
                actions += '<a href="javascript:void(0)" class="dropdown-item vedit" data-index="' + o + '" data-name="' + ident + '"><i class="icon-eye"></i> View Changes </a>';
            } else if (state === 3) {
                actions += '<a href="javascript:void(0)" class="dropdown-item vdeactivation" data-index="' + o + '" data-name="' + ident + '"><i class="icon-eye"></i> View Changes </a>';
            } else if (state === 4) {
                actions += '<a href="javascript:void(0)" class="dropdown-item activate" data-index="' + o + '" data-name="' + ident + '"><i class="icon-checkmark4"></i> Activate </a>';
                actions += '<a href="javascript:void(0)" class="dropdown-item delete" data-index="' + o + '" data-name="' + ident + '"><i class="icon-cross2"></i> Delete </a>';
            }

            actions += '</div></div>';
            return actions;
        },
        flagRecords:function(ident, index, status, type){
            swal({
                title:'',
                text: $('.lang-pack > ._ajax_question_'+status, e).html(),
                type: (typeof type === 'undefined') ? 'info':type,
                showCancelButton: true,
                closeOnConfirm: false,
                showLoaderOnConfirm: true
            }, function( isConfirm){
                if( isConfirm )
                    setTimeout( function(){
                        $.ajax({
                            type : 'post', data: { 'action': status, 'index':index}, dataType: 'json',
                            success: function( o ){
                                if( o.status ==='00'){
                                    _alert.success( o.message );
                                    makerchecker.refreshTables();
                                }
                                else _alert.error( o.message );
                            },
                            statusCode:{
                                404: _http._404, 500: _http._500, 403:_http._403
                            }
                        });
                    }, 200);
            });
        },
        fnEdit: function( index, fn ){
            var ui = _options._editview;
            _http
                .jsonRequest(e, {'action':'fetch-record', 'index':index })
                .done( function( o ){
                    if("00" === o.status ){
                        if( 'function' === typeof fn)
                            fn( o );
                    }
                    else _alert.error( o.message );
                });
        },
        fnDeactivate: function( index, label ){
            var ui = _options._deactivateView;
            ui.find('[name="index"]').val( index ).end()
                .find('.identity').html( label ).end()
                .modal('show').end();
        },
        fnViewChanges:function( index ){
            var $this = _options._viewChanges;
            _http
                .jsonRequest(e, { 'action':'vedit', 'index':index})
                .done( function( o ){
                    var changes = '<table class="table table-bordered table-condensed"><tr><th>Field</th><th>Old Value</th><th>New Value</th></tr>';
                    var rows = [];
                    $.each(o.data, function (i, val) {
                        rows.push('<tr><td>' + val.field + '</td><td>' + val.oldvalue + '</td><td>'+ val.newvalue+'</td></tr>');
                    });
                    changes += rows.join('')+'</table>';
                    $this.find('[name="index"]').val( index ).end()
                        .find('.modal-body').html(changes).end()
                        .modal('show');
                });
        },
        fnViewReasons:function( index ){
            var $this = _options._viewReasons;
            _http
                .jsonRequest(e, {'action':'vdeactivation', 'index':index})
                .done( function(o){
                    if("00" === o.status){
                        $('.user', $this).html( o.editor );
                        $('.reason', $this).html( o.reason );
                        $('.description', $this).html( o.description );
                        $('input[name="index"]', $this).val( o.index );
                        $this.modal('show');
                    }
                });
        },
        activeTable: function( fn ){
            let cols = _options._tactive.find('thead > tr > th', e).length;
            return _options._tactive.ajaxTable({
                table: _options._tactive,
                fnServerParams: function( aoData) {
                    aoData.push({'name':'fetch-table', 'value':'1'});
                },
                "fnRowCallback" :function(nRow, aData){
                    //Append action buttons
                    $(nRow).children('td:eq('+(cols-1)+')').html( makerchecker.fnRowActions(aData[cols-1], aData[0], 1 ));
                    //Edit events
                    $('.edit', nRow).click( function(){
                        makerchecker.fnEdit( $(this).data('index'), fn);
                    });
                    //Deactivate events
                    $('.deactivate', nRow).click( function(){
                        makerchecker.fnDeactivate( $(this).data('index'), $(this).data('name'));
                    });
                }
            });
        },
        newTable :function(){
            var cols = _options._tnew.find('thead > tr > th', e).length;
            return _options._tnew.ajaxTable({
                table: _options._tnew,
                "fnServerParams":function( aoData) {
                    aoData.push({'name':'fetch-table', 'value':'0'});
                },
                "fnRowCallback":function(nRow, aData, iDisplayIndex, iDisplayIndexFull){
                    //Append action buttons
                    $(nRow).children('td:eq('+(cols-1)+')').html( makerchecker.fnRowActions(aData[cols-1], aData[0], 0 ));
                    //Handle events
                    $('.approve-new', nRow).click( function(){
                        makerchecker.flagRecords( $(this).data('name'), $(this).data('index'), "approve-new");
                    });
                    $('.decline-new', nRow).click( function(){
                        makerchecker.flagRecords( $(this).data('name'), $(this).data('index'), "decline-new", "warning");
                    });
                }
            });
        },
        editedTable : function(){
            var cols = _options._tedited.find('thead > tr > th', e).length;
            return _options._tedited.ajaxTable({
                table: _options._tedited,
                "fnServerParams":function( aoData) {
                    aoData.push({'name':'fetch-table', 'value':'2'});
                },
                "fnRowCallback":function(nRow, aData){
                    //Append action buttons
                    $(nRow).children('td:eq('+(cols-1)+')').html( makerchecker.fnRowActions(aData[cols-1], aData[0], 2 ));
                    //Handle events
                    $('.vedit', nRow).click( function(){
                        makerchecker.fnViewChanges( $(this).data('index'));
                    });
                }
            });
        },
        deactivatedTable:function(){
            var cols = _options._tdeactivated.find('thead > tr > th', e).length,
                _table = _options._tdeactivated.ajaxTable({
                    table: _options._tdeactivated,
                    "fnServerParams":function( aoData) {
                        aoData.push({'name':'fetch-table', 'value':'3'});
                    },
                    "fnRowCallback":function(nRow, aData){
                        //Append action buttons
                        $(nRow).children('td:eq('+(cols-1)+')').html( makerchecker.fnRowActions(aData[cols-1], aData[0], 3 ));
                        //Handle events
                        $('.vdeactivation', nRow).click( function(){
                            makerchecker.fnViewReasons( $(this).data('index'));
                        });
                    }
                });
            return _table;
        },
        inactiveTable:function(){
            var cols = _options._tinactive.find('thead > tr > th', e).length;
            return _options._tinactive.ajaxTable({
                table: _options._tinactive,
                "fnServerParams":function( aoData) {
                    aoData.push({'name':'fetch-table', 'value':'4'});
                },
                "fnRowCallback":function(nRow, aData){
                    //Append action buttons
                    $(nRow).children('td:eq('+(cols-1)+')').html( makerchecker.fnRowActions(aData[cols-1], aData[0], 4 ));
                    //Handle events
                    $('.delete', nRow).click( function(){
                        makerchecker.flagRecords( $(this).data('name'), $(this).data('index'), "delete", "warning");
                    });
                    $('.activate', nRow).click( function(){
                        makerchecker.flagRecords( $(this).data('name'), $(this).data('index'), "activate");
                    });
                }
            });
        },
        //Initiliaze all maker checker tables
        initTables:function( fn ){
            _activeTable = this.activeTable( fn );
            _editedTable = this.editedTable();
            _newTable = this.newTable();
            _deactivatedTable = this.deactivatedTable();
            _inactiveTable = this.inactiveTable();
            // p._tableAux();
        },
        //Refresh maker checker tables
        refreshMkTables:function(){
            _activeTable.fnDraw();
            _editedTable.fnDraw();
            _newTable.fnDraw();
            _deactivatedTable.fnDraw();
            _inactiveTable.fnDraw();
        },
        //Allow overriding of the refresh function
        refreshTables : function(){
            if( "function" === typeof makerchecker.options.refreshFn) new makerchecker.options.refreshFn();
            else makerchecker.refreshMkTables();
        },
        options : {
            refreshFn :null
        }
    };

    //When approving a deactivation request
    $('form', _options._viewReasons).on("submit", function(event){
        event.preventDefault();
        var action = _options._viewReasons.find('[type=submit]:focus').data('action');
        $('[name="action"]', _options._viewReasons).val( action );
        _http.endRequest( $(event.target).serializeArray(), _options._viewReasons);
        _houseKeep( _options._viewReasons );
        return false;
    });

    //When deactivating a record
    $('form', _options._deactivateView).formValidation().on('success.fv.form', function(e){
        e.preventDefault(); _options._deactivateView.hide();
        var _request = _http.jsonRequest(_options._deactivateView, $(e.target).serializeArray());
        _request.done( function( o){
            if( o.status === "00"){
                _alert.success( o.message );
                makerchecker.refreshTables();
                _houseKeep( _options._deactivateView );

                //Default value
                $("[name='action']", _options._deactivateView).val("deactivate");
                _options._deactivateView.modal('hide');
            }
            else _alert.error( o.message );_options._deactivateView.show();
        });
        return false;
    });

    //When approving changes
    $('form', _options._viewChanges).on("submit", function(event){
        event.preventDefault();
        var action = $(this).find('[type=submit]:focus').data('action');
        $('[name="action"]', $(this)).val( action );
        _http.endRequest($(this).serializeArray(), _options._viewChanges);
        return false;
    });

    // Initialise the typeahead controls when a page completely loads
    var typeaheadRegister = function () {
        $(window).load(function () {
            $('input.typeahead').each(function () {
                var
                    e = $(this),
                    remoteUrl = (typeof (e.data('remote-url')) !== 'undefined') ? e.data('remote-url') : window.location.pathname,
                    hound = new Bloodhound({
                        datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
                        queryTokenizer: Bloodhound.tokenizers.whitespace,
                        remote: {
                            // The query
                            url: remoteUrl + '?_q=%QUERY',
                            wildcard: '%QUERY',
                            cache: false,
                            // The additional data params passed to the query
                            ajax: {
                                data: {
                                    _limit: (typeof (e.data('limit')) !== 'undefined') ? e.data('limit') : 20,
                                    _data: function () {
                                        // Set the class to show you are loading the information
                                        e.addClass('tt-autocomplete');

                                        // Set the data function
                                        var att = e.get(0).attributes, xxx = {};

                                        for (var i = 0, n = att.length; i < n; i++) {
                                            if (att[i].nodeName.indexOf('data-') >= 0) {
                                                // set everything else
                                                try {
                                                    // Check if the value is a json string
                                                    var o = window.JSON.parse(att[i].value);

                                                    // Handle non-exception-throwing cases:
                                                    // Neither JSON.parse(false) or JSON.parse(1234) throw errors, hence
                                                    // the type-checking, but... JSON.parse(null) returns 'null', and
                                                    // typeof null === "object", so we must check for that, too.
                                                    if (o && o !== null && typeof o === "object")
                                                        xxx[att[i].nodeName.replace('data-', '')] = o;
                                                    else if ( o && o !== null )
                                                        xxx[att[i].nodeName.replace('data-', '')] = o;
                                                } catch (exception) {
                                                    xxx[att[i].nodeName.replace('data-', '')] = att[i].value;
                                                }
                                            }
                                        }

                                        return window.JSON.stringify(xxx);
                                    }
                                },
                                error: function (jqXHR, textStatus, errorThrown) {
                                    if ( jqXHR.status === 403 ) window.location.reload();
                                },
                                complete: function () {
                                    e.removeClass('tt-autocomplete');
                                }
                            }
                        }
                    });

                // Initialise the bloodhound
                hound.initialize();

                // Initialise the typeahead object
                e.typeahead({
                    hint: true,
                    highlight: true
                }, {
                    displayKey: 'value',
                    source: hound.ttAdapter()
                });

                // if there is an input element to attach the id to
                if (typeof e.data('control') !== 'undefined' && e.data('control') !== false) {
                    e.on('typeahead:selected', function (jqO, datum) {
                        document.getElementsByName(e.data('control'))[0].value = (datum.id === '_q') ? '' : datum.id;
                    });
                }
            });
        });
    };

    var reportDateRange = function (e, fnDraw, ranges) {
        var yr = new Date().getFullYear(), sdate = moment(yr+"-01-01"), edate = moment(yr+"-12-31");
        if (typeof (ranges) === 'undefined') {
            ranges = {
                'Today': [moment(), moment()],
                'Last 7 Days': [moment().subtract(6, 'days'), new Date()],
                'Last 30 Days': [moment().subtract(29, 'days'), new Date()],
                'This Month': [moment().startOf('month'), moment().endOf('month')],
                'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
                'Current Year': [moment(yr+"-01-01"), moment(yr+"-12-31")],
                'Previous Year': [moment(yr+"-01-01").subtract(1, 'year'), moment(yr+"-12-31").subtract(1, 'year')]
            };
        }

        $('.date-range', e).daterangepicker({
            ranges: ranges,
            opens: 'left',
            locale: {
                format: 'YYYY-MM-DD',
                startDate: sdate.format('YYYY-MM-DD'),
                endDate: edate.format('YYYY-MM-DD')
            }
        }, function (start, end) {
            $('.date-range span', e)
                .html(start.format('MMM D, YYYY') + ' - ' + end.format('MMM D, YYYY'))
                .data('start-date', start.format('YYYY-MM-DD'))
                .data('end-date', end.format('YYYY-MM-DD'));
            fnDraw(start, end);
        });

        $('.date-range span', e)
            .html(sdate.format('MMM D, YYYY') + ' - ' + edate.format('MMM D, YYYY'))
            .data('start-date', sdate.format('YYYY-MM-DD'))
            .data('end-date', edate.format('YYYY-MM-DD'));
    };

    //Externalize reusable methods
    return {
        makerchecker : makerchecker,
        options: _options,
        createTable: p._createTable,
        tableAux: p._tableAux,
        toggleLoading : _toggleLoading,
        panelLoading : _panelLoading,
        houseKeep: _houseKeep,
        dataTable : _dataTable,
        alert: _alert,
        mobileValidator: _mobileValidator,
        passwordValidator : _passwordValidator,
        submitForm: _http.endRequest,
        http: _http,
        typeaheadInit : typeaheadRegister,
        initReportDateRange : reportDateRange
    };
}(utils || {}));

