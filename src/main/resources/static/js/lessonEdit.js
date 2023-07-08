    function save() {
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        var jsonFormData = {};
        $(formData).each(function (index, obj) {
            jsonFormData[obj.name] = obj.value;
        });
        $.ajax({
            url: '/admin-page/lesson-save',
            type: 'POST',
            contentType: "application/json",
            data: JSON.stringify(jsonFormData),
            beforeSend: function (xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function (result) {
                var status = result.status;
                if (status == "Success") {
                    showSuccessToast(result.message);
                } else {
                    showErrorToast(result.message)
                }
            },
            error: function () {
                alert('Помилка при відправці запиту');
            }
        });
    }