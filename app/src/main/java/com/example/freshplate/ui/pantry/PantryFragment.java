package com.example.freshplate.ui.pantry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.navigation.fragment.NavHostFragment;
import com.example.freshplate.R;
import com.example.freshplate.databinding.FragmentPantryBinding; // Data Binding 生成的类

public class PantryFragment extends Fragment {

    private PantryViewModel viewModel;
    private FragmentPantryBinding binding;
    private PantryAdapter adapter; // 你的 RecyclerView 适配器

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPantryBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(PantryViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        // 1. 设置 RecyclerView
        setupRecyclerView();

        // 2. (核心) 观察 ViewModel 中的数据
        viewModel.getFilteredItems().observe(getViewLifecycleOwner(), items -> {
            // 当数据（来自数据库）发生变化时，此代码会自动运行
            adapter.submitList(items); // 假设你使用了 ListAdapter
        });

        // 3. (导航) 监听 "+" 按钮点击
        viewModel.getNavigateToAddItemEvent().observe(getViewLifecycleOwner(), navigate -> {
            if (navigate) {
                NavHostFragment.findNavController(this).navigate(R.id.action_pantryFragment_to_addItemFragment);
                viewModel.onAddItemNavigated(); // 重置事件
            }
        });

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new PantryAdapter();
        binding.rvPantryItems.setAdapter(adapter);
        binding.rvPantryItems.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
