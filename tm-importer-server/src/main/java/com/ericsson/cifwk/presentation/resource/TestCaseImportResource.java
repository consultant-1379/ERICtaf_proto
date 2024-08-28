
package com.ericsson.cifwk.presentation.resource;

import com.ericsson.cifwk.infrastructure.PropertyLoader;
import com.ericsson.cifwk.infrastructure.RestClient;
import com.ericsson.cifwk.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.presentation.dto.doc.Impl.TextParser;
import com.ericsson.cifwk.presentation.dto.doc.converter.TestCaseInfoConverterForDoc;
import com.ericsson.cifwk.presentation.dto.excel.Reader.ReaderForXLS;
import com.ericsson.cifwk.presentation.dto.excel.Reader.SimpleReaderForXLS;
import com.ericsson.cifwk.presentation.dto.excel.Writer.XlsWriter;
import com.ericsson.cifwk.presentation.dto.excel.converter.SimpleTestCaseInfoConverter;
import com.ericsson.cifwk.presentation.dto.excel.converter.TestCaseInfoConverter;
import com.google.common.collect.Lists;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

// The Java class will be hosted at the URI path "/myresource"
@Path("/test-cases")
public class TestCaseImportResource {

    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String CLIENT_URL = "clientUrl";
    public static final String PATH = "path";

    private String testCaseIdPattern = "testCaseIdPattern";
    private String defaultRequirment = "defaultDocRequirement";

    private final String datePattern = "dd-MM-yyyy_HH-mm-ss";


    @GET
    @Produces("text/plain")
    public String getIt() {
        return "Test Cases!";
    }


    @POST
    @Path("/import/xls")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTestCases(@FormDataParam("file") InputStream fileInputStream,
                                    @FormDataParam("file") FormDataContentDisposition fileDisposition) {

        ReaderForXLS xlsReader = new ReaderForXLS();
        List<TestCaseInfo> undelivered = Lists.newArrayList();
        TestCaseInfoConverter testCaseInfoConverter = new TestCaseInfoConverter();
        XlsWriter xlsWriter = new XlsWriter();

        if (!fileDisposition.getFileName().endsWith(".xls")) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("File is not an .xls file extension")
                    .build();
        }

        try {
            List<Map> xlsData = xlsReader.parse(fileInputStream);
            testCaseInfoConverter.setTestCaseIdPart(PropertyLoader.getProperty(testCaseIdPattern).toString());
            List<TestCaseInfo> json = testCaseInfoConverter.convertToTestCaseInfo(xlsData);

            RestClient restClient = new RestClient(PropertyLoader.getProperty(CLIENT_URL).toString());
            restClient.login(PropertyLoader.getProperty(USER).toString(),
                    PropertyLoader.getProperty(PASSWORD).toString());

            Response response;
            for(TestCaseInfo testCaseInfo: json){
                response = restClient.path(PropertyLoader.getProperty(PATH).toString())
                        .request()
                        .post(Entity.entity(testCaseInfo, MediaType.APPLICATION_JSON));

                if (response.getStatus() != Response.Status.CREATED.getStatusCode()){
                    undelivered.add(testCaseInfo);
                }
            }
            restClient.logout();

            DateFormat dateFormat = new SimpleDateFormat(datePattern);
            Date date = new Date();
            xlsWriter.writeToFile(undelivered, dateFormat.format(date));

            return Response
                    .status(Response.Status.OK)
                    .entity(undelivered)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }

    }

    @POST
    @Path("/import/simple/xls")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSimpleTestCases(@FormDataParam("file") InputStream fileInputStream,
                                    @FormDataParam("file") FormDataContentDisposition fileDisposition) {

        SimpleReaderForXLS xlsReader = new SimpleReaderForXLS();
        List<TestCaseInfo> undelivered = Lists.newArrayList();
        SimpleTestCaseInfoConverter testCaseInfoConverter = new SimpleTestCaseInfoConverter();
        XlsWriter xlsWriter = new XlsWriter();

        if (!fileDisposition.getFileName().endsWith(".xls")) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("File is not an .xls file extension")
                    .build();
        }

        try {
            List<Map> xlsData = xlsReader.parse(fileInputStream);
            List<TestCaseInfo> json = testCaseInfoConverter.convertToTestCaseInfo(xlsData);

            RestClient restClient = new RestClient(PropertyLoader.getProperty(CLIENT_URL).toString());
            restClient.login(PropertyLoader.getProperty(USER).toString(),
                    PropertyLoader.getProperty(PASSWORD).toString());

            Response response;
            for(TestCaseInfo testCaseInfo: json){
                response = restClient.path(PropertyLoader.getProperty(PATH).toString())
                        .request()
                        .post(Entity.entity(testCaseInfo, MediaType.APPLICATION_JSON));

                if (response.getStatus() != Response.Status.CREATED.getStatusCode()){
                    undelivered.add(testCaseInfo);
                }
            }
            restClient.logout();

            return Response
                    .status(Response.Status.OK)
                    .entity(undelivered)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }

    }

    @POST
    @Path("/import/doc")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTestCasesFromOOXML(@FormDataParam("file") InputStream fileInputStream,
                                    @FormDataParam("file") FormDataContentDisposition fileDisposition) {

        List<TestCaseInfo> json;
        TestCaseInfoConverterForDoc testCaseInfoConverter = new TestCaseInfoConverterForDoc();
        List<TestCaseInfo> undelivered = Lists.newArrayList();
        List<Map> datum;
        XlsWriter xlsWriter = new XlsWriter();

        TextParser testParser = new TextParser();

        if (!fileDisposition.getFileName().endsWith(".txt")) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("File is not an .docx file extension")
                    .build();
        }

        try {
            datum = testParser.parse(fileInputStream);
            testCaseInfoConverter.setTestCaseIdPattern(PropertyLoader.getProperty(testCaseIdPattern).toString());
            testCaseInfoConverter.setLegacyRequirement(PropertyLoader.getProperty(defaultRequirment).toString());
            json = testCaseInfoConverter.convertToTestCaseInfo(datum);

            RestClient restClient = new RestClient(PropertyLoader.getProperty(CLIENT_URL).toString());
            restClient.login(PropertyLoader.getProperty(USER).toString(),
                    PropertyLoader.getProperty(PASSWORD).toString());

            Response response;
            for (TestCaseInfo testCaseInfo: json) {
                response = restClient.path(PropertyLoader.getProperty(PATH).toString())
                        .request()
                        .post(Entity.entity(testCaseInfo, MediaType.APPLICATION_JSON));

                if (response.getStatus() != Response.Status.CREATED.getStatusCode()){
                    undelivered.add(testCaseInfo);
                }
            }

            restClient.logout();

            DateFormat dateFormat = new SimpleDateFormat(datePattern);
            Date date = new Date();
            xlsWriter.writeToFile(undelivered, dateFormat.format(date));

            System.out.println("Test Cases Uploaded: "+ json.size());

            return Response
                    .status(Response.Status.OK)
                    .entity(undelivered)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }

    }
}
