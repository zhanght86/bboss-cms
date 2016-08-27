package com.sany.masterdata.hr.entity.jobinfo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for JobInfo complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="JobInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="job_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="job_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="org_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="remarks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="u_flag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JobInfo", propOrder = {"jobId", "jobName", "orgId", "remarks", "uFlag"})
public class JobInfo {

    @XmlElementRef(name = "job_id", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> jobId;

    @XmlElementRef(name = "job_name", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> jobName;

    @XmlElementRef(name = "org_id", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> orgId;

    @XmlElementRef(name = "remarks", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> remarks;

    @XmlElementRef(name = "u_flag", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> uFlag;

    /**
     * Gets the value of the jobId property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getJobId() {
        return jobId;
    }

    /**
     * Gets the value of the jobName property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getJobName() {
        return jobName;
    }

    /**
     * Gets the value of the orgId property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getOrgId() {
        return orgId;
    }

    /**
     * Gets the value of the remarks property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getRemarks() {
        return remarks;
    }

    /**
     * Gets the value of the uFlag property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getUFlag() {
        return uFlag;
    }

    /**
     * Sets the value of the jobId property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setJobId(JAXBElement<String> value) {
        this.jobId = value;
    }

    /**
     * Sets the value of the jobName property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setJobName(JAXBElement<String> value) {
        this.jobName = value;
    }

    /**
     * Sets the value of the orgId property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setOrgId(JAXBElement<String> value) {
        this.orgId = value;
    }

    /**
     * Sets the value of the remarks property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setRemarks(JAXBElement<String> value) {
        this.remarks = value;
    }

    /**
     * Sets the value of the uFlag property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setUFlag(JAXBElement<String> value) {
        this.uFlag = value;
    }

}
