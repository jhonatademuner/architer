CREATE TABLE users (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL,
    last_modified_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP
);
