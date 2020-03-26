package tw.com.barwand.ks191113;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Vibrator;
import android.widget.Toast;

/**
 * Created by Michael on 2019/2/13.
 */
public class GlobalVariable extends Application {
    public String g_str_BUSHO ; //記錄在庫部署管理資料欄位
    public String g_str_ZAIKO_BASHO;    //記錄在庫場所資料欄位
    public String g_str_InvType;    //記錄<月次> 或<期末>

    //記錄各項設定檔
    public String g_strIP,g_strPort,g_strPass,g_strURL,g_strNameSpace,g_strBT,g_strOraTNS,g_strDep_ID,g_strMac_ID,g_strHouseC,g_strVolume;

    //設定BEEP聲
    public static void startTone(String name,int duration,int iVolume) {
        int tone = 0;
        //int iVolume = 100; // 0 ~ 100 音量，但不是裝置實際音量，而是產生音波的音量
        try {
            ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, iVolume);

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
            toneG.startTone(tone, duration); // tone : 聲音類型 /  duration : 聲音持續時間

        } catch (Exception e) {
            //Toast.makeText(this,e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public  static void OKbeep(Context ctx,String MSG,int beepTime,boolean Vibrate ,int vibrateTime) {
        // call  OKbeep(getApplicationContext(),500);
        try {
            //mRingtone.play();
            //mVibrator.vibrate(new long[]{10, 500, 100, 500, 100, 500}, -1);
            //setVibrate(1000);//設定震動
            if (Vibrate){
                Vibrator myVibrator = (Vibrator) ctx.getSystemService(Service.VIBRATOR_SERVICE);
                myVibrator.vibrate(vibrateTime);
            }

            SharedPreferences settings = ctx.getSharedPreferences("config.xml", MODE_PRIVATE);
            String strVolume=settings.getString("VOLUME", "");
            int iVolume = 100;// 0 ~ 100 音量，但不是裝置實際音量，而是產生音波的音量
            if (strVolume.matches("")){
                iVolume = 100;
            }
            else{
                iVolume =Integer.parseInt(strVolume);
            }

            startTone("beep",beepTime,iVolume);//設定BEEP聲
            if (!MSG.matches("")){
                Toast.makeText(ctx,MSG, Toast.LENGTH_SHORT).show();
                //mTxvResult.setText(errMSG);
            }

        } catch (Exception e) {
            Toast.makeText(ctx,e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /*
            public  static void Errbeep(Context ctx) {
                try {
                    //mRingtone.play();
                    //mVibrator.vibrate(new long[]{10, 500, 100, 500, 100, 500}, -1);

                    //setVibrate(1000);//設定震動
                    startTone("chirp_chirp_chirp",1000);//設定BEEP聲

                } catch (Exception e) {
                    Toast.makeText(ctx,e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            */

    public  static void Errbeep(Context ctx,String errMSG,int beepTime,boolean Vibrate ,int vibrateTime) {
        // call Errbeep(getApplicationContext(),"(錯誤聲 + 無訊息)+(震動 + 1秒)",1000,true,1000);// (錯誤聲 1秒+ 無訊息)+(震動 1秒)

        try {
            //mRingtone.play();
            //mVibrator.vibrate(new long[]{10, 500, 100, 500, 100, 500}, -1);

            //setVibrate(getApplicationContext(),1000);//設定震動
            if (Vibrate){
                Vibrator myVibrator = (Vibrator) ctx.getSystemService(Service.VIBRATOR_SERVICE);
                myVibrator.vibrate(vibrateTime);
            }

            SharedPreferences settings = ctx.getSharedPreferences("config.xml", MODE_PRIVATE);
            String strVolume=settings.getString("VOLUME", "");
            int iVolume = 100;// 0 ~ 100 音量，但不是裝置實際音量，而是產生音波的音量
            if (strVolume.matches("")){
                iVolume = 100;
            }
            else{
                iVolume =Integer.parseInt(strVolume);
            }

            startTone("chirp_chirp_chirp",beepTime,iVolume);//設定BEEP聲

            if (!errMSG.matches("")){
                Toast.makeText(ctx,errMSG, Toast.LENGTH_SHORT).show();
                //mTxvResult.setText(errMSG);
            }
        } catch (Exception e) {
            Toast.makeText(ctx,e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //設定震動
    public static void setVibrate(Context ctx,int time){
        // call  setVibrate(getApplicationContext(),1000);//設定震動
        try {
            Vibrator myVibrator = (Vibrator) ctx.getSystemService(Service.VIBRATOR_SERVICE);
            myVibrator.vibrate(time);

        } catch (Exception e) {
            Toast.makeText(ctx,e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    /*
    //放入 Sample
    GlobalVariable globalVar = (GlobalVariable)getApplicationContext();
    globalVar.g_str_BUSHO = key_UserID;

    //讀出 Sample
    GlobalVariable globalVar = (GlobalVariable)getApplicationContext();
    String UserID = lobalVar.g_str_BUSHO;
    */

    //另一種寫法,變數宣告要用 private
    /*
        // private String g_str_BUSHO;
        //記錄在庫部署管理資料欄位=======
        //修改 變數字串
        public void set_BUSHO(String data){
            this.g_str_BUSHO = data;
        }
        //顯示 變數字串
        public String get_BUSHO() {
            return g_str_BUSHO;
        }
        //==============================
    */

}

