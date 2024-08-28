INSERT INTO `api` (`value`) VALUES ("Stable");
INSERT INTO `api_usage`.`API` (`value`) VALUES ("Deprecated");
INSERT INTO `api_usage`.`API` (`value`) VALUES ("Internal");
INSERT INTO `api_usage`.`API` (`value`) VALUES ("Experimental");


INSERT INTO `api_usage`.`AnnotatedClass` (`name`, `api`) VALUES ("com.ericsson.cifwk.taf.depricated.DepricatedClass", "2");


INSERT INTO `api_usage`.`AnnotatedMethod` (`name`, `api`) VALUES ("com.ericsson.cifwk.taf.depricated.DepricatedMethod", "2");


INSERT INTO `api_usage`.`TeamMethod` (`name`, `TeamClass_id`) VALUES ("void createData(com.ericsson.enm.data.Data)", 1);


INSERT INTO `api_usage`.`TeamClass` (`name`, `TeamJar_id`) VALUES ("com.ericsson.oss.services.taf.operators.Operator", 1);


INSERT INTO `api_usage`.`TeamJar` (`name`) VALUES ("/jars/ERICTAFdataservice_CXP9035555-1.1.259.jar");


INSERT INTO `api_usage`.`TeamMethod_AnnotatedClass` (`TeamMethod_id`, `AnnotatedClass_id`) VALUES (1, 1);


INSERT INTO `api_usage`.`TeamMethod_AnnotatedMethod` (`TeamMethod_id`, `AnnotatedMethod_id`) VALUES (1, 1);
