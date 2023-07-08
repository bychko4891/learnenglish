// $(document).ready(function () {
const searchInput = document.getElementById('searchInput');

searchInput.addEventListener('input', function () {
    const searchTerm = searchInput.value;
    if (searchTerm.length > 2) {
        // Викликати функцію для виконання пошуку на сервері зі значенням searchTerm
        searchItems(searchTerm);
    }
});

function searchItems(searchTerm) {
    $.ajax({
        url: '/admin-page/search', // Шлях до вашого ендпоінта пошуку на сервері
        type: 'GET',
        data: {searchTerm: searchTerm},
        success: function (response) {
            // Обробити отримані результати пошуку
            displaySearchResults(response);
        },
        error: function () {
            console.error('Помилка при виконанні запиту на сервер');
        }
    });
}

function displaySearchResults(results) {
    const searchResultsContainer = document.getElementById('searchResults');
    searchResultsContainer.classList.add('search_result_style');
    if (results.length === 0) {
        searchResultsContainer.classList.remove('search_result_style');
    }
    // Додайте обробник події click на сторінці
    document.addEventListener('click', function (event) {
        const targetElement = event.target;

        // Перевірка, чи клікнули поза блоком результатів пошуку
        if (!searchResultsContainer.contains(targetElement)) {
            searchResultsContainer.innerHTML = '';
            searchResultsContainer.classList.remove('search_result_style');
        }
    });
    searchResultsContainer.innerHTML = '';

    for (let i = 0; i < results.length; i++) {
        const result = results[i];

        // Створення блоку для кожного результату
        const resultBlock = document.createElement('div');
        resultBlock.classList.add('result-block');

        // Створення назви
        const nameElement = document.createElement('h2');
        nameElement.textContent = result.engText;
        resultBlock.appendChild(nameElement);

        // Створення перекладу
        const translationElement = document.createElement('p');
        translationElement.textContent = result.ukrText;
        resultBlock.appendChild(translationElement);

        // Створення скритого поля з id екземпляра
        const idElement = document.createElement('input');
        idElement.type = 'hidden';
        idElement.value = result.id;
        resultBlock.appendChild(idElement);

        // Створення кнопки "Додати"
        const addButton = document.createElement('button');
        addButton.classList.add('add-button');
        addButton.textContent = 'Додати';
        addButton.addEventListener('click', function () {
            const selectedId = this.parentNode.querySelector('input[type="hidden"]').value;

            const addedItemsContainer = document.getElementById('addedItemsContainer');
            const newItem = document.createElement('div');
            newItem.classList.add('phrasesList');

            // Створення input з атрибутом name="id" та значенням value="${selectedId}"
            const inputElement = document.createElement('input');
            inputElement.type = 'hidden';
            inputElement.name = 'id';
            inputElement.value = selectedId;
            newItem.appendChild(inputElement);

            // Створення label для поля engText
            const labelElement = document.createElement('label');
            labelElement.textContent = result.engText;
            newItem.appendChild(labelElement);

            addedItemsContainer.appendChild(newItem);
        });

        resultBlock.appendChild(addButton);

        searchResultsContainer.appendChild(resultBlock);
    }
}


// $(document).ready(function () {
//     tinymce.init({
//         selector: 'textarea#info',
//         convert_urls : false,
//         height: 500,
//         plugins: [
//             'advlist', 'autolink', 'lists', 'link', 'image', 'charmap', 'preview',
//             'anchor', 'searchreplace', 'visualblocks', 'code', 'fullscreen',
//             'insertdatetime', 'media', 'table', 'help', 'wordcount'
//         ],
//         toolbar:
//             'undo redo | formatselect |' +
//             ' bold italic underline forecolor | link image media table mergetags |' +
//             ' addcomment showcomments | spellcheckdialog code typography |' +
//             ' align lineheight | checklist numlist bullist indent outdent |' +
//             ' emoticons charmap | removeformat'
//         ,
//         content_css: [
//             '//fonts.googleapis.com/css?family=Lato:300,300i,400,400i',
//             '//www.tiny.cloud/css/codepen.min.css'
//         ],
//         api_key: 'j8dxs8puyiugoamq11vn3bctaonh1jhzvd0cewcb1jiyl2c6',
//         menubar: true,
//         statusbar: true,
//         branding: true
//     });
//
//     $('#editor').submit(function (event) {
//         event.preventDefault();
//         save();
//     });
//
// });

function save() {
    // event.preventDefault();
    var csrfToken = $("meta[name='_csrf']").attr("content");
    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
    const selectedItemsInput = document.querySelectorAll('#addedItemsContainer input[name="id"]');
    var translationPairList = [];
    selectedItemsInput.forEach(function (input) {
        translationPairList.push({id: input.value});
    });
    var url = '/admin-page/page-phrases-save';
    var translationPairsPage = {
        id: $('#editor input[name="id"]').val(),
        name: $('#editor input[name="name"]').val(),
        published: $('#toggleSwitch').is(':checked'),
        info: $('#editor textarea[name="info"]').val(),
        translationPairList: translationPairList

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
        translationPairsPage: translationPairsPage,
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

// });


// $(document).ready(function () {
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
    // });
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
                } else {
                }
            })
            .catch(error => {
                console.error('Помилка при отриманні підкатегорій:', error);
            });
    });
});
// });