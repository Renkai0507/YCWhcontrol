package tw.com.barwand.ks191113;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

import java.util.Hashtable;
import java.util.Map;

import static tw.com.barwand.ks191113.GlobalVariable.Errbeep;

public class input_dialog_1 extends AppCompatActivity {
    RequestQueue mQueue;
    String mUrl = "";
    Button mbtnSure,mbtnCancel;
    EditText medtUserNo, medtUserPass;
    TextView mTxvResult;
    String fstrIP,fstrPort,fstrPass,fstrURL,fstrNameSpace;
    StringRequest mGetRequest;
    String OutputData = "";

    //DataBaseHandler dataBaseHandler=new DataBaseHandler(this);

    private SharedPreferences settings;
    private static final String data = "DATA";
    private static final String ipField = "IP";
    private static final String portField = "PORT";
    private static final String passField = "PASS";
    private static final String urlField = "URL";
    private static final String nsField = "NAMESPACE";
    private static final int REQUEST_Emp = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_dialog_1);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 讓手機螢幕保持直立模式

        initComponent();
        readData();//讀取各項設定值

        mbtnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserNo=medtUserNo.getText().toString();
                String UserPass=medtUserPass.getText().toString();
                //readData();//讀取各項設定值
                //volley_post_check("Main");
                //int flag = UserPass.compareTo(fstrPass);
                boolean flag=UserPass.equals(fstrPass); // equals 效率比 compareTo 高.
                if (flag == true){
                    startSetupPara();
                    finish();
                }
                else {
                    Toast.makeText(input_dialog_1.this,
                            "密碼錯誤,請重新輸入!!",
                           Toast.LENGTH_SHORT).show();
                    mTxvResult.setText("密碼錯誤!!");
                    medtUserPass.setText("");
                    medtUserPass.requestFocus();
                }

            }
        });

        mbtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //finish();
            readData();//讀取各項設定值
            //if (fstrIP.equals("") || fstrPort.equals(""))
            //{
                //startSetupPara();
            //}else
            //{
                //volley_post_check("Setup");
            //}

            String UserPass=medtUserPass.getText().toString();

            boolean flag=UserPass.equals(fstrPass); // equals 效率比 compareTo 高.
            if (flag == true){
                startSetupPara();
                //finish();
            }
            else {
                Toast.makeText(input_dialog_1.this,
                        "密碼錯誤,請重新輸入!!",
                       Toast.LENGTH_SHORT).show();
                mTxvResult.setText("密碼錯誤!!");
                medtUserPass.setText("");
                medtUserPass.requestFocus();
            }



            }
        });

    }


    // 連接 layout 上的物件
    public void initComponent() {
        mbtnSure = (Button) findViewById(R.id.btnSure);
        mbtnCancel = (Button) findViewById(R.id.btnCancel);
        medtUserNo = (EditText) findViewById(R.id.edtUserNo);
        medtUserPass = (EditText) findViewById(R.id.edtUserPass);
        mTxvResult = (TextView) findViewById(R.id.txvResult);
    }
    public void ClearAll() {
        medtUserNo.setText("");
        medtUserPass.setText("");
        mTxvResult.setText("");
        medtUserNo.requestFocus();
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

    public void startSetupPara() {
        Intent it = new Intent(this, set_para_1.class);
        startActivity(it);
    }


    // 利用 Volley 實現 POST 請求
    private void volley_post_check(final String Type) {
        String result = "",methodName="",SOAP_ACTION1="",NAMESPACE="";
        String strbits="";
        //final String W=medtInput.getText().toString().split(";")[3];
        //final String LH=medtInput.getText().toString().split(";")[0];
        //final String PH=medtInput.getText().toString().split(";")[1];
        OutputData = "";
        mUrl="http://" + fstrIP + ":" +fstrPort + "/ycworksys/yc_checkEmp.php";

        mQueue = Volley.newRequestQueue(getApplicationContext());
        final String finalStrbits = strbits;
        mGetRequest = new  StringRequest(Request.Method.POST, mUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            int n =new JSONArray(response).length(); //筆數
                            int i=1;//取第1筆
                            String stMsg =new JSONArray(response).getJSONObject(i-1).getString("OUTPUT");

                            if (stMsg.equals("OK")){
                                /*Toast.makeText(getApplicationContext(),"資料寫入成功",Toast.LENGTH_SHORT).show();
                                mTxvResult.setText("資料寫入成功!!");
                                mTxvResult.setTextColor(Color.GREEN);*/
                                if (Type.equals("Main")){
                                    String stAllow =new JSONArray(response).getJSONObject(i-1).getString("Item01");
                                    if (stAllow.equals("Y")) {
                                        startMainActive();
                                    }
                                    else{
                                        Errbeep(getApplicationContext(),"此帳號無效!",1000,true,1000);
                                        mTxvResult.setText("此帳號無效!");
                                        mTxvResult.setTextColor(Color.RED);
                                        ClearAll();
                                    }
                                }else{
                                    String stAllow =new JSONArray(response).getJSONObject(i-1).getString("Item01");
                                    String stAllow2 =new JSONArray(response).getJSONObject(i-1).getString("Item02");
                                    if (stAllow.equals("Y") && stAllow2.equals("Y") ) {
                                        startSetupPara();
                                    }
                                    else{
                                        Errbeep(getApplicationContext(),"此帳號不可進入設定畫面!",1000,true,1000);
                                        mTxvResult.setText("此帳號不可進入設定畫面!");
                                        mTxvResult.setTextColor(Color.RED);
                                    }
                                }

                            }
                            else if (stMsg.equals("NG")) {
                                Errbeep(getApplicationContext(),"帳號密碼錯誤!",1000,true,1000);
                                mTxvResult.setText("帳號密碼錯誤!");
                                mTxvResult.setTextColor(Color.RED);
                                ClearAll();

                            }else
                            {
                                Errbeep(getApplicationContext(),"資料查詢錯誤!",1000,true,1000);
                                mTxvResult.setText(stMsg);
                                mTxvResult.setTextColor(Color.RED);
                                ClearAll();

                            }

                            //mtxvW.setText("");

                            //mTxvResult.setText(OutputData);

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"Not available",Toast.LENGTH_SHORT).show();
                            mTxvResult.setText(e.getMessage());
                            mTxvResult.setTextColor(Color.RED);
                            e.printStackTrace();
                        }

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
                params.put("stEmpNo", medtUserNo.getText().toString());
                params.put("stEmpPwd", medtUserPass.getText().toString());
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

    public void startMainActive() {
        Intent it = new Intent(this,MainActivity.class);
        //new一個Bundle物件，並將要傳遞的資料傳入
        Bundle bundle = new Bundle();
        //bundle.putDouble("SearchType","");
        bundle.putString("Emp", medtUserNo.getText().toString());
        //將Bundle物件assign給intent
        it.putExtras(bundle);
        ClearAll();
        //startActivity(it);
        startActivityForResult(it, REQUEST_Emp);
        //finish();
    }
}
