--
-- PostgreSQL database dump
--

-- Dumped from database version 10.10 (Ubuntu 10.10-0ubuntu0.18.04.1)
-- Dumped by pg_dump version 10.10 (Ubuntu 10.10-0ubuntu0.18.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: thing_tracker; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA thing_tracker;


--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: expense; Type: TABLE; Schema: thing_tracker; Owner: -
--

CREATE TABLE thing_tracker.expense (
    id integer NOT NULL,
    user_id integer NOT NULL,
    price integer NOT NULL,
    date timestamp without time zone NOT NULL,
    comment character varying(255)
);


--
-- Name: expense_id_seq; Type: SEQUENCE; Schema: thing_tracker; Owner: -
--

CREATE SEQUENCE thing_tracker.expense_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: expense_id_seq; Type: SEQUENCE OWNED BY; Schema: thing_tracker; Owner: -
--

ALTER SEQUENCE thing_tracker.expense_id_seq OWNED BY thing_tracker.expense.id;


--
-- Name: expense_to_expense_type_dict; Type: TABLE; Schema: thing_tracker; Owner: -
--

CREATE TABLE thing_tracker.expense_to_expense_type_dict (
    id integer NOT NULL,
    expense_id integer NOT NULL,
    expense_type_id integer NOT NULL
);


--
-- Name: expense_to_expense_type_dict_id_seq; Type: SEQUENCE; Schema: thing_tracker; Owner: -
--

CREATE SEQUENCE thing_tracker.expense_to_expense_type_dict_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: expense_to_expense_type_dict_id_seq; Type: SEQUENCE OWNED BY; Schema: thing_tracker; Owner: -
--

ALTER SEQUENCE thing_tracker.expense_to_expense_type_dict_id_seq OWNED BY thing_tracker.expense_to_expense_type_dict.id;


--
-- Name: expense_type_dict; Type: TABLE; Schema: thing_tracker; Owner: -
--

CREATE TABLE thing_tracker.expense_type_dict (
    id integer NOT NULL,
    user_id integer NOT NULL,
    used_count integer,
    name character varying(64) NOT NULL
);


--
-- Name: expense_type_dict_id_seq; Type: SEQUENCE; Schema: thing_tracker; Owner: -
--

CREATE SEQUENCE thing_tracker.expense_type_dict_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: expense_type_dict_id_seq; Type: SEQUENCE OWNED BY; Schema: thing_tracker; Owner: -
--

ALTER SEQUENCE thing_tracker.expense_type_dict_id_seq OWNED BY thing_tracker.expense_type_dict.id;


--
-- Name: group; Type: TABLE; Schema: thing_tracker; Owner: -
--

CREATE TABLE thing_tracker."group" (
    id integer NOT NULL,
    creator_id integer NOT NULL,
    name character varying(64) NOT NULL
);


--
-- Name: group_id_seq; Type: SEQUENCE; Schema: thing_tracker; Owner: -
--

CREATE SEQUENCE thing_tracker.group_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: group_id_seq; Type: SEQUENCE OWNED BY; Schema: thing_tracker; Owner: -
--

ALTER SEQUENCE thing_tracker.group_id_seq OWNED BY thing_tracker."group".id;


--
-- Name: group_user; Type: TABLE; Schema: thing_tracker; Owner: -
--

CREATE TABLE thing_tracker.group_user (
    id integer NOT NULL,
    group_id integer NOT NULL,
    user_id integer NOT NULL
);


--
-- Name: group_user_id_seq; Type: SEQUENCE; Schema: thing_tracker; Owner: -
--

CREATE SEQUENCE thing_tracker.group_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: group_user_id_seq; Type: SEQUENCE OWNED BY; Schema: thing_tracker; Owner: -
--

ALTER SEQUENCE thing_tracker.group_user_id_seq OWNED BY thing_tracker.group_user.id;


--
-- Name: role; Type: TABLE; Schema: thing_tracker; Owner: -
--

CREATE TABLE thing_tracker.role (
    id integer NOT NULL,
    code character varying(32) NOT NULL,
    name character varying(64) NOT NULL
);


--
-- Name: role_id_seq; Type: SEQUENCE; Schema: thing_tracker; Owner: -
--

CREATE SEQUENCE thing_tracker.role_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: role_id_seq; Type: SEQUENCE OWNED BY; Schema: thing_tracker; Owner: -
--

ALTER SEQUENCE thing_tracker.role_id_seq OWNED BY thing_tracker.role.id;


--
-- Name: user; Type: TABLE; Schema: thing_tracker; Owner: -
--

CREATE TABLE thing_tracker."user" (
    id integer NOT NULL,
    email character varying(64) NOT NULL,
    full_name character varying(64) NOT NULL,
    password character varying(255)
);


--
-- Name: user_id_seq; Type: SEQUENCE; Schema: thing_tracker; Owner: -
--

CREATE SEQUENCE thing_tracker.user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: user_id_seq; Type: SEQUENCE OWNED BY; Schema: thing_tracker; Owner: -
--

ALTER SEQUENCE thing_tracker.user_id_seq OWNED BY thing_tracker."user".id;


--
-- Name: user_invite; Type: TABLE; Schema: thing_tracker; Owner: -
--

CREATE TABLE thing_tracker.user_invite (
    id integer NOT NULL,
    inviter_id integer NOT NULL,
    target_id integer NOT NULL,
    group_id integer NOT NULL
);


--
-- Name: user_invite_id_seq; Type: SEQUENCE; Schema: thing_tracker; Owner: -
--

CREATE SEQUENCE thing_tracker.user_invite_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: user_invite_id_seq; Type: SEQUENCE OWNED BY; Schema: thing_tracker; Owner: -
--

ALTER SEQUENCE thing_tracker.user_invite_id_seq OWNED BY thing_tracker.user_invite.id;


--
-- Name: user_role; Type: TABLE; Schema: thing_tracker; Owner: -
--

CREATE TABLE thing_tracker.user_role (
    column_1 integer NOT NULL,
    user_id integer NOT NULL,
    role_id integer NOT NULL
);


--
-- Name: user_role_column_1_seq; Type: SEQUENCE; Schema: thing_tracker; Owner: -
--

CREATE SEQUENCE thing_tracker.user_role_column_1_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: user_role_column_1_seq; Type: SEQUENCE OWNED BY; Schema: thing_tracker; Owner: -
--

ALTER SEQUENCE thing_tracker.user_role_column_1_seq OWNED BY thing_tracker.user_role.column_1;


--
-- Name: expense id; Type: DEFAULT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.expense ALTER COLUMN id SET DEFAULT nextval('thing_tracker.expense_id_seq'::regclass);


--
-- Name: expense_to_expense_type_dict id; Type: DEFAULT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.expense_to_expense_type_dict ALTER COLUMN id SET DEFAULT nextval('thing_tracker.expense_to_expense_type_dict_id_seq'::regclass);


--
-- Name: expense_type_dict id; Type: DEFAULT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.expense_type_dict ALTER COLUMN id SET DEFAULT nextval('thing_tracker.expense_type_dict_id_seq'::regclass);


--
-- Name: group id; Type: DEFAULT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker."group" ALTER COLUMN id SET DEFAULT nextval('thing_tracker.group_id_seq'::regclass);


--
-- Name: group_user id; Type: DEFAULT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.group_user ALTER COLUMN id SET DEFAULT nextval('thing_tracker.group_user_id_seq'::regclass);


--
-- Name: role id; Type: DEFAULT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.role ALTER COLUMN id SET DEFAULT nextval('thing_tracker.role_id_seq'::regclass);


--
-- Name: user id; Type: DEFAULT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker."user" ALTER COLUMN id SET DEFAULT nextval('thing_tracker.user_id_seq'::regclass);


--
-- Name: user_invite id; Type: DEFAULT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.user_invite ALTER COLUMN id SET DEFAULT nextval('thing_tracker.user_invite_id_seq'::regclass);


--
-- Name: user_role column_1; Type: DEFAULT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.user_role ALTER COLUMN column_1 SET DEFAULT nextval('thing_tracker.user_role_column_1_seq'::regclass);


--
-- Name: expense expense_pkey; Type: CONSTRAINT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.expense
    ADD CONSTRAINT expense_pkey PRIMARY KEY (id);


--
-- Name: expense_to_expense_type_dict expense_to_expense_type_dict_pkey; Type: CONSTRAINT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.expense_to_expense_type_dict
    ADD CONSTRAINT expense_to_expense_type_dict_pkey PRIMARY KEY (id);


--
-- Name: expense_type_dict expense_type_dict_pkey; Type: CONSTRAINT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.expense_type_dict
    ADD CONSTRAINT expense_type_dict_pkey PRIMARY KEY (id);


--
-- Name: group group_pkey; Type: CONSTRAINT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker."group"
    ADD CONSTRAINT group_pkey PRIMARY KEY (id);


--
-- Name: group_user group_user_pkey; Type: CONSTRAINT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.group_user
    ADD CONSTRAINT group_user_pkey PRIMARY KEY (id);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- Name: user_invite user_invite_pkey; Type: CONSTRAINT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.user_invite
    ADD CONSTRAINT user_invite_pkey PRIMARY KEY (id);


--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: user_role user_role_pkey; Type: CONSTRAINT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (column_1);


--
-- Name: expense_id_uindex; Type: INDEX; Schema: thing_tracker; Owner: -
--

CREATE UNIQUE INDEX expense_id_uindex ON thing_tracker.expense USING btree (id);


--
-- Name: expense_to_expense_type_dict_id_uindex; Type: INDEX; Schema: thing_tracker; Owner: -
--

CREATE UNIQUE INDEX expense_to_expense_type_dict_id_uindex ON thing_tracker.expense_to_expense_type_dict USING btree (id);


--
-- Name: expense_type_dict_id_uindex; Type: INDEX; Schema: thing_tracker; Owner: -
--

CREATE UNIQUE INDEX expense_type_dict_id_uindex ON thing_tracker.expense_type_dict USING btree (id);


--
-- Name: expense_type_dict_name_uindex; Type: INDEX; Schema: thing_tracker; Owner: -
--

CREATE UNIQUE INDEX expense_type_dict_name_uindex ON thing_tracker.expense_type_dict USING btree (name);


--
-- Name: group_id_uindex; Type: INDEX; Schema: thing_tracker; Owner: -
--

CREATE UNIQUE INDEX group_id_uindex ON thing_tracker."group" USING btree (id);


--
-- Name: group_name_uindex; Type: INDEX; Schema: thing_tracker; Owner: -
--

CREATE UNIQUE INDEX group_name_uindex ON thing_tracker."group" USING btree (name);


--
-- Name: group_user_id_uindex; Type: INDEX; Schema: thing_tracker; Owner: -
--

CREATE UNIQUE INDEX group_user_id_uindex ON thing_tracker.group_user USING btree (id);


--
-- Name: role_code_uindex; Type: INDEX; Schema: thing_tracker; Owner: -
--

CREATE UNIQUE INDEX role_code_uindex ON thing_tracker.role USING btree (code);


--
-- Name: role_id_uindex; Type: INDEX; Schema: thing_tracker; Owner: -
--

CREATE UNIQUE INDEX role_id_uindex ON thing_tracker.role USING btree (id);


--
-- Name: user_id_uindex; Type: INDEX; Schema: thing_tracker; Owner: -
--

CREATE UNIQUE INDEX user_id_uindex ON thing_tracker."user" USING btree (id);


--
-- Name: user_invite_id_uindex; Type: INDEX; Schema: thing_tracker; Owner: -
--

CREATE UNIQUE INDEX user_invite_id_uindex ON thing_tracker.user_invite USING btree (id);


--
-- Name: user_role_column_1_uindex; Type: INDEX; Schema: thing_tracker; Owner: -
--

CREATE UNIQUE INDEX user_role_column_1_uindex ON thing_tracker.user_role USING btree (column_1);


--
-- Name: user_username_uindex; Type: INDEX; Schema: thing_tracker; Owner: -
--

CREATE UNIQUE INDEX user_username_uindex ON thing_tracker."user" USING btree (email);


--
-- Name: expense_to_expense_type_dict expense_to_expense_type_dict_expense_id_fk; Type: FK CONSTRAINT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.expense_to_expense_type_dict
    ADD CONSTRAINT expense_to_expense_type_dict_expense_id_fk FOREIGN KEY (expense_id) REFERENCES thing_tracker.expense(id);


--
-- Name: expense_to_expense_type_dict expense_to_expense_type_dict_expense_type_dict_id_fk; Type: FK CONSTRAINT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.expense_to_expense_type_dict
    ADD CONSTRAINT expense_to_expense_type_dict_expense_type_dict_id_fk FOREIGN KEY (expense_type_id) REFERENCES thing_tracker.expense_type_dict(id);


--
-- Name: expense_type_dict expense_type_dict_user_id_fk; Type: FK CONSTRAINT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.expense_type_dict
    ADD CONSTRAINT expense_type_dict_user_id_fk FOREIGN KEY (user_id) REFERENCES thing_tracker."user"(id);


--
-- Name: expense expense_user_id_fk; Type: FK CONSTRAINT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.expense
    ADD CONSTRAINT expense_user_id_fk FOREIGN KEY (user_id) REFERENCES thing_tracker."user"(id);


--
-- Name: group_user group_user_group_id_fk; Type: FK CONSTRAINT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.group_user
    ADD CONSTRAINT group_user_group_id_fk FOREIGN KEY (group_id) REFERENCES thing_tracker."group"(id);


--
-- Name: group group_user_id_fk; Type: FK CONSTRAINT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker."group"
    ADD CONSTRAINT group_user_id_fk FOREIGN KEY (creator_id) REFERENCES thing_tracker."user"(id);


--
-- Name: group_user group_user_user_id_fk; Type: FK CONSTRAINT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.group_user
    ADD CONSTRAINT group_user_user_id_fk FOREIGN KEY (user_id) REFERENCES thing_tracker."user"(id);


--
-- Name: user_invite user_invite_group_id_fk; Type: FK CONSTRAINT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.user_invite
    ADD CONSTRAINT user_invite_group_id_fk FOREIGN KEY (group_id) REFERENCES thing_tracker."group"(id);


--
-- Name: user_invite user_invite_user_id_fk; Type: FK CONSTRAINT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.user_invite
    ADD CONSTRAINT user_invite_user_id_fk FOREIGN KEY (inviter_id) REFERENCES thing_tracker."user"(id);


--
-- Name: user_invite user_invite_user_id_fk_2; Type: FK CONSTRAINT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.user_invite
    ADD CONSTRAINT user_invite_user_id_fk_2 FOREIGN KEY (target_id) REFERENCES thing_tracker."user"(id);


--
-- Name: user_role user_role_role_id_fk; Type: FK CONSTRAINT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.user_role
    ADD CONSTRAINT user_role_role_id_fk FOREIGN KEY (role_id) REFERENCES thing_tracker.role(id);


--
-- Name: user_role user_role_user_id_fk; Type: FK CONSTRAINT; Schema: thing_tracker; Owner: -
--

ALTER TABLE ONLY thing_tracker.user_role
    ADD CONSTRAINT user_role_user_id_fk FOREIGN KEY (user_id) REFERENCES thing_tracker."user"(id);


--
-- PostgreSQL database dump complete
--

