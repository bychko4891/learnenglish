document.addEventListener('DOMContentLoaded', function() {
    $(document).ready(function () {
        var resultDivSuccess = $('#result-success');
        var resultDivError = $('#result-error');
        $('#registration').submit(function (event) {
            event.preventDefault();
            // Get CSRF token from the meta tag-->
            var csrfToken = $("meta[name='_csrf']").attr("content");
            var csrfHeader = $("meta[name='_csrf_header']").attr("content");
            var password = document.getElementById("psw").value;
            var isValid = checkPassword(password); // викликаємо функцію з другого скрипту

            // var url = $(this).attr('action');
            var formData = $(this).serializeArray();
            delete formData.confirmPassword;
            // console.log(formData);
            if (isValid) {
                if ($("#confirmPassword").val() === $("#psw").val()) {
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
                        success: function (result, textStatus, jqXHR) {
                            if (jqXHR.status === 200) {
                                console.log("Success:", result);
                                resultDivSuccess.text(result);
                                document.getElementById("registration").reset();
                                setTimeout(function () {
                                    window.location.href = "/login";
                                }, 5000);
                            }

                            // else if (jqXHR.status === 409) {
                            //     console.log("Conflict:", result);
                            //     resultDivError.text(result);
                            //     // setTimeout(hideMessageError, 5000);
                            //     // Обробка конфлікту (ResponseEntity.status(HttpStatus.CONFLICT))
                            // }
                            // }
                            // },error: function (jqXHR, textStatus, errorThrown) {
                            //     console.log(jqXHR.responseText);
                            //     resultDivError.text(jqXHR.responseText);
                            // }

                            // },


                            // error: function (jqXHR, textStatus, errorThrown) {
                            //     console.log("Error:", jqXHR.responseText);
                            //     // Обробка помилки
                            // }
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
                            // },

                        }, error: function () {
                            // let shel = {};
                            // alert(Boolean(shel)) //  <--вікно підвердження
                            // Поміщаємо повідомлення про помилку в div-елемент
                            resultDivError.text('Юзер з таким Email вже існує');
                        }

                    });
                } else {
                    // якщо не всі поля заповнені, не виконуємо запит на сервер і виводимо помилку
                    alert('Пароль не співпадають!');
                    return;
                }
            }else {
                alert('Пароль не відповідає вимогам безпеки!');
                return;
            }
        });

        // function hideMessageSuccess() {
        //     resultDivSuccess.text('');
        //
        // }

        // function hideMessageError() {
        //
        //     resultDivError.text('');
        // }


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
            confPassword();
            if (psw.type === 'password') {
                psw.setAttribute('type', 'text');
                toggleBtn.classList.add('hide');
            } else {
                psw.setAttribute('type', 'password');
                toggleBtn.classList.remove('hide');
            }
        }
        function confPassword() {
            if (confirmPassword.type === 'password') {
                confirmPassword.setAttribute('type', 'text');
                // toggleBtn.classList.add('hide');
            } else {
                confirmPassword.setAttribute('type', 'password');
                // toggleBtn.classList.remove('hide');
            }
        }
        psw.addEventListener('keyup', function () {
            checkPassword(this.value);
        });
    });
});