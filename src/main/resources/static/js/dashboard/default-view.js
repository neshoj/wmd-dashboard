{
    let Site = window.Site,
        token = $("meta[name='_csrf']").attr("content"),
        header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
        xhr.setRequestHeader("Accept", "application/json");
    });

    $(document).ready(function () {

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

        //Set up calendar
        $('#calendar').fullCalendar({
            header: {
                left: 'prev,next today',
                center: 'title'
            },
            height: 500,
            events: window.location.pathname,
            handleWindowResize: true,
            defaultView: 'month'
        });

        function fetchDashboardStatistics() {
            let url = window.location.href + 'dashboard-statistic'

            fetch(url)
                .then((resp) => resp.json())
                .then(function (data) {
                    console.log('Dashboard statistics')
                    console.log(data)

                    $('#no-of-stations').text(data.stations);
                    $('#hardware-monitor').text(data.hardware);
                    $('#tagged-vehicles').text(data.taggedVehicle);
                    $('#tagged-vehicles').text(data.taggedVehicle);
                    $('#census').text(data.census);
                    $('#weighed-vehicles').text(data.weighedVehicles);
                    // $('#no-of-stations').counter();

                })
                .catch(function (error) {
                    console.log(error)
                    alert('We could not load dashboard stats at this moment. Please try again!')
                })
        }

        fetchDashboardStatistics()

        function fetchCensusStatistics() {
            let url = window.location.href + 'census-statistic'

            fetch(url)
                .then((resp) => resp.json())
                .then(function (data) {
                    console.log('Census statistics')

                    $('#census-buses').text(data.buses);
                    $('#census-trucks-3t').text(data.trucksAboveThreeTonnes);
                    $('#census-trucks-7t').text(data.trucksAboveSevenTonnes);

                })
                .catch(function (error) {
                    console.log(error)
                    alert('We could not load dashboard stats at this moment. Please try again!')
                })
        }
    });
}