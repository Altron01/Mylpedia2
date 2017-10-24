package com.altron.mylpedia2.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.altron.mylpedia2.Adapter.Deck_Big_Screen_Adapter;
import com.altron.mylpedia2.Adapter.Mini_Screen_Adapter;
import com.altron.mylpedia2.DB.DB_Handler;
import com.altron.mylpedia2.Misc.Custom_Classes.Card;
import com.altron.mylpedia2.Misc.Custom_Classes.Card_Holder;
import com.altron.mylpedia2.Misc.Custom_Classes.Deck;
import com.altron.mylpedia2.Misc.Custom_Classes.Deck_Cards;
import com.altron.mylpedia2.Misc.Filtrator;
import com.altron.mylpedia2.Misc.JSONReader;
import com.altron.mylpedia2.Misc.Singleton;
import com.altron.mylpedia2.R;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.ButterKnife;

public class Deck_Creator extends AppCompatActivity {

    @BindView(R.id.dc_spn_edition)    Spinner Spn_Edition;
    @BindView(R.id.dc_spn_class)      Spinner Spn_Class;
    @BindView(R.id.dc_grid_all_cards) GridView Grid_Cards;
    @BindView(R.id.dc_btn_view_deck)  Button Btn_Deck_View;
    @BindView(R.id.dc_edit_deck_name) EditText Edit_Deck_Name;
    @BindView(R.id.dc_txt_cards)      TextView Txt_Deck_Size;
    GridView Deck_Cards;

    List<Card> Card_List = new ArrayList<Card>();
    ImageLoader Image_Loader;
    Deck_Big_Screen_Adapter deck_big_screen_adapter;
    final Context context = this;
    Filtrator _Filtrator;
    Mini_Screen_Adapter Card_Adapter;
    DB_Handler _DB_Handler;

    Deck_Cards deck;
    Deck _Deck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_creator);
        ButterKnife.bind(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Card_List = Arrays.asList(new JSONReader().Cartas(context));
        Image_Loader = Singleton.getInstance(context).getImageLoader();

        _Filtrator = new Filtrator();
        Card_Adapter = new Mini_Screen_Adapter(context,android.R.layout.simple_list_item_1, Card_List, Image_Loader, false);
        _DB_Handler = DB_Handler.getInstance(context);

        deck = new Deck_Cards();

        _Filtrator.Odenador(Card_List);
        String name = getIntent().getStringExtra("Name");
        if(!(name.equals("Error"))){
            _Deck = _DB_Handler.deck_search(name);
            deck = _DB_Handler.card_search(_Deck.getId(),  Card_List);
            Edit_Deck_Name.setText(_Deck.getName());
        }else{
            _Deck = new Deck("", 1000);
        }
        Txt_Deck_Size.setText(Txt_Deck_Size.getText() + Integer.toString (deck.size) + "/50-" + Integer.toString(50 - deck.size) + " Oros");
        Grid_Cards.setAdapter(Card_Adapter);

        Btn_Deck_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.content_deck_view);
                deck_big_screen_adapter = new Deck_Big_Screen_Adapter(Deck_Creator.this, android.R.layout.simple_list_item_1, deck.cards, Image_Loader);
                Deck_Cards = (GridView) dialog.findViewById(R.id.dv_grid_cards);
                Deck_Cards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        switch (deck.cards.get(i).quantity){
                            case 1:
                                deck.cards.remove(i);
                                break;
                            case 2:
                            case 3:
                                deck.cards.get(i).quantity = deck.cards.get(i).quantity - 1;
                                break;
                        }
                        deck.size = deck.size - 1;
                        Txt_Deck_Size.setText(Integer.toString (deck.size) + "/50-" + Integer.toString(50 - deck.size) + " Oros");
                        deck_big_screen_adapter = new Deck_Big_Screen_Adapter(Deck_Creator.this, android.R.layout.simple_list_item_1, deck.cards, Image_Loader);
                        Deck_Cards.setAdapter(deck_big_screen_adapter);
                    }
                });
                Deck_Cards.setAdapter(deck_big_screen_adapter);
                TextView Deck_Name = (TextView) dialog.findViewById(R.id.dv_txt_deck_name);
                if(Edit_Deck_Name.getText().toString().equals("")){
                    Deck_Name.setText("Sin Nombre");
                }else{
                    Deck_Name.setText(Edit_Deck_Name.getText());
                }
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);

                int width, height;
                width = metrics.widthPixels;
                height = metrics.heightPixels;

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = (int) (width*0.95);
                lp.height = (int) (height*0.9);
                dialog.getWindow().setAttributes(lp);

                dialog.show();
            }
        });

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String Race = "-------Todas-------";
                String Edition_Selected = Spn_Edition.getItemAtPosition(Spn_Edition.getSelectedItemPosition()).toString();
                String Class_Selected = Spn_Class.getItemAtPosition(Spn_Class.getSelectedItemPosition()).toString();
                Card_List = new ArrayList<Card>(_Filtrator.Filter("", Race, -1, -1, Class_Selected, Edition_Selected));
                Card_Adapter = new Mini_Screen_Adapter(Deck_Creator.this, android.R.layout.simple_list_item_1, Card_List, Image_Loader, false);
                _Filtrator.Restart_List();
                Grid_Cards.setAdapter(Card_Adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        Spn_Class.setOnItemSelectedListener(listener);
        Spn_Edition.setOnItemSelectedListener(listener);

        Grid_Cards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(deck.size >= 50){
                    Toast.makeText(Deck_Creator.this, "Ya tienes 50 cards", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                boolean not_found = true;
                for(Card_Holder card : deck.cards){
                    if(card.card.getId() == Card_List.get(i).getId()){
                        not_found = false;
                        if(card.quantity < 3){
                            card.quantity = card.quantity + 1;
                            deck.size = deck.size + 1;
                            Txt_Deck_Size.setText("Cartas: " + Integer.toString (deck.size) + "/50-" + Integer.toString(50 - deck.size) + " Oros");
                            Toast.makeText(Deck_Creator.this, "Añadido " + Card_List.get(i).getName(), Toast.LENGTH_SHORT)
                                    .show();
                        }else{
                            Toast.makeText(Deck_Creator.this, "Ya tienes el máximo de copias", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        break;
                    }
                }
                if (not_found){
                    deck.cards.add(new Card_Holder(Card_List.get(i), 1));
                    deck.size = deck.size + 1;
                    Txt_Deck_Size.setText("Cartas: " + Integer.toString (deck.size) + "/50-" + Integer.toString(50 - deck.size) + " Oros");
                    Toast.makeText(Deck_Creator.this, "Añadido " + Card_List.get(i).getName(), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    @OnClick(R.id.dc_btn_finish)
    public void End_Deck(){
        if(Edit_Deck_Name.getText().toString().equals("")){
            Toast.makeText(Deck_Creator.this, "El deck necesita un Nombre", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if(!(_Deck.getName().equals(""))){
            _DB_Handler.insert_card(deck.cards, _Deck.getId());
        }else{
            _Deck.setName(Edit_Deck_Name.getText().toString());
            _Deck.setId(_DB_Handler.deck_count()+1);
            _DB_Handler.insert_deck(_Deck);
            _DB_Handler.insert_card(deck.cards, _Deck.getId());
        }
        _DB_Handler.close();
        Intent intent = new Intent(Deck_Creator.this, My_Deck.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _DB_Handler.close();
    }
}
