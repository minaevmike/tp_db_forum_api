SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `forum` ;
CREATE SCHEMA IF NOT EXISTS `forum` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `forum` ;

-- -----------------------------------------------------
-- Table `forum`.`Users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `forum`.`Users` ;

CREATE TABLE IF NOT EXISTS `forum`.`Users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(32) NULL,
  `mail` VARCHAR(32) NOT NULL,
  `name` VARCHAR(16) NULL,
  `isAnonymous` TINYINT(1) NOT NULL DEFAULT FALSE,
  `about` VARCHAR(128) NULL,
  PRIMARY KEY (`id`, `mail`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `mail_UNIQUE` (`mail` ASC),
  INDEX `reverse` (`mail` ASC, `id` ASC),
  UNIQUE INDEX `id_desc` (`id` DESC, `mail` ASC),
  INDEX `reverse_desc` (`mail` ASC, `id` DESC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `forum`.`Forums`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `forum`.`Forums` ;

CREATE TABLE IF NOT EXISTS `forum`.`Forums` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_mail` VARCHAR(32) NOT NULL,
  `short_name` VARCHAR(128) NOT NULL,
  `name` VARCHAR(128) NULL,
  PRIMARY KEY (`id`, `user_mail`, `short_name`),
  INDEX `fk_Forums_Users1_idx` (`user_mail` ASC),
  UNIQUE INDEX `short_name_UNIQUE` (`short_name` ASC, `id` ASC),
  CONSTRAINT `fk_Forums_Users1`
    FOREIGN KEY (`user_mail`)
    REFERENCES `forum`.`Users` (`mail`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `forum`.`Follows`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `forum`.`Follows` ;

CREATE TABLE IF NOT EXISTS `forum`.`Follows` (
  `user` INT NOT NULL,
  `follow` INT NULL,
  `isDeleted` TINYINT(1) NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`user`, `follow`),
  UNIQUE INDEX `userToFollowing` (`follow` ASC, `user` ASC),
  CONSTRAINT `fk_Follows_Users_m`
    FOREIGN KEY (`user`)
    REFERENCES `forum`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Follows_Users_m1`
    FOREIGN KEY (`follow`)
    REFERENCES `forum`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `forum`.`Threads`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `forum`.`Threads` ;

CREATE TABLE IF NOT EXISTS `forum`.`Threads` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `forum_id` INT NOT NULL,
  `date` DATETIME NULL,
  `likes` INT NOT NULL DEFAULT 0,
  `dislikes` INT NOT NULL,
  `message` TEXT NULL,
  `points` INT NULL,
  `slug` VARCHAR(128) NOT NULL,
  `title` VARCHAR(128) NOT NULL,
  `isDeleted` TINYINT(1) NOT NULL,
  `isClosed` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`, `user_id`, `forum_id`),
  UNIQUE INDEX `idThreads_UNIQUE` (`id` ASC),
  INDEX `fk_Threads_Forums1_idx` (`forum_id` ASC),
  INDEX `fk_Threads_Users1_idx` (`user_id` ASC),
  INDEX `date_order` (`date` ASC, `isDeleted` ASC, `id` ASC),
  INDEX `date_order_rev` (`date` DESC, `isDeleted` ASC, `id` ASC),
  CONSTRAINT `fk_Threads_Forums1`
    FOREIGN KEY (`forum_id`)
    REFERENCES `forum`.`Forums` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Threads_Users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `forum`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `forum`.`Subscriptions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `forum`.`Subscriptions` ;

CREATE TABLE IF NOT EXISTS `forum`.`Subscriptions` (
  `user_id` INT NOT NULL,
  `thread_id` INT NOT NULL,
  `isDeleted` TINYINT(1) NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`user_id`, `thread_id`),
  INDEX `fk_Subscriprions_Users1_idx` (`user_id` ASC),
  INDEX `fk_Subscriprions_Threads1_idx` (`thread_id` ASC),
  UNIQUE INDEX `subscription` (`user_id` ASC, `thread_id` ASC),
  CONSTRAINT `fk_Subscriprions_Users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `forum`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Subscriprions_Threads1`
    FOREIGN KEY (`thread_id`)
    REFERENCES `forum`.`Threads` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `forum`.`Posts`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `forum`.`Posts` ;

CREATE TABLE IF NOT EXISTS `forum`.`Posts` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `thread_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `forum_id` INT NOT NULL,
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
  `isDeleted` TINYINT(1) NULL,
  PRIMARY KEY (`id`, `thread_id`, `user_id`, `forum_id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_Posts_Threads1_idx` (`thread_id` ASC),
  INDEX `fk_Posts_Users1_idx` (`user_id` ASC),
  INDEX `fk_Posts_Posts1_idx` (`parent_post` ASC),
  INDEX `fk_Posts_Forums1_idx` (`forum_id` ASC),
  UNIQUE INDEX `date_ordering` (`date` DESC, `isDeleted` ASC, `id` ASC),
  INDEX `date_order_rev` (`date` DESC, `isDeleted` ASC, `id` ASC),
  CONSTRAINT `fk_Posts_Threads1`
    FOREIGN KEY (`thread_id`)
    REFERENCES `forum`.`Threads` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Posts_Users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `forum`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Posts_Posts1`
    FOREIGN KEY (`parent_post`)
    REFERENCES `forum`.`Posts` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Posts_Forums1`
    FOREIGN KEY (`forum_id`)
    REFERENCES `forum`.`Forums` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
