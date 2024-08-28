package com.ericsson.cifwk.presentation.dto.excel.Reader;

import org.apache.poi.poifs.filesystem.OfficeXmlFileException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by egergle on 27/02/2015.
 */
public interface OfficeReader {
    List<Map> parse(InputStream inputStream) throws OfficeXmlFileException, IOException;
}
