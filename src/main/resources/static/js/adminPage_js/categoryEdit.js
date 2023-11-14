function save() {
    var csrfToken = $("meta[name='_csrf']").attr("content");
    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
    var categoryPages = [$('#page').val()];
    var category = {
        id: $('#editor input[name="id"]').val(),
        name: $('#editor input[name="name"]').val(),
        mainCategory: $('#toggleSwitch').is(':checked'),
        categoryPages: categoryPages,
        info: $('#editor textarea[name="info"]').val()
    };
    var mainCategoryId = $('#subcategorySelect').val();
    var subcategoryId =  $('#subSubcategorySelect').val();
    var data = {
        category: category,
        mainCategoryId: mainCategoryId,
        subcategoryId: subcategoryId
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
            if (status === "Success") {
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
    const subcategorySelect = document.getElementById('subcategorySelect');
    const subSubcategorySelect = document.getElementById('subSubcategorySelect');
    subcategorySelect.addEventListener('change', function () {
        const selectedCategoryId = subcategorySelect.value;
        if (selectedCategoryId != 0) {
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
                } else {
                }
            })
            .catch(error => {
                console.error('Помилка при отриманні підкатегорій:', error);
            });
        }else {
            var options = subSubcategorySelect.getElementsByTagName("option");
            for (var i = 1; i < options.length; i++) {
                subSubcategorySelect.removeChild(options[i]);
            }
        }
    });
});