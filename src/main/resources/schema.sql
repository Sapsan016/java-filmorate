create table FILMS
(
    FILM_ID      INTEGER auto_increment,
    FILM_NAME    CHARACTER VARYING(50) not null,
    DESCRIPTION  CHARACTER VARYING(200),
    RELEASE_DATE DATE                  not null,
    DURATION     INTEGER               not null,
    RATING       CHARACTER VARYING(10) not null,
    constraint FILMS_PK
        primary key (FILM_ID)
);

create table USERS
(
    USER_ID      INTEGER auto_increment,
    USER_NAME    CHARACTER VARYING(30) not null,
    EMAIL        CHARACTER VARYING(20),
    BIRTHDAY     DATE                  not null,
    LOGIN       CHARACTER VARYING(10) not null,
    constraint USERS_PK
        primary key (USER_ID)
);

create table GENRES
(
    GENRE_ID      INTEGER auto_increment,
    GENRE_NAME    VARCHAR(10) not null,
    constraint GENRE_PK
        primary key (GENRE_ID)
);



create table FILMS_GENRE
(
    FILM_GENRE_ID INTEGER auto_increment,
    GENRE_ID      INTEGER not null,
    FILM_ID       INTEGER not null,
    constraint FILMS_GENRE_PK
        primary key (FILM_GENRE_ID),
    constraint FILMS_GENRE_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS
            on update cascade on delete cascade,
    constraint FILMS_GENRE_GENRES_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRES
);

create table FILM_LIKES
(
    FILM_LIKE_ID INTEGER auto_increment,
    FILM_ID      INTEGER not null,
    USER_ID      INTEGER not null,
    constraint FILM_LIKES_PK
        primary key (FILM_LIKE_ID),
    constraint FILM_LIKES_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS
            on update cascade on delete cascade,
    constraint FILM_LIKES_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
            on update cascade on delete cascade
);

create table USER_FRIENDS
(
    USER_FRIEND_ID INTEGER auto_increment,
    USER_ID        INTEGER not null,
    FRIEND_ID      INTEGER not null,
    STATUS         BOOLEAN,
    constraint USER_FRIENDS_PK
        primary key (USER_FRIEND_ID),
    constraint USER_FRIENDS_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
            on update cascade on delete cascade,
    constraint USER_FRIENDS_USERS_USER_ID_FK_2
        foreign key (FRIEND_ID) references USERS
            on update cascade on delete cascade
);

