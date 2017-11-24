package com.fjflPic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


public class WebServiceUtil {
    /**
     * 调用远程过程
     * 
     * @param url
     * @param method
     * @param params
     * @return
     * @throws HttpException
     * @throws IOException
     * @throws DocumentException
     */
    public static String call6In1(String url, String method,
            LinkedHashMap<String, String> params) throws HttpException,
            IOException, DocumentException {
//        LogUtil.debug("Enter WebServiceUtil#call6In1,url="+url+",method="+method+",mapParams="+params);
        String res = null;
        PostMethod postMethod = new PostMethod(url);
        postMethod.setRequestHeader("Content-Type", "application/soap+xml");
        postMethod.setRequestHeader("SOAPAction", "zhpt177");
        postMethod.setRequestHeader("Connection", "Keep-Alive");
        postMethod.setRequestHeader("Accept-Encoding", "gzip, deflate");
        RequestEntity entity = new StringRequestEntity(getSoapXml(url, method,
                params), "text/xml", "utf-8");
        postMethod.setRequestEntity(entity);
        HttpClient client = new HttpClient();
        client.getParams().setConnectionManagerTimeout(8000);// used when
                                                             // retrieving an
                                                             // HTTP connection
                                                             // from the HTTP
                                                             // connection
                                                             // manager.
        client.getParams().setSoTimeout(8000);// which is the timeout for
                                              // waiting for data. A timeout
                                              // value of zero is interpreted as
                                              // an infinite timeout.
        client.executeMethod(postMethod);
        if (postMethod.getStatusCode() == HttpStatus.SC_OK) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InputStream is = postMethod.getResponseBodyAsStream();
            byte[] buf = new byte[128];
            int len;
            while ((len = is.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            String responseXml = new String(bos.toByteArray(), "utf-8");
            bos.close();
            is.close();
            res = getRPCReturn(responseXml, method);
            res = URLDecoder.decode(res, "utf-8");
        } else {
            throw new RuntimeException("请求失败");
        }
        
//        LogUtil.debug("Exit WebServiceUtil#call6In1,resp="+res);

        return res;
    }

    /**
     * 组织SOAP文档
     * 
     * @param url
     * @param method
     * @param params
     * @return
     */
    public static String getSoapXml(String url, String method,
            LinkedHashMap<String, String> params) {
        StringBuffer xml = new StringBuffer();
        xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        xml.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                + " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\""
                + " xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        xml.append("<soap:Body>");
        xml.append("<m:" + method + " xmlns:m=\"" + url + "\">");
        /* 有顺序问题所以不能使用 */
        for (Iterator<String> iterator = params.keySet().iterator(); iterator
                .hasNext();) {
            String key = iterator.next();
            xml.append("<" + key + ">");
            xml.append(params.get(key));
            xml.append("</" + key + ">");
        }
        xml.append("</m:" + method + ">");
        xml.append("</soap:Body>");
        xml.append("</soap:Envelope>");
        return xml.toString();
    }

    /**
     * 获取返回结果
     * 
     * @param responseXml
     * @param method
     * @return
     * @throws DocumentException
     * @throws UnsupportedEncodingException
     */
    public static String getRPCReturn(String responseXml, String method)
            throws DocumentException, UnsupportedEncodingException {
        String res = null;
        Document doc = DocumentHelper.parseText(responseXml);
        Element root = doc.getRootElement();
        Element returnEl = null;
        try {
            returnEl = root.element("Body").element(method + "Response")
                    .element(method + "Return");
        } catch (Exception e) {
        }
        if (returnEl != null) {
            res = returnEl.getText();
        }
        return res;
    }
}
