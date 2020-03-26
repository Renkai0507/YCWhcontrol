package tw.com.barwand.ks191113;

import android.app.DatePickerDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import tw.com.flag.api.FlagBt;
import tw.com.flag.api.OnFlagMsgListener;

//import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
//import static android.icu.text.Normalizer.NO;
//import static android.os.Build.VERSION_CODES.M;
//import static java.nio.charset.CodingErrorAction.IGNORE;

public class WorkPrint01 extends AppCompatActivity
        implements OnFlagMsgListener {

    Spinner mspnEmp,mspnSupp,mspnPdct;
    Button mbtnPrint,mbtnSelect,mbtnConnect,mbtnDisconnect,mbtnSearchA01,mbtnWork01Del;
    TextView mtxvCount,mtxvIndate;
    RadioGroup mrdgSupp;
    EditText medtqty,medtprintqty,medtEmpNo,medtweight,medtweight2,medtPartNo;
    FlagBt bt; //宣告藍牙物件;
    String fstrIP,fstrPort,fstrPass,fstrURL,fstrNameSpace,fstrBT;
    DataBaseHandler dataBaseHandler=new DataBaseHandler(this);
    private static final int REQUEST_Emp = 1;
    private int mYear, mMonth, mDay;
    //先行定義時間格式
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    private SharedPreferences settings;
    private static final String data = "DATA";
    private static final String ipField = "IP";
    private static final String portField = "PORT";
    private static final String passField = "PASS";
    private static final String urlField = "URL";
    private static final String nsField = "NAMESPACE";
    private static final String btField = "BT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workprint_01);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 讓手機螢幕保持直立模式
        bt = new FlagBt(this); // 建立藍牙物件

        //mBtNameOrMac="84:25:3F:1F:8A:3D";

        mtxvCount=(TextView)findViewById(R.id.txvCount);
        medtqty=(EditText)findViewById(R.id.edtQty);
        medtprintqty=(EditText)findViewById(R.id.edtPrintQty);
        medtweight=(EditText) findViewById(R.id.edtWeight);
        medtweight2=(EditText) findViewById(R.id.edtWeight2);
        medtPartNo=(EditText) findViewById(R.id.edtPartNo);
        mrdgSupp = (RadioGroup) findViewById(R.id.rdgSupp);

        //取得現在時間
        Date dt=new Date();
        //透過SimpleDateFormat的format方法將Date轉為字串
        String dts=sdf.format(dt);  //日期

        readData(); //讀取各項設定值

        connect(); // 連接藍芽印表機

        mtxvIndate=(TextView)  findViewById(R.id.txvIndate);
        mtxvIndate.setText(dts);
        mtxvIndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDate();
            }
        });


        //員工Spinner===============================================================================================
        mspnEmp=(Spinner)findViewById(R.id.spnEmp);
        //ArrayList<String> listEmp=dataBaseHandler.getAllEmp();
        //ArrayAdapter<String> adapterEmp=new ArrayAdapter<String>(this, R.layout.spinner_emp, R.id.text, listEmp);
        //mspnEmp.setAdapter(adapterEmp);
        //=========================================================================================================

        //廠商Spinner===============================================================================================
        mspnSupp=(Spinner)findViewById(R.id.spnSup);
        //ArrayList<String> listSupp=dataBaseHandler.getAllSupp();
        //ArrayAdapter<String> adapterSupp=new ArrayAdapter<String>(this, R.layout.spinner_supp, R.id.text, listSupp);
        //mspnSupp.setAdapter(adapterSupp);
        //=========================================================================================================

        //產品Spinner===============================================================================================
        mspnPdct=(Spinner)findViewById(R.id.spnPdct);
        //ArrayList<String> listPdct=dataBaseHandler.getAllPdct();
        //ArrayAdapter<String> adapterPdct=new ArrayAdapter<String>(this, R.layout.spinner_pdct, R.id.text, listPdct);
        //mspnPdct.setAdapter(adapterPdct);
        //=========================================================================================================

        //CountA01();
        mbtnWork01Del=(Button)findViewById(R.id.btnWork01_del);
        mbtnWork01Del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent it = new Intent(WorkPrint01.this, Work01_del.class);
                //startActivity(it);
            }
        });
        mbtnSearchA01=(Button)findViewById(R.id.btnSearchA01);
        mbtnSearchA01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent it = new Intent(WorkPrint01.this,SearchA01.class);
                //startActivity(it);
            }
        });
        mbtnConnect=(Button)findViewById(R.id.btnCon);
        mbtnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //connect(view);
                connect();
            }
        });

        mbtnDisconnect=(Button)findViewById(R.id.btnDiscon);
        mbtnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quit(view);
            }
        });

        mbtnPrint=(Button)findViewById(R.id.btnPrint);

        mbtnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WorkPrint01.this);
                builder.setIcon(android.R.mipmap.sym_def_app_icon);
                builder.setTitle("收貨資料確認");
                builder.setMessage("請確認收貨資料是否正確?");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //取得當前按鈕背景顏色=================================================
                        Drawable background = mbtnPrint.getBackground();
                        ColorDrawable colorDrawable = (ColorDrawable) background;
                        int color = colorDrawable.getColor();
                        //=====================================================================
                        if (color==0Xffd6d7d7){

                            if (medtPartNo.getText().toString().matches("")||medtqty.getText().toString().matches("")||medtprintqty.getText().toString().matches("")){
                                Toast.makeText(WorkPrint01.this, "品號、每包數量與總包數不能是空白!!", Toast.LENGTH_LONG).show();
                            }
                            else{
                                print(view);
                                //CountA01();
                            }

                        }
                        else{
                            Toast.makeText(WorkPrint01.this,"請先連線印表機", Toast.LENGTH_LONG).show();
                        }
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
    }
    public void onDestroy() {
        bt.stop(); // 確保程式結束前會停止藍牙連線
        super.onDestroy();
    }


    //設定震動
    public void setVibrate(int time){
        try {
            Vibrator myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
            myVibrator.vibrate(time);

        } catch (Exception e) {
            Toast.makeText(WorkPrint01.this,e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //設定BEEP聲
    protected void startTone(String name,int duration) {
        int tone = 0;
        try {
            ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);

            if (name.equals("beep")) {
                tone = ToneGenerator.TONE_PROP_BEEP;
            } else if (name.equals("beep_beep_beep")) {
                tone = ToneGenerator.TONE_CDMA_CONFIRM;
            } else if (name.equals("long_beep")) {
                tone = ToneGenerator.TONE_CDMA_ABBR_ALERT;
            } else if (name.equals("doodly_doo")) {
                tone = ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE;
            } else if (name.equals("chirp_chirp_chirp")) {
                tone = ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD;
            } else if (name.equals("dialtone")) {
                tone = ToneGenerator.TONE_SUP_RINGTONE;
            }
            toneG.startTone(tone, duration);

        } catch (Exception e) {
            Toast.makeText(WorkPrint01.this,e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void Errbeep() {
        try {
            //mRingtone.play();
            //mVibrator.vibrate(new long[]{10, 500, 100, 500, 100, 500}, -1);

            setVibrate(1000);//設定震動
            startTone("chirp_chirp_chirp",1000);//設定BEEP聲

        } catch (Exception e) {
            Toast.makeText(WorkPrint01.this,e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void Errbeep(String errMSG) {
        try {
            //mRingtone.play();
            //mVibrator.vibrate(new long[]{10, 500, 100, 500, 100, 500}, -1);

            setVibrate(1000);//設定震動
            startTone("chirp_chirp_chirp",1000);//設定BEEP聲

            if (!errMSG.matches("")){
                Toast.makeText(WorkPrint01.this,errMSG, Toast.LENGTH_SHORT).show();
                //mTxvResult.setText(errMSG);
            }
        } catch (Exception e) {
            Toast.makeText(WorkPrint01.this,e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void readData(){
        //settings = getSharedPreferences(data,0);
        settings =getSharedPreferences("config.xml", MODE_PRIVATE);
        fstrIP=settings.getString(ipField, "");
        fstrPort=settings.getString(portField, "");
        fstrPass=settings.getString(passField, "");
        fstrURL="http://" + settings.getString(urlField, "");
        fstrNameSpace="http://" + settings.getString(nsField, "") + "/";;
        fstrBT=settings.getString(btField, "");
    }

    public void connect() {
        if(!bt.connect()) // 選取已配對裝置進行連線(不指定名稱,由已配對的設備選擇)
            Toast.makeText(this,"找不到任何已配對裝置", Toast.LENGTH_SHORT).show();

        //if(!bt.connect("RP2-17250B1302")) // 選取已配對裝置進行連線(指定名稱)
        //if(!bt.connect("84:25:3F:1F:8A:3D")) // 選取已配對裝置進行連線 (指定 BT MAC)
        //if(!bt.connect(this.fstrBT)) // 選取已配對裝置進行連線 (變數代入BT名稱或MAC)

        //if(!bt.connect(this.fstrBT)) // 選取已配對裝置進行連線 (參數設定 BT Name or MAC)
        //    Toast.makeText(this,"找不到任何已配對裝置", Toast.LENGTH_SHORT).show();
        //txv.setText("找不到任何已配對裝置");
    }

    public void print(View v) {
        int id;
        String strPrintModel = "";
        /*===================================================================
                                    如果用“.”作為分隔的話，必須是如下寫法：
                                    String.split("\\."),這樣才能正確的分隔開，不能用String.split(".");
                                    如果用“|”作為分隔的話，必須是如下寫法：
                                    String.split("\\|"),這樣才能正確的分隔開，不能用String.split("|");
                  ===================================================================*/
        String stremp="E001";// String.valueOf(mspnEmp.getSelectedItem()).split("\\|")[0];      //員工代號
        String stremp_desc="管理者";// String.valueOf(mspnEmp.getSelectedItem()).split("\\|")[1]; //員工姓名

        String strpdct="LOT001";// String.valueOf(mspnPdct.getSelectedItem()).split("\\|")[0];       //產品代號
        String strpdct_desc="測試品號";// String.valueOf(mspnPdct.getSelectedItem()).split("\\|")[1];  //產品名稱

        String strsupp="S001";// String.valueOf(mspnSupp.getSelectedItem()).split("\\|")[0];       //廠商代號
        String strsupp_desc="八旺";// String.valueOf(mspnSupp.getSelectedItem()).split("\\|")[1];  //廠商名稱

       // String strsupp_big5=new String(strsupp_desc.getBytes(),"Big5");
        /*try {
            strsupp_desc = new String(strsupp_desc.getBytes(),"Big5");
            stremp_desc = new String(stremp_desc.getBytes(),"Big5");
            strpdct_desc = new String(stremp_desc.getBytes(),"Big5");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/

        String strqty=medtqty.getText().toString(); //每包數量
        String strPrintqty=medtprintqty.getText().toString();     //總包數

        String strQty=String.valueOf(Integer.valueOf(strqty)*Integer.valueOf(strPrintqty)); //每包數量*總包數
        String strdate=mtxvIndate.getText().toString(); //收貨日期
        String strwgt=medtweight.getText().toString(); //重量
        String strwgt2=medtweight2.getText().toString(); //重量

        String strQRcode= strpdct+"|"+ strqty+"|"+strdate+"|"+strsupp;  //QRcode

        String strPartNo=medtPartNo.getText().toString(); // 品號

        id = mrdgSupp.getCheckedRadioButtonId();
        switch (id) {
            case R.id.rdoA3230B:  // Argox AME-3230B
                strPrintModel = "AME-3230B";
                break;

            case R.id.rdoPR2:  // Honeywell  PR2 / PR3
                strPrintModel = "PR2";
                break;

            case R.id.rdoRP2:  // Honeywell  RP2
                strPrintModel ="RP2";
                break;

            case R.id.rdoPC4X:  // Honeywell  PC42,43
                strPrintModel ="PC4X";
                break;
        }

        try {
            //寫入列印記錄
            //dataBaseHandler.insertA01(stremp,strsupp,strpdct,strQty,strdate,strQRcode);
            String strPrint_AME_3230B="",strPrint_PR2="",strPrint_RP2="",strPrint_PC4X="";

            //ARGOX AME-3230B  公司測試標籤格式==========================================================
            /*
                        strPrint_AME_3230B ="^XA^MNN^PR2^PW600^LL0240"+"" +
                                "^A@N,30,20,R:900.ARF^FO15,15^FD種類:^FS"+"" +
                                "^A@N,30,20,R:900.ARF^FO115,15^FD"+strpdct_desc+"^FS"+"" +
                                "^A@N,30,20,R:900.ARF^FO15,65^FD品號:^FS"+"" +
                                "^A@N,30,20,R:900.ARF^FO115,65^FD"+strpdct+"^FS"+"" +
                                "^A@N,30,20,R:900.ARF^FO15,115^FD每包數量:^FS"+"" +
                                "^A@N,30,20,R:900.ARF^FO190,115^FD"+strqty+"^FS"+"" +
                                "^FO330,80^BQN,2,4^FDLA,"+strQRcode+"^FS"+"" +
                                "^PQ"+strPrintqty+""+
                                "^XZ";
                    */
            //==========================================================================

            //ARGOX AME-3230B 實際標籤格式(伯樂檳榔)==================================================
                     strPrint_AME_3230B ="^XA^PR2^LL0240" +
                    "^XA^LH0,0"+
                    "^A@N,30,20,R:900.ARF^FO15,15^FD種類:^FS"+"" +
                    "^A@N,30,20,R:900.ARF^FO115,15^FD"+strpdct_desc+"^FS"+"" +
                    "^A@N,30,20,R:900.ARF^FO15,60^FD送達地:              店^FS"+"" +
                    "^A@N,30,20,R:900.ARF^FO15,105^FD重量:^FS"+"" +
                    "^A@N,30,20,R:900.ARF^FO115,105^FD"+strwgt+" 斤 "+strwgt2+" 兩"+"^FS"+"" +
                    "^A@N,30,20,R:900.ARF^FO15,150^FD每包數量:^FS"+"" +
                    "^A@N,30,20,R:900.ARF^FO190,150^FD"+strqty+"^FS"+"" +
                    "^A@N,30,20,R:900.ARF^FO350,150^FD"+ strPrintModel +"^FS"+"" +
                    "^A@N,30,20,R:900.ARF^FO15,195^FD收貨日期:^FS"+"" +
                    "^A@N,30,20,R:900.ARF^FO190,195^FD"+strdate+"^FS"+"" +
                    "^FO350,5^BQN,2,4^FDLA,"+strQRcode+"^FS"+"" +
                    "^PQ"+strPrintqty+""+
                    "^XZ";

                    //bt.write(strPrint.getBytes("Big5"));
            //=============================================================
            //ZPL
            //            strPrint ="^XA^LL0240^FO100,100^A0,40,40^FDTEST^FS^PQ1^XZ";

            //Honeywell RP2 (CPCL)指令測試標籤格式==========================================================
            /*
                   strPrint_RP2 ="! 0 203 203 400 1" + "\n" +
                            "CONTRAST 3" + "\n" +
                            "COUNTRY USA" + "\n" +
                            "SETMAG 1 1" + "\n" +
                            "T 5 0 10 10 Chinese TEST Sample Start" + "\n" +
                            "COUNTRY BIG5" + "\n" +
                            "ST kaiu.ttf 10 10 0 30 八旺實業測試標籤" + "\n" +
                            "ST kaiu.ttf 10 10 180 190 每包數量:" + "\n" +
                            "ST kaiu.ttf 10 10 180 230 " + strqty + "\n" +
                            "ST kaiu.ttf 10 10 180 270 總包數:" + "\n" +
                            "ST kaiu.ttf 10 10 180 310 " + strPrintqty + "\n" +
                            "COUNTRY USA" + "\n" +
                            "BARCODE 128 1 2 75 10 85 ABCD12345" + "\n" +
                            "B QR 10 200 M 2 U 5" + "\n" +
                            "MA,QR code 1234This is the data barwand test" + "\n" +
                            "ENDQR" + "\n" +
                            "T 5 0 10 360 Chinese TEST Sample END" + "\n" +
                            "PRINT" + "\n";
                            */

                    //bt.write(strPrint.getBytes("Big5"));
            //==========================================================================
            //Honeywell RP2 (DPL)指令測試標籤格式==========================================================
            //中文測試(UTF-8)
            String strSTX="\u0002";

            strPrint_RP2 =strSTX + "L" + "\n" +
                    "D11" + "\n" +
                    "H14" + "\n" +
                    "3911S6100100160P010P010八旺實業 Bar Wand" + "\n" +
                    "321100000300160QTY : " + strqty + "\n" +
                    "3911S6100500160P010P010總包數 : " + strPrintqty + "\n" +
                    "Q0001" + "\n" +
                    "E" + "\n";

            //bt.write(strPrint_RP2.getBytes("UTF-8")); //DPL
            //==========================================================================

            //Honeywell PR2-PR3(CPCL)指令測試標籤格式==========================================================
            strPrint_PR2 ="! 0 200 200 800 1" + "\n" +
                    "TONE 0" + "\n" +
                    "SPEED 3" + "\n" +
                    "ON-FEED IGNORE" + "\n" +
                    "NO-PACE" + "\n" +
                    "JOURNAL" + "\n" +
                    "CENTER" + "\n" +
                    "COUNTRY BIG5" + "\n" +
                    "SETMAG 3 3" + "\n" +
                    "TEXT 55 0 0 130 八旺-電子發票證明聯" + "\n" +
                    "TEXT 55 0 0 180      103年01-02月" + "\n" +
                    "SETMAG 2 2" + "\n" +
                    "T 7 0 50 250    AB-11223344" + "\n" +
                    "SETMAG 2 2" + "\n" +
                    "LEFT" + "\n" +
                    "T 55 0 32 290 2014-02-05 15:56:33" + "\n" +
                    "T 55 0 30 320 隨機碼 9999  總計 NT$1300" + "\n" +
                    "T 55 0 30 350 賣方 70764319" + "\n" +
                    "B 39 0 3 50 30 400 10304FU297984424165" + "\n" +
                    "B QR 40 480 M 2 U 5" + "\n" +
                    "MA,http://www.barwand.com.tw/" + "\n" +
                    "ENDQR" + "\n" +
                    "PRINT" + "\n";

            //bt.write(strPrint.getBytes("Big5"));
            //==========================================================================
            //Honeywell PC43 (DP)指令測試標籤格式==========================================================


            strPrint_PC4X ="NASC 8" + "\n" +
                    "BF ON" + "\n" +
                    "PP 100,200" + "\n" +
                    "BF" + "\"" + "MSung HK Medium\",10" + "\n" +
                    "BT \"CODE39A\"" + "\n" +
                    "PB \"" + strPartNo + "\"" + "\n" +
                    "PP 100,130" + "\n" +
                    "FT \"MSung HK Medium\",12" + "\n" +
                    "PT \"每包數量 : " + strqty + "\""+ "\n" +
                    "PP 100,80" + "\n" +
                    "FT \"MSung HK Medium\",12" + "\n" +
                    "PT \"總包數 : " + strPrintqty + "\""+ "\n" +
                    "PP 450,80" + "\n" +
                    "PM \"GLOBE.1\"" + "\n" +
                    "PF" + "\n";



            //bt.write(strPrint.getBytes("Big5"));
            //==========================================================================
            //bt.write(strPrint);
            /*
                        if (strPrintModel.equals("AME-3230B")) {
                            bt.write(strPrint_AME_3230B.getBytes("Big5"));
                        }
                        else if(strPrintModel.equals("PR2")) {
                            bt.write(strPrint_PR2.getBytes("Big5"));
                        }
                        else if(strPrintModel.equals("RP2")) {
                            bt.write(strPrint_RP2.getBytes("Big5"));
                        }
                        */

            switch (strPrintModel) {
                case "AME-3230B" :
                    bt.write(strPrint_AME_3230B.getBytes("Big5"));
                case "PR2" :
                    bt.write(strPrint_PR2.getBytes("Big5"));
                    break;
                case "RP2" :
                    //bt.write(strPrint_RP2.getBytes("Big5")); //CPCL
                    bt.write(strPrint_RP2.getBytes("UTF-8")); //DPL
                    break;
                case "PC4X" :
                    bt.write(strPrint_PC4X.getBytes("UTF-8"));
                    //bt.write(strPrint_PC4X.getBytes());
                default:
                    break;
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
        /*catch (Exception e) {
            Log.e("MainActivity", "Exe ", e);
        }*/

    }
    public void quit(View v) {
        bt.stop();
        //finish();
    }

    @Override
    public void onFlagMsg(Message msg) {
        switch(msg.what) {
            case FlagBt.CONNECTING: // 嘗試與已配對裝置連線
                //Toast.makeText(this,"正在連線到：" + bt.getDeviceName(), Toast.LENGTH_SHORT).show();
                //txv.setText("正在連線到：" + bt.getDeviceName());
                break;
            case FlagBt.CONNECTED:  // 與已配對裝置連線成功
                //Toast.makeText(this,"已連線到：" + bt.getDeviceName(), Toast.LENGTH_SHORT).show();
                mbtnPrint.setBackgroundColor(0Xffd6d7d7);
                //colorButtonNormal => @color/button_material_light => #ffd6d7d7
                //txv.setText("已連線到：" + bt.getDeviceName());
                break;
            case FlagBt.CONNECT_FAIL: // 連線失敗
                //Toast.makeText(this,"連線失敗！請重連", Toast.LENGTH_SHORT).show();
                mbtnPrint.setBackgroundColor(0Xffff4444);
                //txv.setText("連線失敗！請重連");
                break;
            case FlagBt.CONNECT_LOST: // 目前連線意外中斷
                //Toast.makeText(this,"連線中斷!請重連", Toast.LENGTH_SHORT).show();
                mbtnPrint.setBackgroundColor(0Xffff4444);
                //txv.setText("連線中斷!請重連");
                break;
        }
    }
    public void showDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(WorkPrint01.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String format = setDateFormat(year,month,day);
                try {
                    Date dt =sdf.parse(format);
                    String strindate=sdf.format(dt);
                    mtxvIndate.setText(strindate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, mYear,mMonth, mDay).show();
    }
    private String setDateFormat(int year,int monthOfYear,int dayOfMonth) {
        // 日期格式：yyyy-mm-dd
        return String.valueOf(year) + "/"
                + String.valueOf(monthOfYear + 1) + "/"
                + String.valueOf(dayOfMonth);
    }
}
