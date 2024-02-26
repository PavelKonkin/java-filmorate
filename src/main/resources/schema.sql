-- Удаление всех таблиц
drop table IF EXISTS FILM_LIKE;
drop table IF EXISTS USER_FRIEND;
drop table IF EXISTS FILM_GENRE;
drop table IF EXISTS GENRE;
drop table IF EXISTS USERS;
drop table IF EXISTS FILMS;
drop table IF EXISTS MPA;

create table IF NOT EXISTS MPA
(
    ID   INTEGER auto_increment,
    NAME CHARACTER VARYING not null,
    constraint MRA_RATING_PK
        primary key (ID)
);
create table IF NOT EXISTS FILMS
(
    ID           INTEGER auto_increment,
    NAME         CHARACTER VARYING not null,
    DESCRIPTION  CHARACTER VARYING,
    RELEASE_DATE DATE,
    DURATION     INTEGER           not null,
    RATING_ID    INTEGER           not null,
    constraint FILMS_PK
        primary key (ID),
    constraint FILMS_MRA_RATING_FK
        foreign key (RATING_ID) references MPA
);
create table IF NOT EXISTS USERS
(
    ID       INTEGER auto_increment,
    NAME     CHARACTER VARYING,
    EMAIL    CHARACTER VARYING not null,
    LOGIN    CHARACTER VARYING     not null,
    BIRTHDAY DATE,
    constraint USERS_PK
        primary key (ID)
);
create table IF NOT EXISTS GENRE
(
    ID   INTEGER auto_increment,
    NAME CHARACTER VARYING not null,
    constraint GENRE_PK
        primary key (ID)
);
create table IF NOT EXISTS FILM_GENRE
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint FILM_GENRE_PK
        primary key (FILM_ID, GENRE_ID),
    constraint FILM_GENRE_FILMS_ID_FK
        foreign key (FILM_ID) references FILMS
            on delete cascade,
    constraint FILM_GENRE_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRE
            on delete cascade
);
create table IF NOT EXISTS USER_FRIEND
(
    USER_ID              INTEGER not null,
    FRIEND_ID            INTEGER not null,
    constraint USER_FRIEND_PK
        primary key (USER_ID, FRIEND_ID),
    constraint USER_FRIEND_USERS_ID_FK
        foreign key (USER_ID) references USERS
            on delete cascade,
    constraint USER_FRIEND_USERS_ID_FK_2
        foreign key (FRIEND_ID) references USERS
            on delete cascade
);
create table IF NOT EXISTS FILM_LIKE
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint FILM_LIKE_PK
        primary key (FILM_ID, USER_ID),
    constraint FILM_LIKE_FILMS_ID_FK
        foreign key (FILM_ID) references FILMS
            on delete cascade,
    constraint FILM_LIKE_USERS_ID_FK
        foreign key (USER_ID) references USERS
            on delete cascade
);