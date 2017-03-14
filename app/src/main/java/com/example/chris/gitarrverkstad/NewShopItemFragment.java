package com.example.chris.gitarrverkstad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
    String imageToSend;
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
        editTextDesc = (EditText) currentView.findViewById(R.id.new_item_desc);
        editTextCurrOwn = (EditText) currentView.findViewById(R.id.new_item_maker);
        editTextModel = (EditText) currentView.findViewById(R.id.new_item_model);
        editTextPrice = (EditText) currentView.findViewById(R.id.new_item_price);
        editTextPrevOwn = (EditText) currentView.findViewById(R.id.new_item_previous_owner);

        addListeners();
        // Function? to load image array into imageview to display images
        // add textfields for information
        // add buttons to confirm item or discard it
        return currentView;
    }

    public void addListeners() {
        ImageButton addPictureButton = (ImageButton) currentView.findViewById(R.id.new_picture_button);
        Button publishButton = (Button) currentView.findViewById(R.id.new_shop_item_publish);
        Button cancelButton = (Button) currentView.findViewById(R.id.new_shop_item_cancel);

        addPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto(v);
            }
        });

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String API_BASE_URL = "http://andersverkstad.zapto.org:8080";
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                String image_Str = null;
                if(imageUri != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 72, stream);
                        byte[] byte_Arr = stream.toByteArray();
                        image_Str = Base64.encodeToString(byte_Arr, Base64.DEFAULT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

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
                        editTextCurrOwn.getText().toString(),
                        image_Str
                ));
                call.enqueue(new CustomCallback());

                Toast toast = Toast.makeText(currentView.getContext(), "Publicerad!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }

    /*
    public void uploadPhoto(){
        OkHttpClient client = new OkHttpClient.Builder().build();
        UploadService service = new Retrofit.Builder().baseUrl("http://10.250.121.150:8080").client(client).build().create(UploadService.class);
        File file = new File(imageUri.getPath());

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_text");
        ;
        retrofit2.Call<okhttp3.ResponseBody> req = service.postImage(body, name);
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
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new ShopFragment());
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
