package com.ericsson.cifwk.sikuli;

import org.sikuli.basics.Debug;
import org.sikuli.script.Location;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

/**
 *
 */
public class Main {

    public static void main(String[] args) {


        Debug.setDebugLevel(3);

        Screen s = new Screen();

        Region region = s.newRegion(new Location(10, 10), 100, 100);
        region.highlight();

        String text = region.text();
        System.out.println(text);
        System.exit(0);
    }

}
