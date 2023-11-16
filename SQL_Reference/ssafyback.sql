-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema ssafyback
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ssafyback` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `ssafyback` ;

-- -----------------------------------------------------
-- Table `ssafyback`.`members`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ssafyback`.`members` ;

CREATE TABLE IF NOT EXISTS `ssafyback`.`members` (
  `user_id` VARCHAR(16) NOT NULL,
  `user_name` VARCHAR(20) NOT NULL,
  `user_password` VARCHAR(16) NOT NULL,
  `email_id` VARCHAR(20) NULL DEFAULT NULL,
  `email_domain` VARCHAR(45) NULL DEFAULT NULL,
  `have_car` tinyint(1) DEFAULT False, 
  `join_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

insert into `ssafyback`.`members` (user_id, user_name, user_password, email_id, email_domain, join_date)
values     ('ssafy', '김싸피', '1234', 'ssafy', 'ssafy.com', now()), 
        ('admin', '관리자', '1234', 'admin', 'google.com', now());
    
commit;


-- -----------------------------------------------------
-- Table `ssafyback`.`board`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ssafyback`.`board` ;

CREATE TABLE IF NOT EXISTS `ssafyback`.`board` (
  `article_no` INT NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(16) NULL DEFAULT NULL,
  `subject` VARCHAR(100) NULL DEFAULT NULL,
  `content` VARCHAR(2000) NULL DEFAULT NULL,
  `hit` INT NULL DEFAULT 0,
  `recommend` INT NULL DEFAULT 0,
  `register_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`article_no`),
  INDEX `board_to_members_user_id_fk` (`user_id` ASC) VISIBLE,
  CONSTRAINT `board_to_members_user_id_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `ssafyback`.`members` (`user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

alter table members add salt varchar(100) not null default '0';

alter table members modify user_password varchar(200);

-- -----------------------------------------------------
-- Table `ssafyback`.`comment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ssafyback`.`comment` ;
CREATE TABLE IF NOT EXISTS `ssafyback`.`comment` (
  `comment_no` INT NOT NULL AUTO_INCREMENT,
  `article_no` INT NOT NULL,
  `user_id` VARCHAR(16) NULL DEFAULT NULL,
  `content` VARCHAR(2000) NULL DEFAULT NULL,
  `register_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`comment_no`),
  INDEX `board_to_members_user_id_fk` (`user_id` ASC) VISIBLE,
  CONSTRAINT `comment_to_members_article_no_fk`
    FOREIGN KEY (`article_no`)
    REFERENCES `ssafyback`.`board` (`article_no`),
  CONSTRAINT `comment_to_members_user_id_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `ssafyback`.`members` (`user_id`)
)
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;