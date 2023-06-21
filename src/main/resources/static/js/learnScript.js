var lessonId;
var fragment;
$(document).ready(function () {
    lessonId = document.getElementById('lessonId').getAttribute('data-lesson-id');
});

$(window).on('scroll', function () {
    var videoContainer = $('#video-container');
    var videoTop = videoContainer.offset().top;
    var videoBottom = videoTop + videoContainer.outerHeight();
    var windowTop = $(window).scrollTop();
    var windowBottom = windowTop + $(window).height();

});


// скрипт який підмінює img на iframe на сторінці lesson
$(document).ready(function () {
    $('.about-radio').change(function () {
        var tabId = $(this).attr('id');

        if (tabId === 'learnInfo') {
            var videoContainer = $('#video-container');
            var img = videoContainer.find('img');
            var videoSrc = img.data('src');

            var iframe = $('<iframe>', {
                src: videoSrc,
                width: '560',
                height: '314',
                allowfullscreen: 'allowfullscreen'
            });
            videoContainer.empty().append(iframe);
        }
    });
});


function sendTooServer() {
    var textShow = document.getElementById('textShow');
    var checkButton = document.getElementById('checkButton');
    var nextButton = document.getElementById('nextButton');
    var lessonId = document.getElementById('lessonId').getAttribute('data-lesson-id');
    var url = "/lesson/" + lessonId + "/reload";
    $.ajax({
        type: "GET",
        url: url,
        success: function (result) {
            fragment = result.fragment;
            if (result.fragment === "Fragment 1") {
                checkButton.classList.remove('hidden');
                $('#replace_div').load("/fragmentsPages/lessonFragment1", function () {
                    textShow.classList.add('hidden');
                    nextButton.classList.add('disabled');
                    nextButton.setAttribute('disabled', 'disabled');
                    $('#ukr-text').html(result.ukrText);
                });
            } else if (result.fragment === "Fragment 2") {
                checkButton.classList.remove('hidden');
                $('#replace_div').load("/fragmentsPages/lessonFragment2", function () {
                    textShow.classList.add('hidden');
                    nextButton.classList.add('disabled');
                    nextButton.setAttribute('disabled', 'disabled');

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
}


// Виклик функції при завантаженні сторінки
window.addEventListener('load', sendTooServer);


// Виклик функції при кліку на кнопку
var nextText = document.getElementById('nextText');
nextText.addEventListener('click', sendTooServer);


function getData() {
    if (fragment === "Fragment 4") {
        var sentence = document.getElementById("sentence");
        var wordSequence = sentence.innerText.trim(); // Отримати текстове значення речення
        var data = {textCheck: wordSequence};
    } else {
        var data = $('#textCheck').serialize();
    }
    var url = "/lesson/" + lessonId + "/check";
    $.ajax({
        url: url,
        type: "GET",
        data: data,
        success: function (result) {
            var nextButton = document.getElementById('nextButton');
            var checkButton = document.getElementById('checkButton');
            nextButton.classList.remove('disabled');
            nextButton.removeAttribute('disabled');
            checkButton.classList.add('disabled');
            checkButton.setAttribute('disabled', 'disabled');
            $('#result').html(result);
        },
        error: function () {
            // let shel = {};
            // alert(Boolean(shel))
            showErrorToast('Помилка запиту на сервер');
        }
    });
}

// ************   checkbox   *************** //
$(document).ready(function () {
    $('#toggleSwitch').on('change', function () {
        // Get CSRF token from the meta tag-->
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        var isChecked = $(this).prop('checked');
        var userId = $(this).data('user-id');
        var url = '/user/' + userId + '/user-text-check';
        $.ajax({
            url: url,
            type: 'POST',
            data: {userActive: isChecked},
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (result) {
                var status = result.status;
                if (status == "Success") {
                    showSuccessToast(result.message);
                    var nextButton = document.getElementById('nextButton');
                    sendTooServer();
                    nextButton.classList.remove('disabled');
                    nextButton.removeAttribute('disabled');
                } else {
                    showErrorToast(result.message);
                }
            },
            error: function () {
                // Виникла помилка при відправленні запиту
                console.log('Помилка при відправленні запиту');
            }
        });
    });
});

// ************   Додавання тексту в базу   *************** //
$(document).ready(function () {
    $('#add-pair').submit(function (event) {
        event.preventDefault();
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        var url = '/translation-pair/add';
        var formData = $(this).serializeArray();
        if ($('textarea[name="ukrText"]').val() && $('textarea[name="engText"]').val()) {
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
                    if (status == "Success") {
                        $('textarea[name="ukrText"]').val('');
                        $('textarea[name="ukrTextFemale"]').val('');
                        $('textarea[name="engText"]').val('');
                        showSuccessToast(result.message);
                    } else {
                        showErrorToast(result.message);
                    }
                },
                error: function (xhr) {
                    var response = JSON.parse(xhr.responseText);
                    if (Array.isArray(response)) {
                        for (var i = 0; i < response.length; i++) {
                            var error = response[i];
                            if (error.fieldName === ('ukrText')) {
                                $('#ukrText').css('border-color', 'red');
                                $('#ukrTextError').html(error.fieldMessage);
                            } else if (error.fieldName === ('ukrTextFemale')) {
                                $('#ukrTextFemale').css('border-color', 'red');
                                $('#ukrTextFemaleError').html(error.fieldMessage);
                            } else if (error.fieldName === ('engText')) {
                                $('#engText').css('border-color', 'red');
                                $('#engTextError').html(error.fieldMessage);
                            }
                        }
                    } else {
                        showErrorToast("Помилка сервера");
                    }
                }
            });
        } else {
            // якщо не всі поля заповнені, не виконуємо запит на сервер і виводимо помилку
            alert('Будь ласка, заповніть поле вводу');
            return;
        }
    });
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