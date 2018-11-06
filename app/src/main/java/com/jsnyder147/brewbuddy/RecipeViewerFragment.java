package com.jsnyder147.brewbuddy;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecipeViewerFragment extends Fragment {

    private int position;

    private TextView tvRecipeName;
    private TextView tvBrewerName;
    private TextView tvStyleName;
    private TextView tvTypeName;
    private TextView tvIBU;
    private TextView tvSRM;
    private TextView tvABV;
    private TextView tvNotes;
    private TextView tvYeast;
    private ListView lvHops;
    private ListView lvMalts;


    // Instance variables for preferences
    private SharedPreferences prefs;
    private String brewerName;
    private int measurementSystem = 0;
    private int gravityUnits = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Set default values in preferences
        PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);

        // Get shared preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // Turn on Options Menu
        setHasOptionsMenu(true);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_viewer, container, false);

        Intent intent = getActivity().getIntent();

        tvRecipeName = (TextView) view.findViewById(R.id.recipeName);
        tvBrewerName = (TextView) view.findViewById(R.id.brewerName);
        tvStyleName = (TextView) view.findViewById(R.id.recipeStyle);
        tvTypeName = (TextView) view.findViewById(R.id.recipeType);
        tvNotes = (TextView) view.findViewById(R.id.notesEditText);
        tvIBU = (TextView) view.findViewById(R.id.IBU);
        tvSRM = (TextView) view.findViewById(R.id.SRM);
        tvABV = (TextView) view.findViewById(R.id.ABV);
        tvYeast = (TextView) view.findViewById(R.id.yeastTextView);
        lvHops = (ListView) view.findViewById(R.id.hopsDisplayListView);
        lvMalts = (ListView) view.findViewById(R.id.maltsDisplayListView);

        // Get position of Recipe in recipe list from intent extra
        position = intent.getIntExtra("position", 0);

        // Display Recipe Information
        setText();

        tvNotes.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                    Recipe.getRecipes().get(position).setNotes(s.toString());
                else {
                    Recipe.getRecipes().get(position).setNotes(null);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();



        // Get preferences
        brewerName = prefs.getString("pref_brewer_name", null);
        measurementSystem = Integer.parseInt(prefs.getString("pref_system_of_measurement", "0"));
        gravityUnits = Integer.parseInt(prefs.getString("pref_gravity_units", "0"));
        setText();


    }

    // Create Options Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        SettingsFragment settingsFragment = (SettingsFragment) getFragmentManager().findFragmentById(R.id.settings_fragment);

        inflater.inflate(R.menu.fragment_new_recipe, menu);

    }

    // Option Menu Items actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_recipe_builder:
                startActivity(new Intent(getActivity(), RecipeListActivity.class));
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

        private void setText() {
            Recipe.getRecipes().get(position).calculateABV(gravityUnits);
            tvRecipeName.setText(Recipe.getRecipes().get(position).getBatchName());
            tvBrewerName.setText(prefs.getString("pref_brewer_name", null));
            tvStyleName.setText(Recipe.getRecipes().get(position).getBatchStyle());
            tvTypeName.setText(Recipe.getRecipes().get(position).getBatchType());
            tvIBU.setText(Integer.toString(Recipe.getRecipes().get(position).getIBU()));
            tvSRM.setText(Integer.toString(Recipe.getRecipes().get(position).getSRM()));
            tvABV.setText(Recipe.getRecipes().get(position).getABV());
            tvYeast.setText(Recipe.getRecipes().get(position).getYeast());
            if(Recipe.getRecipes().get(position).getNotes() != null){
                tvNotes.setText(Recipe.getRecipes().get(position).getNotes());
            }

            ArrayList<String> hops = new ArrayList<>(Recipe.getRecipes().get(position).getHops());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_hop_item_listview, hops);
            lvHops.setAdapter(adapter);
            ArrayList<String> malts = new ArrayList<>(Recipe.getRecipes().get(position).getMalts());
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), R.layout.activity_malt_item_listview, malts);
            lvMalts.setAdapter(adapter1);

        }
}

