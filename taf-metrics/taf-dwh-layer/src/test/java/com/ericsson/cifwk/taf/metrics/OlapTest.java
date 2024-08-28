package com.ericsson.cifwk.taf.metrics;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.olap4j.Axis;
import org.olap4j.CellSet;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.layout.RectangularCellSetFormatter;
import org.olap4j.mdx.IdentifierNode;
import org.olap4j.metadata.Cube;
import org.olap4j.metadata.Hierarchy;
import org.olap4j.metadata.Member;
import org.olap4j.query.Query;
import org.olap4j.query.QueryAxis;
import org.olap4j.query.QueryDimension;
import org.olap4j.query.Selection;

import java.io.PrintWriter;

/**
 *
 */
public class OlapTest {

    public static final String CUBE_NAME = "Performance Testing";
    public static final String DIMENSION_MEASURE = "Measures";
    public static final String DIMENSION_TARGET = "Target";

    OlapServer olapServer;

    @Before
    public void setUp() {
        olapServer = new OlapServer();
        olapServer.connect();
    }

    @Test
    public void queryExecution() throws Exception {
        CellSet query = olapServer.mdx("SELECT\n" +
                "NON EMPTY {[Measures].[Avg Response Time]} ON COLUMNS,\n" +
                "NON EMPTY {{[Time.Time Hierarchy].[Day].Members}} ON ROWS\n" +
                "FROM [Performance Testing]");

        printResults(query);
    }

    @Test
    public void standardDeviation() throws Exception {
        CellSet query = olapServer.mdx("SELECT\n" +
                "NON EMPTY {[Measures].[Standard Deviation]} ON COLUMNS,\n" +
                "NON EMPTY {{[Time.Time Hierarchy].[Day].Members}} ON ROWS\n" +
                "FROM [Performance Testing]");

        printResults(query);
    }

    @Test
    @Ignore
    public void basicQuery() throws Exception {
        OlapConnection connection = olapServer.getOlapConnection();
        Cube cube = connection.getOlapSchema().getCubes().get(CUBE_NAME);

        Query query = new Query("q", cube);

        QueryDimension measuresDim = query.getDimension(DIMENSION_MEASURE);
        query.getAxis(Axis.COLUMNS).addDimension(measuresDim);

        QueryDimension targetDim = query.getDimension(DIMENSION_TARGET);
        targetDim.include(Selection.Operator.MEMBER, IdentifierNode.ofNames("URI").getSegmentList());

        query.getAxis(Axis.ROWS).addDimension(targetDim);

        execute(query);
    }

    @Test
    public void timeBasedQuery() throws Exception {
        OlapConnection connection = olapServer.getOlapConnection();
        Cube cube = connection.getOlapSchema().getCubes().get(CUBE_NAME);

        Query query = new Query("q", cube);

        QueryDimension measuresDim = query.getDimension(DIMENSION_MEASURE);
        QueryAxis columnAxis = query.getAxis(Axis.COLUMNS);
        columnAxis.addDimension(measuresDim);

        Hierarchy timeHierarchy = cube.getHierarchies().get("Time.Time Hierarchy");
        Member member = timeHierarchy.getLevels().get("Day").getMembers().get(0);
        QueryDimension targetDim = query.getDimension(DIMENSION_TARGET);
        targetDim.include(member);

        query.getAxis(Axis.ROWS).addDimension(targetDim);

        execute(query);
    }

    private void execute(Query myQuery) throws OlapException {
        myQuery.validate();

        System.out.println("==========================================================");
        System.out.println(myQuery.getSelect().toString());
        System.out.println("==========================================================");

        CellSet cellSet = myQuery.execute();

        printResults(cellSet);
    }

    private void printResults(CellSet execute) {
        RectangularCellSetFormatter formatter = new RectangularCellSetFormatter(false);
        PrintWriter writer = new PrintWriter(System.out);
        formatter.format(execute, writer);
        writer.flush();
    }

    @After
    public void tearDown() {
        olapServer.disconnect();
    }

}
