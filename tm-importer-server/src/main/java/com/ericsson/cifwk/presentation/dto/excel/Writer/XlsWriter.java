package com.ericsson.cifwk.presentation.dto.excel.Writer;

import com.ericsson.cifwk.infrastructure.PropertyLoader;
import com.ericsson.cifwk.presentation.dto.ReferenceData;
import com.ericsson.cifwk.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.presentation.dto.TestStepInfo;
import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by egergle on 23/03/2015.
 */
public class XlsWriter {

    private final String delimiter = ",";
    private final String excelPre97Extention = ".xls";
    private static final String REJECTED_PATH = "rejectedPath";

    public void writeToFile (List<TestCaseInfo> testCaseInfos, String filename) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Rejected TC's");

        int rownum = 0;
        int headerNum = 0;
        Row header = sheet.createRow(rownum++);
        createCell(header, headerNum++, "Groups");
        createCell(header, headerNum++, "Title");
        createCell(header, headerNum++, "Description");
        createCell(header, headerNum++, "ExecutionType");
        createCell(header, headerNum++, "Context");
        createCell(header, headerNum++, "TestSteps");

        int cellNum = 0;
        for (TestCaseInfo testCaseInfo : testCaseInfos) {
            Row row = sheet.createRow(rownum++);

            createCell(row, cellNum++, testCaseInfo.getGroups());
            createCell(row, cellNum++, testCaseInfo.getTitle());
            createCell(row, cellNum++, testCaseInfo.getDescription());
            createCell(row, cellNum++, testCaseInfo.getExecutionType());
            createCell(row, cellNum++, testCaseInfo.getContexts());

            Cell cell = row.createCell(cellNum++);
            for (TestStepInfo testStepInfo: testCaseInfo.getTestSteps()) {
                cell.setCellValue(testStepInfo.getName());
                Row row2 = sheet.createRow(rownum++);
                cell = row.createCell(cellNum);
            }
        }

        try {
            PropertyLoader.getProperty(REJECTED_PATH).toString();
            if (testCaseInfos.size() > 0) {
                FileOutputStream out =
                        new FileOutputStream(new File(PropertyLoader.getProperty(REJECTED_PATH).toString()
                                + filename + excelPre97Extention));
                workbook.write(out);
                out.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void createCell (Row row, int cellNum, Object data) {
        Cell cell = row.createCell(cellNum);
        if(data instanceof List) {
            cell.setCellValue(joinTestStepInfo((List<TestStepInfo>) data));
        } else if(data instanceof String) {
            cell.setCellValue((String) data);
        } else if(data instanceof ReferenceData) {
            ReferenceData refData = (ReferenceData) data;
            cell.setCellValue(joinReferenceDataItem(refData.getItems()));
        } else if(data instanceof ReferenceDataItem) {
            ReferenceDataItem refDataItem = (ReferenceDataItem) data;
            cell.setCellValue(refDataItem.getTitle());
        } else if(data instanceof Set) {
            Set<ReferenceDataItem> refDataItem = (Set<ReferenceDataItem>) data;
            List<ReferenceDataItem> list = Lists.newArrayList(refDataItem);
            cell.setCellValue(joinReferenceDataItem(list));
        }

    }

    public String joinReferenceDataItem (List<ReferenceDataItem> list) {
        String expandList = "";
        String delim = "";
        for (ReferenceDataItem value: list) {
            expandList += delim + value.getTitle();
            delim = delimiter;
        }

        return expandList;
    }

    public String joinTestStepInfo (List<TestStepInfo> list) {
        String expandList = "";
        String delim = "";
        for (TestStepInfo value: list) {
            expandList += delim + value.getName();
            delim = delimiter;
        }

        return expandList;
    }
}
