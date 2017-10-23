package com.altron.mylpedia2.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.altron.mylpedia2.Misc.Singleton;
import com.altron.mylpedia2.R;
import com.android.volley.toolbox.NetworkImageView;

public class Big_Screen extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_big_screen);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Bundle extras = getIntent().getExtras();

            NetworkImageView Network_Image = (NetworkImageView) findViewById(R.id.bs_network_image);
            Network_Image.setDefaultImageResId(R.drawable.img_logo_small);
            Network_Image.setErrorImageResId(R.drawable.img_error);
            Network_Image.setAdjustViewBounds(true);
            Network_Image.setImageUrl(extras.getString("url"), Singleton.getInstance(this).getImageLoader());

            ((TextView) findViewById(R.id.bs_txt_name)).setText(extras.getString("name"));
            ((TextView) findViewById(R.id.bs_txt_class)).setText("Tipo: " + extras.getString("_class"));
            ((TextView) findViewById(R.id.bs_txt_frequency)).setText("Frecuencia: " + extras.getString("frequency"));
            ((TextView) findViewById(R.id.bs_txt_edition)).setText("Edición: " + extras.getString("edition"));
            ((TextView) findViewById(R.id.bs_txt_cost)).setText("Coste: " + Integer.toString(extras.getInt("cost")));

            TextView Txt_Strength = (TextView) findViewById(R.id.bs_txt_strength);
            if(extras.getInt("strength") != -1)
                Txt_Strength.setText("Fuerza: " + Integer.toString(extras.getInt("strength")));
            else
                Txt_Strength.setVisibility(View.GONE);

            ((TextView) findViewById(R.id.bs_txt_ability_text)).setText(extras.getString("ability"));
        }

}