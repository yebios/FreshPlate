package com.example.freshplate.data.remote;

import com.example.freshplate.data.model.Recipe;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApiService {
    /**
     * 根据食材搜索食谱 (Spoonacular 端点)
     *
     * @param apiKey      你的 API 密钥
     * @param ingredients 一个用逗号分隔的食材字符串 (例如 "apples,flour")
     * @param number      返回的食谱数量 (例如 20)
     * @param ranking     排序方式。 1 = 最大化匹配; 2 = 最小化缺失的食材 (推荐)
     * @return 一个包含食谱列表的 Call 对象
     */
    @GET("recipes/findByIngredients")
    Call<List<Recipe>> searchRecipesByIngredients(
            @Query("apiKey") String apiKey,
            @Query("ingredients") String ingredients,
            @Query("number") int number,
            @Query("ranking") int ranking
    );
}
