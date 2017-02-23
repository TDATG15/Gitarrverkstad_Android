package com.example.chris.gitarrverkstad;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefa_000 on 2017-02-10.
 */

public class ShopFragment extends Fragment {
    private List<GalleryItem> galleryItems = new ArrayList<GalleryItem>();
    Document doc;
    static final int CAM_REQUEST = 1;
    View currentView;
    int value;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.shop_layout, container, false);
        try {
            ConnectXml();
        } catch(Exception e){
            Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG);
            e.printStackTrace();
        }
        return currentView;
    }

    private void ConnectXml() throws Exception{
            new XMLTask().execute("http://andersverkstad.zapto.org:8080/ProjectEE-war/webresources/entities.instrument");
    }

    private void afterConnection(Document document){
        this.doc = document;
        populateList();
        populateListView();
        registerClickCallback();
    }

    public void populateList(){
        NodeList descList = doc.getElementsByTagName("beskrivning");
        NodeList modelList = doc.getElementsByTagName("modell");
        NodeList priceList = doc.getElementsByTagName("pris");
        NodeList creatorList = doc.getElementsByTagName("tillverkare");
        NodeList idList = doc.getElementsByTagName("instrumentId");
        NodeList prevownList = doc.getElementsByTagName("tidigareAgare");
        //(String desc, int imageID, String model, double price, String creator, String prevown, String instrumentID)
        for(int i = 0; i < descList.getLength(); i++){
            galleryItems.add(new GalleryItem(
                        descList.item(i).getTextContent(),
                        R.mipmap.ic_guitar_icon,
                        modelList.item(i).getTextContent(),
                        Double.parseDouble(priceList.item(i).getTextContent()),
                        creatorList.item(i).getTextContent(),
                        prevownList.item(i).getTextContent(),
                        idList.item(i).getTextContent()
                    ));
        }
    }

    public void populateListView(){
        ArrayAdapter<GalleryItem> adapter = new GalleryItemListAdapter();
        ListView list = (ListView) currentView.findViewById(R.id.shop_gallery_list);
        list.setAdapter(adapter);
    }

    public class GalleryItemListAdapter extends ArrayAdapter<GalleryItem>{
        public GalleryItemListAdapter(){
            super(getActivity(), R.layout.item_shopcontainer, galleryItems);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = getActivity().getLayoutInflater().inflate(R.layout.item_shopcontainer, parent, false);
            }
            GalleryItem currentItem = galleryItems.get(position);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.itemgallery_image);
            imageView.setImageResource(currentItem.getImageID());
            TextView textView = (TextView) itemView.findViewById(R.id.itemgallery_name);
            textView.setText(currentItem.getModel());
            textView = (TextView) itemView.findViewById(R.id.itemgallery_desc);
            textView.setText(currentItem.getDesc());
            textView = (TextView) itemView.findViewById(R.id.itemgallery_price);
            textView.setText("" + currentItem.getPrice() + "kr");
            return itemView;
        }
    }

    public void registerClickCallback(){
        ListView list = (ListView) currentView.findViewById(R.id.shop_gallery_list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id){
                FragmentManager fragmentManager = getFragmentManager();
                GalleryItemSelectedFragment frag = new GalleryItemSelectedFragment();
                frag.setSelectedItem(galleryItems.get(position));
                fragmentManager.beginTransaction().replace(R.id.content_frame, frag).commit();
            }
        });

        ImageView new_item_button = (ImageView) currentView.findViewById(R.id.new_item_button);
        new_item_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new NewShopItemFragment()).commit();
            }
        });
    }

    private File getFile() {
        File folder = new File("sdcard/camera_app");
        if(!folder.exists()) {
            folder.mkdir();
        }

        File image_file = new File(folder, "cam_image.jpg");
        return image_file;
    }

    public class XMLTask extends AsyncTask<String, Document, Document> {



        @Override
        protected Document doInBackground(String... params){
            HttpXmlConnecter xmlconnecter;
            Document doc = null;
            try {
                xmlconnecter = new HttpXmlConnecter(params[0]);
                xmlconnecter.start();
                //temp = temp + xmlhandler.toString();
                //temp = temp + xmlhandler.getChildNodesTextContext("instrument", 0);
                doc = xmlconnecter.getDoc();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return doc;
        }
        @Override
        protected void onPostExecute(Document result) {
            super.onPostExecute(result);
            //olddoc = new DocumentManager(result);
            afterConnection(result);
        }

    }
}
