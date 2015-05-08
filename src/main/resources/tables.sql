SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";

CREATE TABLE IF NOT EXISTS `swreloaded_player` (
  `player_id`    INT(6) UNSIGNED NOT NULL AUTO_INCREMENT,
  `uuid`  VARCHAR(255)     NOT NULL UNIQUE,
  `playername`  VARCHAR(60)     NOT NULL UNIQUE,
  `first_seen`   DATETIME        NOT NULL,
  `last_seen`    DATETIME        NOT NULL,
  `score`        INT(6)          NOT NULL DEFAULT 0,
  `balance`      INT(6)          NOT NULL DEFAULT 0,
  `games_played` INT(6) UNSIGNED NOT NULL DEFAULT 0,
  `games_won`    INT(6) UNSIGNED NOT NULL DEFAULT 0,
  `kills`        INT(6) UNSIGNED NOT NULL DEFAULT 0,
  `deaths`       INT(6) UNSIGNED NOT NULL DEFAULT 0,
  `killdeath`    DECIMAL(5,2) UNSIGNED NOT NULL DEFAULT 0,
  `blocksplaced` INT(10) UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY (`player_id`),
  KEY (`uuid`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =latin1;
