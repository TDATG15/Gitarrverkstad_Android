package com.example.chris.gitarrverkstad;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

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
    EditText editTextDesc;
    EditText editTextPrice;
    EditText editTextModel;
    EditText editTextPrevOwn;
    EditText editTextCurrentOwn;
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.edit_gallery_item_layout, container, false);
        editTextModel = (EditText) currentView.findViewById(R.id.selectedGalleryItem_model);
        editTextModel.setText(selectedItem.getModel());
        editTextDesc = (EditText) currentView.findViewById(R.id.selectedGalleryItem_desc);
        editTextDesc.setText(selectedItem.getDesc());
        editTextPrice = (EditText) currentView.findViewById(R.id.selectedGalleryItem_price);
        editTextPrice.setText("" + selectedItem.getPrice() + "");
        editTextPrevOwn = (EditText) currentView.findViewById(R.id.selectedGalleryItem_previous_owner);
        editTextPrevOwn.setText(selectedItem.getPrevown());
        editTextCurrentOwn = (EditText) currentView.findViewById(R.id.selectedGalleryItem_current_owner);
        editTextCurrentOwn.setText(selectedItem.getCreator());
        registerClickCallback();
        setHasOptionsMenu(true);
        return currentView;
    }

    public void exitLayout(){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new ShopFragment()).commit();
    }

    public void registerClickCallback(){
        ImageButton new_picture = (ImageButton) currentView.findViewById(R.id.selectedGalleryItem_photob);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_shop_item_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.edit_shop_item_accept) {
            //TODO: Accepting makes program crash, something with the text i think, might be that the server is down.
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
                    editTextCurrentOwn.getText().toString(),
                    selectedItem.getInstrumentID()));
            call.enqueue(new Callback<Instrument>() {

                @Override
                public void onResponse(Call<Instrument> call, Response<Instrument> response) {
                    exitLayout();
                    Toast toast = Toast.makeText(currentView.getContext(), "Inlägg ändrad!", Toast.LENGTH_SHORT);
                    toast.show();
                }

                @Override
                public void onFailure(Call<Instrument> call, Throwable t) {
                    exitLayout();
                }

            });
        } else if (id == R.id.edit_shop_item_remove) {
            builder.setMessage("Är du säker på att du vill avbryta ändringar?").setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String API_BASE_URL = "http://andersverkstad.zapto.org:8080";
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(API_BASE_URL)
                            .client(new OkHttpClient())
                            .addConverterFactory(SimpleXmlConverterFactory.create())
                            .build();
                    InstrumentClient client = retrofit.create(InstrumentClient.class);
                    retrofit2.Call<Instrument> call = client.deleteInstrument(selectedItem.getInstrumentID());
                    call.enqueue(new CustomCallback());
                    Toast toast = Toast.makeText(currentView.getContext(), "Tog bort inlägg!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
            builder.create();
            builder.show();
        } else if (id == R.id.edit_shop_item_cancel) {
            builder.setMessage("Är du säker på att du vill radera inlägget?").setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    exitLayout();
                }
            });
            builder.create();
            builder.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
