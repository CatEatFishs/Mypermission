package com.xxha.xh.mypermission.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Lm on 2017/10/23.
 * Email:1002464056@qq.com
 */
@Target(ElementType.METHOD)//放在什么位置
// ElementType.METHOD放在方法上面,
// ElementType.TYPE放在类上面,
// ElementType.FIELD放在属性上面
@Retention(RetentionPolicy.RUNTIME)//是编译时检测 还是 运行时检测
public @interface PermissionFail {

    public int requestCode();
}
