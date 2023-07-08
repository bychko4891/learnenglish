var formData;
$(document).ready(function () {
    tinymce.init({
        selector: 'textarea#info',
        convert_urls : false,
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

    $('#editor').submit(function (event) {
        event.preventDefault();
        formData = $(this).serializeArray();
        save();
    });

});