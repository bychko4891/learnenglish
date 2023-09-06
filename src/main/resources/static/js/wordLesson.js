var wordLessonId = $('input[name="wordLessonId"]').val();
$(document).ready(function () {
    switchTab('tab1')
});

function switchButton(tabId) {

    $('.word_tabs li button').removeClass('active');

    $('#' + tabId + '-btn').addClass('active');

    $('input[name="tab-control"]').prop('checked', false);

    $('#' + tabId).prop('checked', true);

    var slider = $('.slider');
    var activeTab = $('.word_tabs li button.active');
    var index = activeTab.parent().index();

    var translationValue = index * 100;

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
                $('#content-tab2').html(data);
                wordsStart();
            });
            tab1.innerHTML = '';
            tab3.innerHTML = '';
        }else if(tabId === 'tab3'){
            $.ajax({
                url: "/fragmentsPages/wordLessonAudit",
                method: "GET"
            }).done(function (data) {
                $('#content-tab3').html(data);
                wordsStart();
            });
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
    switchButton(tabId);

}