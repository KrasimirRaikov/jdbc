CREATE TABLE address (
  id        BIGINT NOT NULL,
  residence TEXT   NOT NULL,
  street    TEXT   NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE contact (
  id         BIGINT NOT NULL,
  user_id    BIGINT NOT NULL,
  address_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users (id),
  FOREIGN KEY (address_id) REFERENCES address (id)
);
CREATE TABLE users (
  id   BIGINT PRIMARY KEY NOT NULL,
  name TEXT               NOT NULL
);