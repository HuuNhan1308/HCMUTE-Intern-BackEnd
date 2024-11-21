ALTER TABLE recruitment_request
    ADD message_to_student VARCHAR(255);

ALTER TABLE recruitment_request
    ADD point DOUBLE PRECISION;

ALTER TABLE recruitment_request
    ALTER COLUMN recruitment_id SET NOT NULL;