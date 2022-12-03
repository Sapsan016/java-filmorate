create table IF NOT EXISTS MPA
(
    MPA_ID   INTEGER               not null,
    MPA_NAME CHARACTER VARYING(10) not null,
    constraint MPA_PK
        primary key (MPA_ID)
);
create table IF NOT EXISTS FILMS
(
    FILM_ID      INTEGER auto_increment,
    FILM_NAME    CHARACTER VARYING(50) not null,
    DESCRIPTION  CHARACTER VARYING(200),
    RELEASE_DATE DATE                  not null,
    DURATION     INTEGER               not null,
    RATING       INTEGER               not null,
    constraint FILMS_PK
        primary key (FILM_ID),
    constraint FILMS_MPA_MPA_ID_FK
        foreign key (RATING) references MPA
);

create table IF NOT EXISTS USERS
(
    USER_ID      INTEGER auto_increment,
    EMAIL        CHARACTER VARYING(50),
    USER_NAME    CHARACTER VARYING(50),
    LOGIN       CHARACTER VARYING(50) not null,
    BIRTHDAY     DATE                  not null,
    constraint USERS_PK
        primary key (USER_ID)
);

create table IF NOT EXISTS GENRES
(
    GENRE_ID      INTEGER auto_increment,
    GENRE_NAME    VARCHAR(20) not null,
    constraint GENRE_PK
        primary key (GENRE_ID)
);


create table IF NOT EXISTS FILMS_GENRE
(
    GENRE_ID      INTEGER,
    FILM_ID       INTEGER not null,
    constraint FILMS_GENRE_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS
            on update cascade on delete cascade,
    constraint FILMS_GENRE_GENRES_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRES
);

create table IF NOT EXISTS FILM_LIKES
(
    FILM_ID      INTEGER not null,
    USER_ID      INTEGER not null,

    constraint FILM_LIKES_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS
            on update cascade on delete cascade,
    constraint FILM_LIKES_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
            on update cascade on delete cascade
);

create table IF NOT EXISTS FRIENDS
(
    USER_ID        INTEGER not null,
    FRIEND_ID      INTEGER not null,
    STATUS         BOOLEAN,

    constraint FRIENDS_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
            on update cascade on delete cascade,
    constraint FRIENDS_USERS_USER_ID_FK_2
        foreign key (FRIEND_ID) references USERS
            on update cascade on delete cascade
);


