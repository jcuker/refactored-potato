package com.jordancuker.refactoredpotato;


/* Consider
https://github.com/umano/AndroidSlidingUpPanel
https://github.com/TakuSemba/Spotlight
*https://github.com/KeepSafe/TapTargetView
*https://github.com/gowong/material-sheet-fab
*https://github.com/futuresimple/android-floating-action-button
*
* */

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnChartValueSelectedListener, OnChartGestureListener {

    private PieChart mChart;
    private ArrayList<PartyEntity> arrPartiesInvolved;
    public float totalAmount = 425;

    public class PartyEntity{
        public String name;
        public float percent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        arrPartiesInvolved = new ArrayList<>();
        PartyEntity entity = new PartyEntity();
        entity.name = "Greg";
        entity.percent = .70f;
        arrPartiesInvolved.add(entity);
        entity = new PartyEntity();
        entity.name = "Jordan";
        entity.percent = .30f;
        arrPartiesInvolved.add(entity);

        InitializeCategoryAndAmount();
        InitializePieChart();

    }

    private void InitializeCategoryAndAmount() {
        TextView categoryName = (TextView) findViewById(R.id.categoryHeader);
        TextView categoryPrice = (TextView) findViewById(R.id.categoryTotal);

        categoryName.setText("Utilities");
        categoryPrice.setText("$425.00");
    }


    private void InitializePieChart() {

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
                float amountOwed = totalAmount * (float) party.percent;
                return String.format(Locale.getDefault(), "$ %.2f", amountOwed);
            }
            return "";
        }
    }

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
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_name_select);
        dialog.setTitle("Title...");

        Button positiveButton = (Button) dialog.findViewById(R.id.positiveButtonNameSelect);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrPartiesInvolved = new ArrayList<PartyEntity>();
                EditText person1 = (EditText) dialog.findViewById(R.id.person1EditText);
                EditText person1Percent = (EditText) dialog.findViewById(R.id.person1EditNumber);
                EditText person2 = (EditText) dialog.findViewById(R.id.person2EditText);
                EditText person2Percent = (EditText) dialog.findViewById(R.id.person2EditNumber);

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
        Button negativeButton = (Button) dialog.findViewById(R.id.negativeButtonNameSelect);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
        Toast.makeText(getApplicationContext(), "Long press the chart to edit it.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    //endregion

}
