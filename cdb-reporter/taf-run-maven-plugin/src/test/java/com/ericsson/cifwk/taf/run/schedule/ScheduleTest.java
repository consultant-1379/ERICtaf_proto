package com.ericsson.cifwk.taf.run.schedule;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;

import org.junit.Test;

public class ScheduleTest {

	private Schedule sch = new Schedule();
	private String schedule = "<schedule>	"
			+ "<item>	"
			+ "<component>com.ericsson.ci.cloud.cdb_ossrc_install.cite-tests</component>	"
			+ "<suite>install.xml</suite>	"
			+ "<stop_on_fail>true</stop_on_fail>" + "</item>" + "<item>	"
			+ "<component>com.ericsson.cifwk.taf.cdb-manual</component>"
			+ "	<suite>manual_tests.xml</suite>	"
			+ "<stop_on_fail>false</stop_on_fail>" + "</item>" + "<item>"
			+ "<component>com.ericsson.oss.bsim.test-pom</component>	"
			+ "<stop_on_fail>false</stop_on_fail>" + "<groups>cdb</groups>"
			+ "</item>" + "</schedule>";

	@Test
	public void shallReadTheString() {
		sch.readSchedule(new ByteArrayInputStream(schedule.getBytes()));
		assertEquals(sch.getItems().size(), 3);
		System.out.println(sch.getItems());
	}

}
