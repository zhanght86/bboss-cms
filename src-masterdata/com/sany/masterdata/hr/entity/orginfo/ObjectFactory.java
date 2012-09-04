
package com.sany.masterdata.hr.entity.orginfo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.sany.masterdata.hr.entity.orginfo package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _OrgInfoOrgName_QNAME = new QName("http://service.sany.com", "orgName");
    private final static QName _OrgInfoOrgLevel_QNAME = new QName("http://service.sany.com", "orgLevel");
    private final static QName _OrgInfoUFlag_QNAME = new QName("http://service.sany.com", "u_flag");
    private final static QName _OrgInfoOrgNumber_QNAME = new QName("http://service.sany.com", "orgNumber");
    private final static QName _OrgInfoOrgId_QNAME = new QName("http://service.sany.com", "orgId");
    private final static QName _OrgInfoParentId_QNAME = new QName("http://service.sany.com", "parentId");
    private final static QName _OrgInfoEnName_QNAME = new QName("http://service.sany.com", "enName");
    private final static QName _ParameterOtype_QNAME = new QName("http://service.sany.com", "otype");
    private final static QName _ParameterLow_QNAME = new QName("http://service.sany.com", "low");
    private final static QName _ParameterBigeinDate_QNAME = new QName("http://service.sany.com", "bigeinDate");
    private final static QName _ParameterOperator_QNAME = new QName("http://service.sany.com", "operator");
    private final static QName _ParameterEndDate_QNAME = new QName("http://service.sany.com", "endDate");
    private final static QName _ParameterHigh_QNAME = new QName("http://service.sany.com", "high");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.sany.masterdata.hr.entity.orginfo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetOrgListResponse }
     * 
     */
    public GetOrgListResponse createGetOrgListResponse() {
        return new GetOrgListResponse();
    }

    /**
     * Create an instance of {@link ArrayOfOrgInfo }
     * 
     */
    public ArrayOfOrgInfo createArrayOfOrgInfo() {
        return new ArrayOfOrgInfo();
    }

    /**
     * Create an instance of {@link GetOrgList }
     * 
     */
    public GetOrgList createGetOrgList() {
        return new GetOrgList();
    }

    /**
     * Create an instance of {@link Parameter }
     * 
     */
    public Parameter createParameter() {
        return new Parameter();
    }

    /**
     * Create an instance of {@link OrgInfo }
     * 
     */
    public OrgInfo createOrgInfo() {
        return new OrgInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "orgName", scope = OrgInfo.class)
    public JAXBElement<String> createOrgInfoOrgName(String value) {
        return new JAXBElement<String>(_OrgInfoOrgName_QNAME, String.class, OrgInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "orgLevel", scope = OrgInfo.class)
    public JAXBElement<String> createOrgInfoOrgLevel(String value) {
        return new JAXBElement<String>(_OrgInfoOrgLevel_QNAME, String.class, OrgInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "u_flag", scope = OrgInfo.class)
    public JAXBElement<String> createOrgInfoUFlag(String value) {
        return new JAXBElement<String>(_OrgInfoUFlag_QNAME, String.class, OrgInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "orgNumber", scope = OrgInfo.class)
    public JAXBElement<String> createOrgInfoOrgNumber(String value) {
        return new JAXBElement<String>(_OrgInfoOrgNumber_QNAME, String.class, OrgInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "orgId", scope = OrgInfo.class)
    public JAXBElement<String> createOrgInfoOrgId(String value) {
        return new JAXBElement<String>(_OrgInfoOrgId_QNAME, String.class, OrgInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "parentId", scope = OrgInfo.class)
    public JAXBElement<String> createOrgInfoParentId(String value) {
        return new JAXBElement<String>(_OrgInfoParentId_QNAME, String.class, OrgInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "enName", scope = OrgInfo.class)
    public JAXBElement<String> createOrgInfoEnName(String value) {
        return new JAXBElement<String>(_OrgInfoEnName_QNAME, String.class, OrgInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "otype", scope = Parameter.class)
    public JAXBElement<String> createParameterOtype(String value) {
        return new JAXBElement<String>(_ParameterOtype_QNAME, String.class, Parameter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "low", scope = Parameter.class)
    public JAXBElement<String> createParameterLow(String value) {
        return new JAXBElement<String>(_ParameterLow_QNAME, String.class, Parameter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "bigeinDate", scope = Parameter.class)
    public JAXBElement<String> createParameterBigeinDate(String value) {
        return new JAXBElement<String>(_ParameterBigeinDate_QNAME, String.class, Parameter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "operator", scope = Parameter.class)
    public JAXBElement<String> createParameterOperator(String value) {
        return new JAXBElement<String>(_ParameterOperator_QNAME, String.class, Parameter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "endDate", scope = Parameter.class)
    public JAXBElement<String> createParameterEndDate(String value) {
        return new JAXBElement<String>(_ParameterEndDate_QNAME, String.class, Parameter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "high", scope = Parameter.class)
    public JAXBElement<String> createParameterHigh(String value) {
        return new JAXBElement<String>(_ParameterHigh_QNAME, String.class, Parameter.class, value);
    }

}
