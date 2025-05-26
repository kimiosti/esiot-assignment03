function refresh() {
    fetch("api/data", {
        method: "POST"
    }).then(res => {
        return res.json();
    }).then(data => {
        chart.destroy();
        chart = new Chart(chartArea, {
            type: 'line',
            data: {
                labels: data.times,
                datasets: [{
                    label: 'Temperature',
                    data: data.temp,
                }]
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    });
}

const chartArea = document.getElementById("chart");
var chart = new Chart(chartArea, {
    type: 'line',
    data: {
        labels: [],
        datasets: [{
            label: 'Temperature',
            data: [],
        }]
    },
    options: {
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
});

setInterval(refresh, 2000);