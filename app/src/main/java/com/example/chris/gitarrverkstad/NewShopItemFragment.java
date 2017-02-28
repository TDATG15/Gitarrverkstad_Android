package com.example.chris.gitarrverkstad;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by chris on 2017-02-14.
 */

public class NewShopItemFragment extends Fragment {

    private View currentView;
    private static int TAKE_PICTURE = 1;
    private Uri imageUri;
    private LinearLayout linearLayout;
    int instrumentId;
    Button saveButton;
    Button cancelButton;
    EditText editTextDesc;
    EditText editTextModel;
    EditText editTextPrice;
    EditText editTextPrevOwn;
    EditText editTextCurrOwn;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.new_shop_item, container, false);
        linearLayout = (LinearLayout) currentView.findViewById(R.id.new_item_linear);
        ImageButton addPictureButton = (ImageButton) currentView.findViewById(R.id.new_picture_button);
        addPictureButton.setOnClickListener(cameraListener);
        saveButton = (Button) currentView.findViewById(R.id.new_item_done);
        cancelButton = (Button) currentView.findViewById(R.id.new_item_cancel);
        editTextDesc = (EditText) currentView.findViewById(R.id.new_item_desc);
        editTextCurrOwn = (EditText) currentView.findViewById(R.id.new_item_maker);
        editTextModel = (EditText) currentView.findViewById(R.id.new_item_model);
        editTextPrice = (EditText) currentView.findViewById(R.id.new_item_price);
        editTextPrevOwn = (EditText) currentView.findViewById(R.id.new_item_previous_owner);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                exitLayout();
            }
        });
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
                retrofit2.Call<Instrument> call = client.postInstrument(new Instrument(
                        editTextDesc.getText().toString(),
                        instrumentId,
                        editTextModel.getText().toString(),
                        editTextPrice.getText().toString(),
                        editTextPrevOwn.getText().toString(),
                        editTextCurrOwn.getText().toString())
                );
                call.enqueue(new CustomCallback());
            }
        });
        // Function? to load image array into imageview to display images
        // add textfields for information
        // add buttons to confirm item or discard it
        return currentView;
    }

    //Action listener for button
    private View.OnClickListener cameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            takePhoto(v);
        }
    };

    //Take photo
    private void takePhoto(View v) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date now = new Date();
        String fileName = formatter.format(now) + ".jpg";

        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    //Do something when photo was successfully taken
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            ImageView imageView = new ImageView(currentView.getContext());
            imageView.setImageDrawable(Drawable.createFromPath(imageUri.getPath()));
            linearLayout.addView(imageView);
            // store photo to some kind of array
        }
    }
    public void setInstrumentId(int instrumentId){
        this.instrumentId = instrumentId;
    }
    public void exitLayout(){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new ShopFragment()).commit();
    }

    public class CustomCallback implements Callback<Instrument>{
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
