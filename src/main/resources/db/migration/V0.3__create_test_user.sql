DO $$
DECLARE created_id INTEGER;

BEGIN
INSERT INTO thing_tracker.user (email, full_name, password) VALUES ('testUser@gmail.com', 'testUser', 'testUserPassword') RETURNING id INTO created_id;
INSERT INTO thing_tracker.user_role (user_id, role_id) VALUES (created_id, 1);

INSERT INTO thing_tracker.user (email, full_name, password) VALUES ('testUser2@gmail.com', 'testUser2', 'testUserPassword2') RETURNING id INTO created_id;
INSERT INTO thing_tracker.user_role (user_id, role_id) VALUES (created_id, 1);
INSERT INTO thing_tracker.user_role (user_id, role_id) VALUES (created_id, 2);

END $$