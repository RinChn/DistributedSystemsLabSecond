document.addEventListener('DOMContentLoaded', () => {
    // Загружаем список фильмов при загрузке страницы
    fetchFilms();
    // Инициализация модального окна
    initModal();

    // Инициализация поисковой строки
    const searchInput = document.querySelector('.search');
    searchInput.addEventListener('input', debounce(handleSearchInput, 300));
    searchInput.addEventListener('keydown', handleSearchKeyDown);
});

// Функция для получения всех фильмов
function fetchFilms() {
    fetch('http://localhost:8080/api/v1/films')
    .then(response => response.json())
    .then(data => {
        renderFilms(data); // Отображаем все фильмы
    })
    .catch(error => console.error('Ошибка при загрузке фильмов:', error));
}

// Функция для отображения фильмов в виде таблицы
function renderFilms(data) {
    const wrapper = document.querySelector('#films .wrapper');
    wrapper.innerHTML = ''; // Очистка контейнера
    
    // Создание таблицы
    const table = document.createElement('table');
    table.classList.add('table');
    
    // Создание заголовков таблицы
    const thead = document.createElement('thead');
    const headerRow = document.createElement('tr');
    const headers = ['Название', 'Режиссер', 'Год выпуска', 'Длительность', 'Жанр', ''];
    
    headers.forEach(text => {
        const th = document.createElement('th');
        th.classList.add('col_header');
        th.textContent = text;
        headerRow.appendChild(th);
    });
    thead.appendChild(headerRow);
    table.appendChild(thead);
    
    // Создание тела таблицы
    const tbody = document.createElement('tbody');
    
    data.forEach(film => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td class="table_data">${film.title}</td>
            <td class="table_data">${film.directorName}</td>
            <td class="table_data">${film.yearReleased}</td>
            <td class="table_data">${film.length}</td>
            <td class="table_data">${film.genre}</td>
            <td class="table_data"><button class="button delete-button" onclick="deleteFilm('${film.title}', '${film.directorName}')"></button></td>
        `;

        tbody.appendChild(row);
    });
    
    table.appendChild(tbody);
    wrapper.appendChild(table);
}

// Функция для удаления фильма
function deleteFilm(title, directorName) {
    fetch('http://localhost:8080/api/v1/films', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ title, directorName })
    })
    .then(response => {
        if (response.ok) {
            fetchFilms(); // Обновление списка после удаления
        } else {
            console.error('Ошибка при удалении фильма');
        }
    })
    .catch(error => console.error('Ошибка сети:', error));
}

// Функция для добавления фильма
function addFilm(filmData) {
    fetch('http://localhost:8080/api/v1/films', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(filmData)
    })
    .then(response => {
        if (response.ok) {
            fetchFilms(); // Обновление списка после добавления
        } else {
            console.error('Ошибка при добавлении фильма');
        }
    })
    .catch(error => console.error('Ошибка сети:', error));
}

// Функция для инициализации модального окна
function initModal() {
    const modal = document.getElementById("modal");
    const openModalBtn = document.getElementById("modal__open");
    const closeModalBtn = document.querySelector(".modal__close");
    const form = document.getElementById("film-form");
    
    // Загружаем жанры
    fetchGenres();

    // Открытие модального окна
    openModalBtn.addEventListener("click", () => {
        modal.style.display = "flex";
    });

    // Закрытие окна при клике на "×"
    closeModalBtn.addEventListener("click", () => {
        modal.style.display = "none";
    });

    // Закрытие при клике вне окна
    window.addEventListener("click", (event) => {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    });

    // Обработчик отправки формы
    form.addEventListener("submit", (event) => {
        event.preventDefault();

        // Собираем данные формы
        const filmData = {
            title: document.getElementById('title').value,
            directorName: document.getElementById('director').value,
            yearReleased: parseInt(document.getElementById('year').value),
            length: parseInt(document.getElementById('duration').value),
            genre: document.getElementById('genre').value
        };

        // Добавляем фильм
        addFilm(filmData);

        // Закрываем модальное окно
        modal.style.display = "none";

        // Очищаем форму
        form.reset();
    });
}

// Функция для получения жанров и добавления их в селектор
function fetchGenres() {
    const genreSelect = document.getElementById('genre');
    const genres = [
        'ACTION', 'ADVENTURE', 'COMEDY', 'DRAMA', 'HORROR', 'THRILLER', 
        'FANTASY', 'ROMANCE', 'MYSTERY', 'CRIME', 'ANIMATION', 'DOCUMENTARY', 
        'MUSICAL', 'HISTORICAL', 'BIOGRAPHY', 'SPORT'
    ];
    // Очистка селектора
    genreSelect.innerHTML = '';

    // Создание первого пустого option
    const defaultOption = document.createElement('option');
    defaultOption.textContent = 'Выберите жанр';
    genreSelect.appendChild(defaultOption);

    // Заполнение селектора жанрами
    genres.forEach(genre => {
        const option = document.createElement('option');
        option.value = genre;
        option.textContent = genre;
        genreSelect.appendChild(option);
    });
}

// Функция для отображения подсказок
function displaySuggestions(suggestions) {
    const suggestionsContainer = document.getElementById('suggestions'); 
    suggestionsContainer.innerHTML = ''; // Очистка предыдущих подсказок

    if (Array.isArray(suggestions)) {
        suggestionsContainer.style.display = 'block';
        
        suggestions.forEach(suggestion => {
            const suggestionItem = document.createElement('div');
            suggestionItem.classList.add('suggestion');
            suggestionItem.textContent = suggestion.title + ' ' + suggestion.directorName; // Вывод названия фильма

            // Добавляем обработчик клика, чтобы выбрать подсказку
            suggestionItem.addEventListener('click', () => selectSuggestion(suggestion));
            suggestionsContainer.appendChild(suggestionItem);
        });
    }
}

// Функция для отправки запроса на сервер и получения подсказок
function handleSearchInput(event) {
    const query = event.target.value.trim();  // Получаем текст из инпута

    if (query.length >= 1) {
        // Отправляем запрос на сервер для получения подсказок
        fetch('http://localhost:8080/api/v1/films/search', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                title: query,
                directorName: null,
                maxYearReleased: null,
                minYearReleased: null,
                maxLength: null,
                minLength: null,
                genre: null
            })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json()
        })
        .then(data => {
            console.log(data);
            displaySuggestions(data);  // Отображаем подсказки
        })
        .catch(error => console.error('Ошибка при получении подсказок:', error));
    } 
    else {
        // Если строка поиска пустая, скрываем подсказки
        displaySuggestions([]);
    }
}

function handleSearchKeyDown(event) {
    if (event.key === 'Enter') {
        const query = event.target.value.trim();
        if (query) {
            fetch('http://localhost:8080/api/v1/films/search', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    title: query,
                    director: null,
                    maxYearReleased: null,
                    minYearReleased: null,
                    maxLength: null,
                    minLength: null,
                    genre: null
                })
            })
            .then(response => response.json())
            .then(data => {
                renderFilms(data);
            })
            .catch(error => console.error('Ошибка при фильтрации фильмов:', error));
        } else {
            // Если строка поиска пустая, показываем все фильмы
            fetchFilms(); // Отображаем все фильмы
        }
    }
}

// Функция для обработки выбора подсказки
function selectSuggestion(film) {
    const searchInput = document.querySelector('.search');
    searchInput.value = film.title;  // Заполняем поле поиска выбранным названием
    
    // выполняем поиск по выбранному фильму
    fetch('http://localhost:8080/api/v1/films/search', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            title: film.title,
            director: null,
            maxYearReleased: null,
            minYearReleased: null,
            maxLength: null,
            minLength: null,
            genre: null
        })
    })
    .then(response => response.json())
    .then(data => {
        renderFilms(data); // Отображаем найденные фильмы
    })
    .catch(error => console.error('Ошибка при фильтрации фильмов:', error));
    
    displaySuggestions([]);
}

function debounce(func, delay) {
    let timeout;
    return function () {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, arguments), delay);
    };
}