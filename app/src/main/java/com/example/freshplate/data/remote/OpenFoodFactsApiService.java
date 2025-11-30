package com.example.freshplate.data.remote;

import com.example.freshplate.data.model.ProductResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * OpenFoodFacts API 服务接口
 * API 文档: https://world.openfoodfacts.org/data
 */
public interface OpenFoodFactsApiService {
    /**
     * 根据条码查询产品信息
     * @param barcode 产品条码 (例如 "5449000000996")
     * @return 产品响应
     */
    @GET("api/v0/product/{barcode}.json")
    Call<ProductResponse> getProductByBarcode(@Path("barcode") String barcode);
}

