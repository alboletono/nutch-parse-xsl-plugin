//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.7 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2014.10.01 à 09:02:07 PM CEST 
//


package org.apache.nutch.parse.xsl.xml.rule;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour anonymous complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element name="rule" type="{http://www.example.org/rules/}TRule"/>
 *       &lt;/sequence>
 *       &lt;attribute name="filterUrlsWithNoRule" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "rule"
})
@XmlRootElement(name = "rules")
public class Rules {

    @XmlElement(required = true)
    protected List<TRule> rule;
    @XmlAttribute(name = "filterUrlsWithNoRule")
    protected Boolean filterUrlsWithNoRule;

    /**
     * Gets the value of the rule property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rule property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRule().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TRule }
     * 
     * 
     */
    public List<TRule> getRule() {
        if (rule == null) {
            rule = new ArrayList<TRule>();
        }
        return this.rule;
    }

    /**
     * Obtient la valeur de la propriété filterUrlsWithNoRule.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isFilterUrlsWithNoRule() {
        if (filterUrlsWithNoRule == null) {
            return true;
        } else {
            return filterUrlsWithNoRule;
        }
    }

    /**
     * Définit la valeur de la propriété filterUrlsWithNoRule.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFilterUrlsWithNoRule(Boolean value) {
        this.filterUrlsWithNoRule = value;
    }

}
