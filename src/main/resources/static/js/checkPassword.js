document.addEventListener('DOMContentLoaded', function () {
    $(document).ready(function () {
        var resultDivError = $('#result-error');
        $('#registration').submit(function (event) {
            event.preventDefault();
            // Get CSRF token from the meta tag-->
            var csrfToken = $("meta[name='_csrf']").attr("content");
            var csrfHeader = $("meta[name='_csrf_header']").attr("content");
            var password = document.getElementById("psw").value;
            var isValid = checkPassword(password); // викликаємо функцію з другого скрипту
            var formData = $(this).serializeArray();
            delete formData.confirmPassword;
            if (isValid) {
                if ($("#confirmPassword").val() === $("#psw").val()) {
                    var jsonFormData = {};
                    $(formData).each(function (index, obj) {
                        jsonFormData[obj.name] = obj.value;
                    });
                    console.log(jsonFormData);
                    $.ajax({
                        url: "/registration",
                        type: "POST",
                        contentType: "application/json",
                        data: JSON.stringify(jsonFormData),
                        beforeSend: function (xhr) {
                            xhr.setRequestHeader(csrfHeader, csrfToken);
                        },
                        success: function (result, textStatus, jqXHR) {
                            if (jqXHR.status === 200) {
                                $('#registrationPage').load("/fragmentsPages/successRegistrationFragment", function () {
                                    $('#result-success').html(result);
                                    setTimeout(function () {
                                        window.location.href = "/login";
                                    }, 12000);
                                });
                            }
                        },
                        error: function (xhr, status, error) {
                            console.log(xhr.responseText);
                            // let shel = {};
                            // alert(Boolean(shel)) //  <--вікно підвердження
                            resultDivError.text(xhr.responseText);
                            setTimeout(hideMessageError, 5000);
                        }
                    });
                } else {
                    // alert('Пароль не співпадають!');
                    resultDivError.text('Пароль не співпадають!');
                    setTimeout(hideMessageError, 4000);
                    return;
                }
            } else {
                // alert('Пароль не відповідає вимогам безпеки!');
                resultDivError.text('Пароль не відповідає вимогам безпеки!');
                setTimeout(hideMessageError, 4000);
                return;
            }
        });
        function hideMessageError() {

            resultDivError.text('');
        }

        let psw = document.getElementById('psw');
        let confirmPassword = document.getElementById('confirmPassword');
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
            if (psw.type === 'password' && confirmPassword.type === 'password') {
                psw.setAttribute('type', 'text');
                confirmPassword.setAttribute('type', 'text');
                toggleBtn.classList.add('hide');
            } else {
                psw.setAttribute('type', 'password');
                confirmPassword.setAttribute('type', 'password');
                toggleBtn.classList.remove('hide');
            }
        }

        psw.addEventListener('keyup', function () {
            checkPassword(this.value);
        });
    });
});