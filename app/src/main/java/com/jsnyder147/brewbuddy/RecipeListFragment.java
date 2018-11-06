package com.jsnyder147.brewbuddy;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class RecipeListFragment extends Fragment {

    // Instance variables for preferences
    private SharedPreferences prefs;
    private String brewerName;
    private int measurementSystem = 0;
    private int gravityUnits = 0;

    // Test get ListView
    private ListView recipeList;

    // Declare Instance Variables


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Set default values in preferences
        PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);

        // Get shared preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // Turn on Options Menu
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        // Get references to the widget
        recipeList = (ListView) view.findViewById(R.id.recipeListView);

        // Set up a test list
       // setUpList();

        // Display recipes in list view
        displayList();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("Recipe List Fragment", "Entered list");
        // Display recipes in list view
        displayList();

        // Get preferences
        brewerName = prefs.getString("pref_brewer_name", null);
        measurementSystem = Integer.parseInt(prefs.getString("pref_system_of_measurement", "0"));
        gravityUnits = Integer.parseInt(prefs.getString("pref_gravity_units","0"));

    }
    // Create Options Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){

        SettingsFragment settingsFragment = (SettingsFragment)  getFragmentManager().findFragmentById(R.id.settings_fragment);

        inflater.inflate(R.menu.fragment_recipe_list, menu);

    }

    // Option Menu Items actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_new_recipe:
                startActivity(new Intent(getActivity(), NewRecipeActivity.class));
                return true;
            case R.id.menu_help:
                startActivity(new Intent(getActivity(), HelpActivity.class));
                return true;
            case R.id.menu_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Display Recipes in ListView
    private void displayList(){
        // List of Recipe Names to Display in List
        ArrayList<String> recipeNames = new ArrayList<>();

        // Add recipe names if they aren't already in list
        for(Recipe recipe:Recipe.getRecipes()){
            if(!recipeNames.contains(recipe.getBatchName()))
                recipeNames.add(recipe.getBatchName());
        }

        // Sort in Ascending order
        Collections.sort(recipeNames);

        // Set up array adapter for recipe names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_recipe_item_listview,recipeNames);
        recipeList.setAdapter(adapter);

        // Set click Listener
        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                Intent viewRecipe = new Intent(getActivity(), RecipeViewerActivity.class);
                viewRecipe.putExtra("position", position);
                startActivity(viewRecipe);
            }
        });
    }

    private void setUpList() {
        String[] names = {"All Day IPA", "Zombie Dust", "Dark Lord"};
        String[] styles = {"Session IPA", "IPA", "Imperial Stout"};
        String[] types = {"All-Grain", "All-Grain", "Extract"};
        int[] IBUs = {45, 63, 39};
        int[] SRMs = {10, 15, 37};
        double[] ABVs = {5, 7.3, 11.4};
        String[] yeast = {"White Labs 2071", "Imperial Yeast A01", "WYeast American Ale 1272"};
        ArrayList<String> hop1 = new ArrayList<String>();
        hop1.add("Citra");
        hop1.add("Cascade");
        ArrayList<String> malt1 = new ArrayList<String>();
        malt1.add("2-Row");
        malt1.add("60L");
        ArrayList<String> hop2 = new ArrayList<String>();
        hop2.add("Citra");
        ArrayList<String> malt2 = new ArrayList<String>();
        malt2.add("2-Row");
        malt2.add("80L");
        malt2.add("Wheat");
        ArrayList<String> hop3 = new ArrayList<String>();
        hop3.add("Chinook");
        hop3.add("Cascade");
        hop3.add("Spaten");
        ArrayList<String> malt3 = new ArrayList<String>();
        malt3.add("2 row");
        malt3.add("Munich");
        malt3.add("Roasted Barley");
        malt3.add("Chocolate Malt");
        malt3.add("Crystal 120L");


        ArrayList<ArrayList> hopsList = new ArrayList<>();
        hopsList.add(hop1);
        hopsList.add(hop2);
        hopsList.add(hop3);
        ArrayList<ArrayList> maltsList = new ArrayList<>();
        maltsList.add(malt1);
        maltsList.add(malt2);
        maltsList.add(malt3);

        if (Recipe.getRecipes().isEmpty()) {
            for (int i = 0; i < 3; i++) {
                Recipe recipe = new Recipe();
                recipe.setBatchName(names[i]);
                recipe.setBatchStyle(styles[i]);
                recipe.setBatchType(types[i]);
                recipe.setIBU(IBUs[i]);
                recipe.setSRM(SRMs[i]);
                recipe.setABV(ABVs[i]);
                recipe.setYeast(yeast[i]);
                recipe.setHops(hopsList.get(i));
                recipe.setMalts(maltsList.get(i));
                Recipe.setRecipes(recipe);
            }
        }
    }

}
