package com.example.freshplate.ui.additem;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.freshplate.data.model.PantryItem;
import com.example.freshplate.data.model.ProductResponse;
import com.example.freshplate.data.repository.PantryRepository;
import com.example.freshplate.data.repository.ProductRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class AddItemViewModel extends AndroidViewModel {

    private final PantryRepository repository;
    private final ProductRepository productRepository;
    // 定义日期格式化
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault());
    // 用于 Data Binding 的公开字段
    public final MutableLiveData<String> itemName = new MutableLiveData<>();
    public final MutableLiveData<String> quantity = new MutableLiveData<>();
    public final MutableLiveData<String> expirationDateString = new MutableLiveData<>("Select Date");
    // 内部保存的日期，用于存入数据库
    private LocalDate selectedExpirationDate;


    // 用于通知 Fragment 导航的 LiveData
    // (!! 更改) 用于通知 "保存" 或 "取消" 已完成，可以返回上一个界面
    private final MutableLiveData<Boolean> _navigateBack = new MutableLiveData<>(false);
    public LiveData<Boolean> getNavigateBackEvent() {
        return _navigateBack;
    }

    // (!! 更改) 用于通知显示日期选择器
    private final MutableLiveData<Boolean> _showDatePicker = new MutableLiveData<>(false);
    public LiveData<Boolean> getShowDatePickerEvent() {
        return _showDatePicker;
    }

    // (!! 新增) 用于通知启动条码扫描器
    private final MutableLiveData<Boolean> _scanBarcode = new MutableLiveData<>(false);
    public LiveData<Boolean> getScanBarcodeEvent() {
        return _scanBarcode;
    }

    // (!! 新增) 用于在保存失败时显示提示 (例如 "请输入名称")
    private final MutableLiveData<String> _toastMessage = new MutableLiveData<>();
    public LiveData<String> getToastMessageEvent() {
        return _toastMessage;
    }

    // (!! 新增) 用于显示产品查询加载状态
    private final MutableLiveData<Boolean> _isLoadingProduct = new MutableLiveData<>(false);
    public final LiveData<Boolean> isLoadingProduct = _isLoadingProduct;
    public LiveData<Boolean> getIsLoadingProduct() {
        return _isLoadingProduct;
    }

    // (!! 新增) 保存正在编辑的物品的 ID
    private final MutableLiveData<Integer> itemId = new MutableLiveData<>();

    // (!! 新增) 标记我们是处于添加模式还是编辑模式
    private boolean isEditMode = false;

    // (!! 新增) 当处于编辑模式时，从数据库加载物品
    // 确保永远不为 null，避免 Fragment 调用 observe 时 NPE
    private LiveData<PantryItem> loadedItem = new MutableLiveData<>(null);


    // (你需要通过 ViewModelFactory 或 Hilt 注入 repository)
    public AddItemViewModel(@NonNull Application application) {
        super(application);
        this.repository = new PantryRepository(application);
        this.productRepository = new ProductRepository();

        // (!! 关键) Transformations.switchMap
        // 当 itemId 发生变化时，根据是否为编辑模式选择数据源
        loadedItem = Transformations.switchMap(itemId, id -> {
            if (id != null && id != -1) {
                return repository.getItemById(id);
            }
            // 添加模式：返回一个空的 LiveData
            return new MutableLiveData<>(null);
        });
    }

    // (!! 新增) 由 AddItemFragment 调用
    public void start(int id) {
        // 根据传入的 id 决定是否为编辑模式
        isEditMode = id != -1;
        itemId.setValue(id);
    }

    // (!! 新增) 由 AddItemFragment 调用，用于填充表单
    public LiveData<PantryItem> getLoadedItem() {
        return loadedItem;
    }

    // 由 fragment_add_item.xml 中的 "Save" 按钮调用
    public void onSaveClicked() {
        // 1. 数据验证
        String nameValue = itemName.getValue();
        if (nameValue == null || nameValue.trim().isEmpty()) {
            _toastMessage.setValue("Please enter an item name");
            return;
        }
        String quantityValue = quantity.getValue();
        if (quantityValue == null || quantityValue.trim().isEmpty()) {
            _toastMessage.setValue("Please enter a quantity");
            return;
        }
        if (selectedExpirationDate == null) {
            _toastMessage.setValue("Please select an expiration date");
            return;
        }

        String name = nameValue.trim();
        int qty;
        try {
            qty = Integer.parseInt(quantityValue.trim());
            // (!! 关键) 根据模式决定是更新还是插入
            if (isEditMode) {
                // 我们需要原始的 ID
                PantryItem itemToUpdate = loadedItem.getValue();
                if (itemToUpdate != null) {
                    itemToUpdate.name = name;
                    itemToUpdate.quantity = qty;
                    itemToUpdate.expirationDate = selectedExpirationDate;
                    repository.update(itemToUpdate);
                }
            } else {
                // 2. 创建PantryItem对象
                PantryItem newItem = new PantryItem(name, qty, selectedExpirationDate);
                // 3. 调用仓库保存
                repository.insert(newItem);
            }
        } catch (NumberFormatException e) {
            _toastMessage.setValue("Please enter a valid quantity");
            return;
        }

        // 4. 通知 Fragment 导航回上一个界面
        _navigateBack.setValue(true);
    }

    // (!! 更改) 当我们从数据库加载物品时，填充字段
    public void populateFields(PantryItem item) {
        if (item != null) {
            itemName.setValue(item.name);
            quantity.setValue(String.valueOf(item.quantity));
            // (!! 关键) 我们必须同时设置 String 和 LocalDate
            onDateSelected(item.expirationDate);
        }
    }

    // (!! 更改) onDateSelected 现在有两个用途
    public void onDateSelected(LocalDate date) {
        this.selectedExpirationDate = date;
        expirationDateString.setValue(this.selectedExpirationDate.format(dateFormatter));
    }

    // (!! 更改) onDateSelected (来自 DatePicker)
    public void onDateSelected(Long selection) {
        Instant instant = Instant.ofEpochMilli(selection);
        LocalDate date = instant.atZone(ZoneId.of("UTC")).toLocalDate();
        onDateSelected(date); // 调用我们的新方法
    }

    /**
     * (!! 新增) 由 "Cancel" 按钮的 android:onClick 调用
     */
    public void onCancelClicked() {
        // 通知 Fragment 导航回上一个界面
        _navigateBack.setValue(true);
    }

    /**
     * (!! 新增) 由 "Scan Barcode" 按钮的 android:onClick 调用
     */
    public void onScanBarcodeClicked() {
        // 通知 Fragment 启动扫描器
        _scanBarcode.setValue(true);
    }

    /**
     * 由 "Expiration Date" 字段的 android:onClick 调用
     */
    public void onSelectDateClicked() {
        _showDatePicker.setValue(true);
    }

    /**
     * (!! 更新) 当条码扫描完成后，由 Fragment 调用
     * @param barcodeResult 扫描到的条码
     */
    public void onBarcodeScanned(String barcodeResult) {
        // 显示加载状态
        _isLoadingProduct.setValue(true);

        // 调用 OpenFoodFacts API 查询产品信息
        productRepository.getProductByBarcode(barcodeResult, new ProductRepository.ProductCallback() {
            @Override
            public void onSuccess(ProductResponse productResponse) {
                // 在主线程更新 UI
                _isLoadingProduct.postValue(false);

                // 自动填充产品名称
                String productName = productResponse.product.getDisplayName();
                itemName.postValue(productName);

                // 显示成功消息
                _toastMessage.postValue("Product found: " + productName);
            }

            @Override
            public void onNotFound() {
                _isLoadingProduct.postValue(false);
                _toastMessage.postValue("Product not found in database. Please enter manually.");

                // 可以选择将条码作为临时名称
                itemName.postValue("Barcode: " + barcodeResult);
            }

            @Override
            public void onError(String errorMessage) {
                _isLoadingProduct.postValue(false);
                _toastMessage.postValue("Error: " + errorMessage);
            }
        });
    }

    // --- 事件重置方法 (防止重复触发) ---

    public void onNavigationHandled() {
        _navigateBack.setValue(false);
    }

    public void onDatePickerShown() {
        _showDatePicker.setValue(false);
    }

    public void onScanHandled() {
        _scanBarcode.setValue(false);
    }

    public void onToastShown() {
        _toastMessage.setValue(null);
    }
}
