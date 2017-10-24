package com.altron.mylpedia2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.altron.mylpedia2.Misc.Custom_Classes.Card_Holder;
import com.altron.mylpedia2.R;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by altron on 1/19/17.
 */

public class Deck_Big_Screen_Adapter extends ArrayAdapter<Card_Holder> {

    private ImageLoader imageLoader;
    private List<Card_Holder> cardList;

    public Deck_Big_Screen_Adapter(Context context, int resources, List<Card_Holder> cardList, ImageLoader imageLoader) {
        super(context, resources, cardList);
        this.cardList = new ArrayList<>(cardList);
        this.imageLoader = imageLoader;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = convertView;
        if (itemView == null) {
            itemView =  inflater.inflate(R.layout.content_deck_big_view, parent, false);
        }

        NetworkImageView networkImage = (NetworkImageView) itemView.findViewById(R.id.db_network_image);
        networkImage.setDefaultImageResId(R.drawable.img_logo_small);
        networkImage.setErrorImageResId(R.drawable.img_error);
        networkImage.setAdjustViewBounds(true);
        networkImage.setImageUrl(cardList.get(position).card.getUrl(), imageLoader);
        TextView txtCount = (TextView) itemView.findViewById(R.id.db_txt_count);
        txtCount.setText(" x" + cardList.get(position).quantity);
        return itemView;
    }
}
