{
    let Site = window.Site,
        token = $("meta[name='_csrf']").attr("content"),
        header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
        xhr.setRequestHeader("Accept", "application/json");
    });

    $(document).ready(function () {

        // let backgroundColors = [
        //     'rgb(244, 67, 54)',
        //     'rgb(233, 30, 99)',
        //     'rgb(156, 39, 176)',
        //     'rgb(103, 58, 183)',
        //     'rgb(63, 81, 181)',
        //     'rgb(33, 150, 243)',
        //     'rgb(3, 169, 244)',
        //     'rgb(0, 188, 212)',
        //     'rgb(0, 150, 136)',
        //     'rgb(76, 175, 80)',
        //     'rgb(139, 195, 74)',
        //     'rgb(205, 220, 57)',
        //     'rgb(255, 235, 59)',
        //     'rgb(255, 193, 7)',
        //     'rgb(255, 152, 0)',
        //     'rgb(255, 87, 34)',
        //     'rgb(121, 85, 72)',
        //     'rgb(158, 158, 158)',
        //     'rgb(96, 125, 139)'
        // ];
        // //    Start weighing Donut Chart
        // let donutChartCensusCtx = document.getElementById('donutWeighingStatusChart').getContext('2d');
        // let weighStatusLabel = [];
        // let weighStatusData = [];
        // chartData.pieChartData.forEach((data) => {
        //     weighStatusLabel.push(data.label);
        //     weighStatusData.push(data.data);
        // });
        // let donutOptions = {
        //     responsive: true,
        //     legend: {
        //         display: false
        //     },
        //     animation: {
        //         animateScale: true,
        //         animateRotate: true
        //     }
        // };
        // let donutConfig = {
        //     type: 'doughnut',
        //     data: {
        //         datasets: [{
        //             data: weighStatusData,
        //             backgroundColor: backgroundColors,
        //             label: 'Vehicle Census'
        //         }],
        //         labels: weighStatusLabel,
        //     },
        //     options: donutOptions
        // };
        // let donutChart = new Chart(donutChartCensusCtx, donutConfig);
        // // End weighing donut chart
        //
        // // Start Vehicle Census Donut Chart
        // let lineChartCensusCtx = document.getElementById('vehicleChartCensus').getContext('2d');
        // let axleConfiguration = [];
        // let axleConfigurationCount = [];
        // chartData.censusBarChartData.forEach((data) => {
        //     axleConfiguration.push(data.axleConfiguration);
        //     axleConfigurationCount.push(data.count);
        // });
        // let options = {
        //     responsive: true,
        //     legend: {
        //         display: false
        //     },
        //     animation: {
        //         animateScale: true,
        //         animateRotate: true
        //     }
        // };
        // let config = {
        //     type: 'doughnut',
        //     data: {
        //         datasets: [{
        //             data: axleConfigurationCount,
        //             backgroundColor: backgroundColors,
        //             label: 'Vehicle Census'
        //         }],
        //         labels: axleConfiguration,
        //     },
        //     options: options
        // };
        // let censusChart = new Chart(lineChartCensusCtx, config);
        // // End Vehicle Census Donut Chart
        //
        // // Start tag chart
        // let tagChartConfig = {
        //     type: 'pie',
        //     data: {
        //         datasets: [{
        //             data: [
        //                 chartData.clearTags,
        //                 chartData.pendingTags
        //             ],
        //             backgroundColor: [
        //                 'rgb(76, 175, 80)',
        //                 'rgb(255, 87, 34)'
        //             ],
        //             label: 'Tagging'
        //         }],
        //         labels: [
        //             'Cleared Tags',
        //             'Pending Tags'
        //         ]
        //     },
        //     options: {
        //         responsive: true,
        //         legend: {
        //             display: false
        //         }
        //     }
        // };
        // let tagctx = document.getElementById('tag-chart').getContext('2d');
        // let tagChart = new Chart(tagctx, tagChartConfig);
        // // End tag chart

        // Start Vehicle weighing statistics chart
        let lineChart = document.getElementById('weighingLineChart').getContext('2d');
        let lables = [];
        let overloadWeights = [];
        let withinLimitsWeights = [];

        if (chartData.withinLimit.length > chartData.overload.length) {
            for (let i = 0; i < chartData.withinLimit.length; i++) {
                lables.push(chartData.withinLimit[i].month);
            }
        } else {
            for (let i = 0; i < chartData.overload.length; i++) {
                lables.push(chartData.overload[i].month);
            }
        }

        for (let i = 0; i < lables.length; i++) {
            overloadWeights.push(chartData.overload[i] ? chartData.overload[i].count : 0);
            withinLimitsWeights.push(chartData.withinLimit[i] ? chartData.withinLimit[i].count : 0);
        }
        let chart = new Chart(lineChart, {
            type: 'bar',
            data: {
                labels: lables,
                datasets: [{
                    label: 'Overloads',
                    backgroundColor: '#6d6b6b',
                    borderColor: '#6d6b6b',
                    data: overloadWeights,
                    fill: false
                }, {
                    label: 'Within Limit',
                    backgroundColor: '#f4e415',
                    borderColor: '#f4e415',
                    data: withinLimitsWeights,
                    fill: false
                }]
            },
            // Configuration options go here
            options: {
                responsive: true,
                maintainAspectRatio: false,
                legend: {
                    position: 'bottom',
                },
                title: {
                    display: true,
                    text: 'Vehicle Weighing'
                }
            }
        });
        // End vehicle weighing statistics chart

        $('.weighbridges').change(function (e) {
            let weighbridgeId = $(this).find("option:selected").val();

            console.log('Weighbridge id ' + weighbridgeId);
            utils.http.jsonRequest($(".am-wrapper"),
                {
                    action: 'fetch-line-chart',
                    index: weighbridgeId
                })
                .done(function (o) {
                    if (o.status === '00') {
                        console.log(JSON.stringify(o))
                    } else {
                        utils.alert.error(o.message);
                    }
                });
        });
    });
}