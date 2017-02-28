package com.example.chris.gitarrverkstad;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by stefa_000 on 2017-02-13.
 */

public class GalleryItemSelectedFragment extends Fragment {
    View currentView;
    private GalleryItem selectedItem;
    static final int CAM_REQUEST = 1;
    private ImageView imageView;
    Button saveButton;
    Button deleteButton;
    EditText editTextDesc;
    EditText editTextPrice;
    EditText editTextModel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.edit_gallery_item_layout, container, false);
        saveButton = (Button) currentView.findViewById(R.id.selectedGalleryItem_publishb);
        deleteButton = (Button) currentView.findViewById(R.id.selectedGalleryItem_deleteb);
        editTextModel = (EditText) currentView.findViewById(R.id.selectedGalleryItem_name);
        editTextModel.setText(selectedItem.getModel());
        editTextDesc = (EditText) currentView.findViewById(R.id.selectedGalleryItem_desc);
        editTextDesc.setText(selectedItem.getDesc());
        editTextPrice = (EditText) currentView.findViewById(R.id.selectedGalleryItem_price);
        editTextPrice.setText("" + selectedItem.getPrice() + "");
        imageView = (ImageView) currentView.findViewById(R.id.selectedGalleryItem_image);
        if (!imageView.hasOverlappingRendering()) {
            imageView.setImageResource(selectedItem.getImageID());
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String API_BASE_URL = "http://andersverkstad.zapto.org:8080";
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .client(new OkHttpClient())
                        .addConverterFactory(SimpleXmlConverterFactory.create())
                        .build();
                InstrumentClient client = retrofit.create(InstrumentClient.class);
                retrofit2.Call<Instrument> call = client.putInstrument(new Instrument(
                        editTextDesc.getText().toString(),
                        Integer.parseInt(selectedItem.getInstrumentID()),
                        editTextModel.getText().toString(),
                        editTextPrice.getText().toString(),
                        selectedItem.getCreator(),
                        selectedItem.getPrevown()), selectedItem.getInstrumentID());
                call.enqueue(new Callback<Instrument>() {

                    @Override
                    public void onResponse(Call<Instrument> call, Response<Instrument> response) {
                        exitLayout();
                    }

                    @Override
                    public void onFailure(Call<Instrument> call, Throwable t) {
                        exitLayout();
                    }

                });
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String API_BASE_URL = "http://andersverkstad.zapto.org:8080";
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .client(new OkHttpClient())
                        .addConverterFactory(SimpleXmlConverterFactory.create())
                        .build();
                InstrumentClient client = retrofit.create(InstrumentClient.class);
                retrofit2.Call<Instrument> call = client.deleteInstrument(selectedItem.getInstrumentID());
                call.enqueue(new CustomCallback());
            }
        });
        registerClickCallback();
        return currentView;
    }

    public void exitLayout(){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new ShopFragment()).commit();
    }

    public void registerClickCallback(){
        Button new_picture = (Button) currentView.findViewById(R.id.selectedGalleryItem_photob);
        new_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(camera_intent, CAM_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path = "sdcard/camera_app/cam_image.jpg";
        imageView.setImageDrawable(Drawable.createFromPath(path));
    }

    private File getFile() {
        File folder = new File("sdcard/camera_app");
        if(!folder.exists()) {
            folder.mkdir();
        }

        File image_file = new File(folder, "cam_image.jpg");
        return image_file;
    }

    public GalleryItem getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(GalleryItem selectedItem) {
        this.selectedItem = selectedItem;
    }

    public class CustomCallback implements Callback<Instrument> {
        @Override
        public void onResponse(Call<Instrument> call, Response<Instrument> response) {
            exitLayout();
        }

        @Override
        public void onFailure(Call<Instrument> call, Throwable t) {
            exitLayout();
        }
    }
}
