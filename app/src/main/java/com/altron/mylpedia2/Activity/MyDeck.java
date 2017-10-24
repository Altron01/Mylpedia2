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

    @BindView(R.id.dp_list_decks)   ListView listDecks;
    @BindView(R.id.dp_btn_new_deck) Button btnNewDeck;
    List<String> listDeckNames;
    DB_Handler dbHandler;

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

        dbHandler = DB_Handler.getInstance(My_Deck.this);

        listDeckNames = new ArrayList<>(dbHandler.get_deck_names());

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listDeckNames);
        listDecks.setAdapter(arrayAdapter);

        listDecks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                dbHandler.delete_deck(listDecks.getItemAtPosition(i).toString());
                arrayAdapter.remove(listDecks.getItemAtPosition(i).toString());
                listDecks.setAdapter(arrayAdapter);
                return false;
            }
        });
        listDecks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(My_Deck.this, DeckCreator.class);
                intent.putExtra("Name", listDeckNames.get(i));
                dbHandler.close();
                startActivity(intent);
            }
        });
        btnNewDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_Deck.this, DeckCreator.class);
                intent.putExtra("Name", "Error");
                dbHandler.close();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHandler.close();
    }
}
