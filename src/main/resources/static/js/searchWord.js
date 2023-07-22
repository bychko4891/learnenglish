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
    searchResultsContainer.innerHTML = '';
    if (results.length === 0) {
        searchResultsContainer.innerHTML = 'За вашим запитом нічого не знайдено';
    }
    document.addEventListener('click', function (event) {
        const targetElement = event.target;
        if (!searchResultsContainer.contains(targetElement)) {
            searchResultsContainer.innerHTML = '';
            searchResultsContainer.classList.remove('search_result_style');
        }
    });


    for (let i = 0; i < results.length; i++) {
        const result = results[i];

        const resultBlock = document.createElement('div');
        resultBlock.classList.add('result-block');

        const nameElement = document.createElement('h5');
        nameElement.textContent = result.name;
        resultBlock.appendChild(nameElement);

        const translationElement = document.createElement('p');
        translationElement.textContent = '- ' + result.translate;
        resultBlock.appendChild(translationElement);

        const idElement = document.createElement('input');
        idElement.type = 'hidden';
        idElement.value = result.id;
        resultBlock.appendChild(idElement);

        const linkElement = document.createElement('a');
        linkElement.href = '/word/' + result.id;
        linkElement.appendChild(resultBlock);

        searchResultsContainer.appendChild(linkElement);

    }
}