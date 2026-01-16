-- Adminer 4.8.1 MySQL 8.0.41-0ubuntu0.22.04.1 dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS `spring_security`;
USE `spring_security`;

DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `roles` (`id`, `name`) VALUES
(1,	'USER'),
(2,	'SELLER'),
(3,	'ADMIN');

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `users` (`id`, `username`, `password`, `type`) VALUES
(18,	'user',	'$2a$10$prusVirwE6SnRm6J5zdMB.sudNtXvo76qm/8aberE3ucZ3mspbwaG',	'AUTH_BASIC'),
(19,	'seller',	'$2a$10$/7K0udUAKs8h8m7OKH2K1O.wQ9O86MTuiMVj2rjju0sWdPRgIhnp.',	'AUTH_BASIC'),
(20,	'admin',	'$2a$10$D0VP1ccagBQtY6iUb825euAWs4HFFrG2JQh7fBZFjBNXuLMEteWwC',	'AUTH_BASIC'),
(21,	'1173_Adias Afnan Valentino',	NULL,	'OAUTH2_GOOGLE'),
(22,	'adias311',	NULL,	'OAUTH2_GITHUB');

DROP TABLE IF EXISTS `users_roles`;
CREATE TABLE `users_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  KEY `user_id` (`user_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `users_roles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `users_roles_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES
(18,	1),
(19,	1),
(19,	2),
(20,	3),
(21,	1),
(22,	1);

-- 2025-02-18 23:48:05
