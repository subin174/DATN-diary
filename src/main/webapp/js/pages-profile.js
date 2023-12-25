document.addEventListener("DOMContentLoaded", function () {
    // Fetch data from the API
    fetch('http://localhost:8080/api/v1/admin/account/count-account-by-year')
        .then(response => response.json())
        .then(data => {
            // Line chart
            new Chart(document.getElementById("chartjs-line"), {
                type: "line",
                data: {
                    labels: Object.keys(data), // Use the months from the API response
                    datasets: [{
                        label: "Account Count",
                        fill: true,
                        backgroundColor: "transparent",
                        borderColor: window.theme.primary,
                        data: Object.values(data).map(monthData => monthData[0])
                    }]
                },
                options: {
                    maintainAspectRatio: false,
                    legend: {
                        display: false
                    },
                    tooltips: {
                        intersect: false
                    },
                    hover: {
                        intersect: true
                    },
                    plugins: {
                        filler: {
                            propagate: false
                        }
                    },
                    scales: {
                        xAxes: [{
                            reverse: true,
                            gridLines: {
                                color: "rgba(0,0,0,0.05)"
                            }
                        }],
                        yAxes: [{
                            display: true,
                            gridLines: {
                                color: "rgba(0,0,0,0)",
                                fontColor: "#fff"
                            }
                        }]
                    }
                }
            });
        })
        .catch(error => {
            console.error('Error fetching data from the API:', error);
        });
});

document.addEventListener("DOMContentLoaded", function () {
    // Fetch data from the API
    fetch('http://localhost:8080/api/v1/diary/count-diary-by-year')
        .then(response => response.json())
        .then(data => {
            // Line chart
            new Chart(document.getElementById("chartjs-line-2"), {
                type: "line",
                data: {
                    labels: Object.keys(data), // Use the months from the API response
                    datasets: [{
                        label: "Diary Count",
                        fill: true,
                        backgroundColor: "transparent",
                        borderColor: window.theme.primary,
                        data: Object.values(data).map(monthData => monthData[0])
                    }]
                },
                options: {
                    maintainAspectRatio: false,
                    legend: {
                        display: false
                    },
                    tooltips: {
                        intersect: false
                    },
                    hover: {
                        intersect: true
                    },
                    plugins: {
                        filler: {
                            propagate: false
                        }
                    },
                    scales: {
                        xAxes: [{
                            reverse: true,
                            gridLines: {
                                color: "rgba(0,0,0,0.05)"
                            }
                        }],
                        yAxes: [{
                            display: true,
                            gridLines: {
                                color: "rgba(0,0,0,0)",
                                fontColor: "#fff"
                            }
                        }]
                    }
                }
            });
        })
        .catch(error => {
            console.error('Error fetching data from the API:', error);
        });
});

document.addEventListener("DOMContentLoaded", function () {
    var currentDate = new Date();
    var defaultDate = currentDate.toISOString().slice(0, 19).replace("T", " ");
    document.getElementById("datetimepicker-dashboard").flatpickr({
        inline: true,
        prevArrow: "<span title=\"Previous month\">&laquo;</span>",
        nextArrow: "<span title=\"Next month\">&raquo;</span>",
        defaultDate: defaultDate
    });
});

document.addEventListener("DOMContentLoaded", function () {
    fetch('http://localhost:8080/api/v1/diary/count-mood-by-year')
        .then(response => response.json())
        .then(data => {
            const labels = Object.keys(data);
            const emotions = ["Trầm cảm", "Lo lắng", "Áp lực", "Mất kiểm soát", "Vui vẻ", "Hạnh phúc", "Buồn bã"];

            const datasets = emotions.map((emotion, index) => {
                const countValues = labels.map(month => {
                    const monthData = data[month].find(entry => entry[1] === emotion);
                    return monthData ? monthData[0] : 0;
                });

                return {
                    label: emotion,
                    data: countValues,
                    borderColor: getLineColor(index),
                    fill: false
                };
            });

            new Chart(document.getElementById("chartjs-line-3"), {
                type: "line",
                data: {
                    labels: labels,
                    datasets: datasets
                },
                options: {
                    legend: {
                        display: true,
                        position: 'bottom',
                    }
                }
            });
        })
        .catch(error => {
            console.error('Error fetching data from the API:', error);
        });
});

function getLineColor(index) {
    const colors = ["red", "brown", "yellow", "purple", "green", "pink", "black"/* Add more colors here */];
    return colors[index] || getRandomColor();
}

function getRandomColor() {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

document.addEventListener("DOMContentLoaded", function () {
    // Fetch data from the API
    fetch('http://localhost:8080/api/v1/admin/sound/getCountMood')
        .then(response => response.json())
        .then(data => {
            // Extract data from the API response
            const moodData = data.data;

            // Prepare data for Chart.js
            const labels = moodData.map(entry => entry[1]);
            const counts = moodData.map(entry => entry[0]);

            // Create doughnut chart
            new Chart(document.getElementById("chartjs-doughnut"), {
                type: 'doughnut',
                data: {
                    labels: labels,
                    datasets: [{
                        data: counts,
                        backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4CAF50'], // Add more colors as needed
                    }]
                },
                options: {
                    responsive: true,
                    legend: {
                        position: 'bottom',
                    }
                }
            });
        })
        .catch(error => {
            console.error('Error fetching data from the API:', error);
        });
});
document.addEventListener("DOMContentLoaded", function () {
    // Fetch data from the API
    fetch('http://localhost:8080/api/v1/diary/count-quantity-diary')
        .then(response => response.json())
        .then(data => {
            // Update the quantityDiary element with the fetched value
            document.getElementById("quantityDiary").textContent = data[0];
        })
        .catch(error => {
            console.error('Error fetching data from the API:', error);
        });
});
document.addEventListener("DOMContentLoaded", function () {
    // Fetch data from the API
    fetch('http://localhost:8080/api/v1/admin/account/count-quantity-account')
        .then(response => response.json())
        .then(data => {
            // Update the quantityDiary element with the fetched value
            document.getElementById("quantityAcc").textContent = data[0];
        })
        .catch(error => {
            console.error('Error fetching data from the API:', error);
        });
});
document.addEventListener("DOMContentLoaded", function () {
    // Fetch data from the API
    fetch('http://localhost:8080/api/v1/admin/sound/count-quantity-sound')
        .then(response => response.json())
        .then(data => {
            // Update the quantityDiary element with the fetched value
            document.getElementById("quantitySound").textContent = data[0];
        })
        .catch(error => {
            console.error('Error fetching data from the API:', error);
        });
});

