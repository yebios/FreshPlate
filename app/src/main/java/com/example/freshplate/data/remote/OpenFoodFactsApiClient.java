package com.example.freshplate.data.remote;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * OpenFoodFacts API 客户端
 * 与主 API 使用不同的 Base URL
 */
public class OpenFoodFactsApiClient {
    private static final String BASE_URL = "https://world.openfoodfacts.org/";
    private static final String USER_AGENT = "FreshPlate - Android - 1.0 - freshplate.example.com";

    private static Retrofit retrofit = null;

    /**
     * 获取 Retrofit 实例
     * OpenFoodFacts 要求在请求头中添加 User-Agent
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            // 创建 OkHttpClient 并添加拦截器来设置 User-Agent
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("User-Agent", USER_AGENT)
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

