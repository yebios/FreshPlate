package com.example.freshplate.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.freshplate.data.model.Recipe;
import com.example.freshplate.data.remote.ApiClient;
import com.example.freshplate.data.remote.RecipeApiService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRepository {
    private RecipeApiService apiService;
    // (!! 在这里填入你的 API 密钥)
    private static final String API_KEY = "26ec70afc680434c8d2f08636dac4703";

    public RecipeRepository() {
        this.apiService = ApiClient.getClient().create(RecipeApiService.class);
    }

    /**
     * 异步从 API 获取食谱
     */
    public LiveData<List<Recipe>> searchRecipes(String ingredientsQuery) {
        MutableLiveData<List<Recipe>> data = new MutableLiveData<>();

        apiService.searchRecipesByIngredients(API_KEY, ingredientsQuery, 20, 2) // 获取20个，并最小化缺失食材
                .enqueue(new Callback<List<Recipe>>() {
                    @Override
                    public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            data.setValue(response.body());
                        } else {
                            data.setValue(null); // API 错误
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Recipe>> call, Throwable t) {
                        data.setValue(null); // 网络错误
                    }
                });
        return data;
    }
}
