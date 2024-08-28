package com.ericsson.duraci.test.helpers;

import org.junit.Assert;
import org.junit.Test;

import com.ericsson.duraci.datawrappers.ResultCode;

public class EventBuilderHelperTest {

	@Test
	public void getRandomResultCode() {
		ResultCode resultCode = EventBuilderHelper.getRandomResultCode(0.8);
		Assert.assertNotNull(resultCode);
	}

}
