$(document).ready(function () {
    tinymce.init({
        selector: 'textarea#lessonInfo',
        height: 500,
        plugins: [
            'anchor autolink charmap codesample emoticons link lists media ',
            'searchreplace table visualblocks wordcount '


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
        menubar: false,
        statusbar: false,
        branding: false
    });
    $('#myForm').submit(function (event) {
        event.preventDefault(); // Заборонити звичайну поведінку форми (наприклад, перезавантаження сторінки)
        // Відправити дані форми на сервер
        $.ajax({
            url: $(this).attr('action'), // URL для відправки запиту
            type: $(this).attr('method'), // Метод запиту (POST)
            data: $(this).serialize(), // Дані форми
            success: function (response) {
                $('#responseDiv').text(response);
            },
            error: function () {
                // Обробка помилки, якщо відповідь сервера не отримана або є помилка
                alert('Помилка при відправці запиту');
            }
        });
    });
});