package com.ericsson.eiffel.er.rest.resource;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoQueryTest {
	//	private MongoClient mongoClient;
	private DB db;
	private DBCollection eventCollection;

	@Before
	public void setUp() {
		try {
			MongoClient mongoClient = new MongoClient();
			db = mongoClient.getDB("perf-test-events");
			eventCollection = db.getCollection("allevents");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@After
	public void tearDown() {
		//		db.
	}

	@Test
	public void findEventById() throws Exception {
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("eventId", "29393ec0-c233-4877-82c8-e74443c5ae2d");

		DBCursor cursor = eventCollection.find(searchQuery);

		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}
	}

	@Test
	public void eventsInTimePeriod() throws Exception {

		String startTimeStr = "03/02/2014 12:04:00";
		String endTimeStr = "05/02/2014 14:04:00";

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		Date startTime = formatter.parse(startTimeStr);
		Date endTime = formatter.parse(endTimeStr);
		
		DBObject query = new BasicDBObject();
//		query.put("eventTime", new BasicDBObject("$gt", startTime).append("$lte", endTime));
		//		query.put("eventTime", "2014-02-03T15:01:46.391Z");
		query.put("eventTime", new BasicDBObject("$gt", "2014-02-06T15:11:33.000Z").append("$lt", "2014-02-06T15:15:33.000Z"));

		Function<Object[], DBCursor> f = new Function<Object[], DBCursor>() {
			@Override
			public DBCursor apply(Object[] input) {
				DBCursor cursor = eventCollection.find((DBObject)input[0]);
				while (cursor.hasNext()) {
					cursor.next();
				}
				return null;
			}
		};
		
		executeAndRegisterTime(f, query);
	}

	@Test
	public void sampleCount() throws Exception {
		final DBObject match = new BasicDBObject("$match", new BasicDBObject("eventType", "TestSampleEvent") );
		DBObject groupFields = new BasicDBObject( "_id", "null");
		groupFields.put("total", new BasicDBObject( "$sum", 1));
		final DBObject group = new BasicDBObject("$group", groupFields);

		Function<Object[], AggregationOutput> f = new Function<Object[], AggregationOutput>() {
			@Override
			public AggregationOutput apply(Object[] input) {
				return eventCollection.aggregate((DBObject)input[0], (DBObject)input[1]);
			}
		};
		
		AggregationOutput output = executeAndRegisterTime(f, match, group);
		
		System.out.println(output);
	}

	@Test
	public void executeCommand() throws Exception {
		MongoClient mongoClient = new MongoClient();
		DB db = mongoClient.getDB("test-events-stage");

		// Get amount of successful samples
		CommandResult command = db.command("{ $group : { _id : \"$eventId\", totalPop : { $sum : \"$size\" } } }");
		System.out.println(command);
	}
	
	private <T> T executeAndRegisterTime(Function<Object[], T> function, Object... params) {
		long start = System.currentTimeMillis();
		T result = function.apply(params);
		System.out.println("Execution took " + (System.currentTimeMillis() - start) + " millis");
		return result;
	}
}
