ALTER TABLE recruitment
    ALTER COLUMN business_id SET NOT NULL;

ALTER TABLE recruitment
    ALTER COLUMN description TYPE VARCHAR(5000) USING (description::VARCHAR(5000));

ALTER TABLE recruitment
    ALTER COLUMN key_skills TYPE VARCHAR(5000) USING (key_skills::VARCHAR(5000));