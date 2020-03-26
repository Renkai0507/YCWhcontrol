package tw.com.barwand.ks191113;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import static tw.com.barwand.ks191113.GlobalVariable.Errbeep;

/**
 * Created by Michael on 2018/6/19.
 */


public class op5003 extends AppCompatActivity {
    RequestQueue mQueue;
    String mUrl = "";
    TextView mTxvResult,mTxvFuncID;
    TextView mTxvWhName,mTxvPdctName1,mTxvPdctName2,mTxvUnit,mTxvSafeQty,mTxvNowQty,mTxvTitle,mTxvTotal;
    EditText mEdtInAprt,mEdtInNo,mEdtWhNo,mEdtPdctNo,mEdtQty;
    Button mBtnSearch01,mBtnSearch02,mBtnSave;
    RadioGroup mRdgWhType;
    RadioButton mRdoA,mRdoB;
    StringRequest mGetRequest;
    String OutputData = "";
    String  fstrIP,fstrPort,fstrPass,fstrURL,fstrNameSpace,fstrBT,fstrOraTNS,fstrDep_ID,fstrMac_ID,fstrEmpNo;
    String fstrBucketType,fstrUseType,fstrWhType,Searchtype;
    String fstrInvType;
    String[] libNo,libName;
    String[] resultData = new String[15];
    ListView listResult;
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
        setContentView(R.layout.activity_op5003);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 讓手機螢幕保持直立模式
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//不自動彈出虛擬鍵盤

        //設定提示聲與震動=========================================================================
        //Uri notifyUri = RingtoneManager.getDefaultUri(RingtoneManager.ID_COLUMN_INDEX);
        //mRingtone = RingtoneManager.getRingtone(this, notifyUri); //設定提示聲
        //mVibrator = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE); //設定震動
        //=========================================================================================

        //取的intent中的bundle物件
        Bundle bundle =this.getIntent().getExtras();
        fstrEmpNo=bundle.getString("Emp");
        fstrWhType="A";

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
        Date dt=new Date();
        //透過SimpleDateFormat的format方法將Date轉為字串
        String dts=sdf.format(dt);  //日期
        //mtxvItemDate=(TextView) findViewById(R.id.txvItemDate);
        //mtxvItemDate.setText(strYear+"-"+strMon+"-"+strDay);
        //mtxvItemDate.setText(dts);

        mEdtInAprt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    if (mEdtInAprt.getText().toString().equals(""))
                    {
                        Errbeep(getApplicationContext(),"入庫單位不可為空，請確認!!",1000,true,1000);
                        mEdtInAprt.requestFocus();
                        //return false;
                    }else
                    {
                        mEdtInNo.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });

        mEdtInNo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    if (mEdtInNo.getText().toString().equals(""))
                    {
                        Errbeep(getApplicationContext(),"單號不可為空，請確認!!",1000,true,1000);
                        mEdtInNo.requestFocus();
                        //return false;
                    }
                    return true;
                }
                return false;
            }
        });

        mEdtPdctNo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    //if (mEdtWhNo.getText().toString().equals(""))
                    //{
                    //    Errbeep(getApplicationContext(),"儲位不可為空，請確認!!",1000,true,1000);
                    //    mEdtWhNo.requestFocus();
                        //return false;
                    //}else
                    //{
                    Searchtype = "pdct";
                    Intent it = new Intent(op5003.this,php_search01.class);

                    //new一個Bundle物件，並將要傳遞的資料傳入
                    Bundle bundle = new Bundle();
                    //bundle.putDouble("SearchType","");
                    bundle.putString("SearchType", "pdct");
                    bundle.putString("Searchpdct", mEdtPdctNo.getText().toString());
                    //將Bundle物件assign給intent
                    it.putExtras(bundle);
                    //startActivity(it);
                    startActivityForResult(it, REQUEST_Cust);
//                        volley_post_pdct("",mEdtPdctNo.getText().toString());
//                        volley_post_search_stock(mEdtPdctNo.getText().toString());
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


                    //}

                    return true;
                }
                return false;
            }
        });
//        mEdtWhNo.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP)
//                {
//                    mEdtWhNo.requestFocus();
//                    //return false;
////                    }else
////                    {
//                    //                        volley_post_wh(mEdtWhNo.getText().toString());
//                    Searchtype="wh";
//                    Intent it = new Intent(op5003.this,php_search01.class);
//
//                    //new一個Bundle物件，並將要傳遞的資料傳入
//                    Bundle bundle = new Bundle();
//                    //bundle.putDouble("SearchType","");
//                    bundle.putString("SearchType", mEdtWhNo.getText().toString());
//
//                    //將Bundle物件assign給intent
//                    it.putExtras(bundle);
//                    //startActivity(it);
//                    startActivityForResult(it, REQUEST_Cust);
//
//
//                    mEdtPdctNo.requestFocus();
//                }
//                return false;
//            }
//        });

//        mBtnSearch01.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                mEdtWhNo.requestFocus();
//                //return false;
////                    }else
////                    {
//                //                        volley_post_wh(mEdtWhNo.getText().toString());
//                Searchtype="wh";
//                Intent it = new Intent(op5003.this,php_search01.class);
//
//                //new一個Bundle物件，並將要傳遞的資料傳入
//                Bundle bundle = new Bundle();
//                //bundle.putDouble("SearchType","");
//                bundle.putString("SearchType", mEdtWhNo.getText().toString());
//
//                //將Bundle物件assign給intent
//                it.putExtras(bundle);
//                //startActivity(it);
//                startActivityForResult(it, REQUEST_Cust);
//
//
//                mEdtPdctNo.requestFocus();
//            }
//        });

        mBtnSearch02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEdtInAprt.getText().toString().equals("") || mEdtInNo.getText().toString().equals("") )
                {
                    Errbeep(getApplicationContext(),"入庫單位與單號不可空，請確認!!",1000,true,1000);
                }else
                {
                    Intent it = new Intent(op5003.this,php_search01.class);

                    //new一個Bundle物件，並將要傳遞的資料傳入
                    Bundle bundle = new Bundle();
                    //bundle.putDouble("SearchType","");
                    bundle.putString("SearchType", "Pdct_In");
                    bundle.putString("SearchAprt", mEdtInAprt.getText().toString());
                    bundle.putString("SearchInNo", mEdtInNo.getText().toString());
                    bundle.putString("SearchInQty", mEdtQty.getText().toString());

                    //將Bundle物件assign給intent
                    it.putExtras(bundle);
                    //startActivity(it);
                    startActivityForResult(it, REQUEST_Pdct);
                }

            }
        });

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /*if (mEdtInAprt.getText().toString().equals("") || mEdtInNo.getText().toString().equals("") || mEdtWhNo.getText().toString().equals("") ||
                    mEdtPdctNo.getText().toString().equals("") || mEdtQty.getText().toString().equals("") )
            {
                Errbeep(getApplicationContext(),"入庫單位、單號、儲位、料號、數量不可為空，請確認!!",1000,true,1000);
            }else
            {
                volley_post_insert(mEdtInAprt.getText().toString(),mEdtInNo.getText().toString(),mEdtWhNo.getText().toString(),mEdtPdctNo.getText().toString(),mEdtQty.getText().toString());
            }*/
            }
        });

        mRdgWhType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rdoA:  // 無序號
                        fstrWhType="A";
                        break;
                    case R.id.rdoB:  // 序號
                        fstrWhType="B";
                        break;
                }
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
        mTxvTotal= (TextView) findViewById(R.id.txv_total);

        mEdtInAprt= (EditText) findViewById(R.id.edtInAprt);
        mEdtInNo= (EditText) findViewById(R.id.edtInNo);
        mEdtWhNo= (EditText) findViewById(R.id.edWhNo);
        mEdtPdctNo= (EditText) findViewById(R.id.edtPdctNo);
        mEdtQty= (EditText) findViewById(R.id.edtQty);

//        mBtnSearch01= (Button) findViewById(R.id.btnSearch01);
        mBtnSearch02= (Button) findViewById(R.id.btnSearch02);
        mBtnSave= (Button) findViewById(R.id.btnSave);

        mRdgWhType = (RadioGroup) findViewById(R.id.rdgWhType);
        mRdoA=(RadioButton)findViewById(R.id.rdoA);
        mRdoB=(RadioButton)findViewById(R.id.rdoB);
        listResult = (ListView) findViewById(R.id.list_result);

    }
    public void ClearAll() {
        mEdtInAprt.setText("");
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
        mEdtInAprt.requestFocus();

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
                    if (Searchtype.equals("pdct"))
                    {
                        Bundle mExtra = mDataIntent.getExtras();
                        mEdtPdctNo.setText(mExtra.getString("ID"));
                        volley_post_pdct(mEdtWhNo.getText().toString(),mEdtPdctNo.getText().toString());
                        volley_post_search_stock(mEdtPdctNo.getText().toString());

                        mEdtPdctNo.requestFocus();
                        mEdtPdctNo.selectAll();
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    }else {
                        Bundle mExtra = mDataIntent.getExtras();
                        mEdtWhNo.setText(mExtra.getString("ID"));
                        mTxvWhName.setText(mExtra.getString("Name"));
                        mEdtPdctNo.requestFocus();
                    }
                    //medtCustNo.setText(cust_no);
                    //mtxvCustName.setText(cust_name);

                    //mEdtBarcode.requestFocus();
                }
                break;
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
                                //String stNowQty =new JSONArray(response).getJSONObject(i-1).getString("Item05");
                                String stTotal =new JSONArray(response).getJSONObject(i-1).getString("Item06");
                                if(stTotal.equals("null"))stTotal="0";
                                mTxvPdctName1.setText(stPdctName1);
                                mTxvPdctName2.setText(stPdctName2);
                                mTxvUnit.setText(stUnit);
                                mTxvSafeQty.setText(stSafeQty);
                                mTxvTotal.setText(stTotal);
                                //mTxvNowQty.setText(stNowQty);

                            }
                             else if (stMsg.equals("No-Data")){
                                 Errbeep(getApplicationContext(),"無此料號，請確認!!",1000,true,1000);
                                 mEdtPdctNo.requestFocus();
                                 mEdtPdctNo.selectAll();
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
    private void volley_post_search_stock(final String str_bar_data) {
        String result = "",methodName="",SOAP_ACTION1="",NAMESPACE="";
        String strbits="";

        //final String W=medtInput.getText().toString().split(";")[3];
        //final String LH=medtInput.getText().toString().split(";")[0];
        //final String PH=medtInput.getText().toString().split(";")[1];
        OutputData = "";
        mUrl="http://" + fstrIP + ":" +fstrPort + "/ycworksys/yc_search_pdct_stock.php";

        mQueue = Volley.newRequestQueue(getApplicationContext());
        final String finalStrbits = strbits;
        mGetRequest = new  StringRequest(Request.Method.POST, mUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJson(response);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTxvFuncID.setText("");
                mTxvResult.setText("VolleyError");
                mTxvResult.setTextColor(Color.RED);
            }
        }) {
            // 傳遞參數
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new Hashtable<String, String>();
                params.put("stPdct", str_bar_data);
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
    public void showJson(String response)
    {
        String OutputData = "";
        try {
            //JSONObject jsonObject = new JSONObject(response);
            //JSONArray jsonArray = jsonObject.getJSONArray("");

            JSONArray jsonObject = new JSONArray(response);
            //測試 ListView ===============================
            String[] libstNo = parseJSONData(jsonObject);
            ArrayAdapter listAdapter = new ArrayAdapter(
                    op5003.this,
                    R.layout.list_item1,
                    libstNo);
            listResult.setAdapter(listAdapter);
            //==========================================

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            mTxvResult.setText(e.getMessage());
            e.printStackTrace();
        }
    }

    private String[] parseJSONData(JSONArray dataInput) {
        try {
            String[] lib_ID = new String[dataInput.length()];
            String[] lib_NAME = new String[dataInput.length()];
            String[] lib_Qty = new String[dataInput.length()];
            String[] libAllData = new String[dataInput.length()];
            for (int i=0; i<dataInput.length(); i++) {
                JSONObject lib = dataInput.getJSONObject(i);
                lib_ID[i] = lib.getString("Item01");
                lib_NAME[i] = lib.getString("Item02");
                libAllData[i] = String.format(lib.getString("Item01")+"  |  "+lib.getString("Item02"));
            }
            //libID=lib_ID;  //傳給全域變數暫存
            //libName =lib_NAME;//傳給全域變數暫存
            //libQty =lib_Qty;//傳給全域變數暫存
            return libAllData;
        } catch (JSONException e) {
            //TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }


}

