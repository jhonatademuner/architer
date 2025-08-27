CREATE TABLE interviews (
    id UUID PRIMARY KEY,
    title TEXT NOT NULL,
    status TEXT NOT NULL,
    duration INT,
    behavior_id UUID NOT NULL,
    challenge_id UUID NOT NULL,
    seniority TEXT NOT NULL,
    user_id UUID NOT NULL,
    last_modified_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (behavior_id) REFERENCES behaviors(id),
    FOREIGN KEY (challenge_id) REFERENCES challenges(id)
);

CREATE TABLE interview_messages (
    id UUID PRIMARY KEY,
    interview_id UUID NOT NULL,
    role TEXT NOT NULL,
    content TEXT NOT NULL,
    sent_at TIMESTAMP NOT NULL,
    FOREIGN KEY (interview_id) REFERENCES interviews(id)
);

CREATE TABLE interview_feedbacks (
	id UUID PRIMARY KEY,
	interview_id UUID NOT NULL,
	overall_evaluation TEXT NOT NULL,
	overall_remarks TEXT NOT NULL,
	created_at TIMESTAMP NOT NULL,
	FOREIGN KEY (interview_id) REFERENCES interviews(id)
);

CREATE TABLE interview_detailed_feedbacks (
	id UUID PRIMARY KEY,
	feedback_id UUID NOT NULL,
	subject TEXT NOT NULL,
	evaluation TEXT NOT NULL,
	remarks TEXT NOT NULL,
	FOREIGN KEY (feedback_id) REFERENCES interview_feedbacks(id)
);
