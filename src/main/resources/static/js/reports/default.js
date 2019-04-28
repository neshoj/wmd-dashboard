$(() => {
    let $$ = $(document);
    let token = $("meta[name='_csrf']").attr("content"),
        header = $("meta[name='_csrf_header']").attr("content");
    let parentTable = $('.parent-table');

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
        xhr.setRequestHeader("Accept", "application/json");
    });

    let parent = $(".parent-panel"), child = $(".child-panel"), childTable;

    // Event fired before
    let drawStart = table => {
        let e = $.Event('tabledrawstart');
        e.table = table;
        $(window).trigger(e);
    };

    let drawing = (row, aData) => {
        let e = $.Event('tabledrawing');
        e.tableRow = row;
        e.aData = aData;
        $(window).trigger(e);
    };

    // Fired when last row is drawn
    let drawEnd = table => {
        let e = $.Event('tabledrawend');
        e.table = table;
        $(window).trigger(e);
    };

    // handle tables
    let columnCount = $('thead th', parentTable).length;
    var rowCounter = 0;
    let oTable = utils.dataTable({
        table: parentTable,
        fnRowCallback: (nRow, aData) => {
            drawing(nRow, aData);
            if (rowCounter === 0)
                drawStart( parentTable );

            if (child.length > 0) {
                console.log('columnCount', columnCount)
                $(nRow).children('td:eq(0)').html('<a href="#" class="childTrigger" data-id="' + aData[columnCount] + '">' + aData[0] + '</a>');

                $('a', nRow).on('click', e => {
                    e.preventDefault();
                    parent.hide();
                    child.toggleClass("d-none");

                    let index = aData[columnCount];
                    $('.card-title', child).html(aData[0]);

                    childTable = utils.dataTable({
                        table: $(".child-table"),
                        fnServerParams: (aoData) => {
                            aoData.push(
                                {name: 'data', value: index},
                                {name:'wbsNo', value:  $('.weighbridge-station-no > .wbs-title-btn', child).data('index') },
                                {name:'actionTaken', value:  $('.action-taken > .title-btn', child).data('index') },
                                {name:'startDate', value: $('.date-range span', child).data('start-date')},
                                {name:'endDate', value: $('.date-range span', child).data('end-date')}
                            );
                        }
                    });
                });

            }
            rowCounter++;
        },
        fnServerParams : function( aoData ){
            aoData.push(
                {name:'wbsNo', value:  $('.weighbridge-station-no  > .wbs-title-btn', parent).data('index') },
                {name:'actionTaken', value:  $('.action-taken > .title-btn', parent).data('index') },
                {name:'startDate', value: $('.date-range span', parent).data('start-date')},
                {name:'endDate', value: $('.date-range span', parent).data('end-date')}
            );
        },
    });


    $$.on('click', '.btn-info', () => {
        if (!parent.is(":visible")) {
            parent.show();
            child.toggleClass("d-none");
            childTable.fnDestroy();
            childTable = undefined;
        }
    });

    //When downloading reports
    $('.download-report a').each(function(){
        $(this).attr('href', window.location.pathname+"/"+$(this).data('action'));
    });

    //Refresh Event for Child Table
    $('.btn-success', child).click(function () {
        if (child.is(":visible")) {
            childTable.fnDraw();
        }
    });

    //Refresh Event for Parent Table
    $('.btn-success', parent).click(function () {
        if (parent.is(":visible")) {
            oTable.fnDraw();
        }
    });

    //When updating drop-down filters of parent panel
    $(".action-taken > .dropdown-menu button", parent).click( function(e) {
        console.log('Button selected ');
        e.preventDefault();
        let el = $(this).parents()[2];
        let css = $(this).data("filter");

        $('.title-btn',  el ).html( $(this).html() );
        $('.title-btn',  el ).data('index', $(this).data('index'));

        $("."+ css + ' > .title-btn',  child ).html( $(this).html() );
        $("."+ css + ' > .title-btn',  child ).data('index', $(this).data('index'));
        oTable.fnDraw();
    });

    //When updating drop-down filters of child panel
    $(".action-taken > .dropdown-menu button", child).click( function(e) {
        e.preventDefault();
        let el = $(this).parents()[2];
        $('.title-btn',  el ).html( $(this).html() );
        $('.title-btn',  el ).data('index', $(this).data('index'));
        childTable.fnDraw();
    });

    //When updating drop-down filters of parent panel
    $(".weighbridge-station-no > .dropdown-menu button", parent).click( function(e) {
        console.log('Button selected ');
        e.preventDefault();
        let el = $(this).parents()[2];
        let css = $(this).data("filter");

        $('.wbs-title-btn',  el ).html( $(this).html() );
        $('.wbs-title-btn',  el ).data('index', $(this).data('index'));

        $("."+ css + ' > .wbs-title-btn',  child ).html( $(this).html() );
        $("."+ css + ' > .wbs-title-btn',  child ).data('index', $(this).data('index'));
        oTable.fnDraw();
    });

    //When updating drop-down filters of child panel
    $(".weighbridge-station-no > .dropdown-menu button", child).click( function(e) {
        e.preventDefault();
        let el = $(this).parents()[2];
        $('.wbs-title-btn',  el ).html( $(this).html() );
        $('.wbs-title-btn',  el ).data('index', $(this).data('index'));
        childTable.fnDraw();
    });

    //Initialize date range for parent panel
    utils.initReportDateRange('', function(start, end){
        try {
            let daterange = $('.date-range');
            daterange.data('daterangepicker').setStartDate(start);
            daterange.data('daterangepicker').setEndDate(end);

            $('.date-range span')
                .html(start.format('MMM D, YYYY') + ' - ' + end.format('MMM D, YYYY'))
                .data('start-date', start.format('YYYY-MM-DD'))
                .data('end-date', end.format('YYYY-MM-DD'));

            if (typeof childTable === 'undefined')  oTable.fnDraw();
            else childTable.fnDraw();
        }
        catch (e){
            console.error( e );
        }
    });

    //Initialize date range for child panel
    utils.initReportDateRange(child, function(){ childTable.fnDraw(); });

});