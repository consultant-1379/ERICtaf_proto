package com.ericsson.rule.engine.impl;

import com.ericsson.duraci.eiffelmessage.binding.MessageBusBindings;
import com.ericsson.duraci.eiffelmessage.binding.exceptions.BindingDisposalException;
import com.ericsson.duraci.eiffelmessage.binding.exceptions.EiffelMessageBindingException;
import com.ericsson.duraci.eiffelmessage.sending.exceptions.EiffelMessageSenderException;
import com.ericsson.duraci.logging.EiffelLog;
import com.ericsson.duraci.logging.JavaLoggerEiffelLog;
import com.ericsson.duraci.rules.RuleEngine;
import com.ericsson.duraci.rules.RuleHeader;
import com.ericsson.duraci.rules.exceptions.RuleValidationException;
import com.ericsson.rule.engine.configuration.MyConfiguration;
import com.ericsson.rule.engine.message.matcher.JiraCheck;
import com.ericsson.rule.engine.message.matcher.JiraFeed;
import com.ericsson.rule.engine.message.sender.Sender;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class Implementation extends Thread {

    RuleEngine ruleEngine;
    EiffelLog log;
    MessageBusBindings binder;
    RuleHeader ruleHeader;
    Sender sender;
    Thread backgroundThread;

    public static void main(String args[]) throws Exception {
        Implementation impl = new Implementation();

        impl.startRuleEngine();
        Thread.sleep(5000);
        impl.checkJiraFromTimeToTime();
        Runtime.getRuntime().addShutdownHook(impl);
    }

    public Implementation() throws IOException {
        MyConfiguration conf = new MyConfiguration();

        sender = new Sender(conf);
        log = new JavaLoggerEiffelLog(Implementation.class);
        binder = new MessageBusBindings.Factory().create(conf.getMessageBus(), conf.getDomainId());
        ruleHeader = new RuleHeader("com.ericsson.rule.engine");
        ruleEngine = new RuleEngine.Factory().createBasic(log, binder, conf.getDomainId() + ".#", ruleHeader, getRules(), true);
    }

    public void startRuleEngine() throws EiffelMessageBindingException, IOException, RuleValidationException {
        System.out.println("starting engine");
        ruleEngine.start();
    }

    public void stopRuleEngine() {
        System.out.println("stopping engine");
        ruleEngine.stop();
        try {
            binder.dispose();
        } catch (BindingDisposalException e) {
            e.printStackTrace();
        }
        sender.dispose();
    }

    public String getRules() throws IOException {
        String rules;
        try {
            rules = readFile(getRuleFilePath());
        } catch (IOException e) {
            throw new IOException("Could not read rule file: " + getRuleFilePath(), e);
        }
        return (rules);
    }

    public String getRuleFilePath() {
        return "src/main/resources/TafRules.drl";
    }

    private String readFile(String filename) throws IOException {
        File f = new File(filename);
        return (Files.toString(f, Charset.defaultCharset()));
    }

    private void checkJiraFromTimeToTime() throws EiffelMessageSenderException {
        System.out.println("Sending Message");
        JiraCheck jiraCheck = new JiraCheck(new JiraFeed());
        backgroundThread = new Thread(jiraCheck);
        backgroundThread.start();
    }

    @Override
    public void run() {
        backgroundThread.interrupt();
        stopRuleEngine();
    }

}
