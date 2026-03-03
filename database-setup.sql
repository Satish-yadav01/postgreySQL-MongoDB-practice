-- Database setup script for PostgreSQL CRUD Application

-- Create database (run this as postgres superuser)
CREATE DATABASE crud_db;

-- Connect to the database
\c crud_db;

-- The application will automatically create the users table via Hibernate
-- But if you want to create it manually, use this:

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15),
    address VARCHAR(200),
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index on email for faster lookups
CREATE INDEX IF NOT EXISTS idx_email ON users(email);

-- Insert sample data (optional)
INSERT INTO users (name, email, phone, address, active) VALUES
('John Doe', 'john.doe@example.com', '1234567890', '123 Main St', true),
('Jane Smith', 'jane.smith@example.com', '0987654321', '456 Oak Ave', true),
('Bob Johnson', 'bob.johnson@example.com', '5555555555', '789 Pine Rd', true);

-- Verify the data
SELECT * FROM users;
