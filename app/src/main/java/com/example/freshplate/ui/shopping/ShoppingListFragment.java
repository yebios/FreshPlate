package com.example.freshplate.ui.shopping;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.freshplate.databinding.FragmentShoppingListBinding; // 自动生成的

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
        binding.setLifecycleOwner(getViewLifecycleOwner());

        // 3. 设置 Adapter (将 viewModel 传入)
        adapter = new ShoppingListAdapter(viewModel);
        binding.rvShoppingList.setAdapter(adapter);
        binding.rvShoppingList.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 4. 观察列表数据
        viewModel.getAllItems().observe(getViewLifecycleOwner(), items -> {
            if (items != null) {
                adapter.submitList(items);
            }
        });
    }
}
