USE gotthere_database;

CREATE TABLE `user` (
	id int(11) PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL
);

INSERT INTO `user` (username, password) VALUES ('admin','admin');