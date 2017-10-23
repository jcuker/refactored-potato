package com.jordancuker.refactoredpotato.Activities;


/* Consider
https://github.com/umano/AndroidSlidingUpPanel
https://github.com/TakuSemba/Spotlight
*https://github.com/KeepSafe/TapTargetView
*https://github.com/gowong/material-sheet-fab
*https://github.com/futuresimple/android-floating-action-button
*
* */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.jordancuker.refactoredpotato.BuildConfig;
import com.jordancuker.refactoredpotato.R;
import com.jordancuker.refactoredpotato.SupportingClasses.PartyEntity;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnChartValueSelectedListener, OnChartGestureListener {

    private PieChart mChart;
    private ArrayList<PartyEntity> arrPartiesInvolved;

    private float totalAmount, utilitiesAmount, groceriesAmount, miscAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        arrPartiesInvolved = new ArrayList<>();
        PartyEntity entity = new PartyEntity();
        entity.name = "Greg";
        entity.percent = .70f;
        arrPartiesInvolved.add(entity);
        entity = new PartyEntity();
        entity.name = "Jordan";
        entity.percent = .30f;
        arrPartiesInvolved.add(entity);

        InitializeCategoriesAndAmounts();
        InitializeFAB();
        InitializeToolbar();
        InitializePieChart();
    }

    private void InitializeToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        Spinner spinner = findViewById(R.id.spinner_nav);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                InitializePieChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Categories, R.layout.toolbar_spinner);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void InitializeFAB() {
        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.fab);
        final View actionA = findViewById(R.id.action_a);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_amounts);

                Button positiveButton = dialog.findViewById(R.id.amountPositiveButton);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences sharedPreferences = getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);

                        EditText utilitiesAmountET = dialog.findViewById(R.id.utilitiesEditText);
                        EditText groceriesAmountET = dialog.findViewById(R.id.groceriesEditText);
                        EditText miscAmountET = dialog.findViewById(R.id.miscEditText);

                        float newTotal = 0;

                        if(utilitiesAmountET.getText() == null){
                            newTotal += utilitiesAmount;
                        }
                        else{
                            String editTextValue = utilitiesAmountET.getText().toString();
                            float newUtilitiesAmount = Float.valueOf(editTextValue);
                            newTotal += newUtilitiesAmount;
                            utilitiesAmount = newUtilitiesAmount;
                            sharedPreferences.edit().putFloat("utilities_amount", newUtilitiesAmount).apply();
                        }

                        if(groceriesAmountET.getText() == null){
                            newTotal += groceriesAmount;
                        }
                        else{
                            String editTextValue = groceriesAmountET.getText().toString();
                            float newGroceriesAmount = Float.valueOf(editTextValue);
                            newTotal += newGroceriesAmount;
                            groceriesAmount = newGroceriesAmount;
                            sharedPreferences.edit().putFloat("groceries_amount", newGroceriesAmount).apply();
                        }

                        if(miscAmountET.getText() == null){
                            newTotal += miscAmount;
                        }
                        else{
                            String editTextValue = utilitiesAmountET.getText().toString();
                            float newMiscAmount = Float.valueOf(editTextValue);
                            newTotal += newMiscAmount;
                            miscAmount = newMiscAmount;
                            sharedPreferences.edit().putFloat("misc_amount", newMiscAmount).apply();
                        }

                        sharedPreferences.edit().putFloat("total_amount", newTotal).apply();
                        totalAmount = newTotal;

                        mChart.invalidate();
                        InitializeCategoriesAndAmounts();
                        InitializePieChart();
                        dialog.dismiss();
                    }
                });
                Button negativeButton = dialog.findViewById(R.id.amountNegativeButton);
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                menuMultipleActions.collapse();
            }
        });
        final View actionB = findViewById(R.id.action_b);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_name_select);

                Button positiveButton = dialog.findViewById(R.id.nameselectPositiveButton);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        arrPartiesInvolved = new ArrayList<>();
                        EditText person1 = dialog.findViewById(R.id.person1NameEditText);
                        EditText person1Percent = dialog.findViewById(R.id.person1PercentEditText);
                        EditText person2 = dialog.findViewById(R.id.person2NameEditText);
                        EditText person2Percent = dialog.findViewById(R.id.person2PercentEditText);

                        if(person1.getText() != null && person1Percent != null){
                            PartyEntity entity = new PartyEntity();
                            entity.name = person1.getText().toString();
                            entity.percent = Float.parseFloat(person1Percent.getText().toString());
                            arrPartiesInvolved.add(entity);
                        }
                        if(person2.getText() != null && person2Percent != null){
                            PartyEntity entity = new PartyEntity();
                            entity.name = person2.getText().toString();
                            entity.percent = Float.parseFloat(person2Percent.getText().toString());
                            arrPartiesInvolved.add(entity);
                        }
                        mChart.invalidate();
                        InitializePieChart();
                        dialog.dismiss();
                    }
                });
                Button negativeButton = dialog.findViewById(R.id.nameselectNegativeButton);
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                menuMultipleActions.collapse();
            }
        });
    }

    private void InitializeCategoriesAndAmounts() {
        SharedPreferences sharedPreferences = getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);

        if(sharedPreferences != null){
            totalAmount = sharedPreferences.getFloat("total_amount", 0);
            utilitiesAmount = sharedPreferences.getFloat("utilities_amount", 0);
            groceriesAmount = sharedPreferences.getFloat("groceries_amount", 0);
            miscAmount = sharedPreferences.getFloat("misc_amount", 0);
        }

    }

    private void InitializePieChart() {

        TextView dollarAmountTV = findViewById(R.id.categoryTotal);

        Spinner toolbarSpinner = findViewById(R.id.spinner_nav);

        String categoryHeader = toolbarSpinner.getSelectedItem().toString();

        if(categoryHeader.toLowerCase().equals("total")){
            dollarAmountTV.setText("$" + String.valueOf(totalAmount));
        }
        else if(categoryHeader.toLowerCase().equals("utilities")){
            dollarAmountTV.setText("$" + String.valueOf(utilitiesAmount));
        }
        else if(categoryHeader.toLowerCase().equals("groceries")){
            dollarAmountTV.setText("$" + String.valueOf(groceriesAmount));
        }
        else if(categoryHeader.toLowerCase().equals("misc")){
            dollarAmountTV.setText("$" + String.valueOf(miscAmount));
        }


        Typeface mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        mChart = (PieChart) findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setDrawHoleEnabled(false);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);
        mChart.setOnChartGestureListener(this);

        setData(arrPartiesInvolved.size());

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setTextColor(Color.WHITE);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(12f);

    }

    private void setData(int count) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < count ; i++) {
            PartyEntity entity = arrPartiesInvolved.get(i);
            PieEntry pieEntry = new PieEntry(entity.percent, entity.name, getResources().getDrawable(R.drawable.ic_menu_gallery));
            entries.add(pieEntry);
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(0f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        dataSet.setValueFormatter(new CustomFormatter());
        dataSet.setValueTextColor(R.color.dollarGreen);
        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.COLORFUL_COLORS) {
            colors.add(c);
        }
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);
        mChart.invalidate();
    }

    private class CustomFormatter implements IValueFormatter{
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            PartyEntity party = GetCorrespondingPartyForPieChartDisplay(entry);

            if(party != null){
                Spinner toolbarSpinner = findViewById(R.id.spinner_nav);

                String categoryHeader = toolbarSpinner.getSelectedItem().toString();

                float amountOwed = 0;

                if(categoryHeader.toLowerCase().equals("total")){
                    amountOwed = totalAmount * party.percent;
                }
                else if(categoryHeader.toLowerCase().equals("utilities")){
                    amountOwed = utilitiesAmount * party.percent;
                }
                else if(categoryHeader.toLowerCase().equals("groceries")){
                    amountOwed = groceriesAmount * party.percent;
                }
                else if(categoryHeader.toLowerCase().equals("misc")){
                    amountOwed = miscAmount * party.percent;
                }

                return String.format(Locale.getDefault(), "$ %.2f", amountOwed);
            }

            return "";
        }
    }

    @Nullable
    private PartyEntity GetCorrespondingPartyForPieChartDisplay(Entry entry){
        for(PartyEntity entity : arrPartiesInvolved){
            if(entity.name.toLowerCase().equals(((PieEntry)entry).getLabel().toLowerCase())){
                return entity;
            }
        }
        return null;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent prefIntent = new Intent(this, SettingsActivity.class);
            startActivity(prefIntent);        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // region required by OnChartGestureListener

    @Override
    public void onChartLongPressed(MotionEvent motionEvent){

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    //endregion

}
