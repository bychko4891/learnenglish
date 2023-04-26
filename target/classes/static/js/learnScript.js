//Заповнення сторінки першими данними при старті Lesson
$(document).ready(function () {
    // Виконуємо запит при першій загрузці сторінки
    var url = $('#reload').attr('action');
    var csrfToken = $("input[name='_csrf']").val();
    $.ajax({
        type: "GET",
        url: url,
        data: $('#reload').serialize(),
        beforeSend: function (xhr) {
            xhr.setRequestHeader("X-CSRF-TOKEN", csrfToken);
        },
        success: function (result) {
            // console.log(result);
            $('.content_block').hide();
            $('#ukr-text').html(result.ukrText);
            $('#english-text').html(result.engText);
        },
        error: function () {
            // console.log('Помилка запиту на сервер');
        }
    });
// **********  Виконуємо запит при надсиланні запита користувачем Lesson *************** //
    $('#reload').submit(function (e) {
        e.preventDefault();
        var data = $(this).serialize();
        var url = $(this).attr('action');
        var csrfToken = $("input[name='_csrf']").val();
        $.ajax({
            type: "GET",
            url: url,
            data: data,
            beforeSend: function (xhr) {
                xhr.setRequestHeader("X-CSRF-TOKEN", csrfToken);
            },
            success: function (result) {
                console.log(result);
                $('.content_block').hide();
                $('#ukr-text').html(result.ukrText);
                $('#english-text').html(result.engText);
            },
            error: function () {
                // console.log('Помилка запиту на сервер');
            }
        });
    });
});
// ***************** Кнопка, щоб відкрити скритий текст *************** //
$(document).ready(function () {
    $('.content_toggle').click(function () {
        $('.content_block').slideToggle(600);
        return false;
    });
});

// ************   Додавання тексту в базу   *************** //
$(document).ready(function () {
    var resultDivSuccess = $('#result-success');
    var resultDivError = $('#result-error');
    $('#add-pair').submit(function (event) {
        event.preventDefault();
        // Get CSRF token from the meta tag-->
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        var url = $(this).attr('action');
        var formData = $(this).serializeArray();

        if ($('textarea[name="ukrText"]').val() && $('textarea[name="engText"]').val()) {
            var ukrTextTemp = $('textarea[name="ukrText"]').val();
            var engTextTemp = $('textarea[name="engText"]').val();
            // console.log(ukrTextTemp);
            if (ukrTextTemp.length > 300 || engTextTemp.length > 300) {
                alert("Вибачте, але дозволено довжину речення максимум 300 символів разом з пропусками!!!");
                return;
            }
            var jsonFormData = {};
            $(formData).each(function (index, obj) {
                jsonFormData[obj.name] = obj.value;
            });
            $.ajax({
                url: url,
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(jsonFormData),
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                },
                success: function (result) {
                    // console.log(result);
                    var status = result.status;
                    console.log(status);
                    if (status == "Success") {
                        $('textarea[name="ukrText"]').val('');
                        $('textarea[name="engText"]').val('');
                        // Отримуємо div-елемент, в який ми будемо поміщати повідомлення
                        resultDivSuccess.text(result.message);
                        setTimeout(hideMessageSuccess, 5000);
                    } else {
                        resultDivError.text(result.message);
                        setTimeout(hideMessageError, 10000);
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
});

//********  авто висота вікна для вводу тексту *************** //
$(document).ready(function () {
    $('textarea').on('input', function () {
        this.style.height = 'auto';
        this.style.height = (this.scrollHeight) + 'px';
    });
});

//********  Profile edit  START  *************** //
$(document).ready(function () {
    // var resultDivSuccess = $('#result-success');
    // var resultDivError = $('#result-error');
    $('#update_profile').submit(function (event) {
        event.preventDefault(); // prevent page from refreshing after form submission-->

        // Get CSRF token from the meta tag-->
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        var url = $(this).attr('action');
        var formData = $(this).serializeArray();

        // if ($('textarea[name="ukrText"]').val() && $('textarea[name="engText"]').val()) {
        //     var ukrTextTemp = $('textarea[name="ukrText"]').val();
        //     var engTextTemp = $('textarea[name="engText"]').val();
        // console.log(ukrTextTemp);
        // if (ukrTextTemp.length > 300 || engTextTemp.length > 300) {
        //     alert("Вибачте, але дозволено довжину речення максимум 300 символів разом з пропусками!!!");
        //     return;
        // }
        // var jsonFormData = {};
        // $(formData).each(function (index, obj) {
        //     jsonFormData[obj.name] = obj.value;
        // });
        console.log(formData);
        $.ajax({
            url: url,
            type: "POST",
            // contentType: "application/json",
            // data: JSON.stringify(jsonFormData),
            data: formData,
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
            },
            error: function () {
                let shel = {};
                alert(Boolean(shel))
                // Поміщаємо повідомлення про помилку в div-елемент
                resultDivError.text('Помилка запиту на сервер');
            }
        });
        // } else {
        //     // якщо не всі поля заповнені, не виконуємо запит на сервер і виводимо помилку
        //     alert('Будь ласка, заповніть поле вводу');
        //     return;
        // }
    });
    // function hideMessageSuccess() {
    //     resultDivSuccess.text('');
    //
    // }
    // function hideMessageError() {
    //
    //     resultDivError.text('');
    // }
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
});
//********  Update Password User  END  *************** //
//********  Registration User  START   *************** //
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
// });
/////////////////////////////////////////////////////////////////////////////
// $(document).ready(function () {
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
    if (psw.type == 'password') {
        psw.setAttribute('type', 'text');
        toggleBtn.classList.add('hide');
    } else {
        psw.setAttribute('type', 'password');
        toggleBtn.classList.remove('hide');
    }
}
    psw.addEventListener('keyup', function() {
        checkPassword(this.value);
    });
});

//********  Validate Password   START   *************** //
// document.addEventListener('DOMContentLoaded', function() {
// let pswd = document.getElementById('pswd');
// let toggleBtn = document.getElementById('toggleBtn');
// let lowerCase = document.getElementById('lover');
// let upperCase = document.getElementById('upper');
// let digit = document.getElementById('number');
// let specialChar = document.getElementById('special');
// let minLength = document.getElementById('length');

// function checkPassword(data){
//
//     const lower = new  RegExp('(?=.*[a-z])');
//     const upper = new  RegExp('(?=.*[A-Z])');
//     const number = new  RegExp('(?=.*[0-9])');
//     const special = new  RegExp('(?=.*[!@#\$%\^&\*])');
//     const length = new  RegExp('(?=.{8,})');
// if(lower.test(data)){
//     lowerCase.classList.add('valid');
// }else{
//     lowerCase.classList.remove('valid');
// }
// if(upper.test(data)){
//     upperCase.classList.add('valid');
// }else{
//     upperCase.classList.remove('valid');
// }
// if(number.test(data)){
//     digit.classList.add('valid');
// }else{
//     digit.classList.remove('valid');
// }
// if(special.test(data)){
//     specialChar.classList.add('valid');
// }else{
//     specialChar.classList.remove('valid');
// }
// if(length.test(data)){
//     minLength.classList.add('valid');
// }else{
//     minLength.classList.remove('valid');
// }
//     console.log(data);
// }

// show password
// toggleBtn.onclick = function (){
//     if (pswd.type == 'password'){
//         pswd.setAttribute('type', 'text');
//         toggleBtn.classList.add('hide');
//     } else {
//         pswd.setAttribute('type', 'password');
//         toggleBtn.classList.remove('hide');
//     }
// }
// });
// $(document).ready(function() {
//     $('#form').submit(function(event) {
//         event.preventDefault(); // prevent page from refreshing after form submission
//
//         var formData = $(this).serializeArray();
//         var jsonFormData = {};
//         $(formData).each(function(index, obj){
//             jsonFormData[obj.name] = obj.value;
//         });
//  console.log(formData);
//  console.log(jsonFormData);
//         $.ajax({
//             url: "/englishADD",
//             type: "POST",
//             contentType: "application/json",
//             data: JSON.stringify(jsonFormData),
//             success: function(response) {
//                 console.log(response);
//             },
//             error: function(xhr) {
//                 console.log(xhr.responseText);
//             }
//         });
//     });
// });
// Додаємо обробник події на кнопку відправки форми
// $('form').submit(function (event) {
//     event.preventDefault();
//     sendMessage();
// });
// Новий текст по запросу
// $(document).on('submit', '#reload', function (event) {
//     event.preventDefault();
//     var form = $(this);
//     // var url = form.attr('action');
//     var data = form.serialize();
//     $.get("/englishJSON", data, function (response) {
//         console.log(response);
//         // $('.content_block').hide();
//         // $('#ukr-text').html(response.ukrText);
//         // $('#english-text').html(response.engText);
//     });
//     return false;
// });
// $(function () {
//     var resultDivSuccess = $('#result-success');
//     var resultDivError = $('#result-error');
//     $('#form').submit(function (e) {
//         e.preventDefault();
//         var data = $(this).serialize();
//         if ($('textarea[name="ukrText"]').val() && $('textarea[name="engText"]').val()) {
//             var ukrTextTemp = $('textarea[name="ukrText"]').val();
//             var engTextTemp = $('textarea[name="engText"]').val();
//             // console.log(ukrTextTemp);
//             if (ukrTextTemp.length > 300 || engTextTemp.length > 300) {
//                 alert("Вибачте, але дозволено довжину речення максимум 300 символів разом з пропусками!!!");
//                 return;
//             }
//             // якщо всі поля заповнені, виконуємо запит на сервер
//             $.ajax({
//                 type: "GET",
//                 url: "/englishADD",
//                 data: data,
//                 success: function (result) {
//                     var status = result.status;
//                     if (status == "Success") {
//                         $('textarea[name="ukrText"]').val('');
//                         $('textarea[name="engText"]').val('');
//                         // Отримуємо div-елемент, в який ми будемо поміщати повідомлення
//                         resultDivSuccess.text(result.message);
//                         setTimeout(hideMessageSuccess, 5000);
//                     } else {
//                         resultDivError.text(result.message);
//                         setTimeout(hideMessageError, 5000);
//                     }
//                 },
//                 error: function () {
//                     let shel = {};
//                     alert(Boolean(shel))
//                     // Поміщаємо повідомлення про помилку в div-елемент
//                     resultDivError.text('Помилка запиту на сервер');
//                 }
//             });
//         } else {
//             // якщо не всі поля заповнені, не виконуємо запит на сервер і виводимо помилку
//             alert('Будь ласка, заповніть поле вводу');
//             return;
//         }
//     });
//
//     function hideMessageSuccess() {
//         resultDivSuccess.text('');
//
//     }
//
//     function hideMessageError() {
//
//         resultDivError.text('');
//     }
// });
// Додаємо обробник події на кнопку відправки форми
// $('form').submit(function (event) {
//     event.preventDefault();
//     sendMessage();
// });


// ------   log in   -------

// $(function () {
//     // var resultDivSuccess = $('#result-success');
//     // var resultDivError = $('#result-error');
//     $('#login_modal').submit(function (e) {
//         e.preventDefault();
//         var data = $(this).serialize();
//         // if ($('input[name="username"]').val() && $('input[name="password"]').val()) {
//         //     var username = $('input[name="username"]').val();
//         //     var password = $('input[name="password"]').val();
//         //     // console.log(ukrTextTemp);
//         //     if (username.length > 300 || password.length > 300) {
//         //         alert("Вибачте, але дозволено довжину речення максимум 300 символів разом з пропусками!!!");
//         //         return;
//         //     }
//         // якщо всі поля заповнені, виконуємо запит на сервер
//         $.ajax({
//             type: "POST",
//             url: "/login",
//             data: data,
//             success: function (result) {
//                 // var status = result.status;
//                 // if (status == "Success") {
//                 //     $('textarea[name="ukrText"]').val('');
//                 //     $('textarea[name="engText"]').val('');
//                 //     // Отримуємо div-елемент, в який ми будемо поміщати повідомлення
//                 //     resultDivSuccess.text(result.message);
//                 //     setTimeout(hideMessageSuccess, 5000);
//                 // } else {
//                 //     resultDivError.text(result.message);
//                 //     setTimeout(hideMessageError, 5000);
//                 // }
//             },
//             error: function () {
//                 let shel = {};
//                 alert(Boolean(shel))
//                 // Поміщаємо повідомлення про помилку в div-елемент
//                 resultDivError.text('Помилка запиту на сервер');
//             }
//         });
//         // }
//         // else {
//         //     // якщо не всі поля заповнені, не виконуємо запит на сервер і виводимо помилку
//         //     alert('Будь ласка, заповніть поле вводу');
//         //     return;
//         // }
//     });
//
//     function hideMessageSuccess() {
//         resultDivSuccess.text('');
//
//     }
//
//     function hideMessageError() {
//
//         resultDivError.text('');
//     }
// });
$(document).ready(function () {
    // var resultDivSuccess = $('#password-edit-success');
    // var resultDivError = $('#password-edit-error');
    $('#upload2').submit(function (event) {
        event.preventDefault();
        // Get CSRF token from the meta tag-->
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        // var url = $(this).attr('action');
        // var formData = $(this).serializeArray();
        var formData = new FormData($('#upload2')[0]);
        // if ($('input[name="password"]').val() && $('input[name="newPassword"]').val()) {
        //     console.log(formData);
            $.ajax({
                url: "/upload",
                type: "post",
                data: formData,
                processData: false,
                contentType: false,
                // enctype: "multipart/form-data",
                // types: "multipart/form-data",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                },
                success: function (result) {
                    console.log(result);
                    // var status = result.status;
                    // console.log(status);
                    // if (status == "Success") {
                    //     $('input[name="password"]').val('');
                    //     $('input[name="newPassword"]').val('');
                    //     // Отримуємо div-елемент, в який ми будемо поміщати повідомлення
                    //     resultDivSuccess.text(result.message);
                    //     setTimeout(hideMessageSuccess, 5000);
                    // } else {
                    //     resultDivError.text(result.message);
                    //     setTimeout(hideMessageError, 5000);
                    // }
                },
                error: function () {
                    let shel = {};
                    alert(Boolean(shel))
                    // Поміщаємо повідомлення про помилку в div-елемент
                    resultDivError.text('Помилка запиту на сервер');
                }
            });
        // } else {
        //     // якщо не всі поля заповнені, не виконуємо запит на сервер і виводимо помилку
        //     alert('Будь ласка, заповніть поле вводу');
        //     return;
        // }
    });

    // function hideMessageSuccess() {
    //     resultDivSuccess.text('');
    // }

    // function hideMessageError() {
    //     resultDivError.text('');
    // }
});