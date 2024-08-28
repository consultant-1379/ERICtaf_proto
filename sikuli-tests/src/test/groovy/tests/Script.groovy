package tests

import org.sikuli.script.Location
import org.sikuli.script.Region
import org.sikuli.script.Screen;

Screen s = new Screen()

Region region = s.newRegion(new Location(10, 10), 100, 100)
region.highlight()

String text = region.text()
System.out.println(text)