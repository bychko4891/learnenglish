var csrfToken = $("meta[name='_csrf']").attr("content");
var csrfHeader = $("meta[name='_csrf_header']").attr("content");
var formData;
$(document).ready(function () {
    tinymce.init({
        selector: 'textarea#info',
        convert_urls : false,
        height: 500,
        plugins: [
            'advlist','imagetools', 'autolink', 'lists', 'link', 'image', 'charmap', 'preview',
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
        image_title: true,
        automatic_uploads: true,
        file_picker_types: 'image',
        imagetools_toolbar: 'alignleft aligncenter alignright alignnone',
        api_key: 'j8dxs8puyiugoamq11vn3bctaonh1jhzvd0cewcb1jiyl2c6',
        menubar: true,
        statusbar: true,
        branding: true,
        file_picker_callback: function (callback, value, meta) {
            var input = document.createElement('input');
            input.setAttribute('type', 'file');
            input.setAttribute('accept', 'image/*');
            input.onchange = function () {
                var file = this.files[0];
                var formData = new FormData();

                formData.append('imageFile', file);

                $.ajax({
                    url: '/admin/web-image/upload',
                    type: 'POST',
                    data: formData,
                    processData: false,
                    contentType: false,
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(csrfHeader, csrfToken);
                    },
                    success: function (imageUrl) {
                        // Викликати callback з отриманим URL зображення
                        callback(imageUrl, { title: file.name });
                    },
                    error: function (xhr, status, error) {
                        // Обробка помилки
                    }
                });
            };

            input.click();
        },
        images_upload_handler: function (blobInfo, success, failure) {
            var xhr, formData;

            xhr = new XMLHttpRequest();
            xhr.withCredentials = false;
            xhr.open('POST', '/admin/web-image/upload');
            xhr.setRequestHeader(csrfHeader, csrfToken);

            xhr.onload = function () {
                var imageUrl = xhr.responseText;
                console.log(imageUrl);
                if (xhr.status === 200 && imageUrl) {
                    success(imageUrl);
                } else {
                    failure('Помилка завантаження зображення');
                }
            };

            xhr.onerror = function () {
                failure('Помилка завантаження зображення');
            };

            formData = new FormData();
            formData.append('imageFile', blobInfo.blob(), blobInfo.filename());

            xhr.send(formData);
        }
    // });
    });

    $('#editor').submit(function (event) {
        event.preventDefault();
        formData = $(this).serializeArray();
        save();
    });

});