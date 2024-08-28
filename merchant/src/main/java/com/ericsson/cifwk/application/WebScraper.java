package com.ericsson.cifwk.application;

import com.ericsson.cifwk.application.util.IO;
import com.ericsson.cifwk.model.Component;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ericsson.cifwk.application.util.Exceptions.propagate;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class WebScraper {

    private static final Logger logger = LoggerFactory.getLogger(WebScraper.class);

    private static final String SSL_TRUST_STORE = "javax.net.ssl.trustStore";
    private static final String JAVA_KEY_STORE = "bazaar.internal.ericsson.com.jks";

    private static final Pattern COMPONENT_ID_PATTERN = Pattern.compile("\\d+");

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:45.0) Gecko/20100101 Firefox/45.0";
    private static final String BAZAAR_HOST = "https://bazaar.internal.ericsson.com/";

    public static final String SEARCH_PAGE = BAZAAR_HOST + "b-view-main-ajax-getsearchresults.php?search=%s&numppage=%d&page=1";
    public static final String COMPONENT_PAGE = BAZAAR_HOST + "b-view-component.php?componentid=";

    private static final int DEFAULT_PAGE_SIZE = 50;

    private ComponentParser parser;

    private String username;
    private String password;

    public WebScraper(ComponentParser parser) {
        this.parser = parser;
        loadCredentials();
        prepareSslCertificate();
    }

    private void loadCredentials() {
        Properties login = IO.loadProperties("bazaar.login.properties");
        username = login.getProperty("username");
        password = login.getProperty("password");
    }

    private void prepareSslCertificate() {
        if (Files.exists(Paths.get(JAVA_KEY_STORE))) {
            System.setProperty(SSL_TRUST_STORE, JAVA_KEY_STORE);
        } else {
            logger.error("Java key store '{}' not found, please see README.md", JAVA_KEY_STORE);
        }
    }

    /**
     * @param queries number of arbitrary query string to be searched in Bazaar
     * @return map containing query strings as keys and list of found components
     * as values; if nothing has been found, then value will be an empty list
     */
    public Map<String, List<Component>> search(String... queries) {
        Map<String, List<Component>> searchResults = new LinkedHashMap<>();
        int counter = 0;
        for (String query : queries) {
            logger.info("Searching for a query '{}'... ({}/{})", query, ++counter, queries.length);

            List<Component> results = search(query);
            searchResults.put(query, results);

            logger.info("Got {} search results for a query '{}'", results.size(), query);
        }
        return searchResults;
    }

    private List<Component> search(String query) {
        Matcher matcher = COMPONENT_ID_PATTERN.matcher(query);
        return matcher.matches()
                ? searchByComponentId(query)
                : searchByKeyWord(query);
    }

    private List<Component> searchByComponentId(String componentId) {
        Document page = loadDocument(COMPONENT_PAGE + componentId);
        Element componentData = page.select("#componentdata").first();
        Optional<Component> component = parser.parseComponentPage(componentId, componentData.text());
        return component.map(Collections::singletonList).orElseGet(Collections::emptyList);
    }

    private List<Component> searchByKeyWord(String query) {
        List<Component> allResults = new ArrayList<>();
        Set<String> variants = getAllVariants(query);
        for (String variant : variants) {
            List<Component> results = searchByKeyWordAutoPageSize(variant);
            if (variants.size() > 1) {
                logger.info("Got {} search results for a variant '{}'", results.size(), variant);
            }
            allResults.addAll(results);
        }
        return allResults;
    }

    private List<Component> searchByKeyWordAutoPageSize(String variant) {
        int pageSize = DEFAULT_PAGE_SIZE;
        boolean pageSizeTooSmall;
        List<Component> results;

        do {
            results = searchByKeyWord(variant, pageSize);
            pageSizeTooSmall = results.size() >= pageSize;
            if (pageSizeTooSmall) {
                logger.warn("The number of the search results has hit the maximum page size ({})", pageSize);
                pageSize *= 2;
            }
        } while (pageSizeTooSmall);

        return results;
    }

    private Set<String> getAllVariants(String query) {
        Set<String> modifications = new LinkedHashSet<>();
        modifications.add(query);
        modifications.add(query.replaceAll("-", " "));
        return modifications;
    }

    private List<Component> searchByKeyWord(String keyWord, int pageSize) {
        Document page = loadDocument(format(SEARCH_PAGE, keyWord, pageSize));
        Element searchResult = page.select("#result_search_div").first();
        if (multipleResultsFound(searchResult)) {
            return scrapSearchPage(searchResult);
        } else if (singleResultFound(searchResult)) {
            return scrapComponentPage(searchResult);
        } else {
            return emptyList();
        }
    }

    private boolean multipleResultsFound(Element searchResult) {
        return hasTag(searchResult, "table");
    }

    /**
     * In case when only one search result is found,
     * Bazaar returns a {@code <meta>} tag containing
     * a redirect to the component page
     */
    private boolean singleResultFound(Element searchResult) {
        return hasTag(searchResult, "meta");
    }

    private boolean hasTag(Element element, String tag) {
        return !element.select(tag).isEmpty();
    }

    private List<Component> scrapSearchPage(Element searchResult) {
        return searchResult.select(".row").stream()
                .map(this::scrapSearchPageEntry)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    private Optional<Component> scrapSearchPageEntry(Element row) {
        String text = row.text();
        String componentIdAttr = row.attr("onclick");
        String componentId = parser.parseComponentId(componentIdAttr);
        return parser.parseSearchPageEntry(componentId, text);
    }

    private List<Component> scrapComponentPage(Element searchResult) {
        String componentPageUrl = searchResult.child(1).attr("content");
        String componentId = parser.parseComponentId(componentPageUrl);
        return searchByComponentId(componentId);
    }

    private Document loadDocument(String url) {
        try {
            return Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .data("username", username)
                    .data("password", password)
                    .post();
        } catch (IOException e) {
            throw propagate(e, "Could not load document '%s'", url);
        }
    }
}
