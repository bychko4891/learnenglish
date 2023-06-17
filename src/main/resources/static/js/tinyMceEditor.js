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
    $('#myForm').submit(function (event) {
        event.preventDefault(); // Заборонити звичайну поведінку форми (наприклад, перезавантаження сторінки)
        // Відправити дані форми на сервер
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        var formData = $(this).serializeArray();
        var jsonFormData = {};
        $(formData).each(function (index, obj) {
            jsonFormData[obj.name] = obj.value;
        });
        console.log(jsonFormData);
        $.ajax({
            url: $(this).attr('action'), // URL для відправки запиту
            type: $(this).attr('method'), // Метод запиту (POST)
            contentType: "application/json",
            data: JSON.stringify(jsonFormData),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
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