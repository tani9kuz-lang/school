-- liquibase formatted sql

-- changeset tkuznetsova:1
CREATE TABLE faculty (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         color VARCHAR(255) NOT NULL,
                         CONSTRAINT unique_name_color UNIQUE (name, color)
);

-- changeset tkuznetsova:2
CREATE TABLE student (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL UNIQUE,
                         age INTEGER DEFAULT 20 CHECK (age >= 16),
                         faculty_id BIGINT,
                         CONSTRAINT fk_faculty FOREIGN KEY (faculty_id) REFERENCES faculty (id) ON DELETE SET NULL
);
-- changeset tkuznetsova:3
CREATE TABLE avatar (
                        id BIGSERIAL PRIMARY KEY,
                        file_path VARCHAR(255),
                        file_size BIGINT NOT NULL,
                        media_type VARCHAR(255),
                        data OID,
                        student_id BIGINT,
                        CONSTRAINT fk_avatar_student FOREIGN KEY (student_id) REFERENCES student (id) ON DELETE CASCADE
);


-- changeset tkuznetsova:4
CREATE INDEX student_name_idx ON student (name);

-- changeset tkuznetsova:5
CREATE INDEX faculty_name_color_idx ON faculty (name, color);
-- changeset tkuznetsova:6
CREATE SEQUENCE avatar_seq START WITH 1 INCREMENT BY 50;
