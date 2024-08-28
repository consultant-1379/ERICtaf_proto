package com.ericsson.cifwk;

import com.ericsson.cifwk.application.ComponentParser;
import com.ericsson.cifwk.application.DependencyAnalyzer;
import com.ericsson.cifwk.application.WebScraper;
import com.ericsson.cifwk.application.print.ComponentsConsolePrinter;
import com.ericsson.cifwk.application.print.ComponentsCsvPrinter;
import com.ericsson.cifwk.application.print.DependencyPrinter;
import com.ericsson.cifwk.application.print.Printer;
import com.ericsson.cifwk.application.util.IO;
import com.ericsson.cifwk.model.Component;
import com.ericsson.cifwk.model.Dependency;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class Main {

    private WebScraper scraper;
    private DependencyAnalyzer analyzer;
    private DependencyPrinter printer;
    private List<Printer> printers;

    public static void main(String[] args) {
        new Main().run(args);
    }

    private Main() {
        ComponentParser parser = new ComponentParser();
        scraper = new WebScraper(parser);
        analyzer = new DependencyAnalyzer(scraper);
        printer = new DependencyPrinter("dependencies");
        printers = asList(
                new ComponentsConsolePrinter(),
                new ComponentsCsvPrinter("components")
        );
    }

    private void run(String... args) {
        if (args.length == 0) {
            printUsage();
        } else if (args.length == 1) {
            determineMode(args[0]);
        } else {
            searchByQueries(args);
        }
    }

    private void printUsage() {
        IO.printToConsole("description.txt");
    }

    private void determineMode(String arg) {
        Path path = Paths.get(arg);
        if (Files.exists(path)) {
            analyzeDependencies(path);
        } else {
            searchByQueries(arg);
        }
    }

    private void analyzeDependencies(Path path) {
        Map<Dependency, List<Component>> dependencyMap = analyzer.analyze(path);
        printer.print(dependencyMap);
    }

    private void searchByQueries(String... queries) {
        Map<String, List<Component>> results = scraper.search(queries);
        for (Printer printer : printers) {
            printer.print(results);
        }
    }
}
