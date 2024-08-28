package com.ericsson.cifwk.sikuli

import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer
import org.sikuli.basics.Debug
import org.sikuli.basics.Settings

/**
 *
 */
class ScriptRunner {

    static void main(String[] args) {
        configureSikuli()

        def configuration = new CompilerConfiguration()
        def customizer = new ImportCustomizer()
        customizer.addStarImports("org.sikuli.script")
        def binding = new Binding([
                api: new GuiTesting()
        ])
        def shell = new GroovyShell(binding, configuration);
        shell.evaluate(new File("./sikuli-tests/src/test/groovy/tests/Script.groovy"))
    }

    private static void configureSikuli() {
        Debug.setDebugLevel(3)

        Settings.OcrTextSearch = true;
        Settings.OcrTextRead = true;
    }

}
