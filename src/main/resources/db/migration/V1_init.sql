-- Migration V1 : Initialisation de la base de données

CREATE TABLE IF NOT EXISTS users (
                                         id SERIAL PRIMARY KEY,
                                         username VARCHAR(50) UNIQUE NOT NULL,
                                         password VARCHAR(255) NOT NULL,
                                         email VARCHAR(100) UNIQUE NOT NULL,
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS casbin_rule (
                                           id SERIAL PRIMARY KEY,
                                           ptype VARCHAR(255),
                                           v0 VARCHAR(255),
                                           v1 VARCHAR(255),
                                           v2 VARCHAR(255),
                                           v3 VARCHAR(255),
                                           v4 VARCHAR(255),
                                           v5 VARCHAR(255)
);


CREATE TABLE IF NOT EXISTS documents (
                                         id SERIAL PRIMARY KEY,
                                         title VARCHAR(255) NOT NULL,
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );