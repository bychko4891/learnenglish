$('select#entitySelect').on('change', function () {
    var selectedOption = $('option:selected', this);
    $('input[name="address"]').val(selectedOption.attr('data-page-address'));
});


function save() {
    var csrfToken = $("meta[name='_csrf']").attr("content");
    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
    var id = $('#editor input[name="id"]').val();
    var url = '/admin-page/text-of-app-page/' + id + '/edit';
    var textOfAppPage = {
        id: $('#editor input[name="id"]').val(),
        name: $('#editor input[name="name"]').val(),
        text: $('#editor textarea[name="text"]').val(),
    };
    var selectedPageApplication = {
        id: $('#entitySelect').val(),
        namePage: $('#entitySelect option:selected').text(),
        address: $('#editor input[name="address"]').val()
    };
    var data = {
        textOfAppPage: textOfAppPage,
        selectedPageApplication: selectedPageApplication
    };
    $.ajax({
        url: url,
        type: 'POST',
        contentType: "application/json",
        data: JSON.stringify(data),
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
            let shel = {};
            alert(Boolean(shel));
            showErrorToast('Помилка запиту на сервер');
        }
    });
}