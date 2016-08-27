package com.sany.masterdata.hr.entity.userinfo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for Parameter complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Parameter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bigeinDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="endDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="high" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="low" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="operator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="otype" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Parameter", propOrder = {"bigeinDate", "endDate", "high", "low", "operator", "otype"})
public class Parameter {

    @XmlElementRef(name = "bigeinDate", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> bigeinDate;

    @XmlElementRef(name = "endDate", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> endDate;

    @XmlElementRef(name = "high", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> high;

    @XmlElementRef(name = "low", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> low;

    @XmlElementRef(name = "operator", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> operator;

    @XmlElementRef(name = "otype", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> otype;

    /**
     * Gets the value of the bigeinDate property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getBigeinDate() {
        return bigeinDate;
    }

    /**
     * Gets the value of the endDate property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getEndDate() {
        return endDate;
    }

    /**
     * Gets the value of the high property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getHigh() {
        return high;
    }

    /**
     * Gets the value of the low property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getLow() {
        return low;
    }

    /**
     * Gets the value of the operator property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getOperator() {
        return operator;
    }

    /**
     * Gets the value of the otype property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getOtype() {
        return otype;
    }

    /**
     * Sets the value of the bigeinDate property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setBigeinDate(JAXBElement<String> value) {
        this.bigeinDate = value;
    }

    /**
     * Sets the value of the endDate property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setEndDate(JAXBElement<String> value) {
        this.endDate = value;
    }

    /**
     * Sets the value of the high property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setHigh(JAXBElement<String> value) {
        this.high = value;
    }

    /**
     * Sets the value of the low property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setLow(JAXBElement<String> value) {
        this.low = value;
    }

    /**
     * Sets the value of the operator property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setOperator(JAXBElement<String> value) {
        this.operator = value;
    }

    /**
     * Sets the value of the otype property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setOtype(JAXBElement<String> value) {
        this.otype = value;
    }

}
