CREATE TABLE customer (
  id        BIGINT NOT NULL,
  name      TEXT    NOT NULL,
  last_name TEXT,
  egn       TEXT,
  PRIMARY KEY (id)
);

CREATE TABLE customer_history (
  id          BIGINT NOT NULL DEFAULT nextval('customer_history_id_seq' :: REGCLASS),
  change_date TIMESTAMP WITH TIME ZONE,
  customer_id BIGINT NOT NULL,
  name        TEXT,
  last_name   TEXT,
  egn         TEXT,
  PRIMARY KEY (id),
  FOREIGN KEY (customer_id) REFERENCES customer (id)
);

CREATE OR REPLACE FUNCTION customer_history_procedure()
  RETURNS TRIGGER AS $customer_history$
BEGIN
  INSERT INTO customer_history (change_date, customer_id, name, last_name, egn)
  VALUES (CURRENT_TIMESTAMP, OLD.id, OLD.name, OLD.last_name, OLD.egn);
  RETURN NEW;
END
$customer_history$ LANGUAGE plpgsql;

CREATE TRIGGER customer_history BEFORE UPDATE ON customer
FOR EACH ROW EXECUTE PROCEDURE customer_history_procedure();
