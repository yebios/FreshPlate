package com.example.freshplate.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * 表示 OpenFoodFacts API 的完整响应
 */
public class ProductResponse {
    @SerializedName("status")
    public int status; // 1 = 找到, 0 = 未找到

    @SerializedName("status_verbose")
    public String statusVerbose;

    @SerializedName("code")
    public String code; // 条码号

    @SerializedName("product")
    public Product product;

    /**
     * 判断产品是否找到
     */
    public boolean isFound() {
        return status == 1 && product != null;
    }
}

