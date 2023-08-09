$('select#entitySelect').on('change', function () {
    var selectedOption = $('option:selected', this);
    $('input[name="address"]').val(selectedOption.attr('data-page-address'));
});


    function save() {
        // event.preventDefault();
        var resultDivSuccess = $('#resultDivSuccess');
        var resultDivError = $('#resultDivError');
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        var id = $('#editor input[name="id"]').val();
        var url = '/admin-page/text-of-app-page/' + id + '/edit';
        var textOfAppPage = {
            id: $('#editor input[name="id"]').val(),
            name: $('#editor input[name="name"]').val(),
            text: $('#editor textarea[name="text"]').val(),
            // pageApplication: $('#editor input[name="pageApplication"]').val()
        };
        var selectedPageApplication = {
            id: $('#entitySelect').val(),
            namePage: $('#entitySelect option:selected').text(),
            // address: $('#entitySelect option:selected').data('page-address'),
            address: $('#editor input[name="address"]').val()
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
                var status = result.status;
                if (status == "Success") {
                    showSuccessToast(result.message)
                    // showAlert(message, "success");
                    // Отримуємо div-елемент, в який ми будемо поміщати повідомлення
                    resultDivSuccess.text(result.message);
                    setTimeout(hideMessageSuccess, 5000);
                } else {
                    showErrorToast(result.message)
                    // showAlert(message, "error");
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
    }
// });
