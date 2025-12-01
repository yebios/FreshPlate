package com.example.freshplate.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.freshplate.data.model.Recipe;
import com.example.freshplate.data.model.RecipeDetail;
import com.example.freshplate.data.model.RecipeResponse;
import com.example.freshplate.data.remote.ApiClient;
import com.example.freshplate.data.remote.RecipeApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRepository {
    private RecipeApiService apiService;

    private static final String API_KEY = "26ec70afc680434c8d2f08636dac4703";

    public RecipeRepository() {
        this.apiService = ApiClient.getClient().create(RecipeApiService.class);
    }

    /**
     * 异步从 API 获取食谱
     */
    public LiveData<List<Recipe>> searchRecipes(String ingredientsQuery) {
        MutableLiveData<List<Recipe>> data = new MutableLiveData<>();

        // 调用 complexSearch
        apiService.complexSearch(API_KEY, ingredientsQuery, 20, true, true, 2)
                .enqueue(new Callback<RecipeResponse>() {
                    @Override
                    public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // 从包装类中提取列表
                            data.setValue(response.body().getRecipes());
                        } else {
                            data.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RecipeResponse> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    /**
     *  异步从 API 获取单个食谱的详细信息
     */
    public LiveData<RecipeDetail> getRecipeDetails(int recipeId) {
        MutableLiveData<RecipeDetail> data = new MutableLiveData<>();

        apiService.getRecipeInformation(recipeId, API_KEY, false) // false = 不需要营养信息
                .enqueue(new Callback<RecipeDetail>() {
                    @Override
                    public void onResponse(Call<RecipeDetail> call, Response<RecipeDetail> response) {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                        } else {
                            data.setValue(null); // API 错误
                        }
                    }

                    @Override
                    public void onFailure(Call<RecipeDetail> call, Throwable t) {
                        data.setValue(null); // 网络错误
                    }
                });
        return data;
    }
}
