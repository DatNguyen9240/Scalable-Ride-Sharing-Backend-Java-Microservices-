-- V2: add tenant_id column for multi-tenant support and populate legacy rows
ALTER TABLE trips ADD COLUMN IF NOT EXISTS tenant_id VARCHAR(100);

-- Mark any existing rows as coming from a legacy tenant so NOT NULL can be enforced
UPDATE trips SET tenant_id = 'baseline_tenant' WHERE tenant_id IS NULL;

ALTER TABLE trips ALTER COLUMN tenant_id SET NOT NULL;

CREATE INDEX IF NOT EXISTS idx_trips_tenant_id ON trips (tenant_id);
CREATE INDEX IF NOT EXISTS idx_trips_tenant_passenger ON trips (tenant_id, passenger_id);
