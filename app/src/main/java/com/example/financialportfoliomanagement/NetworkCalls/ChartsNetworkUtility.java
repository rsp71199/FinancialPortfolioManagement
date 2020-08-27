package com.example.financialportfoliomanagement.NetworkCalls;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.financialportfoliomanagement.R;
import com.example.financialportfoliomanagement.Utilities.ApiEndPoints;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChartsNetworkUtility {
    public RequestQueue queue;
    private StringRequest stringRequest;
    private Context context;

    public ChartsNetworkUtility(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }


    public void setChart(final LineChart lineChart
            , final CandleStickChart candleStickChart
            , String symbol, String function, final String interval, String apiKey, final int chart_id, final ProgressDialog progressDialog) {

        progressDialog.show();
        String arraySearchKey = "";
        String url = "";
        if (function == ApiEndPoints.TIME_SERIES_INTRADAY) {
            arraySearchKey = "Time Series (" + interval + ")";
            url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol="
                    + symbol + "&interval=" + interval + "&apikey=" + ApiEndPoints.alphaApi;


        } else if (function == ApiEndPoints.TIME_SERIES_DAILY) {
            arraySearchKey = "Time Series (Daily)";
            url = "https://www.alphavantage.co/query?function="
                    + function + "&symbol="
                    + symbol + "&outputsize=full&apikey="
                    + apiKey;
        } else if (function == ApiEndPoints.TIME_SERIES_WEEKLY) {
            arraySearchKey = "Weekly Time Series";
            url = "https://www.alphavantage.co/query?function="
                    + function + "&symbol="
                    + symbol + "&outputsize=full&apikey="
                    + apiKey;
        } else if (function == ApiEndPoints.TIME_SERIES_MONTHLY) {
            arraySearchKey = "Monthly Time Series";
            url = "https://www.alphavantage.co/query?function="
                    + function + "&symbol="
                    + symbol + "&outputsize=full&apikey="
                    + apiKey;
        }

        Log.i("TAG", "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" + url);
//        url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&apikey="+ApiEndPoints.alphaApi;
        final String finalArraySearchKey = arraySearchKey;
        stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {

//                        Log.i("TAG",res);
                        if (chart_id == 0) {
//                            candleStickChart.setVisibility(View.VISIBLE);
//                            lineChart.setVisibility(View.INVISIBLE);
                            setCandleStickChartData(candleStickChart, res, finalArraySearchKey, progressDialog);
                        } else {
//                            candleStickChart.setVisibility(View.INVISIBLE);
//                            lineChart.setVisibility(View.VISIBLE);
                            setLineChartData(lineChart, res, finalArraySearchKey, progressDialog);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.cancel();
            }
        });


        queue.add(stringRequest);
    }

    public void setCandleStickChartData(final CandleStickChart candleStickChart, String response, String arrayKey, final ProgressDialog progressDialog) {
        try {
            final JSONObject json = new JSONObject(response).getJSONObject(arrayKey);
            final Iterator<String> x = json.keys();

            final ArrayList<CandleEntry> candleEntryArrayList = new ArrayList<>();
            final ArrayList<String> xAxis = new ArrayList<>();

            candleEntryArrayList.clear();
            final Runnable r = new Runnable() {
                public void run() {
                    try {
                        int i = 0;
                        while (x.hasNext()) {
                            String key = (String) x.next();
                            JSONObject jsonElement = new JSONObject(json.get(key).toString());
                            Log.i("TAG", jsonElement.toString());

                            xAxis.add(key);
                            float open = Float.valueOf(jsonElement.get("1. open").toString()).floatValue();
                            float high = Float.valueOf(jsonElement.get("2. high").toString()).floatValue();
                            float low = Float.valueOf(jsonElement.get("3. low").toString()).floatValue();
                            float close = Float.valueOf(jsonElement.get("4. close").toString()).floatValue();
                            float val = Float.valueOf(jsonElement.get("5. volume").toString()).floatValue();


                            candleEntryArrayList.add(new CandleEntry(
                                    i,
                                    high,
                                    low,
                                    open,
                                    close
                            ));

                            i++;
                        }
                        setCandleStickChartDataUtil(candleStickChart, candleEntryArrayList, progressDialog, xAxis);
                    } catch (Exception e) {
                        progressDialog.cancel();
                    }

                }

            };
            r.run();
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.cancel();
        }

    }

    public void setCandleStickChartDataUtil(CandleStickChart candleStickChart
            , ArrayList<CandleEntry> candleEntryArrayList, ProgressDialog progressDialog, ArrayList<String> xAxis) {
        CandleDataSet set1 = new CandleDataSet(candleEntryArrayList, "Data Set");
        candleStickChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxis));
        set1.setDrawIcons(false);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setShadowColor(Color.DKGRAY);
        set1.setShadowWidth(0.7f);
        set1.setDecreasingColor(Color.RED);
        set1.setDecreasingPaintStyle(Paint.Style.FILL);
        set1.setIncreasingColor(Color.rgb(122, 242, 84));
        set1.setIncreasingPaintStyle(Paint.Style.STROKE);
        set1.setNeutralColor(Color.BLUE);

        CandleData data = new CandleData(set1);

        candleStickChart.setData(data);
        progressDialog.cancel();
        candleStickChart.invalidate();
    }

    public void setLineChartData(final LineChart lineChart, String response, String arrayKey, final ProgressDialog progressDialog) {
        try {
            final JSONObject json = new JSONObject(response).getJSONObject(arrayKey);
            final Iterator<String> x = json.keys();

            final ArrayList<Entry> entryHighArrayList = new ArrayList<>();
            final ArrayList<Entry> entryLowArrayList = new ArrayList<>();
            final ArrayList<String> xAxis = new ArrayList<>();
            entryHighArrayList.clear();

            final Runnable r = new Runnable() {
                public void run() {
                    try {
                        int i = 0;
                        while (x.hasNext()) {
                            String key = (String) x.next();
                            JSONObject jsonElement = new JSONObject(json.get(key).toString());
//                            Log.i("TAG", jsonElement.toString());

                            float open = Float.valueOf(jsonElement.get("1. open").toString()).floatValue();
                            float high = Float.valueOf(jsonElement.get("2. high").toString()).floatValue();
                            float low = Float.valueOf(jsonElement.get("3. low").toString()).floatValue();
                            float close = Float.valueOf(jsonElement.get("4. close").toString()).floatValue();
                            float val = Float.valueOf(jsonElement.get("5. volume").toString()).floatValue();

                            xAxis.add(key);
                            entryHighArrayList.add(new Entry(i, high));
                            entryLowArrayList.add(new Entry(i, low));


                            i++;
                        }
                        setLineChartDataUtil(lineChart, entryHighArrayList, entryLowArrayList, xAxis, progressDialog);
                    } catch (Exception e) {
                        progressDialog.cancel();
                    }

                }

            };
            r.run();
        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.cancel();
        }
    }

    public void setLineChartDataUtil(LineChart lineChart
            , ArrayList<Entry> entryHighArrayList
            , ArrayList<Entry> entryLowArrayList, ArrayList<String> xAxis, ProgressDialog progressDialog) {
        LineDataSet set1 = new LineDataSet(entryHighArrayList, "high ");
        LineDataSet set2 = new LineDataSet(entryLowArrayList, "low ");


        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxis));


        set1.setColor(Color.GREEN);
        set1.setLineWidth(1.0f);
        set1.setDrawValues(false);
        set1.setDrawCircles(false);
        set1.setMode(LineDataSet.Mode.LINEAR);
        set1.setDrawFilled(true);
        Drawable drawable1 = ContextCompat.getDrawable(context, R.drawable.fade_line_chart);
        set1.setFillDrawable(drawable1);


        set2.setColor(Color.RED);
        Drawable drawable2 = ContextCompat.getDrawable(context, R.drawable.fade_line_chart2);
        set2.setFillDrawable(drawable2);
        set2.setLineWidth(1.0f);
        set2.setDrawValues(false);
        set2.setDrawCircles(false);
        set2.setMode(LineDataSet.Mode.LINEAR);
        set2.setDrawFilled(true);


        // create a data object with the data sets
        LineData data1 = new LineData(set2);
        LineData data2 = new LineData(set2);

        // set data
        List<ILineDataSet> lines = new ArrayList<ILineDataSet>();
        lines.add(set1);
        lines.add(set2);
        lineChart.setData(new LineData(lines));

        lineChart.invalidate();

        progressDialog.cancel();
        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();
        l.setEnabled(false);
    }
}
