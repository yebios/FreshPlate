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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

// 条形码扫描
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import com.example.freshplate.databinding.FragmentAddItemBinding;
import com.google.android.material.datepicker.MaterialDatePicker;

public class AddItemFragment extends Fragment {

    private AddItemViewModel viewModel;

    // (!! 新增) 现代的方式来处理 Activity 结果 (例如, 扫描)
    // 我们在这里注册 "ScanContract" (来自 Zxing 库)
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
        FragmentAddItemBinding binding = FragmentAddItemBinding.inflate(inflater, container, false);

        // 2. 获取 ViewModel (你需要设置 ViewModelFactory)
        viewModel = new ViewModelProvider(this).get(AddItemViewModel.class);

        // 3. 将 ViewModel 绑定到 XML
        binding.setViewModel(viewModel);

        // 4. 设置 LifecycleOwner 以便 LiveData 能够自动更新 XML
        binding.setLifecycleOwner(getViewLifecycleOwner());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 5. 设置所有 LiveData 观察者
        setupObservers();
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
    }



    /**
     * (!! 新增) 创建并显示 MaterialDatePicker 的方法
     */
    private void showDatePicker() {
        // 创建 DatePicker
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Expiration Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds()) // 默认选中今天
                .build();

        // 侦听“确定”按钮点击
        datePicker.addOnPositiveButtonClickListener(selection -> {
            // (!! 关键) 将用户选择的时间戳 (Long) 传回给 ViewModel
            viewModel.onDateSelected(selection);
        });

        // 侦听取消或关闭
        datePicker.addOnDismissListener(dialog -> {
            // (!! 关键) 通知 ViewModel 选择器已关闭，重置事件
            viewModel.onDatePickerShown();
        });

        datePicker.addOnNegativeButtonClickListener(v -> viewModel.onDatePickerShown());

        // 显示 DatePicker
        // 使用 "datePickerTag" 作为唯一的 Tag，防止重复显示
        datePicker.show(getParentFragmentManager(), "datePickerTag");
    }

    /**
     * (!! 新增) 启动 Zxing 条码扫描器
     */
    private void launchBarcodeScanner() {
        // 1. 创建一个配置对象
        ScanOptions options = new ScanOptions();
        // 2. 自定义扫描界面的外观和行为
        options.setPrompt("Scan a barcode");    // 在扫描界面底部显示提示文字
        options.setBeepEnabled(true);           // 扫描成功时播放“哔”声
        options.setOrientationLocked(false);    // 允许屏幕旋转
        // 3. (!! 关键 !!) 告诉第1部分中准备好的 "发射器"：
        barcodeLauncher.launch(options);
    }
}
