package com.xxha.xh.mypermission.permission;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.List;

/**
 * Created by Lm on 2017/10/23.
 * Email:1002464056@qq.com
 */

public class PermissionHelp {
    //传什么参数
    // 1.object Activity or Fragment   2,int 请求码 3.需要申请的权限数组
    private Object mObject;
    private int mRequestCode;
    private String[] mResuestPermission;


    public PermissionHelp(Object object){
        this.mObject=object;
    }
    // 以什么方式传参数

    //直接传参数
    public static void requestPermission(Activity activity,int requestCode,String[] permissions){
        PermissionHelp.with(activity).requestCode(requestCode).requestPermissions(permissions).request();
    }
    //链式传参

    // 传 Activity
    public static PermissionHelp with(Activity activity){
        return new PermissionHelp(activity);
    }
    // 传 Fragment
    public static PermissionHelp with(Fragment fragment){
        return new PermissionHelp(fragment);
    }
    // 添加请求码
    public PermissionHelp requestCode(int requestCode){
        this.mRequestCode=requestCode;
        return this;
    }
    // 添加请求的权限数组
    public PermissionHelp requestPermissions(String... permissions){
        this.mResuestPermission=permissions;
        return this;
    }
    //真正判断 和 发起请求权限
    public void request(){
        //判断是否是6.0及以上
        if (!PermissionUtils.isOverMarshmallow()) {
            //如果不是6.0及以上 那么直接执行方法  反射执行方法
            //执行什么方法不确定 那么我们执行采用注解的方式给方法打一个标记
            //然后通过反射去执行  注解+反射
            Log.d("TAG","所有方法1");
            PermissionUtils.executeSucceedMethod(mObject,mRequestCode);
            return;
        }
        //如果是6.0及以上，那么首先 判断权限是否授予
        //需要申请的权限中  获取没有授予的权限
        List<String> deniedPermissions=PermissionUtils.getDeniedPermission(mObject,mResuestPermission);
        //如果授予了，直接执行方法，反射执行方法
        if (deniedPermissions.size()==0) {
            //全部都授予了
            PermissionUtils.executeSucceedMethod(mObject,mRequestCode);
        }else {
            //如果没有授予，那么就申请权限
            ActivityCompat.requestPermissions(PermissionUtils.getActivity(mObject)
                    ,deniedPermissions.toArray(new String[deniedPermissions.size()])
                    ,mRequestCode);
        }

    }
    /**
     * 处理申请权限的回调
     * @param requestCode
     * @param permissions
     */
    public static void requestPermissionsResult(Object object,int requestCode, String[] permissions) {

        //再次获取没有授予的权限
        List<String> deniedPermissions=PermissionUtils.getDeniedPermission(object,permissions);
        if (deniedPermissions.size()==0) {
            //权限用户都同意授予了
            PermissionUtils.executeSucceedMethod(object,requestCode);
        }else {
            //你申请的权限中 有用户不同意的
            PermissionUtils.executeFailMethod(object,requestCode);
        }
    }


}
