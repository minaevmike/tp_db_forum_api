SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `forum_api` ;
CREATE SCHEMA IF NOT EXISTS `forum_api` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `forum_api` ;

-- -----------------------------------------------------
-- Table `forum_api`.`Users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `forum_api`.`Users` ;

CREATE TABLE IF NOT EXISTS `forum_api`.`Users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(32) NULL,
  `mail` VARCHAR(32) NOT NULL,
  `name` VARCHAR(16) NOT NULL,
  `isAnonymous` TINYINT(1) NOT NULL DEFAULT FALSE,
  `about` VARCHAR(128) NULL,
  PRIMARY KEY (`id`, `mail`, `username`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `mail_UNIQUE` (`mail` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `forum_api`.`Forums`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `forum_api`.`Forums` ;

CREATE TABLE IF NOT EXISTS `forum_api`.`Forums` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `short_name` VARCHAR(128) NULL,
  PRIMARY KEY (`id`, `user_id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_Forums_Users1_idx` (`user_id` ASC),
  CONSTRAINT `fk_Forums_Users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `forum_api`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `forum_api`.`Follows`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `forum_api`.`Follows` ;

CREATE TABLE IF NOT EXISTS `forum_api`.`Follows` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user` INT NOT NULL,
  `follow` INT NULL,
  PRIMARY KEY (`id`, `user`, `follow`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_Follows_Users_idx` (`user` ASC),
  INDEX `fk_Follows_Users1_idx` (`follow` ASC),
  CONSTRAINT `fk_Follows_Users`
    FOREIGN KEY (`user`)
    REFERENCES `forum_api`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Follows_Users1`
    FOREIGN KEY (`follow`)
    REFERENCES `forum_api`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `forum_api`.`Threads`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `forum_api`.`Threads` ;

CREATE TABLE IF NOT EXISTS `forum_api`.`Threads` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `forum_id` INT NOT NULL,
  `date` DATETIME NULL,
  `likes` INT NULL,
  `dislikes` INT NULL,
  `message` TEXT NULL,
  `points` INT NULL,
  `slug` VARCHAR(128) NOT NULL,
  `title` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`, `user_id`, `forum_id`),
  UNIQUE INDEX `idThreads_UNIQUE` (`id` ASC),
  INDEX `fk_Threads_Forums1_idx` (`forum_id` ASC),
  INDEX `fk_Threads_Users1_idx` (`user_id` ASC),
  CONSTRAINT `fk_Threads_Forums1`
    FOREIGN KEY (`forum_id`)
    REFERENCES `forum_api`.`Forums` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Threads_Users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `forum_api`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `forum_api`.`Subscriptions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `forum_api`.`Subscriptions` ;

CREATE TABLE IF NOT EXISTS `forum_api`.`Subscriptions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `thread_id` INT NULL,
  PRIMARY KEY (`id`, `user_id`, `thread_id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_Subscriprions_Users1_idx` (`user_id` ASC),
  INDEX `fk_Subscriprions_Threads1_idx` (`thread_id` ASC),
  CONSTRAINT `fk_Subscriprions_Users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `forum_api`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Subscriprions_Threads1`
    FOREIGN KEY (`thread_id`)
    REFERENCES `forum_api`.`Threads` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `forum_api`.`Posts`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `forum_api`.`Posts` ;

CREATE TABLE IF NOT EXISTS `forum_api`.`Posts` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `thread_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `parent_post` INT NULL,
  `message` TEXT NOT NULL,
  `date` DATETIME NOT NULL,
  `likes` INT NOT NULL DEFAULT 0,
  `dislikes` INT NOT NULL DEFAULT 0,
  `points` INT NOT NULL DEFAULT 0,
  `isApproved` TINYINT(1) NULL,
  `isHighlighted` TINYINT(1) NULL,
  `isEdited` TINYINT(1) NULL,
  `isSpam` TINYINT(1) NULL,
  `isDeeleted` TINYINT(1) NULL,
  PRIMARY KEY (`id`, `thread_id`, `user_id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_Posts_Threads1_idx` (`thread_id` ASC),
  INDEX `fk_Posts_Users1_idx` (`user_id` ASC),
  INDEX `fk_Posts_Posts1_idx` (`parent_post` ASC),
  CONSTRAINT `fk_Posts_Threads1`
    FOREIGN KEY (`thread_id`)
    REFERENCES `forum_api`.`Threads` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Posts_Users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `forum_api`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Posts_Posts1`
    FOREIGN KEY (`parent_post`)
    REFERENCES `forum_api`.`Posts` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
