package com.ericsson.cifwk.taf.run.schedule;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * ScheduleItems contain information about item in schedule.
 * 
 */
@XmlRootElement(name = "item")
public class ScheduleItem {

	private String component;

	private String suite;

	private String groups;

	@XmlElement(name = "stop_on_fail")
	private boolean stopOnFail;

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getSuite() {
		return suite;
	}

	public void setSuite(String suite) {
		this.suite = suite;
	}

	public boolean isStopOnFailure() {
		return stopOnFail;
	}

	public void setStopOnFailure(boolean stopOnFailure) {
		this.stopOnFail = stopOnFailure;
	}

	public String getGroups() {
		return this.groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}

	public String toString() {
		return "Component: " + getComponent() + ", suite: " + getSuite()
				+ ", stop on failure: " + isStopOnFailure() + ", groups: "
				+ getGroups();
	}
}
