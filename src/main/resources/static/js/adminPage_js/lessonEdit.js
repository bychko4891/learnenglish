    function save() {
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        var mainCategorySelect = $('#mainCategorySelect').val();
        var subcategorySelect = $('#subcategorySelect').val();
        var subSubcategorySelect = $('#subSubcategorySelect').val();
        var categoryId = 0;
        if(subSubcategorySelect > 0) {
            categoryId = subSubcategorySelect;
        } else if (subcategorySelect > 0) {
            categoryId = subcategorySelect;
        } else if(mainCategorySelect > 0){
            categoryId = mainCategorySelect;
        }
        var phraseLesson = {
            id: $('#editor input[name="lessonId"]').val(),
            name: $('#editor input[name="name"]').val(),
            published: $('#toggleSwitch').is(':checked'),
            description: $('#editor textarea[name="info"]').val(),
            category: {
                id: categoryId
            }
        };
        var data = {
            phraseLesson: phraseLesson
        };
        console.log(data);
        $.ajax({
            url: '/admin-page/lesson-save',
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
                    showErrorToast(result.message)
                }
            },
            error: function () {
                alert('Помилка при відправці запиту');
            }
        });
    }

    $(document).ready(function () {
        const mainCategorySelect = document.getElementById('mainCategorySelect');
        const subcategorySelect = document.getElementById('subcategorySelect');
        const subSubcategorySelect = document.getElementById('subSubcategorySelect');
        mainCategorySelect.addEventListener('change', function () {
            const selectedCategoryId = mainCategorySelect.value;
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