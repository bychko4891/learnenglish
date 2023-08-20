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

// $('#next').submit(function (event) {
//     const slides = document.querySelector('.slider_word');
//     event.preventDefault();
//     ++pagePrev;
//     var url = '/word-lesson/' + categoryId + '/word-next';
//     if (page + 1 === pagePrev) {
//         ++page;
//         if (page < totalPage) {
//             $.ajax({
//                 url: url,
//                 type: "GET",
//                 data: {page: page},
//                 success: function (result) {
//                     totalPage = result.totalPage;
//                     wordLessonId = result.wordLessonId;
//                     addWordToSlider(result);
//                     currentIndex = slides.children.length - 2;
//                     updateSlider();
//                 },
//                 error: function () {
//                     let shel = {};
//                     alert(Boolean(shel))
//                 }
//             });
//         } else if (page === totalPage) {
//             addEndSlide()
//             currentIndex = slides.children.length - 2;
//             updateSlider();
//         } else if (page === totalPage + 1) {
//             currentIndex = slides.children.length - 1;
//             updateSlider();
//         }
//     } else {
//         currentIndex++;
//         if (currentIndex >= slides.children.length) {
//             currentIndex = slides.children.length - 1;
//         }
//         updateSlider();
//     }
// });


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
var hint = 2;
function deleteWord(){
    const inputContainer = document.querySelector(".input-container");
    const inputs = inputContainer.querySelectorAll("input");
    if (currentIndex > 0) {
        currentIndex--;
        inputs[currentIndex].value = "";
    }
}
function hint(){

}