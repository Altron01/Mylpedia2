package com.altron.mylpedia2.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.altron.mylpedia2.Adapter.Mini_Screen_Adapter;
import com.altron.mylpedia2.Misc.Custom_Classes.Card;
import com.altron.mylpedia2.Misc.Filtrator;
import com.altron.mylpedia2.Misc.JSONReader;
import com.altron.mylpedia2.Misc.Singleton;
import com.altron.mylpedia2.R;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

public class Main_Screen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.mas_txt_edition)TextView Txt_Edition;
    @BindView(R.id.mas_grid_cards) GridView Grid_Card_List;
    @BindView(R.id.Name_EditText)  EditText Edit_Name;
    Filtrator filtrator = new Filtrator();
    ImageLoader Image_Loader;

    Spinner Spn_Edition;
    Spinner Spn_Cost;
    Spinner Spn_Class;
    Spinner Spn_Strength;
    Spinner Spn_Race;

    List<Card> Card_List = new ArrayList<Card>();

    Mini_Screen_Adapter Card_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Image_Loader = Singleton.getInstance(this).getImageLoader();
        Card_List = Arrays.asList(new JSONReader().Cartas(this));
        filtrator.Odenador(Card_List);
        Card_Adapter = new Mini_Screen_Adapter(this,android.R.layout.simple_list_item_1, Card_List, Image_Loader, true);
        if(Configuration.ORIENTATION_PORTRAIT == getResources().getConfiguration().orientation) {
            Grid_Card_List.setNumColumns(1);
        }else{
            Grid_Card_List.setNumColumns(2);
        }
        Grid_Card_List.setAdapter(Card_Adapter);
        Grid_Card_List.setOnScrollListener(on_scroll_listener);
        Grid_Card_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Main_Screen.this, Big_Screen.class);
                System.out.print("Salio");
                intent.putExtra("url", Card_List.get(i).getUrl());
                intent.putExtra("name", Card_List.get(i).getName());
                intent.putExtra("_class", Card_List.get(i).get_sort());
                intent.putExtra("frequency", Card_List.get(i).getFrequency());
                intent.putExtra("edition", Card_List.get(i).getEdition());
                intent.putExtra("cost", Card_List.get(i).getCost());
                intent.putExtra("strength", Card_List.get(i).getStrength());
                intent.putExtra("ability", Card_List.get(i).getAbility());
                startActivity(intent);
            }
        });
        Edit_Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                filtrator.Restart_List();
                String Edition = Spn_Edition.getItemAtPosition(Spn_Edition.getSelectedItemPosition()).toString();
                String Cost = Spn_Cost.getItemAtPosition(Spn_Cost.getSelectedItemPosition()).toString();
                String Class = Spn_Class.getItemAtPosition(Spn_Class.getSelectedItemPosition()).toString();
                String Strength = Spn_Strength.getItemAtPosition(Spn_Strength.getSelectedItemPosition()).toString();
                String Race = Spn_Race.getItemAtPosition(Spn_Race.getSelectedItemPosition()).toString();
                String Name = Edit_Name.getText().toString();
                Card_List = filtrator.Filter(Name, Race, Integer.parseInt(Cost.equals("-------Todas-------") ? "-1" : Cost), Integer.parseInt(Strength.equals("-------Todas-------") ? "-1" : Strength), Class, Edition);

                Card_Adapter = new Mini_Screen_Adapter(Main_Screen.this, android.R.layout.simple_list_item_1, Card_List, Image_Loader, true);
                Grid_Card_List.setAdapter(Card_Adapter);
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Spn_Edition = (Spinner) navigationView.getMenu().findItem(R.id.nav_edicion).getActionView().findViewById(R.id.spn_edition);
        Spn_Cost = (Spinner) navigationView.getMenu().findItem(R.id.nav_coste).getActionView().findViewById(R.id.spn_cost);
        Spn_Class = (Spinner) navigationView.getMenu().findItem(R.id.nav_clase).getActionView().findViewById(R.id.spn_class);
        Spn_Strength = (Spinner) navigationView.getMenu().findItem(R.id.nav_fuerza).getActionView().findViewById(R.id.spn_strength);
        Spn_Race = (Spinner) navigationView.getMenu().findItem(R.id.nav_raza).getActionView().findViewById(R.id.spn_race);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtrator.Restart_List();
                String Edition = Spn_Edition.getItemAtPosition(Spn_Edition.getSelectedItemPosition()).toString();
                String Cost = Spn_Cost.getItemAtPosition(Spn_Cost.getSelectedItemPosition()).toString();
                String Class = Spn_Class.getItemAtPosition(Spn_Class.getSelectedItemPosition()).toString();
                String Strength = Spn_Strength.getItemAtPosition(Spn_Strength.getSelectedItemPosition()).toString();
                String Race = Spn_Race.getItemAtPosition(Spn_Race.getSelectedItemPosition()).toString();
                String Name = Edit_Name.getText().toString();
                Card_List = filtrator.Filter(Name, Race, Integer.parseInt(Cost.equals("-------Todas-------") ? "-1" : Cost), Integer.parseInt(Strength.equals("-------Todas-------") ? "-1" : Strength), Class, Edition);
                Card_Adapter = new Mini_Screen_Adapter(Main_Screen.this, android.R.layout.simple_list_item_1, Card_List, Image_Loader, true);
                Grid_Card_List.setAdapter(Card_Adapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
        Spn_Edition.setOnItemSelectedListener(listener);
        Spn_Cost.setOnItemSelectedListener(listener);
        Spn_Class.setOnItemSelectedListener(listener);
        Spn_Strength.setOnItemSelectedListener(listener);
        Spn_Race.setOnItemSelectedListener(listener);

        navigationView.setNavigationItemSelectedListener(this);
    }

    AbsListView.OnScrollListener on_scroll_listener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            Txt_Edition.setText(Card_List.get(firstVisibleItem).getEdition());
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.nav_my_decks:
                intent = new Intent(this, My_Deck.class);
                startActivity(intent);
                return true;
            case R.id.nav_deck_creator:
                intent = new Intent(this, Deck_Creator.class);
                intent.putExtra("Name", "Error");
                startActivity(intent);
                return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
