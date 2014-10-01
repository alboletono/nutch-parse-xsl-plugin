package org.apache.nutch.parse.xsl;

import java.io.File;
import java.util.HashMap;

import javax.xml.bind.JAXB;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.parse.xsl.xml.rule.Rules;
import org.apache.nutch.parse.xsl.xml.rule.TRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class allows to manage a set of Transformer singletons. It allows to
 * avoid having several instances of Transformers with XSL to load each time for
 * performance matter. The decision to use a given Transformer is determined by
 * a set of rules (@see Rules)
 * 
 * @see Transformer
 * @author avigier
 * 
 */
public class RulesManager {

	/** All the rules used to determine which xsl parser to use */
	protected Rules rules = null;

	/** A map containing all transformers given their file name as key */
	protected HashMap<String, Transformer> transformers;

	/** The XSLT file to use for transformation */
	public static final String CONF_XML_RULES = "parser.xsl.rulesFile";

	private static final Logger LOG = LoggerFactory.getLogger(RulesManager.class);

	private static RulesManager instance = null;

	/**
	 * Default constructor forbidden.
	 */
	private RulesManager() {
		super();
	}

	/**
	 * Instanciates an object using the apache nutch configuration (that
	 * contains the property defining the rules).
	 * 
	 * @param configuration
	 * @throws Exception
	 */
	private RulesManager(Configuration configuration) throws Exception {
		super();

		// Getting rules file
		String rulesFile = configuration.get(CONF_XML_RULES);
		if (rulesFile == null)
			throw new Exception("The rules file shall be set in your configuration file");

		// Loading rules object
		try {
			this.rules = JAXB.unmarshal(new File(rulesFile), Rules.class);
		} catch (Exception e) {
			throw new Exception("Cannot load the rules file, please check it.", e);
		}

	}

	/**
	 * @param configuration
	 *            the configuration used to create the singleton
	 * @return the singleton instance
	 * @throws Exception
	 */
	public static RulesManager getInstance(Configuration configuration) throws Exception {
		if (instance == null)
			instance = new RulesManager(configuration);
		return instance;

	}

	/**
	 * 
	 * @param url
	 *            the url to filter
	 * @return the transformer file path that suits the rules
	 * @throws Exception
	 */
	public String getTransformerFilePath(String url) throws Exception {
		String xslFile = null;

		if (url == null)
			throw new Exception("Cannot get transformer for a null url");

		// Search for a matching rule by applying defined regex
		// The first matching rule will be applied
		for (TRule rule : this.rules.getRule()) {
			if (url.matches(rule.getMatches())) {
				if (LOG.isDebugEnabled()) {
					LOG.debug(String.format("Url %s is matching regex rule %s", url, rule.getMatches()));
				}
				xslFile = rule.getTransformer().getFile();

				break;
			}
		}
		if (xslFile == null) {
			throw new Exception("No filter found for url: " + url);
		}

		return xslFile;
	}

	/**
	 * 
	 * @param url
	 *            the url to filter
	 * @return the transformer that suits the rules
	 * @throws Exception
	 */
	public Transformer getTransformer(String url) throws Exception {
		Transformer transformer = null;
		String xslFile = this.getTransformerFilePath(url);
		if (xslFile != null) {
			// Creating map if needed
			if (this.transformers == null) {
				this.transformers = new HashMap<String, Transformer>();
			}
			transformer = this.transformers.get(xslFile);
			// Getting xsl file
			if (transformer == null) {
				transformer = createTransformer(xslFile);
				this.transformers.put(xslFile, transformer);
			}

		}
		return transformer;
	}

	/**
	 * 
	 * @param url
	 *            the url to test match in rules file
	 * @return true if the url is matching a rule.
	 * @throws Exception
	 */
	public boolean matches(String url) throws Exception {
		return this.getTransformerFilePath(url) != null;
	}

	/**
	 * 
	 * @param xslFile
	 *            the path of the xsl file to load
	 * @return the transformer corresponding to the xsl file
	 * @throws Exception
	 */
	private Transformer createTransformer(String xslFile) throws Exception {
		Transformer transformer = null;
		try {
			transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(xslFile));
		} catch (Exception e) {
			throw new Exception("Cannot create transformer for xsl file " + xslFile, e);
		}
		return transformer;
	}
	
	
	/**
	 * @return the current set of rules defined in the xml file
	 */
	public Rules getRules() {
		return rules;
	}

}
