package com.sany.masterdata.hr.entity.userinfo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for UserInfo complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="abkrs" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="btrtl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dat01" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dat02" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="datum" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="famst" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gbdat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gbdep" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gblnd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gbort" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gesch" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="icnum" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="insti" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="locat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nachn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="natio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orgeh" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orgeh4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orgeh5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orgeh6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orgeh_1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orgeh_2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orgeh_3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pcode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="persg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="persk" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="plans" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="posnc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="possc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="posty" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="racky" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="slart" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="user_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="usrid4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="usrid_3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="usrid_5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vorna" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="werks" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zbmcj" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zdate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zgclb" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zlpos" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zposty" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zsxzy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ztdetail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zxxxx" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserInfo", propOrder = {"abkrs", "btrtl", "dat01", "dat02", "datum", "famst", "gbdat", "gbdep",
        "gblnd", "gbort", "gesch", "icnum", "insti", "locat", "nachn", "natio", "orgeh", "orgeh4", "orgeh5", "orgeh6",
        "orgeh1", "orgeh2", "orgeh3", "pcode", "persg", "persk", "plans", "posnc", "possc", "posty", "racky", "slart",
        "userId", "usrid4", "usrid3", "usrid5", "vorna", "werks", "zbmcj", "zdate", "zgclb", "zlpos", "zposty",
        "zsxzy", "ztdetail", "zxxxx"})
public class UserInfo {

    @XmlElementRef(name = "abkrs", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> abkrs;

    @XmlElementRef(name = "btrtl", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> btrtl;

    @XmlElementRef(name = "dat01", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> dat01;

    @XmlElementRef(name = "dat02", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> dat02;

    @XmlElementRef(name = "datum", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> datum;

    @XmlElementRef(name = "famst", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> famst;

    @XmlElementRef(name = "gbdat", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> gbdat;

    @XmlElementRef(name = "gbdep", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> gbdep;

    @XmlElementRef(name = "gblnd", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> gblnd;

    @XmlElementRef(name = "gbort", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> gbort;

    @XmlElementRef(name = "gesch", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> gesch;

    @XmlElementRef(name = "icnum", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> icnum;

    @XmlElementRef(name = "insti", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> insti;

    @XmlElementRef(name = "locat", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> locat;

    @XmlElementRef(name = "nachn", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> nachn;

    @XmlElementRef(name = "natio", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> natio;

    @XmlElementRef(name = "orgeh", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> orgeh;

    @XmlElementRef(name = "orgeh4", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> orgeh4;

    @XmlElementRef(name = "orgeh5", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> orgeh5;

    @XmlElementRef(name = "orgeh6", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> orgeh6;

    @XmlElementRef(name = "orgeh_1", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> orgeh1;

    @XmlElementRef(name = "orgeh_2", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> orgeh2;

    @XmlElementRef(name = "orgeh_3", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> orgeh3;

    @XmlElementRef(name = "pcode", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> pcode;

    @XmlElementRef(name = "persg", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> persg;

    @XmlElementRef(name = "persk", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> persk;

    @XmlElementRef(name = "plans", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> plans;

    @XmlElementRef(name = "posnc", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> posnc;

    @XmlElementRef(name = "possc", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> possc;

    @XmlElementRef(name = "posty", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> posty;

    @XmlElementRef(name = "racky", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> racky;

    @XmlElementRef(name = "slart", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> slart;

    @XmlElementRef(name = "user_id", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> userId;

    @XmlElementRef(name = "usrid4", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> usrid4;

    @XmlElementRef(name = "usrid_3", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> usrid3;

    @XmlElementRef(name = "usrid_5", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> usrid5;

    @XmlElementRef(name = "vorna", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> vorna;

    @XmlElementRef(name = "werks", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> werks;

    @XmlElementRef(name = "zbmcj", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> zbmcj;

    @XmlElementRef(name = "zdate", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> zdate;

    @XmlElementRef(name = "zgclb", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> zgclb;

    @XmlElementRef(name = "zlpos", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> zlpos;

    @XmlElementRef(name = "zposty", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> zposty;

    @XmlElementRef(name = "zsxzy", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> zsxzy;

    @XmlElementRef(name = "ztdetail", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> ztdetail;

    @XmlElementRef(name = "zxxxx", namespace = "http://service.sany.com", type = JAXBElement.class)
    protected JAXBElement<String> zxxxx;

    /**
     * Gets the value of the abkrs property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getAbkrs() {
        return abkrs;
    }

    /**
     * Gets the value of the btrtl property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getBtrtl() {
        return btrtl;
    }

    /**
     * Gets the value of the dat01 property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getDat01() {
        return dat01;
    }

    /**
     * Gets the value of the dat02 property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getDat02() {
        return dat02;
    }

    /**
     * Gets the value of the datum property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getDatum() {
        return datum;
    }

    /**
     * Gets the value of the famst property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getFamst() {
        return famst;
    }

    /**
     * Gets the value of the gbdat property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getGbdat() {
        return gbdat;
    }

    /**
     * Gets the value of the gbdep property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getGbdep() {
        return gbdep;
    }

    /**
     * Gets the value of the gblnd property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getGblnd() {
        return gblnd;
    }

    /**
     * Gets the value of the gbort property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getGbort() {
        return gbort;
    }

    /**
     * Gets the value of the gesch property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getGesch() {
        return gesch;
    }

    /**
     * Gets the value of the icnum property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getIcnum() {
        return icnum;
    }

    /**
     * Gets the value of the insti property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getInsti() {
        return insti;
    }

    /**
     * Gets the value of the locat property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getLocat() {
        return locat;
    }

    /**
     * Gets the value of the nachn property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getNachn() {
        return nachn;
    }

    /**
     * Gets the value of the natio property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getNatio() {
        return natio;
    }

    /**
     * Gets the value of the orgeh property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getOrgeh() {
        return orgeh;
    }

    /**
     * Gets the value of the orgeh1 property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getOrgeh1() {
        return orgeh1;
    }

    /**
     * Gets the value of the orgeh2 property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getOrgeh2() {
        return orgeh2;
    }

    /**
     * Gets the value of the orgeh3 property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getOrgeh3() {
        return orgeh3;
    }

    /**
     * Gets the value of the orgeh4 property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getOrgeh4() {
        return orgeh4;
    }

    /**
     * Gets the value of the orgeh5 property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getOrgeh5() {
        return orgeh5;
    }

    /**
     * Gets the value of the orgeh6 property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getOrgeh6() {
        return orgeh6;
    }

    /**
     * Gets the value of the pcode property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getPcode() {
        return pcode;
    }

    /**
     * Gets the value of the persg property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getPersg() {
        return persg;
    }

    /**
     * Gets the value of the persk property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getPersk() {
        return persk;
    }

    /**
     * Gets the value of the plans property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getPlans() {
        return plans;
    }

    /**
     * Gets the value of the posnc property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getPosnc() {
        return posnc;
    }

    /**
     * Gets the value of the possc property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getPossc() {
        return possc;
    }

    /**
     * Gets the value of the posty property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getPosty() {
        return posty;
    }

    /**
     * Gets the value of the racky property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getRacky() {
        return racky;
    }

    /**
     * Gets the value of the slart property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getSlart() {
        return slart;
    }

    /**
     * Gets the value of the userId property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getUserId() {
        return userId;
    }

    /**
     * Gets the value of the usrid3 property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getUsrid3() {
        return usrid3;
    }

    /**
     * Gets the value of the usrid4 property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getUsrid4() {
        return usrid4;
    }

    /**
     * Gets the value of the usrid5 property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getUsrid5() {
        return usrid5;
    }

    /**
     * Gets the value of the vorna property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getVorna() {
        return vorna;
    }

    /**
     * Gets the value of the werks property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getWerks() {
        return werks;
    }

    /**
     * Gets the value of the zbmcj property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getZbmcj() {
        return zbmcj;
    }

    /**
     * Gets the value of the zdate property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getZdate() {
        return zdate;
    }

    /**
     * Gets the value of the zgclb property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getZgclb() {
        return zgclb;
    }

    /**
     * Gets the value of the zlpos property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getZlpos() {
        return zlpos;
    }

    /**
     * Gets the value of the zposty property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getZposty() {
        return zposty;
    }

    /**
     * Gets the value of the zsxzy property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getZsxzy() {
        return zsxzy;
    }

    /**
     * Gets the value of the ztdetail property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getZtdetail() {
        return ztdetail;
    }

    /**
     * Gets the value of the zxxxx property.
     * 
     * @return
     *         possible object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public JAXBElement<String> getZxxxx() {
        return zxxxx;
    }

    /**
     * Sets the value of the abkrs property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setAbkrs(JAXBElement<String> value) {
        this.abkrs = value;
    }

    /**
     * Sets the value of the btrtl property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setBtrtl(JAXBElement<String> value) {
        this.btrtl = value;
    }

    /**
     * Sets the value of the dat01 property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setDat01(JAXBElement<String> value) {
        this.dat01 = value;
    }

    /**
     * Sets the value of the dat02 property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setDat02(JAXBElement<String> value) {
        this.dat02 = value;
    }

    /**
     * Sets the value of the datum property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setDatum(JAXBElement<String> value) {
        this.datum = value;
    }

    /**
     * Sets the value of the famst property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setFamst(JAXBElement<String> value) {
        this.famst = value;
    }

    /**
     * Sets the value of the gbdat property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setGbdat(JAXBElement<String> value) {
        this.gbdat = value;
    }

    /**
     * Sets the value of the gbdep property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setGbdep(JAXBElement<String> value) {
        this.gbdep = value;
    }

    /**
     * Sets the value of the gblnd property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setGblnd(JAXBElement<String> value) {
        this.gblnd = value;
    }

    /**
     * Sets the value of the gbort property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setGbort(JAXBElement<String> value) {
        this.gbort = value;
    }

    /**
     * Sets the value of the gesch property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setGesch(JAXBElement<String> value) {
        this.gesch = value;
    }

    /**
     * Sets the value of the icnum property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setIcnum(JAXBElement<String> value) {
        this.icnum = value;
    }

    /**
     * Sets the value of the insti property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setInsti(JAXBElement<String> value) {
        this.insti = value;
    }

    /**
     * Sets the value of the locat property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setLocat(JAXBElement<String> value) {
        this.locat = value;
    }

    /**
     * Sets the value of the nachn property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setNachn(JAXBElement<String> value) {
        this.nachn = value;
    }

    /**
     * Sets the value of the natio property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setNatio(JAXBElement<String> value) {
        this.natio = value;
    }

    /**
     * Sets the value of the orgeh property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setOrgeh(JAXBElement<String> value) {
        this.orgeh = value;
    }

    /**
     * Sets the value of the orgeh1 property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setOrgeh1(JAXBElement<String> value) {
        this.orgeh1 = value;
    }

    /**
     * Sets the value of the orgeh2 property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setOrgeh2(JAXBElement<String> value) {
        this.orgeh2 = value;
    }

    /**
     * Sets the value of the orgeh3 property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setOrgeh3(JAXBElement<String> value) {
        this.orgeh3 = value;
    }

    /**
     * Sets the value of the orgeh4 property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setOrgeh4(JAXBElement<String> value) {
        this.orgeh4 = value;
    }

    /**
     * Sets the value of the orgeh5 property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setOrgeh5(JAXBElement<String> value) {
        this.orgeh5 = value;
    }

    /**
     * Sets the value of the orgeh6 property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setOrgeh6(JAXBElement<String> value) {
        this.orgeh6 = value;
    }

    /**
     * Sets the value of the pcode property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setPcode(JAXBElement<String> value) {
        this.pcode = value;
    }

    /**
     * Sets the value of the persg property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setPersg(JAXBElement<String> value) {
        this.persg = value;
    }

    /**
     * Sets the value of the persk property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setPersk(JAXBElement<String> value) {
        this.persk = value;
    }

    /**
     * Sets the value of the plans property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setPlans(JAXBElement<String> value) {
        this.plans = value;
    }

    /**
     * Sets the value of the posnc property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setPosnc(JAXBElement<String> value) {
        this.posnc = value;
    }

    /**
     * Sets the value of the possc property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setPossc(JAXBElement<String> value) {
        this.possc = value;
    }

    /**
     * Sets the value of the posty property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setPosty(JAXBElement<String> value) {
        this.posty = value;
    }

    /**
     * Sets the value of the racky property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setRacky(JAXBElement<String> value) {
        this.racky = value;
    }

    /**
     * Sets the value of the slart property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setSlart(JAXBElement<String> value) {
        this.slart = value;
    }

    /**
     * Sets the value of the userId property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setUserId(JAXBElement<String> value) {
        this.userId = value;
    }

    /**
     * Sets the value of the usrid3 property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setUsrid3(JAXBElement<String> value) {
        this.usrid3 = value;
    }

    /**
     * Sets the value of the usrid4 property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setUsrid4(JAXBElement<String> value) {
        this.usrid4 = value;
    }

    /**
     * Sets the value of the usrid5 property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setUsrid5(JAXBElement<String> value) {
        this.usrid5 = value;
    }

    /**
     * Sets the value of the vorna property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setVorna(JAXBElement<String> value) {
        this.vorna = value;
    }

    /**
     * Sets the value of the werks property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setWerks(JAXBElement<String> value) {
        this.werks = value;
    }

    /**
     * Sets the value of the zbmcj property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setZbmcj(JAXBElement<String> value) {
        this.zbmcj = value;
    }

    /**
     * Sets the value of the zdate property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setZdate(JAXBElement<String> value) {
        this.zdate = value;
    }

    /**
     * Sets the value of the zgclb property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setZgclb(JAXBElement<String> value) {
        this.zgclb = value;
    }

    /**
     * Sets the value of the zlpos property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setZlpos(JAXBElement<String> value) {
        this.zlpos = value;
    }

    /**
     * Sets the value of the zposty property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setZposty(JAXBElement<String> value) {
        this.zposty = value;
    }

    /**
     * Sets the value of the zsxzy property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setZsxzy(JAXBElement<String> value) {
        this.zsxzy = value;
    }

    /**
     * Sets the value of the ztdetail property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setZtdetail(JAXBElement<String> value) {
        this.ztdetail = value;
    }

    /**
     * Sets the value of the zxxxx property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    public void setZxxxx(JAXBElement<String> value) {
        this.zxxxx = value;
    }

}
