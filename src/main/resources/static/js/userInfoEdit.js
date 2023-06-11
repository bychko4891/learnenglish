//********  Profile edit  START  *************** //
const firstNameInput = document.getElementById('first-name');
const lastNameInput = document.getElementById('last-name');
var initialFirstNameValue;
var initialLastNameValue;

$(document).ready(function () {
    initialFirstNameValue = firstNameInput.value;
    initialLastNameValue = lastNameInput.value;

});

function reloadValue() {
    initialFirstNameValue = firstNameInput.value;
    initialLastNameValue = lastNameInput.value;

}

$(document).ready(function () {
    var resultDivSuccess = $('#resultDivSuccess');
    var resultDivError = $('#resultDivError');
    $('#update_profile').submit(function (event) {
        event.preventDefault();
        var url = $(this).attr('action');
        var formData = $(this).serializeArray();
        if (checkUserInfo(formData)) {
            if ($('#first-name').val() || $('#last-name').val()) {
                $.ajax({
                    url: url,
                    type: "POST",
                    data: formData,
                    success: function (result) {
                        reloadValue();
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
        } else {
            console.log("console info: Інформація успішно оновлена");
            resultDivSuccess.text("Інформація успішно оновлена");
            setTimeout(hideMessageSuccess, 5000);
        }
    });

    function hideMessageSuccess() {
        resultDivSuccess.text('');
    }

    function hideMessageError() {
        resultDivError.text('');
    }
});

function checkUserInfo(data) {
    const firstName = data.find(field => field.name === 'firstName').value;
    const lastName = data.find(field => field.name === 'lastName').value;
    const gender = $('input[name="gender"]:checked').val();
    // const initialFirstName = $('#first-name').attr('data-initial-value');
    // const initialLastName = $('#last-name').attr('data-initial-value');
    const initialGender = $('#gender').attr('data-initial-value');

    if (initialFirstNameValue === firstName && initialLastNameValue === lastName && initialGender === gender) {
        return false;
    } else {
        return true;
    }
}

//********  Profile edit  END   *************** //

//********  Update Password User  START  *************** //
$(document).ready(function () {
    var resultDivSuccess = $('#password-edit-success');
    var resultDivError = $('#password-edit-error');
    $('#update_password').submit(function (event) {
        event.preventDefault();
        var url = $(this).attr('action');
        var formData = $(this).serializeArray();
        if ($('input[name="password"]').val() && $('input[name="newPassword"]').val()) {
            // console.log(formData);
            $.ajax({
                url: url,
                type: "POST",
                data: formData,
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
//********   User  CALENDAR  *************** //
$(document).ready(function () {
    fetch('/sessionData')
        .then(response => response.json())
        .then(data => {
            var userId = data.userId;
            var availableDates = [];
            var url = '/user/' + userId + '/training-days';
            $.getJSON(url, function (data) {
                var numberOfDays = data.length;
                $('#numberOfDaysUser').text("Ви вже займаєтесь " + numberOfDays + " днів.");
                availableDates = data.map(function (dateStr) {
                    var dateParts = dateStr.split('-');
                    return new Date(dateParts[0], dateParts[1] - 1, dateParts[2]);
                });
                // після отримання даних з сервера, ініціалізуємо календар
                $('#calendar').datepicker({
                    beforeShowDay: function (date) {
                        var dateString = $.datepicker.formatDate('yy-mm-dd', date);
                        var isAvailable = availableDates.findIndex(function (date) {
                            return $.datepicker.formatDate('yy-mm-dd', date) == dateString;
                        }) >= 0;
                        var cssClass = isAvailable ? 'highlight' : 'disabled';
                        return [isAvailable, cssClass];
                    }
                });
            });
        });
});