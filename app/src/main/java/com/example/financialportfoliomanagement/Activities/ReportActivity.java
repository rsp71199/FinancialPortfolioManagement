package com.example.financialportfoliomanagement.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.financialportfoliomanagement.Models.SearchResult;
import com.example.financialportfoliomanagement.Models.User;
import com.example.financialportfoliomanagement.R;
import com.example.financialportfoliomanagement.Utilities.ChartMarker;
import com.example.financialportfoliomanagement.ViewModel.AuthViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReportActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CardView lineCard;
    private PieChart chart;
    private LineChart lineChart;
    private AuthViewModel authViewModel;
    private User user_main;
    private ImageButton survey;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    private void init() {

        survey = findViewById(R.id.survey);
        survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), QuestionnareActivity.class));
            }
        });
        QuarterlyChart();
        setLineChart();
        setData(4, 23);
        setAuthViewModel();


    }

    private void setAuthViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getAuthData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                user_main = user;
                Log.i("Report>>>>>>>>>>", user.getUser_id());
                getReportDataFromNetwork();
            }
        });
        if (authViewModel.getAuthData().getValue() == null) {
            authViewModel.setFirebaseFirestore();
        }

    }

    private void setData(int count, float range) {

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(12f, "Quarter 1")); //revenue1
        entries.add(new PieEntry(29f, "Quarter 2")); //expense1
        entries.add(new PieEntry(18f, "Quarter 3")); //revenue2
        entries.add(new PieEntry(41f, "Quarter 4")); //expense2
        PieDataSet dataSet = new PieDataSet(entries, "Quarterly breakdown");

        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        List<Integer> colors = new ArrayList<Integer>();
        colors.add(ContextCompat.getColor(this, R.color.Aqua));
        colors.add(ContextCompat.getColor(this, R.color.Orchid));
        colors.add(ContextCompat.getColor(this, R.color.DarkOrchid));
        colors.add(ContextCompat.getColor(this, R.color.Blue));
        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
//        data.setValueTypeface(tfLight);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();


        final ArrayList<Entry> entryHighArrayList = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 300; i++) {
            entryHighArrayList.add(new Entry(i, (int) rand.nextInt(100)));
        }
        LineDataSet set1 = new LineDataSet(entryHighArrayList, "high ");
        set1.setDrawValues(false);
        set1.setDrawCircles(false);
        set1.setMode(LineDataSet.Mode.LINEAR);
        set1.setDrawFilled(true);
        Drawable drawable1 = ContextCompat.getDrawable(this, R.drawable.fade_line_chart);
        set1.setFillDrawable(drawable1);
        List<ILineDataSet> lines = new ArrayList<ILineDataSet>();
        lines.add(set1);
        lineChart.setData(new LineData(lines));
        lineChart.invalidate();
        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();
        l.setEnabled(false);
    }

    private void QuarterlyChart() {
        chart = findViewById(R.id.piechart);
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

//        chart.setCenterTextTypeface(tfLight);
//        chart.setCenterText(generateCenterSpannableText());

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void setLineChart() {
        lineCard = findViewById(R.id.lineChartCard);
        ChartMarker chartMarker = new ChartMarker(this, R.layout.tool_tip);
        lineChart = findViewById(R.id.lineChart);
//        lineChart.setBackgroundColor(getColor(R.color.primary));
        lineChart.setDrawGridBackground(false);
        lineChart.getDescription().setEnabled(true);
        lineChart.setMarker(chartMarker);
        lineChart.setBorderColor(Color.BLACK);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.getAxisLeft().setDrawAxisLine(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setGridLineWidth(0.5f);
        lineChart.getAxisLeft().setGridColor(R.color.White);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setDrawLabels(false);
        lineChart.getAxisLeft().setTextColor(Color.WHITE);
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.getXAxis().setDrawLabels(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setLabelCount(4, false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setDrawAxisLine(false);
        lineChart.getXAxis().setTextColor(Color.WHITE);


    }

    public void getReportDataFromNetwork() {
        final List<SearchResult> searchResults = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://fpmaserver.herokuapp.com/api/user/report/" + user_main.getUser_id();
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.\
                        Log.i("TAG", response);

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("TAG", error.toString());

            }
        }) {

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                super.onBackPressed();
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}