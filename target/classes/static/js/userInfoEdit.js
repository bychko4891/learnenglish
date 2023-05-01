//********  Profile edit  START  *************** //
$(document).ready(function () {
    var resultDivSuccess = $('#resultDivSuccess');
    var resultDivError = $('#resultDivError');
    $('#update_profile').submit(function (event) {
        event.preventDefault();
        // Get CSRF token from the meta tag-->
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        var url = $(this).attr('action');
        var formData = $(this).serializeArray();
        if ($('#first-name').val() || $('#last-name').val()) {     //доработать !!!!
            $.ajax({
                url: url,
                type: "POST",
                data: formData,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                },
                success: function (result) {
                    // console.log(result);
                    resultDivSuccess.text(result);
                    setTimeout(hideMessageSuccess, 5000);
                },
                error: function () {
                    let shel = {};
                    alert(Boolean(shel))
                    resultDivError.text('Помилка запиту на сервер');
                    setTimeout(hideMessageError, 5000);
                }
            });
        } else {
            // якщо не всі поля не заповнені, не виконуємо запит на сервер і виводимо помилку
            alert('Будь ласка, заповніть поле вводу');
            return;
        }
    });
    function hideMessageSuccess() {
        resultDivSuccess.text('');
    }
    function hideMessageError() {
        resultDivError.text('');
    }
});

//********  Profile edit  END   *************** //

//********  Update Password User  START  *************** //
$(document).ready(function () {
    var resultDivSuccess = $('#password-edit-success');
    var resultDivError = $('#password-edit-error');
    $('#update_password').submit(function (event) {
        event.preventDefault();
        // Get CSRF token from the meta tag-->
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        var url = $(this).attr('action');
        var formData = $(this).serializeArray();
        if ($('input[name="password"]').val() && $('input[name="newPassword"]').val()) {
            // console.log(formData);
            $.ajax({
                url: url,
                type: "POST",
                data: formData,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                },
                success: function (result) {
                    // console.log(result);
                    var status = result.status;
                    // console.log(status);
                    if (status == "Success") {
                        $('input[name="password"]').val('');
                        $('input[name="newPassword"]').val('');
                        // Отримуємо div-елемент, в який ми будемо поміщати повідомлення
                        resultDivSuccess.text(result.message);
                        setTimeout(hideMessageSuccess, 5000);
                    } else {
                        resultDivError.text(result.message);
                        setTimeout(hideMessageError, 5000);
                    }
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
            alert('Будь ласка, заповніть поле вводу');
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
//********  Update Password User  END  *************** //