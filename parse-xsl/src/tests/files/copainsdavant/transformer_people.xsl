<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


	<xsl:template match="/">
		<documents>
			<document>
				<field name="lastName">
					<xsl:value-of select="//META[@property='profile:last_name']/@content" />
				</field>

				<field name="firstName">
					<xsl:value-of select="//META[@property='profile:first_name']/@content" />
				</field>

				<field name="gender">
					<xsl:value-of select="//META[@property='profile:gender']/@content" />
				</field>

				<field name="city">
					<xsl:value-of select="//SPAN[@class='locality']" />
				</field>

				<field name="country">
					<xsl:value-of select="//SPAN[@class='country-name']" />
				</field>

				<field name="birthDate">
					<xsl:variable name="extractedValue" select="//H4[text()='Né le :']/../P" />
					<xsl:value-of select="normalize-space(substring-after(substring-before($extractedValue, '('), '.'))" />
				</field>



			</document>
		</documents>
	</xsl:template>

</xsl:stylesheet>