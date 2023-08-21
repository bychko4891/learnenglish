var slider = document.querySelector('.slider_word');
var page = 1;
var totalPage = 4;
var currentIndex = 0;
var wordLessonId;

function startSlid() {
    const mainSlide = slider.children[0];
    const secondSlide = slider.children[1];
    if (mainSlide) {
        mainSlide.classList.add('slide_active');
    }
    if (secondSlide) {
        secondSlide.classList.add('slide_no_active');
    }
}

$('#next').submit(function (event) {
    const slides = document.querySelector('.slider_word');
    event.preventDefault();
    ++pagePrev;
    var url = '/word-lesson/' + categoryId + '/word-next';
    if (page + 1 === pagePrev) {
        ++page;
        if (page < totalPage) {
            $.ajax({
                url: url,
                type: "GET",
                data: {page: page},
                success: function (result) {
                    totalPage = result.totalPage;
                    wordLessonId = result.wordLessonId;
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


function updateSlider() {
    const slide = slider.firstElementChild;
    const slideStyles = window.getComputedStyle(slide);
    const slideWidth = slide.offsetWidth + parseInt(slideStyles.marginLeft) + parseInt(slideStyles.marginRight);
    slider.style.transform = `translateX(-${currentIndex * slideWidth}px)`;

    const mainSlide = slider.children[currentIndex];

    if (mainSlide) {
        mainSlide.classList.remove('slide_no_active');
        mainSlide.classList.add('slide_active');
    }

    const currentSlide = slider.children[currentIndex - 1];
    if (currentSlide) {
        currentSlide.classList.remove('slide_active');
        currentSlide.classList.add('slide_no_active');
    }
}
function addEndSlide() {
    const slide = document.createElement('div');
    slide.className = 'slide bb slide_no_active';
    slide.id = 'endSlide';
    slider.appendChild(slide);
    $('#endSlide').load("/fragmentsPages/endSlide", function () {
    });
    currentIndex++;
    updateSlider();
}


function addWordToSlider(word) {
    const letters = word.name.split("");
    var lettersShuffle = shuffleArray(letters);
    const slide = document.createElement('div');
    slide.className = 'slide bb slide_no_active';
    slide.id = 'slide-' + word.id;
    slider.appendChild(slide);
    $.ajax({
        url: "/fragmentsPages/slide",
        method: "GET"
    }).done(function (data) {
        $('#slide-' + word.id).html(data);

        const slideDiv = $(slide);

        const lettersContainer = document.getElementsByClassName("lettersContainer");

        const nameHeading = slideDiv.find("h3");
        nameHeading.text(word.name);

        const descriptionHeading = slideDiv.find("h5");
        descriptionHeading.text('"' + word.description + '"');

        const transcriptionHeading = slideDiv.find("h6");
        transcriptionHeading.text(word.transcription);

        const audioName = slideDiv.find("source");
        audioName.attr("src", "/audio/" + word.audioName);

        const image = slideDiv.find("img:first");
        image.attr("src", "/word-image/" + word.imageName);

        const wordId = slideDiv.find("input");
        wordId.val(word.id);

        lettersShuffle.forEach(letter => {
            const button = document.createElement("button");
            button.classList.add("SpellingCard_letterKey__7BiUn", "btn", "btn-primary");
            button.type = "button";
            button.value = letter;
            button.textContent = letter;
            button.addEventListener("click", () => handleLetterClick(letter));
            lettersContainer.appendChild(button);
        });
    });
    currentIndex++;
    updateSlider();
}

// Функція для перемішування масиву
function shuffleArray(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
}
function playAudio(element) {
    var audio = element.parentNode.querySelector('.audio');
    audio.load();
    audio.play();
}


function wordsStart() {
    slider = document.querySelector('.slider_word');

    var url = '/word-lesson/' + categoryId + '/word-start';
    $.ajax({
        url: url,
        type: "GET",
        data: {size: 2},
        success: function (result) {
            const objectDivs = document.querySelectorAll(".slide");

            objectDivs.forEach((div, index) => {
                const letters = result[index].name.split("");
                const lettersShuffle = shuffleArray(letters);

                const lettersContainer = div.querySelector(".letters-container");

                const inputsContainer = div.querySelector(".inputs-container");

                const descriptionHeading = div.querySelector("h5");
                descriptionHeading.textContent = '"' + result[index].description + '"';

                const audioName = div.querySelector("source");
                audioName.src = "/audio/" + result[index].audioName;

                const image = div.querySelector("img");
                image.src = "/word-image/" + result[index].imageName;

                const wordId = div.querySelector("input[name='id']");
                wordId.value = result[index].id;

                letters.forEach(() => {
                    const input = document.createElement("input");
                    input.type = "text";
                    input.required = true;
                    input.maxLength = 1;

                    inputsContainer.appendChild(input);
                });

                lettersShuffle.forEach(letter => {
                    const button = document.createElement("button");
                    button.classList.add("btn", "btn-primary");
                    button.type = "button";
                    button.value = letter;
                    button.textContent = letter;
                    button.addEventListener("click", () => handleLetterClick(letter));
                    lettersContainer.appendChild(button);
                });
                const deleteButton = document.createElement("button");
                deleteButton.classList.add("btn", "btn-primary");
                deleteButton.type = "button";
                deleteButton.innerHTML = '<i class="fa-solid fa-delete-left"></i>';
                deleteButton.onclick = deleteWord;
                lettersContainer.appendChild(deleteButton);
            });
            startSlid();
        }
    });
}
var hintCount = 0;
let currentIndexWord = 0;
function deleteWord(){
    const inputsContainer = document.querySelector(".inputs-container");
    const inputs = inputsContainer.querySelectorAll("input");
    if (currentIndexWord > 0) {
        currentIndexWord--;
        inputs[currentIndexWord].setAttribute('value', '');
    }
}
// Обробник натискання кнопки з літерою
//Переробити!!! Зараз орієнтир йде за індексом, але з цього випливають проблеми,
// якщо користувач зненацька захоче поставити самостійно літеру, то тоді все ламається
// потрібно орієнтуватися на останній не порожній, чи якось так
function handleLetterClick(letter) {
    const inputsContainer = document.querySelector(".inputs-container");
    const inputs = inputsContainer.querySelectorAll("input");
    if (currentIndexWord < inputs.length) {
        inputs[currentIndexWord].setAttribute('value', letter);
        currentIndexWord++;
    }
}
function hint(element){
    var hintButton = element; // element - це вже кнопка, яка була передана у функцію
    var audioContainer = hintButton.closest('.block_audio_confirm').querySelector('.audio-container');
    if(hintCount === 0) {
        var inputContainer = hintButton.closest('.block_confirm').querySelector('.input-container');
        var inputsContainer = hintButton.closest('.block_confirm').querySelector('.inputs-container');
        var lettersContainer = hintButton.closest('.block_confirm').querySelector('.letters-container');

        inputContainer.classList.add('no_active');
        inputsContainer.classList.remove('no_active');
        lettersContainer.classList.remove('no_active');
        ++hintCount;
    } else if (hintCount === 1){
        hintCount = 0;
        audioContainer.classList.remove('no_active');
        hintButton.style.visibility = 'hidden';
    }
}
function confirm(element){
    const inputsContainer = element.closest('.block_confirm').querySelector('.inputs-container');
    const wordResultSuccess = element.closest('.block_confirm').querySelector('.word-result-success');
    const wordResultError = element.closest('.block_confirm').querySelector('.word-result-error');

    var valuesArray = "";

    inputsContainer.querySelectorAll("input").forEach(input => {
        const value = input.value;

        if (value !== "") {
            valuesArray += value;
        }
    });
    var csrfToken = $("meta[name='_csrf']").attr("content");
    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
    var wordId = $(element).closest('.block_confirm').find('input[name="id"]').val();
    $.ajax({
        url: '/' + wordId + '/word-confirm',
        type: "POST",
        data: {wordConfirm: valuesArray},
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeader, csrfToken);
        },
        success: function (result) {
            var status = result.status;
            if (status == "Success") {
                wordResultSuccess.innerHTML = result.info;

                // showSuccessToast(result.message);
            } else {
                wordResultError.innerHTML = result.info;
                // showErrorToast(result.message);
            }
        },
        error: function () {
            let shel = {};
            alert(Boolean(shel))
        }
    });



}