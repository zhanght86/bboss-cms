package com.sany.workflow.job.action;

import org.junit.Before;
import org.junit.Test;

public class CheckProcessOvertimeTest {
private static CheckProcessOvertime testCase = new CheckProcessOvertime();
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCheckProcessOvertimeJob() throws Exception {
		testCase.checkProcessOvertimeJob();
	}

}
