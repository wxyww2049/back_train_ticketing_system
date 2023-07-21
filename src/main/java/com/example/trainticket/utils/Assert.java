package com.example.trainticket.utils;


import java.lang.reflect.Field;

public class Assert {

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static boolean nonNull(Object o){
        return o != null;
    }

    public static boolean isEmpty(String str){
        return str == null || str.isEmpty();
    }

    // 当未发现Null字段 返回true
    public static boolean parameterCheck(Object o) {
        boolean all = false;
        if (o.getClass().isAnnotationPresent(CheckNull.class)) {
            if (!o.getClass().getAnnotation(CheckNull.class).allowNull()) {
                all = true;
            }
        }
        for (Field field : o.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                if (isNull(field.get(o))) {
                    // 如果变量有NonNull注解并且allowNull为False
                    if (all || (field.isAnnotationPresent(CheckNull.class) && !field.getAnnotation(CheckNull.class).allowNull())) {
                        return false;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
