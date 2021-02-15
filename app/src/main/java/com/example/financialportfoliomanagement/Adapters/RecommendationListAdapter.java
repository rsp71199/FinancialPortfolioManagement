package com.example.financialportfoliomanagement.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.financialportfoliomanagement.R;

import java.util.List;

public class RecommendationListAdapter extends RecyclerView.Adapter<RecommendationListAdapter.MyViewHolder> {
    public List<String> mDataset;

//    public Context context;


    public void refresh(List<String> dataset) {
        mDataset = dataset;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView shortName, ret, risk, weight;
        public Button delete;


        public MyViewHolder(View v) {
            super(v);
            shortName = v.findViewById(R.id.shortName);
            ret = v.findViewById(R.id.ret);
            risk = v.findViewById(R.id.risk);
            weight = v.findViewById(R.id.weight);


        }
    }

    public RecommendationListAdapter(List<String> myDataset) {

        this.mDataset = myDataset;

//        this.context = cont;
    }

    @Override
    public RecommendationListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recommendation_list_item, parent, false);
        return new RecommendationListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecommendationListAdapter.MyViewHolder holder, final int position) {
        String ft = mDataset.get(position);
        String a[] = ft.split(",");
        float w = Float.parseFloat(a[1]);
        w = w * 100;
        String we = "Wt " + w + "%";
        float ret = Float.parseFloat(a[2]);
        ret = ret * 252 * 100;
        String re = "Ret " + ret + "%";

        float risk = Float.parseFloat(a[3]);
        risk = (float) (risk * Math.sqrt(252) * 100);
        String ri = "Risk " + risk + "%";

        holder.shortName.setText(a[0]);
        holder.weight.setText(we);
        holder.ret.setText(re);
        holder.risk.setText(ri);

    }

    @Override
    public int getItemCount() {
        if (mDataset == null) return 0;
        return mDataset.size();
    }
}
