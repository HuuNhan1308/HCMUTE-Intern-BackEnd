ALTER TABLE student
    DROP CONSTRAINT fk6geq7tnjed7u4hvgv1ac6lyh;

ALTER TABLE student
    DROP COLUMN faculty_id;

DROP SEQUENCE permission_seq CASCADE;

ALTER TABLE recruitment
    ALTER COLUMN business_id SET NOT NULL;

ALTER TABLE business
    ALTER COLUMN managed_by SET NOT NULL;

ALTER TABLE recruitment_request
    ALTER COLUMN recruitment_id SET NOT NULL;