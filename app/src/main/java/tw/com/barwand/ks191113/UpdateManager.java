package tw.com.barwand.ks191113;

/**
 * apk更新管理类
 * Created by liu song on 2017/5/9.
 */

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.util.List;



public class UpdateManager {

    private volatile static UpdateManager INSTANCE;
    private static Context context;
    private DownloadManager downloadManager;
    private PackageInfo apkInfo;

    private UpdateManager(Context context) {
        this.context = context;
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public static UpdateManager getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (UpdateManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UpdateManager(context);
                }
            }
        }
        return INSTANCE;
    }

    public void downloadApk(String url) {
        if (!isDownloadManagerAvailable()) {
            startDownloadManager();
        } else {
            download(url);
        }
    }

    /**
     * 判断PackageManager组件下载管理是否可用
     * PackageManager主要是管理应用程序包，通过它就可以获取应用程序信息
     *
     * @return
     */
    private boolean isDownloadManagerAvailable() {
        try {
            int state = context.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 启用/禁用 下载管理界面
     */
    private void startDownloadManager() {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        if (intentAvailable(intent)) {
            context.startActivity(intent);
        }
    }

    /**
     * intent是否有效
     *
     * @param intent
     * @return
     */
    private boolean intentAvailable(Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        //查找满足intent条件的Activity
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 通过判断来进行安装或者下载
     *
     * @param url
     */
    private void download(String url) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        long downloadId = sp.getLong(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
        if (downloadId != -1L) {
            int status = getDownloadStatus(downloadId);
            //下载成功
            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                Uri uri = downloadManager.getUriForDownloadedFile(downloadId);
                if (uri != null) {
                    apkInfo = getApkInfo(uri.getPath());
                    if (isUpadateAvailable(apkInfo)) {
                        //更新可用,直接安装
                        startInstall(uri);
                    } else {
                        //不可更新说明这是以前的下载，则重新下载
                        downloadManager.remove(downloadId);
                        startDownload(url);
                    }
                }
            } else {
                //下载失败，重新下载
                downloadManager.remove(downloadId);
                startDownload(url);
            }
        } else {
            //开始新的下载
            startDownload(url);
        }
    }

    /**
     * 开启下载apk
     *
     * @param url
     */
    private void startDownload(String url) {
        long id = startDownloadFile(url);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putLong(DownloadManager.EXTRA_DOWNLOAD_ID, id).commit();
    }

    /**
     * 到安装界面安装apk
     *
     * @param uri
     */
    private void startInstall(Uri uri) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(install);
    }

    /**
     * 获取apk程序信息[packageName,versionName...]
     *
     * @param path
     */
    private PackageInfo getApkInfo(String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            return info;
        }
        return null;
    }

    /**
     * 下载的apk和当前程序版本比较
     *
     * @param apkInfo apk file's packageInfo
     * @return 如果当前应用版本小于apk的版本则返回true
     */
    private boolean isUpadateAvailable(PackageInfo apkInfo) {
        if (apkInfo == null) {
            return false;
        }
        String localPackage = context.getPackageName();
        if (apkInfo.packageName.equals(localPackage)) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(localPackage, 0);
                if (apkInfo.versionCode > packageInfo.versionCode) {
                    return true;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 根据下载id,获取下载状态
     *
     * @return int
     * @see DownloadManager#STATUS_PENDING
     * @see DownloadManager#STATUS_PAUSED
     * @see DownloadManager#STATUS_RUNNING
     * @see DownloadManager#STATUS_SUCCESSFUL
     * @see DownloadManager#STATUS_FAILED
     */
    private int getDownloadStatus(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                }
            } finally {
                c.close();
            }
        }
        return -1;
    }

    /**
     * 开启下载
     *
     * @param url
     * @return download id
     */
    private long startDownloadFile(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //设置用于下载时的网络类型，默认任何网络都可以下载，提供的网络常量有：NETWORK_BLUETOOTH、NETWORK_MOBILE、NETWORK_WIFI.
        /*request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);*/
        //设置漫游状态下是否可以下载
        /*request.setAllowedOverRoaming(false);*/
        //下载完成后保留下载的notification
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        //设置Notification的title信息
        request.setTitle("更新");
        //设置Notification的message信息
        request.setDescription("下载完成后点击安装");

        //设置文件的保存的位置[三种方式]
        //第一种
        //file:///storage/emulated/0/Android/data/your-package/files/Download/update.apk
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "app" + ".apk");
        //第二种
        //file:///storage/emulated/0/Download/update.apk
        //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "update.apk");
        //第三种 自定义文件路径
        //request.setDestinationUri()

        // 设置下载文件的mineType。因为下载管理Ui中点击某个已下载完成文件及下载完成点击通知栏提示都会根据mimeType去打开文件，
        // 所以我们可以利用这个属性用于响应点击的打开文件
        request.setMimeType("application/vnd.android.package-archive"); //apk
        return downloadManager.enqueue(request);//异步
    }
}
