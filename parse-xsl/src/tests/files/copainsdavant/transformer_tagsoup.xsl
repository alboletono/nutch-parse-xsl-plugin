<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


	<xsl:template match="/">
		<documents>
			<document>
				<field name="lastName">
					<xsl:value-of select="//meta[@property='profile:last_name']/@content" />
				</field>

				<field name="firstName">
					<xsl:value-of select="//meta[@property='profile:first_name']/@content" />
				</field>

				<field name="gender">
					<xsl:value-of select="//meta[@property='profile:gender']/@content" />
				</field>

				<field name="city">
					<xsl:value-of select="//span[@class='locality']" />
				</field>

				<field name="country">
					<xsl:value-of select="//span[@class='country-name']" />
				</field>

				<field name="birthDate">
					<xsl:value-of select="//h4[text()='Né le :']/../P" />
					<!-- Need to upgrade to XSLT 2.0 <xsl:analyze-string select="//H4[text()='Né 
						le :']/../P" regex="\d{4}"> <xsl:matching-substring> <xsl:value-of select="regex-group(0)" 
						/> </xsl:matching-substring> </xsl:analyze-string> -->
				</field>



			</document>
		</documents>
	</xsl:template>

</xsl:stylesheet>