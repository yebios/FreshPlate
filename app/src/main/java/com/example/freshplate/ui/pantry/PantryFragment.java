package com.example.freshplate.ui.pantry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.navigation.fragment.NavHostFragment;
import com.example.freshplate.R;
import com.example.freshplate.data.model.PantryItem;
import com.example.freshplate.databinding.FragmentPantryBinding; // Data Binding 生成的类
import androidx.recyclerview.widget.ItemTouchHelper; // (!! 导入)
import androidx.recyclerview.widget.RecyclerView;    // (!! 导入)
import com.google.android.material.snackbar.Snackbar; // (!! 导入)

public class PantryFragment extends Fragment {

    private PantryViewModel viewModel;
    private FragmentPantryBinding binding;
    private PantryAdapter adapter; // 你的 RecyclerView 适配器

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPantryBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(PantryViewModel.class);
        binding.setViewModel(viewModel);
        // 延后到 onViewCreated 再设置 LifecycleOwner，避免过早访问导致崩溃
        // binding.setLifecycleOwner(getViewLifecycleOwner());

        // 将 RecyclerView 初始化移到 onViewCreated，确保视图层级稳定
        // setupRecyclerView();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 现在视图已创建，安全设置 LifecycleOwner
        binding.setLifecycleOwner(getViewLifecycleOwner());

        // 1. 设置 RecyclerView
        setupRecyclerView();

        // 2. (核心) 观察 ViewModel 中的数据
        viewModel.getFilteredItems().observe(getViewLifecycleOwner(), items -> {
            if (items != null) {
                adapter.submitList(items);
            }
        });

        // 3. (导航) 监听 "+" 按钮点击
        viewModel.getNavigateToAddItemEvent().observe(getViewLifecycleOwner(), navigate -> {
            if (navigate) {
                NavHostFragment.findNavController(this).navigate(R.id.action_pantryFragment_to_addItemFragment);
                viewModel.onAddItemNavigated(); // 重置事件
            }
        });

        // (!! 新增) 观察 "item to delete" 事件，用于显示 Undo Snackbar
        viewModel.getItemToDelete().observe(getViewLifecycleOwner(), item -> {
            if (item != null) {
                showUndoSnackbar(item);
                viewModel.deleteEventHandled(); // 重置事件
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 防止内存泄漏
        binding = null;
    }

    private void setupRecyclerView() {
        adapter = new PantryAdapter();
        adapter.setOnItemClickListener(item -> {
            // (!! 关键) 点击物品时，导航到 AddItemFragment 并传递 ID
            Bundle args = new Bundle();
            args.putInt("itemId", item.id);
            NavHostFragment.findNavController(this).navigate(
                    R.id.action_pantryFragment_to_addItemFragment,
                    args
            );
        });
        binding.rvPantryItems.setAdapter(adapter);
        binding.rvPantryItems.setLayoutManager(new LinearLayoutManager(getContext()));
        // (!! 关键) 在这里添加 ItemTouchHelper
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false; // 我们不支持拖动排序
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // 1. 获取被滑动的物品
                int position = viewHolder.getBindingAdapterPosition();
                PantryItem item = adapter.getCurrentList().get(position);

                // 2. (!! 关键) 通知 ViewModel 删除物品
                viewModel.deleteItem(item);
            }
        }).attachToRecyclerView(binding.rvPantryItems); // 将 "滑动" 功能附加到 RecyclerView;

    }

    /**
     * (!! 新增) 显示一个带有 "Undo" 按钮的 Snackbar
     */
    private void showUndoSnackbar(PantryItem item) {
        Snackbar.make(binding.getRoot(), "Deleted " + item.name, Snackbar.LENGTH_LONG)
                .setAction("UNDO", v -> {
                    // (!! 关键) 通知 ViewModel 撤销删除
                    viewModel.undoDelete(item);
                })
                .show();
    }
}
