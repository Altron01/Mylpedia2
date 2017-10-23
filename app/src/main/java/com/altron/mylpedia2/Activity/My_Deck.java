package com.altron.mylpedia2.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.altron.mylpedia2.DB.DB_Handler;
import com.altron.mylpedia2.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class My_Deck extends AppCompatActivity {

    @BindView(R.id.dp_list_decks)   ListView List_Decks;
    @BindView(R.id.dp_btn_new_deck) Button Btn_New_Deck;
    List<String> List_Deck_Names;
    DB_Handler db_handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_deck);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db_handler = DB_Handler.getInstance(My_Deck.this);

        List_Deck_Names = new ArrayList<>(db_handler.get_deck_names());

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, List_Deck_Names);
        List_Decks.setAdapter(arrayAdapter);

        List_Decks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                db_handler.delete_deck(List_Decks.getItemAtPosition(i).toString());
                arrayAdapter.remove(List_Decks.getItemAtPosition(i).toString());
                List_Decks.setAdapter(arrayAdapter);
                return false;
            }
        });
        List_Decks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(My_Deck.this, Deck_Creator.class);
                intent.putExtra("Name", List_Deck_Names.get(i));
                db_handler.close();
                startActivity(intent);
            }
        });
        Btn_New_Deck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_Deck.this, Deck_Creator.class);
                intent.putExtra("Name", "Error");
                db_handler.close();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db_handler.close();
    }
}
