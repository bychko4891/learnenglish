function previewImage() {
    var fileInput = document.getElementById('imageInput');
    var preview = document.getElementById('preview');

    if (fileInput.files && fileInput.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            preview.src = e.target.result;
        };

        reader.readAsDataURL(fileInput.files[0]);
    }
}

function save() {

    console.log("Save");
    var csrfToken = $("meta[name='_csrf']").attr("content");
    var csrfHeader = $("meta[name='_csrf_header']").attr("content");

    var mainCategorySelect = $('#mainCategorySelect').val();
    var subcategorySelect = $('#subcategorySelect').val();
    var categoryId = 0;
    if (subcategorySelect > 0) {
        categoryId = subcategorySelect;
    } else if(mainCategorySelect > 0){
        categoryId = mainCategorySelect;
    }

    var categoryPages = [$('#page').val()];

    var formData = new FormData();
    formData.append('image', $('#imageInput')[0].files[0]);

    var category = {
        id: $('#editor input[name="id"]').val(),
        name: $('#editor input[name="name"]').val(),
        mainCategory: $('#toggleSwitch').is(':checked'),
        categoryPages: categoryPages,
        description: $('#editor textarea[name="info"]').val(),
        parentCategory: { id: categoryId }
    };
    console.log(category);
    formData.append('category', new Blob([JSON.stringify(category)], {type: 'application/json'}));

    console.log(formData);
    $.ajax({
        url: '/admin/category-edit/save',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
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
    const mainCategorySelect = document.getElementById('mainCategorySelect');
    const subcategorySelect = document.getElementById('subcategorySelect');
    mainCategorySelect.addEventListener('change', function () {
        const selectedCategoryId = mainCategorySelect.value;
        if (selectedCategoryId != 0) {
        fetch(`/admin/getSubcategories?mainCategoryId=${selectedCategoryId}`)
            .then(response => response.json())
            .then(subcategories => {
                if (subcategories.length > 0) {
                    subcategories.forEach(subcategory => {
                        const option = document.createElement('option');
                        option.value = subcategory.id;
                        option.text = subcategory.name;
                        subcategorySelect.appendChild(option);
                    });
                } else {
                }
            })
            .catch(error => {
                console.error('Помилка при отриманні підкатегорій:', error);
            });
        }else {
            var options = subcategorySelect.getElementsByTagName("option");
            for (var i = 1; i < options.length; i++) {
                subcategorySelect.removeChild(options[i]);
            }
        }
    });
});