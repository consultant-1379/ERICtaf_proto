@echo off

@echo Importing SSL certificate...
@keytool -import  -v -file bazaar.internal.ericsson.com.crt -keystore bazaar.internal.ericsson.com.jks -storepass bazaar
@echo Done!
