package com.example.chris.gitarrverkstad;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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

    public void postIntoURL(String toPost, int index) throws Exception{
        url = new URL(url.getPath() + "/" + Integer.toString(index));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");

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
