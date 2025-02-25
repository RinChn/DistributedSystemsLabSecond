document.addEventListener('DOMContentLoaded', () => {
    // Загружаем список режиссеров при загрузке страницы
    fetchDirectors();
    // Инициализация модального окна
    //initModal();

    // Инициализация поисковой строки
    const searchInput = document.querySelector('.search');
    //searchInput.addEventListener('input', debounce(handleSearchInput, 300));
    //searchInput.addEventListener('keydown', handleSearchKeyDown);
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