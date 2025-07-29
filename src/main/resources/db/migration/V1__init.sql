-- Création de la table casbin_rule
CREATE TABLE IF NOT EXISTS casbin_rule (
                             id SERIAL PRIMARY KEY,
                             ptype VARCHAR(100) NOT NULL,
                             v0 VARCHAR(100),
                             v1 VARCHAR(100),
                             v2 VARCHAR(100),
                             v3 VARCHAR(100),
                             v4 VARCHAR(100),
                             v5 VARCHAR(100)
);

-- Création de la table users
CREATE TABLE IF NOT EXISTS users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL,
                       role VARCHAR(50),
                       is_admin BOOLEAN DEFAULT false
);

-- Création de la table documents
CREATE TABLE IF NOT EXISTS documents (
                           id SERIAL PRIMARY KEY,
                           title VARCHAR(200) NOT NULL,
                           content TEXT,
                           owner VARCHAR(100) NOT NULL,
                           category VARCHAR(100),
                           shared_with_json JSONB,
                           permissions_json JSONB
);

-- Création de la table casbin_grouping_policy
CREATE TABLE IF NOT EXISTS casbin_grouping_policy (
                                        id SERIAL PRIMARY KEY,
                                        ptype VARCHAR(100) NOT NULL,
                                        v0 VARCHAR(100),
                                        v1 VARCHAR(100)
);