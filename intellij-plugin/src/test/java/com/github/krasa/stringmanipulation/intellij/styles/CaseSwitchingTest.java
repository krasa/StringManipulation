package com.github.krasa.stringmanipulation.intellij.styles;

import com.github.krasa.stringmanipulation.intellij.CaseSwitchingSettings;
import com.intellij.openapi.diagnostic.Logger;
import org.junit.After;
import org.junit.Before;

public abstract class CaseSwitchingTest {
	private static final Logger LOG = com.intellij.openapi.diagnostic.Logger.getInstance(CaseSwitchingTest.class);
	protected CaseSwitchingSettings caseSwitchingSettings = CaseSwitchingSettings.getInstance();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		caseSwitchingSettings.resetToDefault();
	}
}
