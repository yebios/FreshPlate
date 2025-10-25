package com.example.freshplate.data.remote;


import com.example.freshplate.data.model.RecipeResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApiService {
    /**
     * (!! 更改) 使用 complexSearch 来获取食谱及其完整信息
     *
     * @param apiKey               你的 API 密钥
     * @param includeIngredients   用逗号分隔的食材字符串 (例如 "apples,flour")
     * @param number               返回的食谱数量
     * @param addRecipeInformation (!! 关键) true - 返回完整信息 (包括描述和烹饪时间)
     * @param fillIngredients      (!! 关键) true - 返回食谱的食材列表
     * @param ranking              2 = 最小化缺失的食材
     * @return 一个包含食谱列表的 Call 对象
     */
    @GET("recipes/complexSearch")
    Call<RecipeResponse> complexSearch(
            @Query("apiKey") String apiKey,
            @Query("includeIngredients") String includeIngredients,
            @Query("number") int number,
            @Query("addRecipeInformation") boolean addRecipeInformation,
            @Query("fillIngredients") boolean fillIngredients,
            @Query("ranking") int ranking
    );
}
