DO $$
DECLARE created_id INTEGER;
DECLARE test_user_id INTEGER;

BEGIN

SELECT id INTO test_user_id FROM thing_tracker.user WHERE email = 'testUser@gmail.com';
INSERT INTO thing_tracker.group (creator_id, name) VALUES (test_user_id, 'testGroup') RETURNING id INTO created_id;
INSERT INTO thing_tracker.group_user (group_id, user_id) VALUES (created_id, test_user_id);

END $$