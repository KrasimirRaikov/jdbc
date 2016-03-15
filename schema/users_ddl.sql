CREATE DATABASE users;

CREATE TABLE users (
  id      INTEGER PRIMARY KEY NOT NULL,
  name    TEXT                NOT NULL,
  surname TEXT,
  egn     TEXT                NOT NULL,
  age     INTEGER
);