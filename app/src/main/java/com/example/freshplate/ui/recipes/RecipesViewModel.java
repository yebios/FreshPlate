package com.example.freshplate.ui.recipes;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import com.example.freshplate.data.model.Ingredient;
import com.example.freshplate.data.model.PantryItem;
import com.example.freshplate.data.model.Recipe;
import com.example.freshplate.data.repository.PantryRepository;
import com.example.freshplate.data.repository.RecipeRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecipesViewModel extends AndroidViewModel{
    private final PantryRepository pantryRepository;
    private final RecipeRepository recipeRepository;

    private final LiveData<List<PantryItem>> pantryItems; // 源1: 本地数据库
    private LiveData<List<Recipe>> apiRecipesSource;      // 源2: 远程 API

    // (!! 最终结果) Fragment 将观察这个
    private final MediatorLiveData<List<Recipe>> sortedRecipeList = new MediatorLiveData<>();

    public RecipesViewModel(@NonNull Application application) {
        super();
        pantryRepository = new PantryRepository(application);
        recipeRepository = new RecipeRepository();

        // 1. 获取储藏室物品
        pantryItems = pantryRepository.getAllItems();

        // 2. 将 pantryItems 添加为源。
        // 当用户点击 "Recipes" 标签时，这个 LiveData 会被激活，
        // 它的观察者 (Fragment) 会触发 pantryItems 加载。
        sortedRecipeList.addSource(pantryItems, items -> {
            if (items != null && !items.isEmpty()) {
                // (!! 触发) 储藏室物品已加载，现在去获取食谱
                fetchAndSortRecipes(items);
            } else if (items != null) {
                sortedRecipeList.setValue(new ArrayList<>()); // 储藏室为空
            }
        });
    }
    /**
     * 暴露给 Fragment 观察
     */
    public LiveData<List<Recipe>> getSortedRecipes() {
        return sortedRecipeList;
    }

    /**
     * 步骤 1: 将储藏室物品转换为 API 查询
     */
    private void fetchAndSortRecipes(List<PantryItem> currentPantry) {
        String ingredientsQuery = currentPantry.stream()
                .map(PantryItem::getName)
                .collect(Collectors.joining(","));

        // (!! 移除旧的 API 源，以防重复调用)
        if (apiRecipesSource != null) {
            sortedRecipeList.removeSource(apiRecipesSource);
        }

        // 步骤 2: 调用 API
        apiRecipesSource = recipeRepository.searchRecipes(ingredientsQuery);

        // 步骤 3: 协调两个数据源
        sortedRecipeList.addSource(apiRecipesSource, apiRecipes -> {
            if (apiRecipes != null) {
                // (!! 核心) API 结果已返回，现在执行排序
                List<Recipe> sortedList = sortRecipesByExpiration(apiRecipes, currentPantry);
                sortedRecipeList.setValue(sortedList);
            } else {
                sortedRecipeList.setValue(new ArrayList<>()); // API 错误
            }
        });
    }

    /**
     * (!! 核心排序逻辑)
     */
    private List<Recipe> sortRecipesByExpiration(List<Recipe> apiRecipes, List<PantryItem> pantryItems) {
        // 1. 创建一个储藏室物品的快速查找表 (名称 -> 过期日期)
        Map<String, LocalDate> pantryMap = new HashMap<>();
        for (PantryItem item : pantryItems) {
            pantryMap.put(item.name.toLowerCase(), item.expirationDate);
        }

        // 2. 遍历 API 返回的食谱，计算它们的 "最紧急日期"
        for (Recipe recipe : apiRecipes) {
            LocalDate soonestDate = LocalDate.MAX;
            boolean hasPantryIngredient = false;

            // (!! 检查所有食材，包括已拥有的和缺失的)
            List<Ingredient> allIngredients = new ArrayList<>();
            if (recipe.getUsedIngredients() != null) allIngredients.addAll(recipe.getUsedIngredients());
            if (recipe.getMissedIngredients() != null) allIngredients.addAll(recipe.getMissedIngredients());

            for (Ingredient ingredient : allIngredients) {
                // 检查这个食材是否在我们的储藏室中
                LocalDate expirationDate = pantryMap.get(ingredient.getName().toLowerCase());
                if (expirationDate != null) {
                    hasPantryIngredient = true;
                    // 如果是，检查它是否是迄今为止最快过期的
                    if (expirationDate.isBefore(soonestDate)) {
                        soonestDate = expirationDate;
                    }
                }
            }

            // 只有当食谱至少用到了我们的一样食材时，才设置它的优先级
            if (hasPantryIngredient) {
                recipe.setSoonestExpirationDate(soonestDate);
            }
            // (如果食谱完全不匹配我们的食材，它将保留 LocalDate.MAX 并排在最后)
        }

        // 3. 排序！
        // .compare(r1, r2) 将按升序排序 (最早的日期在前)
        apiRecipes.sort((r1, r2) ->
                r1.getSoonestExpirationDate().compareTo(r2.getSoonestExpirationDate()));

        return apiRecipes;
    }
}
