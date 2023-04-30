document.addEventListener('DOMContentLoaded', function() {
    $(document).ready(function () {
        // var resultDivSuccess = $('#result-success');
        // var resultDivError = $('#result-error');
        $('#registration').submit(function (event) {
            event.preventDefault();
            // Get CSRF token from the meta tag-->
            var csrfToken = $("meta[name='_csrf']").attr("content");
            var csrfHeader = $("meta[name='_csrf_header']").attr("content");
            var password = document.getElementById("psw").value;
            var isValid = checkPassword(password); // викликаємо функцію з другого скрипту

            // var url = $(this).attr('action');
            var formData = $(this).serializeArray();
            // console.log(formData);
            if (isValid) {
                var jsonFormData = {};
                $(formData).each(function (index, obj) {
                    jsonFormData[obj.name] = obj.value;
                });
                // console.log(jsonFormData);
                $.ajax({
                    url: "/registration",
                    type: "POST",
                    contentType: "application/json",
                    data: JSON.stringify(jsonFormData),
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(csrfHeader, csrfToken);
                    },
                    success: function (result) {
                        console.log(result);
                        // var status = result.status;
                        // console.log(status);
                        // if (status == "Success") {
                        //     $('textarea[name="ukrText"]').val('');
                        //     $('textarea[name="engText"]').val('');
                        //     // Отримуємо div-елемент, в який ми будемо поміщати повідомлення
                        //     resultDivSuccess.text(result.message);
                        //     setTimeout(hideMessageSuccess, 5000);
                        // } else {
                        //     resultDivError.text(result.message);
                        //     setTimeout(hideMessageError, 10000);
                        // }
                        // window.location.replace('https://example.com/newpage');
                    },
                    error: function () {
                        let shel = {};
                        alert(Boolean(shel))
                        // Поміщаємо повідомлення про помилку в div-елемент
                        resultDivError.text('Помилка запиту на сервер');
                    }
                });
            } else {
                // якщо не всі поля заповнені, не виконуємо запит на сервер і виводимо помилку
                alert('Пароль не відповідає вимогам безпеки!');
                return;
            }
        });

        function hideMessageSuccess() {
            resultDivSuccess.text('');

        }

        function hideMessageError() {

            resultDivError.text('');
        }


        let psw = document.getElementById('psw');
        let toggleBtn = document.getElementById('toggleBtn');
        let lowerCase = document.getElementById('lover');
        let upperCase = document.getElementById('upper');
        let digit = document.getElementById('number');
        let specialChar = document.getElementById('special');
        let minLength = document.getElementById('length');

        function checkPassword(data) {

            const lower = new RegExp('(?=.*[a-z])');
            const upper = new RegExp('(?=.*[A-Z])');
            const number = new RegExp('(?=.*[0-9])');
            const special = new RegExp('(?=.*[!@#\$%\^&\*])');
            const length = new RegExp('(?=.{8,})');
            let isValid = true;
            if (!lower.test(data)) {
                lowerCase.classList.remove('valid');
                isValid = false;
            } else {
                lowerCase.classList.add('valid');
            }
            if (!upper.test(data)) {
                upperCase.classList.remove('valid');
                isValid = false;
            } else {
                upperCase.classList.add('valid');
            }
            if (!number.test(data)) {
                digit.classList.remove('valid');
                isValid = false;
            } else {
                digit.classList.add('valid');
            }
            if (!special.test(data)) {
                specialChar.classList.remove('valid');
                isValid = false;
            } else {
                specialChar.classList.add('valid');
            }
            if (!length.test(data)) {
                minLength.classList.remove('valid');
                isValid = false;
            } else {
                minLength.classList.add('valid');
            }
            // console.log(data);
            return isValid;
        }

        toggleBtn.onclick = function () {
            if (psw.type === 'password') {
                psw.setAttribute('type', 'text');
                toggleBtn.classList.add('hide');
            } else {
                psw.setAttribute('type', 'password');
                toggleBtn.classList.remove('hide');
            }
        }
        psw.addEventListener('keyup', function () {
            checkPassword(this.value);
        });
    });
});