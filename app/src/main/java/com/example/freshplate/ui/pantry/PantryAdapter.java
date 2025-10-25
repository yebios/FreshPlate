package com.example.freshplate.ui.pantry;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freshplate.data.model.PantryItem;
import com.example.freshplate.databinding.ListItemPantryBinding;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * 适配器，用于在 RecyclerView 中显示 PantryItem 列表。
 * 它使用 ListAdapter，通过 DiffUtil 在后台线程上自动计算列表差异。
 */
public class PantryAdapter extends ListAdapter<PantryItem, PantryAdapter.PantryViewHolder> {
    /**
     * 构造函数。
     */
    public PantryAdapter() {
        super(DIFF_CALLBACK);
    }
    /**
     * DiffUtil.ItemCallback
     * 告诉 ListAdapter 如何比较两个 PantryItem 对象，以确定它们是否相同
     * 以及它们的内容是否发生了变化。
     */
    private static final DiffUtil.ItemCallback<PantryItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<PantryItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull PantryItem oldItem, @NonNull PantryItem newItem) {
                    // 检查唯一 ID
                    return oldItem.id == newItem.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull PantryItem oldItem, @NonNull PantryItem newItem) {
                    // 检查内容是否变化。
                    // (使用 LocalDate.equals() 是安全的)
                    return oldItem.quantity == newItem.quantity &&
                            Objects.equals(oldItem.name, newItem.name) &&
                            Objects.equals(oldItem.expirationDate, newItem.expirationDate);
                }
            };

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // (!! 关键) 使用 Data Binding 来填充布局
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemPantryBinding binding = ListItemPantryBinding.inflate(inflater, parent, false);
        return new PantryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        // 获取当前位置的物品
        PantryItem currentItem = getItem(position);
        // 绑定数据
        holder.bind(currentItem);
    }

    /**
     * ViewHolder 类
     * 持有对 list_item_pantry.xml 布局的引用 (通过 Data Binding)
     */
    public static class PantryViewHolder extends RecyclerView.ViewHolder {
        // 持有自动生成的 Data Binding 类的实例
        private final ListItemPantryBinding binding;

        public PantryViewHolder(@NonNull ListItemPantryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /**
         * 将一个 PantryItem 绑定到布局。
         * @param item 要绑定的物品
         */
        public void bind(PantryItem item) {
            // (!! 关键) 将 item 对象设置到 XML 布局中声明的 "item" 变量上
            binding.setItem(item);
            // 立即执行绑定，以避免 RecyclerView 闪烁
            binding.executePendingBindings();
        }
    }
}
