CREATE TABLE users.role
(
    id            VARCHAR(255) NOT NULL,
    name          VARCHAR(255),
    employee_role VARCHAR(255),
    CONSTRAINT pk_role PRIMARY KEY (id)
);

ALTER TABLE users.role
    ADD CONSTRAINT F_ROLE_ON_EMPLOYEE_ROLE FOREIGN KEY (employee_role) REFERENCES users.employees (id);