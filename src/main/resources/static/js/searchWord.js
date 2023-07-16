const searchInput = document.getElementById('searchInput');

searchInput.addEventListener('input', function () {
    const searchTerm = searchInput.value;
    if (searchTerm.length > 0) {
        searchItems(searchTerm);
    }
});

function searchItems(searchTerm) {
    $.ajax({
        url: '/search-word',
        type: 'GET',
        data: {searchTerm: searchTerm},
        success: function (response) {
            displaySearchResults(response);
            console.log(response);
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

        // Створення посилання



        // Створення блоку для кожного результату
        const resultBlock = document.createElement('div');
        resultBlock.classList.add('result-block');



        // Створення назви
        const nameElement = document.createElement('h5');
        nameElement.textContent = result.name;
        resultBlock.appendChild(nameElement);

        // Створення перекладу
        const translationElement = document.createElement('p');
        translationElement.textContent = '- ' + result.translate;
        resultBlock.appendChild(translationElement);

        // Створення скритого поля з id екземпляра
        const idElement = document.createElement('input');
        idElement.type = 'hidden';
        idElement.value = result.id;
        resultBlock.appendChild(idElement);

        const linkElement = document.createElement('a');
        linkElement.href = '/word/' + result.id; // Додайте потрібне посилання
        linkElement.appendChild(resultBlock);

        searchResultsContainer.appendChild(linkElement);

    }
}