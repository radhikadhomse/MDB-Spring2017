package com.example.datarsd1.pokedex;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    private Pokedex.Pokemon currentPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView nameView, speciesView, attackView, defenseView, hpView;
        ImageView profilePic;

        nameView = (TextView) findViewById(R.id.profilename); speciesView = (TextView) findViewById(R.id.profilespecies);
        profilePic = (ImageView) findViewById(R.id.profileimage);

        nameView.setText("#"+getIntent().getStringExtra("number")+": "+getIntent().getStringExtra("name"));
        speciesView.setText("the "+getIntent().getStringExtra("species"));

        attackView = (TextView) findViewById(R.id.profileatk);
        attackView.setText("Attack: "+getIntent().getStringExtra("attack"));
        defenseView = (TextView) findViewById(R.id.profiledef);
        defenseView.setText("Defense: "+getIntent().getStringExtra("defense"));
        hpView = (TextView) findViewById(R.id.profilehp);
        hpView.setText("HitPoints: "+getIntent().getStringExtra("hp"));

        Glide.with(this)
                .load("http://img.pokemondb.net/artwork/" + getIntent().getStringExtra("name").replaceAll(" ", "").toLowerCase() + ".jpg")
                .into(profilePic);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
                search.putExtra(SearchManager.QUERY, (getIntent().getStringExtra("name")));
                startActivity(search);
            }
        });
    }
}
