package com.mingorto.project.daway2;

import android.arch.persistence.room.TypeConverter;

import com.mingorto.project.daway2.parsing.Comment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CommentsConverter {
    @TypeConverter
    public static List<Comment> fromString(String value) {
        Type listType = new TypeToken<List<Comment>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<Comment> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }


}
