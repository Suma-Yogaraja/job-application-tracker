CREATE INDEX IF NOT EXISTS idx_app_user_created_at
ON applications (user_id, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_app_user_status
ON applications (user_id, status);
