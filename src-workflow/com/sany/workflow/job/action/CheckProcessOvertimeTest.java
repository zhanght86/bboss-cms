package com.sany.workflow.job.action;

import static org.junit.Assert.*;

import java.sql.SQLException;

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
