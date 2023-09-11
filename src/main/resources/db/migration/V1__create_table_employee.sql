CREATE TABLE users.employees
(
    id       VARCHAR(255) NOT NULL,
    name     VARCHAR(255),
    email    VARCHAR(255),
    password VARCHAR(255),
    CONSTRAINT pk_employees PRIMARY KEY (id)
);
