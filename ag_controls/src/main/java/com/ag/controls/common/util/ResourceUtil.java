package com.ag.controls.common.util;

import android.content.Context;

import java.lang.reflect.Field;

public class ResourceUtil {

    /**
     * 获取Drawble-xx目录下的静态图片文件
     *
     * @param context
     * @param name
     * @return
     */
    public static int getDrawbleByName(Context context, String name) {
        return getValueIntByName(context, name, "drawable");
    }

    /**
     * 获取Layout-xx目录下的静态布局
     *
     * @param context
     * @param name
     * @return
     */
    public static int getLayoutByName(Context context, String name) {
        return getValueIntByName(context, name, "layout");
    }

    /**
     * 获取ID目录下的控件或值
     *
     * @param context
     * @param name
     * @return
     */
    public static int getIdByName(Context context, String name) {
        return getValueIntByName(context, name, "id");
    }

    /**
     * 获取Layout-xx目录下的静态布局
     *
     * @param context
     * @param name
     * @return
     */
    public static int getStringByName(Context context, String name) {
        return getValueIntByName(context, name, "string");
    }

    /**
     * 获取values-xx目录下的静态布局
     *
     * @param context
     * @param name
     * @return
     */
    public static int getDimenByName(Context context, String name) {
        return getValueIntByName(context, name, "dimen");
    }

    public static int getAnimByName(Context context, String name) {
        return getValueIntByName(context, name, "anim");
    }

    public static int getColorByName(Context context, String name) {
        return getValueIntByName(context, name, "color");
    }

    /**
     * 获取项目的静态文件或控件
     *
     * @param context
     * @param name
     * @param type    drawble/layout/id
     * @return
     */
    public static int getValueIntByName(Context context, String name,
                                        String type) {
        return context.getResources().getIdentifier(name, type,
                context.getPackageName());
    }

    /**
     * 对于 context.getResources().getIdentifier 无法获取的数据 , 或者数组
     * <p/>
     * 资源反射值
     *
     * @param name
     * @param type
     * @return
     * @paramcontext
     */
    private static Object getResourceId(Context context, String name,
                                        String type) {
        String className = context.getPackageName() + ".R";
        try {
            Class<?> cls = Class.forName(className);
            for (Class<?> childClass : cls.getClasses()) {
                String simple = childClass.getSimpleName();
                if (simple.equals(type)) {
                    for (Field field : childClass.getFields()) {
                        String fieldName = field.getName();
                        if (fieldName.equals(name)) {
                            System.out.println(fieldName);
                            return field.get(null);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * context.getResources().getIdentifier 无法获取到 styleable 的数据
     *
     * @param name
     * @return
     * @paramcontext
     */
    public static int getStyleable(Context context, String name) {
        return ((Integer) getResourceId(context, name, "styleable")).intValue();
    }

    /**
     * 获取 styleable 的 ID 号数组
     *
     * @param name
     * @return
     * @paramcontext
     */
    public static int[] getStyleableArray(Context context, String name) {
        return (int[]) getResourceId(context, name, "styleable");
    }
}
