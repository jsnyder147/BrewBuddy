package com.jsnyder147.brewbuddy;

import android.util.Log;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

public class Recipe implements Comparable<Recipe>{

    // Declare instance Variables
    private String batchName;
    private String batchStyle;
    private String batchType;
    private int IBU;
    private int SRM;
    private double ABV;
    private double OG;
    private double FG;
    private String yeast;
    private String notes;
    private ArrayList<String> hops = new ArrayList<>();
    private ArrayList<String> malts = new ArrayList<>();

    private static ArrayList<Recipe> recipes= new ArrayList<>();

    public Recipe(){

    }

    /*****************************************
        Getter and Setter for Batch Name
     *****************************************/

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    /*****************************************
        Getter and Setter for Batch Style
     *****************************************/

    public String getBatchStyle() {
        return batchStyle;
    }

    public void setBatchStyle(String batchStyle) {
        this.batchStyle = batchStyle;
    }

    /*****************************************
        Getter and Setter for Batch Type
     *****************************************/

    public String getBatchType() {
        return batchType;
    }

    public void setBatchType(String batchType) {
        this.batchType = batchType;
    }

    /*****************************************
        Getter and Setter for IBU
     *****************************************/

    public int getIBU() {
        return IBU;
    }

    public void setIBU(int IBU) {
        this.IBU = IBU;
    }

    /*****************************************
        Getter and Setter for SRM
     *****************************************/

    public int getSRM() {
        return SRM;
    }

    public void setSRM(int SRM) {
        this.SRM = SRM;
    }

    /*****************************************
        Getter and Setter for ABV
     *****************************************/

    public String getABV() {
        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMinimumFractionDigits(1);
        String abv = percent.format(ABV/100);
        return abv;
    }

    public void setABV(double ABV) { this.ABV = ABV;
    }

    /*****************************************
        Getter and Setter for OG
     *****************************************/

    public double getOG() {return OG;}

    public void setOG(double OG) {this.OG = OG;}

    /*****************************************
        Getter and Setter for FG
     *****************************************/

    public double getFG() {return FG;}

    public void setFG(double FG) {this.FG = FG;}

    /*****************************************
        Getter and Setter for Yeast
     *****************************************/

    public String getYeast() {
        return yeast;
    }

    public void setYeast(String yeast) {
        this.yeast = yeast;
    }

    /*****************************************
        Getter and Setter for Hops
     *****************************************/

    public ArrayList<String> getHops() {
        return hops;
    }

    public void setHops(ArrayList<String> hops) {
        this.hops = hops;
    }

    /*****************************************
        Getter and Setter for Malts
     *****************************************/

    public ArrayList<String> getMalts() {
        return malts;
    }

    public void setMalts(ArrayList<String> malts) {
        this.malts = malts;
    }

    /*****************************************
         Getter and Setter for Recipes
     *****************************************/

    public static ArrayList<Recipe> getRecipes(){
        return recipes;
    }

    public static void setRecipes(Recipe recipe){
        recipes.add(recipe);
        Collections.sort(recipes);
    }

    /*****************************************
        Getter and Setter for Notes
     *****************************************/

    public String getNotes() { return notes;}

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public double calculateABV(int measurementType) {
        if (measurementType == 0) {
            //this.ABV = (OG - FG) * 131.25;
            Log.d("Recipe", "" + ABV);
            return (OG - FG) * 131.25;

        } else {
            //double og = convertGravity(OG);
            //double fg = convertGravity(FG);
            Log.d("Recipe", "OG: " + OG + " FG: " + FG);
            //this.ABV = (OG-FG) * .516;
            Log.d("Recipe", "OG ABV: " + ABV);
            return (OG-FG) * .516;

        }
    }


    @Override
    public String toString(){
        return batchName;
    }

    @Override
    public int compareTo(Recipe o){
        int i, j;
        String temp;
        if(getBatchName().compareToIgnoreCase(o.getBatchName()) > 0){
            return 1;
        }
        else if(getBatchName().compareToIgnoreCase(o.getBatchName()) < 0) {
            return -1;
        }
        else {
            return 0;
        }
    }



}
