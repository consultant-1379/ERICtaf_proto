package com.ericsson.cifwk.taf.run.schedule;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.jar.JarFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

/**
 * Schedule contains a list of scheduled items
 * 
 */
@XmlRootElement(name = "schedule")
public class Schedule {
	private Log log = new SystemStreamLog();

	@XmlElement(name = "item")
	private List<ScheduleItem> scheduleItems;

	Schedule() {
	};

	public Schedule(File jar, String scheduleName) {
		log.info("Creating schedule from " + jar + " of name " + scheduleName);
		JarFile scheduleJar;
		InputStream fileContent = null;
		try {
			scheduleJar = new JarFile(jar);
			fileContent = scheduleJar.getInputStream(scheduleJar
					.getJarEntry(scheduleName));
			readSchedule(fileContent);
		} catch (IOException | NullPointerException e) {
			log.error("Cannot read schedule " + scheduleName, e);
		} finally {
			try {
				if (fileContent != null)
					fileContent.close();
			} catch (IOException e) {
			}
		}

	}

	void readSchedule(InputStream scheduleContent) {
		try {
			JAXBContext context = JAXBContext.newInstance(Schedule.class);
			Unmarshaller um = context.createUnmarshaller();
			Schedule subSchedule = (Schedule) um.unmarshal(scheduleContent);
			setItems(subSchedule.getItems());
		} catch (JAXBException e) {
			log.error("Cannot create schedule", e);
			throw new RuntimeException(e);
		}

	}

	public void setItems(List<ScheduleItem> items) {
		this.scheduleItems = items;
	}

	public List<ScheduleItem> getItems() {
		return scheduleItems;
	}

}
