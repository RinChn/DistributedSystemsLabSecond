document.addEventListener('DOMContentLoaded', () => {
    // Загружаем список режиссеров при загрузке страницы
    fetchDirectors();
    // Инициализация модального окна
    initModal();

    // Инициализация поисковой строки
    const searchInput = document.querySelector('.search');
    searchInput.addEventListener('input', debounce(handleSearchInput, 300));
    searchInput.addEventListener('keydown', handleSearchKeyDown);
});

// Функция для получения всех режиссеров
function fetchDirectors() {
    fetch('http://localhost:8080/api/v1/directors')
    .then(response => response.json())
    .then(data => {
        renderDirectors(data); // Отображаем всех режиссеров
    })
    .catch(error => console.error('Ошибка при загрузке списка режиссеров:', error));
}

// Функция для отображения режиссеров в виде таблицы
function renderDirectors(data) {
    const wrapper = document.querySelector('#directors .wrapper');
    wrapper.innerHTML = ''; // Очистка контейнера
    
    // Создание таблицы
    const table = document.createElement('table');
    table.classList.add('table');
    
    // Создание заголовков таблицы
    const thead = document.createElement('thead');
    const headerRow = document.createElement('tr');
    const headers = ['Имя Фамилия', ''];
    
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
    
    data.forEach(director => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td class="table_data">${director.name}</td>
            <td class="table_data"><button onclick="deleteDirector('${director.name}')">Удалить</button></td>
        `;
        tbody.appendChild(row);
    });
    
    table.appendChild(tbody);
    wrapper.appendChild(table);
}

// Функция для инициализации модального окна
function initModal() {
    const modal = document.getElementById("modal");
    const openModalBtn = document.getElementById("modal__open");
    const closeModalBtn = document.querySelector(".modal__close");
    const form = document.getElementById("director-form");

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
        const director = {
            name: document.getElementById('name').value + ' ' + document.getElementById('last-name').value
        };

        // Добавляем режиссера
        addDirector(director);

        // Закрываем модальное окно
        modal.style.display = "none";

        // Очищаем форму
        form.reset();
    });
}

// Функция для добавления режиссера
function addDirector(director) {
    fetch('http://localhost:8080/api/v1/directors', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(director)
    })
    .then(response => {
        if (response.ok) {
            fetchDirectors(); // Обновление списка после добавления
        } else {
            console.error('Ошибка при добавлении режиссера');
        }
    })
    .catch(error => console.error('Ошибка сети:', error));
}

// Функция для удаления режиссера с проверкой на привязанные фильмы
function deleteDirector(directorName) {
    // Сначала получаем список всех фильмов
    fetch('http://localhost:8080/api/v1/films')
        .then(response => response.json())
        .then(films => {
            // Проверяем, есть ли у режиссера привязанные фильмы
            const hasFilms = films.some(film => film.directorName === directorName);

            if (hasFilms) {
                // Если у режиссера есть привязанные фильмы, показываем предупреждение
                alert("Невозможно удалить режиссера, так как у него есть привязанные фильмы.");
            } else {
                // Если привязанных фильмов нет, продолжаем удаление
                fetch('http://localhost:8080/api/v1/directors', {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(directorName)
                })
                .then(response => {
                    if (response.ok) {
                        fetchDirectors(); // Обновление списка после удаления
                    } else {
                        console.error('Ошибка при удалении режиссера');
                    }
                })
                .catch(error => console.error('Ошибка сети:', error));
            }
        })
        .catch(error => console.error('Ошибка при получении списка фильмов:', error));
}

// Функция для отправки запроса на сервер и получения подсказок
function handleSearchInput(event) {
    const query = event.target.value.trim();  // Получаем текст из инпута

    if (query.length >= 1) {
        // Отправляем запрос на сервер для получения подсказок
        fetch('http://localhost:8080/api/v1/directors/search', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ name: query })
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

// Функция для отображения подсказок
function displaySuggestions(suggestions) {
    const suggestionsContainer = document.getElementById('suggestions'); 
    suggestionsContainer.innerHTML = ''; // Очистка предыдущих подсказок

    if (Array.isArray(suggestions)) {
        suggestionsContainer.style.display = 'block';
        
        suggestions.forEach(suggestion => {
            const suggestionItem = document.createElement('div');
            suggestionItem.classList.add('suggestion');
            suggestionItem.textContent = suggestion.name; // Вывод названия фильма

            // Добавляем обработчик клика, чтобы выбрать подсказку
            suggestionItem.addEventListener('click', () => selectSuggestion(suggestion));
            suggestionsContainer.appendChild(suggestionItem);
        });
    }
}

// Функция для обработки выбора подсказки
function selectSuggestion(director) {
    const searchInput = document.querySelector('.search');
    searchInput.value = director.name;  // Заполняем поле поиска выбранным режиссером
    
    // выполняем поиск по режиссеру
    fetch('http://localhost:8080/api/v1/directors/search', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name: director.name })
    })
    .then(response => response.json())  
    .then(data => {
        renderDirectors(data); // Отображаем найденные фильмы
    })
    .catch(error => console.error('Ошибка при фильтрации фильмов:', error));
    
    displaySuggestions([]);
}

function handleSearchKeyDown(event) {
    if (event.key === 'Enter') {
        const query = event.target.value.trim();
        if (query) {
            fetch('http://localhost:8080/api/v1/directors/search', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ name: query })
            })
            .then(response => response.json())
            .then(data => {
                renderDirectors(data);
            })
            .catch(error => console.error('Ошибка при фильтрации режиссеров:', error));
        } else {
            // Если строка поиска пустая, показываем все фильмы
            fetchDirectors(); // Отображаем все фильмы
        }
    }
}

function debounce(func, delay) {
    let timeout;
    return function () {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, arguments), delay);
    };
}