var categoryId = $('input[name="categoryId"]').val();
$(document).ready(function () {
    switchTab('tab1')
});

function switchButton(tabId) {

    // Забираємо активний клас від усіх кнопок
    $('.word_tabs li button').removeClass('active');

    // Вмикаємо вибрану кнопку
    $('#' + tabId + '-btn').addClass('active');

    // Вимикаємо всі radio-кнопки
    $('input[name="tab-control"]').prop('checked', false);

    // Вмикаємо потрібний radio-кнопку, яка відповідає вибраній вкладці
    $('#' + tabId).prop('checked', true);

    // Оновлення стилів слайдера при активній вкладці
    var slider = $('.slider');
    var activeTab = $('.word_tabs li button.active');
    var index = activeTab.parent().index();

    var translationValue = index * 100; // Вираховуємо значення трансформації для слайдера

    slider.css('transform', 'translateX(' + translationValue + '%)');
}

function switchTab(tabId) {
    const tab1 = document.getElementById('content-tab1');
    const tab2 = document.getElementById('content-tab2');
    const tab3 = document.getElementById('content-tab3');
    const activeTabContent = document.getElementById('content-' + tabId);
    if (tabId.trim() !== "") {
        activeTabContent.classList.add('active');
        if(tabId === 'tab2'){
            $.ajax({
                url: "/fragmentsPages/wordLessonSpelling",
                method: "GET"
            }).done(function (data) {
                $('#content-tab2').html(data); // Замінити вміст елемента з завантаженими даними
                wordsStart(); // Виклик функції
            });


            tab1.innerHTML = '';
            tab3.innerHTML = '';
        }else if(tabId === 'tab3'){
            tab1.innerHTML = '';
            tab2.innerHTML = '';
        }else {
            $('#content-tab1').load("/fragmentsPages/wordLessonReview", function () {
                wordsStart();
            });
            tab2.innerHTML = '';
            tab3.innerHTML = '';
        }
    }
    // Викликаємо функцію для перемикання табів, яка забезпечить оновлення слайдера
    switchButton(tabId);

}