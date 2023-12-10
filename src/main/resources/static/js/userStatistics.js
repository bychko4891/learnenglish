var availableDates = [];
var url = '/user/user-statistics';
$.getJSON(url, function (data) {
    var studyTimeInTwoWeeks = data.studyTimeInTwoWeeks;
    console.log(studyTimeInTwoWeeks);
    var repetitionsCount = data.repetitionsCount; // repetitionsCount
    var repetitionsCountPrev = data.repetitionsCountPrev; // repetitionsCountPrev
    var repetitionsCountNow = data.repetitionsCountNow; // repetitionsCountNow
    var daysInARowCount = data.daysInARowCount; // daysInARowCount
    var errorCount = data.errorCount; // errorCount
    // console.log(studyTimeInTwoWeeks);
    var yData = studyTimeInTwoWeeks.map(function(element) {
        return Math.round(element / 60);
    });
    var xData = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14];
    // Знаходження максимального значення у списку
    var maxValue = Math.max(...yData);
    // Отримання елементу canvas за його ідентифікатором
    var ctx = document.getElementById('myChart').getContext('2d');
    // Створення лінійного графіка
    var myChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: xData,
            datasets: [{
                label: 'Хвилин',
                data: yData,
                borderColor: 'rgb(48,124,124)',
                backgroundColor: 'rgba(75, 192, 192, 0.7)',
                fill: true,
                borderWidth:1,
                pointStyle: 'rectRot',
                pointRadius: 5,
                pointBorderColor: 'rgb(0, 0, 0)'
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true,
                    max: Math.ceil(maxValue / 20) * 20, // Максимальне значення на осі Y (заокруглене до ближчого кратного 20)
                    stepSize: 20 // Крок на осі Y
                }
            }
        }
    });


    // Отримання елементу canvas за його ідентифікатором
    var ctx = document.getElementById('myChart2').getContext('2d');

    // Створення стовпчикової діаграми
    var myChart2 = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['К-сть повторень вчора', 'К-сть повторень сьогодні'],
            datasets: [{
                label: 'Повторення',
                data: [repetitionsCountPrev, repetitionsCountNow],
                backgroundColor: ['rgba(255, 99, 132, 0.7)', 'rgba(54, 162, 235, 0.7)'],
                borderColor: ['rgb(246,48,88)', 'rgb(20,149,243)'],
                borderWidth: 1
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


    var ctx = document.getElementById('myChart3').getContext('2d');
    var myChart3 = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['Повторення', 'Помилки'],
            datasets: [{
                data: [repetitionsCount, errorCount], // Замініть ці значення на ваші дані
                backgroundColor: ['green', 'red']
            }]
        },
        options: {
            responsive: true
        }
    });



});