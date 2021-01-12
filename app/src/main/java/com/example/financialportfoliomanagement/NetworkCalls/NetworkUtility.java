package com.example.financialportfoliomanagement.NetworkCalls;


import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.financialportfoliomanagement.Interfaces.OnCandleChartsDataRetrieveInterface;
import com.example.financialportfoliomanagement.Interfaces.OnChartDataRetrieveFailure;
import com.example.financialportfoliomanagement.Interfaces.OnIndexDataRetrieveInterface;
import com.example.financialportfoliomanagement.Interfaces.OnLineChartDataRetrieveInterface;
import com.example.financialportfoliomanagement.Interfaces.OnNewsRetrieveInterface;
import com.example.financialportfoliomanagement.Interfaces.OnSearchResultRetrieveInterface;
import com.example.financialportfoliomanagement.Interfaces.OnStocksDataRetrieveInterface;
import com.example.financialportfoliomanagement.Models.Index;
import com.example.financialportfoliomanagement.Models.News;
import com.example.financialportfoliomanagement.Models.SearchResult;
import com.example.financialportfoliomanagement.Utilities.ApiEndPoints;
import com.example.financialportfoliomanagement.Utilities.JSONFetcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkUtility {
    private Context context;
//    private User user;
    private OnIndexDataRetrieveInterface onIndexDataRetrieveInterface;
    private OnStocksDataRetrieveInterface onStocksDataRetrieveInterface;
    private OnSearchResultRetrieveInterface onSearchResultRetrieveInterface;
    private OnCandleChartsDataRetrieveInterface onCandleChartsDataRetrieveInterface;
    private OnLineChartDataRetrieveInterface onLineChartDataRetrieveInterface;
    private OnChartDataRetrieveFailure onChartDataRetrieveFailure;

    public void setOnNewsRetrieveInterface(OnNewsRetrieveInterface onNewsRetrieveInterface) {
        this.onNewsRetrieveInterface = onNewsRetrieveInterface;
    }

    private OnNewsRetrieveInterface onNewsRetrieveInterface;
    Object object;


    public void setOnCandleChartsDataRetrieveInterface(OnCandleChartsDataRetrieveInterface onCandleChartsDataRetrieveInterface) {
        this.onCandleChartsDataRetrieveInterface = onCandleChartsDataRetrieveInterface;
    }

    public void setOnLineChartDataRetrieveInterface(OnLineChartDataRetrieveInterface onLineChartDataRetrieveInterface) {
        this.onLineChartDataRetrieveInterface = onLineChartDataRetrieveInterface;
    }

    public void setOnChartDataRetrieveFailure(OnChartDataRetrieveFailure onChartDataRetrieveFailure) {
        this.onChartDataRetrieveFailure = onChartDataRetrieveFailure;
    }

    public void setOnSearchResultRetrieveInterface(OnSearchResultRetrieveInterface onSearchResultRetrieveInterface) {
        this.onSearchResultRetrieveInterface = onSearchResultRetrieveInterface;
    }

    public void setOnIndexDataRetrieveInterface(OnIndexDataRetrieveInterface onIndexDataRetrieveInterface) {
        this.onIndexDataRetrieveInterface = onIndexDataRetrieveInterface;
    }

    public void setOnStocksDataRetrieveInterface(OnStocksDataRetrieveInterface onStocksDataRetrieveInterface) {
        this.onStocksDataRetrieveInterface = onStocksDataRetrieveInterface;
    }

    public NetworkUtility(Context context) {
        this.context = context;
    }

//    public NetworkUtility(Context context, User user) {
//        this.context = context;
//        this.user  = user;
//    }


    public void setChart(String symbol, String function, final String interval, String apiKey, final int chart_id) {

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
        final String finalArraySearchKey = arraySearchKey;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {

                        if (chart_id == 0) {
                            onCandleChartsDataRetrieveInterface.onCandleChartDataRetrieveSuccess(res, finalArraySearchKey);
                        } else {
                            onLineChartDataRetrieveInterface.onLineChartDataRetrieveSuccess(res, finalArraySearchKey);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                onChartDataRetrieveFailure.onFailure();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }
    public void get_index_summary(){
        String url = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/get-summary";
        StringRequest request = new StringRequest(Request.Method.GET
                , url
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    Log.e("Summary_Network",response);
                    process_index_summary_data(response);
                }
                else {
                    Log.e("Your Array Response", "Data Null");
                    onIndexDataRetrieveInterface.onIndexDataRetrieveFailure();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
                onIndexDataRetrieveInterface.onIndexDataRetrieveFailure();
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
    public void process_index_summary_data(String response_real) {

        final List<Index> indexList = new ArrayList<>();
        try {
            JSONObject res = new JSONObject(JSONFetcher.fetch(context, "dummy_summary.json"));//getData from network
            JSONObject marketSummaryResponse = res.getJSONObject("marketSummaryResponse");
            final JSONArray result = marketSummaryResponse.getJSONArray("result");
            new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject element = result.getJSONObject(i);
                            String fullExchangeName = element.getString("fullExchangeName");
                            String regularMarketChangePercent = element.getJSONObject("regularMarketChangePercent").getString("fmt");
                            String regularMarketChangePercentRaw = element.getJSONObject("regularMarketChangePercent").getString("raw");
                            String regularMarketPrice = element.getJSONObject("regularMarketPrice").getString("raw");
                            String regularMarketChange = element.getJSONObject("regularMarketChange").getString("fmt");
                            String regularMarketPreviousClose = element.getJSONObject("regularMarketPreviousClose").getString("raw");
                            boolean b = false;
                            float percent = Float.parseFloat(regularMarketChangePercentRaw);
                            if (percent <= 0) b = true;
                            indexList.add(new Index(fullExchangeName
                                    , regularMarketPrice
                                    , regularMarketChange + " (" + regularMarketChangePercent + ")"
                                    , regularMarketPreviousClose, b));

                            Log.i("TAG", fullExchangeName);
                        }
                        onIndexDataRetrieveInterface.onIndexDataRetrieveSuccess(indexList);
                    } catch (Exception e) {
                        onIndexDataRetrieveInterface.onIndexDataRetrieveFailure();
                    }
                }
            }.run();
        } catch (JSONException e) {
            e.printStackTrace();
            onIndexDataRetrieveInterface.onIndexDataRetrieveFailure();
        }
    }
    public void get_search_result(String symbol) {
        final List<SearchResult> searchResults = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=" + symbol + "&apikey=B02L3PBPXDL1PUY4";
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.\

                        try {
                            final JSONObject json = new JSONObject(response);
                            final JSONArray jsonArray = json.getJSONArray("bestMatches");
//                            Log.i("TAG", response);
                            final Handler handler = new Handler();
                            final Runnable r = new Runnable() {
                                public void run() {
                                    searchResults.clear();
                                    try {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonElement = jsonArray.getJSONObject(i);
                                            String sym = jsonElement.get("1. symbol").toString();
                                            String equityName = jsonElement.get("2. name").toString();
                                            boolean b = false;

                                            searchResults.add(new SearchResult(equityName, sym, b));
//                                            Log.i("TAG", equityName);
                                        }
                                        onSearchResultRetrieveInterface.onSearchResultRetrieveSuccess(searchResults);
                                    } catch (Exception e) {
                                        onSearchResultRetrieveInterface.onSearchResultRetrieveFailure();
                                    }
                                }
                            };
                            r.run();
                        } catch (JSONException e) {
                            onSearchResultRetrieveInterface.onSearchResultRetrieveFailure();
                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("TAG", error.toString());
                onSearchResultRetrieveInterface.onSearchResultRetrieveFailure();
            }
        });
        queue.add(stringRequest);
    }

    public void get_news() {
        String url = "https://newsapi.org/v2/everything?q=stocks&apiKey=4428399959044fbdbbf9e78d3c3913c1";
        StringRequest request = new StringRequest(Request.Method.GET
                , url
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray articles = new JSONObject(response).getJSONArray("articles");
                    List<News> newsList = new ArrayList<>();
                    for (int i = 0; i < articles.length() && i < 30; i++) {
                        newsList.add(new News(articles.getJSONObject(i).getString("title")
                                , articles.getJSONObject(i).getString("description")
                                , articles.getJSONObject(i).getString("urlToImage")));
                    }
                    onNewsRetrieveInterface.success(newsList);
                } catch (Exception e) {
                    onNewsRetrieveInterface.failure(e);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
                onNewsRetrieveInterface.failure(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }
}
