CREATE DATABASE users;

create TABLE users (
    id integer PRIMARY KEY not null,
    name text not null,
    surname text,
    egn text,
    age integer
);