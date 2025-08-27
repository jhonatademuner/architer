CREATE TABLE revoked_tokens (
    jti TEXT NOT NULL PRIMARY KEY,
    user_id UUID NOT NULL,
    token_type TEXT NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    revoked_at TIMESTAMP NOT NULL
);
