
    merge into MPA (ID, name) values ( 1, 'G' ), ( 2, 'PG' ), ( 3, 'PG-13' ), ( 4, 'R' ), ( 5, 'NC-17' );


    merge into GENRE (ID, name) values ( 1, 'Комедия' ), ( 2, 'Драма' ), ( 3, 'Мультфильм' )
                                 , ( 4, 'Триллер' ), ( 5, 'Документальный' ), ( 6, 'Боевик' );



-- Удаление всех таблиц
-- drop table IF EXISTS FILM_LIKE;
-- drop table IF EXISTS USER_FRIEND;
-- drop table IF EXISTS FILM_GENRE;
-- drop table IF EXISTS GENRE;
-- drop table IF EXISTS USERS;
-- drop table IF EXISTS FILMS;
-- drop table IF EXISTS MPA;

