CREATE TABLE companies
(
    id     SERIAL PRIMARY KEY,
    name   VARCHAR(255)     NOT NULL,
    budget DOUBLE PRECISION NOT NULL
);

CREATE TABLE company_employees
(
    company_id  BIGINT NOT NULL REFERENCES companies (id) ON DELETE CASCADE,
    employee_id BIGINT NOT NULL
);
