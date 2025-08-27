CREATE TABLE behaviors (
    id UUID PRIMARY KEY,
    icon TEXT,
    title TEXT NOT NULL,
    overview TEXT NOT NULL,
    description TEXT NOT NULL,
    content TEXT NOT NULL,
    last_modified_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL
);
