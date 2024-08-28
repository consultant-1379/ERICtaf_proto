package com.ericsson.cifwk.sikuli;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.basics.Debug;
import org.sikuli.basics.Settings;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;
import org.sikuli.script.ScreenImage;
import ru.yandex.qatools.allure.annotations.Attach;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.model.AttachmentType;

import java.io.File;

/**
 *
 */
public class SikuliTest {

    @Before
    public void setUp() {
        Debug.setDebugLevel(3);

        Settings.OcrTextSearch = true;
        Settings.OcrTextRead = true;
        System.setProperty("SIKULI_IMAGE_PATH", "c:/sync/ERICtaf_proto/sikuli-tests/src/test/resources/images/");
        //Settings.BundlePath =  "c:/sync/ERICtaf_proto/sikuli-tests/src/test/resources/images/";
    }

    @Test
    public void shouldOpenWindows() throws Exception {
        Pattern pattern = new Pattern("windows.png");

        Screen screen = new Screen();
        Match match = clickWinButton(pattern, screen);
        saveScreenshot(screen);
        findText(match);
    }

    @Test
    @Title("This is a longer title")
    @Description("This is a very looooong description string")
    @Features(value = {"Component-X", "Component-Y"})
    @Stories(value = {"CIP-123", "CIP-234"})
    public void testCase1() {
        attachLogFile();
    }

    @Test
    @Title("This is a longer title")
    @Description("This is a very looooong description string")
    @Features(value = {"Component-X",})
    @Stories(value = {"CIP-234"})
    public void testCase2() {
    }

    @Test
    @Title("This is a longer title")
    @Description("This is a very looooong description string")
    @Features(value = {"Component-Y"})
    @Stories(value = {"CIP-789"})
    public void testCase3() {
    }

    @Step
    private void findText(Match match) {
        Region above = match.above(300);
        String text = above.text();
        System.out.println(text);
    }

    @Step("Click Win button")
    private Match clickWinButton(Pattern pattern, Screen screen) throws org.sikuli.script.FindFailed {
        Match match = screen.find(pattern);
        match.click();
        return match;
    }

    @Attach(name = "Screenshot", type = AttachmentType.PNG)
    public File saveScreenshot(Screen screen) {
        ScreenImage image = screen.capture();
        return new File(image.getFile());
    }

    @Attach(name = "Log", type = AttachmentType.TXT)
    public String attachLogFile() {
        return "first line\nsecond line\nthird line\n...";
    }

}
