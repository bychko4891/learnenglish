function save() {
    var csrfToken = $("meta[name='_csrf']").attr("content");
    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
    var categoryPages = [$('#page').val()];
    var wordsCategory = {
        id: $('#editor input[name="id"]').val(),
        name: $('#editor input[name="name"]').val(),
        mainCategory: $('#toggleSwitch').is(':checked'),
        categoryPages: categoryPages,
        info: $('#editor textarea[name="info"]').val()
    };
    var mainCategorySelect = {
        id: $('#mainCategorySelect').val()
    };
    var subcategorySelect = {
        id: $('#subcategorySelect').val()
    };
    var data = {
        wordsCategory: wordsCategory,
        mainCategorySelect: mainCategorySelect,
        subcategorySelect: subcategorySelect
    };
    $.ajax({
        url: '/admin-page/category-save',
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
                setTimeout(function () {
                    location.reload();
                }, 3000);
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
    mainCategorySelect.addEventListener('change', function () {
        const selectedCategoryId = mainCategorySelect.value;
        fetch(`/admin-page/getSubcategories?mainCategoryId=${selectedCategoryId}`)
            .then(response => response.json())
            .then(subcategories => {
                if (subcategories.length > 0) {
                    // subcategorySelect.innerHTML = '';
                    subcategories.forEach(subcategory => {
                        const option = document.createElement('option');
                        option.value = subcategory.id;
                        option.text = subcategory.name;
                        subcategorySelect.appendChild(option);
                    });

                    // Показуємо subcategorySelect, якщо є підкатегорії
                    // subcategorySelect.style.display = subcategories.length > 0 ? 'block' : 'none';
                } else {}
            })
            .catch(error => {
                console.error('Помилка при отриманні підкатегорій:', error);
            });
    });
});