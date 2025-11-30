package com.example.freshplate.data.repository;

import android.util.Log;

import com.example.freshplate.data.model.ProductResponse;
import com.example.freshplate.data.remote.OpenFoodFactsApiClient;
import com.example.freshplate.data.remote.OpenFoodFactsApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 产品信息仓库
 * 负责从 OpenFoodFacts API 获取产品信息
 */
public class ProductRepository {
    private static final String TAG = "ProductRepository";
    private final OpenFoodFactsApiService apiService;

    public ProductRepository() {
        this.apiService = OpenFoodFactsApiClient.getClient()
                .create(OpenFoodFactsApiService.class);
    }

    /**
     * 根据条码查询产品信息
     * @param barcode 条码
     * @param callback 回调接口
     */
    public void getProductByBarcode(String barcode, ProductCallback callback) {
        Call<ProductResponse> call = apiService.getProductByBarcode(barcode);

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductResponse productResponse = response.body();

                    if (productResponse.isFound()) {
                        Log.d(TAG, "Product found: " + productResponse.product.getDisplayName());
                        callback.onSuccess(productResponse);
                    } else {
                        Log.d(TAG, "Product not found in database");
                        callback.onNotFound();
                    }
                } else {
                    Log.e(TAG, "API response failed: " + response.code());
                    callback.onError("Failed to fetch product: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage(), t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * 产品查询回调接口
     */
    public interface ProductCallback {
        void onSuccess(ProductResponse product);
        void onNotFound();
        void onError(String errorMessage);
    }
}

