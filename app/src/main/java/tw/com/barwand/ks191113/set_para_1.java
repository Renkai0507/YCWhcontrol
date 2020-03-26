package tw.com.barwand.ks191113;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static tw.com.barwand.ks191113.GlobalVariable.Errbeep;
import static tw.com.barwand.ks191113.GlobalVariable.OKbeep;

public class set_para_1 extends AppCompatActivity {
    Button mbtnRead,mbtnSave,mbtnClear;
    EditText medtIP, medtPort, medtPass, medtURL, medtNameSpace, medtBT, medtOraTNS, medtDep_ID, medtMac_ID, medtHouseC, medtVolume, medtRec, medtWash;
    //DataBaseHandler dataBaseHandler=new DataBaseHandler(this);

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
    private static final String recField = "RECYCLE";
    private static final String washField = "WASH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_para_1);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 讓手機螢幕保持直立模式

        initComponent();
        readData();//讀取各項設定值


        mbtnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readData();
            }
        });

        mbtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_field()){
                    saveData();
                    OKbeep(getApplicationContext(),"儲存成功!",1000,true,1000);
                }
            }
        });

        mbtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearAll();
            }
        });
    }



    // 連接 layout 上的物件
    public void initComponent() {
        mbtnRead = (Button) findViewById(R.id.btnRead);
        mbtnSave = (Button) findViewById(R.id.btnSave);
        mbtnClear = (Button) findViewById(R.id.btnClear);
        medtIP = (EditText) findViewById(R.id.edtIP);
        medtPort = (EditText) findViewById(R.id.edtPort);
        medtPass = (EditText) findViewById(R.id.edtPass);
        medtURL = (EditText) findViewById(R.id.edtURL);
        medtNameSpace = (EditText) findViewById(R.id.edtNameSpace);
        medtBT = (EditText) findViewById(R.id.edtBT);
        medtOraTNS= (EditText) findViewById(R.id.edtOraTNS);
        medtDep_ID= (EditText) findViewById(R.id.edtDep_ID);
        medtMac_ID= (EditText) findViewById(R.id.edtMac_ID);
        medtHouseC= (EditText) findViewById(R.id.edtHouseC);
        medtVolume= (EditText) findViewById(R.id.edtVolume);
        medtRec= (EditText) findViewById(R.id.edtRec);
        medtWash= (EditText) findViewById(R.id.edtWash);
    }
    public void ClearAll() {
        medtIP.setText("");
        medtPort.setText("");
        medtPass.setText("");
        medtURL.setText("");
        medtNameSpace.setText("");
        medtBT.setText("");
        medtOraTNS.setText("");
        medtDep_ID.setText("");
        medtMac_ID.setText("");
        medtHouseC.setText("");
        medtVolume.setText("");
        medtRec.setText("");
        medtWash.setText("");
        medtIP.requestFocus();
    }

    public boolean check_field() {
        boolean bolResult = true;
        //音量只介於0~100
        String strData = medtVolume.getText().toString();
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(strData);
        if(m.matches()){
            if (strData.matches("")){
                strData = "100";
                medtVolume.setText(strData);
            }

            int iVolume =Integer.parseInt(strData);
            if (iVolume>100){
                medtVolume.setText("100");
            }
        }
        else{
            Errbeep(getApplicationContext(),"音量輸入錯誤!!",1000,true,1000);
            medtVolume.selectAll();
            medtVolume.requestFocus();
            bolResult= false;
        }


        return bolResult;
    }

    public void readData(){
        //settings = getSharedPreferences(data,0);
        settings = getSharedPreferences("config.xml", MODE_PRIVATE);
        medtIP.setText(settings.getString(ipField, ""));
        medtPort.setText(settings.getString(portField, ""));
        medtPass.setText(settings.getString(passField, ""));
        medtURL.setText(settings.getString(urlField, ""));
        medtNameSpace.setText(settings.getString(nsField, ""));
        medtBT.setText(settings.getString(btField, ""));
        medtOraTNS.setText(settings.getString(tnsField, ""));
        medtDep_ID.setText(settings.getString(depidField, ""));
        medtMac_ID.setText(settings.getString(macidField, ""));
        medtHouseC.setText(settings.getString(housecField, ""));
        medtVolume.setText(settings.getString(volumeField, ""));
        medtWash.setText(settings.getString(washField, ""));
        medtRec.setText(settings.getString(recField, ""));
    }
    public void saveData(){
        //settings = getSharedPreferences(data,0);
        settings = getSharedPreferences("config.xml", MODE_PRIVATE);
        settings.edit()
                .putString(ipField, medtIP.getText().toString())
                .putString(portField, medtPort.getText().toString())
                .putString(passField, medtPass.getText().toString())
                .putString(urlField, medtURL.getText().toString())
                .putString(nsField, medtNameSpace.getText().toString())
                .putString(btField, medtBT.getText().toString())
                .putString(tnsField, medtOraTNS.getText().toString())
                .putString(depidField, medtDep_ID.getText().toString())
                .putString(macidField, medtMac_ID.getText().toString())
                .putString(housecField, medtHouseC.getText().toString())
                .putString(volumeField, medtVolume.getText().toString())
                .putString(washField, medtWash.getText().toString())
                .putString(recField, medtRec.getText().toString())
                .commit();

        //更新Global variable(全域變數)
        GlobalVariable globalVar = (GlobalVariable)getApplicationContext();
        globalVar.g_strIP=medtIP.getText().toString();
        globalVar.g_strPort=medtPort.getText().toString();
        globalVar.g_strPass=medtPass.getText().toString();
        globalVar.g_strURL="http://" + medtURL.getText().toString();
        globalVar.g_strNameSpace="http://" + medtNameSpace.getText().toString() + "/";
        globalVar.g_strOraTNS=medtOraTNS.getText().toString();
        globalVar.g_strDep_ID=medtDep_ID.getText().toString();
        globalVar.g_strMac_ID=medtMac_ID.getText().toString();
        globalVar.g_strHouseC=medtHouseC.getText().toString();
        globalVar.g_strVolume=medtVolume.getText().toString();
    }
}
