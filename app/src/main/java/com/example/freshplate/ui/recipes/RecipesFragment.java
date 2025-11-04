package com.example.freshplate.ui.recipes;

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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.freshplate.R;
import com.example.freshplate.databinding.FragmentRecipesBinding;

import java.util.ArrayList;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderEffectBlur;
import eightbitlab.com.blurview.RenderScriptBlur;

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

        // 初始化 BlurView
        setupBlur();

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

    private void setupBlur() {
        if (binding == null) return;
        BlurView blurView = binding.blurView;
        if (blurView == null) return;
        View decorView = requireActivity().getWindow().getDecorView();
        ViewGroup rootView = decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();
        float radius = 20f;
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
}
