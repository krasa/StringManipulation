package osmedile.intellij.stringmanip.styles;

import com.intellij.openapi.diagnostic.Logger;
import org.junit.After;
import org.junit.Before;
import osmedile.intellij.stringmanip.CaseSwitchingSettings;

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
