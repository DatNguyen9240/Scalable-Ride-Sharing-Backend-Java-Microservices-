-- V1: create trips table (multi-tenant via tenant_id column)
CREATE TABLE IF NOT EXISTS trips (
  id BIGSERIAL PRIMARY KEY,
  tenant_id VARCHAR(100) NOT NULL,
  passenger_id BIGINT NOT NULL,
  driver_id BIGINT,
  pickup_location TEXT NOT NULL,
  destination TEXT NOT NULL,
  status VARCHAR(50) NOT NULL,
  requested_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  started_at TIMESTAMP WITHOUT TIME ZONE,
  completed_at TIMESTAMP WITHOUT TIME ZONE,
  fare NUMERIC(10,2),
  created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_trips_tenant_id ON trips (tenant_id);
CREATE INDEX IF NOT EXISTS idx_trips_tenant_passenger ON trips (tenant_id, passenger_id);
