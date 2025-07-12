CREATE TABLE movie_entity (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    overview TEXT,
    release_date DATE,
    vote_average DOUBLE PRECISION,
    poster_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
