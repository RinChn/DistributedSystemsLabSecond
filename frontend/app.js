document.addEventListener('DOMContentLoaded', () => {
    // Загружаем список фильмов при загрузке страницы
    fetchFilms();
    
    // Инициализация модального окна
    initModal();
    
});

// Функция для получения списка фильмов и их отображения в виде таблицы
function fetchFilms() {
    fetch('http://localhost:8080/api/v1/films')
        .then(response => response.json())
        .then(data => {
            console.log(data)
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
                
                const title = document.createElement('td');
                title.classList.add('table_data');
                title.textContent = film.title;
                
                const director = document.createElement('td');
                director.classList.add('table_data');
                director.textContent = film.directorName;
                
                const year = document.createElement('td');
                year.classList.add('table_data');
                year.textContent = film.yearReleased;
                
                const duration = document.createElement('td');
                duration.classList.add('table_data');
                duration.textContent = film.length;
                
                const genre = document.createElement('td');
                genre.classList.add('table_data');
                genre.textContent = film.genre;
                
                const deleteCell = document.createElement('td');
                deleteCell.classList.add('table_data');
                const deleteButton = document.createElement('button');
                deleteButton.textContent = 'Удалить';
                deleteButton.addEventListener('click', () => deleteFilm(film.title, film.directorName));
                deleteCell.appendChild(deleteButton);
                
                row.appendChild(title);
                row.appendChild(director);
                row.appendChild(year);
                row.appendChild(duration);
                row.appendChild(genre);
                row.appendChild(deleteCell);
                
                tbody.appendChild(row);
            });
            
            table.appendChild(tbody);
            wrapper.appendChild(table);
        })
        .catch(error => console.error('Ошибка загрузки фильмов:', error));
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
        console.log(filmData)

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