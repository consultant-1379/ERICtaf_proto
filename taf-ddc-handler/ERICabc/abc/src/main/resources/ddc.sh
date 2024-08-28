#!/bin/bash
export JAVA_HOME=/usr/java/jdk1.7.0_17
$JAVA_HOME/bin/java -cp taf-ddc-handler.jar:$JAVA_HOME/lib/tools.jar -Dcom.ericsson.cifwk.diagmon.util.common.torvernum=7 com.ericsson.cifwk.diagmon.util.instr.Instr -defaultPollInterval 5 -metrics abc.xml $@
