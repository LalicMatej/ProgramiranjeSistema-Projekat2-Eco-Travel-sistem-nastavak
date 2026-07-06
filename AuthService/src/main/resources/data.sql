-- Insertovanje 5 rolova
INSERT IGNORE INTO roles (id, role) VALUES (1, 'ROLE_ADMIN');
INSERT IGNORE INTO roles (id, role) VALUES (2, 'ROLE_GUEST');
INSERT IGNORE INTO roles (id, role) VALUES (3, 'ROLE_GUIDE');
INSERT IGNORE INTO roles (id, role) VALUES (4, 'ROLE_HOST');
INSERT IGNORE INTO roles (id, role) VALUES (5, 'ROLE_SUPPORT');

-- Insertovanje test korisnika
INSERT IGNORE INTO users (id, username, api_key)
VALUES (1, 'admin', 'RAF-ADMIN-1744320000-abc123def456-admin');

INSERT IGNORE INTO users (id, username, api_key)
VALUES (2, 'guest1', 'RAF-GUEST-1744320000-ghi789jkl012-guest');

INSERT IGNORE INTO users (id, username, api_key)
VALUES (3, 'guide1', 'RAF-GUIDE-1744320000-mno345pqr678-guide');

-- Dodela uloga korisnicima
INSERT IGNORE INTO user_roles (user_id, role_id) VALUES (1, 1); -- admin -> ADMIN
INSERT IGNORE INTO user_roles (user_id, role_id) VALUES (2, 2); -- guest1 -> GUEST
INSERT IGNORE INTO user_roles (user_id, role_id) VALUES (3, 3); -- guide1 -> GUIDE
INSERT IGNORE INTO user_roles (user_id, role_id) VALUES (1, 4); -- admin -> HOST
INSERT IGNORE INTO user_roles (user_id, role_id) VALUES (1, 5); -- admin -> SUPPORT

-- Postavi auto-increment sekvence iznad seed podataka
ALTER TABLE roles AUTO_INCREMENT = 6;
ALTER TABLE users AUTO_INCREMENT = 4;
