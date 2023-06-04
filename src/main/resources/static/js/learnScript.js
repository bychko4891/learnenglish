//Заповнення сторінки першими данними при старті Lesson

var div;
var lessonId;
var userId;
$(document).ready(function () {
    lessonId = document.getElementById('lessonId').getAttribute('data-lesson-id');
    userId = document.getElementById('userId').getAttribute('data-user-id');
    // div = document.getElementById('buttonCheck');
    var nextButton = document.getElementById('nextButton');
    var textShow = document.getElementById('textShow');
    var checkButton = document.getElementById('checkButton');
    var url = "/user/" + userId + "/lesson/" + lessonId + "/reload";
    $.ajax({
        type: "GET",
        url: url,
        success: function (result) {
            console.log(result.fragment);

            if (result.fragment === "Fragment 1") {
                $('#replace_div').load("/fragmentsPages/lessonFragment1", function () {
                    textShow.classList.add('hidden');
                    checkButton.classList.remove('hidden');
                    nextButton.classList.add('disabled');
                    nextButton.setAttribute('disabled', 'disabled');
                    $('#ukr-text').html(result.ukrText);
                });
            } else if (result.fragment === "Fragment 2") {
                $('#replace_div').load("/fragmentsPages/lessonFragment2", function () {
                    textShow.classList.add('hidden');
                    checkButton.classList.remove('hidden');
                    nextButton.classList.add('disabled');
                    nextButton.setAttribute('disabled', 'disabled');
                    $('#eng-text').html(result.engText);
                });
            } else if (result.fragment === "Fragment 3"){
                $('#replace_div').load("/fragmentsPages/lessonFragment3", function () {
                    var checkButton = document.getElementById('checkButton');
                    checkButton.classList.add('hidden');
                    $('#ukr-text').html(result.ukrText);
                    $('#eng-text').html(result.engText);
                });
            } else {
                $('#replace_div').load("/fragmentsPages/lessonFragment4", function () {
                    textShow.classList.add('hidden');
                    checkButton.classList.remove('hidden');
                    nextButton.classList.add('disabled');
                    nextButton.setAttribute('disabled', 'disabled');
                    var ul = document.getElementById("words");
                    ul.html(result.ukrText);
                    // $('#eng-text').html(result.engText);
                    // var words = result.engText.split(" ");
                    // console.log(words);
                    // // Додаємо слова до списку (масиву)
                    // var wordList = [];
                    // for (var i = 0; i < words.length; i++) {
                    //     wordList.push(words[i]);
                    // }
                    // console.log(wordList);


                    // Пройтися по елементам списку і створити <li> для кожного елемента
                    for (var i = 0; i < result.engTextList.length; i++) {
                        var li = document.createElement("li");
                        li.textContent = result.engTextList[i];
                        ul.appendChild(li);
                    }
                });
            }
        },
        error: function () {
            // console.log('Помилка запиту на сервер');
        }
    });
});

// **********  Виконуємо запит при надсиланні запита користувачем Lesson *************** //

$('#nextText').submit(function (e) {
    e.preventDefault();
    // var lessonId = document.getElementById('lessonId').getAttribute('data-lesson-id');
    // var userId = document.getElementById('userId').getAttribute('data-user-id');
    var textShow = document.getElementById('textShow');
    var url = "/user/" + userId + "/lesson/" + lessonId + "/reload";
    $.ajax({
        type: "GET",
        url: url,
        success: function (result) {
            console.log(result.fragment);
            var checkButton = document.getElementById('checkButton');
            var nextButton = document.getElementById('nextButton');
            if (result.fragment === "Fragment 1") {
                $('#replace_div').load("/fragmentsPages/lessonFragment1", function () {
                    textShow.classList.add('hidden');
                    checkButton.classList.remove('hidden');
                    nextButton.classList.add('disabled');
                    nextButton.setAttribute('disabled', 'disabled');
                    $('#ukr-text').html(result.ukrText);
                });
            } else if (result.fragment === "Fragment 2") {
                $('#replace_div').load("/fragmentsPages/lessonFragment2", function () {
                    textShow.classList.add('hidden');
                    nextButton.classList.add('disabled');
                    nextButton.setAttribute('disabled', 'disabled');
                    checkButton.classList.remove('hidden');
                    $('#eng-text').html(result.engText);
                });
            } else {
                $('#replace_div').load("/fragmentsPages/lessonFragment3", function () {
                    nextButton.classList.remove('disabled');
                    nextButton.removeAttribute('disabled');
                    checkButton.classList.add('hidden');
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


function getData() {
    var resultDivSuccess = $('#result-success');
    var resultDivError = $('#result-error');
    // var url = window.location.href;
    // var lessonId = url.match(/lesson\/(\d+)/)[1];
    // url = $(this).attr('action') + lessonId + "/check";
    var url = "/user/" + userId + "/lesson/" + lessonId + "/check";
    $.ajax({
        url: url,
        type: "GET",
        data: $('#textCheck').serialize(),
        success: function (result) {
            var nextButton = document.getElementById('nextButton');
            var checkButton = document.getElementById('checkButton');
            nextButton.classList.remove('disabled');
            nextButton.removeAttribute('disabled');
            checkButton.classList.add('disabled');
            checkButton.setAttribute('disabled', 'disabled');
            $('#result').html(result);
            // console.log(result);

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