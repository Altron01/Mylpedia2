package com.altron.mylpedia2.Adapter;

import android.content.Context;
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
    private List<Card> cardList;
    private boolean flag;

    public Mini_Screen_Adapter(Context context, int resources, List<Card> cardList, ImageLoader imageLoader, boolean _flag) {
        super(context, resources, cardList);
        this.cardList = new ArrayList<>(cardList);
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
        Network_Image.setImageUrl(cardList.get(position).getUrl(), imageLoader);

        if(flag) {
            ((TextView) itemView.findViewById(R.id.mis_txt_name)).setText(cardList.get(position).getName());
            if (cardList.get(position).getCost() != -1) {
                ((TextView) itemView.findViewById(R.id.mis_txt_cost)).setText("Coste: " + cardList.get(position).getCost());
            }
            ((TextView) itemView.findViewById(R.id.mis_txt_class)).setText(cardList.get(position).get_sort());
            ((TextView) itemView.findViewById(R.id.mis_txt_frequency)).setText(cardList.get(position).getFrequency());
        }
        return itemView;
    }
}
