ALTER TABLE challenges
    ADD COLUMN external_id TEXT NOT NULL;

ALTER TABLE behaviors
    ADD COLUMN external_id TEXT NOT NULL;
