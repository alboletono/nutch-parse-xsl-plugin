package org.apache.nutch.parse.xsl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.nutch.crawl.CrawlDatum;
import org.apache.nutch.crawl.Inlinks;
import org.apache.nutch.indexer.IndexingException;
import org.apache.nutch.indexer.IndexingFilter;
import org.apache.nutch.indexer.NutchDocument;
import org.apache.nutch.parse.Parse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xpath.internal.XPathAPI;

/**
 * This class allows to:
 * <ul>
 * <li>index automatically fields defined in rules file.
 * <li>exlude urls that are not declared in the rules file.
 */
public class XslIndexFilter implements IndexingFilter {

	private static final String NAME_ATTRIBUTE = "name";

	private static final String FIELD_TAG = "//field";

	private Configuration conf;

	private static final Logger LOG = LoggerFactory.getLogger(XslParseFilter.class);

	private static HashMap<String, List<String>> transformers = new HashMap<String, List<String>>();

	/**
	 * @return the current configuration.
	 */
	@Override
	public Configuration getConf() {
		return this.conf;
	}

	/**
	 * Sets the current configuration.
	 */
	@Override
	public void setConf(Configuration conf) {
		this.conf = conf;
	}

	@Override
	public NutchDocument filter(NutchDocument doc, Parse parse, Text url, CrawlDatum datum, Inlinks inlinks) throws IndexingException {

		NutchDocument result = null;
		if (doc == null)
			return result;

		try {

			// Rules manager that contains url corresponding transformer.
			RulesManager manager = RulesManager.getInstance(this.conf);

			// Getting transformer file path associated to rule if exists
			String xsltFilePath = null;
			try {
				xsltFilePath = manager.getTransformerFilePath(url.toString());
			} catch (Exception e) {
				LOG.info("Xslt not found");
			}

			// The url matches a rule, we keep it
			if (xsltFilePath != null) {
				// We keep the document
				result = doc;
				List<String> fields = XslIndexFilter.transformers.get(xsltFilePath);
				// List was never loaded
				if (fields == null) {
					fields = this.extractFields(xsltFilePath);
				}

				// All the fields defined in the xsl file will be put directly
				// into the nutch document
				// Fields defined by the xsl plugin are only stored in parse
				// meta.
				if (parse != null && parse.getData() != null && parse.getData().getParseMeta() != null) {
					for (String field : fields) {
						String value = parse.getData().getParseMeta().get(field);
						doc.add(field, value);
					}
				}

			}
			// The document is indexed anyway because explicitly decided
			else if (!manager.getRules().isFilterUrlsWithNoRule()) {
				result = doc;
				LOG.info("The url " + url.toString() + " has been kept because it has been explicitly specified in the rules");
			}
			// The document is not indexed
			else {
				LOG.info("The url " + url.toString() + " has been filtered because no xsl file fits the defined rules");
			}

		} catch (Exception e) {
			String message = "Cannot index data";
			if (url != null && url.toString() != null) {
				message += " from " + url.toString();
			}
			LOG.error(message, e);
		}

		return result;
	}

	/**
	 * 
	 * @param xsltFilePath
	 *            the path of the xsl file
	 * @return the list of fields defined in xsl file
	 * @throws Exception
	 */
	protected List<String> extractFields(String xsltFilePath) throws Exception {
		List<String> fields = new ArrayList<String>();
		// Creating xsl DOM document
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(xsltFilePath));
		NodeList list = XPathAPI.selectNodeList(document, FIELD_TAG);
		HashSet<String> hashedFields = new HashSet<String>();
		// Populating list
		for (int i = 0; i < list.getLength(); i++) {
			NamedNodeMap attributes = list.item(i).getAttributes();
			if (attributes != null && attributes.getNamedItem(NAME_ATTRIBUTE) != null) {
				hashedFields.add(attributes.getNamedItem(NAME_ATTRIBUTE).getNodeValue());
			}
		}
		// Keeps list
		fields.addAll(hashedFields);
		XslIndexFilter.transformers.put(xsltFilePath, fields);

		return fields;
	}

}
