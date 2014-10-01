package org.apache.nutch.parse.xsl;

import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.PropertyConfigurator;
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.parse.xsl.XslParseFilter.PARSER;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * A simple class to extract some data from a http://www.marmiton.com page. This
 * is dedicated to meta extraction for recipes.
 */
@RunWith(Parameterized.class)
public class CopainsDavantTest extends AbstractAmisFinder {

	static {
		PropertyConfigurator.configure("conf/log4j.properties");
	}

	/**
	 * 
	 * @param parser
	 *            the parser to use the extract method to use
	 */
	public CopainsDavantTest(PARSER parser) {
		super(parser);
		this.getConfiguration().set(RulesManager.CONF_XML_RULES, "src/plugin/parse-xsl/src/tests/files/copainsdavant/rules.xml");
	}

	/**
	 * Initializes parameters. TODO integrate test for Tag Soup when working.
	 * 
	 * @return the collection of parameters.
	 */
	@Parameters(name = "{index}: parser {0}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { { PARSER.NEKO } });
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPeople() throws Exception {

		Metadata metadata = super.startTestPeople("http://copainsdavant.linternaute.com/p/dominique-a-6984602", "src/plugin/parse-xsl/src/tests/files/copainsdavant/people1.html");

		this.assertMetadataPeople(metadata, "Dominique", "A.", "male", "BRETIGNY SUR ORGE", "France", "1977", null);
	}

	/**
	 * Tests the fetch of a wanted people.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testWantedPeople() throws Exception {

		Metadata metadata = super.startTestWantedPeople("http://copainsdavant.linternaute.com/recherche-amis/danielle-uchi-6460", "src/plugin/parse-xsl/src/tests/files/copainsdavant/wanted1.html");

		this.assertMetadataWantedPeople(
				metadata,
				"Danielle Uchi",
				"Jean Michel LE CLERC",
				"Jean Michel LE CLERC Pendant les cours, je dessinai un personnage un peu bizarre sur des petits bouts de papier, notre domaine, à la sortie de l'ESD, était Capoulade, le boul. St-Michel... il y a 2 jours");

	}
}
