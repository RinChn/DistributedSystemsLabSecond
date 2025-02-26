document.addEventListener('DOMContentLoaded', () => {
    // Загружаем список сеансов при загрузке страницы
    fetchSessions();
    // Инициализация модального окна
    initModal();
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
    const headers = ['Дата', 'Время', 'Название', 'Режиссер', '', ''];
    
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
        // Преобразование даты в читаемый формат
        const formattedDate = new Date(session.date).toLocaleDateString('ru-RU');
        // Удаление секунд из времени (берем только HH:mm)
        const formattedTime = session.time.substring(0, 5);
        
        const row = document.createElement('tr');
        row.innerHTML = `
            <td class="table_data">${formattedDate}</td>
            <td class="table_data">${formattedTime}</td>
            <td class="table_data">${session.film.title}</td>
            <td class="table_data">${session.film.directorName}</td>
            <td class="table_data">
                <button class="button addi-button" 
                        onclick="viewSession('${session.time}', '${session.date}', '${session.cinemaHallNumber}')">
                        Управлять
                </button>
            </td>
            <td class="table_data">
                <button class="button delete-button" 
                        onclick="deleteSession('${session.time}', '${session.date}', '${session.cinemaHallNumber}')">
                </button>
            </td>
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
            console.error('Ошибка при добавлении сеанса');
        }
    })
    .catch(error => console.error('Ошибка сети:', error));
}

// Функция для инициализации модального окна
function initModal() {
    const modal = document.getElementById("modal");
    const openModalBtn = document.getElementById("modal__open");
    const closeModalBtn = document.querySelector(".modal__close");
    const form = document.getElementById("session-form");

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
        const timeInput = document.getElementById('time').value;
        const session = {
            date: document.getElementById('date').value,
            time: timeInput + ":00",
            cinemaHallNumber: parseInt(document.getElementById('hall').value),
            filmTitle: document.getElementById('title').value,
            directorName: document.getElementById('director').value
        };
        console.log(session);

        // Добавляем фильм
        addSession(session);

        // Закрываем модальное окно
        modal.style.display = "none";

        // Очищаем форму
        form.reset();
    });
}

// Функция для удаления сеанса
function deleteSession(time, date, cinemaHallNumber) {
    fetch('http://localhost:8080/api/v1/sessions', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ time, date, cinemaHallNumber })
    })
    .then(response => {
        if (response.ok) {
            fetchSessions(); // Обновление списка после удаления
        } else {
            console.error('Ошибка при удалении сеанса');
        }
    })
    .catch(error => console.error('Ошибка сети:', error));
}