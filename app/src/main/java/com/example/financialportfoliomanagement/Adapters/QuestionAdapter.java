package com.example.financialportfoliomanagement.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financialportfoliomanagement.Models.Question;
import com.example.financialportfoliomanagement.R;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder> {
    public List<Question> getmDataset() {
        return mDataset;
    }

    private List<Question> mDataset;
    private Context context;

    public QuestionAdapter(List<Question> mDataset, Context context) {
        this.mDataset = mDataset;
        this.context = context;
    }

    public void refresh(List<Question> indexList) {
        mDataset = indexList;
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView question;
        public RadioGroup radioGroup;
        public RadioButton op0, op2, op3, op1;

        public MyViewHolder(View v) {
            super(v);
            question = v.findViewById(R.id.question);
            radioGroup = v.findViewById(R.id.radioGroup1);
            op0 = v.findViewById(R.id.radio0);
            op1 = v.findViewById(R.id.radio1);
            op2 = v.findViewById(R.id.radio2);
            op3 = v.findViewById(R.id.radio3);
        }
    }


    @NonNull
    @Override
    public QuestionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_item, parent, false);


        return new QuestionAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.MyViewHolder holder, final int position) {
        holder.question.setText(mDataset.get(position).question);
        List<String> op = mDataset.get(position).options;
        holder.op0.setText(op.get(0));
        holder.op1.setText(op.get(1));
        holder.op2.setText(op.get(2));
        if (op.size() == 4)
            holder.op3.setText(op.get(3));
        else
            holder.op3.setVisibility(View.INVISIBLE);
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Question q = mDataset.get(position);
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                View radioButton = radioGroup.findViewById(radioButtonID);
                int idx = radioGroup.indexOfChild(radioButton);
                RadioButton r = (RadioButton) radioGroup.getChildAt(idx);
                String selectedtext = r.getText().toString();

                q.setSelected_ans(q.getWeights().get(q.options.indexOf(selectedtext)));
                mDataset.set(position, q);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

