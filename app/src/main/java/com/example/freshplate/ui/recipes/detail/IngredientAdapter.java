package com.example.freshplate.ui.recipes.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshplate.R;
import com.example.freshplate.data.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<Ingredient> ingredients = new ArrayList<>();
    private OnIngredientClickListener clickListener;

    /**
     * 点击监听器接口
     */
    public interface OnIngredientClickListener {
        void onIngredientClick(Ingredient ingredient);
    }

    /**
     * 设置点击监听器
     */
    public void setOnIngredientClickListener(OnIngredientClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.bind(ingredients.get(position), clickListener);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView addButton;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tv_ingredient_name);
            addButton = itemView.findViewById(R.id.iv_add_to_shopping);
        }

        public void bind(Ingredient ingredient, OnIngredientClickListener listener) {
            // (!! TODO: 我们需要 API 返回 "original" 字符串以显示 "1 cup Flour")
            nameTextView.setText(ingredient.getName());

            // 设置整个 item 的点击监听
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onIngredientClick(ingredient);
                }
            });

            // 设置添加按钮的点击监听
            addButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onIngredientClick(ingredient);
                }
            });
        }
    }
}
