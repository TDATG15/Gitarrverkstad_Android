package com.example.chris.gitarrverkstad;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by stefa_000 on 2017-02-17.
 */

public class HttpXmlConnecter {
    private URL url;
    private HttpURLConnection connection;
    private Document doc;

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public HttpURLConnection getConnection() {
        return connection;
    }

    public void setConnection(HttpURLConnection connection) {
        this.connection = connection;
    }

    public String convertDocumentToString(Document doc) throws TransformerException{
        StringWriter sw = new StringWriter();
        Transformer serializer = TransformerFactory.newInstance().newTransformer();
        serializer.transform(new DOMSource(doc), new StreamResult(sw));
        return sw.toString();
    }

    public void postIntoURL(Document doc, int index) throws Exception{
        url = new URL(url.getPath() + "/" + Integer.toString(index));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type","application/xml");
        String input = convertDocumentToString(doc);
        OutputStream os = connection.getOutputStream();
        os.write(input.getBytes());
        os.flush();
        if(connection.getResponseCode() != HttpURLConnection.HTTP_CREATED){
            throw new RuntimeException("Failed: HTTP ERROR CODE : " + connection.getResponseCode());
        }
        stop();
    }

    public HttpXmlConnecter(String xmlurl) throws Exception{
        this.url = new URL(xmlurl);
    }

    public void start() throws Exception{
        connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        doc = parseXML(connection.getInputStream());
    }

    public void stop() throws Exception{
        connection.disconnect();
    }

    public Document parseXML(InputStream stream) throws Exception{
        DocumentBuilderFactory objDocumentBuilderFactory = null;
        DocumentBuilder objDocumentBuilder = null;
        Document doc = null;
        objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();
        doc = objDocumentBuilder.parse(stream);
        return doc;
    }

    @Override
    public String toString(){
        String temp = doc.getTextContent();
        if(temp != null){
            return temp;
        }
        return "";
    }
}
