package com.ericsson.cifwk.sikuli

import org.sikuli.script.Location
import org.sikuli.script.Screen

/**
 *
 */
class GuiTesting {

    public screen() {
        Screen s = new Screen()
        def screenImage = s.capture()
        screenImage.

        Region region = s.newRegion(new Location(10, 10), 100, 100)
        region.highlight()

        String text = region.text()
    }


}
