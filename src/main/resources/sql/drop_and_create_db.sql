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
	year_of_release integer NOT NULL,
	description text
);

CREATE TABLE copies (
	copy_id serial PRIMARY KEY,
	film_id integer,
	type int NOT NULL,
	status character varying NOT NULL,
    rent_price numeric NOT NULL DEFAULT 0,
    purchase_price numeric NOT NULL DEFAULT 0,

	FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE CASCADE
);

CREATE TABLE rents (
    rental_id SERIAL PRIMARY KEY,
    film_id SERIAL REFERENCES films(film_id),
    client_id SERIAL REFERENCES users(user_id),
    rent_or_purchase int NOT NULL,
    start_time timestamp NOT NULL,
    end_time timestamp,
    price int NOT NULL
);
