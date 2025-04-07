CREATE TABLE IF NOT EXISTS companies
(
    id     SERIAL PRIMARY KEY,
    name   VARCHAR(255) NOT NULL,
    budget BIGINT       NOT NULL CHECK (budget >= 0)
);

CREATE TABLE IF NOT EXISTS company_employees
(
    company_id  BIGINT NOT NULL REFERENCES companies (id) ON DELETE CASCADE,
    employee_id BIGINT NOT NULL,
    PRIMARY KEY (company_id, employee_id)
);