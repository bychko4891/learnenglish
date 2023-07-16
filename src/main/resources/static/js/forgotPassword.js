// Функція для генерації випадкових чисел та відправки суми на сервер
function generateAndSend() {
    // Генерування випадкових чисел
    var number1 = Math.floor(Math.random() * 100) + 1; // Від 1 до 100
    var number2 = Math.floor(Math.random() * 9) + 1; // Від 1 до 9

    // Обчислення суми
    var sum = number1 + number2;

    // Оновлення вмісту div з числами
    var randomNumbersDiv1 = document.getElementById('randomNumbers1');
    randomNumbersDiv1.innerHTML = number1;
    var randomNumbersDiv2 = document.getElementById('randomNumbers2');
    randomNumbersDiv2.innerHTML = number2;

    // Відправка суми на сервер
    var xhr = new XMLHttpRequest();
    var url = '/forgot-password/captcha'; // Замініть на власну адресу контроллера

    xhr.open('POST', url, true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    // Додавання CSRF-токену до заголовків
    var csrfToken = $("meta[name='_csrf']").attr("content");
    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
    xhr.setRequestHeader(csrfHeader, csrfToken);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            // Операція успішно виконана
            console.log('Сума була успішно відправлена на сервер.');
        }
    };

    var data = 'sum=' + encodeURIComponent(sum);
    xhr.send(data);
}

// Додавання обробника події на кнопку "Згенерувати та відправити"
var generateButton = document.getElementById('reloadCaptcha');
generateButton.addEventListener('click', generateAndSend);

// Запуск генерації та відправки при завантаженні сторінки
window.addEventListener('load', generateAndSend);

$(document).ready(function () {
    var resultDivSuccess = $('#resultDivSuccess');
    var resultDivError = $('#resultDivError');
    $('#forgotPassword').submit(function (event) {
        event.preventDefault();
        var url = $(this).attr('action');
        var formData = $(this).serializeArray();
        $.ajax({
            url: url,
            type: "POST",
            data: formData,
            success: function (result) {
                var status = result.status;
                // console.log(status);
                if (status == "Success") {
                    $('input[name="password"]').val('');
                    $('input[name="newPassword"]').val('');
                    // Отримуємо div-елемент, в який ми будемо поміщати повідомлення
                    resultDivSuccess.text(result.message);
                    setTimeout(hideMessageSuccess, 5000);
                    setTimeout(function () {
                        window.location.href = "/login";
                    }, 3000);
                } else {
                    resultDivError.text(result.message);
                    setTimeout(hideMessageError, 5000);
                }
            },
            error: function () {
                let shel = {};
                alert(Boolean(shel))
                resultDivError.text('Помилка запиту на сервер');
                setTimeout(hideMessageError, 5000);
            }
        });
    });

    function hideMessageSuccess() {
        resultDivSuccess.text('');
    }

    function hideMessageError() {
        resultDivError.text('');
    }
});