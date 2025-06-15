CREATE TABLE challenges (
    id UUID PRIMARY KEY,
    title TEXT NOT NULL,
    overview TEXT NOT NULL,
    content TEXT NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL
);
