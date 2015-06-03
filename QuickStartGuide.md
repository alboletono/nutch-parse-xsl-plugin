#A quick start guide to install and use the parse xsl plugin.

# Quick start guide #

As an example we will crawl a well known french site for friend searching: Copainsdavant.

In order to configure quickly a crawler I'm using for copainsdavant web site:

```bash

mkdir -p ~/crawlers
cd 	~/crawlers
wget http://mirrors.ukfast.co.uk/sites/ftp.apache.org/nutch/1.9/apache-nutch-1.9-bin.tar.gz
gzip -d apache-nutch-1.9-bin.tar.gz
tar -xvf apache-nutch-1.9-bin.tar.gz
cd apache-nutch-1.9
mkdir -p urls
cd urls
touch seed.txt
```

Edit seed.txt and add the line:

```text

http://copainsdavant.linternaute.com/glossary/users/a
```


```bash

cd apache-nutch-1.9
vi conf/nutch-site.xml
```

Edit conf/nutch-site.xml and add the lines:

```xml


<property>

<name>http.agent.name

Unknown end tag for &lt;/name&gt;



<value>CopainsDavant Spider

Unknown end tag for &lt;/value&gt;





Unknown end tag for &lt;/property&gt;



<property>

<name>parser.xsl.rulesFile

Unknown end tag for &lt;/name&gt;



<value>conf/parse-xsl-rules.xml

Unknown end tag for &lt;/value&gt;



<description>The XML file that contains rules to use

Unknown end tag for &lt;/description&gt;





Unknown end tag for &lt;/property&gt;



<!-- We activate the xsl parser (to get metadata from xsl) -->
<property>

<name>plugin.includes

Unknown end tag for &lt;/name&gt;



<value>protocol-http|urlfilter-regex|parse-(html|tika)|parse-xsl|index-(basic|anchor|metadata)|indexer-solr|scoring-opic|urlnormalizer-(pass|regex|basic)

Unknown end tag for &lt;/value&gt;



<description>Regular expression naming plugin directory names to
include.  Any plugin not matching this expression is excluded.
In any case you need at least include the nutch-extensionpoints plugin. By
default Nutch includes crawling just HTML and plain text via HTTP,
and basic indexing and search plugins. In order to use HTTPS please enable
protocol-httpclient, but be aware of possible intermittent problems with the
underlying commons-httpclient library.



Unknown end tag for &lt;/description&gt;





Unknown end tag for &lt;/property&gt;



```

Copy the parser-xsl plugin and its associated plugin.xml to [NUTCH](NUTCH.md)/plugins/parse-xsl.
Download them on: https://drive.google.com/folderview?id=0B2rj-LWOgZmOZWc2MDBnemlNVFE&usp=sharing

Copy the rule file and the associated xsl file(s) (could be several depending of your rules) to your [NUTCH](NUTCH.md)/conf directory.

Rule file example (conf/parse-xsl-rules.xml):

```xml

<rules>

<rule matches="http://copainsdavant\.linternaute\.com/p/\w+-\w+-\d+">

<transformer file="conf/transformer_people.xsl" />


Unknown end tag for &lt;/rule&gt;





Unknown end tag for &lt;/rules&gt;



```

XSL file example (conf/transformer\_people.xsl):

```xml

<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


<xsl:template match="/">

<documents>

<document>

<field name="lastName">

<xsl:value-of select="//META[@property='profile:last_name']/@content" />



Unknown end tag for &lt;/field&gt;



<field name="firstName">

<xsl:value-of select="//META[@property='profile:first_name']/@content" />



Unknown end tag for &lt;/field&gt;



<field name="gender">

<xsl:value-of select="//META[@property='profile:gender']/@content" />



Unknown end tag for &lt;/field&gt;



<field name="city">

<xsl:value-of select="//SPAN[@class='locality']" />



Unknown end tag for &lt;/field&gt;



<field name="country">

<xsl:value-of select="//SPAN[@class='country-name']" />



Unknown end tag for &lt;/field&gt;



<field name="birthDate">

<xsl:variable name="extractedValue" select="//H4[text()='Né le :']/../P" />

<xsl:value-of select="normalize-space(substring-after(substring-before($extractedValue, '('), '.'))" />



Unknown end tag for &lt;/field&gt;





Unknown end tag for &lt;/document&gt;





Unknown end tag for &lt;/documents&gt;





Unknown end tag for &lt;/template&gt;





Unknown end tag for &lt;/stylesheet&gt;


```

Copy the conf/schema-solr4.xml from nutch to your solr server conf (usually example/solr/collection1/conf/schema.xml

Do not forget to add the following line in Solr file:

```xml

<field name="_version_" type="long" indexed="true" stored="true"/>
```

Also, add your custom fields, for example:

```xml

<field name="lastName" type="string" stored="true" indexed="true"/>
<field name="firstName" type="string" stored="true" indexed="true"/>
<field name="gender" type="string" stored="true" indexed="true"/>
<field name="city" type="string" stored="true" indexed="true"/>
<field name="country" type="string" stored="true" indexed="true"/>
<field name="birthDate" type="string" stored="true" indexed="true"/>
```


```bash

cd 	~/crawlers/apache-nutch-1.9
```

Edit bin/nutch and add the line (fit it to your jdk):

```text

NUTCH_JAVA_HOME=/usr/lib/jvm/java-6-openjdk-amd64
```

If not exists (fit it to your jdk):

```bash

sudo apt-get install java-6-openjdk-jdk
```

Then run a test crawl:
```bash

bin/crawl urls crawl http://localhost/solr 500
```
For debug purpose, to avoid crawling lots of urls i'm just setting the sizeFetchList that is hardcoded into the crawl script to 50 instead of 5000.