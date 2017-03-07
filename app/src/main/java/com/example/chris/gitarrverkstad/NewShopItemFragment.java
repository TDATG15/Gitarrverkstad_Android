package com.example.chris.gitarrverkstad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
        editTextDesc = (EditText) currentView.findViewById(R.id.new_item_desc);
        editTextCurrOwn = (EditText) currentView.findViewById(R.id.new_item_maker);
        editTextModel = (EditText) currentView.findViewById(R.id.new_item_model);
        editTextPrice = (EditText) currentView.findViewById(R.id.new_item_price);
        editTextPrevOwn = (EditText) currentView.findViewById(R.id.new_item_previous_owner);

        setHasOptionsMenu(true);
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

    /*
    public void uploadPhoto(){
        OkHttpClient client = new OkHttpClient.Builder().build();
        Client service = new Retrofit.Builder().baseUrl("http://10.250.121.150:8080").client(client).build().create(Client.class);
        File file = new File(imageUri.getPath());
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_text");
        retrofit2.Call<okhttp3.ResponseBody> req = service.postImage(name, body);
        req.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                editTextDesc.setText("You did it");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                editTextDesc.setText(t.getMessage());
                t.printStackTrace();
            }
        });
    }
    */

    //Take photo
    private void takePhoto(View v) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date now = new Date();
        //String fileName = formatter.format(now) + ".jpg";
        String fileName = "ins" + instrumentId + "d" + formatter.format(now) + ".jpg";

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.new_shop_item_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.new_shop_item_menu_publish) {
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

            //uploadPhoto();

            Toast toast = Toast.makeText(currentView.getContext(), "Publicerad!", Toast.LENGTH_SHORT);
            toast.show();
        } else if (id == R.id.new_shop_item_menu_cancel) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Är du säker på att du vill radera inlägget?")
                    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            exitLayout();
                        }
                    });
            builder.create();
            builder.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
