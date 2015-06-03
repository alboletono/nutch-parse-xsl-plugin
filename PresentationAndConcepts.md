#Presentation and concepts of the nutch parse xsl plugin

# Common Nutch crawl scenario #

Nutch is a great tool to crawl sites. Out of the box it can crawl http sites but can implement several other protocols.
Because it is transforming all inputs (PDF, DOC, TXT, etc...) in html with Apache Tika (another great Apache tool) you can manipulate html with DOM in java.

Imagine you want to crawl PDFs on your personal site. Nutch will extract all PDFs metadata and the full text content without doing anything.


# A more specific crawl scenario #

But now imagine you want to extract some specific piece of data in the content of a document. Nutch has a solution: plugins. And this is very powerful.

Basically you will create a new parse plugin (based on the HtmlParseFilter interface) to manage your own metadata. You will have to manipulate the Document as a java DOM object. You can use several things like NodeWalker to iterate through nodes. After having written two little parsers of few lines it appeared to me that it was a little bit painful to write so much lines with attributes and tag checks. So why not using XSL like several other search engines?

I know, XSL can take lots of processing, you have to know the language itself, etc... But with a generic XSL plugin you won't have to create each time a new parser and writing some boring java codes, just Xpaths. Furthermore, you will focus only on the metadata extraction by writing small Xpaths.

# How the XSL parser works #

Here is how it is integrated into the global Nutch crawling process (sorry for the poor figure):

Fetching --> HTML Parsing (Tika, HtmlParser) --> <b>XSL plugin</b> --> indexer plugins + XSL plugin (indexer part)

In the XSL plugin, you can extract data with Xpaths, XSL functions and predicates, create static metadata, etc... The only thing you have to do is to:
**create a set of rules: an XML file containing which transformation applying on a given url** create a transformer: an XSL file that describes each field to extract.

In the Rule file we will associate a regex on a url to identify an XSL file to use for transormation.
Rule file example:

```xml

<rules>
<rule matches="http://copainsdavant\.linternaute\.com/p/\w+-\w+-\d+">
<transformer file="conf/transformer_people.xsl" />


Unknown end tag for &lt;/rule&gt;




Unknown end tag for &lt;/rules&gt;


```

We will produce field tags that will be automatically added to the index.

XSL file example:

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

The interesting part is the following, the equivalent by using pure Java code with NodeWalker technique:

```java

protected static void parsePeople(Node parent, Parse parse) {


NodeWalker walker = new NodeWalker(parent);

while (walker.hasNext()) {

NodeWrapper n = new NodeWrapper(walker.nextNode());
NodeWrapper result = null;

// First name
if ((result = n.matchesTagWithAttribute("meta", "property",
"profile:first_name")) != NodeWrapper.NULL_NODE) {
String value = result.getAttributeValue("content");
addMetadata(parse, CommonMetadata.META_PEOPLE_FIRST_NAME, value);
walker.skipChildren();
} else
// Last name
if ((result = n.matchesTagWithAttribute("meta", "property",
"profile:last_name")) != NodeWrapper.NULL_NODE) {
String value = result.getAttributeValue("content");
addMetadata(parse, CommonMetadata.META_PEOPLE_LAST_NAME, value);
walker.skipChildren();
} else
// gender
if ((result = n.matchesTagWithAttribute("meta", "property",
"profile:gender")) != NodeWrapper.NULL_NODE) {
String value = result.getAttributeValue("content");
addMetadata(parse, CommonMetadata.META_PEOPLE_GENDER, value);
walker.skipChildren();
} else
// city
if ((result = n
.matchesTagWithAttribute("span", "class", "locality")) != NodeWrapper.NULL_NODE) {
String value = result.getTextContent();
addMetadata(parse, CommonMetadata.META_PEOPLE_CITY, value);
walker.skipChildren();

} else
// country
if ((result = n.matchesTagWithAttribute("span", "class",
"country-name")) != NodeWrapper.NULL_NODE) {
String value = result.getTextContent();
addMetadata(parse, CommonMetadata.META_PEOPLE_COUNTRY, value);
walker.skipChildren();
} else
// birth date
if ((result = n.matchesTagWithContent("h4", "Né le :")) != NodeWrapper.NULL_NODE) {
walker.skipChildren();

// Normally the birth date is after the specific title
NodeWrapper next = n.findNextElement(walker, "p");

if (next != NodeWrapper.NULL_NODE) {
String value = next.getTextContent();
Pattern pattern = Pattern.compile(".*(\\d{4}).*");
Matcher matcher = pattern.matcher(value);
if (matcher.matches() && matcher.groupCount() == 1) {
addMetadata(parse, CommonMetadata.META_PEOPLE_BIRTH_DATE,
matcher.group(1));
}
}
walker.skipChildren();
}
}

```

And here is an almost complete utility to manipulate NodeWalker (home made):
```java

/**
* Some utilities to get html tag/attributes using TagSoup.
*
* @author avigier
*
*/
public class NodeWrapper {

private Node node;

/** A null node when no matching resulting node */
public static NodeWrapper NULL_NODE = new NodeWrapper();

private static final Logger LOG = LoggerFactory
.getLogger(NodeWrapper.class);

private NodeWrapper() {
super();
this.node = new NodeImpl();
}

/**
*
* @param node
*            the node on which getting html information
*/
public NodeWrapper(Node node) {
this();
this.node = node;
}

/**
*
* @param tag
*            the tag of the child to get
* @param attributeName
*            the attribute name of the child to get (if specified)
* @param attributeValue
*            the attribute value of the child to get (if specified)
* @return the nodewrapper that corresponds to the query
*/
public NodeWrapper getChildWithTag(String tag, String attributeName,
String attributeValue) {
NodeWrapper result = NULL_NODE;

if (this.node.hasChildNodes()) {
for (int i = 0; i < this.node.getChildNodes().getLength(); i++) {
Node current = this.node.getChildNodes().item(i);
boolean matches = current.getNodeName().equals(tag);
// An attribute has been specified
if (attributeName != null && current.hasAttributes()) {
matches = matches
&& current.getAttributes().getNamedItem(
attributeName) != null;
// An attribute value has been specified
if (attributeValue != null && matches) {
matches = attributeValue.equals(current.getAttributes()
.getNamedItem(attributeName).getNodeValue());
}

}
if (matches) {
result = new NodeWrapper(current);
break;
}
}
}

return result;
}

/**
*
* @param walker
* @param tag
*            the tag name to find
* @return the found node if any
*/
public NodeWrapper findNextElement(NodeWalker walker, String tag) {
NodeWrapper result = null;
boolean found = false;
Node current = null;
while (walker.hasNext() && !found) {
current = walker.nextNode();
found = current.getNodeType() == Node.ELEMENT_NODE;
}

// Checking if the found element matches the expected tag
if (found && current != null && tag.equals(current.getNodeName())) {
result = new NodeWrapper(current);
} else {
result = NULL_NODE;
}

return result;
}

/**
*
* @param tag
*            the tag of the child to get.
* @return the node wrapper that corresponds to the query.
*/
public NodeWrapper getChildWithTag(String tag) {
return this.getChildWithTag(tag, null, null);
}

/**
* If it matches the node, the node itself is returned. Otherwise the fake
* null node.
*
* @param tag
* @param attributeName
* @param attributeValue
* @return If it matches the node, the node itself is returned. Otherwise
*         the fake null node.
*/
public NodeWrapper matchesTagWithAttribute(String tag,
String attributeName, String attributeValue) {
boolean matches = false;

if (tag.equals(this.node.getNodeName())) {
NamedNodeMap attributes = this.node.getAttributes();
if (attributes != null) {
Node attribute = attributes.getNamedItem(attributeName);

// If an attribute value is provided, we use it for the
// comparison.
if (attributeValue != null && attribute != null) {
matches = attributeValue.equals(attribute.getNodeValue());
}
}
}
NodeWrapper result = null;
if (matches) {
result = new NodeWrapper(this.node);
} else {
result = NULL_NODE;
}
return result;

}

/**
*
* @param tag
*            the tag to find
* @param content
*            the content to match
* @return the node itself otherwise the null node.
*/
public NodeWrapper matchesTagWithContent(String tag, String content) {

boolean matches = false;
if (this.node.getTextContent() != null) {
try {
String utf8Content = new String(this.node.getTextContent()
.getBytes(), "UTF-8");

matches = tag.equals(this.node.getNodeName())
&& content.equals(utf8Content.trim());
} catch (Exception e) {
LOG.error("Cannot convert string to utf-8: "
+ this.node.getTextContent());
}
}

NodeWrapper result = null;
if (matches) {
result = this;
} else {
result = NULL_NODE;
}
return result;
}

/**
* If it matches the node, the node itself is returned. Otherwise the fake
* null node. No check is done on the attribute value.
*
* @param tag
* @param attributeName
* @return If it matches the node, the node itself is returned. Otherwise
*         the fake null node.
*/
public NodeWrapper matchesTagWithAttribute(String tag, String attributeName) {

return this.matchesTagWithAttribute(tag, attributeName, null);
}

/**
*
* @return the current node.
*/
public Node getNode() {
return this.node;
}

/**
*
* @param name
*            the name of the attribute to get
* @return the value of the attribute if it exists, null otherwise.
*/
public String getAttributeValue(String name) {
String result = null;

if (this.node.getAttributes() != null
&& this.node.getAttributes().getNamedItem(name) != null) {
result = this.node.getAttributes().getNamedItem(name)
.getNodeValue().trim();
}

return result;
}

/**
*
* @param string
*            the string from which stripping html tags.
* @return
*/
private String stripHtmlTags(String string) {
return string.replaceAll("\\<.*?\\>", "");

}

/**
* Rewrites the Node.getTextContent to allow a strip of the tags and a trim.
* @param string
* @return the text content cleaned.
*/
public String getTextContent() {
String result = this.node.getTextContent();
if (result != null)
result = this.stripHtmlTags(result.trim());
return result;

}

}
```

As you can see injecting new types of sites will be more obvious. You won't have to code some more java, you will have to update your XML rule file and create a new XSL file.

You can use the power of java to create JUnits and test directly your work without running Nutch.