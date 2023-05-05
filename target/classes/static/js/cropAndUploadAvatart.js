//********  Croppie and upload avatar START *************** //
$(document).ready(function () {
    var $uploadCrop = $('#upload-demo').croppie({
        viewport: {
            width: 200,
            height: 200,
            type: 'circle'
        },
        enableExif: true
    });

    $('#upload').on('change', function () {
        var reader = new FileReader();
        reader.onload = function (e) {
            $uploadCrop.croppie('bind', {
                url: e.target.result
            }).then(function () {
                console.log('jQuery bind complete');
            });
        }
        reader.readAsDataURL(this.files[0]);
    });
    function avatarAdd () {
        $uploadCrop.croppie('result', {
            type: 'canvas',
            size: 'viewport'
        }).then(function (resp) {
            var img = document.getElementById('userAvatar');
            img.src = resp;
        }); }

    $('#crop').on('click', function () {
        $uploadCrop.croppie('result', {
            type: 'blob',
            size: 'viewport'
        }).then(function (blob) {
            avatarAdd();
            var formData = new FormData();
            formData.append('file', blob, 'image.png');
            // var userId = window.location.href.split('/').pop();
            var url = window.location.href;
            var userId = url.match(/user\/(\d+)/)[1];
            url = '/user/' + userId + '/upload-avatar';
            var csrfToken = $("meta[name='_csrf']").attr("content");
            var csrfHeader = $("meta[name='_csrf_header']").attr("content");
            $.ajax({
                type: 'POST',
                url: url,
                data: formData,
                processData: false,
                contentType: false,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                },
                success: function (data) {
                    console.log(data);
                },
                error: function (xhr, textStatus, errorThrown) {
                    console.log(xhr.responseText);
                }
            });
        });
    });
});

//********  Croppie and upload avatar END *************** //