-- user_message
CREATE TABLE thing_tracker.user_message
(
  id serial PRIMARY KEY NOT NULL,
  message varchar(512) NOT NULL,
  user_id int NOT NULL,
  date TIMESTAMP,
  read boolean,
  CONSTRAINT user_message_user_id_fk FOREIGN KEY (user_id) REFERENCES thing_tracker."user" (id)
);
CREATE UNIQUE INDEX user_message_id_uindex ON thing_tracker.user_message (id);


-- message_action
CREATE TABLE thing_tracker.message_action
(
  id serial PRIMARY KEY NOT NULL,
  action varchar(127) NOT NULL
);
CREATE UNIQUE INDEX message_action_id_uindex ON thing_tracker.message_action (id);
CREATE UNIQUE INDEX message_action_action_uindex ON thing_tracker.message_action (action);



-- user_message_action
CREATE TABLE thing_tracker.user_message_action
(
  id serial PRIMARY KEY NOT NULL,
  user_message_id int NOT NULL,
  message_action_id int NOT NULL,
  CONSTRAINT user_message_action_user_message_id_fk FOREIGN KEY (user_message_id) REFERENCES thing_tracker.user_message (id),
  CONSTRAINT user_message_action_message_action_id_fk FOREIGN KEY (message_action_id) REFERENCES thing_tracker.message_action (id)
);
CREATE UNIQUE INDEX user_message_action_id_uindex ON thing_tracker.user_message_action (id);


INSERT INTO thing_tracker.message_action (id, action) VALUES (1, 'ACCEPT');
INSERT INTO thing_tracker.message_action (id, action) VALUES (2, 'REJECT');
INSERT INTO thing_tracker.message_action (id, action) VALUES (3, 'READ');