CREATE TABLE interviews (
    id UUID PRIMARY KEY,
    title TEXT NOT NULL,
    time_spent INT,
    feedback TEXT,
    behavior UUID NOT NULL,
    challenge UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (behavior) REFERENCES behaviors(id),
    FOREIGN KEY (challenge) REFERENCES challenges(id)
);

CREATE TABLE interview_messages (
    id UUID PRIMARY KEY,
    role TEXT NOT NULL,
    content TEXT NOT NULL,
    interview UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (interview) REFERENCES interviews(id)
);
