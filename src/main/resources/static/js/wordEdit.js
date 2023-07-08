function save() {
    var csrfToken = $("meta[name='_csrf']").attr("content");
    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
    // var id = $('#wordsMainCategories input[name="id"]').val();
    // Зчитуємо аудіофайли зі вказаних інпутів
    var url = '/admin-page/word-save';
    var word = {
        id: $('#editor input[name="id"]').val(),
        name: $('#editor input[name="name"]').val(),
        translate: $('#editor input[name="translate"]').val(),
        brTranscription: $('#editor input[name="brTranscription"]').val(),
        usaTranscription: $('#editor input[name="usaTranscription"]').val(),
        irregularVerbPt: $('#editor input[name="irregularVerbPt"]').val(),
        irregularVerbPp: $('#editor input[name="irregularVerbPp"]').val(),
        published: $('#toggleSwitch').is(':checked'),
        text: $('#editor textarea[name="text"]').val()
    };
    var mainCategorySelect = {
        id: $('#mainCategorySelect').val()
    };
    var subcategorySelect = {
        id: $('#subcategorySelect').val()
    };
    var subSubcategorySelect = {
        id: $('#subSubcategorySelect').val()
    };
    var data = {
        word: word,
        mainCategorySelect: mainCategorySelect,
        subcategorySelect: subcategorySelect,
        subSubcategorySelect: subSubcategorySelect
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
                showSuccessToast(result.message);
            } else {
                showErrorToast(result.message)
            }
        },
        error: function () {
            let shel = {};
            alert(Boolean(shel))
            showErrorToast('Помилка запиту на сервер');
        }
    });
}

$(document).ready(function () {
    const mainCategorySelect = document.getElementById('mainCategorySelect');
    const subcategorySelect = document.getElementById('subcategorySelect');
    const subSubcategorySelect = document.getElementById('subSubcategorySelect');
    mainCategorySelect.addEventListener('change', function () {
        const selectedCategoryId = mainCategorySelect.value;
        fetch(`/admin-page/getSubcategories?mainCategoryId=${selectedCategoryId}`)
            .then(response => response.json())
            .then(subcategories => {
                if (subcategories.length > 0) {
                    subcategories.forEach(subcategory => {
                        const option = document.createElement('option');
                        option.value = subcategory.id;
                        option.text = subcategory.name;
                        subcategorySelect.appendChild(option);
                    });

                    // Показуємо subcategorySelect, якщо є підкатегорії
                    // subcategorySelect.style.display = subcategories.length > 0 ? 'block' : 'none';
                } else {
                }
            })
            .catch(error => {
                console.error('Помилка при отриманні підкатегорій:', error);
            });
    });
    subcategorySelect.addEventListener('change', function () {
        const selectedCategoryId = subcategorySelect.value;
        fetch(`/admin-page/getSubcategories?mainCategoryId=${selectedCategoryId}`)
            .then(response => response.json())
            .then(subcategories => {
                if (subcategories.length > 0) {
                    subcategories.forEach(subcategory => {
                        const option = document.createElement('option');
                        option.value = subcategory.id;
                        option.text = subcategory.name;
                        subSubcategorySelect.appendChild(option);
                    });

                    // Показуємо subcategorySelect, якщо є підкатегорії
                    // subcategorySelect.style.display = subcategories.length > 0 ? 'block' : 'none';
                } else {
                }
            })
            .catch(error => {
                console.error('Помилка при отриманні підкатегорій:', error);
            });
    });
});