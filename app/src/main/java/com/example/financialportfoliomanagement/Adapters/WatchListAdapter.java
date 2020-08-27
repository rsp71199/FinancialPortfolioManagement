package com.example.financialportfoliomanagement.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.financialportfoliomanagement.R;

import java.util.List;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.MyViewHolder> {
    private List<String> mDataset;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView searchName;
        public TextView searchSymbol;
        public ImageButton add_to_list;

        public MyViewHolder(View v) {
            super(v);
            searchName = v.findViewById(R.id.searchName);
            searchSymbol = v.findViewById(R.id.searchSymbol);
            add_to_list = v.findViewById(R.id.add_to_list);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public WatchListAdapter(List<String> myDataset, Context cont) {

        this.mDataset = myDataset;
        this.context = cont;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public WatchListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result, parent, false);


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to the charts activity
            }
        });
        return new WatchListAdapter.MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final WatchListAdapter.MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.searchName.setText(mDataset.get(position));
        holder.add_to_list.setImageResource(R.drawable.options_added);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mDataset == null) return 0;
        return mDataset.size();
    }
}

