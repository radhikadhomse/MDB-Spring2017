package com.example.datarsd1.pokedex;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener{

    RecyclerView searchResults;
    private Menu optionsMenu;
    private ArrayList<Pokedex.Pokemon> pokemonList;
    private PokeAdapter pokeAdapter;
    private boolean linear;
    private String filterType1, filterType2;
    private double currMinHp, currMinAtk, currMinDef;
    FloatingActionButton filters, typeFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        filterType1 = "None";
        filterType2 = "None";
        currMinHp = 0.0;
        currMinAtk = 0.0;
        currMinDef = 0.0;

        searchResults = (RecyclerView) findViewById(R.id.searchresultsview);
        searchResults.setLayoutManager(new LinearLayoutManager(this));
        filters = (FloatingActionButton) findViewById(R.id.filter);
        filters.setOnClickListener(this);
        typeFilters = (FloatingActionButton) findViewById(R.id.typefilter);
        typeFilters.setOnClickListener(this);

        Pokedex dex = new Pokedex();
        pokemonList = dex.getPokemon();

        pokeAdapter = new PokeAdapter(getApplicationContext(), pokemonList);
        searchResults.setAdapter(pokeAdapter);

        linear = true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);

        optionsMenu = menu;

        SearchView pokemonNameSearch = (SearchView) menu.findItem(R.id.name_search).getActionView();

        pokemonNameSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Pokedex.Pokemon> searchResultList = new ArrayList<Pokedex.Pokemon>();
                for (int i = 0 ; i < pokemonList.size(); i++)
                {
                    if (pokemonList.get(i).name.toLowerCase().startsWith(newText.toLowerCase()) || pokemonList.get(i).number.startsWith(newText))
                        searchResultList.add(pokemonList.get(i));
                }
                pokeAdapter.updateList(searchResultList);
                pokeAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.randomshuffle:
                ArrayList<Pokedex.Pokemon> randomResultsList = new ArrayList<Pokedex.Pokemon>();
                for (int i = 0 ; randomResultsList.size() < 20 ; i++)
                    randomResultsList.add(i,pokemonList.get((int)(Math.random()*pokemonList.size())));
                pokeAdapter.updateList(randomResultsList);
                pokeAdapter.notifyDataSetChanged();
                break;
            case R.id.layouttoggle:
                if (linear) {
                    searchResults.setLayoutManager(new GridLayoutManager(this, 2));
                    linear = false;
                }
                else {
                    searchResults.setLayoutManager(new LinearLayoutManager(this));
                    linear = true;
                }
                break;
        }
        return true;
    }

    public void filterByMinStat(double minHP, double minAtk, double minDef)
    {
        ArrayList<Pokedex.Pokemon> filterList = new ArrayList<Pokedex.Pokemon>();
        for (int i = 0 ; i < pokemonList.size(); i++)
        {
            if (Double.parseDouble(pokemonList.get(i).hp) > minHP && Double.parseDouble(pokemonList.get(i).attack) > minAtk && Double.parseDouble(pokemonList.get(i).defense) > minDef)
                filterList.add(pokemonList.get(i));
        }
        pokeAdapter.updateList(filterList);
        pokeAdapter.notifyDataSetChanged();
        if (filterList.size() == 0)
            Toast.makeText(getApplicationContext(), "No results found!", Toast.LENGTH_SHORT).show();
    }

    public void filterByType(String type1, String type2)
    {
        ArrayList<Pokedex.Pokemon> typeFilterList = new ArrayList<Pokedex.Pokemon>();
        for (int i = 0 ; i < pokemonList.size(); i++)
        {
            if (type1.equalsIgnoreCase("None") && type2.equalsIgnoreCase("None"))
                typeFilterList.add(pokemonList.get(i));
            if (type2.equalsIgnoreCase("None") && !type1.equalsIgnoreCase("None"))
                if (type1.equalsIgnoreCase(pokemonList.get(i).type))
                    typeFilterList.add(pokemonList.get(i));
            if (type1.equalsIgnoreCase("None") && !type2.equalsIgnoreCase("None"))
                if (type2.equalsIgnoreCase(pokemonList.get(i).type))
                    typeFilterList.add(pokemonList.get(i));
            if (type1.equalsIgnoreCase(pokemonList.get(i).type) && type2.equalsIgnoreCase(pokemonList.get(i).type2))
                typeFilterList.add(pokemonList.get(i));
        }
        pokeAdapter.updateList(typeFilterList);
        pokeAdapter.notifyDataSetChanged();
        if (typeFilterList.size() == 0)
            Toast.makeText(getApplicationContext(), "No results found!", Toast.LENGTH_SHORT).show();
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.filter:
                LayoutInflater li = LayoutInflater.from(this);
                View promptsView = li.inflate(R.layout.filter_input, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                alertDialogBuilder.setView(promptsView);

                final EditText hpInput = (EditText) promptsView.findViewById(R.id.popuphp);
                final EditText attackInput = (EditText) promptsView.findViewById(R.id.popupatk);
                final EditText defenseInput = (EditText) promptsView.findViewById(R.id.popupdef);
                hpInput.setText("" + currMinHp);
                attackInput.setText("" + currMinAtk);
                defenseInput.setText("" + currMinDef);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Filter",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // if filtering by numerical attributes, not filtering by type
                                        filterType1 = "None";
                                        filterType2 = "None";
                                        currMinHp = Double.parseDouble(hpInput.getText().toString());
                                        currMinAtk = Double.parseDouble(attackInput.getText().toString());
                                        currMinDef = Double.parseDouble(defenseInput.getText().toString());

                                        filterByMinStat(currMinHp, currMinAtk, currMinDef);
                                    }})
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }});

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;

            case R.id.typefilter:
                LayoutInflater tli = LayoutInflater.from(this);
                View typePromptsView = tli.inflate(R.layout.type_input, null);
                final Spinner type1Spinner = (Spinner) typePromptsView.findViewById(R.id.type1spinner);
                final Spinner type2Spinner = (Spinner) typePromptsView.findViewById(R.id.type2spinner);

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.pokemontypes, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                type1Spinner.setAdapter(adapter);
                type2Spinner.setAdapter(adapter);
                type1Spinner.setSelection(adapter.getPosition(filterType1));
                type2Spinner.setSelection(adapter.getPosition(filterType2));

                AlertDialog.Builder typeAlertDialogBuilder = new AlertDialog.Builder(this);
                typeAlertDialogBuilder.setView(typePromptsView);
                typeAlertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Filter",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // if filtering by type, do not filter by numerical attributes
                                        currMinAtk = 0.0;
                                        currMinDef = 0.0;
                                        currMinHp = 0.0;

                                        filterType1 = type1Spinner.getSelectedItem().toString();
                                        filterType2 = type2Spinner.getSelectedItem().toString();
                                        filterByType(type1Spinner.getSelectedItem().toString(),type2Spinner.getSelectedItem().toString());
                                    }})
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }});

                AlertDialog typeAlertDialog = typeAlertDialogBuilder.create();
                typeAlertDialog.show();
                break;
        }
    }

}
