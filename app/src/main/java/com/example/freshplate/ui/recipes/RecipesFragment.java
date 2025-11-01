package com.example.freshplate.ui.recipes;

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
// (!! 导入自动生成的 Data Binding 类)
import com.example.freshplate.databinding.FragmentRecipesBinding;
import com.example.freshplate.R;

import java.util.ArrayList;

public class RecipesFragment extends Fragment {
    private RecipesViewModel viewModel;
    private FragmentRecipesBinding binding;
    private RecipeAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // (!! 更改) 使用 Data Binding 填充
        binding = FragmentRecipesBinding.inflate(inflater, container, false);

        // 设置 Adapter 和 RecyclerView
        setupRecyclerView();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new RecipeAdapter();

        // (!! 关键) 在这里设置点击监听器
        adapter.setOnItemClickListener(recipe -> {
            Bundle args = new Bundle();
            args.putInt("recipeId", recipe.getId());
            NavHostFragment.findNavController(this).navigate(R.id.recipeDetailFragment, args);
        });

        binding.rvRecipes.setAdapter(adapter);
        binding.rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 获取 ViewModel
        viewModel = new ViewModelProvider(this).get(RecipesViewModel.class);

        // (!! 关键) 观察 ViewModel 的最终结果
        viewModel.getSortedRecipes().observe(getViewLifecycleOwner(), recipes -> {
            if (recipes != null) {
                // (!! 你可以在这里添加逻辑：如果 recipes.isEmpty()，显示 "无食谱" 提示)
                adapter.submitList(recipes);
            } else {
                // (!! 处理 API 错误)
                adapter.submitList(new ArrayList<>()); // 提交空列表
            }
        });
    }
}
