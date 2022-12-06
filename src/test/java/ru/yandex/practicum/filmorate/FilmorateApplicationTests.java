package ru.yandex.practicum.filmorate;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MotionCompany;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MotionCompanyStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class FilmorateApplicationTests {
    UserStorage userStorage;
    FilmStorage filmStorage;
    MotionCompanyStorage motionCompanyStorage;
    GenreStorage genreStorage;
    List<Genre> genres = new ArrayList<>();

    User testUser1 = new User(0, "testUser1@ya.ru", "User1", "u1",
            LocalDate.of(2000, 01, 01), new ArrayList<>());
    User updateUser1 = new User(1, "testUser1@gmail.ru", "User1Update", "u1UpD",
            LocalDate.of(2000, 02, 01), new ArrayList<>());
    User testUser2 = new User(0, "testUser2@ya.ru", "User2", "u2",
            LocalDate.of(2001, 01, 01), new ArrayList<>());
    MotionCompany motionCompany = new MotionCompany(1, "G");
    MotionCompany motionCompany2 = new MotionCompany(2, "PG");
    Genre genre = new Genre(1, "Комедия");
    Film testFilm1 = new Film(0, "Film1", "It's test film 1", LocalDate.of(2001, 01, 01),
            100, new ArrayList<>(), motionCompany, genres);
    Film updateTestFilm1 = new Film(1, "UpdFilm1", "It's test film 1 update", LocalDate.of(2000, 01, 01),
            150, new ArrayList<>(), motionCompany2, genres);
    @Test
    public void testFindAndUpdateUserById() {
        userStorage.createUser(testUser1);
        User user = userStorage.getUserById(1);
        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("testUser1@ya.ru", user.getEmail());
        assertEquals("User1", user.getName());
        assertEquals("u1", user.getLogin());
        assertEquals(LocalDate.of(2000, 01, 01), user.getBirthday());
        assertEquals(0, user.getFriendsIds().size());
        userStorage.updateUser(updateUser1);
        user = userStorage.getUserById(1);
        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("testUser1@gmail.ru", user.getEmail());
        assertEquals("User1Update", user.getName());
        assertEquals("u1UpD", user.getLogin());
        assertEquals(LocalDate.of(2000, 02, 01), user.getBirthday());
    }

    @Test
    public void testFindALLUsers() {
        userStorage.createUser(testUser1);
        List<User> userList = userStorage.getAllUsers();
        assertThat(userList.size() == 1);
    }

    @Test
    public void testDeleteUser() {
        userStorage.createUser(testUser1);
        User user = userStorage.getUserById(1);
        assertNotNull(user);
        userStorage.deleteUserById(1);
        try {
            userStorage.getUserById(1);
        } catch (UserNotFoundException e) {
            assertEquals("Пользователь не найден", e.getMessage());
        }
    }

    @Test
    public void testAddRemoveFriend() {
        userStorage.createUser(testUser1);
        userStorage.createUser(testUser2);
        userStorage.addFriend(1, 2);
        assertEquals(2, userStorage.getUserById(1).getFriendsIds().get(0));
        userStorage.removeFriend(1, 2);
        assertEquals(0, userStorage.getUserById(1).getFriendsIds().size());
    }

    @Test

    public void testFindUpdateFilmById() {
        //genres.add(genre);
        filmStorage.addFilm(testFilm1);
        Film film = filmStorage.getFilmById(1);
        assertNotNull(film);
        assertEquals(1, film.getId());
        assertEquals("Film1", film.getName());
        assertEquals("It's test film 1", film.getDescription());
        assertEquals(LocalDate.of(2001, 01, 01), film.getReleaseDate());
        assertEquals(100, film.getDuration());
        assertEquals(0, film.getLikes().size());
        assertEquals(motionCompany.getId(), film.getMpa().getId());
        assertEquals(0, film.getGenres().size());
        genres.add(genre);
        filmStorage.updateFilm(updateTestFilm1);
        film = filmStorage.getFilmById(1);
        assertEquals(1, film.getId());
        assertEquals("UpdFilm1", film.getName());
        assertEquals("It's test film 1 update", film.getDescription());
        assertEquals(LocalDate.of(2000, 01, 01), film.getReleaseDate());
        assertEquals(150, film.getDuration());
        assertEquals(motionCompany2.getId(), film.getMpa().getId());
        assertEquals("Комедия", film.getGenres().get(0).getName());
    }
    @Test
    public void testFindALLFilms() {
        filmStorage.addFilm(testFilm1);
        List<Film> filmList = filmStorage.getAllFilms();
        assertThat(filmList.size() == 1);
    }
    @Test
    public void testDeleteFilm() {
        filmStorage.addFilm(testFilm1);
        Film film = filmStorage.getFilmById(1);
        assertNotNull(film);
        filmStorage.removeFilmById(1);
        try {
            filmStorage.getFilmById(1);
        } catch (FilmNotFoundException e) {
            assertEquals("Фильм не найден", e.getMessage());
        }
    }
    @Test
    public void testAddRemoveLike() {
        filmStorage.addFilm(testFilm1);
        userStorage.createUser(testUser1);
        Film film = filmStorage.addLike(1,1);
        assertEquals(1, film.getLikes().get(0));
        film = filmStorage.removeLike(1,1);
        assertEquals(0, film.getLikes().size());
    }

    @Test
    public void testGetAllMpa() {
        assertEquals(5, motionCompanyStorage.getAllMpa().size());
    }
    @Test
    public void testGetMpaById() {
        assertEquals("G", motionCompanyStorage.getMpaById(1).getName());
    }
    @Test
    public void testGetAllGenres() {
        assertEquals(6, genreStorage.getAllGenres().size());
    }
    @Test
    public void testGetGenreById() {
        assertEquals("Комедия", genreStorage.getGenreById(1).getName());
    }
}





