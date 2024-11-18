CREATE TABLE avatar
(
    avatar_id     VARCHAR(255) NOT NULL,
    date_created  TIMESTAMP WITHOUT TIME ZONE,
    date_modified TIMESTAMP WITHOUT TIME ZONE,
    date_deleted  TIMESTAMP WITHOUT TIME ZONE,
    deleted       BOOLEAN,
    file_name     VARCHAR(255),
    file_type     VARCHAR(255),
    owner_id      VARCHAR(255),
    file_data     BYTEA,
    CONSTRAINT pk_avatar PRIMARY KEY (avatar_id)
);