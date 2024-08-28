package com.ericsson.cifwk.presentation.dto.doc;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Created by egergle on 18/02/2015.
 */

public interface DocumentParser {
    List<Object> parse(InputStream fileInputStream);
}
