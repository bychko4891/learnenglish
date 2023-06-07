//Заповнення сторінки першими данними при старті Lesson

var div;
var lessonId;

$(document).ready(function () {
    lessonId = document.getElementById('lessonId').getAttribute('data-lesson-id');

    // div = document.getElementById('buttonCheck');
    var nextButton = document.getElementById('nextButton');
    var textShow = document.getElementById('textShow');
    var checkButton = document.getElementById('checkButton');
    var url = "/lesson/" + lessonId + "/reload";
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
                    console.log(result.ukrText);
                    $('#ukr-text').html(result.ukrText);
                });
            } else if (result.fragment === "Fragment 2") {
                $('#replace_div').load("/fragmentsPages/lessonFragment2", function () {
                    textShow.classList.add('hidden');
                    checkButton.classList.remove('hidden');
                    nextButton.classList.add('disabled');
                    nextButton.setAttribute('disabled', 'disabled');
                    console.log(result.engText);
                    $('#eng-text').html(result.engText);
                });
            } else if (result.fragment === "Fragment 3") {
                $('#replace_div').load("/fragmentsPages/lessonFragment3", function () {
                    $('.content_block').hide();
                    var checkButton = document.getElementById('checkButton');
                    checkButton.classList.add('hidden');
                    $('#ukr-text').html(result.ukrText);
                    $('#eng-text').html(result.engText);
                });
            } else {
                textShow.classList.add('hidden');
                checkButton.classList.remove('hidden');
                checkButton.onclick = sendDataToServer;
                nextButton.classList.add('disabled');
                nextButton.setAttribute('disabled', 'disabled');
                var ul = document.getElementById("words");
                $('#replace_div').load("/fragmentsPages/lessonFragment4", function () {
                    $('#ukr-text').html(result.ukrText);
                    var ul = document.getElementById("words");
                    for (var i = 0; i < result.engTextList.length; i++) {
                        var li = document.createElement("li");
                        li.textContent = result.engTextList[i];
                        li.draggable = true;
                        li.ondragstart = function (event) {
                            drag(event);
                        };
                        ul.append(li);
                    }
                });

            }
        },
        error: function () {
            // console.log('Помилка запиту на сервер');
        }
    });
});
// *****************


// **********  Виконуємо запит при надсиланні запита користувачем Lesson *************** //

$('#nextText').submit(function (e) {
    e.preventDefault();
    var textShow = document.getElementById('textShow');
    var url = "/lesson/" + lessonId + "/reload";
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
            } else if (result.fragment === "Fragment 3") {
                $('#replace_div').load("/fragmentsPages/lessonFragment3", function () {
                    $('.content_block').hide();
                    var checkButton = document.getElementById('checkButton');
                    checkButton.classList.add('hidden');
                    $('#ukr-text').html(result.ukrText);
                    $('#eng-text').html(result.engText);
                });
            } else {
                textShow.classList.add('hidden');
                checkButton.classList.remove('hidden');
                checkButton.onclick = sendDataToServer;
                nextButton.classList.add('disabled');
                nextButton.setAttribute('disabled', 'disabled');
                // wordList = result.engTextList;
                var ul = document.getElementById("words");
                $('#replace_div').load("/fragmentsPages/lessonFragment4", function () {
                    $('#ukr-text').html(result.ukrText);
                    var ul = document.getElementById("words");
                    for (var i = 0; i < result.engTextList.length; i++) {
                        var li = document.createElement("li");
                        li.textContent = result.engTextList[i];
                        li.draggable = true;
                        li.ondragstart = function (event) {
                            drag(event);
                        };
                        ul.append(li);
                    }
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
    var url = "/lesson/" + lessonId + "/check";
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

function sendDataToServer() {
    var sentence = document.getElementById("sentence");
    var wordSequence = sentence.innerText.trim(); // Отримати текстове значення речення
    var url = "/lesson/" + lessonId + "/check";
    console.log(wordSequence + " * sendDataToServer *");
    // Відправити дані на сервер
    $.ajax({
        url: url,
        type: "GET",
        data: {textCheck: wordSequence},
        // contentType: "text/plain",
        success: function (result) {
            var nextButton = document.getElementById('nextButton');
            var checkButton = document.getElementById('checkButton');
            nextButton.classList.remove('disabled');
            nextButton.removeAttribute('disabled');
            checkButton.classList.add('disabled');
            checkButton.setAttribute('disabled', 'disabled');
            $('#result').html(result);
        },
        error: function (xhr, status, error) {
            // Обробка помилки
            console.error(error);
        }
    });
}


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

// *********             Навігація по заняттям                       ********* //
$(document).ready(function () {
    var previousLesson = document.getElementById("previousLesson");
    var lessonIdUserInput = document.getElementById("lessonIdInput");
    var submitButton = document.getElementById("submitButton");
    var nextLesson = document.getElementById("nextLesson");
// Обробник події для кнопки "Підтвердити"
    submitButton.addEventListener("click", function () {
        var lessonIdInput = lessonIdUserInput.value;
        loadLesson(lessonIdInput);
    });

// Обробник події для кнопки "Назад"
    previousLesson.addEventListener("click", function () {
        var previousLessonId = lessonId - 1;
        loadLesson(previousLessonId);
    });

// Обробник події для кнопки "Вперед"
    nextLesson.addEventListener("click", function () {
        console.log("id " + lessonId);
        var nextLessonId = parseInt(lessonId) + 1;
        console.log("next " + nextLessonId);
        loadLesson(nextLessonId);
    });

// Функція для завантаження уроку за його ідентифікатором
    function loadLesson(lessonIdData) {
        if (lessonIdData < 1) {
            lessonIdData = 1;
        }
        if (lessonIdData > 16) {
            lessonIdData = 16;
        }
        var url = "/lesson/" + lessonIdData;
        window.location.href = url;
    }
});