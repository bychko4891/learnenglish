//Заповнення сторінки першими данними при старті
$(document).ready(function () {
    $.ajax({
        url: "/englishJSON",
        type: "GET",
        // dataType: "html",
        data: {param: "value"},
        success: function (data) {
            $('#ukr-text').html(data.ukrText);
            $('#english-text').html(data.engText);
            // $("#show_result").hide();
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(textStatus, errorThrown);
        }
    });
});
//Додавання тексту в базу
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
$('form').submit(function (event) {
    event.preventDefault();
    sendMessage();
});
// Новий текст по запросу
$(document).ready(function () {
    $('#reload').click(function () {
        $.get("/englishJSON", function (data) {
            // console.log(data); // тут ви можете виконати потрібні дії зі змінною data, яка містить дані з сервера
            $('.content_block').hide();
            $('#ukr-text').html(data.ukrText);
            $('#english-text').html(data.engText);
            // $("#show_result").hide();
        });
    });
});
// Кнопка, щоб відкрити скритий текст
$(document).ready(function () {
    $('.content_toggle').click(function () {
        $('.content_block').slideToggle(600);
        return false;
    });
});
//авто висота вікна для вводу тексту
$(document).ready(function () {
    $('textarea').on('input', function () {
        this.style.height = 'auto';
        this.style.height = (this.scrollHeight) + 'px';
    });
});
// ------   log in   -------

$(function () {
    // var resultDivSuccess = $('#result-success');
    // var resultDivError = $('#result-error');
    $('#login_modal').submit(function (e) {
        e.preventDefault();
        var data = $(this).serialize();
        // if ($('input[name="username"]').val() && $('input[name="password"]').val()) {
        //     var username = $('input[name="username"]').val();
        //     var password = $('input[name="password"]').val();
        //     // console.log(ukrTextTemp);
        //     if (username.length > 300 || password.length > 300) {
        //         alert("Вибачте, але дозволено довжину речення максимум 300 символів разом з пропусками!!!");
        //         return;
        //     }
        // якщо всі поля заповнені, виконуємо запит на сервер
        $.ajax({
            type: "POST",
            url: "/login",
            data: data,
            success: function (result) {
                // var status = result.status;
                // if (status == "Success") {
                //     $('textarea[name="ukrText"]').val('');
                //     $('textarea[name="engText"]').val('');
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
        // }
        // else {
        //     // якщо не всі поля заповнені, не виконуємо запит на сервер і виводимо помилку
        //     alert('Будь ласка, заповніть поле вводу');
        //     return;
        // }
    });

    function hideMessageSuccess() {
        resultDivSuccess.text('');

    }

    function hideMessageError() {

        resultDivError.text('');
    }
});