package com.xxha.xh.mypermission;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xxha.xh.mypermission.permission.PermissionFail;
import com.xxha.xh.mypermission.permission.PermissionHelp;
import com.xxha.xh.mypermission.permission.PermissionSucceed;


public class MainActivity extends AppCompatActivity {

    private static final int CALL_PHONE_REQUESTCODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view) {
       clickPhone();
        Log.d("TAG","所有方法");
    }
    public void clickPhone(){
       /** // 1.先判断有没有权限
        int isPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        // 2.如果有权限，则直接调用拨打电话方法
        if (PackageManager.PERMISSION_GRANTED==isPermission) {
            callPhone();
        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},CALL_PHONE_REQUESTCODE);
        }
        */

        /** PermissionHelp.requestPermission(this,
                                            CALL_PHONE_REQUESTCODE,new String[]{Manifest.permission.CALL_PHONE});
         */
        PermissionHelp.with(this)
                .requestCode(CALL_PHONE_REQUESTCODE)
                .requestPermissions(new String[]{Manifest.permission.CALL_PHONE})
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode==CALL_PHONE_REQUESTCODE) {
//            if (grantResults!=null && grantResults.length>0) {
//                if (grantResults[0]== PackageManager.PERMISSION_GRANTED) {
//                    callPhone();
//                }else {
//                    Toast.makeText(this, "用户拒接拨打电话", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
        PermissionHelp.requestPermissionsResult(this,requestCode,permissions);
    }

    @PermissionSucceed(requestCode = CALL_PHONE_REQUESTCODE)
    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:10086");
        intent.setData(data);
        startActivity(intent);
    }
    @PermissionFail(requestCode = CALL_PHONE_REQUESTCODE)
    private void callPhoneFail() {
        Toast.makeText(this, "用户拒接拨打电话", Toast.LENGTH_SHORT).show();
    }

}
