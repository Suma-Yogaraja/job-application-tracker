CREATE TABLE IF NOT EXISTS activity_log (
    id BIGSERIAL PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id BIGINT,
    application_id BIGINT,
    interview_id BIGINT,
    payload JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_activity_app_created
ON activity_log (application_id, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_activity_interview_created
ON activity_log (interview_id, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_activity_created
ON activity_log (created_at DESC);
