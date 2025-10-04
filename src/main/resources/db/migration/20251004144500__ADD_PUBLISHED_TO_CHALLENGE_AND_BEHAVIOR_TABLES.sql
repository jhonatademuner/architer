ALTER TABLE challenges
    ADD COLUMN published BOOLEAN NOT NULL;

ALTER TABLE behaviors
    ADD COLUMN published BOOLEAN NOT NULL;
