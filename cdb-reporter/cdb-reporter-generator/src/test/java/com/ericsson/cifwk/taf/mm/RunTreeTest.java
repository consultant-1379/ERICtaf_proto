package com.ericsson.cifwk.taf.mm;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.FileUtils;
import org.junit.Test;

public class RunTreeTest {

	@Test
	public void shallCreateCombinedRed() {

		assertFalse(RunTree.combine("test", "target/Jcat_LOGS", new File(
				"src/test/resources/r1/target/Jcat_LOGS/r1.mm"), new File(
				"src/test/resources/r2/target/Jcat_LOGS/r2.mm")));
		assertTrue(new File(RunTree.REPORT_DIR + "/" + RunTree.FILE_NAME)
				.exists());
		try {
			FileUtils.forceDelete(new File(RunTree.REPORT_DIR));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
