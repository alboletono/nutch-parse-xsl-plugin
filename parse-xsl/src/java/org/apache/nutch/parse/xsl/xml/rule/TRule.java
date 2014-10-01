//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.7 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2014.10.01 à 09:02:07 PM CEST 
//


package org.apache.nutch.parse.xsl.xml.rule;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour TRule complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="TRule">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="transformer" type="{http://www.example.org/rules/}TTransformer"/>
 *       &lt;/sequence>
 *       &lt;attribute name="matches" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TRule", propOrder = {
    "transformer"
})
public class TRule {

    @XmlElement(required = true)
    protected TTransformer transformer;
    @XmlAttribute(name = "matches", required = true)
    protected String matches;

    /**
     * Obtient la valeur de la propriété transformer.
     * 
     * @return
     *     possible object is
     *     {@link TTransformer }
     *     
     */
    public TTransformer getTransformer() {
        return transformer;
    }

    /**
     * Définit la valeur de la propriété transformer.
     * 
     * @param value
     *     allowed object is
     *     {@link TTransformer }
     *     
     */
    public void setTransformer(TTransformer value) {
        this.transformer = value;
    }

    /**
     * Obtient la valeur de la propriété matches.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatches() {
        return matches;
    }

    /**
     * Définit la valeur de la propriété matches.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatches(String value) {
        this.matches = value;
    }

}
