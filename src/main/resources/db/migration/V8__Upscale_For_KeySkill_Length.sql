ALTER TABLE recruitment
    ALTER COLUMN business_id SET NOT NULL;

ALTER TABLE recruitment
    ALTER COLUMN key_skills TYPE VARCHAR(1000) USING (key_skills::VARCHAR(1000));