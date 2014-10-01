package org.apache.nutch.parse.xsl;

import java.util.List;

import junit.framework.TestCase;

/**
 * 
 * Testing the filter that will auto import fields defined in the xsl file.
 * 
 */
public class XslIndexFilterTest extends TestCase {

	/**
	 * Test the fields fetch from xsl file.
	 */
	public void testFields() {
		XslIndexFilter filter = new XslIndexFilter();
		try {
			List<String> list = filter.extractFields("src/plugin/parse-xsl/src/tests/files/copainsdavant/transformer_people.xsl");
			assertNotNull(list);
			assertEquals(6, list.size());
		} catch (Exception e) {
			fail();
		}
	}
}
