package com.ericsson.duraci.test.helpers;

import java.util.regex.Pattern;

import org.codehaus.plexus.util.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import com.ericsson.duraci.datawrappers.ExecutionId;
import com.ericsson.duraci.eiffelmessage.messages.EiffelMessage;
import com.ericsson.duraci.eiffelmessage.messages.events.testing.TestSampleEvent;
import com.ericsson.duraci.eiffelmessage.messages.v2.EiffelMessageImpl;
import com.ericsson.duraci.eiffelmessage.serialization.Serializer;
import com.ericsson.duraci.eiffelmessage.serialization.printing.Printer;

public class SampleEventBuilderTest {

	@Test
	public void autoReplaceNumberPlaceholders() {
		String result = new SampleEventBuilder().autoReplaceNumberPlaceholders("<<XXX>><<X>>");
		Assert.assertTrue(StringUtils.isNumeric(result));
		
		String url = "http://ess-system-<<XXX>>/resource/restAction<<X>>.do";
		Pattern numberPlaceholder = Pattern.compile("(<<X+>>)");
		Assert.assertTrue(numberPlaceholder.matcher(url).find(0));
		result = new SampleEventBuilder().autoReplaceNumberPlaceholders(url);
		Assert.assertFalse(numberPlaceholder.matcher(result).find(0));

		result = new SampleEventBuilder().autoReplaceNumberPlaceholders("aa<<XXX>>/<<another>>");
		Assert.assertTrue(StringUtils.contains(result, "<<another>>"));
	}
	
	@Test
	public void autoReplaceGuidPlaceholders() {
		String result = new SampleEventBuilder().autoReplaceGuidPlaceholders("<<GUID>>");
		Assert.assertFalse(StringUtils.contains(result, "<<GUID>>"));
	}

	@Test
	public void randomValueInRange() {
		int randomValueInRange = new SampleEventBuilder().randomValueInRange("100-10000");
		Assert.assertTrue(randomValueInRange >= 100 && randomValueInRange <= 10000);
	}
	
	@Test
	public void createTestSampleEvent() throws Exception {
		TestSampleEvent event = new SampleEventBuilder().createTestSampleEvent(new ExecutionId(), 123456L);
        
		EiffelMessage message = new EiffelMessageImpl("domainId", event);
		
		Serializer serializer = new Serializer();
        Printer printer = serializer.pretty(message);

        System.out.println(printer.print());
	}
}
