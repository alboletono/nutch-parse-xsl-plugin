package org.apache.nutch.parse.xsl;

import java.io.FileReader;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *  
 */
public class ParseTechnicalTests extends AbstractCrawlTest {

	/**
	 * Executes some xpath on neko parsed document
	 */
	public void testXpathNeko() {
		try {
			DocumentFragment doc = parseNeko(new InputSource(new FileReader("src/plugin/parse-xsl/src/tests/files/copainsdavant/people1.html")));
			XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList result = (NodeList) xpath.compile("//DIV").evaluate(doc, XPathConstants.NODESET);
			assertNotNull(result);
			assertEquals(111, result.getLength());
			System.out.println(result.getLength());
			result = (NodeList) xpath.compile("//HTML").evaluate(doc, XPathConstants.NODESET);
			assertNotNull(result);
			System.out.println(result.getLength());
			assertEquals(1, result.getLength());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Executes some xpath on TagSoup parsed document TODO not working with
	 * TagSoup. Investigate why.
	 */
	public void testXpathTagSoup() {
		try {
			DocumentFragment doc = parseTagSoup(new InputSource(new FileReader("src/plugin/parse-xsl/src/tests/files/copainsdavant/people1.html")));
			XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList result = (NodeList) xpath.compile("//div").evaluate(doc, XPathConstants.NODESET);
			assertNotNull(result);
			assertEquals(111, result.getLength());
			System.out.println(result.getLength());
			result = (NodeList) xpath.compile("//html").evaluate(doc, XPathConstants.NODESET);
			assertNotNull(result);
			System.out.println(result.getLength());
			assertEquals(1, result.getLength());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
