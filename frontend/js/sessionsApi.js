document.addEventListener('DOMContentLoaded', () => {
    // Загружаем список сеансов при загрузке страницы
    fetchSessions();
});

// Функция для получения всех сеансов
function fetchSessions() {
    fetch('http://localhost:8080/api/v1/sessions')
    .then(response => response.json())
    .then(data => {
        console.log(data);
        renderSessions(data); // Отображаем все сеансы
    })
    .catch(error => console.error('Ошибка при загрузке сеансов:', error));
}

// Функция для отображения сеансов в виде таблицы
function renderSessions(data) {
    const wrapper = document.querySelector('#sessions .wrapper');
    wrapper.innerHTML = ''; // Очистка контейнера
    
    // Создание таблицы
    const table = document.createElement('table');
    table.classList.add('table');
    
    // Создание заголовков таблицы
    const thead = document.createElement('thead');
    const headerRow = document.createElement('tr');
    const headers = ['Дата', 'Время', 'Название', '', ''];
    
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
    
    data.forEach(session => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td class="table_data">${session.date}</td>
            <td class="table_data">${session.time}</td>
            <td class="table_data">${session.filmTitle}</td>
            <td class="table_data"><button class="button" onclick="">Управлять</button></td>
            <td class="table_data"><button class="button delete-button" onclick=""></button></td>
        `;

        tbody.appendChild(row);
    });
    
    table.appendChild(tbody);
    wrapper.appendChild(table);
}

// Функция для добавления сеанса
function addSession(session) {
    fetch('http://localhost:8080/api/v1/sessions', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(session)
    })
    .then(response => {
        if (response.ok) {
            fetchSessions(); // Обновление списка после добавления
        } else {
            console.error('Ошибка при добавлении фильма');
        }
    })
    .catch(error => console.error('Ошибка сети:', error));
}

