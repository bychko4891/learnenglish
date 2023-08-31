var slider = document.querySelector('.slider_word');
var page = 1;
var totalPage = 4;
var currentIndex = 0;
var wordLessonId;
var currentIndexWord = 0;

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
    const slideFragment = word.wordAuditSlide;
    const slide = document.createElement('div');
    slide.className = 'slide bb';
    if(slideFragment === 'slideAuditRadios') slide.className = 'slide bb radios';
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

        if(slideFragment === 'slideAuditRadios'){
            $.ajax({
                url: '/word-lesson/' + categoryId + '/word-lesson-audit-add-words',
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
    var url = '/word-lesson/' + categoryId + '/word-audit-start';
    $.ajax({
        url: url,
        type: "GET",
        success: function (result) {
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
                        url: '/word-lesson/' + categoryId + '/word-lesson-audit-add-words',
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
    var csrfToken = $("meta[name='_csrf']").attr("content");
    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
    var wordId = $(element).closest('.block_confirm').find('input[name="id"]').val();

    const currentSlide = $(element).closest('.slide.bb.radios');
    console.log('YES');
    var word = '';
    if (currentSlide.length > 0) {
        word = currentSlide.find('input[name="group-wordSelect-' + wordId + '"]:checked').val();
        console.log(word + ' word');
    } else {
        word = $(element).closest('.block_confirm').find('input[name="wordConfirm"]').val();
    }

    // hintButton.disabled = true;
    // element.disabled = true;


    $.ajax({
        url: '/word/' + wordId + '/word-audit-confirm',
        type: "POST",
        data: {wordConfirm: word},
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
    var url = '/word-lesson/' + categoryId + '/word-audit-next';
    ++page;
    if (page < totalPage) {
        $.ajax({
            url: url,
            type: "GET",
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
    } else if (page === totalPage + 1) {
        currentIndexWord = 0;
        addEndSlide()
        currentIndex = slides.children.length - 2;
        updateSlider();
    } else if (page === totalPage + 2) {
        currentIndex = slides.children.length - 1;
        updateSlider();
    }
}