//Заповнення сторінки першими данними при старті Lesson
$(document).ready(function () {
    // Виконуємо запит при першій загрузці сторінки
    var url = $('#reload').attr('action');
    var csrfToken = $("input[name='_csrf']").val();
    $.ajax({
        type: "GET",
        url: url,
        data: $('#reload').serialize(),
        beforeSend: function (xhr) {
            xhr.setRequestHeader("X-CSRF-TOKEN", csrfToken);
        },
        success: function (result) {
            // console.log(result);
            $('.content_block').hide();
            $('#ukr-text').html(result.ukrText);
            $('#english-text').html(result.engText);
        },
        error: function () {
            // console.log('Помилка запиту на сервер');
        }
    });
// **********  Виконуємо запит при надсиланні запита користувачем Lesson *************** //
    $('#reload').submit(function (e) {
        e.preventDefault();
        var data = $(this).serialize();
        var url = $(this).attr('action');
        var csrfToken = $("input[name='_csrf']").val();
        $.ajax({
            type: "GET",
            url: url,
            data: data,
            beforeSend: function (xhr) {
                xhr.setRequestHeader("X-CSRF-TOKEN", csrfToken);
            },
            success: function (result) {
                // console.log(result);
                $('.content_block').hide();
                $('#ukr-text').html(result.ukrText);
                $('#english-text').html(result.engText);
            },
            error: function () {
                // console.log('Помилка запиту на сервер');
            }
        });
    });
// ***************** Кнопка, щоб відкрити скритий текст *************** //
    $('.content_toggle').click(function () {
        $('.content_block').slideToggle(600);
        return false;
    });
});

// ************   Додавання тексту в базу   *************** //
$(document).ready(function () {
    var resultDivSuccess = $('#result-success');
    var resultDivError = $('#result-error');
    $('#add-pair').submit(function (event) {
        event.preventDefault();
        // Get CSRF token from the meta tag-->
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        var url = $(this).attr('action');
        var formData = $(this).serializeArray();

        if ($('textarea[name="ukrText"]').val() && $('textarea[name="engText"]').val()) {
            var ukrTextTemp = $('textarea[name="ukrText"]').val();
            var engTextTemp = $('textarea[name="engText"]').val();
            if (ukrTextTemp.length > 300 || engTextTemp.length > 300) {
                alert("Вибачте, але дозволено довжину речення максимум 300 символів разом з пропусками!!!");
                return;
            }
            var jsonFormData = {};
            $(formData).each(function (index, obj) {
                jsonFormData[obj.name] = obj.value;
            });
            $.ajax({
                url: url,
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(jsonFormData),
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                },
                success: function (result) {
                    var status = result.status;
                    // console.log(status);
                    if (status == "Success") {
                        $('textarea[name="ukrText"]').val('');
                        $('textarea[name="engText"]').val('');
                        // Отримуємо div-елемент, в який ми будемо поміщати повідомлення
                        resultDivSuccess.text(result.message);
                        setTimeout(hideMessageSuccess, 5000);
                    } else {
                        resultDivError.text(result.message);
                        setTimeout(hideMessageError, 10000);
                    }
                },
                error: function () {
                    let shel = {};
                    alert(Boolean(shel))
                    // Поміщаємо повідомлення про помилку в div-елемент
                    resultDivError.text('Помилка запиту на сервер');
                }
            });
        } else {
            // якщо не всі поля заповнені, не виконуємо запит на сервер і виводимо помилку
            alert('Будь ласка, заповніть поле вводу');
            return;
        }
    });

    function hideMessageSuccess() {
        resultDivSuccess.text('');

    }

    function hideMessageError() {

        resultDivError.text('');
    }
});

//********  авто висота вікна для вводу тексту *************** //
$(document).ready(function () {
    $('textarea').on('input', function () {
        this.style.height = 'auto';
        this.style.height = (this.scrollHeight) + 'px';
    });
});





$(document).ready(function () {
    // var resultDivSuccess = $('#password-edit-success');
    // var resultDivError = $('#password-edit-error');
    $('#upload2').submit(function (event) {
        event.preventDefault();
        // Get CSRF token from the meta tag-->
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        // var url = $(this).attr('action');
        // var formData = $(this).serializeArray();
        var formData = new FormData($('#upload2')[0]);
        // if ($('input[name="password"]').val() && $('input[name="newPassword"]').val()) {
        //     console.log(formData);
        $.ajax({
            url: "/upload",
            type: "post",
            data: formData,
            processData: false,
            contentType: false,
            // enctype: "multipart/form-data",
            // types: "multipart/form-data",
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (result) {
                console.log(result);
                // var status = result.status;
                // console.log(status);
                // if (status == "Success") {
                //     $('input[name="password"]').val('');
                //     $('input[name="newPassword"]').val('');
                //     // Отримуємо div-елемент, в який ми будемо поміщати повідомлення
                //     resultDivSuccess.text(result.message);
                //     setTimeout(hideMessageSuccess, 5000);
                // } else {
                //     resultDivError.text(result.message);
                //     setTimeout(hideMessageError, 5000);
                // }
            },
            error: function () {
                let shel = {};
                alert(Boolean(shel))
                // Поміщаємо повідомлення про помилку в div-елемент
                resultDivError.text('Помилка запиту на сервер');
            }
        });
        // } else {
        //     // якщо не всі поля заповнені, не виконуємо запит на сервер і виводимо помилку
        //     alert('Будь ласка, заповніть поле вводу');
        //     return;
        // }
    });

    // function hideMessageSuccess() {
    //     resultDivSuccess.text('');
    // }

    // function hideMessageError() {
    //     resultDivError.text('');
    // }
});


////////////////////////////////////////////////////////////////////////////////////////////////


window.onload = function () {


}