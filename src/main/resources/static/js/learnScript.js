//Заповнення сторінки першими данними при старті
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
// **********  Виконуємо запит при надсиланні форми користувачем *************** //
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
        event.preventDefault(); // prevent page from refreshing after form submission-->

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
// Новий текст по запросу
// $(document).ready(function () {
//     $(function () {
//         $('#reload').submit(function (e) {
//             e.preventDefault();
//             var data = $(this).serialize();
//             // var url = $(this).attr('action');
//             var url = $('#reload').attr('action');
//             var csrfToken = $("input[name='_csrf']").val();
//             $.ajax({
//                 type: "GET",
//                 url: url,
//                 data: data,
//                 // dataType: "html", // Додатковий параметр
//                 beforeSend: function (xhr) {
//                     xhr.setRequestHeader("X-CSRF-TOKEN", csrfToken);
//                 },
//                 success: function (result) {
//                     console.log(result);
//                     $('.content_block').hide();
//                     $('#ukr-text').html(result.ukrText);
//                     $('#english-text').html(result.engText);
//                 }
//                 // ,
//                 // error: function () {
//                 //     let shel = {};
//                 //     alert(Boolean(shel))
//                 //     // Поміщаємо повідомлення про помилку в div-елемент
//                 //     resultDivError.text('Помилка запиту на сервер');
//                 // }
//             });
//
//         });
//     });
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