var slider = document.querySelector('.slider_word');
var page = 1;
var totalPage = 4;
var currentIndex = 0;

function updateSlider() {
    const slide = slider.firstElementChild;
    const slideStyles = window.getComputedStyle(slide);
    const slideWidth = slide.offsetWidth + parseInt(slideStyles.marginLeft) + parseInt(slideStyles.marginRight);
    slider.style.transform = `translateX(-${currentIndex * slideWidth}px)`;

    const mainSlide = slider.children[currentIndex];

    if (mainSlide) {
        mainSlide.classList.add('slide_active');
    }
    const currentSlide = slider.children[currentIndex - 1];
    if (currentSlide) {
        currentSlide.classList.remove('slide_active');
    }
}


function addWordToSlider(word) {
    const slideFragment = word.wordAuditSlide;
    const slide = document.createElement('div');
    slide.className = 'slide bb';
    if (slideFragment === 'slideAuditRadios') slide.className = 'slide bb radios';
    slider.append(slide);
    const slideDiv = $(slide);
    $.ajax({
        url: '/fragmentsPages/' + slideFragment,
        method: "GET"
    }).done(function (data) {
        slideDiv.html(data);

        const descriptionHeading = slideDiv.find("h5");
        descriptionHeading.text('"' + word.description + '"');

        const image = slideDiv.find("img");
        image.attr("src", "/word-image/" + word.imageName);

        const wordId = slideDiv.find("input[name='id']");
        wordId.val(word.id);

        const id = 'wordSelect-' + word.id;

        if (slideFragment === 'slideAuditRadios') {
            $.ajax({
                url: '/word-lesson/' + wordLessonId + '/word-lesson-audit-add-words',
                type: "GET",
                success: function (words) {
                    var wordsAudit = words;
                    wordsAudit.push(word.name);
                    const shuffledArray = shuffleArray(wordsAudit);
                    const checkboxBlocks = slide.querySelectorAll('.ks-cboxtags');

                    checkboxBlocks.forEach(block => {
                        const inputs = block.querySelectorAll('input');
                        const labels = block.querySelectorAll('label');

                        inputs.forEach((input, index) => {
                            const value = shuffledArray[index];
                            input.name = 'group-' + id;
                            input.id = id + '-' + index;
                            input.value = value;
                            labels[index].textContent = value;
                            labels[index].setAttribute('for', id + '-' + index);
                        });
                    });
                }
            });
        }


    });
    currentIndex++;
    updateSlider();
}

function shuffleArray(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
}


function wordsStart() {
    slider = document.querySelector('.slider_word');
    var url = '/word-lesson/' + wordLessonId + '/word-audit-start';
    $.ajax({
        url: url,
        type: "GET",
        success: function (result) {
            totalPage = result[0].totalPage;
            const objectDivs = document.querySelectorAll(".slide");

            objectDivs.forEach((div, index) => {

                const descriptionHeading = div.querySelector("h5");
                descriptionHeading.textContent = '"' + result[index].description + '"';

                const image = div.querySelector("img");
                image.src = "/word-image/" + result[index].imageName;

                const wordId = div.querySelector("input[name='id']");
                wordId.value = result[index].id;

                const id = 'wordSelect-' + result[index].id;

                if (index === 1) {
                    $.ajax({
                        url: '/word-lesson/' + wordLessonId + '/word-lesson-audit-add-words',
                        type: "GET",
                        success: function (words) {
                            var wordsAudit = words;
                            wordsAudit.push(result[index].name);
                            const shuffledArray = shuffleArray(wordsAudit);
                            const checkboxBlocks = div.querySelectorAll('.ks-cboxtags');

                            checkboxBlocks.forEach(block => {
                                const inputs = block.querySelectorAll('input');
                                const labels = block.querySelectorAll('label');

                                inputs.forEach((input, index) => {
                                    const value = shuffledArray[index];
                                    input.name = 'group-' + id;
                                    input.id = id + '-' + index;
                                    input.value = value;
                                    labels[index].textContent = value;
                                    labels[index].setAttribute('for', id + '-' + index);
                                });
                            });
                        }
                    });
                }
            });
        }
    });
}


function confirm(element) {
    // nextSlide();
    var csrfToken = $("meta[name='_csrf']").attr("content");
    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
    var wordId = $(element).closest('.block_confirm').find('input[name="id"]').val();

    const currentSlide = $(element).closest('.slide.bb.radios');
    var word = '';
    if (currentSlide.length > 0) {
        word = currentSlide.find('input[name="group-wordSelect-' + wordId + '"]:checked').val();
    } else {
        word = $(element).closest('.block_confirm').find('input[name="wordConfirm"]').val();
    }
    var userWordLessonStatistic = {
        wordLessonId: wordLessonId,
        wordId: wordId,
        userAnswer: word
    }

    $.ajax({
        url: '/word-lesson/audit/' + wordId + '/word-confirm',
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(userWordLessonStatistic),
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeader, csrfToken);
        },
        success: function (result) {
            nextSlide();
        },
        error: function () {
            let shel = {};
            alert(Boolean(shel))
        }
    });
}

$(document).on('input', 'input[name="wordConfirm"]', function () {
    var confirmButton = $(this).closest('.slide_active').find('.confirm.btn_main');

    if ($(this).val().length > 0) {
        confirmButton.prop('disabled', false);
    } else {
        confirmButton.prop('disabled', true);
    }
});


function nextSlide() {
    const slides = document.querySelector('.slider_word');
    var url = '/word-lesson/' + wordLessonId + '/word-audit-next';
    ++page;
    if (page <= totalPage) {
        $.ajax({
            url: url,
            type: "GET",
            success: function (result) {
                totalPage = result.totalPage;
                // wordLessonId = result.wordLessonId;
                addWordToSlider(result);
                currentIndex = slides.children.length - 2;
                updateSlider();
            },
            error: function () {
                console.log("Next error");
                let shel = {};
                alert(Boolean(shel))
            }
        });
    } else if (page === totalPage + 1) {
        currentIndex = slides.children.length - 1;
        updateSlider();
    } else if (page === totalPage + 2) {
        stopTimer();
        addEndSlide();
    }
}

function addEndSlide() {

    const clock = document.getElementById('timerDiv');
    clock.style.visibility = 'hidden';
    slider.innerHTML = '';
    const slide = document.createElement('div');
    slide.className = 'audit_result bb';
    slider.appendChild(slide);
    slider.style.transform = `translateX(0px)`;
    slider.style.margin = `0px`;
    $.ajax({
        url: '/word-lesson/' + wordLessonId + '/audit-result',
        type: 'GET',
        success: function (result) {
            $('.audit_result').load("/fragmentsPages/wordLessonAuditResult", function () {
                console.log(result);
                $('#audit-message').html(result.message);
                $('#total-count').html(result.totalWords);
                $('#error-count').html(result.dtoUserWordLessonStatisticErrorList.length);

                var progressBar = document.querySelector('.progress-bar');
                var progressText = document.querySelector('.progress-text');
                var percent = result.rating;
                progressBar.style.strokeDasharray = (2 * 3.1415 * 87) * (percent / 100) + ' 999';
                progressText.textContent = percent + '%';
                const container = document.getElementById('error-content');
                const template = document.querySelector('.object-container');
                result.dtoUserWordLessonStatisticErrorList.forEach(item => {
                    const clone = template.cloneNode(true); // Клонуємо блок-шаблон

                    // Заповнюємо дані з об'єкта в клонований блок
                    clone.querySelector('.right-word').textContent = item.word;
                    clone.querySelector('.user-answer').textContent = item.userAnswer;
                    clone.querySelector('.word-info').innerHTML = item.wordInfo;

                    // Додаємо клонований блок до контейнера
                    container.appendChild(clone);
                });
                document.querySelector('.object-container:first-child').style.display = 'none';

            });

        },
        error: function () {
            let shel = {};
            alert(Boolean(shel))
        }
    });

}


window.addEventListener('beforeunload', function (event) {
    console.log('Stop timer!');
    $.ajax({
        url: '/stop-timer',
        type: "GET"
    });
});

function timerPosition() {
    const pageHeight = Math.max(
        document.body.scrollHeight,
        document.documentElement.scrollHeight,
        document.body.offsetHeight,
        document.documentElement.offsetHeight,
        document.body.clientHeight,
        document.documentElement.clientHeight
    );
    const windowHeight = window.innerHeight;
    const hasScrollbar = pageHeight > windowHeight;
    const timerDiv = document.querySelector('#timerDiv');

    const userAgent = navigator.userAgent;
    if (!/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(userAgent) && windowHeight > 776) {
        if (hasScrollbar) {
            timerDiv.style.paddingRight = '15px';
            // console.log("YES!");
        }
        // else {
        //     console.log("NO!");
        // }
    }
}

function startAudit() {
    var minutes = parseFloat((totalPage / 3).toFixed(2));
    console.log(minutes);
    startTimer(minutes);
}

let timeinterval; // Глобальна змінна для ідентифікатора таймера
let remainingTime = 0; // Глобальна змінна для залишкового часу таймера
let isTimerStopped = false; // Змінна, яка вказує, чи був таймер вручну зупинений

function startTimer(minutes) {
    timerPosition();
    $.ajax({
        url: '/start-timer',
        type: "GET"
    });

    // Зупинити попередній таймер, якщо він був запущений
    stopTimer();

    // Обчислити кінцевий час для таймера
    const deadline = new Date(Date.parse(new Date()) + minutes * 60 * 1000);

    // Запустити таймер з обчисленим часом
    initializeClock(deadline);

    // Встановити таймаут, який викличе функцію onTimerEnd після закінчення таймера
    setTimeout(function () {
        if (!isTimerStopped) {
            onTimerEnd();
        }
    }, minutes * 60 * 1000);
}

// Ваша функція initializeClock (залиште її без змін)
function initializeClock(endtime) {
    const clock = document.getElementById('timerDiv');
    const minutesSpan = clock.querySelector('.minutes');
    const secondsSpan = clock.querySelector('.seconds');

    function updateClock() {
        const t = getTimeRemaining(endtime);

        minutesSpan.textContent = ('0' + t.minutes).slice(-2);
        secondsSpan.textContent = ('0' + t.seconds).slice(-2);

        remainingTime = t.total; // Оновлюємо залишковий час тут

        if (t.total <= 0) {
            clearInterval(timeinterval);
        }
    }

    updateClock();
    timeinterval = setInterval(updateClock, 1000);
}

// Ваша функція getTimeRemaining (залиште її без змін)
function getTimeRemaining(endtime) {
    const total = Date.parse(endtime) - Date.parse(new Date());
    const seconds = Math.floor((total / 1000) % 60);
    const minutes = Math.floor((total / 1000 / 60) % 60);

    return {
        total,
        minutes,
        seconds
    };
}

function stopTimer() {
    clearInterval(timeinterval);
    const clock = document.getElementById('timerDiv');
    clock.querySelector('.minutes').textContent = '00';
    clock.querySelector('.seconds').textContent = '00';
    remainingTime = 0;
    isTimerStopped = true;
}

function onTimerEnd() {
    addEndSlide();
}