package com.alwin.app.alwinapp.ui.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alwin.app.alwinapp.R;
import com.alwin.app.alwinapp.utils.ApiUrl;
import com.alwin.app.alwinapp.utils.FileUtils;
import com.alwin.app.alwinapp.utils.SharePreferenceUtil;
import com.alwin.app.alwinapp.utils.ToastUtil;

import java.io.File;


public class LoadingUrlActivity extends Activity {
    private MyWebView webView;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private static final int REQ_CAMERA = FILECHOOSER_RESULTCODE + 1;
    private static final int REQ_CHOOSE = REQ_CAMERA + 1;
    private ValueCallback<Uri> mUploadMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alwin_web);
        webView = (MyWebView) findViewById(R.id.webView1);
        goUrl();
    }

    /**
     * 同步一下cookie
     */
    public static void synCookies(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        String cookiess = SharePreferenceUtil.getCookies(context);
        String newCoomk = cookiess + ";statuslock=0;";
        cookieManager.setCookie(url, cookiess + ";statuslock=0;");//cookies是在HttpClient中获得的cookie
        CookieSyncManager.getInstance().sync();
    }


    private void goUrl() {


        String url = "http://" + SharePreferenceUtil.getHost(getApplicationContext()) + ":" + SharePreferenceUtil.getPort(getApplicationContext()) + ApiUrl.HOMEPAGE_URL;
        synCookies(getApplicationContext(), url);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(1024 * 1024 * 4);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);

        webView.setWebChromeClient(new WebChromeClient() {


// For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                if (mUploadMessage != null) return;
                mUploadMessage = uploadMsg;
                selectImage();
            }

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooser(uploadMsg, "");
            }

            // For Android  > 4.1.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooser(uploadMsg, acceptType);
            }



            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    webView.getProgressbar().setVisibility(View.INVISIBLE);
                } else {
                    if (webView.getProgressbar().getVisibility() == View.INVISIBLE) {
                        webView.getProgressbar().setVisibility(View.VISIBLE);
                    }
                }
                webView.getProgressbar().setProgress(newProgress);
                super.onProgressChanged(view, newProgress);

            }


        });
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return false;
            }
            @Override
            public void onPageFinished(WebView view,String url){
                ToastUtil.showSortToast(getApplicationContext(),url);
                //处理退出后，清楚缓存的用户名和密码
               // ToastUtil.showLongToast(getApplicationContext(), view.getTitle());
                if(view.getTitle().equals("logout")){
                    SharePreferenceUtil.remove(getApplicationContext(),SharePreferenceUtil.USERNAME);
                    SharePreferenceUtil.remove(getApplicationContext(),SharePreferenceUtil.PASSWORD);
                    CookieSyncManager.createInstance(getApplicationContext());
                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.setAcceptCookie(true);
                    cookieManager.removeSessionCookie();//移除
                }
            }
        });
    }

    /**
     * 检查SD卡是否存在
     *
     * @return
     */
    public final boolean checkSDcard() {
        boolean flag = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (!flag) {
            Toast.makeText(this, "请插入手机存储卡再使用本功能", Toast.LENGTH_SHORT).show();
        }
        return flag;
    }

    String compressPath = "";

    protected final void selectImage() {
        if (!checkSDcard())
            return;
        String[] selectPicTypeStr = {"拍照", "相册"};
        new AlertDialog.Builder(this)
                .setItems(selectPicTypeStr,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                switch (which) {
                                    // 相机拍摄
                                    case 0:
                                        openCarcme();
                                        break;
                                    // 手机相册
                                    case 1:
                                        chosePic();
                                        break;
                                    default:
                                        break;
                                }
                                compressPath = Environment
                                        .getExternalStorageDirectory()
                                        .getPath()
                                        + "/wanying/temp";
                                new File(compressPath).mkdirs();
                                compressPath = compressPath + File.separator
                                        + "compress.jpg";
                            }
                        }).show();
    }

    String imagePaths;
    Uri cameraUri;

    /**
     * 打开照相机
     */
    private void openCarcme() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        imagePaths = Environment.getExternalStorageDirectory().getPath()
                + "/wanyin/temp/"
                + (System.currentTimeMillis() + ".jpg");
        // 必须确保文件夹路径存在，否则拍照后无法完成回调
        File vFile = new File(imagePaths);
        if (!vFile.exists()) {
            File vDirPath = vFile.getParentFile();
            vDirPath.mkdirs();
        } else {
            if (vFile.exists()) {
                vFile.delete();
            }
        }
        cameraUri = Uri.fromFile(vFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(intent, REQ_CAMERA);
    }

    /**
     * 拍照结束后
     */
    private void afterOpenCamera() {
        File f = new File(imagePaths);
        addImageGallery(f);
        File newFile = FileUtils.compressFile(f.getPath(), compressPath);
    }

    /**
     * 解决拍照后在相册中找不到的问题
     */
    private void addImageGallery(File file) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    /**
     * 本地相册选择图片
     */
    private void chosePic() {
        FileUtils.delFile(compressPath);
        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
        String IMAGE_UNSPECIFIED = "image/*";
        innerIntent.setType(IMAGE_UNSPECIFIED); // 查看类型
        Intent wrapperIntent = Intent.createChooser(innerIntent, null);
        startActivityForResult(wrapperIntent, REQ_CHOOSE);
    }

    /**
     * 选择照片后结束
     *
     * @param data
     */
    private Uri afterChosePic(Intent data) {

        // 获取图片的路径：
        String[] proj = {MediaStore.Images.Media.DATA};
        // 好像是android多媒体数据库的封装接口，具体的看Android文档
        Cursor cursor = managedQuery(data.getData(), proj, null, null, null);
        if (cursor == null) {
//            Toast.makeText(this, "上传的图片仅支持png或jpg格式", Toast.LENGTH_SHORT).show();
            return null;
        }
        // 按我个人理解 这个是获得用户选择的图片的索引值
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        // 将光标移至开头 ，这个很重要，不小心很容易引起越界
        cursor.moveToFirst();
        // 最后根据索引值获取图片路径
        String path = cursor.getString(column_index);
        File newFile = FileUtils.compressFile(path, compressPath);
//        if (path != null && (path.endsWith(".png") || path.endsWith(".PNG") || path.endsWith(".jpg") || path.endsWith(".JPG"))) {
//            File newFile = FileUtils.compressFile(path, compressPath);
//            return Uri.fromFile(newFile);
//        } else {
//            //Toast.makeText(this, "上传的图片仅支持png或jpg格式", Toast.LENGTH_SHORT).show();
//        }
        return Uri.fromFile(newFile);
        // return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (null == mUploadMessage)
            return;
        Uri uri = null;
        if (requestCode == REQ_CAMERA) {
            afterOpenCamera();
            uri = cameraUri;
        } else if (requestCode == REQ_CHOOSE) {
            uri = afterChosePic(intent);
        }
        mUploadMessage.onReceiveValue(uri);
        mUploadMessage = null;
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (webView.canGoBack()) {
//                webView.goBack();// 返回上一页面
//                return true;
//            } else {
//                System.exit(0);// 退出程序
//            }
//        }
//        return super.onKeyDown(keyCode, event);
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        } else {
            finish();
        }
        return super.onKeyDown(keyCode, event);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.removeAllViews();
        webView.destroy();
    }
}
