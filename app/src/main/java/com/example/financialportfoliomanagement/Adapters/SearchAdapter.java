package com.example.financialportfoliomanagement.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.financialportfoliomanagement.Models.SearchResult;
import com.example.financialportfoliomanagement.Models.User;
import com.example.financialportfoliomanagement.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    private List<SearchResult> mDataset;
    private User user;


    public void refresh(List<SearchResult> searchResultList) {
        mDataset = searchResultList;
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
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

    public SearchAdapter(List<SearchResult> myDataset, User user) {
        this.mDataset = myDataset;
        this.user = user;
    }


    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result, parent, false);


        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Log.i("TAG", mDataset.get(position).name);

        holder.add_to_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDataset.get(position).added_to_list != true) {
                    holder.add_to_list.setImageResource(R.drawable.options_added);

                    notifyItemChanged(position);
                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                    user.add_watch_list_item(mDataset.get(position).symbol);
                    firebaseFirestore.collection("users").document(user.getUser_id())
                            .update("watch_list_symbols", user.getWatch_list_symbols());
                }
            }
        });
        if (user.getWatch_list_symbols().contains(mDataset.get(position).symbol)) {
            holder.add_to_list.setImageResource(R.drawable.options_added);
        } else {
            holder.add_to_list.setImageResource(R.drawable.options);
        }


        holder.searchName.setText(mDataset.get(position).name);
        holder.searchSymbol.setText(mDataset.get(position).symbol);

    }

    @Override
    public int getItemCount() {
        if (mDataset == null) return 0;
        return mDataset.size();
    }
}
