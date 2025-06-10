CREATE TABLE verification_tokens (
    verification_token_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    credential_id INT,
    verif_token VARCHAR(255),
    expire_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT FK_TokenCredential FOREIGN KEY (credential_id) REFERENCES credentials(credential_id) ON DELETE CASCADE
);