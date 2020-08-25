package com.example.financialportfoliomanagement.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financialportfoliomanagement.Models.News;
import com.example.financialportfoliomanagement.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private List<News> mDataset;
    private Context context;

    public NewsAdapter(List<News> mDataset, Context context) {
        this.mDataset = mDataset;
        this.context = context;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView summary;
        public ImageView imageView;

        public MyViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            imageView = v.findViewById(R.id.image);
            summary = v.findViewById(R.id.summary);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to the charts activity
            }
        });
        return new NewsAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(context, ChartsActivity.class);
//
//                context.startActivity(i);
            }
        });
        holder.title.setText(mDataset.get(position).title);
        holder.summary.setText(mDataset.get(position).summary);
        Picasso.get().load(mDataset.get(position).url).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
