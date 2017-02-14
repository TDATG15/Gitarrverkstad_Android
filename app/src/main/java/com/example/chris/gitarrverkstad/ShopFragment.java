package com.example.chris.gitarrverkstad;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefa_000 on 2017-02-10.
 */

public class ShopFragment extends Fragment {
    private List<GalleryItem> galleryItems = new ArrayList<GalleryItem>();
    View currentView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.shop_layout, container, false);
        populateList();
        populateListView();
        registerClickCallback();
        return currentView;
    }

    public void populateList(){
        galleryItems.add(new GalleryItem("Kalles gitarr", "Mycket bra!!!! bla bla bla bla bla", R.mipmap.ic_guitar_icon, 4000));
        galleryItems.add(new GalleryItem("Pelles gitarr", "Mycket dålig!!!! bla bla bla bla bla", R.mipmap.ic_guitar_icon, 3000));
        galleryItems.add(new GalleryItem("Karls gitarr", "Den är okej. bla bla bla bla bla", R.mipmap.ic_guitar_icon, 2000));
        galleryItems.add(new GalleryItem("Super gitarr", "Mycket bra!!!! bla bla bla bla bla", R.mipmap.ic_guitar_icon, 4000));
        galleryItems.add(new GalleryItem("Gammal gitarr", "Mycket dålig!!!! bla bla bla bla bla", R.mipmap.ic_guitar_icon, 3000));
        galleryItems.add(new GalleryItem("Ny gitarr", "Den är okej. bla bla bla bla bla", R.mipmap.ic_guitar_icon, 2000));
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
            textView.setText(currentItem.getName());
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
    }
}
