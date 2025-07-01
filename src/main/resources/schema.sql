CREATE TABLE IF NOT EXISTS documents (
    id VARCHAR(255) PRIMARY KEY,
    title VARCHAR(500),
    content TEXT,
    owner VARCHAR(255),
    shared_with_json TEXT,
    permissions_json TEXT
);