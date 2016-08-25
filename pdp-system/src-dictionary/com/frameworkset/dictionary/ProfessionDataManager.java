
package com.frameworkset.dictionary;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.frameworkset.dictionary.xml.XMLParser;
import com.frameworkset.dictionary.xml.XMLParserException;
import com.frameworkset.util.DaemonThread;
import com.frameworkset.util.ResourceInitial;
 

class ProfessionDataManager implements ResourceInitial,DataManager {
    private static Logger log = Logger.getLogger(ProfessionDataManager.class);
    private Map datas;
    private String description; 
    private String version;
    
    private DaemonThread daemonThread;
    private String configFileDTD = "dictionary.dtd";
    private String configFile = "dictionary.xml";
    
//    static {
//        try {
//        	getInstance().init();
//        } catch (ProfessionDataManagerException e) {
//            log.error(e);
//        }
//    }

    ProfessionDataManager() {
    }


    private void reset() {
        datas.clear();
        description = null;
        version = null;
        datas = null;
    }

    public void reinit() {
        reset();

        try {
            final ClassLoader loader = ProfessionDataManager.class.getClassLoader();
            XMLParser parser = new ProfessionDataXMLParser();
            URL xml = loader.getResource(configFile);

            if (xml == null) {
                xml = getTCL().getResource(configFile);
            }

            if (xml == null) {
                xml = getTCL().getResource("/" + configFile);
            }

            if (xml == null) {
                xml = ClassLoader.getSystemResource(configFile);
            }

            if (xml == null) {
                xml = ClassLoader.getSystemResource("/" + configFile);
            }

            String url = "";

            if (xml == null) {
                url = System.getProperty("user.dir");
                url += ("/" + configFile);
            }

            String path = xml.getPath();
            parser.init(xml.openStream());
            datas = parser.load();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            
            e.printStackTrace();
        } catch (XMLParserException e) {
            
            e.printStackTrace();
        } catch (IOException e) {
            
            e.printStackTrace();
        }
    }

    public void init() throws ProfessionDataManagerException {
        if (datas == null) {
            //获取根目录
            //				final String path = KHCERPContext.getKhcerpconf();
            //				//获取xml文件路径
            //				String xmlPath = KHCERPContext
            //								.getKHCERPConfigure()
            //								.getConfigureResource("professiondata.xml.path");
            final ClassLoader loader = ProfessionDataManager.class.getClassLoader();
            XMLParser parser = new ProfessionDataXMLParser();
            parser.setValidateDTD(false);

            //				parser.setResolver(
            //					new EntityResolver()
            //					{
            //						public InputSource resolveEntity(String publicId,
            //										String systemId) throws SAXException,
            //										IOException
            //						{
            //							InputSource is = null;
            //
            //							try
            //							{
            //								URL dtdurl = loader.getResource(configFileDTD);
            //
            //								java.io.InputStream dtdStream = dtdurl.openStream();
            //
            //								is = new InputSource(dtdStream);
            //							}
            //							catch (Exception ex)
            //							{
            //                                log.error(ex);
            //								ex.printStackTrace();
            //							}
            //							return is;
            //						}
            //					}
            //			);
            try {
                //parser.init(path + xmlPath);
                URL xml = loader.getResource(configFile);

                if (xml == null) {
                    xml = getTCL().getResource(configFile);
                }

                if (xml == null) {
                    xml = getTCL().getResource("/" + configFile);
                }

                if (xml == null) {
                    xml = ClassLoader.getSystemResource(configFile);
                }

                if (xml == null) {
                    xml = ClassLoader.getSystemResource("/" + configFile);
                }

                String url = "";

                if (xml == null) {
                    url = System.getProperty("user.dir");
                    url += ("/" + configFile);
                }

                String path = xml.getPath();
                parser.init(xml.openStream());
                datas = parser.load();

                /**
                 * 启动系统监控程序
                 */
                if (daemonThread == null) {
                    daemonThread = new DaemonThread(path,
                            this);
                    daemonThread.start();
                    log.debug("Dictionary file Monitor started success!");
                } else {
                    if (!daemonThread.started()) {
                        daemonThread.start();
                        log.debug("Dictionary file Monitor started success!");
                    }
                }
            } catch (XMLParserException e1) {
                log.error(e1);
                throw new ProfessionDataManagerException(e1.getMessage());
            } catch (IOException e1) {
                log.error(e1);
                throw new ProfessionDataManagerException(e1.getMessage());
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private ClassLoader getTCL()
        throws IllegalAccessException, InvocationTargetException {
        Method method = null;

        try {
            method = (java.lang.Thread.class).getMethod("getContextClassLoader",
                    null);
        } catch (NoSuchMethodException e) {
            return null;
        }

        return (ClassLoader) method.invoke(Thread.currentThread(), null);
    }

    /**
     * 获取对应id专业数据
     */
    public Data getData(String id) throws ProfessionDataManagerException {
        if (datas == null) {
            throw new ProfessionDataManagerException("专项数据没有装载成功");
        }

        Data data = ((Data) datas.get(id));

        if (data == null) {
            throw new ProfessionDataManagerException("给定名称的专业数据不存在[id=" + id +
                "]");
        }

        return data;
    }

    /**
     * 获取专项数据项
     */
    public List getDataItems(String id)
        throws ProfessionDataManagerException {
        //throw new ProfessionDataManagerException("not support method");
        return getData(id).getItems();
    }

    public String getDescription() throws ProfessionDataManagerException {
        if (description == null) {
            throw new ProfessionDataManagerException("not configure");
        }

        return description;
    }

    public String getItemName(String xmlid, String value)
        throws ProfessionDataManagerException {
        Data data = getData(xmlid);

        if (data == null) {
            throw new ProfessionDataManagerException("专业数据文件中不存在xmlid为'" +
                xmlid + "'的数据项");
        }

        return data.getItemName(value);

        //throw new ProfessionDataManagerException("xmlid为'" + xmlid + "'专业数据中不存在值为" + value + "数据项");
    }

    public String getItemValue(String xmlid, String name)
        throws ProfessionDataManagerException {
        Data data = getData(xmlid);

        if (data == null) {
            throw new ProfessionDataManagerException("专业数据文件中不存在xmlid为'" +
                xmlid + "'的数据项");
        }

        return data.getItemValue(name);

        //throw new ProfessionDataManagerException("xmlid为'" + xmlid + "'专业数据中不存在名称为" + name + "数据项");
    }

    public String getVersion() throws ProfessionDataManagerException {
        if (version == null) {
            throw new ProfessionDataManagerException("not configure");
        }

        return version;
    }
    

    /**
     * 将存放在xml文件中的专业数据解析为Data类型对象的集合,存放到Map中
     *
     */
    final class ProfessionDataXMLParser extends XMLParser {
        /**
         * @param dataPath
         * @throws XMLParserException
         */
        public ProfessionDataXMLParser() {
            super();
        }

        private void loadItem(Data data, Node xmlData) {
            try {
                NodeList items = foundNodeByTagName(xmlData, "item");
                List list = new ArrayList();

                for (int i = 0; (items != null) && (i < items.getLength());
                        i++) {
                    Node name = foundNodeByTagName(items.item(i), "name").item(0);
                    Node value = foundNodeByTagName(items.item(i), "value")
                                     .item(0);
                    Item item = new Item();
                    item.setName(name.getChildNodes().item(0).getNodeValue());
                    item.setValue(value.getChildNodes().item(0).getNodeValue());
                    list.add(item);
                }

                data.setItems(list);
            } catch (XMLParserException e) {
                e.printStackTrace();
            }
        }

        /**
         * 实现public abstract java.util.Map XMLParser.load()方法
         */
        public Map load() {
            Map datas = Collections.synchronizedMap(new HashMap());

            try {
                NodeList ret = foundNodeByTagName(document, "data");

                for (int i = 0; (ret != null) && (i < ret.getLength()); i++) {
                    Node node = ret.item(i);

                    try {
                        Data data = new Data();
                        data.setName(getAttribute((Element) node, "id"));
                        data.setDescription(getAttribute((Element) node, "name"));
                        loadItem(data, node);
                        datas.put(data.getName(), data);
                    } catch (XMLParserException e2) {
                        e2.printStackTrace();
                    }
                }

                NodeList descriptionList = foundNodeByTagName(document,
                        "description");
                description = descriptionList.item(0).getChildNodes().item(0)
                                             .getNodeValue();

                NodeList versionList = foundNodeByTagName(document, "version");
                version = versionList.item(0).getChildNodes().item(0)
                                     .getNodeValue();
            } catch (XMLParserException e) {
                e.printStackTrace();
            }

            return datas;
        }
    }


	public void destroy() {
		// TODO Auto-generated method stub
		
	}



	/* (non-Javadoc)
	 * @see com.frameworkset.dictionary.DataManager#getDataByID(java.lang.String)
	 */
	public Data getDataByID(String dictionaryID) throws ProfessionDataManagerException {
		// TODO Auto-generated method stub
		return null;
	}



	/* (non-Javadoc)
	 * @see com.frameworkset.dictionary.DataManager#getTaxCodesByUserId(java.lang.String)
	 */
	public List getTaxCodesByUserId(String userId,String dicttypeId) {
		// TODO Auto-generated method stub
		return null;
	}



	/* (non-Javadoc)
	 * @see com.frameworkset.dictionary.DataManager#getTaxCodesByOrgId(java.lang.String)
	 */
	public List getTaxCodesByOrgId(String orgId,String dicttypeId) {
		// TODO Auto-generated method stub
		return null;
	}



	/* (non-Javadoc)
	 * @see com.frameworkset.dictionary.DataManager#getDataByDcitNameAndUser(java.lang.String, java.lang.String)
	 */
	public Data getDataByDcitNameAndUser(String dictionname, String userId) throws ProfessionDataManagerException {
		// TODO Auto-generated method stub
		return null;
	}



	


	/* (non-Javadoc)
	 * @see com.frameworkset.dictionary.DataManager#hasOrgTaxcodeRelation(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean hasOrgTaxcodeRelation(String orgid, String dictypeid, String dictdatavalue, String opcode) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean removeDictFromCacheByID(String dictionaryID)
			throws ProfessionDataManagerException {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void destory() {
		this.reset();
		
	}
}
