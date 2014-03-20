package com.sany.mbp.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONObject;
import org.xml.sax.InputSource;

import com.sany.masterdata.task.HrSyncTask;


public class test {

	/**
	 * @param args
	 * @throws MalformedURLException 
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws MalformedURLException, UnsupportedEncodingException {
		
		
//		HrSyncTask hst = new HrSyncTask();
//		hst.initialData();
		
		//		File file = new File("d:\\2.xml");
		//		List list = new ArrayList();
		////		InputStream stream = new ByteArrayInputStream(xmlstr.getBytes());
		//		SAXReader saxReader = new SAXReader();   
		//		try {  
		//			Document document = saxReader.read(file);   
		//			Element employees=document.getRootElement();   
		//			for( Iterator i = employees.elementIterator();  i.hasNext();){   
		//				Element employee = (Element) i.next();   
		//				JSONObject jsonObject = new JSONObject();
		//				for(Iterator j = employee.elementIterator(); j.hasNext();){   
		//					Element node=(Element) j.next();   
		//					jsonObject.put(node.getName(), node.getText());
		//					System.out.println(node.getName()+":"+node.getText()); 
		//				}
		//				System.out.println(employee.getName()+" === "+employee.getText());
		//				System.out.println(jsonObject);
		//				list.add(jsonObject);
		//			}
		//		}catch (Exception e) {
		//		}


//		System.out.println(xmltoMap(sss));
//		System.out.println(xmltoList(s1));
//		System.out.println(xmlToMap1(sss));
		//		System.out.println(xmlToMapList(sss));
	}
	

//	public static List xmltoList(String xml) {  
//		try {  
//			List<HashMap> list = new ArrayList<HashMap>();  
//			Document document = DocumentHelper.parseText(xml);  
//			Element nodesElement = document.getRootElement();  
//			List nodes = nodesElement.elements();  
//			for (Iterator its = nodes.iterator(); its.hasNext();) {  
//				Element nodeElement = (Element) its.next();  
//				HashMap map = xmltoMap(nodeElement.asXML());  
//				list.add(map);  
//				map = null;  
//			}  
//			nodes = null;  
//			nodesElement = null;  
//			document = null;  
//			return list;  
//		} catch (Exception e) {  
//			e.printStackTrace();  
//		}  
//		return null;  
//	}
//	
//	public static HashMap xmltoMap(String xml) {  
//		try {  
//			HashMap map = new HashMap();  
//			Document document = DocumentHelper.parseText(xml);  
//			Element nodeElement = document.getRootElement();  
//			List node = nodeElement.elements();  
//			for (Iterator it = node.iterator(); it.hasNext();) {  
//				Element elm = (Element) it.next();  
//				if(elm.elementIterator().hasNext()){
//					map.put(elm.getName(), xmltoList(elm.asXML())); 
//				}else{
//					map.put(elm.getName(), elm.getText());
//				}
//				elm = null;  
//			}  
//			node = null;  
//			nodeElement = null;  
//			document = null;  
//			return map;  
//		} catch (Exception e) {  
//			e.printStackTrace();  
//		}  
//		return null;  
//	}  
//
//	@SuppressWarnings({ "rawtypes", "unchecked"})
//	public static List xmlToMap1(String xmlstr){
//		List relist = new ArrayList();
//		SAXReader saxReader = new SAXReader();   
//		try {  
//			InputStream stream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
//			Document document = saxReader.read(stream);   
//			Element element0 =document.getRootElement();  
//			//			element0.elements();
//
//
//			for( Iterator i = element0.elementIterator();  i.hasNext();){   
//				Element element1 = (Element) i.next();   
//				HashMap mapObject = new HashMap();
//				if(element1.elementIterator().hasNext()){
//					System.out.println(element1.asXML());
//					List relisttemp = xmlToMap1(element1.asXML());
//					mapObject.put(element1.getName(), relisttemp);
//				}else{
//					mapObject.put(element1.getName(), element1.getText());
//				}
//			}
//			//			relist.add(mapObject);
//
//		}catch (Exception e) {
//		}
//		return relist;
//	}
//
//	public static List xmlToMapList(String xmlstr){
//		List list = new ArrayList();
//		SAXReader saxReader = new SAXReader();   
//		try {  
//			InputStream stream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
//			Document document = saxReader.read(stream);   
//			Element element0 =document.getRootElement();   
//			for( Iterator i = element0.elementIterator();  i.hasNext();){   
//				Element element1 = (Element) i.next();   
//				HashMap mapObject = new HashMap();
//				for(Iterator j = element1.elementIterator(); j.hasNext();){   
//					Element node=(Element) j.next();   
//					mapObject.put(node.getName(), node.getText());
//				}
//				mapObject.put(element1.getName(), element1.getText());
//				list.add(mapObject);
//			}
//		}catch (Exception e) {
//		}
//		return list;
//	}
//
//	private static String sss = "<?xml version=\"1.0\"?>"+
//			"<IntentDealerContractDataContract xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
//			"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"+
//			" <ContractId></ContractId>"+//保存前 自动生成guid
//			"<LineNo>0</LineNo>"+//自动生成
//			"<StatusCode>V02240</StatusCode>"+//销售意向状态，新增默认为“已录入”的状态，提交时，默认为“提交”的状态编码
//			"<DealerId>7eec9afe-966a-4560-995e-39cd46077074</DealerId>"+//代理商ID
//			"<DealerName>湖南三一工程机械有限公司</DealerName>"+//代理商名称
//			" <StationId>2c13c63b-387b-434f-9115-0cf42e14a91f</StationId>"+//员工ID
//			" <BmuId>c30a3404-4abf-46a8-a538-918200a2f25c</BmuId>"+//事业部编码
//			" <StatusName />"+
//			" <ApplyId>15f3f364-818c-4295-9141-690f41121e41</ApplyId>"+//--------------------------？
//			" <ApplyName>王孟</ApplyName>"+//申请人
//			" <ApplyDate>2012-06-08T05:50:53</ApplyDate>"+//申请时间
//			" <CustomerId>151b7ebf-c52b-4820-8770-0d3c36d54d48</CustomerId>"+//--------从客户信息中取出
//			" <CustName>贺新军</CustName>"+
//			" <CustPayTypeCode>V03921</CustPayTypeCode>"+//付款方式ID
//			" <CustPayTypeName>自办按揭</CustPayTypeName>"+//付款方式名称
//			"<SalesStaffId>15f3f364-818c-4295-9141-690f41121e41</SalesStaffId>"+//营销代表编号
//			" <SalesStaffName>王孟</SalesStaffName>"+//营销代表名称
//			"<PayConditionInfo>按揭：首付2%，计2万元，办理2成2年按揭，按揭金额2万元；按揭费用：保证金2万元；" +
//			"手续费2%，计2万元；抵押公证费2万元；保险2万元；续保押金2万元；合计10万元。</PayConditionInfo>"+
//			" <SpecailNotes />"+//其它约定
//			" <SingleFlag>0</SingleFlag>"+//是否单台设备 0 单台 1 多台
//			" <OAreaFlag>1</OAreaFlag>"+//是否跨区域 0否 1 
//			" <BidFlag>1</BidFlag>"+//是否投标
//			" <PurchaseType>V09910</PurchaseType>"+//采购属性
//			" <ContractAddressCode>1002110100</ContractAddressCode>"+//合同签署地 编码
//			" <ReceiverName>TW</ReceiverName>"+//收货人名称
//			"  <ReceiverTel>123456784</ReceiverTel>"+//收货人电话
//			" <ReceiverIDCode />"+
//			"  <SpecialDesciption />"+//保修期特殊配置后面的空白输入区域
//			"  <ReceiveAddressCode>V10010</ReceiveAddressCode>"+//验货地点编码
//			" <TransFeeTypeCode>V10110</TransFeeTypeCode>"+//运输费用编码
//			" <Renter />"+//没有用到
//			" <ContractAddressDesc />"+//合同签署地详细描述
//			" <DeliveryAddressCode>1002130100</DeliveryAddressCode>"+//交货地点编码
//			" <DeliveryAddressDesc />"+//交货地点详细描述
//			" <DealerSupLi>"+
//			"   <DealerSupplyContract>"+
//			"    <GourpID>6</GourpID>"+
//			"     <ProductType1 />"+
//			"    <ProductType2 />"+
//			"    <XYcoun1>0</XYcoun1>"+
//			"    <XYcoun2>0</XYcoun2>"+
//			"    <XYAmt1>100.0000</XYAmt1>"+
//			"<XYAmt2>0.0000</XYAmt2>"+
//			"<IsSelect>false</IsSelect>"+
//			" </DealerSupplyContract>"+
//			"<DealerSupplyContract>"+
//			" <GourpID>1</GourpID>"+
//			"   <ProductType1>壹佰万元</ProductType1>"+
//			"  <ProductType2 />"+
//			"   <XYcoun1>11</XYcoun1>"+
//			"  <XYcoun2>0</XYcoun2>"+
//			"  <XYAmt1>100.0000</XYAmt1>"+
//			"  <XYAmt2>0.0000</XYAmt2>"+
//			"  <IsSelect>false</IsSelect>"+
//			" </DealerSupplyContract>"+
//			"</DealerSupLi>"+
//			" <SalesPurposeNo></SalesPurposeNo>"+//销售意向ID
//			" <Insurer />"+//保证人
//			" <IsCheckCredit>1</IsCheckCredit>"+//是否已经信用调查
//			" <IsMarkDisposition>1</IsMarkDisposition>"+//是否标配
//			"</IntentDealerContractDataContract>";
//
//	
//	private static String s1 = " <DealerSupLi>"+
//			 "   <DealerSupplyContract>"+
//			  "    <GourpID>6</GourpID>"+
//			 "     <ProductType1 />"+
//			  "    <ProductType2 />"+
//			  "    <XYcoun1>0</XYcoun1>"+
//			  "    <XYcoun2>0</XYcoun2>"+
//			  "    <XYAmt1>100.0000</XYAmt1>"+
//			      "<XYAmt2>0.0000</XYAmt2>"+
//			     "<IsSelect>false</IsSelect>"+
//			   " </DealerSupplyContract>"+
//			    "<DealerSupplyContract>"+
//			     " <GourpID>1</GourpID>"+
//			   "   <ProductType1>壹佰万元</ProductType1>"+
//			    "  <ProductType2 />"+
//			   "   <XYcoun1>11</XYcoun1>"+
//			    "  <XYcoun2>0</XYcoun2>"+
//			    "  <XYAmt1>100.0000</XYAmt1>"+
//			    "  <XYAmt2>0.0000</XYAmt2>"+
//			    "  <IsSelect>false</IsSelect>"+
//			   " </DealerSupplyContract>"+
//			  "</DealerSupLi>";
}