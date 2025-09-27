CREATE TABLE app_settings (
    id UUID PRIMARY KEY,
    setting_key TEXT NOT NULL UNIQUE,
    setting_value TEXT NOT NULL,
    description TEXT NOT NULL,
    value_type TEXT NOT NULL,
    last_modified_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL
);
