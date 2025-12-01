package com.example.freshplate.ui.additem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.freshplate.data.model.PantryItem;
import com.example.freshplate.databinding.FragmentAddItemBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class AddItemFragment extends Fragment {

    private AddItemViewModel viewModel;
    // 将 binding 提升为成员变量，便于在 onViewCreated 设置 LifecycleOwner，并在 onDestroyView 释放
    private FragmentAddItemBinding binding;

    // 处理 Activity 结果 (例如, 扫描)
    // 注册 "ScanContract" (来自 Zxing 库)
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    // 成功获取到条码
                    viewModel.onBarcodeScanned(result.getContents());
                }
            }
    );

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // 1. 使用 Data Binding 填充布局
        binding = FragmentAddItemBinding.inflate(inflater, container, false);

        // 2. 获取 ViewModel (你需要设置 ViewModelFactory)
        viewModel = new ViewModelProvider(this).get(AddItemViewModel.class);

        // 3. 将 ViewModel 绑定到 XML
        binding.setViewModel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 现在视图已创建，安全地设置 LifecycleOwner
        binding.setLifecycleOwner(getViewLifecycleOwner());

        // 从导航参数中获取 itemId
        int itemId = -1;
        if (getArguments() != null) {
            // 使用传统的 Bundle 方式获取参数
            itemId = getArguments().getInt("itemId", -1);
        }

        // 启动 ViewModel 并传入 ID
        viewModel.start(itemId);

        // 观察从数据库加载的物品
        viewModel.getLoadedItem().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(PantryItem item) {
                if (item != null) {
                    viewModel.populateFields(item);
                    // 移除观察者，这样旋转屏幕时
                    // 不会用旧数据覆盖用户的新输入
                    viewModel.getLoadedItem().removeObserver(this);
                }
            }
        });

        // 5. 设置所有 LiveData 观察者
        setupObservers();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 防止内存泄漏
        binding = null;
    }

    /**
     * 设置所有 ViewModel 事件的观察者
     */
    private void setupObservers(){
        // 观察 "返回" 事件 (来自 Save 或 Cancel 按钮)
        viewModel.getNavigateBackEvent().observe(getViewLifecycleOwner(), navigate -> {
            if (navigate) {
                // 执行导航
                NavHostFragment.findNavController(this).popBackStack();
                // 通知 ViewModel 事件已处理
                viewModel.onNavigationHandled();
            }
        });

        // 观察 "显示日期选择器" 事件
        viewModel.getShowDatePickerEvent().observe(getViewLifecycleOwner(), show -> {
            if (show) {
                showDatePicker();
                // (!! 注意) 我们在 showDatePicker() 的 dismiss 监听中重置事件
            }
        });

        // 观察 "启动条码扫描" 事件
        viewModel.getScanBarcodeEvent().observe(getViewLifecycleOwner(), scan -> {
            if (scan) {
                launchBarcodeScanner();
                // 通知 ViewModel 事件已处理
                viewModel.onScanHandled();
            }
        });

        // 观察 "显示 Toast" 事件 (用于验证错误)
        viewModel.getToastMessageEvent().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                // 通知 ViewModel 事件已处理
                viewModel.onToastShown();
            }
        });

        // 观察产品查询加载状态
        viewModel.getIsLoadingProduct().observe(getViewLifecycleOwner(), isLoading -> {
            // 在加载时禁用扫描按钮，防止重复请求
            if (binding != null) {
                binding.btnScanBarcode.setEnabled(!isLoading);
                binding.btnScanBarcode.setText(isLoading ? "Loading..." : getString(com.example.freshplate.R.string.scan_barcode));
            }
        });
    }



    /**
     * 创建并显示 MaterialDatePicker 的方法
     */
    private void showDatePicker() {
        // 创建 DatePicker
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Expiration Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds()) // 默认选中今天
                .build();

        // 侦听“确定”按钮点击
        datePicker.addOnPositiveButtonClickListener(selection -> {
            // 将用户选择的时间戳 (Long) 传回给 ViewModel
            viewModel.onDateSelected(selection);
        });

        // 侦听取消或关闭
        datePicker.addOnDismissListener(dialog -> {
            // 通知 ViewModel 选择器已关闭，重置事件
            viewModel.onDatePickerShown();
        });

        datePicker.addOnNegativeButtonClickListener(v -> viewModel.onDatePickerShown());

        // 显示 DatePicker
        // 使用 "datePickerTag" 作为唯一的 Tag，防止重复显示
        datePicker.show(getParentFragmentManager(), "datePickerTag");
    }

    /**
     * 启动 Zxing 条码扫描器
     */
    private void launchBarcodeScanner() {
        // 1. 创建一个配置对象
        ScanOptions options = new ScanOptions();
        // 2. 自定义扫描界面的外观和行为
        options.setPrompt("Scan a barcode");    // 在扫描界面底部显示提示文字
        options.setBeepEnabled(true);           // 扫描成功时播放“哔”声
        options.setOrientationLocked(false);    // 允许屏幕旋转
        // 3. 告诉第1部分中准备好的 "发射器"：
        barcodeLauncher.launch(options);
    }
}
