var slider = document.querySelector('.slider_word');
var prevButton = document.querySelector('.prev-button');
var page = 1;
var pagePrev = 1;
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
    const slide = document.createElement('div');
    slide.className = 'slide bb slide_no_active';
    slider.appendChild(slide);
    $.ajax({
        url: "/fragmentsPages/slide",
        method: "GET"
    }).done(function (data) {
        $('.slide').html(data);

        const slideDiv = $(slide);

        const nameHeading = slideDiv.find("h3");
        nameHeading.text(word.name);

        const descriptionHeading = slideDiv.find("h5");
        descriptionHeading.text(word.description);

        const audioName = slideDiv.find("source");
        audioName.attr("src", "/audio/" + word.audioName);

        const image = slideDiv.find("img:first");
        image.attr("src", "/word-image/" + word.imageName);

        const wordId = slideDiv.find("input");
        wordId.val(word.id);
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

    var url = '/word-lesson/' + categoryId + '/word-start';
    $.ajax({
        url: url,
        type: "GET",
        data: {size: 2},
        success: function (result) {
            const objectDivs = document.querySelectorAll(".slide");

            objectDivs.forEach((div, index) => {
                const nameHeading = div.querySelector("h3");
                nameHeading.textContent = result[index].name;

                const descriptionHeading = div.querySelector("h5");
                descriptionHeading.textContent = result[index].description;

                const audioName = div.querySelector("source");
                audioName.src = "/audio/" + result[index].audioName;

                const image = div.querySelector("img");
                image.src = "/word-image/" + result[index].imageName;

                const wordId = div.querySelector("input");
                wordId.value = result[index].id;
            });
            startSlid();
        }
    });
}