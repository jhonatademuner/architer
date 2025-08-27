CREATE TABLE challenges (
    id UUID PRIMARY KEY,
    icon TEXT,
    title TEXT NOT NULL,
    overview TEXT NOT NULL,
    description TEXT NOT NULL,
    content TEXT NOT NULL,
    category TEXT NOT NULL,
    difficulty TEXT NOT NULL,
    last_modified_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL
);
