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
