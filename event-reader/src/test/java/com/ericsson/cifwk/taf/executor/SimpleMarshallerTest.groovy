package com.ericsson.cifwk.taf.executor;

import static org.junit.Assert.*

import org.testng.annotations.Test

import com.ericsson.cifwk.taf.executor.message.SimpleMarshaller
import com.ericsson.cifwk.taf.executor.messagebus.EventMessage

class SimpleMarshallerTest {

	@Test
	public void marshall(){
		EventMessage m = new EventMessage()
		m.metaData = [a:"23123"]
		m.duration = 7
		new SimpleMarshaller().with {
			println toXml(m)
			println toXml(new Aclass())
			Aclass a = new Aclass()
			a.ala = "asdasd"
			println toXml(a)
			
			Bclass b = new Bclass()
			b.ala = "asdasd"
			
			println toXml(b)
			
			toObject(EventMessage, toXml(m))
			
			println toJson(m)
			println toObject(EventMessage,toJson(m))
		}
		
	}
}
