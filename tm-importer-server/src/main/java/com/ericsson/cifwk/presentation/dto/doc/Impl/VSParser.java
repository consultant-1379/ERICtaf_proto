package com.ericsson.cifwk.presentation.dto.doc.Impl;

/**
 * Created by egergle on 18/02/2015.
 */


import com.ericsson.cifwk.presentation.dto.doc.DocumentParser;
import com.google.common.collect.Lists;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Text;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by egergle on 18/02/2015.
 */

public class VSParser implements DocumentParser {

    final String XPATH_TO_SELECT_TEXT_NODES = "//w:p";
    final String XPATH_TO_SELECT_TAB_NODES = "//w:t";

    public static final String REQUIREMENTS = "requirements";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String STEPS = "steps";

    @Override
    public List<Object> parse(InputStream inputStream) {

        List<Object> jaxbNodes = new ArrayList();
        List<Object> result = new ArrayList();

        List<String> steps;
        List<String> requirements;
        Map map = new HashMap();

        try {
            InputStream fileInputStream = inputStream;

            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(fileInputStream);

            MainDocumentPart mdp = wordMLPackage.getMainDocumentPart();

            /*StyleDefinitionsPart stylesPart = mdp.getStyleDefinitionsPart();
            stylesPart.getDefaultParagraphStyle();*/

            //replacePlaceholder(wordMLPackage, "result", "gerald");

            List<Object> contents = mdp.getContent();
            for (Object o : contents) {
                System.out.println(o.toString());
                result.add(o.toString());
            }
            //result = result.subList(result.indexOf("Test Case Execution"), result.size());


            jaxbNodes = wordMLPackage.getMainDocumentPart().getJAXBNodesViaXPath(XPATH_TO_SELECT_TEXT_NODES, true);

            /*for (Object jaxbNode : getAllElementFromObject(mdp, Text.class)) {
                String paragraphString = ""; //stringWriter.toString();
                Text textElement = (Text)jaxbNode;
                paragraphString += textElement.getValue();

                result.add(paragraphString);
                System.out.println(paragraphString);
            }*/
            result = result.subList(result.indexOf("Test Case Execution"), result.size());
            System.out.println(result.get(1));
            //List<Map> finishResult  = getCriteria(result);



        } catch (Docx4JException docxException) {
            docxException.printStackTrace();

        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    private void replacePlaceholder(WordprocessingMLPackage template, String name, String placeholder) {
        List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);

        for (Object text : texts) {
            Text textElement = (Text) text;
            if (textElement.getValue().equals(placeholder)) {
                textElement.setValue(name);
            }
        }
    }

    private static List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
        List<Object> result = new ArrayList<Object>();
        if (obj instanceof JAXBElement) obj = ((JAXBElement<?>) obj).getValue();

        if (obj.getClass().equals(toSearch))
            result.add(obj);
        else if (obj instanceof ContentAccessor) {
            List<?> children = ((ContentAccessor) obj).getContent();
            for (Object child : children) {
                result.addAll(getAllElementFromObject(child, toSearch));
            }

        }
        return result;
    }

    private List<Map> getCriteria(List<Object> result) {
        List<Map> finishResult =  Lists.newArrayList();
        Map criteria = new HashMap();
        for (Object o: result) {
            String line = o.toString();

            if (line.matches("^[A-Za-z]*\\s[0-9]")) {


            } else if (line == "Priority:") {

            }


        }

        return finishResult;
    }


}

