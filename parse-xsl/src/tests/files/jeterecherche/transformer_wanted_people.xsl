<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


	<xsl:template match="/">
		<documents>
			<document>
				<field name="wantedPeople">
					<xsl:value-of select="normalize-space(//TABLE[@class='avis_de_recherche_fiche_membre_tableau_titre']//TR/TD/SPAN/text())" />
				</field>

				<field name="searcherPeople">
					<xsl:value-of select="normalize-space(//TABLE[@class='avis_de_recherche_fiche_membre_tableau_titre']//TR[2]/TD/STRONG/SPAN/A//text())" /> 
				</field>

				<field name="description">
					<xsl:value-of select="normalize-space(//SPAN[@class='avis_de_recherche_desceiptif_typo_bleu']/text())" />
				</field>

				
			</document>
		</documents>
	</xsl:template>

</xsl:stylesheet>