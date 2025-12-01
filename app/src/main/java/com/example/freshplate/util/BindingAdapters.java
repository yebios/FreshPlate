package com.example.freshplate.util;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.freshplate.R;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BindingAdapters {
    /**
     * 这是一个自定义的 Data Binding 适配器。
     * 它允许我们在 XML 中使用一个名为 "app:pantryItemStatus" 的新属性。
     *
     * @param view           应用此属性的 TextView
     * @param expirationDate 从 data binding 传入的 LocalDate
     */
    @BindingAdapter("pantryItemStatus")
    public static void setPantryItemStatus(TextView view, LocalDate expirationDate) {
        if (expirationDate == null) {
            view.setText("");
            return;
        }

        LocalDate today = LocalDate.now();
        long daysUntilExpired = ChronoUnit.DAYS.between(today, expirationDate);

        if (daysUntilExpired < 0) {
            // 已过期
            view.setText(R.string.expired);
            view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.status_expired));
        } else if (daysUntilExpired <= 3) {
            // 即将过期 (3天内)
            view.setText(R.string.soon);
            view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.status_soon));
        } else {
            // 新鲜
            view.setText(R.string.fresh);
            view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.status_fresh));
        }
    }

    /**
     * 使用 Glide 从 URL 加载图片到 ImageView
     * 允许在 XML 中使用 app:imageUrl="@{recipe.imageUrl}"
     */
    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        if (url != null && !url.isEmpty()) {
            Glide.with(view.getContext())
                    .load(url)
                    .transition(DrawableTransitionOptions.withCrossFade()) // 淡入效果
                    .placeholder(R.drawable.ic_placeholder) // 创建一个占位图
                    .error(R.drawable.ic_error)         // 创建一个错误图
                    .into(view);
        }
    }
}
