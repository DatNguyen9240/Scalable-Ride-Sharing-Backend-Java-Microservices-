-- Add tenant_id to users so multi-tenant queries work
ALTER TABLE users
ADD COLUMN tenant_id VARCHAR(100) NOT NULL DEFAULT 'default';

-- Optional: create index for lookups by tenant
CREATE INDEX IF NOT EXISTS idx_users_tenant_id ON users(tenant_id);
