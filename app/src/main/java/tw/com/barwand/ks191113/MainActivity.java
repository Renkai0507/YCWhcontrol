package tw.com.barwand.ks191113;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

import static tw.com.barwand.ks191113.GlobalVariable.Errbeep;


public class MainActivity extends AppCompatActivity {
    RequestQueue mQueue;
    StringRequest mGetRequest;
    String mUrl = "",OutputData = "";

    //appName: "八旺測試APP",
    //服务器版本号//serverVersion: "2",
    //服务器标志//serverFlag: "1",
    //是否强制更新//lastForce: "1",
    //apk下载地址 : //updateurl: "http://192.168.1.103:8080/36Kr.apk",
    //版本的更新的描述//upgradeinfo: "版本更新:V1.1.1 (測試)"
    String mclientVersion,mappName,mserverVersion,mupgradeInfo,mserverFlag,mlastForce,mupdateUrl;
    private static final String mApkName= "app-debug.apk";

    EditText mEdtSelect;
    TextView mtxvTitle2;
    TextView mTxvResult,mTxvFuncID;
    Button  mbtnDataProcess,mbtnExit,mbtnSetPara,mbtnWorkPrint01,mbtnPHP01,mbtnSOAP01,mbtnUpdate,mbtnPhoto01;
    Button mbtnWritePad01;
    Button mbtnOp5000,mbtnOp9000,mbtnOp5001,mbtnOp5002,mbtnOp5003,mbtnOp5004,mbtnOp5005;
    String  fstrIP,fstrPort,fstrPass,fstrURL,fstrNameSpace,fstrBT,fstrOraTNS,fstrDep_ID,fstrMac_ID,fstrHouseC,fstrEmpNo;
    String fstrInputPass,m_Input;

    String[] resultData = new String[12];

    private static final int O_OUTPUT = 0;
    private static final int O_YYYYMM = 1;
    private static final int REQUEST_Emp = 1;

    private SharedPreferences settings;
    private static final String data = "DATA";
    private static final String ipField = "IP";
    private static final String portField = "PORT";
    private static final String passField = "PASS";
    private static final String urlField = "URL";
    private static final String nsField = "NAMESPACE";
    private static final String btField = "BT";
    private static final String tnsField = "TNS";
    private static final String depidField = "DEPID";
    private static final String macidField = "MACID";
    private static final String housecField = "HOUSEC";
    private static final String volumeField = "VOLUME";

    private final String PERMISSION_WRITE_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 讓手機螢幕保持直立模式

        //取的intent中的bundle物件
        Bundle bundle =this.getIntent().getExtras();
        fstrEmpNo=bundle.getString("Emp");

        initComponent();
        readData(); //讀取各項設定值
        CheckPermission();//取得存取權限
        //volley_check_release();//檢查主機端更新版本

        mclientVersion=getVersion(this);
        //mtxvTitle2.setText(mtxvTitle2.getText().toString() + " v" + mclientVersion);
        //mtxvTitle2.setText( " v" + mclientVersion + " (Beta)");
        mtxvTitle2.setText( " v" + mclientVersion );

        mEdtSelect.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                mEdtSelect.setText("");

                if (i == KeyEvent.KEYCODE_1 && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    startOp5000(view);
                    //checkOp5000();
                    return true;
                }else if (i == KeyEvent.KEYCODE_2 && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    startOp5001(view);
                    //checkOp5000();
                    return true;
                }else if (i == KeyEvent.KEYCODE_3 && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    startOp5002(view);
                    //checkOp5000();
                    return true;
                }
                else{
                    hideSoftKeyboard();//隱藏 keyboard
                    mEdtSelect.setText("");
                    mEdtSelect.requestFocus();
                    return false;
                }

                //else if (i == KeyEvent.KEYCODE_7 && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                //startInputDialog(view);  //進入帳號檢查的輸入畫面
                //return true;
            }
        });

        mbtnOp5000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startOp5000(view);
                //checkOp5000();
            }
        });

        mbtnOp5001.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startOp5001(view);
                //checkOp5000();
            }
        });

        mbtnOp5002.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startOp5002(view);
                //checkOp5000();
            }
        });

        mbtnOp5003.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startOp5003(view);
                //checkOp5000();
            }
        });
        mbtnOp5005.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startOp5005(view);
                //checkOp5000();
            }
        });
        mbtnOp5004.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startOp5004(view);
                //checkOp5000();
            }
        });
        mbtnDataProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSetup(view);
            }
        });

        mbtnWorkPrint01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWorkPrint01(view);
            }
        });

        mbtnSetPara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startInputDialog(view);  //進入帳號檢查的輸入畫面
                //startSetupPara(view); //直接進入設定畫面
                fstrEmpNo="";
                finish();
            }
        });

        mbtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTxvFuncID.setText("UPDATE");
                mTxvResult.setText("");
                volley_check_release();//檢查主機端更新版本

                /*
                            boolean flag=mclientVersion.equals(mserverVersion); // equals 效率比 compareTo 高.
                            if (flag == false && !mserverVersion.equals("")){
                                showUpdataDialog(); //APP 程式更新
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"目前已是最新版!!",Toast.LENGTH_SHORT).show();
                            }
                            */

                //showUpdataDialog(); //APP 程式更新
            }
        });

        mbtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("結束確認");
                builder.setMessage("是否確定要離開程式?");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //finish();
                        MainActivity.super.onDestroy();
                        android.os.Process.killProcess(android.os.Process.myPid()); //獲取PID
                        //System.exit(0); //常規java、c#的標準退出法，返回值為0代表正常退出
                    }
                });

                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });

        mTxvResult.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String strFuncID,strResult;
                strFuncID=mTxvFuncID.getText().toString();
                strResult=mTxvResult.getText().toString();

                /*
                                    if (strFuncID.matches("UPDATE") && strResult.matches("OK")){
                                        boolean flag=mclientVersion.equals(mserverVersion); // equals 效率比 compareTo 高.
                                        if (flag == false && !mserverVersion.equals("")){
                                            showUpdataDialog(); //APP 程式更新
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),"目前已是最新版!!",Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    */
                switch(strFuncID){
                    case "UPDATE":
                        if (strResult.matches("OK")){
                            boolean flag=mclientVersion.equals(mserverVersion); // equals 效率比 compareTo 高.
                            if (flag == false && !mserverVersion.equals("")){
                                showUpdataDialog(); //APP 程式更新
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"目前已是最新版!!",Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;

                    case "Check_TMPP001_GET_UKEIRE_NO":
                        //startOp8000();
                        /*
                                        if (strResult.length()==11){
                                            //hideSoftKeyboard();//隱藏 keyboard
                                            startOp8000();
                                        }
                                        else if (strResult.matches("")) {
                                            //hideSoftKeyboard();//隱藏 keyboard
                                            //Nothing
                                        }
                                        else {
                                            Errbeep(getApplicationContext(),"錯誤,請聯絡管理員!!",1000,true,1000);
                                            mTxvFuncID.setText("");
                                            mTxvResult.setText("錯誤,請聯絡管理員!!");
                                        }
                                        */
                        break;

                    case "hs_search":
                        if (strResult.matches("Y")){
                            //startOp5000(View);
                        }
                        else if (strResult.matches("N")) {
                            Errbeep(getApplicationContext(),"錯誤,無盤點主檔!!",1000,true,1000);
                            mTxvFuncID.setText("");
                            mTxvResult.setText("錯誤,無盤點主檔!!");
                        }
                        else {
                            Errbeep(getApplicationContext(),"錯誤,請聯絡管理員!!",1000,true,1000);
                            mTxvFuncID.setText("");
                            mTxvResult.setText("錯誤,請聯絡管理員!!");
                        }
                        break;
                }
                //else if (strFuncID.matches("A03") && strResult.matches("OK")) {
                //    //hideSoftKeyboard();//隱藏 keyboard
                //}

            }
        });
        //startInputDialog();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            ConfirmExit();//按返回鍵，則執行退出確認
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void ConfirmExit(){//退出確認
        AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this);
        ad.setTitle("登出");
        ad.setMessage("確定要登出系統嗎?");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() {//退出按鈕
            public void onClick(DialogInterface dialog, int i) {
                // TODO Auto-generated method stub
                MainActivity.this.finish();//關閉activity
            }
        });
        ad.setNegativeButton("否",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                //不退出不用執行任何操作
            }
        });
        ad.show();//顯示對話框
    }


    //隱藏 Soft Keyboard
    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        //imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    //顯示 Soft Keyboard
    public void showSoftKeyboard() {
        InputMethodManager imm = ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE));
        //imm.toggleSoftInput();
        int SoftInputAnchor = R.id.edtSelect;
        imm.toggleSoftInput(SoftInputAnchor,0);
    }

    // 連接 layout 上的物件
    public void initComponent() {
        mEdtSelect= (EditText) findViewById(R.id.edtSelect);
        mTxvResult = (TextView) findViewById(R.id.txvResult);
        mTxvFuncID = (TextView) findViewById(R.id.txvFuncID);


        mbtnWorkPrint01 = (Button) findViewById(R.id.btnWorkPrint);
        mbtnPHP01 = (Button) findViewById(R.id.btnPHP01);
        mbtnSOAP01 = (Button) findViewById(R.id.btnSoap01);
        mbtnDataProcess = (Button) findViewById(R.id.btnDataProcess);
        mbtnSetPara = (Button) findViewById(R.id.btnSetPara);
        mbtnUpdate = (Button) findViewById(R.id.btnUpdate);
        mbtnExit = (Button) findViewById(R.id.btnExit);
        mtxvTitle2=(TextView) findViewById(R.id.txvTitle2);

        mbtnPhoto01 = (Button) findViewById(R.id.btnPhoto01);
        mbtnWritePad01= (Button) findViewById(R.id.btnWritePad01);

        mbtnOp5000 = (Button) findViewById(R.id.btnOp5000);
        mbtnOp9000 = (Button) findViewById(R.id.btnOp9000);
        mbtnOp5001 = (Button) findViewById(R.id.btnOp5001);
        mbtnOp5002 = (Button) findViewById(R.id.btnOp5002);
        mbtnOp5003 = (Button) findViewById(R.id.btnOp5003);
        mbtnOp5004 = (Button) findViewById(R.id.btnOp5004);
        mbtnOp5005 = (Button) findViewById(R.id.btnOp5005);

        //mbtnOp1000.setEnabled(false);
        //mbtnOp2000.setEnabled(false);
        //mbtnOp3000.setEnabled(false);
        //mbtnOp4000.setEnabled(false);
        //mbtnOp5000.setEnabled(false);
        //mbtnOp6000.setEnabled(false);
        //mbtnOp8000.setEnabled(false);
        //mbtnOp9000.setEnabled(false);
        //mbtnOpA000.setEnabled(false);
        //mbtnOpB000.setEnabled(false);
        //mbtnOpD000.setEnabled(false);

        mEdtSelect.requestFocus();
    }

    public void readData(){
        //settings = getSharedPreferences(data,0);
        //settings =getSharedPreferences("config.xml", MODE_PRIVATE);
        //fstrIP=settings.getString(ipField, "");
        //fstrPort=settings.getString(portField, "");
        //fstrPass=settings.getString(passField, "");
        //fstrURL="http://" + settings.getString(urlField, "");
        //fstrNameSpace="http://" + settings.getString(nsField, "") + "/";;

        settings =getSharedPreferences("config.xml", MODE_PRIVATE);
        //使用Global variable(全域變數)
        GlobalVariable globalVar = (GlobalVariable)getApplicationContext();
        globalVar.g_strIP=settings.getString(ipField, "");
        globalVar.g_strPort=settings.getString(portField, "");
        globalVar.g_strPass=settings.getString(passField, "");
        globalVar.g_strURL="http://" + settings.getString(urlField, "");
        globalVar.g_strNameSpace="http://" + settings.getString(nsField, "") + "/";
        globalVar.g_strBT=settings.getString(btField, "");
        globalVar.g_strOraTNS=settings.getString(tnsField, "");
        globalVar.g_strDep_ID=settings.getString(depidField, "");
        globalVar.g_strMac_ID=settings.getString(macidField, "");
        globalVar.g_strHouseC=settings.getString(housecField, "");
        globalVar.g_strVolume=settings.getString(volumeField, "");

        fstrIP=globalVar.g_strIP;
        fstrPort=globalVar.g_strPort;
        fstrPass=globalVar.g_strPass;
        fstrURL=globalVar.g_strURL;
        fstrNameSpace=globalVar.g_strNameSpace;
        fstrBT=globalVar.g_strBT;
        fstrOraTNS=globalVar.g_strOraTNS;
        fstrDep_ID=globalVar.g_strDep_ID;
        fstrMac_ID=globalVar.g_strMac_ID;
        fstrHouseC=globalVar.g_strHouseC;
    }

    public void startOp5000(View view) {
        //Intent it = new Intent(this, op5000.class);
        //startActivity(it);
        Intent it = new Intent(this,op5000.class);
        //new一個Bundle物件，並將要傳遞的資料傳入
        Bundle bundle = new Bundle();
        //bundle.putDouble("SearchType","");
        bundle.putString("Emp", fstrEmpNo);
        //將Bundle物件assign給intent
        it.putExtras(bundle);
        //startActivity(it);
        startActivityForResult(it, REQUEST_Emp);
    }

    public void startOp5001(View view) {
        //Intent it = new Intent(this, op5001.class);
        //startActivity(it);
        Intent it = new Intent(this,op5001.class);
        //new一個Bundle物件，並將要傳遞的資料傳入
        Bundle bundle = new Bundle();
        //bundle.putDouble("SearchType","");
        bundle.putString("Emp", fstrEmpNo);
        //將Bundle物件assign給intent
        it.putExtras(bundle);
        //startActivity(it);
        startActivityForResult(it, REQUEST_Emp);
    }
    
    public void startOp5002(View view) {
        //Intent it = new Intent(this, op5002.class);
        //startActivity(it);

        Intent it = new Intent(this,op5002.class);
        //new一個Bundle物件，並將要傳遞的資料傳入
        Bundle bundle = new Bundle();
        //bundle.putDouble("SearchType","");
        bundle.putString("Emp", fstrEmpNo);
        //將Bundle物件assign給intent
        it.putExtras(bundle);
        //startActivity(it);
        startActivityForResult(it, REQUEST_Emp);
    }

    public void startOp5003(View view) {
        //Intent it = new Intent(this, op5003.class);
        //startActivity(it);
        Intent it = new Intent(this,op5003.class);
        //new一個Bundle物件，並將要傳遞的資料傳入
        Bundle bundle = new Bundle();
        //bundle.putDouble("SearchType","");
        bundle.putString("Emp", fstrEmpNo);
        //將Bundle物件assign給intent
        it.putExtras(bundle);
        //startActivity(it);
        startActivityForResult(it, REQUEST_Emp);
    }
    public void startOp5004(View view) {
        //Intent it = new Intent(this, op5003.class);
        //startActivity(it);
        Intent it = new Intent(this,op5004.class);
        //new一個Bundle物件，並將要傳遞的資料傳入
        Bundle bundle = new Bundle();
        //bundle.putDouble("SearchType","");
        bundle.putString("Emp", fstrEmpNo);
        //將Bundle物件assign給intent
        it.putExtras(bundle);
        //startActivity(it);
        startActivityForResult(it, REQUEST_Emp);
    }
    public void startOp5005(View view) {
        //Intent it = new Intent(this, op5003.class);
        //startActivity(it);
        Intent it = new Intent(this,op5005.class);
        //new一個Bundle物件，並將要傳遞的資料傳入
        Bundle bundle = new Bundle();
        //bundle.putDouble("SearchType","");
        bundle.putString("Emp", fstrEmpNo);
        //將Bundle物件assign給intent
        it.putExtras(bundle);
        //startActivity(it);
        startActivityForResult(it, REQUEST_Emp);
    }

    /*public void startOp5000() {
        Intent it = new Intent(this, op5000.class);

        //new一個Bundle物件，並將要傳遞的資料傳入
        Bundle bundle = new Bundle();
        //bundle.putDouble("Weight",height );
        bundle.putString("YYYYMM", resultData[O_YYYYMM]);

        //將Bundle物件assign給intent
        it.putExtras(bundle);
        startActivity(it);
    }*/


    public void startSetup(View view) {
        Intent it = new Intent(this, data_process.class);
        startActivity(it);
    }
    public void startSetupPara(View view) {
        Intent it = new Intent(this, set_para.class);
        startActivity(it);
    }
    public void startWorkPrint01(View view) {
        Intent it = new Intent(this, WorkPrint01.class);
        startActivity(it);
    }
    public void startInputDialog() {
        //Intent it = new Intent(this, input_dialog.class);
        //startActivity(it);
        /*Intent it = new Intent(this, input_dialog.class);
        it.setFlags(it.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
        startActivity(it);*/


        Intent intent = new Intent();
        intent.setClass(this, input_dialog.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //注意本行的FLAG設定
        startActivity(intent);
    }
    private void showInputDialog() {
    //@setView 装入一个EditView
        String pass;
        final EditText editText = new EditText(MainActivity.this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(MainActivity.this);
        inputDialog.setTitle("請輸入密碼:").setView(editText);
        inputDialog.setPositiveButton("確定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //dialog.dismiss();
                        fstrInputPass=editText.getText().toString();
                        //Toast.makeText(MainActivity.this,
                        //        fstrInputPass,
                        //        Toast.LENGTH_SHORT).show();


                        //fstrInputPass=editText.getText().toString();
                    }
                }).show();
    }


    public void checkOp8000() {
        //Intent it = new Intent(this, photo01.class);
        //startActivity(it);

        mTxvFuncID.setText("Check_TMPP001_GET_UKEIRE_NO");
        //mTxvResult.setText("");
        //new WSAsyncTask().execute("Check_TMPP001_GET_UKEIRE_NO",fstrOraTNS);
        volley_post(mTxvFuncID.getText().toString());

    }

    public void checkOp5000() {
        //Intent it = new Intent(this, photo01.class);
        //startActivity(it);

        //mTxvFuncID.setText("KS_YYYYMM");
        mTxvFuncID.setText("hs_search");
        //mTxvResult.setText("");
        //new WSAsyncTask().execute("Check_TMPP001_GET_UKEIRE_NO",fstrOraTNS);
        volley_post(mTxvFuncID.getText().toString());

    }

    /****
     * 獲取移動端版本號
     * 格式可根據比較方式自己設定
     *
     * @param activity
     * @return
     */
    public static final String getVersion(Activity activity) {
        try {
            PackageManager manager = activity.getPackageManager();
            PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
            String version =info.versionCode + "." + info.versionName;
            return  version;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 弹出对话框
     */
    protected void showUpdataDialog() {
        //先刪除之前的APK檔
        String ApkName=mApkName;// "app-debug.apk";
        String fileString =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()+
                File.separator+ApkName;
        File file_ApkName = new File(fileString);
        if (file_ApkName.exists()){
            file_ApkName.delete();
        }

        //開始進入更新程式
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(android.R.drawable.sym_def_app_icon);
        builder.setTitle("版本升级");
        builder.setMessage("是否要更新軟體?" + "\n" + mupgradeInfo);
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                downLoadApk();
            }
        });

        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    protected void downLoadApk() {
        //进度条
        final ProgressDialog pd;
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下載更新....");
        pd.show();
        new Thread(){
            @Override
            public void run() {
                try {
                    //File file = getFileFromServer("http://192.168.0.30:8881/download/app-debug.apk", pd);
                    File file = getFileFromServer(mupdateUrl, pd);
                    //String mApkName= "DownloadManagerDemo" + "_v" + apkInfo.getVersionName() + ".apk";
                    String ApkName=mApkName;//"app-debug.apk";
                    String uriString =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()+
                            File.separator+ApkName;
                    //installApk(file,uriString);
                    installApkByGuide(uriString);   // 7.0以後
                    //installApkByIntent(file);   // 7.0以前
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                }
            }}.start();
    }
    public static File getFileFromServer(String path, ProgressDialog pd) throws Exception{
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            //File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath(), mApkName);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len ;
            int total=0;
            while((len =bis.read(buffer))!=-1){
                fos.write(buffer, 0, len);
                total+= len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        }
        else{
            return null;
        }
    }


    //安装apk  (7.0以前 > 使用Intent来跳转 )
    protected void installApk(File file,String uriString) {
        //版本在 7.0以上是不能直接通过 uri 連結的
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri;
            if(Build.VERSION.SDK_INT >= 24){
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                uri = FileProvider.getUriForFile(this, this.getPackageName() + ".provider", new File(uriString));
            }else{
                uri=Uri.fromFile(new File(uriString));
            }
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            this.startActivity(intent);
            Log.d(">>>", "7.0以上");
        }
        else {
            Intent intent = new Intent();
            //执行动作
            intent.setAction(Intent.ACTION_VIEW);
            //执行的数据类型
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            startActivity(intent);
            Log.d(">>>", "7.0以下");
        }
    }

    //檢查檔案是否存
    private boolean checkFileExist(File file_name,boolean del_flag) {
        if (!file_name.exists()) { // 不存在檔案時，建立
            /*
                        try {
                            del_file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        */
            return false;
        } else { // 檔案存在時刪除
            if (del_flag){
                file_name.delete();
            }
            return true;
        }
    }

    //安装apk  (7.0以前 > 使用Intent来跳转 )
    protected void installApkByIntent(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    //安装apk  (7.0以後 > 使用 FileProvider)
    private void installApkByGuide(String uriString) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if(Build.VERSION.SDK_INT >= 24){
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(this, this.getPackageName() + ".provider", new File(uriString));
        }else{
            uri=Uri.fromFile(new File(uriString));
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        this.startActivity(intent);
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

    // 利用 Volley 實現 POST 請求
    private void volley_check_release() {
        GlobalVariable globalVar = (GlobalVariable)getApplicationContext();
        mUrl="http://" + globalVar.g_strIP + ":" + globalVar.g_strPort + "/ycworksys/release_ycworksys.php";
        //mUrl="http://192.168.0.30:8881/release.php";
        mQueue = Volley.newRequestQueue(getApplicationContext());
        mGetRequest = new  StringRequest(Request.Method.POST, mUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //mTxvResult.setText(response);

                        try {
                            int n =new JSONArray(response).length(); //筆數
                            int i=1;//取第1筆
                            //TEST :  http://192.168.0.30:8881/release.php
                            //app名字
                            //appname: "八旺測試APP",
                            //服务器版本号
                            //serverVersion: "2",
                            //服务器标志
                            //serverFlag: "1",
                            //是否强制更新
                            //lastForce: "1",
                            //apk下载地址，这里我已经下载了官方的apk，放到了服务器里面
                            //updateurl: "http://192.168.1.103:8080/36Kr.apk",
                            //版本的更新的描述
                            //upgradeinfo: "版本更新:V1.1.1 (測試)"
                            mappName =new JSONArray(response).getJSONObject(i-1).getString("appName");
                            mserverVersion =new JSONArray(response).getJSONObject(i-1).getString("serverVersion");
                            mserverFlag = new JSONArray(response).getJSONObject(i-1).getString("serverFlag");
                            mlastForce = new JSONArray(response).getJSONObject(i-1).getString("lastForce");
                            mupdateUrl = new JSONArray(response).getJSONObject(i-1).getString("updateUrl");
                            mupgradeInfo = new JSONArray(response).getJSONObject(i-1).getString("upgradeInfo");

                            OutputData = "總筆數 :   "+ n +" | "
                                    + "第"+ i +"筆 : \n     " +" | "
                                    + mappName +" | "
                                    + mserverVersion +" | "
                                    + mserverFlag +" | "
                                    + mlastForce +" | "
                                    + mupdateUrl +" | "
                                    + mupgradeInfo +" \n ";

                            mTxvResult.setText("OK");

                            //mTxvResult.setText(OutputData);
                            //Toast.makeText(getApplicationContext(),OutputData,Toast.LENGTH_SHORT).show();



                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"Not available : " +e.getMessage(),Toast.LENGTH_SHORT).show();
                            //mTxvResult.setText(e.getMessage());
                            mTxvResult.setText("ND"); //沒有資料
                            e.printStackTrace();
                        }

                        //mTxvResult.setText(OutputData);
                    }
                    //showJson(response);
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTxvResult.setText(error.getMessage());
                //Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        }) {
            // 傳遞參數
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new Hashtable<String, String>();
                //params.put("stNo", mEdtStNo.getText().toString());
                return params;
            }
        };

        mQueue.add(mGetRequest);
    }


    class WSAsyncTask extends AsyncTask<String, String, String> {
        String result = "",methodName="",SOAP_ACTION1="",URL="",NAMESPACE="";

        //private static final String URL ="http://www.barwand.com.tw/WebService.asmx?wsdl"; //Web Services的網址
        //private static final String methodName  =params[0]; //"HelloName";//"HelloWorld"; //要呼叫的函數名稱
        //private static final String NAMESPACE = "http://tempuri.org/"; //Web Services命名空間
        //private static final String SOAP_ACTION1 =NAMESPACE+methodName;// "http://tempuri.org/HelloName"; //Web Services命名空間+函數名稱

        @Override
        protected String doInBackground(String... params) {
            //String Get_HelloWorld="";
            try{
                URL=fstrURL;
                NAMESPACE= fstrNameSpace ;
                methodName=params[0];
                SOAP_ACTION1 =NAMESPACE+methodName;
                SoapObject request1 = new SoapObject(NAMESPACE, methodName ); //// 建立一個 WebService 請求

                switch(params[0]){
                    case "HelloName":
                        request1.addProperty("strName", params[1]);// add 參數 and 值
                        break;

                    case "Check_TMPP001_GET_UKEIRE_NO":
                        request1.addProperty("psTNS", params[1]);// add 參數 and 值
                        break;
                }

                //request1.addProperty("strName", params[1]);// add 參數 and 值
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);// 擴充 SOAP 序列化功能為第11版
                envelope.bodyOut = request1;
                envelope.dotNet = true;// 設定為 .net 預設編碼
                envelope.setOutputSoapObject(request1);// 設定輸出的 SOAP 物件

                //Web method call
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);// 建立一個 HTTP 傳輸層
                androidHttpTransport.debug = true;// 測試模式使用
                androidHttpTransport.call(SOAP_ACTION1, envelope);// 設定 SoapAction 所需的標題欄位

                //XML回傳方式與String方式不同
                if (params[0].equals("Check_TMPP001_GET_UKEIRE_NO")) {
                    //获得服务返回的数据,并且开始解析
                    SoapObject result = (SoapObject) envelope.getResponse();

                    for (int i = 0; i < result.getPropertyCount(); i++) {
                        SoapObject Objecttable = (SoapObject) result.getProperty(i);
                        resultData[O_OUTPUT] = (Objecttable.getProperty("O_OUTPUT").toString());
                        //System.out.println("CAT_N= " +Objecttable.getProperty("CAT_N").toString());
                    }
                }
                else{
                    //get the response
                    SoapPrimitive result= (SoapPrimitive)envelope.getResponse();
                    resultData[O_OUTPUT] = result.toString();
                    //Get_HelloWorld=results;
                }

            }catch (Exception e){
                //Get_HelloWorld=e.getMessage(); //將錯誤訊息傳回
                resultData[O_OUTPUT]=e.getMessage(); //將錯誤訊息傳回
            }

            // 必须使用post方法更新UI组件
            mTxvResult.post(new Runnable() {
                @Override
                public void run() {
                    mTxvResult.setText(resultData[O_OUTPUT]);
                }
            });
            return null;
        }
    }

    // 利用 Volley 實現 POST 請求
    private void volley_post(String strFuncID) {
        String result = "",methodName="",SOAP_ACTION1="",NAMESPACE="";
        GlobalVariable globalVar = (GlobalVariable)getApplicationContext();
        OutputData = "";
        methodName=strFuncID;

        switch(strFuncID) {
            case "Check_TMPP001_GET_UKEIRE_NO":
                //WebService XML
                mUrl=fstrURL + "/" + methodName;
                break;

            case "hs_search":
                //PHP
                //mUrl="http://" + globalVar.g_strIP + ":" + globalVar.g_strPort + "/koestock/" + strFuncID +".php";
                mUrl="http://" + globalVar.g_strIP + ":" + globalVar.g_strPort + "/hsworksys/" + strFuncID +".php";
                break;

            default:
                mTxvResult.setText("NG");
                break;
        }


        mQueue = Volley.newRequestQueue(getApplicationContext());
        mGetRequest = new  StringRequest(Request.Method.POST, mUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj,jsonObj2;

                            if (response != null) {
                                //mTxvResult.setText(jsonObj.toString());

                                String strFuncName=mTxvFuncID.getText().toString();
                                switch(strFuncName) {
                                    case "Check_TMPP001_GET_UKEIRE_NO":
                                        // Json 第一層
                                        jsonObj = XML.toJSONObject(response);
                                        // Json 第二層
                                        jsonObj2 = new JSONObject(jsonObj.getString("TMPP001.GET_UKEIRE_NO"));
                                        resultData[O_OUTPUT] = jsonObj2.getString("O_OUTPUT");
                                        mTxvResult.setText(resultData[O_OUTPUT]);
                                        break;

                                    case "hs_search":
                                        // Json 第二層
                                        //int n =new JSONArray(response).length(); //筆數
                                        int i=1;//取第1筆
                                        //取第1筆
                                        jsonObj2 = new JSONArray(response).getJSONObject(i-1);
                                        resultData[O_OUTPUT] = jsonObj2.getString("s_unit");
                                        resultData[O_YYYYMM] = jsonObj2.getString("s_ppt_no");
                                        //resultData[O_OUTPUT] = new JSONArray(response).getJSONObject(i-1).getString("OUTPUT");
                                        //resultData[O_YYYYMM] = new JSONArray(response).getJSONObject(i-1).getString("YYYYMM");
                                        mTxvResult.setText(resultData[O_OUTPUT]);
                                        break;

                                    default:
                                        mTxvResult.setText("NG");
                                        break;
                                }

                            } else {
                                mTxvResult.setText("NG");
                            }

                        } catch (Exception e) {
                            //Toast.makeText(getApplicationContext(),"Not available",Toast.LENGTH_SHORT).show();
                            //mTxvFuncID.setText("");
                            mTxvResult.setText(e.getMessage());
                            //e.printStackTrace();
                        }
                        //mTxvResult.setText(OutputData);
                    }
                    //showJson(response);
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTxvFuncID.setText("");
                mTxvResult.setText("VolleyError");
            }
        }) {
            // 傳遞參數
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new Hashtable<String, String>();

                String strFuncID,strData;
                strFuncID=mTxvFuncID.getText().toString();
                switch(strFuncID) {
                    case "Check_TMPP001_GET_UKEIRE_NO":
                        params.put("psTNS", fstrOraTNS);
                        break;

                    //default:
                        //mTxvResult.setText(resultData[0]);
                    //    break;
                }
                return params;
            }
        };

        mGetRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mQueue.add(mGetRequest);
    }

        /*
    private void showInputDialog2() {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.activity_input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        fstrInputPass=editText.getText().toString();
                        //resultText.setText("Hello, " + editText.getText());

                        if (fstrInputPass == fstrPass)
                        {
                            Intent it = new Intent(MainActivity.this, set_para.class);
                            startActivity(it);
                        }

                        //finish();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();


        //try{ Looper.loop(); }
        //catch(RuntimeException e){}
    }
        */
    /*
    public synchronized String getInput()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                //customize alert dialog to allow desired input
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        m_Input = "TEST";//alert.getCustomInput();
                        notify();
                    }
                });
                alert.show();
            }
        });

        try
        {
            wait();
        }
        catch (InterruptedException e)
        {
        }

        return m_Input;
    }
    */
}
