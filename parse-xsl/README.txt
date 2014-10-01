The aim of this plugin is to use XSLT to extract metadata from HTML DOM structures.

| Your Data | --> | Parse-html plugin | --> | DOM structure | --> |XSLT plugin |
                  | or TIKA plugin    |
                  
                  
The main advantage is that:
- You won't have to produce any java code, only XSLT and configuration
- It can process DOM structure from DocumentFragment (@see NekoHtml and @see TagSoup)
- It is HtmlParseFilter plugin compatible and can be plugged as any other plugin (parse-js, parse-swf, etc...)


= Deployment of the parse-xsl plugin =

- copy the parse-xsl.jar and its plugin.xml to the new folder [NUTCH]/plugins/parse-xsl
- copy your rules file (can be named whatever you want) and associated xsl used for metadata fetching to the [NUTCH]/conf
- edit the [NUTCH]/conf/nutch-default.xml and add the following property to point on your rules file

    <property>
		<name>parser.xsl.rulesFile</name>
		<value>src/plugin/parse-xsl/src/tests/files/copainsdavant/rules.xml
		</value>
		<description>The XML file that contains rules to use
		</description>
	</property>
- In the same file enable the use of the parse-xsl by updating plugin.includes property (should appear after parse-(html|tika) string