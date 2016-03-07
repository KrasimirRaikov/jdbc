CREATE DATABASE users;

CREATE TABLE users (
  id        INTEGER PRIMARY KEY NOT NULL,
  name      TEXT                NOT NULL,
  last_name TEXT,
  egn       TEXT                NOT NULL,
  age       INTEGER
);