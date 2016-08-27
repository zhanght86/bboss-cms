package com.sany.masterdata.hr.entity.jobinfo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.sany.masterdata.hr.entity.jobinfo package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java representation for XML content.
 * The Java representation of XML content can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory methods for each of these are provided in
 * this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _JobInfoJobId_QNAME = new QName("http://service.sany.com", "job_id");

    private final static QName _JobInfoRemarks_QNAME = new QName("http://service.sany.com", "remarks");

    private final static QName _JobInfoUFlag_QNAME = new QName("http://service.sany.com", "u_flag");

    private final static QName _JobInfoJobName_QNAME = new QName("http://service.sany.com", "job_name");

    private final static QName _JobInfoOrgId_QNAME = new QName("http://service.sany.com", "org_id");

    private final static QName _ParameterOtype_QNAME = new QName("http://service.sany.com", "otype");

    private final static QName _ParameterLow_QNAME = new QName("http://service.sany.com", "low");

    private final static QName _ParameterBigeinDate_QNAME = new QName("http://service.sany.com", "bigeinDate");

    private final static QName _ParameterOperator_QNAME = new QName("http://service.sany.com", "operator");

    private final static QName _ParameterEndDate_QNAME = new QName("http://service.sany.com", "endDate");

    private final static QName _ParameterHigh_QNAME = new QName("http://service.sany.com", "high");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package:
     * com.sany.masterdata.hr.entity.jobinfo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ArrayOfJobInfo }
     * 
     */
    public ArrayOfJobInfo createArrayOfJobInfo() {
        return new ArrayOfJobInfo();
    }

    /**
     * Create an instance of {@link GetJobList }
     * 
     */
    public GetJobList createGetJobList() {
        return new GetJobList();
    }

    /**
     * Create an instance of {@link GetJobListResponse }
     * 
     */
    public GetJobListResponse createGetJobListResponse() {
        return new GetJobListResponse();
    }

    /**
     * Create an instance of {@link JobInfo }
     * 
     */
    public JobInfo createJobInfo() {
        return new JobInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "job_id", scope = JobInfo.class)
    public JAXBElement<String> createJobInfoJobId(String value) {
        return new JAXBElement<String>(_JobInfoJobId_QNAME, String.class, JobInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "job_name", scope = JobInfo.class)
    public JAXBElement<String> createJobInfoJobName(String value) {
        return new JAXBElement<String>(_JobInfoJobName_QNAME, String.class, JobInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "org_id", scope = JobInfo.class)
    public JAXBElement<String> createJobInfoOrgId(String value) {
        return new JAXBElement<String>(_JobInfoOrgId_QNAME, String.class, JobInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "remarks", scope = JobInfo.class)
    public JAXBElement<String> createJobInfoRemarks(String value) {
        return new JAXBElement<String>(_JobInfoRemarks_QNAME, String.class, JobInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "u_flag", scope = JobInfo.class)
    public JAXBElement<String> createJobInfoUFlag(String value) {
        return new JAXBElement<String>(_JobInfoUFlag_QNAME, String.class, JobInfo.class, value);
    }

    /**
     * Create an instance of {@link Parameter }
     * 
     */
    public Parameter createParameter() {
        return new Parameter();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "bigeinDate", scope = Parameter.class)
    public JAXBElement<String> createParameterBigeinDate(String value) {
        return new JAXBElement<String>(_ParameterBigeinDate_QNAME, String.class, Parameter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "endDate", scope = Parameter.class)
    public JAXBElement<String> createParameterEndDate(String value) {
        return new JAXBElement<String>(_ParameterEndDate_QNAME, String.class, Parameter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "high", scope = Parameter.class)
    public JAXBElement<String> createParameterHigh(String value) {
        return new JAXBElement<String>(_ParameterHigh_QNAME, String.class, Parameter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "low", scope = Parameter.class)
    public JAXBElement<String> createParameterLow(String value) {
        return new JAXBElement<String>(_ParameterLow_QNAME, String.class, Parameter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "operator", scope = Parameter.class)
    public JAXBElement<String> createParameterOperator(String value) {
        return new JAXBElement<String>(_ParameterOperator_QNAME, String.class, Parameter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sany.com", name = "otype", scope = Parameter.class)
    public JAXBElement<String> createParameterOtype(String value) {
        return new JAXBElement<String>(_ParameterOtype_QNAME, String.class, Parameter.class, value);
    }

}
