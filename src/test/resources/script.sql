delete from employee;
ALTER TABLE employee ALTER COLUMN id RESTART WITH 1;
INSERT INTO employee (name, email) VALUES ('naushad', 'naushad@gmail.com');
INSERT INTO employee (name, email) VALUES ('shamshad', 'shamshad@gmail.com');
