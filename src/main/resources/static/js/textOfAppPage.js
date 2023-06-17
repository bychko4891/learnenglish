$('select#entitySelect').on('change', function () {
    var selectedOption = $('option:selected', this);
    $('input[name="address"]').val(selectedOption.attr('data-page-address'));
});
$(document).ready(function () {
    tinymce.init({
        selector: 'textarea#lessonInfo',
        height: 500,
        plugins: [
            'advlist', 'autolink', 'lists', 'link', 'image', 'charmap', 'preview',
            'anchor', 'searchreplace', 'visualblocks', 'code', 'fullscreen',
            'insertdatetime', 'media', 'table', 'help', 'wordcount'
        ],
        toolbar:
            'undo redo | formatselect |' +
            ' bold italic underline forecolor | link image media table mergetags |' +
            ' addcomment showcomments | spellcheckdialog code typography |' +
            ' align lineheight | checklist numlist bullist indent outdent |' +
            ' emoticons charmap | removeformat'
        ,
        content_css: [
            '//fonts.googleapis.com/css?family=Lato:300,300i,400,400i',
            '//www.tiny.cloud/css/codepen.min.css'
        ],
        api_key: 'j8dxs8puyiugoamq11vn3bctaonh1jhzvd0cewcb1jiyl2c6',
        menubar: true,
        statusbar: true,
        branding: true
    });
    $('#textOfAppPage').submit(function (event) {
        event.preventDefault();
        var resultDivSuccess = $('#resultDivSuccess');
        var resultDivError = $('#resultDivError');
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        var id = $('#textOfAppPage input[name="id"]').val();
        var url = '/admin-page/text-of-app-page/' + id + '/edit';
        var textOfAppPage = {
            id: $('#textOfAppPage input[name="id"]').val(),
            name: $('#textOfAppPage input[name="name"]').val(),
            text: $('#textOfAppPage textarea[name="text"]').val()
        };
        var selectedPageApplication = {
            id: $('#entitySelect').val(),
            namePage: $('#entitySelect option:selected').text(),
            // address: $('#entitySelect option:selected').data('page-address')
            address: $('#textOfAppPage input[name="address"]').val()
        };
        var data = {
            textOfAppPage: textOfAppPage,
            selectedPageApplication: selectedPageApplication
        };
        $.ajax({
            // url: $(this).attr('action'),
            url: url,
            type: 'POST',
            contentType: "application/json",
            data: JSON.stringify(data),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (result) {
                console.log(result);
                var status = result.status;
                console.log(status);
                if (status == "Success") {
                    // Отримуємо div-елемент, в який ми будемо поміщати повідомлення
                    resultDivSuccess.text(result.message);
                    setTimeout(hideMessageSuccess, 5000);
                } else {
                    resultDivError.text(result.message);
                    setTimeout(hideMessageError, 5000);
                }
            },
            error: function () {
                let shel = {};
                alert(Boolean(shel))
                // Поміщаємо повідомлення про помилку в div-елемент
                resultDivError.text('Помилка запиту на сервер');
            }
        });

        function hideMessageSuccess() {
            resultDivSuccess.text('');
        }

        function hideMessageError() {
            resultDivError.text('');
        }
    });
});
