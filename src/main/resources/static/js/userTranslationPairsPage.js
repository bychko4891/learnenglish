function toggleEditMode(button) {
    var row = button.closest('tr');
    var spans = row.getElementsByTagName('span');
    var inputs = row.getElementsByTagName('input');

    for (var i = 1; i < spans.length; i++) {
        spans[i].classList.toggle('hidden_content');
    }
    for (var j = 0; j < inputs.length; j++) {
        inputs[j].classList.toggle('hidden_content');
    }

    var editButton = row.querySelector('.editButton');
    var saveButton = row.querySelector('.saveButton');
    var cancelButton = row.querySelector('.cancelButton');
    saveButton.classList.remove('disabled');
    editButton.classList.add('disabled');
    cancelButton.classList.remove('disabled');
}

function toggleCancelMode(button) {
    var row = button.closest('tr');
    var spans = row.getElementsByTagName('span');
    var inputs = row.getElementsByTagName('input');


    for (var i = 1; i < spans.length; i++) {
        spans[i].classList.toggle('hidden_content');
    }
    for (var j = 0; j < inputs.length; j++) {
        inputs[j].classList.toggle('hidden_content');
    }

    var editButton = row.querySelector('.editButton');
    var saveButton = row.querySelector('.saveButton');
    var cancelButton = row.querySelector('.cancelButton');
    saveButton.classList.add('disabled');
    editButton.classList.remove('disabled');
    cancelButton.classList.add('disabled');
}

function saveChanges(button) {
    var row = button.parentNode.parentNode;
    var inputs = row.getElementsByTagName('input');
    var spans = row.getElementsByTagName('span');


    var updatedData = {
        id: inputs[0].value,
        ukrText: inputs[1].value,
        engText: inputs[2].value
    };
    var id = updatedData.id;
    var csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
    var csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute("content");
    $.ajax({
        // url: $(this).attr('action'),
        url: '/translation-pair/check-edit',
        type: 'POST',
        contentType: "application/json",
        data: JSON.stringify(updatedData),
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeader, csrfToken);
        },
        success: function (result) {
            if (result.hasOwnProperty('status') && result.status === 'Error') {
                showErrorToast(result.message);
            } else {
                spans[1].textContent = result.ukrText;
                spans[2].textContent = result.engText;
                for (var i = 1; i < spans.length; i++) {
                    spans[i].classList.toggle('hidden_content');
                }
                for (var j = 0; j < inputs.length; j++) {
                    inputs[j].classList.toggle('hidden_content');
                }
                showSuccessToast("Текст успішно змінено");
                var editButton = row.querySelector('.editButton');
                var saveButton = row.querySelector('.saveButton');
                var cancelButton = row.querySelector('.cancelButton');
                saveButton.classList.add('disabled');
                editButton.classList.remove('disabled');
                cancelButton.classList.add('disabled');
            }
        },
        error: function (xhr) {
            var response = JSON.parse(xhr.responseText);
            if (Array.isArray(response)) {
                for (var i = 0; i < response.length; i++) {
                    var error = response[i];
                    if (error.fieldName === ('ukrText')) {
                        $('#ukrText'+id).css('border-color', 'red');
                        $('#ukrTextError'+id).html(error.fieldMessage);
                        setTimeout(function () {
                            $('#ukrTextError'+id).html('');
                        }, 12000);
                    }
                    // else if (error.fieldName === ('ukrTextFemale')) {
                    //     $('#ukrTextFemale'+id).css('border-color', 'red');
                    //     $('#ukrTextFemaleError').html(error.fieldMessage);
                    // }
                    else if (error.fieldName === ('engText')) {
                        $('#engText'+id).css('border-color', 'red');
                        $('#engTextError'+id).html(error.fieldMessage);
                        setTimeout(function () {
                            $('#engTextError'+id).html('');
                        }, 12000);
                    }
                }
            } else {
                showErrorToast("Помилка сервера");
            }
        }
    });
}