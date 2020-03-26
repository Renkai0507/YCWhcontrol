package tw.com.barwand.ks191113;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//import static tw.com.barwand.ks191113.R.id.txvResult4;

//import static tw.com.barwand.phptest01.R.string.data;

public class data_process extends AppCompatActivity {
    Button mbtnUpdate,mbtnExportA01,mbtnExportA02,mbtnDelA01,mbtnDelA02,mbtnUploadPHP,mbtnDownload,mbtnDManager;
    TextView mTxvResult,mTxvResult1,mTxvResult2,mTxvResult3,mTxvResult4;
    private final String PERMISSION_WRITE_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    //private final String PERMISSION_READ_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    DataBaseHandler dataBaseHandler=new DataBaseHandler(this);

    String fstrIP,fstrPort,fstrPass,fstrURL,fstrNameSpace,fstrInputPass,m_Input;

    private SharedPreferences settings;
    private static final String data = "DATA";
    private static final String ipField = "IP";
    private static final String portField = "PORT";
    private static final String passField = "PASS";
    private static final String urlField = "URL";
    private static final String nsField = "NAMESPACE";
    //private final String PERMISSION_WRITE_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_process);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 讓手機螢幕保持直立模式

        initComponent();
        CheckPermission();//取得存取權限
        readData(); //讀取各項設定值

        mbtnUploadPHP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileUpload mFileUpload = new FileUpload();
                        mFileUpload.setOnFileUploadListener(new FileUpload.OnFileUploadListener() {
                            @Override
                            public void onFileUploadSuccess(final String msg) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mTxvResult4.setText(msg);
                                    }
                                });
                            }

                            @Override
                            public void onFileUploadFail(final String msg) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mTxvResult4.setText(msg);
                                    }
                                });
                            }
                        });

                        //String urlString ="http://192.168.0.30:8881/fileUpload2.php";
                        String urlString ="http://" + fstrIP + ":" + fstrPort + "/fileUpload2.php";
                        mFileUpload.doFileUpload(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/A01.TXT",urlString);
                        //mFileUpload.doFileUpload(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/CAS.bmp");
                        //mTxvResult.setText(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" +fileName);
                        //mFileUpload.doFileUpload(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" +fileName);
                    }
                }).start();
            }
        });

        mbtnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String url_1="http://" + fstrIP + ":" + fstrPort + "/download/test01.txt";
                            String url_2="http://"+ fstrIP + ":" + fstrPort + "/download/app-debug.apk";
                            downLoadFromUrl(url_1, "T01.txt", path); //String urlStr, String fileName, String savePath
                            //downLoadFromUrl(url_2, "app-debug.apk", path); //String urlStr, String fileName, String savePath
                            //mTxvResult4.setText("info:" + url + " download success");
                            mTxvResult4.setText( "download success");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        mbtnDManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Context mContext;
                //mContext = getApplicationContext();

                Boolean result = isDownloadManagerAvailable(getApplicationContext());
                if (result) {
                    hasDownloadManager();
                }
            }
        });

        mbtnExportA01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExportA01(); //進貨數量匯總檔
                ExportA01_1(); //進貨序號明細檔
            }
        });

        mbtnExportA02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExportA02(); //進貨數量匯總檔
                ExportA02_1(); //進貨序號明細檔
            }
        });

        mbtnDelA01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(data_process.this);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("進貨資料刪除確認");
                builder.setMessage("是否刪除進貨資料?");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mTxvResult.setText("");
                        try{
                            dataBaseHandler.DeleteA01();
                            mTxvResult4.setTextColor(Color.BLUE);
                            mTxvResult4.setText("進貨資料刪除成功!!");
                            //Toast.makeText(Setup.this,"理貨資料刪除成功", Toast.LENGTH_SHORT).show();

                        }catch(Exception e){
                            Toast.makeText(data_process.this,"進貨資料刪除失敗", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            //顯示錯誤訊息
                            Writer writer = new StringWriter();
                            e.printStackTrace(new PrintWriter(writer));
                            String s = writer.toString();
                            mTxvResult4.setText(s);
                        }
                    }
                });

                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mTxvResult4.setText("");
                    }
                });
                builder.show();

            }
        });

        mbtnDelA02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(data_process.this);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("出貨資料刪除確認");
                builder.setMessage("是否刪除出貨資料?");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mTxvResult.setText("");
                        try{
                            dataBaseHandler.DeleteA02();
                            mTxvResult4.setTextColor(Color.BLUE);
                            mTxvResult4.setText("出貨資料刪除成功!!");
                            //Toast.makeText(Setup.this,"理貨資料刪除成功", Toast.LENGTH_SHORT).show();

                        }catch(Exception e){
                            Toast.makeText(data_process.this,"出貨資料刪除失敗", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            //顯示錯誤訊息
                            Writer writer = new StringWriter();
                            e.printStackTrace(new PrintWriter(writer));
                            String s = writer.toString();
                            mTxvResult4.setText(s);
                        }
                    }
                });

                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mTxvResult4.setText("");
                    }
                });
                builder.show();

            }
        });
    }

    //取得存取權限
    public void CheckPermission(){
        if (!hasPermission()) {
            if (needCheckPermission()) {
                //如果須要檢查權限，由於這個步驟要等待使用者確認，
                //所以不能立即執行儲存的動作，
                //必須在 onRequestPermissionsResult 回應中才執行
            }
        }
    }

    // 連接 layout 上的物件
    public void initComponent() {
        mTxvResult=(TextView)findViewById(R.id.txvResult) ;
        //mTxvResult1=(TextView)findViewById(R.id.txvResult1) ;
        //mTxvResult2=(TextView)findViewById(R.id.txvResult2) ;
        //mTxvResult3=(TextView)findViewById(R.id.txvResult3) ;
        mTxvResult4=(TextView)findViewById(R.id.txvResult4) ;

        mbtnExportA01=(Button)findViewById(R.id.btnExportA01);
        mbtnExportA02=(Button)findViewById(R.id.btnExportA02);
        mbtnDelA01=(Button)findViewById(R.id.btnDelA01);
        mbtnDelA02=(Button)findViewById(R.id.btnDelA02);

        mbtnUploadPHP=(Button)findViewById(R.id.btnUploadPHP);
        mbtnDownload=(Button)findViewById(R.id.btnDownload);
        mbtnDManager=(Button)findViewById(R.id.btnDManager);
    }

    public void readData(){
        //settings = getSharedPreferences(data,0);
        settings =getSharedPreferences("config.xml", MODE_PRIVATE);
        fstrIP=settings.getString(ipField, "");
        fstrPort=settings.getString(portField, "");
        fstrPass=settings.getString(passField, "");
        fstrURL="http://" + settings.getString(urlField, "");
        fstrNameSpace="http://" + settings.getString(nsField, "") + "/";;
    }

    public void ExportA01() {
        try {
            String fileName = "A01.txt";
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(path, fileName);

            // 第二個參數為是否 append
            // 若為 true，則新加入的文字會接續寫在文字檔的最後
            FileOutputStream Output = new FileOutputStream(file, false);

            ArrayList<String> listA01 = dataBaseHandler.getAllA01();
            //ArrayList<String> listA01=dataBaseHandler.getAllEmp();
            //Variable to hold all the values
            String output = "";
            String data = "";
            if (listA01.size()>0) {
                for (int i = 0; i < listA01.size(); i++) {
                    //Append all the values to a string
                    output = listA01.get(i)+"\r\n";
                    data+=output;
                }
                Output.write(data.getBytes());
                Output.close();
                mTxvResult4.setTextColor(Color.BLUE);
                mTxvResult4.setText("進貨資料匯出成功 [A01]!!");
                //Toast.makeText(Setup.this, "資料匯出成功", Toast.LENGTH_LONG).show();
            }
            else{
                mTxvResult4.setTextColor(Color.RED);
                mTxvResult4.setText("進貨無資料可匯出[A01]!!");
            }
        } catch (Exception e) {
            Toast.makeText(data_process.this, "進貨資料匯出失敗[A01]，請確認!!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            //顯示錯誤訊息
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            String s = writer.toString();
            mTxvResult4.setText(s);
        }
    }
    public void ExportA01_1() {
        try {
            String fileName = "A01_1.txt";
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(path, fileName);

            // 第二個參數為是否 append
            // 若為 true，則新加入的文字會接續寫在文字檔的最後
            FileOutputStream Output = new FileOutputStream(file, false);

            ArrayList<String> listA01 = dataBaseHandler.getAllA01List();
            //ArrayList<String> listA01=dataBaseHandler.getAllEmp();
            //Variable to hold all the values
            String output = "";
            String data = "";
            if (listA01.size()>0) {
                for (int i = 0; i < listA01.size(); i++) {
                    //Append all the values to a string
                    output = listA01.get(i)+"\r\n";
                    data+=output;
                }
                Output.write(data.getBytes());
                Output.close();
                mTxvResult4.setTextColor(Color.BLUE);
                mTxvResult4.setText(mTxvResult4.getText().toString() + "\r\n" + "進貨資料匯出成功[A01_1]!!");
                //Toast.makeText(Setup.this, "資料匯出成功", Toast.LENGTH_LONG).show();
            }
            else{
                mTxvResult4.setTextColor(Color.RED);
                mTxvResult4.setText(mTxvResult4.getText().toString() + "\r\n" +"進貨無資料可匯出[A01_1]!!");
            }
        } catch (Exception e) {
            Toast.makeText(data_process.this, "進貨資料匯出失敗[A01_1]，請確認!!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            //顯示錯誤訊息
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            String s = writer.toString();
            mTxvResult4.setText(s);
        }
    }

    public void ExportA02() {
        try {
            String fileName = "A02.txt";
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(path, fileName);

            // 第二個參數為是否 append
            // 若為 true，則新加入的文字會接續寫在文字檔的最後
            FileOutputStream Output = new FileOutputStream(file, false);

            ArrayList<String> listA02 = dataBaseHandler.getAllA02();
            //ArrayList<String> listA01=dataBaseHandler.getAllEmp();
            //Variable to hold all the values
            String output = "";
            String data = "";
            if (listA02.size()>0) {
                for (int i = 0; i < listA02.size(); i++) {
                    //Append all the values to a string
                    output = listA02.get(i)+"\r\n";
                    data+=output;
                }
                Output.write(data.getBytes());
                Output.close();
                mTxvResult4.setTextColor(Color.BLUE);
                mTxvResult4.setText("出貨資料匯出成功 [A02]!!");
                //Toast.makeText(Setup.this, "資料匯出成功", Toast.LENGTH_LONG).show();
            }
            else{
                mTxvResult4.setTextColor(Color.RED);
                mTxvResult4.setText("出貨無資料可匯出[A02]!!");
            }
        } catch (Exception e) {
            Toast.makeText(data_process.this, "出貨資料匯出失敗[A02]，請確認!!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            //顯示錯誤訊息
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            String s = writer.toString();
            mTxvResult4.setText(s);
        }
    }
    public void ExportA02_1() {
        try {
            String fileName = "A02_1.txt";
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(path, fileName);

            // 第二個參數為是否 append
            // 若為 true，則新加入的文字會接續寫在文字檔的最後
            FileOutputStream Output = new FileOutputStream(file, false);

            ArrayList<String> listA02 = dataBaseHandler.getAllA02List();
            //ArrayList<String> listA01=dataBaseHandler.getAllEmp();
            //Variable to hold all the values
            String output = "";
            String data = "";
            if (listA02.size()>0) {
                for (int i = 0; i < listA02.size(); i++) {
                    //Append all the values to a string
                    output = listA02.get(i)+"\r\n";
                    data+=output;
                }
                Output.write(data.getBytes());
                Output.close();
                mTxvResult4.setTextColor(Color.BLUE);
                mTxvResult4.setText(mTxvResult4.getText().toString() + "\r\n" + "出貨資料匯出成功[A02_1]!!");
                //Toast.makeText(Setup.this, "資料匯出成功", Toast.LENGTH_LONG).show();
            }
            else{
                mTxvResult4.setTextColor(Color.RED);
                mTxvResult4.setText(mTxvResult4.getText().toString() + "\r\n" +"出貨無資料可匯出[A02_1]!!");
            }
        } catch (Exception e) {
            Toast.makeText(data_process.this, "出貨資料匯出失敗[A02_1]，請確認!!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            //顯示錯誤訊息
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            String s = writer.toString();
            mTxvResult4.setText(s);
        }
    }

    /**
     * 从网络Url中下载文件
     *
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static void downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        //URL url = new URL("url from apk file is to be downloaded");
        //HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoOutput(true);


        //设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        conn.connect();

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        //mTxvResult4.setText("info:" + url + " download success");
        //System.out.println("info:" + url + " download success");
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
     * 確認是否要請求權限(API > 23)
     * API < 23 一律不用詢問權限
     */
    private boolean needCheckPermission() {
        //MarshMallow(API-23)之後要在 Runtime 詢問權限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {PERMISSION_WRITE_STORAGE};
            int permsRequestCode = 200;
            requestPermissions(perms, permsRequestCode);
            return true;
        }
        return false;
    }
    /**
     * 是否已經請求過該權限
     * API < 23 一律回傳 true
     */
    private boolean hasPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return(ActivityCompat.checkSelfPermission(this, PERMISSION_WRITE_STORAGE) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200){
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(">>>", "取得授權，可以執行動作了");
                    //doSavePicture();
                }
            }
        }
    }

    /**
     * @param context used to check the device version and DownloadManager information
     * @return true if the download manager is available
     */
    public static boolean isDownloadManagerAvailable(Context context) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                return false;
            }
            //Intent intent = new Intent(Intent.ACTION_MAIN);
            //intent.addCategory(Intent.CATEGORY_LAUNCHER);
            //intent.setClassName("com.android.providers.downloads.ui", "com.android.providers.downloads.ui.DownloadList");
            //List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
            //        PackageManager.MATCH_DEFAULT_ONLY);
            //return list.size() > 0;
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    private void hasDownloadManager(){
        String url = "http://192.168.0.30:8881/download/photo_01.gif";
        //String url_2="http://192.168.0.30:8881/download/app-debug.apk";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("下載測試");
        //request.setTitle("photo_01.gif");
        request.setTitle("photo_01.gif");
        // in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "photo_01.gif");
        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

}
