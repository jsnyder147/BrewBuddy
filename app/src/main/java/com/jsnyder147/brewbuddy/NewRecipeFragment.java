package com.jsnyder147.brewbuddy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;

import java.util.ArrayList;

public class NewRecipeFragment extends Fragment implements View.OnClickListener {

    // Instance variables for preferences
    private SharedPreferences prefs;
    private String brewerName;
    private int measurementSystem = 0;
    private int gravityUnits = 0;

    // Widget Variables
    private EditText etBatchName;
    private EditText etStyle;
    private EditText etType;
    private EditText etIBU;
    private EditText etSRM;
    private EditText etOG;
    private EditText etFG;
    private EditText etYeast;
    private Button btSave;
    private ImageButton btAddHop;
    private ImageButton btAddMalt;
    private ListView lvHops;
    private ListView lvMalts;
    private TextView tvDialogText;
    private EditText etDialogEntry;
    private TextView tvOG;
    private TextView tvFG;
    private TextView tvHops;
    private TextView tvMalts;



    // Declare instance variables
    private String batchName;
    private String style;
    private String type;
    private String yeast;
    private int IBU;
    private int SRM;
    private double ABV;
    private double OG;
    private double FG;

    private ArrayList<String> hopsList = new ArrayList<>();
    private ArrayList<String> maltsList = new ArrayList<>();




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
        View view = inflater.inflate(R.layout.fragment_new_recipe, container, false);

        // Get references to the widgets
        etBatchName = (EditText) view.findViewById(R.id.nameEditText);
        etStyle = (EditText) view.findViewById(R.id.styleEditText);
        etType = (EditText) view.findViewById(R.id.typeEditText);
        etIBU = (EditText) view.findViewById(R.id.IBUEditText);
        etSRM = (EditText) view.findViewById(R.id.SRMEditText);
        etYeast = (EditText) view.findViewById(R.id.yeastTextView);
        etOG = (EditText) view.findViewById(R.id.OGEditText);
        etFG = (EditText) view.findViewById(R.id.FGEditText);
        lvHops = (ListView) view.findViewById(R.id.hopsListView);
        lvMalts = (ListView) view.findViewById(R.id.maltsListView);
        btSave = (Button) view.findViewById(R.id.saveButton);
        btAddHop = (ImageButton) view.findViewById(R.id.addHopButton);
        btAddMalt = (ImageButton) view.findViewById(R.id.addMaltButton);
        tvOG = (TextView) view.findViewById(R.id.OGLabel);
        tvFG = (TextView) view.findViewById(R.id.FGLabel);
        tvHops = (TextView) view.findViewById(R.id.hopsLabel);
        tvMalts = (TextView) view.findViewById(R.id.maltLabel);

        // Set on Click listener
        btSave.setOnClickListener(this);
        btAddHop.setOnClickListener(this);
        btAddMalt.setOnClickListener(this);


        return view;
    }


    @Override
    public void onResume(){
        super.onResume();

        // Get preferences
        brewerName = prefs.getString("pref_brewer_name", null);
        measurementSystem = Integer.parseInt(prefs.getString("pref_system_of_measurement", "0"));
        gravityUnits = Integer.parseInt(prefs.getString("pref_gravity_units","0"));

        // Set Label for OG and FG
        if(gravityUnits == 0) {
            tvOG.setText(getActivity().getString(R.string.og_label));
            tvFG.setText(getActivity().getString(R.string.fg_label));
        } else {
            tvOG.setText(getActivity().getString(R.string.og_plato_label));
            tvFG.setText(getActivity().getString(R.string.fg_plato_label));

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveButton:
                saveRecipe();
                break;
            case R.id.addHopButton:
                dialog("Hop:");
                Log.d("Activity", "Hops:" + hopsList);
                break;
            case R.id.addMaltButton:
                dialog("Malt:");
                break;
        }
    }

    // Create Options Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){

        SettingsFragment settingsFragment = (SettingsFragment)  getFragmentManager().findFragmentById(R.id.settings_fragment);

        inflater.inflate(R.menu.fragment_new_recipe, menu);

    }

    // Option Menu Items actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
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

    private void saveRecipe(){

        // Validate that fields are completed
        if(TextUtils.isEmpty(etBatchName.getText())){
            etBatchName.requestFocus();
            etBatchName.setError( "Batch name is required!");
        } else if(TextUtils.isEmpty(etStyle.getText())){
            etStyle.requestFocus();
            etStyle.setError("Style is required");
        } else if(TextUtils.isEmpty(etType.getText())){
            etType.requestFocus();
            etType.setError("Type is required");
        } else if(TextUtils.isEmpty(etIBU.getText())){
            etIBU.requestFocus();
            etIBU.setError("IBU is required");
        } else if(TextUtils.isEmpty(etSRM.getText())){
            etSRM.requestFocus();
            etSRM.setError("SRM is required");
        } else if(TextUtils.isEmpty(etOG.getText())){
            etOG.requestFocus();
            etOG.setError("OG is required");
        } else if(TextUtils.isEmpty(etFG.getText())){
            etFG.requestFocus();
            etFG.setError("FG is required");
        } else if(TextUtils.isEmpty(etYeast.getText())){
            etYeast.requestFocus();
            etYeast.setError("Yeast is required");
        } else if(hopsList.isEmpty()){
            tvHops.setError("Hops are required");
            Toast.makeText(getActivity(), "At Least One Hop is Required", Toast.LENGTH_SHORT).show();
        } else if(maltsList.isEmpty()){
            tvMalts.setError("Hops are required");
            Toast.makeText(getActivity(), "At Least One Malt is Required", Toast.LENGTH_SHORT).show();

        // If all fields are validated add to Recipe and move to Recipe list
        } else {

            batchName = etBatchName.getText().toString();
            style = etStyle.getText().toString();
            type = etType.getText().toString();
            if (!etIBU.getText().toString().isEmpty())
                IBU = Integer.parseInt(etIBU.getText().toString());
            if (!etSRM.getText().toString().isEmpty())
                SRM = Integer.parseInt(etSRM.getText().toString());
            if (!etOG.getText().toString().isEmpty())
                OG = Double.parseDouble(etOG.getText().toString());
            if (!etFG.getText().toString().isEmpty())
                FG = Double.parseDouble(etFG.getText().toString());
            yeast = etYeast.getText().toString();

            Recipe recipe = new Recipe();
            recipe.setBatchName(batchName);
            recipe.setBatchStyle(style);
            recipe.setBatchType(type);
            recipe.setIBU(IBU);
            recipe.setSRM(SRM);
            recipe.setOG(OG);
            recipe.setFG(FG);
            Log.d("New Recipe Fragment", " Gravity Units " + gravityUnits);
            recipe.calculateABV(gravityUnits);
            Log.d("New Recipe Fragment", "OG ABVGET FRAG: " + recipe.getABV());
            recipe.setABV(recipe.calculateABV(gravityUnits));
            Log.d("New Recipe Fragment", "OG ABVGET FRAG: " + recipe.getABV());
            recipe.setYeast(yeast);
            recipe.setHops(hopsList);
            recipe.setMalts(maltsList);
            Recipe.setRecipes(recipe);

            Toast.makeText(getActivity(), "Recipe Saved", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), RecipeListActivity.class));
        }

    }

    // Display Dialog box to get Hop or Malt Item
    private void dialog(final String dialogText){

        // Create Alert Dialog
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.add_item_view, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);
        tvDialogText = (TextView) promptsView.findViewById(R.id.dialogLabel);
        etDialogEntry = (EditText) promptsView.findViewById(R.id.dialogInputEditText);
        tvDialogText.setText(dialogText);
        alertDialogBuilder.setCancelable(false).setPositiveButton("Add",
                new DialogInterface.OnClickListener(){

                    public void onClick(DialogInterface dialog, int id) {
                        if(dialogText.equals("Hop:")) {
                            // Check if user input text
                            if(TextUtils.isEmpty(etDialogEntry.getText())) {
                                etDialogEntry.setError("Hop is required, or press cancel");
                            } else {
                                hopsList.add(etDialogEntry.getText().toString());
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_hop_item_listview, hopsList);
                                lvHops.setAdapter(adapter);
                                tvHops.setError(null);
                            }

                        }
                        else {
                            // Check if user input text
                            if (TextUtils.isEmpty(etDialogEntry.getText())) {
                                etDialogEntry.setError("Malt is required, or press cancel");
                            }else{
                                maltsList.add(etDialogEntry.getText().toString());
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_malt_item_listview, maltsList);
                                lvMalts.setAdapter(adapter);
                                tvMalts.setError(null);
                            }
                        }
                    }
                }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
        });


        // Show Alert Dialog with keyboard
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }

}
