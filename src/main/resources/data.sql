DROP TABLE IF EXISTS binary_data;

CREATE TABLE binary_data (
  id BIGINT PRIMARY KEY,
  left BLOB DEFAULT NULL,
  right BLOB DEFAULT NULL
);