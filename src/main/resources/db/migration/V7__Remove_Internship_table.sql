ALTER TABLE student
    DROP CONSTRAINT fk6geq7tnjed7u4hvgv1ac6lyh;

ALTER TABLE internship
    DROP CONSTRAINT fksl4g1fgqrdcj8yvqujbd5kgig;

DROP TABLE internship CASCADE;

ALTER TABLE student
    DROP COLUMN faculty_id;