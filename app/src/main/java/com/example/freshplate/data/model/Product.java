package com.example.freshplate.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * 表示 OpenFoodFacts API 返回的产品详情
 */
public class Product {
    @SerializedName("product_name")
    public String productName;

    @SerializedName("product_name_en")
    public String productNameEn;

    @SerializedName("brands")
    public String brands;

    @SerializedName("image_url")
    public String imageUrl;

    @SerializedName("image_front_url")
    public String imageFrontUrl;

    @SerializedName("quantity")
    public String quantity;

    @SerializedName("categories")
    public String categories;

    /**
     * 获取产品名称（优先英文，其次通用名称）
     */
    public String getDisplayName() {
        if (productNameEn != null && !productNameEn.trim().isEmpty()) {
            return productNameEn;
        }
        if (productName != null && !productName.trim().isEmpty()) {
            return productName;
        }
        return "Unknown Product";
    }

    /**
     * 获取产品图片 URL（优先正面图片）
     */
    public String getDisplayImageUrl() {
        if (imageFrontUrl != null && !imageFrontUrl.trim().isEmpty()) {
            return imageFrontUrl;
        }
        return imageUrl;
    }
}

