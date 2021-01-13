
package com.jiuqi.dna.gams.jy03.printing.cardwebservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>assetCard complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="assetCard"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="billcode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="billdefineid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cardCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="cunfdd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="jiaz" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="recid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="shiybm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="shiyr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="zicmc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "assetCard", propOrder = {
    "billcode",
    "billdefineid",
    "cardCount",
    "cunfdd",
    "jiaz",
    "recid",
    "shiybm",
    "shiyr",
    "zicmc",
    "querq",
    "shul",
    "hangydl"
})
public class AssetCard {

    protected String billcode;
    protected String billdefineid;
    protected Integer cardCount;
    protected String cunfdd;
    protected String jiaz;
    protected String recid;
    protected String shiybm;
    protected String shiyr;
    protected String zicmc;
    protected String querq;
    protected Integer shul;
    protected String hangydl ;

    /**
     * ��ȡbillcode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillcode() {
        return billcode;
    }

    /**
     * ����billcode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillcode(String value) {
        this.billcode = value;
    }

    /**
     * ��ȡbilldefineid���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBilldefineid() {
        return billdefineid;
    }

    /**
     * ����billdefineid���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBilldefineid(String value) {
        this.billdefineid = value;
    }

    /**
     * ��ȡcardCount���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCardCount() {
        return cardCount;
    }

    /**
     * ����cardCount���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCardCount(Integer value) {
        this.cardCount = value;
    }

    /**
     * ��ȡcunfdd���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCunfdd() {
        return cunfdd;
    }

    /**
     * ����cunfdd���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCunfdd(String value) {
        this.cunfdd = value;
    }

    /**
     * ��ȡjiaz���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJiaz() {
        return jiaz;
    }

    /**
     * ����jiaz���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJiaz(String value) {
        this.jiaz = value;
    }

    /**
     * ��ȡrecid���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecid() {
        return recid;
    }

    /**
     * ����recid���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecid(String value) {
        this.recid = value;
    }

    /**
     * ��ȡshiybm���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShiybm() {
        return shiybm;
    }

    /**
     * ����shiybm���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShiybm(String value) {
        this.shiybm = value;
    }

    /**
     * ��ȡshiyr���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShiyr() {
        return shiyr;
    }

    /**
     * ����shiyr���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShiyr(String value) {
        this.shiyr = value;
    }

    /**
     * ��ȡzicmc���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZicmc() {
        return zicmc;
    }

    /**
     * ����zicmc���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZicmc(String value) {
        this.zicmc = value;
    }
    
    public String getQuerq() {
        return querq;
    }
    
    public void setQuerq(String value) {
        this.querq = value;
    }

	public Integer getShul() {
		return shul;
	}

	public void setShul(Integer shul) {
		this.shul = shul;
	}

	public String getHangydl() {
		return hangydl;
	}

	public void setHangydl(String hangydl) {
		this.hangydl = hangydl;
	}
     
}
