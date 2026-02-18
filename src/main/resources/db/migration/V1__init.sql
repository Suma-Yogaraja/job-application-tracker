--create users tabble
CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

--create Applications Table
CREATE TABLE applications(
       id BIGSERIAL PRIMARY KEY,
       user_id BIGINT NOT NULL,
       company VARCHAR(255) NOT NULL,
       role VARCHAR(255) NOT NULL,
       status VARCHAR(255) NOT NULL,
       notes TEXT,
       link TEXT,
       applied_date DATE,
       created_at TIMESTAMP NOT NULL DEFAULT NOW(),
       CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

--create interview table
CREATE TABLE interviews (
  id BIGSERIAL PRIMARY KEY,
  application_id BIGINT NOT NULL REFERENCES applications(id) ON DELETE CASCADE,
  round_type VARCHAR(30) NOT NULL,
  scheduled_at TIMESTAMP NOT NULL,
  notes TEXT,
  created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_interviews_application_id ON interviews(application_id);

