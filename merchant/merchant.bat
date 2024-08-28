@echo off

if not exist target (
    @echo Building Merchant JAR...
    CALL mvn clean package > nul
    @echo Merchant JAR is ready!
)

CALL java -jar target/merchant-1.0.0-SNAPSHOT.jar %*