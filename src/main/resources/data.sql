INSERT INTO ticket (id, name, description, created_on, desired_resolution_date, status, urgency) VALUES (1, 'task 1', 'this is a ticket1', '2022-02-12', '2022-03-12', 'NEW', 'CRITICAL'),(2, 'ticket 2', 'Yes', '2022-01-14', '2022-02-14', 'DONE', 'HIGH'), (3, 'hard_task', 'Ygggfgfgfg', '2022-02-15', '2022-03-15', 'IN_PROGRESS', 'LOW'), (4, 'new task', 'AAAAAAA!', '2022-03-15', '2022-04-15', 'DONE', 'AVERAGE'), (5, 'ticket 5', 'No interesting', '2022-03-12', '2022-04-12', 'APPROVED', 'LOW');

INSERT INTO user (id, first_name, last_name, role, email, password) VALUES (1, 'Den', 'Mit', 'ROLE_EMPLOYEE', 'user1_mogilev@yopmail.com', '$2a$10$BtSb1vIX7synMgLIKximAeNZlpTAae4kZlZMR9xx7wxXZK2s0B4dC'),(2, 'Peter', 'Bubu', 'ROLE_EMPLOYEE', 'user2_mogilev@yopmail.com', '$2a$10$BtSb1vIX7synMgLIKximAeNZlpTAae4kZlZMR9xx7wxXZK2s0B4dC'),(3, 'Asya', 'Asyna', 'ROLE_MANAGER', 'manager1_mogilev@yopmail.com', '$2a$10$BtSb1vIX7synMgLIKximAeNZlpTAae4kZlZMR9xx7wxXZK2s0B4dC'),(4, 'Ivan', 'Ivanov', 'ROLE_MANAGER', 'manager2_mogilev@yopmail.com', '$2a$10$BtSb1vIX7synMgLIKximAeNZlpTAae4kZlZMR9xx7wxXZK2s0B4dC'),(5, 'Inna', 'Inina', 'ROLE_ENGINEER', 'engineer1_mogilev@yopmail.com', '$2a$10$BtSb1vIX7synMgLIKximAeNZlpTAae4kZlZMR9xx7wxXZK2s0B4dC'),(6, 'Roman', 'Romin', 'ROLE_ENGINEER', 'engineer2_mogilev@yopmail.com', '$2a$10$BtSb1vIX7synMgLIKximAeNZlpTAae4kZlZMR9xx7wxXZK2s0B4dC');

INSERT INTO category (id, name) VALUES (1, 'APPLICATION_AND_SERVICES'),(2, 'BENEFITS_AND_PAPER_WORK'),(3, 'HARDWARE_AND_SOFTWARE'),(4, 'PEOPLE_MANAGEMENT'),(5, 'SECURITY_AND_ACCESS'),(6, 'WORKPLACES_AND_FACILITIES');

INSERT INTO history (id, date, action, description) VALUES (1, '2022-01-05 23:15:30', 'Ticket is created', 'Oooooooo'),(2, '2022-03-04 21:12:17', 'Ticket is created', 'Ticket is created'),(3, '2022-02-02 12:01:03', 'Ticket is created', 'Wow!'),(4, '2021-12-12 07:03:12', 'Ticket is created', 'Noooo!'),(5, '2022-04-01 10:03:52', 'Ticket is created', 'Ticket is created');

INSERT INTO comment (id, text, date) VALUES (1, 'Oooooooo', '2022-02-01 23:15:30'),(2, 'Heeellooo', '2022-03-03 21:12:17'),(3, 'It is bad', '2022-04-04 12:01:03'),(4, 'Okey', '2022-02-04 07:03:12'),(5, 'Not good', '2022-04-01 10:03:52');

UPDATE ticket SET category_id = 1, assignee_id = 5, approver_id = 3, owner_id = 1 WHERE id = 1;
UPDATE ticket SET category_id = 1, assignee_id = 6, approver_id = 4, owner_id = 2 WHERE id = 2;
UPDATE ticket SET category_id = 3, assignee_id = 6, approver_id = 4, owner_id = 1 WHERE id = 3;
UPDATE ticket SET category_id = 5, assignee_id = 5, approver_id = 3, owner_id = 4 WHERE id = 4;
UPDATE ticket SET category_id = 2, assignee_id = 6, approver_id = 4, owner_id = 2 WHERE id = 5;

UPDATE history SET ticket_id = 1, user_id = 1 WHERE id = 1;
UPDATE history SET ticket_id = 3, user_id = 6 WHERE id = 2;
UPDATE history SET ticket_id = 2, user_id = 3 WHERE id = 3;
UPDATE history SET ticket_id = 1, user_id = 5 WHERE id = 4;
UPDATE history SET ticket_id = 1, user_id = 4 WHERE id = 5;

UPDATE comment SET ticket_id = 1, user_id = 1 WHERE id = 1;
UPDATE comment SET ticket_id = 2, user_id = 6 WHERE id = 2;
UPDATE comment SET ticket_id = 3, user_id = 3 WHERE id = 3;
UPDATE comment SET ticket_id = 2, user_id = 5 WHERE id = 4;
UPDATE comment SET ticket_id = 5, user_id = 4 WHERE id = 5;

