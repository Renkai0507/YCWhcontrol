package tw.com.barwand.ks191113;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Map;

import static tw.com.barwand.ks191113.GlobalVariable.Errbeep;
import static tw.com.barwand.ks191113.GlobalVariable.OKbeep;

/**
 * Created by Michael on 2018/6/19.
 */


public class op5000_1 extends AppCompatActivity {
    RequestQueue mQueue;
    String mUrl = "";
    TextView mTxvResult,mTxvFuncID;
    TextView mTxvWhName,mTxvPdctName1,mTxvPdctName2,mTxvUnit,mTxvSafeQty,mTxvNowQty,mTxvTitle;
    EditText mEdtInNo,mEdtWhNo,mEdtPdctNo,mEdtQty,mEdtUpdQty,mEdtnote;
    Button mBtnSearch01,mBtnSearch02,mBtnSave,mBtnDel;
    RadioGroup mRdgWhType;
    RadioButton mRdoA,mRdoB;
    StringRequest mGetRequest;
    String OutputData = "";
    String  fstrIP,fstrPort,fstrPass,fstrURL,fstrNameSpace,fstrBT,fstrOraTNS,fstrDep_ID,fstrMac_ID,fstrEmpNo;
    String fstrBucketType,fstrUseType,fstrWhType,fstrAprt,fstrInNo,fstrWh,fstrPdct,fstrQty,fstrWorkType,fstrSeq,fstrnote;

    String fstrInvType;
    String[] libNo,libName;
    String[] resultData = new String[15];
    //ListView listResult;
    private int mYear, mMonth, mDay;
    //先行定義時間格式
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    //SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");

    private static final int REQUEST_Cust = 1;
    private static final int REQUEST_Pdct = 2;

    private static final int O_OUTPUT = 0;
    //private SharedPreferences settings;
    //private static final String data = "DATA";
    //private static final String ipField = "IP";
    //private static final String portField = "PORT";
    //private static final String passField = "PASS";
    //private static final String urlField = "URL";
    //private static final String nsField = "NAMESPACE";
    //private static final String btField = "BT";
    //private static final String tnsField = "TNS";

    //private Ringtone mRingtone;
    //private Vibrator mVibrator;//震動

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_op5000_1);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 讓手機螢幕保持直立模式
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//不自動彈出虛擬鍵盤

        //設定提示聲與震動=========================================================================
        //Uri notifyUri = RingtoneManager.getDefaultUri(RingtoneManager.ID_COLUMN_INDEX);
        //mRingtone = RingtoneManager.getRingtone(this, notifyUri); //設定提示聲
        //mVibrator = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE); //設定震動
        //=========================================================================================

        //取的intent中的bundle物件
        Bundle bundle =this.getIntent().getExtras();
        fstrAprt=bundle.getString("SearchAprt");
        fstrInNo=bundle.getString("SearchInNo");
        fstrWh=bundle.getString("SearchWh");
        fstrPdct=bundle.getString("SearchPdct");
        fstrQty=bundle.getString("SearchQty");
        fstrSeq=bundle.getString("SearchSeq");
        fstrnote=bundle.getString("SearchNote");

        initComponent();
        readData(); //讀取各項設定值
        //mTxvTitle.setText(mTxvTitle.getText().toString()+" - "+fstrEmpNo);
        //mtxvYM02.setText(bundle.getString("YYYYMM"));

        //showSoftKeyboard();
        //mTxvResult.setText("http://" + fstrIP + ":" + fstrPort);
        mTxvFuncID.setText("");
        mTxvResult.setText("");
        //mTxvResult.setText(fstrURL + "\n" + fstrNameSpace);

        //取得現在時間
        //Date dt=new Date();
        //透過SimpleDateFormat的format方法將Date轉為字串
        //String dts=sdf.format(dt);  //日期
        //mtxvItemDate=(TextView) findViewById(R.id.txvItemDate);
        //mtxvItemDate.setText(strYear+"-"+strMon+"-"+strDay);
        //mtxvItemDate.setText(dts);
        volley_post_pdct(fstrWh,fstrPdct);

        /*mBtnSearch02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEdtInAprt.getText().toString().equals("") || mEdtInNo.getText().toString().equals("") )
                {
                    Errbeep(getApplicationContext(),"入庫單位與單號不可空，請確認!!",1000,true,1000);
                }else
                {
                    Intent it = new Intent(op5000_1.this,php_search01.class);

                    //new一個Bundle物件，並將要傳遞的資料傳入
                    Bundle bundle = new Bundle();
                    //bundle.putDouble("SearchType","");
                    bundle.putString("SearchType", "Pdct");
                    bundle.putString("SearchAprt", mEdtInAprt.getText().toString());
                    bundle.putString("SearchInNo", mEdtInNo.getText().toString());

                    //將Bundle物件assign給intent
                    it.putExtras(bundle);
                    //startActivity(it);
                    startActivityForResult(it, REQUEST_Pdct);
                }

            }
        });*/

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (mEdtUpdQty.getText().toString().equals(""))
            {
                Errbeep(getApplicationContext(),"修改數量不可空，請確認!!",1000,true,1000);
            }else
            {
                fstrWorkType="Update";
                volley_post_update(mEdtUpdQty.getText().toString());
            }
            }
        });

        mBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fstrWorkType="Delete";
                volley_post_update("");
            }
        });

    }
    // 連接 layout 上的物件
    public void initComponent() {
        mTxvResult = (TextView) findViewById(R.id.txvResult);
        mTxvFuncID = (TextView) findViewById(R.id.txvFuncID);
        mTxvWhName = (TextView) findViewById(R.id.txvWhName);
        mTxvPdctName1 = (TextView) findViewById(R.id.txvPdctName1);
        mTxvPdctName2 = (TextView) findViewById(R.id.txvPdctName2);
        mTxvUnit = (TextView) findViewById(R.id.txvUnit);
        mTxvSafeQty = (TextView) findViewById(R.id.txvSafeQty);
        mTxvNowQty = (TextView) findViewById(R.id.txvNowQty);
        mTxvTitle= (TextView) findViewById(R.id.txvTitle);


        mEdtInNo= (EditText) findViewById(R.id.edtInNo);
        mEdtWhNo= (EditText) findViewById(R.id.edtWhNo);
        mEdtPdctNo= (EditText) findViewById(R.id.edtPdctNo);
        mEdtQty= (EditText) findViewById(R.id.edtQty);
        mEdtUpdQty= (EditText) findViewById(R.id.edtUpdQty);
        mEdtnote=(EditText) findViewById(R.id.edtNote);

        mBtnSearch01= (Button) findViewById(R.id.btnSearch01);
        mBtnSearch02= (Button) findViewById(R.id.btnSearch02);
        mBtnSave= (Button) findViewById(R.id.btnSave);
        mBtnDel= (Button) findViewById(R.id.btnDel);

        mRdgWhType = (RadioGroup) findViewById(R.id.rdgWhType);
        mRdoA=(RadioButton)findViewById(R.id.rdoA);
        mRdoB=(RadioButton)findViewById(R.id.rdoB);

    }
    public void ClearAll() {

        mEdtInNo.setText("");
        mEdtWhNo.setText("");
        mEdtPdctNo.setText("");
        mEdtQty.setText("");
        mTxvWhName.setText("");
        mTxvPdctName1.setText("");
        mTxvPdctName2.setText("");
        mTxvUnit.setText("");
        mTxvSafeQty.setText("");
        mTxvNowQty.setText("");
        fstrUseType="";
        fstrBucketType="";


    }

    public void sw_ctrl(String strFuncID) {
        switch(strFuncID) {
            case "Check_TMPB011_N":
                //mEdtItem02.setVisibility(View.INVISIBLE);
                //mtxvItem21.setText("");
                //mEdtItem01.selectAll();
                //mEdtItem01.requestFocus();
                break;

            case "Check_TMPB011_Y":
                //mEdtItem02.setVisibility(View.VISIBLE);
                //mEdtItem02.setText("");
                //mtxvItem21.setText("");
                //mEdtItem02.requestFocus();
                break;

            case "Check_TMPB019":
                //mtxvItem21.setText("");
                //mEdtItem02.selectAll();
                //mEdtItem02.requestFocus();
                break;

            default:
                break;
        }
    }

    //隱藏 Soft Keyboard
    public void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        //imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    //顯示 Soft Keyboard
    /*public void showSoftKeyboard() {
        InputMethodManager imm = ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE));
        //imm.toggleSoftInput();
        int SoftInputAnchor = R.id.edtItem01;
        imm.toggleSoftInput(SoftInputAnchor,0);

    }*/

    public void readData(){
        /*
        //settings = getSharedPreferences(data,0);
        settings =getSharedPreferences("config.xml", MODE_PRIVATE);
        fstrIP=settings.getString(ipField, "");
        fstrPort=settings.getString(portField, "");
        fstrPass=settings.getString(passField, "");
        fstrURL="http://" + settings.getString(urlField, "");
        fstrNameSpace="http://" + settings.getString(nsField, "") + "/";
        fstrOraTNS=settings.getString(tnsField, "");
        */

        //使用Global variable(全域變數)
        GlobalVariable globalVar = (GlobalVariable)getApplicationContext();
        fstrIP=globalVar.g_strIP;
        fstrPort=globalVar.g_strPort;
        fstrPass=globalVar.g_strPass;
        fstrURL=globalVar.g_strURL;
        fstrNameSpace=globalVar.g_strNameSpace;
        fstrBT=globalVar.g_strBT;
        fstrOraTNS=globalVar.g_strOraTNS;
        fstrDep_ID=globalVar.g_strDep_ID;
        fstrMac_ID=globalVar.g_strMac_ID;
    }

    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_Cust:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    mEdtWhNo.setText(mExtra.getString("ID"));
                    mTxvWhName.setText(mExtra.getString("Name"));
                    mEdtPdctNo.requestFocus();
                    //medtCustNo.setText(cust_no);
                    //mtxvCustName.setText(cust_name);

                    //mEdtBarcode.requestFocus();
                }
                break;
        }
    }
    public void SetColorbyZero(TextView obj)
    {
        double num = Double.valueOf(obj.getText().toString());
        if(num<=0)
        {
            obj.setTextColor(Color.RED);
        }else
        {
            obj.setTextColor(Color.GRAY);
        }
    }


    // 利用 Volley 實現 POST 請求
    private void volley_post_pdct(final String str_wh,final String str_pdct) {
        String result = "",methodName="",SOAP_ACTION1="",NAMESPACE="";
        String strbits="";
        //final String W=medtInput.getText().toString().split(";")[3];
        //final String LH=medtInput.getText().toString().split(";")[0];
        //final String PH=medtInput.getText().toString().split(";")[1];
        OutputData = "";
        mUrl="http://" + fstrIP + ":" +fstrPort + "/ycworksys/yc_search_pdct.php";

        mQueue = Volley.newRequestQueue(getApplicationContext());
        final String finalStrbits = strbits;
        mGetRequest = new  StringRequest(Request.Method.POST, mUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //showJson(response);

                        try {
                            int n =new JSONArray(response).length(); //筆數
                            int i=1;//取第1筆
                            String stMsg =new JSONArray(response).getJSONObject(i-1).getString("OUTPUT");

                             if (stMsg.equals("OK"))
                            {
                                String stPdctName1 =new JSONArray(response).getJSONObject(i-1).getString("Item01");
                                String stPdctName2 =new JSONArray(response).getJSONObject(i-1).getString("Item02");
                                String stUnit =new JSONArray(response).getJSONObject(i-1).getString("Item03");
                                String stSafeQty =new JSONArray(response).getJSONObject(i-1).getString("Item04");
                                String stNowQty =new JSONArray(response).getJSONObject(i-1).getString("Item05");

                                mEdtInNo.setText(fstrInNo);
                                mEdtWhNo.setText(fstrWh);
                                mEdtPdctNo.setText(fstrPdct);
                                mEdtQty.setText(fstrQty);
                                mTxvPdctName1.setText(stPdctName1);
                                mTxvPdctName2.setText(stPdctName2);
                                mTxvUnit.setText(stUnit);
                                mTxvSafeQty.setText(stSafeQty);
                                mTxvNowQty.setText(stNowQty);
                                SetColorbyZero(mTxvNowQty);

                                mEdtnote.setText(fstrnote);
                            }
                            else
                            {
                                Errbeep(getApplicationContext(),"資料查詢錯誤!",1000,true,1000);
                                mTxvResult.setText(stMsg);
                                mTxvResult.setTextColor(android.graphics.Color.RED);
                                //mtxvDate.setText("");
                                //mtxvUseTime.setText("");
                                //mtxvLastCust.setText("");
                            }
                            //mEdtBarcode.requestFocus();
                            //mEdtBarcode.setText("");

                            //mtxvW.setText("");

                            //mTxvResult.setText(OutputData);

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"Not available",Toast.LENGTH_SHORT).show();
                            mTxvResult.setText(e.getMessage());
                            mTxvResult.setTextColor(android.graphics.Color.RED);
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTxvFuncID.setText("");
                mTxvResult.setText("VolleyError");
                mTxvResult.setTextColor(android.graphics.Color.RED);
            }
        }) {
            // 傳遞參數
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new Hashtable<String, String>();
                params.put("stWh", str_wh);
                params.put("stPdct", str_pdct);
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

    // 利用 Volley 實現 POST 請求
    private void volley_post_update(final String str_UpdQty) {
        String result = "",methodName="",SOAP_ACTION1="",NAMESPACE="";
        String strbits="";
        //final String W=medtInput.getText().toString().split(";")[3];
        //final String LH=medtInput.getText().toString().split(";")[0];
        //final String PH=medtInput.getText().toString().split(";")[1];
        OutputData = "";
        mUrl="http://" + fstrIP + ":" +fstrPort + "/ycworksys/yc_update_stock.php";

        mQueue = Volley.newRequestQueue(getApplicationContext());
        final String finalStrbits = strbits;
        mGetRequest = new  StringRequest(Request.Method.POST, mUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //showJson(response);

                        try {
                            int n =new JSONArray(response).length(); //筆數
                            int i=1;//取第1筆
                            String stMsg =new JSONArray(response).getJSONObject(i-1).getString("OUTPUT");

                            if (stMsg.equals("OK"))
                            {
                                mTxvNowQty.setText(new JSONArray(response).getJSONObject(i-1).getString("Item01"));
                                SetColorbyZero(mTxvNowQty);
                                if (fstrWorkType.equals("Update"))
                                {
                                    mEdtQty.setText(mEdtUpdQty.getText().toString());
                                    mEdtUpdQty.setText("");
                                }else {
                                    mEdtQty.setText("");
                                    mEdtUpdQty.setText("");
                                }
                                OKbeep(getApplicationContext(),"更新成功!!",1000,true,1000);

                            }
                            else
                            {
                                Errbeep(getApplicationContext(),"資料查詢錯誤!",1000,true,1000);
                                mTxvResult.setText(stMsg);
                                mTxvResult.setTextColor(android.graphics.Color.RED);

                            }


                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"Not available",Toast.LENGTH_SHORT).show();
                            mTxvResult.setText(e.getMessage());
                            mTxvResult.setTextColor(android.graphics.Color.RED);
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTxvFuncID.setText("");
                mTxvResult.setText("VolleyError");
                mTxvResult.setTextColor(android.graphics.Color.RED);
            }
        }) {
            // 傳遞參數
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new Hashtable<String, String>();
                params.put("stUpdTable", "in_mast");
                params.put("stWorkType", fstrWorkType);
                params.put("stAprt", fstrAprt);
                params.put("stInNo", fstrInNo);
                params.put("stWh", fstrWh);
                params.put("stPdct", fstrPdct);
                params.put("stQty", mEdtQty.getText().toString());
                params.put("stUpdQty", mEdtUpdQty.getText().toString());
                params.put("stSeq", fstrSeq);
                params.put("stNote",mEdtnote.getText().toString());
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


}

