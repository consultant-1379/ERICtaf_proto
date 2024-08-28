package com.ericsson.cifwk.presentation.dto.doc.Impl;

/**
 * Created by egergle on 18/02/2015.
 */


import com.ericsson.cifwk.presentation.dto.doc.DocumentParser;
import com.google.common.collect.Lists;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by egergle on 18/02/2015.
 */

public class TextParser {

    public static final String REQUIREMENTS = "requirements";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String PRECONDITION = "precondition";
    public static final String PRIORITY = "priority";
    public static final String STEPS = "steps";

    public List<Map> parse(InputStream inputStream) {

        List<Object> n = Lists.newArrayList();

        String data = convertStreamToString(inputStream);

        data = data.split("Test Case Execution")[2];

        List<String> testCaseBlock = Arrays.asList(data.split("\\d+\\.\\d+\\.\\d"));

        n.add(testCaseBlock);

        return cycleThroughBlock(testCaseBlock);

    }

    private static String convertStreamToString(InputStream inputStream) {
        try (Scanner s = new Scanner(inputStream, "ISO-8859-1").useDelimiter("\\A");) {
            return s.hasNext() ? s.next() : "";
        } catch (Exception e) {
            return "";
        }
    }

    private List<Map> cycleThroughBlock (List<String> testCaseBlock) {
        List<Map> testCases = Lists.newArrayList();

        Map<String, Object> map = new HashMap();

        for (String item: testCaseBlock) {
            map = getTestCaseData(item);
            testCases.add(map);
        }
        return testCases;
    }

    private Map<String, Object> getTestCaseData(String item) {
        Map<String, Object> data = new HashMap();
        List <String> steps = Lists.newArrayList();

        Pattern titlePattern = Pattern.compile("(?m)^.*$");
        Pattern descPattern = Pattern.compile("(?smi)^Purpose.*?(^Priority|^Requirement)"); //(?mi)^Purpose.*$
        Pattern preConditionPattern = Pattern.compile("(?mi)^Precondition.*$");
        Pattern priorityPattern = Pattern.compile("(?mi)^Priority.*$");
        Pattern stepsPattern = Pattern.compile("(?si)Action\\s\\d:.*?Result\\s\\d:");

        String value;
        Matcher matcher = titlePattern.matcher(item);
        if (matcher.find()) {
            data.put(TITLE, matcher.group().trim());
        }

        matcher = descPattern.matcher(item);
        if (matcher.find()) {
            value = matcher.group().trim();
            if (value.endsWith("\nRequirement")) {
                value = value.substring(0, value.length() - 12);
            } else if (value.endsWith("\nPriority")) {
                value = value.substring(0, value.length() - 9);
            }
            data.put(DESCRIPTION, value);
        }

        matcher = preConditionPattern.matcher(item);
        if (matcher.find()) {
            data.put(PRECONDITION, matcher.group().trim());
        }

        matcher = priorityPattern.matcher(item);
        if (matcher.find()) {
            data.put(PRIORITY, matcher.group().trim());
        }

        matcher = stepsPattern.matcher(item);
        while(matcher.find()) {
            steps.add(matcher.group().trim().split("Result\\s\\d")[0]);
        }
        data.put(STEPS, steps);

        return data;
    }

}

