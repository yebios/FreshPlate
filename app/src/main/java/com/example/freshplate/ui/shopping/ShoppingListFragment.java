package com.example.freshplate.ui.shopping;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.freshplate.databinding.FragmentShoppingListBinding;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderEffectBlur;
import eightbitlab.com.blurview.RenderScriptBlur;

public class ShoppingListFragment extends Fragment {

    private ShoppingListViewModel viewModel;
    private FragmentShoppingListBinding binding;
    private ShoppingListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShoppingListBinding.inflate(inflater, container, false);

        // 1. 获取 ViewModel
        viewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);

        // 2. 将 ViewModel 绑定到 XML (用于底部的 EditText 和 Button)
        binding.setViewModel(viewModel);
        // 延后到 onViewCreated 设置 LifecycleOwner
        // binding.setLifecycleOwner(getViewLifecycleOwner());

        // 3. 设置 Adapter (将 viewModel 传入)
        adapter = new ShoppingListAdapter(viewModel);
        binding.rvShoppingList.setAdapter(adapter);
        binding.rvShoppingList.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 现在视图已创建，安全设置 LifecycleOwner
        binding.setLifecycleOwner(getViewLifecycleOwner());

        // 初始化 Blur 背景
        setupBlur();

        // 4. 观察列表数据
        viewModel.getAllItems().observe(getViewLifecycleOwner(), items -> {
            if (items != null) {
                adapter.submitList(items);
            }
        });
    }

    private void setupBlur() {
        if (binding == null) return;
        BlurView blurView = binding.blurView;
        if (blurView == null) return;
        View decorView = requireActivity().getWindow().getDecorView();
        ViewGroup rootView = decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();
        float radius = 18f;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            blurView.setupWith(rootView, new RenderEffectBlur())
                    .setFrameClearDrawable(windowBackground)
                    .setBlurRadius(radius)
                    .setOverlayColor(Color.parseColor("#33FFFFFF"));
        } else {
            blurView.setupWith(rootView, new RenderScriptBlur(requireContext()))
                    .setFrameClearDrawable(windowBackground)
                    .setBlurRadius(radius)
                    .setOverlayColor(Color.parseColor("#33FFFFFF"));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 防止内存泄漏
        binding = null;
    }
}
