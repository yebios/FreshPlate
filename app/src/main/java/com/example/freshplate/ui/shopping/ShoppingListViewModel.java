package com.example.freshplate.ui.shopping;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.freshplate.data.model.ShoppingItem;
import com.example.freshplate.data.repository.ShoppingRepository;

import java.util.List;

public class ShoppingListViewModel extends AndroidViewModel {

    private final ShoppingRepository repository;
    private final LiveData<List<ShoppingItem>> allItems;

    // (!! 关键) 用于和 fragment_shopping_list.xml 中的 EditText 双向数据绑定
    public final MutableLiveData<String> newItemName = new MutableLiveData<>("");

    public ShoppingListViewModel(@NonNull Application application) {
        super(application);
        repository = new ShoppingRepository(application);
        allItems = repository.getAllItems();
    }

    public LiveData<List<ShoppingItem>> getAllItems() {
        return allItems;
    }

    /**
     * 由 fragment_shopping_list.xml 中的 "+" 按钮调用
     */
    public void onAddNewItemClicked() {
        String name = newItemName.getValue();
        if (name != null && !name.trim().isEmpty()) {
            ShoppingItem newItem = new ShoppingItem(name.trim(), false);
            repository.insert(newItem);

            // (!! 关键) 添加后清空输入框
            newItemName.setValue("");
        }
    }

    /**
     * 由 list_item_shopping.xml 中的删除图标调用
     */
    public void onDeleteItemClicked(ShoppingItem item) {
        repository.delete(item);
    }

    /**
     * (!! 关键) 由 list_item_shopping.xml 中的 CheckBox 调用
     * 当复选框状态改变时，我们必须更新数据库。
     */
    public void onItemCheckedChanged(ShoppingItem item, boolean isChecked) {
        if (item.isBought != isChecked) {
            item.isBought = isChecked;
            repository.update(item);
        }
    }
}
