package org.apache.nutch.parse.xsl;

import org.apache.log4j.PropertyConfigurator;
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.parse.ParseResult;
import org.apache.nutch.parse.xsl.XslParseFilter.PARSER;

/**
 * This is the base class to create AmisFinder related tests. Amis Finder is a
 * search engine that indexes several other sites related to people searching
 * for other people. These sites will index two kinds of information:
 * <ul>
 * <li>people and their related people</li>
 * <li>wanted people that are searched by other people</li>
 * </ul>
 * 
 * @author albo
 * 
 */
public abstract class AbstractAmisFinder extends AbstractCrawlTest {

	/** The extraction method to use */
	public enum EXTRACT_METHOD {
		/** Metadata are extracted using XSLT */
		XSLT_EXTRACTION,
		/** Metadata are extracted using NodeWalker (DOM iteration) */
		NODE_WALKER_EXTRACTION
	}

	static {
		PropertyConfigurator.configure("conf/log4j.properties");
	}

	/** The current parser to use */
	protected PARSER parser = null;

	/**
	 * @param parser the parser to use
	 * Instanciates the class given a specific parser. 
	 */
	protected AbstractAmisFinder(PARSER parser) {
		this.parser = parser;
		// Depending of the parameters, the configuration will be built
		// differently */
		if (parser == PARSER.NEKO) {
			this.getConfiguration().set(XslParseFilter.CONF_HTML_PARSER, PARSER.NEKO.toString());
		} else {
			this.getConfiguration().set(XslParseFilter.CONF_HTML_PARSER, PARSER.TAGSOUP.toString());
		}

	}

	/**
	 * Asserts that the following metadata have the expected values.
	 * 
	 * @param metadata
	 *            the metadata object containing parsed values.
	 * @param firstName
	 * @param lastName
	 * @param gender
	 * @param city
	 * @param country
	 * @param birthDate
	 */
	protected void assertMetadataPeople(Metadata metadata, String firstName, String lastName, String gender, String city, String country, String birthDate, String age) {
		// Testing first name
		assertEquals(firstName, metadata.get(CommonMetadata.META_PEOPLE_FIRST_NAME));

		// Testing last name
		assertEquals(lastName, metadata.get(CommonMetadata.META_PEOPLE_LAST_NAME));

		// Testing gender
		assertEquals(gender, metadata.get(CommonMetadata.META_PEOPLE_GENDER));

		// Testing city
		assertEquals(city, metadata.get(CommonMetadata.META_PEOPLE_CITY));

		// Testing country
		assertEquals(country, metadata.get(CommonMetadata.META_PEOPLE_COUNTRY));

		// Testing birth date
		assertEquals(birthDate, metadata.get(CommonMetadata.META_PEOPLE_BIRTH_DATE));

		// Testing age
		assertEquals(age, metadata.get(CommonMetadata.META_PEOPLE_AGE));
	}

	/**
	 * 
	 * @param metadata
	 * @param wantedPeople
	 * @param searcherPeople
	 * @param description
	 */
	protected void assertMetadataWantedPeople(Metadata metadata, String wantedPeople, String searcherPeople, String description) {

		assertEquals(searcherPeople, metadata.get(CommonMetadata.META_SEARCHER_PEOPLE));
		assertEquals(wantedPeople, metadata.get(CommonMetadata.META_WANTED_PEOPLE));
		assertEquals(description, metadata.get(CommonMetadata.META_DESCRIPTION));
	}

	/**
	 * Standard test to simulate a crawl on a people. You should call it in your
	 * extended test class and make some assertions.
	 * 
	 * @param url
	 *            the url to simulate (used as identifier for rules)
	 * @param filePath
	 *            the file to crawl on your file system
	 * @return {@link Metadata} that were extracted.
	 * @throws Exception
	 */

	public Metadata startTestPeople(String url, String filePath) throws Exception {

		ParseResult parseResult = simulateCrawl(this.parser, filePath, url);
		assertNotNull(parseResult);

		Metadata parsedMetadata = parseResult.get(url).getData().getParseMeta();
		assertNotNull(parsedMetadata);
		return parsedMetadata;
	}

	/**
	 * Tests the fetch of a wanted people. Standard test to simulate a crawl on
	 * a wanted people. You should call it in your extended test class and make
	 * some assertions.
	 * 
	 * @param url
	 *            the url to simulate (used as identifier for rules)
	 * @param filePath
	 *            the file to crawl on your file system
	 * @return {@link Metadata} that were extracted.
	 * @throws Exception
	 */
	public Metadata startTestWantedPeople(String url, String filePath) throws Exception {
		ParseResult parseResult = simulateCrawl(this.parser, filePath, url);
		assertNotNull(parseResult.get(url));
		Metadata parsedMetadata = parseResult.get(url).getData().getParseMeta();
		assertNotNull(parsedMetadata);
		return parsedMetadata;
	}
}
