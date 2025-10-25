package com.example.freshplate.data.local;

import androidx.room.TypeConverter;

import java.time.LocalDate;

public class Converters {
    /**
     * 将 String (数据库中存储的 "YYYY-MM-DD") 转换回 LocalDate 对象
     */
    @TypeConverter
    public static LocalDate toLocalDate(String dateString) {
        return dateString == null ? null : LocalDate.parse(dateString);
    }

    /**
     * 将 LocalDate 对象转换为 String (YYYY-MM-DD) 以便存入数据库
     */
    @TypeConverter
    public static String fromLocalDate(LocalDate date) {
        return date == null ? null : date.toString();
    }
}
