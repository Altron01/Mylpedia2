package com.altron.mylpedia2.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.altron.mylpedia2.Activity.Deck_Creator;
import com.altron.mylpedia2.Misc.Custom_Classes.Card;
import com.altron.mylpedia2.Misc.Custom_Classes.Card_Holder;
import com.altron.mylpedia2.Misc.Custom_Classes.Deck;
import com.altron.mylpedia2.Misc.Custom_Classes.Deck_Cards;

import java.util.ArrayList;
import java.util.List;

public class DB_Handler extends SQLiteOpenHelper {

        private static DB_Handler mInstance;

        private static final String CARD_TABLE = "Cards";
        private static final String DECK_TABLE = "Decks";

        private static final String CARD_ID = "card_id";
        private static final String CARD_QUANTITY = "card_quantity";
        private static final String DECK_ID = "deck_id";
        private static final String DECK_NAME = "deck_name";


    private static final String CREATE_CARD_TABLE = "CREATE TABLE IF NOT EXISTS " + CARD_TABLE
            + "("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1, "
            + CARD_ID + " INTEGER NOT NULL, "
            + CARD_QUANTITY + " INTEGER NOT NULL, "
            + DECK_ID + " INTEGER NOT NULL"
            + ");";

    private static final String CREATE_DECK_TABLE = "CREATE TABLE IF NOT EXISTS " + DECK_TABLE
            + "("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1, "
            + DECK_ID + " INTEGER NOT NULL, "
            + DECK_NAME + " TEXT NOT NULL"
            + ");";

    private static final String BORRAR_TABLA_CARTAS = "DROP TABLE " + CARD_TABLE + ";";
    private static final String BORRAR_TABLA_MAZOS = "DROP TABLE " + DECK_TABLE + ";";

    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "MylPedia.db";

    private DB_Handler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    public static synchronized DB_Handler getInstance(Context context){
        if(mInstance == null){
            mInstance = new DB_Handler(context.getApplicationContext());
        }
        return mInstance;
    }

    public void insert_card(List<Card_Holder> deck, int deck_id){
        SQLiteDatabase bd =  getWritableDatabase();
        for(Card_Holder card : deck){
            ContentValues values = new ContentValues();
            values.put(CARD_ID, card.card.getId());
            values.put(CARD_QUANTITY, card.quantity);
            values.put(DECK_ID, deck_id);
            bd.insert(CARD_TABLE, null, values);
        }
        bd.close();
    }
    public void insert_deck(Deck deck){
        SQLiteDatabase bd = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DECK_NAME, deck.getName());
        values.put(DECK_ID, deck.getId());
        bd.insert(DECK_TABLE, null, values);
        bd.close();
    }

    public ArrayList<String> get_deck_names(){
        String busqueda = "SELECT * FROM " + DECK_TABLE;
        Cursor cursor = getReadableDatabase().rawQuery(busqueda, null);
        ArrayList<String> names = new ArrayList<>();
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(DECK_NAME));
            names.add(name);
        }
        cursor.close();
        return names;
    }

    public Deck deck_search(String name){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT " + DECK_ID + " FROM " + DECK_TABLE + " WHERE " + DECK_NAME + " = ?", new String[]{name});
        Deck deck = new Deck();
        while (cursor.moveToNext()){
            int I = cursor.getInt(cursor.getColumnIndex(DECK_ID));
            deck = new Deck(name, I);
        }
        cursor.close();
        return deck;
    }

    public Deck_Cards card_search(int deck_id, List<Card> cards){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + CARD_TABLE + " WHERE " + DECK_ID + " = ?", new String[]{Integer.toString(deck_id)});
        Deck_Cards deck = new Deck_Cards();
        List<Card_Holder> deck_cards = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(CARD_ID));
            int quantity = cursor.getInt(cursor.getColumnIndex(CARD_QUANTITY));
            deck.size = deck.size + quantity;
            deck_cards.add(new Card_Holder(cards.get(id), quantity));
        }
        cursor.close();
        SQLiteDatabase bd = getWritableDatabase();
        bd.delete(CARD_TABLE, DECK_ID + " = ?", new String[]{Integer.toString(deck_id)});
        bd.close();
        deck.cards = deck_cards;
        return deck;
    }

    public int deck_count(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + DECK_TABLE, null);
        int cant = cursor.getCount();
        cursor.close();
        return cant;
    }

    public void delete_deck(String name){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT " + DECK_ID + " FROM " + DECK_TABLE + " WHERE " + DECK_NAME + " = ?", new String[]{name});
        cursor.moveToFirst();
        int id;
        do{
            id = cursor.getInt(cursor.getColumnIndex(DECK_ID));
        }while(cursor.moveToNext());
        cursor.close();
        SQLiteDatabase bd = getReadableDatabase();
        bd.delete(DECK_TABLE, DECK_ID + " = ?", new String[]{Integer.toString(id)});
        bd.close();
        bd = getReadableDatabase();
        bd.delete(CARD_TABLE, DECK_ID + " = ?", new String[]{Integer.toString(id)});
        bd.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CARD_TABLE);
        sqLiteDatabase.execSQL(CREATE_DECK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(BORRAR_TABLA_CARTAS);
        sqLiteDatabase.execSQL(BORRAR_TABLA_MAZOS);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(BORRAR_TABLA_CARTAS);
        sqLiteDatabase.execSQL(BORRAR_TABLA_MAZOS);
        onCreate(sqLiteDatabase);
    }
}