package com.example.chris.gitarrverkstad;

import android.app.Activity;
import android.app.Fragment;
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

/**
 * Created by stefa_000 on 2017-02-13.
 */

public class GalleryItemSelectedFragment extends Fragment {
    View currentView;
    private GalleryItem selectedItem;
    static final int CAM_REQUEST = 1;
    private ImageView imageView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.edit_gallery_item_layout, container, false);
        EditText editText = (EditText) currentView.findViewById(R.id.selectedGalleryItem_name);
        editText.setText(selectedItem.getModel());
        editText = (EditText) currentView.findViewById(R.id.selectedGalleryItem_desc);
        editText.setText(selectedItem.getDesc());
        editText = (EditText) currentView.findViewById(R.id.selectedGalleryItem_price);
        editText.setText("" + selectedItem.getPrice() + "");
        imageView = (ImageView) currentView.findViewById(R.id.selectedGalleryItem_image);
        if (!imageView.hasOverlappingRendering()) {
            imageView.setImageResource(selectedItem.getImageID());
        }
        registerClickCallback();
        return currentView;
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
}
