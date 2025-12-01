package com.example.freshplate.ui.shopping;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshplate.data.model.ShoppingItem;
import com.example.freshplate.databinding.ListItemShoppingBinding;

import java.util.Objects;

public class ShoppingListAdapter extends ListAdapter<ShoppingItem, ShoppingListAdapter.ShoppingViewHolder> {

    // Adapter 需要持有 ViewModel 的引用来处理点击
    private final ShoppingListViewModel viewModel;

    public ShoppingListAdapter(@NonNull ShoppingListViewModel viewModel) {
        super(DIFF_CALLBACK);
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemShoppingBinding binding = ListItemShoppingBinding.inflate(inflater, parent, false);
        return new ShoppingViewHolder(binding, viewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    /**
     * ViewHolder
     */
    public static class ShoppingViewHolder extends RecyclerView.ViewHolder {
        private final ListItemShoppingBinding binding;

        public ShoppingViewHolder(@NonNull ListItemShoppingBinding binding, ShoppingListViewModel viewModel) {
            super(binding.getRoot());
            this.binding = binding;
            // 将 ViewModel 实例设置到布局变量上
            this.binding.setViewModel(viewModel);
        }

        public void bind(ShoppingItem item) {
            // 将 Item 实例设置到布局变量上
            binding.setItem(item);
            binding.executePendingBindings();
        }
    }

    /**
     * DiffUtil
     */
    private static final DiffUtil.ItemCallback<ShoppingItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ShoppingItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull ShoppingItem oldItem, @NonNull ShoppingItem newItem) {
                    return oldItem.id == newItem.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull ShoppingItem oldItem, @NonNull ShoppingItem newItem) {
                    return oldItem.isBought == newItem.isBought &&
                            Objects.equals(oldItem.name, newItem.name);
                }
            };
}
