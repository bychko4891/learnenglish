var slider = document.querySelector('.slider_word');
var prevButton = document.querySelector('.prev-button');
var page = 1;
var pagePrev = 1;
var totalPage = 4;
var currentIndex = 0;
// var wordLessonId;


$('#next').submit(function (event) {
    const slides = document.querySelector('.slider_word');
    event.preventDefault();
    ++pagePrev;
    var url = '/word-lesson/' + wordLessonId + '/word-next';
    if (page + 1 === pagePrev) {
        ++page;
        if (page < totalPage) {
            $.ajax({
                url: url,
                type: "GET",
                data: {page: page},
                success: function (result) {
                    totalPage = result.totalPage;
                    wordLessonId = result.wordLesson.id;
                    addWordToSlider(result);
                    currentIndex = slides.children.length - 2;
                    updateSlider();
                },
                error: function () {
                    let shel = {};
                    alert(Boolean(shel))
                }
            });
        } else if (page === totalPage) {
            addEndSlide()
            currentIndex = slides.children.length - 2;
            updateSlider();
        } else if (page === totalPage + 1) {
            currentIndex = slides.children.length - 1;
            updateSlider();
        }
    } else {
        currentIndex++;
        if (currentIndex >= slides.children.length) {
            currentIndex = slides.children.length - 1;
        }
        updateSlider();
    }
});


prevButton.addEventListener('click', () => {
    if (pagePrev > 1) {
        pagePrev--;
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = 0;
        }
        updateSlider();
    }
});

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
function addEndSlide() {
    const slide = document.createElement('div');
    slide.className = 'slide bb';
    slide.id = 'endSlide';
    slider.appendChild(slide);
    $('#endSlide').load("/fragmentsPages/endSlide", function () {
    });
    currentIndex++;
    updateSlider();
}


function addWordToSlider(result) {
    // console.log(result);
    var vocabularyPage = result.vocabularyPage;
    // console.log(vocabularyPage);

    const slide = document.createElement('div');
    slide.className = 'slide bb';
    slide.id = 'slide-' + vocabularyPage.id;
    slider.appendChild(slide);
    $.ajax({
        url: "/fragmentsPages/slideReview",
        method: "GET"
    }).done(function (data) {
        $('#slide-' + vocabularyPage.id).html(data);

        const slideDiv = $(slide);

        const nameHeading = slideDiv.find("h3");
        nameHeading.text(vocabularyPage.name);

        const descriptionHeading = slideDiv.find("h5");
        descriptionHeading.text(vocabularyPage.cardInfo);

        const transcriptionHeading = slideDiv.find("h6");
        transcriptionHeading.text(vocabularyPage.word.usaTranscription);

        const audioName = slideDiv.find("source");
        audioName.attr("src", "/audio/" + vocabularyPage.word.audio.usaAudioName);

        const image = slideDiv.find("img:first");
        image.attr("src", "/vocabulary-page/" + vocabularyPage.image.imageName);

        const wordId = slideDiv.find("input");
        wordId.val(vocabularyPage.id);

    });
    currentIndex++;
    updateSlider();
}

function playAudio(element) {
    var audio = element.parentNode.querySelector('.audio');
    audio.load();
    audio.play();
}


function wordsStart() {
    slider = document.querySelector('.slider_word');
    var url = '/word-lesson/' + wordLessonId + '/word-start';
    $.ajax({
        url: url,
        type: "GET",
        success: function (result) {

            const objectDivs = document.querySelectorAll(".slide");

            objectDivs.forEach((div, index) => {
                const nameHeading = div.querySelector("h3");
                nameHeading.textContent = result[index].vocabularyPage.name;

                const descriptionHeading = div.querySelector("h5");
                descriptionHeading.textContent = result[index].vocabularyPage.cardInfo;

                const transcriptionHeading = div.querySelector("h6");
                transcriptionHeading.textContent = result[index].vocabularyPage.word.usaTranscription;

                const audioName = div.querySelector("source");
                audioName.src = "/audio/" + result[index].vocabularyPage.word.audio.usaAudioName;

                const image = div.querySelector("img");
                image.width = 270;
                image.height = 270;
                image.src = "/vocabulary-page/" + result[index].vocabularyPage.image.imageName;

                const wordId = div.querySelector("input");
                wordId.value = result[index].id;

            });
        }
    });
}
function wordPlus(event, element) {
    event.preventDefault();
    var wordId = element.closest('.formWordPlus').querySelector('input[name="wordId"]').value;
    var csrfToken = $("meta[name='_csrf']").attr("content");
    var csrfHeader = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        url: '/user/word-plus',
        type: "POST",
        data: {wordId: wordId},
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeader, csrfToken);
        },
        success: function (result) {
            var status = result.status;
            if (status == "Success") {
                showSuccessToast(result.message);
            } else {
                showErrorToast(result.message);
            }
        },
        error: function () {
            showErrorToast("Помилка сервера");
        }
    });
// });
}