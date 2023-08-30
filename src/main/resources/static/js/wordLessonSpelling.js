var slider = document.querySelector('.slider_word');
var page = 1;
var totalPage = 4;
var currentIndex = 0;
var wordLessonId;
var hintCount = 0;
var currentIndexWord = 0;

// var activeSlide;



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
    $('#endSlide').load("/fragmentsPages/endSlideSpelling", function () {
    });
    currentIndex++;
    updateSlider();
}


function addWordToSlider(word) {
    const slide = document.createElement('div');
    slide.className = 'slide bb';
    slider.append(slide);
    const slideDiv = $(slide);
    $.ajax({
        url: "/fragmentsPages/slideSpelling",
        method: "GET"
    }).done(function (data) {
        slideDiv.html(data);
        const letters = word.name.split("");
        const lettersShuffle = shuffleArray(letters);

        const lettersContainer = slideDiv.find(".letters-container");
        const inputsContainer = slideDiv.find(".inputs-container");

        const nameHeading = slideDiv.find("h3");
        nameHeading.text(word.name);

        const descriptionHeading = slideDiv.find("h5");
        descriptionHeading.text('"' + word.description + '"');

        const audioName = slideDiv.find("source");
        audioName.attr("src", "/audio/" + word.audioName);

        const image = slideDiv.find("img:first");
        image.attr("src", "/word-image/" + word.imageName);

        const wordId = slideDiv.find("input[name='id']");
        wordId.val(word.id);

        letters.forEach(() => {
            const input = document.createElement("input");
            input.type = "text";
            input.required = true;
            input.maxLength = 1;
            inputsContainer.append(input);
        });

        lettersShuffle.forEach(letter => {
            const button = document.createElement("button");
            button.classList.add("btn", "btn-primary");
            button.type = "button";
            button.value = letter;
            button.textContent = letter;
            button.addEventListener("click", () => handleLetterClick(letter));
            lettersContainer.append(button);
        });
        const deleteButton = document.createElement("button");
        deleteButton.classList.add("btn", "btn-primary");
        deleteButton.type = "button";
        deleteButton.innerHTML = '<i class="fa-solid fa-delete-left"></i>';
        deleteButton.onclick = deleteWord;
        lettersContainer.append(deleteButton);
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


function wordsStart() {
    slider = document.querySelector('.slider_word');
    var url = '/word-lesson/' + categoryId + '/word-start';
    $.ajax({
        url: url,
        type: "GET",
        // data: {size: 2},
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
        }
    });
}



function deleteWord() {
    const activeSlide = document.querySelector('.slide_active');
    const inputsContainer = activeSlide.querySelector(".inputs-container");
    const inputs = inputsContainer.querySelectorAll("input");
    if (currentIndexWord > 0) {
        currentIndexWord--;
        inputs[currentIndexWord].setAttribute('value', '');
    }
    if (currentIndexWord === 0) {
        var confirmButton = activeSlide.querySelector('.confirm.btn_main');
        confirmButton.disabled = true;
    }
}

// Обробник натискання кнопки з літерою
//Переробити!!! Зараз орієнтир йде за індексом, але з цього випливають проблеми,
// якщо користувач зненацька захоче поставити самостійно літеру, то тоді все ламається
// потрібно орієнтуватися на останній не порожній, чи якось так
function handleLetterClick(letter) {
    const activeSlide = document.querySelector('.slide_active');
    const inputsContainer = activeSlide.querySelector(".inputs-container");
    const inputs = inputsContainer.querySelectorAll("input");
    // const inputsContainer = document.querySelector(".inputs-container");
    // const inputs = inputsContainer.querySelectorAll("input");
    if (currentIndexWord < inputs.length) {
        inputs[currentIndexWord].setAttribute('value', letter);
        currentIndexWord++;
    }
    if (currentIndexWord > 0) {
        var confirmButton = activeSlide.querySelector('.confirm.btn_main');
        confirmButton.disabled = false;
    }
}

function hint(element) {
    var hintButton = element; // element - це вже кнопка, яка була передана у функцію
    var audioContainer = hintButton.closest('.block_audio_confirm').querySelector('.audio-container');
    if (hintCount === 0) {
        var inputContainer = hintButton.closest('.block_confirm').querySelector('.input-container');
        var inputsContainer = hintButton.closest('.block_confirm').querySelector('.inputs-container');
        var lettersContainer = hintButton.closest('.block_confirm').querySelector('.letters-container');

        inputContainer.classList.remove('active');
        inputsContainer.classList.add('active');
        lettersContainer.classList.add('active');
        ++hintCount;
    } else if (hintCount === 1) {
        hintCount = 0;
        audioContainer.classList.add('active');
        hintButton.classList.remove('active');
    }
}

function confirm(element) {
    const inputsContainer = element.closest('.block_confirm').querySelector('.inputs-container');
    const wordResultSuccess = element.closest('.block_confirm').querySelector('.word-result-success');
    const wordResultError = element.closest('.block_confirm').querySelector('.word-result-error');
    var lettersContainer = element.closest('.block_confirm').querySelector('.letters-container');
    var nextSlide = element.closest('.block_confirm').querySelector('.next-slide');
    var hintButton = element.closest('.block_confirm').querySelector('.hint_button');
    var csrfToken = $("meta[name='_csrf']").attr("content");
    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
    var wordId = $(element).closest('.block_confirm').find('input[name="id"]').val();
    hintButton.disabled = true;
    element.disabled = true;

    var wordConfirm = "";
    if (hintCount === 0) {
        wordConfirm = $(element).closest('.block_confirm').find('input[name="wordConfirm"]').val();
    } else {
        lettersContainer.classList.remove('active');
        inputsContainer.querySelectorAll("input").forEach(input => {
            const value = input.value;
            if (value !== "") {
                wordConfirm += value;
            }
        });
    }
    $.ajax({
        url: '/' + wordId + '/word-confirm',
        type: "POST",
        data: {wordConfirm: wordConfirm},
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeader, csrfToken);
        },
        success: function (result) {
            nextSlide.classList.add('active');
            var status = result.status;
            if (status == "Success") {
                wordResultSuccess.innerHTML = result.info;
            } else {
                wordResultError.innerHTML = result.info;
            }
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


function nextSlide(event) {
    hintCount = 0;
    const slides = document.querySelector('.slider_word');
    // activeSlide = document.querySelector('.slide_active');
    event.preventDefault();
    ++pagePrev;
    var url = '/word-lesson/' + categoryId + '/word-next';
    ++page;
    if (page < totalPage) {
        $.ajax({
            url: url,
            type: "GET",
            data: {page: page},
            success: function (result) {
                currentIndexWord = 0;
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
        currentIndexWord = 0;
        addEndSlide()
        currentIndex = slides.children.length - 2;
        updateSlider();
    } else if (page === totalPage + 1) {
        currentIndex = slides.children.length - 1;
        updateSlider();
    }
}

function playAudio(element) {
    var audio = element.parentNode.querySelector('.audio');
    audio.load();
    audio.play();
}

/////////////////////////////////////////////////////////////// <input type="text" class="single-char-input" maxlength="1">
// const singleCharInputs = document.querySelectorAll(".single-char-input");
//
// singleCharInputs.forEach(input => {
//     input.addEventListener("input", function () {
//         if (this.value.length > 1) {
//             this.value = this.value[this.value.length - 1];
//         }
//     });
// });