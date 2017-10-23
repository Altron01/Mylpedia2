package com.altron.mylpedia2.Adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.altron.mylpedia2.Misc.Custom_Classes.Card;
import com.altron.mylpedia2.R;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

public class Mini_Screen_Adapter extends ArrayAdapter<Card> {

    private ImageLoader imageLoader;
    private List<Card> Card_List;
    private boolean flag;

    public Mini_Screen_Adapter(Context context, int resources, List<Card> Card_List, ImageLoader imageLoader, boolean _flag) {
        super(context, resources, Card_List);
        this.Card_List = new ArrayList<>(Card_List);
        this.imageLoader = imageLoader;
        flag = _flag;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = convertView;
        if (itemView == null) {
            itemView =  inflater.inflate(R.layout.activity_mini_screen_adapter, parent, false);
        }

        NetworkImageView Network_Image = (NetworkImageView) itemView.findViewById(R.id.mis_network_image);
        Network_Image.setDefaultImageResId(R.drawable.img_logo_small);
        Network_Image.setErrorImageResId(R.drawable.img_error);
        Network_Image.setAdjustViewBounds(true);
        Network_Image.setImageUrl(Card_List.get(position).getUrl(), imageLoader);

        if(flag) {
            ((TextView) itemView.findViewById(R.id.mis_txt_name)).setText(Card_List.get(position).getName());
            if (Card_List.get(position).getCost() != -1) {
                ((TextView) itemView.findViewById(R.id.mis_txt_cost)).setText("Coste: " + Card_List.get(position).getCost());
            }
            ((TextView) itemView.findViewById(R.id.mis_txt_class)).setText(Card_List.get(position).get_sort());
            ((TextView) itemView.findViewById(R.id.mis_txt_frequency)).setText(Card_List.get(position).getFrequency());
        }
        return itemView;
    }
}
