package com.altron.mylpedia2.Misc;

import android.content.Context;

import com.altron.mylpedia2.Misc.Custom_Classes.Card;
import com.altron.mylpedia2.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONReader {
    public Card[] Cartas(Context context){
        InputStream inputStream = context.getResources().openRawResource(R.raw.card_list);
        Gson gson = new GsonBuilder().create();
        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputreader);
        Card[] cards = gson.fromJson(reader, Card[].class);
        return cards;
    }
}