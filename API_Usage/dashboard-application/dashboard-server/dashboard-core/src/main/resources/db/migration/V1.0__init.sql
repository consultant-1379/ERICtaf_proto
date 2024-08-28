-- MySQL Script generated by MySQL Workbench
-- 06/17/16 09:23:31
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema api_usage
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema api_usage
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `api_usage` DEFAULT CHARACTER SET utf8 ;
USE `api_usage` ;

-- -----------------------------------------------------
-- Table `api_usage`.`API`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `api_usage`.`API` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `api_usage`.`AnnotatedClass`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `api_usage`.`AnnotatedClass` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(90) NULL,
  `api` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_AnnotatedClass_API_idx` (`api` ASC),
  CONSTRAINT `fk_AnnotatedClass_API`
    FOREIGN KEY (`api`)
    REFERENCES `api_usage`.`API` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `api_usage`.`AnnotatedMethod`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `api_usage`.`AnnotatedMethod` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(90) NULL,
  `api` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_AnnotatedMethod_API1_idx` (`api` ASC),
  CONSTRAINT `fk_AnnotatedMethod_API1`
    FOREIGN KEY (`api`)
    REFERENCES `api_usage`.`API` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `api_usage`.`TeamJar`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `api_usage`.`TeamJar` (
  `idTeamJar` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(90) NULL,
  PRIMARY KEY (`idTeamJar`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `api_usage`.`TeamClass`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `api_usage`.`TeamClass` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(90) NULL,
  `TeamJar_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_TeamClass_TeamJar1_idx` (`TeamJar_id` ASC),
  CONSTRAINT `fk_TeamClass_TeamJar1`
    FOREIGN KEY (`TeamJar_id`)
    REFERENCES `api_usage`.`TeamJar` (`idTeamJar`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `api_usage`.`TeamMethod`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `api_usage`.`TeamMethod` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(90) NULL,
  `TeamClass_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_TeamMethod_TeamClass1_idx` (`TeamClass_id` ASC),
  CONSTRAINT `fk_TeamMethod_TeamClass1`
    FOREIGN KEY (`TeamClass_id`)
    REFERENCES `api_usage`.`TeamClass` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `api_usage`.`AnnotatedClass_TeamMethod`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `api_usage`.`AnnotatedClass_TeamMethod` (
  `AnnotatedClass_id` INT NOT NULL,
  `TeamMethod_id` INT NOT NULL,
  PRIMARY KEY (`AnnotatedClass_id`, `TeamMethod_id`),
  INDEX `fk_AnnotatedClass_has_TeamMethod_TeamMethod1_idx` (`TeamMethod_id` ASC),
  INDEX `fk_AnnotatedClass_has_TeamMethod_AnnotatedClass1_idx` (`AnnotatedClass_id` ASC),
  CONSTRAINT `fk_AnnotatedClass_has_TeamMethod_AnnotatedClass1`
    FOREIGN KEY (`AnnotatedClass_id`)
    REFERENCES `api_usage`.`AnnotatedClass` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_AnnotatedClass_has_TeamMethod_TeamMethod1`
    FOREIGN KEY (`TeamMethod_id`)
    REFERENCES `api_usage`.`TeamMethod` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `api_usage`.`AnnotatedClass_has_TeamMethod1`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `api_usage`.`AnnotatedClass_has_TeamMethod1` (
  `AnnotatedClass_id` INT NOT NULL,
  `TeamMethod_id` INT NOT NULL,
  PRIMARY KEY (`AnnotatedClass_id`, `TeamMethod_id`),
  INDEX `fk_AnnotatedClass_has_TeamMethod1_TeamMethod1_idx` (`TeamMethod_id` ASC),
  INDEX `fk_AnnotatedClass_has_TeamMethod1_AnnotatedClass1_idx` (`AnnotatedClass_id` ASC),
  CONSTRAINT `fk_AnnotatedClass_has_TeamMethod1_AnnotatedClass1`
    FOREIGN KEY (`AnnotatedClass_id`)
    REFERENCES `api_usage`.`AnnotatedClass` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_AnnotatedClass_has_TeamMethod1_TeamMethod1`
    FOREIGN KEY (`TeamMethod_id`)
    REFERENCES `api_usage`.`TeamMethod` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `api_usage`.`TeamMethod_AnnotatedMethod`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `api_usage`.`TeamMethod_AnnotatedMethod` (
  `TeamMethod_id` INT NOT NULL AUTO_INCREMENT,
  `AnnotatedMethod_id` INT NOT NULL,
  PRIMARY KEY (`TeamMethod_id`, `AnnotatedMethod_id`),
  INDEX `fk_TeamMethod_has_AnnotatedMethod_AnnotatedMethod1_idx` (`AnnotatedMethod_id` ASC),
  INDEX `fk_TeamMethod_has_AnnotatedMethod_TeamMethod1_idx` (`TeamMethod_id` ASC),
  CONSTRAINT `fk_TeamMethod_has_AnnotatedMethod_TeamMethod1`
    FOREIGN KEY (`TeamMethod_id`)
    REFERENCES `api_usage`.`TeamMethod` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TeamMethod_has_AnnotatedMethod_AnnotatedMethod1`
    FOREIGN KEY (`AnnotatedMethod_id`)
    REFERENCES `api_usage`.`AnnotatedMethod` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `api_usage`.`TeamMethod_AnnotatedClass`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `api_usage`.`TeamMethod_AnnotatedClass` (
  `TeamMethod_id` INT NOT NULL AUTO_INCREMENT,
  `AnnotatedClass_id` INT NOT NULL,
  PRIMARY KEY (`TeamMethod_id`, `AnnotatedClass_id`),
  INDEX `fk_TeamMethod_has_AnnotatedClass_AnnotatedClass1_idx` (`AnnotatedClass_id` ASC),
  INDEX `fk_TeamMethod_has_AnnotatedClass_TeamMethod1_idx` (`TeamMethod_id` ASC),
  CONSTRAINT `fk_TeamMethod_has_AnnotatedClass_TeamMethod1`
    FOREIGN KEY (`TeamMethod_id`)
    REFERENCES `api_usage`.`TeamMethod` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TeamMethod_has_AnnotatedClass_AnnotatedClass1`
    FOREIGN KEY (`AnnotatedClass_id`)
    REFERENCES `api_usage`.`AnnotatedClass` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO `api` (`value`) VALUES ("Stable");
INSERT INTO `api_usage`.`API` (`value`) VALUES ("Deprecated");
INSERT INTO `api_usage`.`API` (`value`) VALUES ("Internal");
INSERT INTO `api_usage`.`API` (`value`) VALUES ("Experimental");



INSERT INTO `api_usage`.`AnnotatedClass` (`name`, `api`) VALUES ("com.ericsson.cifwk.taf.depricated.DepricatedClass", "2");


INSERT INTO `api_usage`.`AnnotatedMethod` (`name`, `api`) VALUES ("com.ericsson.cifwk.taf.depricated.DepricatedMethod", "2");


INSERT INTO `api_usage`.`TeamJar` (`name`) VALUES ("/jars/ERICTAFdataservice_CXP9035555-1.1.259.jar");


INSERT INTO `api_usage`.`TeamClass` (`name`, `TeamJar_id`) VALUES ("com.ericsson.oss.services.taf.operators.Operator", 1);


INSERT INTO `api_usage`.`TeamMethod` (`name`, `TeamClass_id`) VALUES ("void createData(com.ericsson.enm.data.Data)", 1);


INSERT INTO `api_usage`.`TeamMethod_AnnotatedClass` (`TeamMethod_id`, `AnnotatedClass_id`) VALUES (1, 1);


INSERT INTO `api_usage`.`TeamMethod_AnnotatedMethod` (`TeamMethod_id`, `AnnotatedMethod_id`) VALUES (1, 1);
