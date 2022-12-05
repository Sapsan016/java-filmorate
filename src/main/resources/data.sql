merge into GENRES(GENRE_ID,GENRE_NAME)
values (1,'Комедия');
merge into GENRES(GENRE_ID,GENRE_NAME)
values (2,'Драма');
merge into GENRES(GENRE_ID,GENRE_NAME)
values (3, 'Мультфильм');
merge into GENRES(GENRE_ID,GENRE_NAME)
values (4, 'Триллер');
merge into GENRES(GENRE_ID,GENRE_NAME)
values (5, 'Документальный');
merge into GENRES(GENRE_ID,GENRE_NAME)
values (6, 'Боевик');


merge into MPA(MPA_ID,MPA_NAME)
    values (1, 'G');
merge into MPA(MPA_ID,MPA_NAME)
    values (2, 'PG');
merge into MPA(MPA_ID,MPA_NAME)
    values (3, 'PG-13');
merge into MPA(MPA_ID,MPA_NAME)
    values (4, 'R');
merge into MPA(MPA_ID,MPA_NAME)
    values (5, 'NC-17');