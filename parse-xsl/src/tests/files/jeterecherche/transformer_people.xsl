<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


	<xsl:template match="/">
		<documents>
			<document>
				<xsl:variable name="extractFirstName" select="//DIV[@id='TabbedPanels1']/UL/LI/SPAN/text()" />
				<xsl:variable name="firstName" select="normalize-space(substring-after($extractFirstName, 'sur'))" />
				<field name="firstName">
					<xsl:value-of select="$firstName" />
				</field>

				<field name="lastName">
					<xsl:variable name="fullName" select="//STRONG[@class='index_centre_tableau_titre_orange']/text()" />
					<xsl:value-of select="normalize-space(substring-after($fullName, $firstName))" />
				</field>

				<field name="gender">
					<xsl:value-of select="//TD[@class='index_centre_tableau_10_typo_descriptif' and text() = 'Sexe :']/../TD[2]/text()" />
				</field>

				<field name="city">
					<xsl:value-of select="//TD[@class='index_centre_tableau_10_typo_descriptif' and text() = 'Ville :']/../TD[2]/text()" />
				</field>

				<field name="country">
					<xsl:value-of select="//SPAN[@class='country-name']" />
				</field>

				<!-- No birth date -->
				<field name="birthDate">
					
				</field>
				
				<field name="age">
					<xsl:variable name="extract" select="//TD[@class='index_centre_tableau_10_typo_descriptif' and text() = 'Age :']/../TD[2]/text()"></xsl:variable>
					<xsl:value-of select="normalize-space(substring-before($extract, 'ans'))" />
				</field>


			</document>
		</documents>
	</xsl:template>

</xsl:stylesheet>