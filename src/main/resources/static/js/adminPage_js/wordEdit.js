const searchInput = document.getElementById('searchInput');

searchInput.addEventListener('input', function () {
    const searchTerm = searchInput.value;
    if (searchTerm.length > 2) {
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
window.addEventListener('DOMContentLoaded', function () {
    var deleteButtons = document.getElementsByClassName('deleteBtn');
    for (var i = 0; i < deleteButtons.length; i++) {
        deleteButtons[i].addEventListener('click', function () {
            var id = this.getAttribute('data-id');

            deleteTranslationPair(id);
        });
    }
});

function deleteTranslationPair(id) {
    var element = document.querySelector('[data-id="' + id + '"]').parentNode;
    element.parentNode.removeChild(element);
}

function save() {
    var csrfToken = $("meta[name='_csrf']").attr("content");
    var csrfHeader = $("meta[name='_csrf_header']").attr("content");

    const selectedItemsInput = document.querySelectorAll('#addedItemsContainer input[name="id"]');
    const savedItemsInput = document.querySelectorAll('#savedItemsContainer input[name="id"]');
    var translationPairsId = [];
    selectedItemsInput.forEach(function (input) {
        translationPairsId.push(input.value);
    });
    var translationPairs = [];
    savedItemsInput.forEach(function (input) {
        translationPairs.push({id: input.value});
    });

    var url = '/admin-page/word-save';
    var word = {
        id: $('#editor input[name="wordId"]').val(),
        name: $('#editor input[name="name"]').val(),
        translate: $('#editor input[name="translate"]').val(),
        brTranscription: $('#editor input[name="brTranscription"]').val(),
        usaTranscription: $('#editor input[name="usaTranscription"]').val(),
        irregularVerbPt: $('#editor input[name="irregularVerbPt"]').val(),
        irregularVerbPp: $('#editor input[name="irregularVerbPp"]').val(),
        published: $('#toggleSwitch').is(':checked'),
        translationPairs: translationPairs,
        description: $('#editor textarea[name="description"]').val(),
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
        subSubcategorySelect: subSubcategorySelect,
        translationPairsId: translationPairsId
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

$(document).ready(function () {
    $('textarea').on('input', function () {
        this.style.height = 'auto';
        this.style.height = (this.scrollHeight) + 'px';
    });
});