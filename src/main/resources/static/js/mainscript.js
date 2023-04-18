$(document).ready(function () {
    $(function () {
        $('#reload').submit(function (e) {
            e.preventDefault();
            var data = $(this).serialize();
            // var url = $(this).attr('action');
            var url = $('#reload').attr('action');
            // get the CSRF token value from the hidden input field
            var csrfToken = $("input[name='_csrf']").val();

            // get the lessonId value from the hidden input field
            // var lessonId = $("input[name='lessonId']").val();
            // var url = $('#reload').attr('action');
            $.ajax({
                type: "GET",
                url: url,
                data: data,
                // dataType: "html", // Додатковий параметр
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("X-CSRF-TOKEN", csrfToken);
                },
                success: function (result) {
                    console.log(result);
                    $('.content_block').hide();
                    $('#ukr-text').html(result.ukrText);
                    $('#english-text').html(result.engText);
                }
                // ,
                // error: function () {
                //     let shel = {};
                //     alert(Boolean(shel))
                //     // Поміщаємо повідомлення про помилку в div-елемент
                //     resultDivError.text('Помилка запиту на сервер');
                // }
            });

        });
    });
});