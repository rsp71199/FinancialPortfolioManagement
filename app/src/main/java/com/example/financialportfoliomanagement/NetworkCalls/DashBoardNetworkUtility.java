package com.example.financialportfoliomanagement.NetworkCalls;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.financialportfoliomanagement.Adapters.NewsAdapter;
import com.example.financialportfoliomanagement.Adapters.TrendingTickersAdapter;
import com.example.financialportfoliomanagement.Models.News;
import com.example.financialportfoliomanagement.Models.TrendingTicker;
import com.example.financialportfoliomanagement.Utilities.JSONFetcher;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashBoardNetworkUtility {
    private Context context;
    private List<News> newsResults = new ArrayList<>();
    private List<TrendingTicker> trendingTickersResults = new ArrayList<>();
    private RecyclerView.Adapter newsAdapter;
    private RecyclerView.Adapter trendingTickersAdapter;

    Object object;

    public DashBoardNetworkUtility(Context context) {
        this.context = context;

    }

    public void set_NSE_BSE(final TextView bse1, final TextView bse2, final TextView nse1, final TextView nse2) {
        StringRequest request = new StringRequest(Request.Method.GET
                , "https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/get-summary?region=IN&lang=en"
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    set_NSE_BSE_data(response, bse1, bse2, nse1, nse2);
                } else {
                    Log.e("Your Array Response", "Data Null");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com");
                params.put("x-rapidapi-key", "c3053afbcemsh74ad59fc4e199f2p102296jsn1773dd7a07fd");
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    public void set_NSE_BSE_data(String response, final TextView bse1, final TextView bse2, final TextView nse1, final TextView nse2) {
        try {
            JSONObject jsonObject = new JSONObject(JSONFetcher.fetch(context, "dummy_summary.json"));
            JSONObject jsonResult = jsonObject.getJSONObject("marketSummaryResponse");
            JSONArray jsonArray = jsonResult.getJSONArray("result");

            JSONObject bse = new JSONObject(jsonArray.get(0).toString());
            JSONObject nse = new JSONObject(jsonArray.get(1).toString());

            String bse_name = bse.getString("fullExchangeName").toUpperCase().toString();
            String nse_name = nse.getString("fullExchangeName").toUpperCase().toString();


            //for bse
            Log.i("TAG", bse.toString());
            Log.i("TAG", nse.toString());
            JSONObject bse_regularMarketPrice_object = bse.getJSONObject("regularMarketPrice");
            String bse_regularMarketPrice_value = bse_regularMarketPrice_object.getString("fmt");
            JSONObject bse_regularMarketChangePercent_object = bse.getJSONObject("regularMarketChangePercent");
            String bse_regularMarketChangePercent_value = bse_regularMarketChangePercent_object.getString("fmt");
            JSONObject bse_regularMarketChange_object = bse.getJSONObject("regularMarketChange");
            String bse_regularMarketChange_value = bse_regularMarketChange_object.getString("fmt");
            bse1.setText(bse_regularMarketPrice_value);
            bse2.setText(bse_regularMarketChange_value + " (" + bse_regularMarketChangePercent_value + "%)");

            //for nse
            JSONObject nse_regularMarketPrice_object = nse.getJSONObject("regularMarketPrice");
            String nse_regularMarketPrice_value = nse_regularMarketPrice_object.getString("fmt");
            JSONObject nse_regularMarketChangePercent_object = nse.getJSONObject("regularMarketChangePercent");
            String nse_regularMarketChangePercent_value = nse_regularMarketChangePercent_object.getString("fmt");
            JSONObject nse_regularMarketChange_object = nse.getJSONObject("regularMarketChange");
            String nse_regularMarketChange_value = nse_regularMarketChange_object.getString("fmt");
            nse1.setText(nse_regularMarketPrice_value);
            nse2.setText(nse_regularMarketChange_value + " (" + nse_regularMarketChangePercent_value + "%)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void set_NSE_BSE_chart(String interval, String symbol, String range) {
        String url = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/get-charts" +
                "?comparisons=%255EGDAXI%252C%255EFCHI&region=IN&lang=en&symbol="
                + symbol + "^NSEI&interval=" + interval + "&range=" + range;


        StringRequest request = new StringRequest(Request.Method.GET
                , url
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        Log.e("Your Array Response", jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("Your Array Response", "Data Null");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com");
                params.put("x-rapidapi-key", "c3053afbcemsh74ad59fc4e199f2p102296jsn1773dd7a07fd");
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    public void set_NSE_chart_data(final LineChart nse_chart) {
        final ArrayList<Entry> nse_high = new ArrayList<>();

        try {
            JSONObject jsonObject_nse = new JSONObject(JSONFetcher.fetch(context, "dummy_nse_chart.json"));


            //for nse

            JSONObject chart = jsonObject_nse.getJSONObject("chart");
            JSONArray result = chart.getJSONArray("result");
            JSONObject resultElement = result.getJSONObject(0);
            JSONObject indicators = resultElement.getJSONObject("indicators");
            JSONArray quote = indicators.getJSONArray("quote");
            JSONObject quoteElement = quote.getJSONObject(0);
            JSONArray low = quoteElement.getJSONArray("low");
            JSONArray volume = quoteElement.getJSONArray("volume");
            final JSONArray high = quoteElement.getJSONArray("high");
            JSONArray close = quoteElement.getJSONArray("close");
            JSONArray open = quoteElement.getJSONArray("open");

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    nse_high.clear();
                    try {
                        for (int i = 0; i < high.length(); i++) {
                            float val = (float) high.getLong(i);
                            nse_high.add(new Entry(i * 0.001f, val));
                            Log.i("TAG", high.get(i).toString());
                        }
                        LineDataSet set1 = new LineDataSet(nse_high, "DataSet 1");

                        set1.setColor(Color.GREEN);
                        set1.setLineWidth(0.8f);
                        set1.setDrawValues(false);
                        set1.setDrawCircles(false);
                        set1.setMode(LineDataSet.Mode.LINEAR);
                        set1.setDrawFilled(true);
                        LineData data = new LineData(set1);
                        nse_chart.setData(data);
                        Legend l = nse_chart.getLegend();
                        l.setEnabled(false);
                    } catch (Exception e) {

                    }


                }

            };
            r.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set_BSE_chart_data(final LineChart bse_chart) {
        final ArrayList<Entry> bse_high = new ArrayList<>();

        try {
            JSONObject jsonObject_nse = new JSONObject(JSONFetcher.fetch(context, "dummy_bse_chart.json"));


            //for nse

            JSONObject chart = jsonObject_nse.getJSONObject("chart");
            JSONArray result = chart.getJSONArray("result");
            JSONObject resultElement = result.getJSONObject(0);
            JSONObject indicators = resultElement.getJSONObject("indicators");
            JSONArray quote = indicators.getJSONArray("quote");
            JSONObject quoteElement = quote.getJSONObject(0);
            JSONArray low = quoteElement.getJSONArray("low");
            JSONArray volume = quoteElement.getJSONArray("volume");
            final JSONArray high_bse = quoteElement.getJSONArray("high");
            JSONArray close = quoteElement.getJSONArray("close");
            JSONArray open = quoteElement.getJSONArray("open");

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    bse_high.clear();
                    try {
                        for (int i = 0; i < high_bse.length(); i++) {
                            float val = (float) high_bse.getLong(i);
                            bse_high.add(new Entry(i * 0.001f, val));
                            Log.i("TAG", high_bse.get(i).toString());
                        }
                        LineDataSet set2 = new LineDataSet(bse_high, "DataSet 1");

                        set2.setColor(Color.GREEN);
                        set2.setLineWidth(0.8f);
                        set2.setDrawValues(false);
                        set2.setDrawCircles(false);
                        set2.setMode(LineDataSet.Mode.LINEAR);
                        set2.setDrawFilled(true);

                        // create a data object with the data sets
                        LineData data = new LineData(set2);

                        // set data
                        bse_chart.setData(data);

                        // get the legend (only possible after setting data)
                        Legend l = bse_chart.getLegend();
                        l.setEnabled(false);
                    } catch (Exception e) {

                    }


                }

            };
            r.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void set_Trending_Tickers() {
        String url = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/get-trending-tickers?region=IN";

        StringRequest request = new StringRequest(Request.Method.GET
                , url
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        Log.e("Your Array Response", jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("Your Array Response", "Data Null");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com");
                params.put("x-rapidapi-key", "c3053afbcemsh74ad59fc4e199f2p102296jsn1773dd7a07fd");
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    public void set_Trending_Tickers_data(final RecyclerView trendingTickersRecyclerView) {
        try {
            JSONObject jsonObject = new JSONObject(JSONFetcher.fetch(context, "dummy_tickers.json"));
            JSONObject finance = jsonObject.getJSONObject("finance");
            Log.i("Finance", finance.toString());
            JSONArray result = finance.getJSONArray("result");
            Log.i("Result", result.toString());
            final JSONArray quote = new JSONObject(result.get(0).toString()).getJSONArray("quotes");
            Log.i("Quote", quote.toString());
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        trendingTickersResults.clear();
                        for (int i = 0; i < quote.length(); i++) {
                            JSONObject quoteElement = new JSONObject(quote.get(i).toString());
                            Log.i("TAG", quoteElement.toString());
                            trendingTickersResults.add(new TrendingTicker(quoteElement.getString("shortName")
                                    , quoteElement.getString("regularMarketPrice")
                                    , quoteElement.getString("regularMarketChange")
                                    , quoteElement.getString("regularMarketChangePercent")
                                    , quoteElement.getString("quoteType")));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    trendingTickersAdapter = new TrendingTickersAdapter(trendingTickersResults, context);
                    trendingTickersRecyclerView.setAdapter(trendingTickersAdapter);
                }
            };
            r.run();


//            Log.i("TAG", object.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void set_News(final RecyclerView newsRecyclerView) {

        String url = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/get-news?region=IN&category=NBEV";

        StringRequest request = new StringRequest(Request.Method.GET
                , url
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    set_News_data(response, newsRecyclerView);
                } else {
                    Log.e("Your Array Response", "Data Null");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com");
                params.put("x-rapidapi-key", "c3053afbcemsh74ad59fc4e199f2p102296jsn1773dd7a07fd");
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);


    }

    public void set_News_data(String response, final RecyclerView newsRecyclerView) {
        JSONObject jsonObject = null;
        JSONArray jsonArray = new JSONArray();
        try {
            jsonObject = new JSONObject(JSONFetcher.fetch(context, "dummy_news.json"));
            JSONObject jsonObject1 = jsonObject.getJSONObject("items");
            jsonArray = jsonObject1.getJSONArray("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JSONArray finalJsonArray = jsonArray;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                newsResults.clear();
                try {
                    for (int i = 0; i < finalJsonArray.length(); i++) {
                        String title = new JSONObject(finalJsonArray.get(i).toString()).getString("title");
                        String summary = new JSONObject(finalJsonArray.get(i).toString()).getString("summary");
                        JSONObject imageObject = new JSONObject(finalJsonArray.get(i).toString()).getJSONObject("main_image");
                        JSONArray resolutions = imageObject.getJSONArray("resolutions");
                        JSONObject image = new JSONObject(resolutions.get(3).toString());
                        String url = image.getString("url");
                        newsResults.add(new News(title, summary, url));

                    }

                } catch (Exception e) {

                }
                newsAdapter = new NewsAdapter(newsResults, context);
                newsRecyclerView.setAdapter(newsAdapter);

            }
        };

        r.run();
    }
}
