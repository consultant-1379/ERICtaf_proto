#!/bin/bash
ln -s /usr/lib/jvm/jdk1.7.0_17/lib/tools.jar target/lib/tools.jar
java -cp target/ddc-extension-1.0.0-SNAPSHOT.jar:target/lib/* -Dcom.ericsson.cifwk.diagmon.util.common.torvernum=7 com.ericsson.cifwk.diagmon.util.instr.Instr -metrics sut.xml -defaultPollInterval 5 $@
