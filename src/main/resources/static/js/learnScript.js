//Заповнення сторінки першими данними при старті Lesson
// $(document).ready(function () {
//     // Виконуємо запит при першій загрузці сторінки
//     var url = $('#reload').attr('action');
//     var csrfToken = $("input[name='_csrf']").val();
//     $.ajax({
//         type: "GET",
//         url: url,
//         data: $('#reload').serialize(),
//         beforeSend: function (xhr) {
//             xhr.setRequestHeader("X-CSRF-TOKEN", csrfToken);
//         },
//         success: function (result) {
//             // console.log(result);
//             $('.content_block').hide();
//             $('#ukr-text').html(result.ukrText);
//             $('#english-text').html(result.engText);
//         },
//         error: function () {
//             // console.log('Помилка запиту на сервер');
//         }
//     });
var reloadButton;
var div;
var lessonId;
var userId;
$(document).ready(function () {
    lessonId = document.getElementById('lessonId').getAttribute('data-lesson-id');
    userId = document.getElementById('userId').getAttribute('data-user-id');
    // div = document.getElementById('buttonCheck');
    reloadButton = document.getElementById('reloadButton');
    var url = "/user/" + userId + "/lesson/" + lessonId + "/reload";
    $.ajax({
        type: "GET",
        url: url,
        success: function (result) {
            console.log(result.fragment);

            if (result.fragment === "Content 1") {
                $('#replace_div').load("/content/content1", function () {
                    reloadButton.classList.add('disabled');
                    reloadButton.setAttribute('disabled', 'disabled');

                        // div.innerHTML ="<button id='checkButton' onclick=\"getData()\" >Перевірити</button>";
                        $('#ukr-text').html(result.ukrText);
                });
            } else if(result.fragment === "Content 2"){
                $('#replace_div').load("/content/content2", function () {
                    reloadButton.classList.add('disabled');
                    reloadButton.setAttribute('disabled', 'disabled');
                    // div.innerHTML ="<button id='checkButton' onclick=\"getData2()\" >Перевірити</button>";
                    $('#eng-text').html(result.engText);
                });
            } else {
                $('#replace_div').load("/content/content3", function () {
                    var buttonCheck = document.getElementById('buttonCheck');
                    buttonChek.style.visibility = 'hidden';
                    $('#ukr-text').html(result.ukrText);
                    $('#eng-text').html(result.engText);
                });
            }
        },
        error: function () {
            // console.log('Помилка запиту на сервер');
        }
    });
});

// **********  Виконуємо запит при надсиланні запита користувачем Lesson *************** //

function handleButtonClick(reloadButton) {
    // var lessonId = document.getElementById('lessonId').getAttribute('data-lesson-id');
    // var userId = document.getElementById('userId').getAttribute('data-user-id');
    var url = "/user/" + userId + "/lesson/" + lessonId + "/reload";
    $.ajax({
        type: "GET",
        url: url,
        success: function (result) {
            console.log(result.fragment);
            // var reloadButton = document.getElementById('reloadButton');
            if (result.fragment === "Content 1") {
                $('#replace_div').load("/content/content1", function () {
                    reloadButton.classList.add('disabled');
                    reloadButton.setAttribute('disabled', 'disabled');
                    div.innerHTML ="<button id='checkButton' onclick=\"getResult()\" >Перевірити</button>";
                    $('#ukr-text').html(result.ukrText);
                });
            } else if(result.fragment === "Content 2"){
                $('#replace_div').load("/content/content2", function () {
                    reloadButton.classList.add('disabled');
                    reloadButton.setAttribute('disabled', 'disabled');
                    div.innerHTML ="<button id='checkButton' onclick=\"getResult()\" >Перевірити</button>";
                    $('#eng-text').html(result.engText);
                });
            } else {
                $('#replace_div').load("/content/content3", function () {
                    $('#ukr-text').html(result.ukrText);
                    $('#eng-text').html(result.engText);
                });
            }
        },
        error: function () {
            // console.log('Помилка запиту на сервер');
        }
    });


    function getResult() {
        var resultDivSuccess = $('#result-success');
        var resultDivError = $('#result-error');
        // $('#textCheck').submit(function (event) {
        //     event.preventDefault();
        // var url = window.location.href;
        // var lessonId = url.match(/lesson\/(\d+)/)[1];
        // url = $(this).attr('action') + lessonId + "/check";
        var url = "/user/" + userId + "/lesson/" + lessonId + "/check";

        // if ($('textarea[name="ukrText"]').val() && $('textarea[name="engText"]').val()) {
        //   var ukrTextTemp = $('textarea[name="ukrText"]').val();
        //   var engTextTemp = $('textarea[name="engText"]').val();
        //   if (ukrTextTemp.length > 300 || engTextTemp.length > 300) {
        //     alert("Вибачте, але дозволено довжину речення максимум 300 символів разом з пропусками!!!");
        //     return;
        //   }
        $.ajax({
            url: url,
            type: "GET",
            data: $('#textCheck').serialize(),
            // beforeSend: function (xhr) {
            //     xhr.setRequestHeader(csrfHeader, csrfToken);
            // },
            success: function (result) {
                var reloadButton = document.getElementById('reloadButton');
                var checkButton = document.getElementById('checkButton');
                reloadButton.classList.remove('disabled');
                reloadButton.removeAttribute('disabled');
                checkButton.classList.add('disabled');
                checkButton.setAttribute('disabled', 'disabled');
                $('#result').html(result);
                // $('#reloadButton').removeAttribute('disabled');
                console.log(result);
                // if (status == "Success") {
                //   $('textarea[name="ukrText"]').val('');
                //   $('textarea[name="engText"]').val('');
                //   // Отримуємо div-елемент, в який ми будемо поміщати повідомлення
                //   resultDivSuccess.text(result.message);
                //   setTimeout(hideMessageSuccess, 5000);
                // } else {
                //   resultDivError.text(result.message);
                //   setTimeout(hideMessageError, 10000);
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
        //   // якщо не всі поля заповнені, не виконуємо запит на сервер і виводимо помилку
        //   alert('Будь ласка, заповніть поле вводу');
        //   return;
        // }
        // }

        function hideMessageSuccess() {
            resultDivSuccess.text('');

        }

        function hideMessageError() {

            resultDivError.text('');
        }
    }




    // });
// ***************** Кнопка, щоб відкрити скритий текст *************** //
//     $('.content_toggle').click(function () {
//         $('.content_block').slideToggle(600);
//         return false;
//     });
}


//     $('#reload').submit(function (e) {
//         e.preventDefault();
//         var data = $(this).serialize();
//         var url = $(this).attr('action');
//         var csrfToken = $("input[name='_csrf']").val();
//         $.ajax({
//             type: "GET",
//             url: url,
//             data: data,
//             beforeSend: function (xhr) {
//                 xhr.setRequestHeader("X-CSRF-TOKEN", csrfToken);
//             },
//             success: function (result) {
//                 // console.log(result);
//                 $('.content_block').hide();
//                 $('#ukr-text').html(result.ukrText);
//                 $('#english-text').html(result.engText);
//             },
//             error: function () {
//                 // console.log('Помилка запиту на сервер');
//             }
//         });
//     // });
// // ***************** Кнопка, щоб відкрити скритий текст *************** //
//     $('.content_toggle').click(function () {
//         $('.content_block').slideToggle(600);
//         return false;
//     });
// });

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
                    var status = result.status;
                    // console.log(status);
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


////////////////////////////////////////////////////////////////////////////////////////////////


window.onload = function () {


}