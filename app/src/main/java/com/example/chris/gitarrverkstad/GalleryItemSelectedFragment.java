package com.example.chris.gitarrverkstad;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by stefa_000 on 2017-02-13.
 */

public class GalleryItemSelectedFragment extends Fragment{
    View currentView;
    private GalleryItem selectedItem;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.edit_gallery_item_layout, container, false);
        EditText editText = (EditText) currentView.findViewById(R.id.selectedGalleryItem_name);
        editText.setText(selectedItem.getName());
        editText = (EditText) currentView.findViewById(R.id.selectedGalleryItem_desc);
        editText.setText(selectedItem.getDesc());
        editText = (EditText) currentView.findViewById(R.id.selectedGalleryItem_price);
        editText.setText("" + selectedItem.getPrice() + "");
        ImageView imageView = (ImageView) currentView.findViewById(R.id.selectedGalleryItem_image);
        imageView.setImageResource(selectedItem.getImageID());
        return currentView;
    }

    public GalleryItem getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(GalleryItem selectedItem) {
        this.selectedItem = selectedItem;
    }
}
