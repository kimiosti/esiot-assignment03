function toggleMode() {
    fetch("api/action/toggleMode", {
        method: "POST"
    });
}

function restoreState() {
    fetch("api/action/restoreState", {
        method: "POST"
    });
}

function refresh() {
    // TODO - revise refresh operations
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
                    data: data.temps,
                }]
            }
        });

        document.getElementById("avgMeasure").innerHTML = data.average;
        document.getElementById("maxMeasure").innerHTML = data.max;
        document.getElementById("minMeasure").innerHTML = data.min;
        document.getElementById("curState").innerHTML = data.state;
        document.getElementById("openingLevel").innerHTML = data.opening;
    });
}

const chartArea = document.getElementById("chart");
var chart = new Chart(chartArea, { });
refresh();

document.getElementById("modeSwitch").addEventListener("click", toggleMode);
document.getElementById("resetState").addEventListener("click", restoreState);

setInterval(refresh, 2000);