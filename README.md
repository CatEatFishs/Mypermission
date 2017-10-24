# Mypermission
6.0申请动态权限
利用反射加注解的方式
具体使用：
1.
  //申请权限
  public void clickPhone(){
        PermissionHelp.with(this)
                .requestCode(CALL_PHONE_REQUESTCODE)
                .requestPermissions(new String[]{Manifest.permission.CALL_PHONE})
                .request();
    }

2.
  //权限回调
  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelp.requestPermissionsResult(this,requestCode,permissions);
    }

3.
   //注解成功执行的方法
   @PermissionSucceed(requestCode = CALL_PHONE_REQUESTCODE)
    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:10086");
        intent.setData(data);
        startActivity(intent);
    }
   //注解失败执行的方法
    @PermissionFail(requestCode = CALL_PHONE_REQUESTCODE)
    private void callPhoneFail() {
        Toast.makeText(this, "用户拒接拨打电话", Toast.LENGTH_SHORT).show();
    }
