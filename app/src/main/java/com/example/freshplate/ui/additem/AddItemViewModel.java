package com.example.freshplate.ui.additem;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.freshplate.data.model.PantryItem;
import com.example.freshplate.data.repository.PantryRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class AddItemViewModel extends AndroidViewModel {

    private final PantryRepository repository;
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

    // (你需要通过 ViewModelFactory 或 Hilt 注入 repository)
    public AddItemViewModel(@NonNull Application application) {
        super(application);
        this.repository = new PantryRepository(application);
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
        } catch (NumberFormatException e) {
            _toastMessage.setValue("Please enter a valid quantity");
            return;
        }

        // 2. 创建PantryItem对象
        PantryItem newItem = new PantryItem(name, qty, selectedExpirationDate);

        // 3. 调用仓库保存
        repository.insert(newItem);

        // 4. 通知 Fragment 导航回上一个界面
        _navigateBack.setValue(true);
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
     * (!! 关键更改) 当 DatePicker 选择完成时，由 Fragment 调用
     * @param selection 选定的日期，以 UTC 毫秒时间戳 (Long) 形式
     */
    public void onDateSelected(Long selection) {
        // 1. 将毫秒时间戳 (Long) 转换为 Instant (时间线上的一个点)
        Instant instant = Instant.ofEpochMilli(selection);

        // 2. (核心) 将该 Instant 转换为 UTC 时区的 LocalDate
        // MaterialDatePicker 返回的是 UTC 日期的 00:00 时刻
        this.selectedExpirationDate = instant.atZone(ZoneId.of("UTC")).toLocalDate();

        // 3. 格式化日期以便在 UI 上显示
        expirationDateString.setValue(this.selectedExpirationDate.format(dateFormatter));
    }

    /**
     * (!! 新增) 当条码扫描完成后，由 Fragment 调用
     * @param barcodeResult 扫描到的条码
     */
    public void onBarcodeScanned(String barcodeResult) {
        // TODO: 后续需通过该编码调用第三方 API 查询商品名称、价格等详细信息（条形码本身不存储这些信息，仅存储编码）
        // 在这里，你可以调用一个网络 API (例如 OpenFoodFacts) 来根据条码获取产品名称
        // (!! 伪代码)
        // repository.fetchProductNameByBarcode(barcodeResult, (name) -> {
        //    itemName.postValue(name); // postValue 因为这可能在后台线程
        // });

        // (!! 占位符) 现在，我们只把条码号填入名称中
        itemName.setValue("Item: " + barcodeResult);
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
