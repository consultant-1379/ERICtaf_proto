package com.ericsson.cifwk.tdm.configuration.changelogs;

import com.ericsson.cifwk.tdm.api.DataRecord;
import com.ericsson.cifwk.tdm.api.DataSourceIdentity;
import com.ericsson.cifwk.tdm.api.Execution;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import org.jongo.Jongo;

import java.util.Date;
import java.util.List;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 19/02/2016
 */
@ChangeLog
public class InitialChangelog {

    @ChangeSet(order = "001", id = "initialSchema", author = "TDM")
    public void schema(DB db) {
        db.createCollection("dataSources", null);
        db.createCollection("dataRecords", null);
        db.createCollection("executions", null);
        db.createCollection("locks", null);
        db.createCollection("dataRecordLocks", null);
        db.createCollection("dataSourceExecutions", null);
        db.getCollection("dataRecordLocks").createIndex(new BasicDBObject("dataRecordId", 1), new BasicDBObject("unique", true));
        db.getCollection("dataRecordLocks").createIndex(new BasicDBObject("expireAt", 1), new BasicDBObject("expireAfterSeconds", 0));
    }

    @ChangeSet(order = "002", id = "mockData", author = "TDM")
    public void mockData(Jongo jongo) {
        //Insert execution
        Execution execution = new Execution();
        execution.id = "56c5ddf29759e577fc68ab7f";
        execution.team = "TAF";
        execution.startTime = new Date();
        jongo.getCollection("executions").insert(execution);

        //Insert execution
        DataSourceIdentity dataSourceIdentity = new DataSourceIdentity();
        dataSourceIdentity.id = "56c5ddd29759e577fc68ab74";
        dataSourceIdentity.version = 1;
        dataSourceIdentity.name = "Star wars planets";
        dataSourceIdentity.description = "Star wars planets";
        dataSourceIdentity.createdBy = "enikoal";
        dataSourceIdentity.createTime = new Date();
        jongo.getCollection("dataSources").insert(dataSourceIdentity);

        //insert data records
        List<DataRecord> dataRecordList = Lists.newArrayList();
        DataRecord dataRecord = new DataRecord();
        dataRecord.id = "56c5ddd29759e577fc68ab75";
        dataRecord.dataSourceId = "56c5ddd29759e577fc68ab74";
        dataRecord.values = Maps.newHashMap();
        dataRecord.values.put("name", "Alderaan");
        dataRecord.values.put("rotation_period", 24);
        dataRecord.values.put("orbital_period", 364);
        dataRecord.values.put("diameter", 12500);
        dataRecord.values.put("climate", "temperate");
        dataRecord.values.put("gravity", "1 standard");
        dataRecord.values.put("terrain", "grasslands, mountains");
        dataRecord.values.put("surface_water", 40);
        dataRecord.values.put("population", "2000000000");
        dataRecordList.add(dataRecord);


        dataRecord = new DataRecord();
        dataRecord.id = "56c5ddd29759e577fc68ab76";
        dataRecord.dataSourceId = "56c5ddd29759e577fc68ab74";
        dataRecord.values = Maps.newHashMap();
        dataRecord.values.put("name", "Yavin IV");
        dataRecord.values.put("rotation_period", 24);
        dataRecord.values.put("orbital_period", 4818);
        dataRecord.values.put("diameter", 10200);
        dataRecord.values.put("climate", "temperate, tropical");
        dataRecord.values.put("gravity", "1 standard");
        dataRecord.values.put("terrain", "jungle, rainforests");
        dataRecord.values.put("surface_water", 8);
        dataRecord.values.put("population", "1000");
        dataRecordList.add(dataRecord);

        dataRecord = new DataRecord();
        dataRecord.id = "56c5ddd29759e577fc68ab77";
        dataRecord.dataSourceId = "56c5ddd29759e577fc68ab74";
        dataRecord.values = Maps.newHashMap();
        dataRecord.values.put("name", "Hoth");
        dataRecord.values.put("rotation_period", 23);
        dataRecord.values.put("orbital_period", 549);
        dataRecord.values.put("diameter", 7200);
        dataRecord.values.put("climate", "frozen");
        dataRecord.values.put("gravity", "1.1 standard");
        dataRecord.values.put("terrain", "tundra, ice caves, mountain ranges");
        dataRecord.values.put("surface_water", 100);
        dataRecord.values.put("population", "unknown");
        dataRecordList.add(dataRecord);

        dataRecord = new DataRecord();
        dataRecord.id = "56c5ddd29759e577fc68ab78";
        dataRecord.dataSourceId = "56c5ddd29759e577fc68ab74";
        dataRecord.values = Maps.newHashMap();
        dataRecord.values.put("name", "Dagobah");
        dataRecord.values.put("rotation_period", 23);
        dataRecord.values.put("orbital_period", 341);
        dataRecord.values.put("diameter", 8900);
        dataRecord.values.put("climate", "murky");
        dataRecord.values.put("gravity", "N/A");
        dataRecord.values.put("terrain", "swamp, jungles");
        dataRecord.values.put("surface_water", 8);
        dataRecord.values.put("population", "unknown");
        dataRecordList.add(dataRecord);

        dataRecord = new DataRecord();
        dataRecord.id = "56c5ddd29759e577fc68ab79";
        dataRecord.dataSourceId = "56c5ddd29759e577fc68ab74";
        dataRecord.values = Maps.newHashMap();
        dataRecord.values.put("name", "Bespin");
        dataRecord.values.put("rotation_period", 12);
        dataRecord.values.put("orbital_period", 5110);
        dataRecord.values.put("diameter", 118000);
        dataRecord.values.put("climate", "temperate");
        dataRecord.values.put("gravity", "1.5 (surface), 1 standard (Cloud City)");
        dataRecord.values.put("terrain", "gas giant");
        dataRecord.values.put("surface_water", 0);
        dataRecord.values.put("population", "6000000");
        dataRecordList.add(dataRecord);

        dataRecord = new DataRecord();
        dataRecord.id = "56c5ddd29759e577fc68ab7a";
        dataRecord.dataSourceId = "56c5ddd29759e577fc68ab74";
        dataRecord.values = Maps.newHashMap();
        dataRecord.values.put("name", "Endor");
        dataRecord.values.put("rotation_period", 18);
        dataRecord.values.put("orbital_period", 402);
        dataRecord.values.put("diameter", 4900);
        dataRecord.values.put("climate", "temperate");
        dataRecord.values.put("gravity", "0.85 standard");
        dataRecord.values.put("terrain", "forests, mountains, lakes");
        dataRecord.values.put("surface_water", 8);
        dataRecord.values.put("population", "30000000");
        dataRecordList.add(dataRecord);

        dataRecord = new DataRecord();
        dataRecord.id = "56c5ddd29759e577fc68ab7b";
        dataRecord.dataSourceId = "56c5ddd29759e577fc68ab74";
        dataRecord.values = Maps.newHashMap();
        dataRecord.values.put("name", "Naboo");
        dataRecord.values.put("rotation_period", 26);
        dataRecord.values.put("orbital_period", 312);
        dataRecord.values.put("diameter", 12120);
        dataRecord.values.put("climate", "temperate");
        dataRecord.values.put("gravity", "1 standard");
        dataRecord.values.put("terrain", "grassy hills, swamps, forests, mountains");
        dataRecord.values.put("surface_water", 12);
        dataRecord.values.put("population", "4500000000");
        dataRecordList.add(dataRecord);

        dataRecord = new DataRecord();
        dataRecord.id = "56c5ddd29759e577fc68ab7c";
        dataRecord.dataSourceId = "56c5ddd29759e577fc68ab74";
        dataRecord.values = Maps.newHashMap();
        dataRecord.values.put("name", "Coruscant");
        dataRecord.values.put("rotation_period", 24);
        dataRecord.values.put("orbital_period", 368);
        dataRecord.values.put("diameter", 12240);
        dataRecord.values.put("climate", "temperate");
        dataRecord.values.put("gravity", "1 standard");
        dataRecord.values.put("terrain", "cityscape, mountains");
        dataRecord.values.put("surface_water", "unknown");
        dataRecord.values.put("population", "1000000000000");
        dataRecordList.add(dataRecord);

        dataRecord = new DataRecord();
        dataRecord.id = "56c5ddd29759e577fc68ab7d";
        dataRecord.dataSourceId = "56c5ddd29759e577fc68ab74";
        dataRecord.values = Maps.newHashMap();
        dataRecord.values.put("name", "Kamino");
        dataRecord.values.put("rotation_period", 27);
        dataRecord.values.put("orbital_period", 463);
        dataRecord.values.put("diameter", 19720);
        dataRecord.values.put("climate", "temperate");
        dataRecord.values.put("gravity", "1 standard");
        dataRecord.values.put("terrain", "ocean");
        dataRecord.values.put("surface_water", 100);
        dataRecord.values.put("population", "1000000000");
        dataRecordList.add(dataRecord);

        dataRecord = new DataRecord();
        dataRecord.id = "56c5ddd29759e577fc68ab7e";
        dataRecord.dataSourceId = "56c5ddd29759e577fc68ab74";
        dataRecord.values = Maps.newHashMap();
        dataRecord.values.put("name", "Geonosis");
        dataRecord.values.put("rotation_period", 30);
        dataRecord.values.put("orbital_period", 256);
        dataRecord.values.put("diameter", 11370);
        dataRecord.values.put("climate", "temperate, arid");
        dataRecord.values.put("gravity", "0.9 standard");
        dataRecord.values.put("terrain", "rock, desert, mountain, barren");
        dataRecord.values.put("surface_water", 5);
        dataRecord.values.put("population", "100000000000");
        dataRecordList.add(dataRecord);

        jongo.getCollection("dataRecords").insert(dataRecordList.toArray());
    }
}
