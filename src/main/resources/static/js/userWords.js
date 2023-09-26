var csrfToken = $("meta[name='_csrf']").attr("content");
var csrfHeader = $("meta[name='_csrf_header']").attr("content");

$(document).ready(function () {
    $('.toggle').click(function () {
        $(this).siblings('.category').slideToggle().toggleClass('visible');
    });
});


function confirmRemove(element) {
    var confirmation = confirm("Ви впевнені, що хочете видалити цю фразу?");
    if (confirmation) {
        deleteChanges(element);
    }
}

function deleteChanges(element) {
    var wordId = element.closest('.user-word').querySelector('input[name="wordId"]').value;
    $.ajax({
        url: '/user-word/remove',
        type: 'POST',
        data: {wordId: wordId},
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeader, csrfToken);
        },
        success: function (result) {
            var status = result.status;
            if (status === 'Success') {
                showSuccessToast(result.message);
                setTimeout(function () {
                    location.reload();
                }, 3000);
            } else {
                showErrorToast(result.message);
            }

        }
    });

}

$('.toggle-switch').on('change', function() {
    var isRepeatable = $(this).prop('checked');
    var wordId = $(this).data('word-id');
    $.ajax({
        url: '/word/repetition-word-check',
        type: 'POST',
        data: {isRepeatable: isRepeatable,
            wordId: wordId},
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeader, csrfToken);
        },
        success: function (result) {
            var status = result.status;
            if (status === "Success") {
                showSuccessToast(result.message);
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