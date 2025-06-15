CREATE TABLE interviews (
    id UUID PRIMARY KEY,
    title TEXT NOT NULL,
    time_spent INT,
    feedback TEXT,
    assistant_behavior UUID NOT NULL,
    challenge UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (assistant_behavior) REFERENCES assistant_behaviors(id),
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
