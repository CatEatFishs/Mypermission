package com.xxha.xh.mypermission.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lm on 2017/10/23.
 * Email:1002464056@qq.com
 * 处理权限请求的工具类
 */

public class PermissionUtils {
    //这个类里面的方法都是静态方法 所以不能让别人new对象
    private PermissionUtils(){
        throw  new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断是不是6.0以上的版本
     * Marshmallow 棉花糖 6.0
     * @return
     */
    public static boolean isOverMarshmallow(){
        //Build.VERSION.SDK_INT 当前版本
        // Build.VERSION_CODES.M; 棉花糖版本
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 执行成功方法
     * @param reflectObject
     * @param requestCode
     */

    public static void executeSucceedMethod(Object reflectObject, int requestCode) {

        //获取class中所有的方法
        Method[] methods = reflectObject.getClass().getDeclaredMethods();
        //通过遍历找到我们打了标记的方法，
        for (Method method:methods){
            Log.d("TAG","所有方法"+method);
            //获取这个方法上有没有打成功的标记
            PermissionSucceed succeedMethod = method.getAnnotation(PermissionSucceed.class);
            //如果打了 succeedMethod不为null,没打就为null
            if (succeedMethod!=null) {
                //代表该方法打了标记
                // 并且请求码必须跟requestCode一样
                int methodCode = succeedMethod.requestCode();
                if (methodCode==requestCode) {
                    //一致 这就是我们要找的成功的方法
                    //反射执行该方法
                    Log.d("TAG","找到了该方法"+method);
                    executeMethod(reflectObject,method);
                }
            }

        }
    }

    /**
     * 反射执行该方法
     * @param reflectObject
     * @param method
     */
    private static void executeMethod(Object reflectObject, Method method) {
        //反射执行该方法 第一个参数是传 该方法所属于哪个类 第二个参数是传method方法内的参数（callPhone方法没有参数）
        try {
            method.setAccessible(true);//允许执行私有方法
            method.invoke(reflectObject,new Object[]{});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取没有授予的权限
     * @param object  Activity or Fragment
     * @param resuestPermissions
     * @return 没有授予过的权限
     */
    public static List<String> getDeniedPermission(Object object, String[] resuestPermissions) {
        List<String> deniedPermissions=new ArrayList<>();
        for (String requestPermission:resuestPermissions){
            //把没有授予的权限添加到集合
            if (ContextCompat.checkSelfPermission(getActivity(object),requestPermission)== PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(requestPermission);
            }
        }
        return deniedPermissions;
    }

    public static Activity getActivity(Object object) {
        if (object instanceof Activity) {
            return (Activity) object;
        }
        if (object instanceof Fragment) {
            return ((Fragment)object).getActivity();
        }
        return null;
    }


    public static void executeFailMethod(Object reflectObject, int requestCode) {

        //获取class中所有的方法
        Method[] methods = reflectObject.getClass().getDeclaredMethods();
        //通过遍历找到我们打了标记的方法，
        for (Method method:methods){
            Log.d("TAG","所有方法"+method);
            //获取这个方法上有没有打失败的标记
            PermissionFail failMethod = method.getAnnotation(PermissionFail.class);
            //如果打了 succeedMethod不为null,没打就为null
            if (failMethod!=null) {
                //代表该方法打了标记
                // 并且请求码必须跟requestCode一样
                int methodCode = failMethod.requestCode();
                if (methodCode==requestCode) {
                    //一致 这就是我们要找的成功的方法
                    //反射执行该方法
                    Log.d("TAG","找到了失败的方法"+method);
                    executeMethod(reflectObject,method);
                }
            }

        }
    }
}
