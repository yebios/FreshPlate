package com.example.freshplate.ui.recipes.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.freshplate.R;
import com.example.freshplate.data.model.Step;
import java.util.ArrayList;
import java.util.List;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.InstructionViewHolder> {

    private List<Step> steps = new ArrayList<>();

    @NonNull
    @Override
    public InstructionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_instruction, parent, false);
        return new InstructionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionViewHolder holder, int position) {
        holder.bind(steps.get(position));
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    static class InstructionViewHolder extends RecyclerView.ViewHolder {
        TextView numberTextView;
        TextView stepTextView;

        public InstructionViewHolder(@NonNull View itemView) {
            super(itemView);
            numberTextView = itemView.findViewById(R.id.tv_step_number);
            stepTextView = itemView.findViewById(R.id.tv_step_text);
        }

        public void bind(Step step) {
            numberTextView.setText(step.getNumber() + ".");
            stepTextView.setText(step.getStepText());
        }
    }
}