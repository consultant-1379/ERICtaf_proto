package com.ericsson.cifwk.presentation.dto.excel.Reader;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by egergle on 19/02/2015.
 *
 * This supports xls files from 2003 to older
 */
public class SimpleReaderForXLS implements OfficeReader {

    public static final String REQUIREMENTS = "requirements";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String PRIORITY = "priority";
    public static final String COMPONENT = "component";
    public static final String ID = "id";

    private String separator = ",";

    @Override
    public List<Map> parse(InputStream inputStream) throws OfficeXmlFileException, IOException {

        List<Map> result =  Lists.newArrayList();
        List<String> requirements;

        try (InputStream fileInputStream = inputStream) {

            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); //skip first row
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Map map = new HashMap();

                String rawRequirmentCell = row.getCell(4, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
                requirements = Lists.newArrayList(Splitter.on(separator).split(rawRequirmentCell));

                map.put(REQUIREMENTS, requirements);
                map.put(ID, row.getCell(0, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                map.put(TITLE, row.getCell(1, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                map.put(DESCRIPTION, row.getCell(1, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                map.put(COMPONENT, row.getCell(2, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                map.put(PRIORITY, row.getCell(3, Row.CREATE_NULL_AS_BLANK).getStringCellValue());
                result.add(map);
            }

        }

        return result;

    }

}
