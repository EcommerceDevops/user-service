CREATE TABLE credentials (
    credential_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    role VARCHAR(255),
    is_enabled BOOLEAN DEFAULT false,
    is_account_non_expired BOOLEAN DEFAULT true,
    is_account_non_locked BOOLEAN DEFAULT true,
    is_credentials_non_expired BOOLEAN DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT FK_CredentialUser FOREIGN KEY (user_id) REFERENCES users(user_id)
);