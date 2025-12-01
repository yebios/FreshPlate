package com.example.freshplate.ui.recipes.detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.freshplate.data.model.RecipeDetail;
import com.example.freshplate.data.repository.RecipeRepository;
import com.example.freshplate.data.repository.ShoppingRepository;

import lombok.Getter;

public class RecipeDetailViewModel extends AndroidViewModel {

    private final RecipeRepository recipeRepository;
    private final ShoppingRepository shoppingRepository;

    @Getter
    private final LiveData<RecipeDetail> recipeDetails;

    public RecipeDetailViewModel(@NonNull Application application, int recipeId) {
        super(application);
        this.recipeRepository = new RecipeRepository();
        this.shoppingRepository = new ShoppingRepository(application);
        // 启动时立即获取数据
        this.recipeDetails = recipeRepository.getRecipeDetails(recipeId);
    }

    /**
     * 将不在购物清单中的食材添加到购物清单
     * @param ingredientName 食材名称
     */
    public void addIngredientToShoppingList(String ingredientName) {
        shoppingRepository.insertIfNotExists(ingredientName);
    }

    /**
     * ViewModelFactory
     * 这是一个标准的模板，用于在创建 ViewModel 时向其传递参数 (例如 recipeId)
     */
    public static class Factory implements ViewModelProvider.Factory {

        private final Application application;
        private final int recipeId;

        public Factory(@NonNull Application application, int recipeId) {
            this.application = application;
            this.recipeId = recipeId;
        }

        @NonNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new RecipeDetailViewModel(application, recipeId);
        }
    }
}
