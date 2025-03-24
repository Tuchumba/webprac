DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS copies CASCADE;
DROP TABLE IF EXISTS rents CASCADE;

CREATE TABLE users (
	user_id serial PRIMARY KEY,
	username character varying NOT NULL,
	email character varying NOT NULL UNIQUE,
    phone char(11) NOT NULL UNIQUE
);

CREATE TABLE films (
	film_id serial PRIMARY KEY,
	title character varying NOT NULL,
	genre character varying NOT NULL,
	company character varying NOT NULL,
	director character varying NOT NULL,
	year_of_release char(4) NOT NULL,
	description text NOT NULL
);

CREATE TABLE copies (
	copy_id serial PRIMARY KEY,
	film_id integer,
	type character varying NOT NULL,
	status character varying NOT NULL,
	price numeric NOT NULL DEFAULT 0,
	
	FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE CASCADE
);

CREATE TABLE rents (
	rent_id serial PRIMARY KEY,
	user_id integer NOT NULL,
	copy_id integer NOT NULL,
	date_of_transfer date NOT NULL,
	date_of_receipt date NOT NULL,
	actual_date_of_receipt date DEFAULT NULL,
	transfer_amount numeric NOT NULL,

	FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
	FOREIGN KEY (copy_id) REFERENCES copies(copy_id) ON DELETE CASCADE
);
