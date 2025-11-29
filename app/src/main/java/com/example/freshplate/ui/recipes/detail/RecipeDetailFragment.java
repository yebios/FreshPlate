package com.example.freshplate.ui.recipes.detail;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.freshplate.R;
import com.example.freshplate.data.model.Ingredient;
import com.example.freshplate.databinding.FragmentRecipeDetailBinding;
import com.google.android.material.snackbar.Snackbar;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderEffectBlur;
import eightbitlab.com.blurview.RenderScriptBlur;

public class RecipeDetailFragment extends Fragment {

    private RecipeDetailViewModel viewModel;
    private FragmentRecipeDetailBinding binding;
    private IngredientAdapter ingredientAdapter;
    private InstructionAdapter instructionAdapter;
    private int recipeId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 从导航参数中获取 ID（Safe Args 生成后可用）
        if (getArguments() != null) {
            // 如果 Safe Args 还未生成，这里会在编译时报错；生成后恢复该行
            // recipeId = RecipeDetailFragmentArgs.fromBundle(getArguments()).getRecipeId();
            Object id = getArguments().get("recipeId");
            if (id instanceof Integer) {
                recipeId = (Integer) id;
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false);

        // 使用 Factory 来创建 ViewModel
        RecipeDetailViewModel.Factory factory =
                new RecipeDetailViewModel.Factory(requireActivity().getApplication(), recipeId);
        viewModel = new ViewModelProvider(this, factory).get(RecipeDetailViewModel.class);

        // 绑定 ViewModel 到布局
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        // 设置 Adapters
        setupAdapters();

        return binding.getRoot();
    }

    private void setupAdapters() {
        ingredientAdapter = new IngredientAdapter();
        binding.rvIngredients.setAdapter(ingredientAdapter);

        // 设置食材点击监听器
        ingredientAdapter.setOnIngredientClickListener(this::showAddToShoppingListDialog);

        instructionAdapter = new InstructionAdapter();
        binding.rvInstructions.setAdapter(instructionAdapter);
    }

    /**
     * 显示添加到购物清单的确认对话框
     */
    private void showAddToShoppingListDialog(Ingredient ingredient) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.add_to_shopping_list_title)
                .setMessage(getString(R.string.add_to_shopping_list_message, ingredient.getName()))
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    // 添加到购物清单
                    viewModel.addIngredientToShoppingList(ingredient.getName());

                    // 显示成功提示
                    Snackbar.make(binding.getRoot(),
                            getString(R.string.added_to_shopping_list, ingredient.getName()),
                            Snackbar.LENGTH_SHORT)
                            .show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化 Blur 背景
        setupBlur();

        // 观察 ViewModel
        viewModel.getRecipeDetails().observe(getViewLifecycleOwner(), recipeDetail -> {
            if (recipeDetail != null) {
                if (recipeDetail.getIngredients() != null) {
                    ingredientAdapter.setIngredients(recipeDetail.getIngredients());
                }

                if (recipeDetail.getInstructions() != null && !recipeDetail.getInstructions().isEmpty()) {
                    instructionAdapter.setSteps(recipeDetail.getInstructions().get(0).getSteps());
                }

                // Spoonacular 的 'summary' 包含 HTML 标签，需要清理
                if (recipeDetail.getDescription() != null) {
                    binding.tvRecipeDetailTime.setText(Html.fromHtml(recipeDetail.getDescription(), Html.FROM_HTML_MODE_COMPACT));
                }
            } else {
                // 简单处理：清空适配器
                ingredientAdapter.setIngredients(java.util.Collections.emptyList());
                instructionAdapter.setSteps(java.util.Collections.emptyList());
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