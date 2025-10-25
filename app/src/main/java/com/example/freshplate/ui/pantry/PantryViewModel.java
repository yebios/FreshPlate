package com.example.freshplate.ui.pantry;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.freshplate.data.model.PantryItem;
import com.example.freshplate.data.repository.PantryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PantryViewModel extends AndroidViewModel {
    private final PantryRepository repository;
    private final LiveData<List<PantryItem>> allItems;
    // 这是用于 Data Binding 的搜索词
    // 设为 public final，以便 Data Binding 可以进行双向绑定
    public final MutableLiveData<String> searchQuery = new MutableLiveData<>("");

    // 这是最终的 已过滤 列表，Fragment 将观察这个
    private final MediatorLiveData<List<PantryItem>> filteredPantryItems = new MediatorLiveData<>();

    public PantryViewModel(@NonNull Application application) {
        super(application);
        this.repository = new PantryRepository(application);
        this.allItems = repository.getAllItems();
        // 设置 MediatorLiveData
        // 1. 添加 allItems (数据库的原始列表) 作为源
        filteredPantryItems.addSource(allItems, items -> {
            filterList();
        });
        // 2. 添加 searchQuery (搜索框的文本) 作为源
        filteredPantryItems.addSource(searchQuery, query -> {
            filterList();
        });
    }

    private void filterList() {
        List<PantryItem> currentItems = allItems.getValue();
        String query = searchQuery.getValue();

        // 如果原始列表尚未加载，则不执行任何操作
        if (currentItems == null) {
            return;
        }

        // 如果搜索词为空，则显示完整列表
        if (query == null || query.trim().isEmpty()) {
            filteredPantryItems.setValue(currentItems);
            return;
        }

        // 执行过滤
        List<PantryItem> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase(Locale.getDefault());

        for (PantryItem item : currentItems) {
            if (item.name.toLowerCase(Locale.getDefault()).contains(lowerCaseQuery)) {
                filteredList.add(item);
            }
        }

        // 发出新的已过滤列表
        filteredPantryItems.setValue(filteredList);
    }

    // 暴露数据给 UI
    public LiveData<List<PantryItem>> getFilteredItems() {
        return filteredPantryItems;
    }

    // ... (onAddItemClicked 用于导航)
    // ... 用于导航到 AddItemFragment 的 LiveData 事件 ...
    private final MutableLiveData<Boolean> _navigateToAddItem = new MutableLiveData<>(false);
    public LiveData<Boolean> getNavigateToAddItemEvent() { return _navigateToAddItem; }
    public void onAddItemClicked() { _navigateToAddItem.setValue(true); }
    public void onAddItemNavigated() { _navigateToAddItem.setValue(false); }
}
