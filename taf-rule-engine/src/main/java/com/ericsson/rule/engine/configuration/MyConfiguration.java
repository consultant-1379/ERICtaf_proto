package com.ericsson.rule.engine.configuration;

import com.ericsson.duraci.configuration.EiffelConfiguration;
import com.ericsson.duraci.datawrappers.Arm;
import com.ericsson.duraci.datawrappers.MessageBus;

public class MyConfiguration implements EiffelConfiguration {
  //private Arm arm;
  private MessageBus messageBus;
  private String domainId;

  public MyConfiguration() {
    // read out some needed data here to instantiate Arm, Message bus and domainId
    //arm = new Arm("httpString", "ftpString", "nfsString", "downloadRepoName", "uploadRepoName", "userName", "password");
    messageBus = new MessageBus("atclvm793.athtem.eei.ericsson.se", "eiffel.poc.testing");
    //domainId = "diagnostic.monitoring";
    domainId = "test.execution";
  }

  public Arm getArm() {
    return null;
  }

  public MessageBus getMessageBus() {
    return messageBus;
  }

  public String getDomainId() {
    return domainId;
  }
}