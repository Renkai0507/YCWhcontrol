package tw.com.barwand.ks191113;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.Hashtable;
import java.util.Map;

//import static tw.com.barwand.ks191113.R.id.edtItem01;

public class php_search01 extends AppCompatActivity {
    RequestQueue mQueue;
    String mUrl = "";
    TextView mTxvResult,mtxvStNo;
    EditText mEdtStNo;
    Button mbtnSearch;
    StringRequest mGetRequest;
    String OutputData = "",fstrSearchType;
    String  fstrIP,fstrPort,fstrPass,fstrURL,fstrNameSpace,fstrBT,fstrOraTNS,fstrDep_ID,fstrMac_ID,fstrPdctType,fstrWhType,fstrPdctInWh;
    String  fstrAprt,fstrInNo,fstrInQty,fstrPdctNo;
    String[] libID,libName,libQty,libSeq,libBucketType,libUseType;
    ListView listResult;

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
    private static final int REQUEST_Pdct = 1;
    //private Ringtone mRingtone;
    //private Vibrator mVibrator;//震動

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//避免 Android 軟鍵盤蓋住輸入框的問題

        setContentView(R.layout.activity_php_search01);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 讓手機螢幕保持直立模式

        //設定提示聲與震動=========================================================================
        //Uri notifyUri = RingtoneManager.getDefaultUri(RingtoneManager.ID_COLUMN_INDEX);
        //mRingtone = RingtoneManager.getRingtone(this, notifyUri); //設定提示聲
        //mVibrator = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE); //設定震動
        //=========================================================================================

        //取的intent中的bundle物件
        Bundle bundle =this.getIntent().getExtras();
        fstrSearchType=bundle.getString("SearchType");
        if (fstrSearchType.equals("Pdct_In") || fstrSearchType.equals("Pdct_Out"))
        {
            fstrAprt=bundle.getString("SearchAprt");
            fstrInNo=bundle.getString("SearchInNo");
        }else if (fstrSearchType.equals("pdct"))
        {
            fstrPdctNo=bundle.getString("Searchpdct");
//            fstrPdctInWh=bundle.getString("PdctInWh");
        }else if(fstrSearchType.equals("pdctinwh"))
        {
            fstrPdctNo=bundle.getString("Searchpdct");
            fstrPdctInWh=bundle.getString("PdctInWh");
        }
        fstrWhType = bundle.getString("Whtype");
        //fstrPdctType=bundle.getString("PdctType");
        //fstrCustType=bundle.getString("Cust");
        initComponent();
        readData(); //讀取各項設定值

        //mTxvResult.setText("http://" + fstrIP + ":" + fstrPort);
        mEdtStNo.setText(fstrPdctType);
        listResult = (ListView) findViewById(R.id.list_result);
        listResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (fstrSearchType.equals("Pdct_In") )
            {
                Bundle mBundle = new Bundle();
                // 利用索引值取得點擊的項目內容。
                String strID = libID[position].trim();//儲位
                String strName = libName[position].trim();//料號
                String strQty = libQty[position].trim();//入庫數量
                String strSeq = libSeq[position].trim();//序號
                //mBundle.putString("ID", strID);
                //mBundle.putString("Name", strName);
                //mBundle.putString("Qty", strQty);

                Intent it = new Intent(php_search01.this,op5000_1.class);

                //new一個Bundle物件，並將要傳遞的資料傳入
                Bundle bundle = new Bundle();
                //bundle.putDouble("SearchType","");
                bundle.putString("SearchAprt",fstrAprt);
                bundle.putString("SearchInNo", fstrInNo);
                bundle.putString("SearchWh", strID);
                bundle.putString("SearchPdct", strName);
                bundle.putString("SearchQty", strQty);
                bundle.putString("SearchSeq", strSeq);

                //將Bundle物件assign給intent
                it.putExtras(bundle);
                //startActivity(it);
                startActivityForResult(it, REQUEST_Pdct);
                finish();
            }else if (fstrSearchType.equals("Pdct_Out")){
                Bundle mBundle = new Bundle();
                // 利用索引值取得點擊的項目內容。
                String strID = libID[position].trim();//儲位
                String strName = libName[position].trim();//料號
                String strQty = libQty[position].trim();//入庫數量
                String strSeq = libSeq[position].trim();//序號
                //mBundle.putString("ID", strID);
                //mBundle.putString("Name", strName);
                //mBundle.putString("Qty", strQty);

                Intent it = new Intent(php_search01.this,op5001_1.class);

                //new一個Bundle物件，並將要傳遞的資料傳入
                Bundle bundle = new Bundle();
                //bundle.putDouble("SearchType","");
                bundle.putString("SearchAprt",fstrAprt);
                bundle.putString("SearchInNo", fstrInNo);
                bundle.putString("SearchWh", strID);
                bundle.putString("SearchPdct", strName);
                bundle.putString("SearchQty", strQty);
                bundle.putString("SearchSeq", strSeq);

                //將Bundle物件assign給intent
                it.putExtras(bundle);
                //startActivity(it);
                startActivityForResult(it, REQUEST_Pdct);
                finish();
            }else if (fstrSearchType.equals("trans")){}
            else if(fstrSearchType.equals("pdct"))
            {
                String strID = libID[position].trim();
                Bundle mBundle = new Bundle();
                mBundle.putString("ID", strID);
                Intent mBackIntent = new Intent();
                mBackIntent.putExtras(mBundle);
                setResult(Activity.RESULT_OK, mBackIntent);
                finish();
            }

            else
            {
                Bundle mBundle = new Bundle();

                // 利用索引值取得點擊的項目內容。
                String strID = libID[position].trim();
                String strName = libName[position].trim();

                mBundle.putString("ID", strID);
                mBundle.putString("Name", strName);
                mBundle.putString("Whtype", fstrWhType);
                Intent mBackIntent = new Intent();
                mBackIntent.putExtras(mBundle);
                setResult(Activity.RESULT_OK, mBackIntent);
                finish();
            }

            }
        });

        mbtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();//隱藏 keyboard
                volley_post();
            }
        });

        volley_post();

    }
    // 連接 layout 上的物件
    public void initComponent() {
        mTxvResult = (TextView) findViewById(R.id.txvResult);
        mtxvStNo = (TextView) findViewById(R.id.txvStNo);
        mEdtStNo = (EditText) findViewById(R.id.edtStNo);
        mbtnSearch = (Button) findViewById(R.id.btnSearch);

        /*switch(fstrSearchType) {
            case "PO":
                //倉別(位置)主檔
                mtxvStNo.setText("品種 : ");
                break;
            default:
                mtxvStNo.setText("");
                break;
        }*/
    }

    public void readData(){
        //settings = getSharedPreferences(data,0);
        //settings =getSharedPreferences("config.xml", MODE_PRIVATE);
        //fstrIP=settings.getString(ipField, "");
        //fstrPort=settings.getString(portField, "");
        //fstrPass=settings.getString(passField, "");

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
        int SoftInputAnchor =R.id.edtItem01;
        imm.toggleSoftInput(SoftInputAnchor,0);
    }*/

    // 利用 Volley 實現 POST 請求
    private void volley_post() {
        GlobalVariable globalVar = (GlobalVariable)getApplicationContext();
        OutputData = "";
        if (fstrSearchType.equals("Pdct_In") || fstrSearchType.equals("Pdct_Out")||fstrSearchType.equals("trans"))
        {
            mUrl="http://" + globalVar.g_strIP + ":" + globalVar.g_strPort + "/ycworksys/yc_search_scandata.php";
        }
        else if(fstrSearchType.equals("pdct"))
        {
            mUrl="http://" + globalVar.g_strIP + ":" + globalVar.g_strPort + "/ycworksys/yc_search_pdctlist.php";
        }else if (fstrSearchType.equals("pdctinwh"))
        {
            mUrl="http://" + globalVar.g_strIP + ":" + globalVar.g_strPort + "/ycworksys/yc_search_pdctinwh.php";
        }
        else
        {
            mUrl="http://" + globalVar.g_strIP + ":" + globalVar.g_strPort + "/ycworksys/yc_search_wh.php";
        }

        mQueue = Volley.newRequestQueue(getApplicationContext());
        mGetRequest = new  StringRequest(Request.Method.POST, mUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJson(response);

                        //mTxvResult.setText(response);
                        /*
                                                            try {
                                                                int n =new JSONArray(response).length(); //筆數
                                                                int i=1;//取第1筆
                                                                String stNo =new JSONArray(response).getJSONObject(i-1).getString("stNo");
                                                                String stName =new JSONArray(response).getJSONObject(i-1).getString("stName");
                                                                String height = new JSONArray(response).getJSONObject(i-1).getString("height");

                                                                OutputData += "總筆數 :   "+ n +" | "
                                                                            + "第"+ i +"筆 : \n\n     " +" | "
                                                                        + stNo +" | "
                                                                        + stName +" | "
                                                                        + height +" \n\n ";

                                                                mTxvResult.setText(OutputData);

                                                            } catch (JSONException e) {
                                                                Toast.makeText(getApplicationContext(),"Not available",Toast.LENGTH_SHORT).show();
                                                                mTxvResult.setText(e.getMessage());
                                                                e.printStackTrace();
                                                            }
                                                */
                        //mTxvResult.setText(OutputData);
                    }
                    //showJson(response);
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTxvResult.setText(error.getMessage());
            }
        }) {
            // 傳遞參數
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new Hashtable<String, String>();
                if (fstrSearchType.equals("Pdct_In"))
                {
                    params.put("stSearchAprt", fstrAprt);
                    params.put("stSearchInNo", fstrInNo);
                    params.put("stSearchMast","in_mast");

                }else if (fstrSearchType.equals("Pdct_Out"))
                {
                    params.put("stSearchAprt", fstrAprt);
                    params.put("stSearchInNo", fstrInNo);
                    params.put("stSearchMast","out_mast");
                }else if(fstrSearchType.equals("trans"))
                {
                    params.put("stSearchAprt", "");
                    params.put("stSearchInNo", "");
                    params.put("stSearchMast","trans");
                }else if (fstrSearchType.equals("pdct"))
                {
                    params.put("stPdct",fstrPdctNo);
//                    params.put("PdctInWh",fstrPdctInWh);
                }else if(fstrSearchType.equals("pdctinwh"))
                {
                    params.put("stPdct",fstrPdctNo);
                    params.put("PdctInWh",fstrPdctInWh);
                }
                else
                {
                    params.put("stSearchType", fstrSearchType);
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
                    php_search01.this,
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
            String[] lib_Seq = new String[dataInput.length()];
            String[] libAllData = new String[dataInput.length()];
            for (int i=0; i<dataInput.length(); i++) {
                JSONObject lib = dataInput.getJSONObject(i);
                if (fstrSearchType.equals("Pdct_In") || fstrSearchType.equals("Pdct_Out"))
                {
                    lib_ID[i] = lib.getString("Item01");
                    lib_NAME[i] = lib.getString("Item02");
                    lib_Qty[i] = lib.getString("Item03");
                    lib_Seq[i] = lib.getString("Item04");
                    libAllData[i] = String.format(lib.getString("Item01")+"  |  "+lib.getString("Item02")+"  |  "+lib.getString("Item03"));
                }else if (fstrSearchType.equals("trans")) {
                    lib_ID[i] = lib.getString("Item01");
                    lib_NAME[i] = lib.getString("Item02");
                    lib_Qty[i] = lib.getString("Item03");
                    lib_Seq[i] = lib.getString("Item04");
                    libAllData[i] = String.format(lib.getString("Item01") + "  |  " + lib.getString("Item04") + "\n" + lib.getString("Item02") + "  |  " + lib.getString("Item03"));
                }
                else
                {
                    lib_ID[i] = lib.getString("Item01");
                    lib_NAME[i] = lib.getString("Item02");
                    libAllData[i] = String.format(lib.getString("Item01")+"  |  "+lib.getString("Item02"));
                }

            }
            libID=lib_ID;  //傳給全域變數暫存
            libName =lib_NAME;//傳給全域變數暫存
            libQty =lib_Qty;//傳給全域變數暫存
            libSeq =lib_Seq;//傳給全域變數暫存
            return libAllData;
        } catch (JSONException e) {
            //TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

}
