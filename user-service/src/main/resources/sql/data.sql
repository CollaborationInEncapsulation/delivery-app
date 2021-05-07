DELETE FROM users WHERE ID IS NOT NULL;

INSERT INTO users
VALUES ( RANDOM_UUID(), 'user', 'user@example.com', '$2a$06$fJhpqOTtOsY6MpxpnwjPnO97TsrQsoc.C.DNtWJyu4yQHR/9PkiSK', 'ROLE_USER', '+123456789001', 'hello world, land, hello, 012345', NOW());
INSERT INTO users
VALUES ( RANDOM_UUID(), 'admin', 'admin@example.com', '$2a$06$fJhpqOTtOsY6MpxpnwjPnO97TsrQsoc.C.DNtWJyu4yQHR/9PkiSK', 'ROLE_USER,ROLE_ADMIN', '+123456789002', 'hi world, land, hello, 012345', NOW());

