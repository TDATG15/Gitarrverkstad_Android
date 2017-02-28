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
import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by stefa_000 on 2017-02-10.
 */

public class ShopFragment extends Fragment {
    private List<GalleryItem> galleryItems = new ArrayList<GalleryItem>();
    InstrumentList instrumentList;
    static final int CAM_REQUEST = 1;
    View currentView;
    int newestInstrumentId;
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
        String API_BASE_URL = "http://andersverkstad.zapto.org:8080";
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        InstrumentClient client = retrofit.create(InstrumentClient.class);

        retrofit2.Call<InstrumentList> call = client.getInstruments();
        call.enqueue(new Callback<InstrumentList>() {

            @Override
            public void onResponse(Call<InstrumentList> call, Response<InstrumentList> response) {
                InstrumentList instrumentList = response.body();
                afterConnection(instrumentList, null);
            }

            @Override
            public void onFailure(Call<InstrumentList> call, Throwable t) {
                afterConnection(null, t.getMessage());
            }

        });
           // new XMLTask().execute("http://andersverkstad.zapto.org:8080/ProjectEE-war/webresources/entities.instrument");
    }

    private void afterConnection(InstrumentList instrumentList, String failmessage){
        this.instrumentList = instrumentList;
        if(instrumentList != null) {
            populateList();
        }else{
            populateFailList(failmessage);
        }
        populateListView();
        registerClickCallback();
    }

    public void populateList(){
        //boolean notdone = true;
        List<Instrument> instruments = instrumentList.getInstruments();
        for(int i = 0; i < instruments.size(); i++) {
            galleryItems.add(new GalleryItem(
                    instruments.get(i).getBeskrivning(),
                    R.mipmap.ic_guitar_icon,
                    instruments.get(i).getModel(),
                    Integer.parseInt(instruments.get(i).getPris()),
                    instruments.get(i).getTillverkare(),
                    instruments.get(i).getTidigareAgare(),
                    Integer.toString(instruments.get(i).getInstrumentId())
            ));
            /*if (i != instruments.get(i).getInstrumentId() && notdone) {
                notdone = false;
                newestInstrumentId = i;
            }*/
        }
        newestInstrumentId = -1;
        for(int i = 1; i < instruments.size(); i++) {
            if(instruments.get(i).getInstrumentId() > newestInstrumentId) {
                newestInstrumentId = instruments.get(i).getInstrumentId();
            }
        }
        newestInstrumentId = newestInstrumentId + 1;
    }


    public void populateFailList(String failmessage){
            galleryItems.add(new GalleryItem(
                    failmessage,
                    R.mipmap.ic_guitar_icon,
                    "FAIL",
                    0,
                    "FAIL",
                    "FAIL",
                    "FAIL"
            ));
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
                NewShopItemFragment frag = new NewShopItemFragment();
                frag.setInstrumentId(newestInstrumentId);
                fragmentManager.beginTransaction().replace(R.id.content_frame, frag).commit();
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
/*
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
            afterConnection(result);
        }

    }*/
}
