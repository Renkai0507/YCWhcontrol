package tw.com.barwand.ks191113;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.util.ArrayList;

/**
 * Created by KAI on 2018/5/25.
 */

public class DataBaseHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MainDB.db";
    //public static final String TABLE_EMP = "EMP";
    //public static final String TABLE_SUPP = "SUPP";
    //public static final String TABLE_PDCT = "PDCT";
    public static final String TABLE_A01 = "A01";
    public static final String TABLE_A02 = "A02";
    //public static final String TABLE_CUST = "CUST";
    public static final int DATABASE_VERSION = 1;
    String serial = Build.SERIAL; //取得手機序號

    /*public static final String CREATE_EMP = "CREATE TABLE IF NOT EXISTS "+ TABLE_EMP+
            "(Emp_no VARCHAR(8) PRIMARY KEY NOT NULL, " +
            "Emp_name VARCHAR(50))";

    public static final String CREATE_SUPP = "CREATE TABLE IF NOT EXISTS "+ TABLE_SUPP+
            "(Supp_no VARCHAR(8) PRIMARY KEY NOT NULL, " +
            "Supp_name VARCHAR(50))";

    public static final String CREATE_PDCT = "CREATE TABLE IF NOT EXISTS "+ TABLE_PDCT+
            "(Pdct_no VARCHAR(10) PRIMARY KEY NOT NULL, " +
            "Pdct_name VARCHAR(100))";*/

    public static final String CREATE_A01 = "CREATE TABLE IF NOT EXISTS "+ TABLE_A01+
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "cust_no VARCHAR(20) , " +
            "mobile_no VARCHAR(50) ," +
            "sn_no VARCHAR(50)," +
            "qty  INTEGER," +
            "rem01 VARCHAR(50)," +
            "rem02 VARCHAR(50))" ;

    public static final String CREATE_A02 = "CREATE TABLE IF NOT EXISTS "+ TABLE_A02+
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "cust_no VARCHAR(20) , " +
            "mobile_no VARCHAR(50) ," +
            "sn_no VARCHAR(50)," +
            "qty  INTEGER," +
            "rem01 VARCHAR(50)," +
            "rem02 VARCHAR(50))" ;

    /*public static final String CREATE_CUST = "CREATE TABLE IF NOT EXISTS "+ TABLE_CUST+
            "(Cust_no VARCHAR(10)  NOT NULL, " +
            "Cust_name VARCHAR(100))";

    public static final String CREATE_A02 = "CREATE TABLE IF NOT EXISTS "+ TABLE_A02+
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Cust_no VARCHAR(10)  NOT NULL, " +
            "Pdct_no VARCHAR(10)," +
            "Qty VARCHAR(5)," +
            "Date VARCHAR(10)," +
            "QRcode VARCHAR(100))";*/

    public static final String DROP_A01="DROP TABLE IF EXISTS " + TABLE_A01;
    public static final String DROP_A02="DROP TABLE IF EXISTS " + TABLE_A02;
    //public static final String DELETE_SUPP="DROP TABLE IF EXISTS " + TABLE_SUPP;
    //public static final String DELETE_PDCT="DROP TABLE IF EXISTS " + TABLE_PDCT;
    //public static final String DELETE_EMP="DELETE FROM " + TABLE_EMP;
    //public static final String DELETE_SUPP="DELETE FROM " + TABLE_SUPP;
    //public static final String DELETE_PDCT="DELETE FROM " + TABLE_PDCT;
    public static final String DELETE_A01="DELETE FROM " + TABLE_A01;
    public static final String DELETE_A02="DELETE FROM " + TABLE_A02;
    //public static final String DELETE_CUST="DELETE FROM " + TABLE_CUST;
    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    public void onCreate(SQLiteDatabase db) {
        // Create the table
        //db.execSQL(DROP_A01);
        //db.execSQL(DROP_A02);
        //db.execSQL(CREATE_EMP);
        //db.execSQL(CREATE_SUPP);
        //db.execSQL(CREATE_PDCT);
        db.execSQL(CREATE_A01);
        //db.execSQL(CREATE_CUST);
        db.execSQL(CREATE_A02);
    }

    //Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(DELETE_EMP);
        //db.execSQL(DELETE_SUPP);
        //db.execSQL(DELETE_PDCT);
        //db.execSQL(DROP_A01);
        //db.execSQL(DROP_A02);
        //db.execSQL(DELETE_A01);
        //db.execSQL(DELETE_CUST);
        //db.execSQL(DELETE_A02);

        //Drop older table if existed

        //Create tables again
        //onCreate(db);

        //oldVersion=舊的資料庫版本；newVersion=新的資料庫版本
        if (newVersion > oldVersion) {
            db.beginTransaction();//建立交易

            boolean success = false;//判斷參數

            //由之前不用的版本，可做不同的動作
            switch (oldVersion) {
                case 1:
                    //db.execSQL("ALTER TABLE newMemorandum ADD COLUMN reminder integer DEFAULT 0");
                    //db.execSQL("ALTER TABLE newMemorandum ADD COLUMN type VARCHAR");
                    //db.execSQL("ALTER TABLE newMemorandum ADD COLUMN memo VARCHAR");
                    oldVersion++;

                    success = true;
                    break;
            }

            if (success) {
                db.setTransactionSuccessful();//正確交易才成功
            }
            db.endTransaction();
        }
        else {
            onCreate(db);
        }

    }
    /*//員工資料寫入==================================================================================
    public void insertEmp(String strEmp_no,String strEmp_name) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put("Emp_no", strEmp_no);
            values.put("Emp_name", strEmp_name);
            // Insert Row
            db.insert(TABLE_EMP, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
    }
    //廠商資料寫入===================================================================================
    public void insertSupp(String strSupp_no,String strSupp_name) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put("Supp_no", strSupp_no);
            values.put("Supp_name", strSupp_name);
            // Insert Row
            db.insert(TABLE_SUPP, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
    }
    //產品資料寫入==================================================================================
    public void insertPdct(String strPdct_no,String strPdct_name) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put("Pdct_no", strPdct_no);
            values.put("Pdct_name", strPdct_name);
            // Insert Row
            db.insert(TABLE_PDCT, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
    }
    //=============================================================================================
    //客戶資料寫入==================================================================================
    public void insertCust(String strCust_no,String strCust_name) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put("Cust_no", strCust_no);
            values.put("Cust_name", strCust_name);
            // Insert Row
            db.insert(TABLE_CUST, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
    }
    //=============================================================================================*/
    //A01資料寫入(收貨作業)==================================================================================
    public void insertA01(String strCustNo,String strMobileNo,String strSnNo, int intQty) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put("cust_no", strCustNo);
            values.put("mobile_no", strMobileNo);
            values.put("sn_no", strSnNo);
            values.put("qty", intQty);
            // Insert Row
            db.insert(TABLE_A01, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
    }
    //=============================================================================================
    //A02資料寫入(出貨作業)==================================================================================
    public void insertA02(String strCustNo,String strMobileNo,String strSnNo, int intQty) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put("cust_no", strCustNo);
            values.put("mobile_no", strMobileNo);
            values.put("sn_no", strSnNo);
            values.put("qty", intQty);
            // Insert Row
            db.insert(TABLE_A02, null, values);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
    }
    //=============================================================================================


   /* //GetEmpArray==================================================================================
    public ArrayList<String> getAllEmp(){

        ArrayList<String> list=new ArrayList<String>();
        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();

        try {

            String selectQuery = "SELECT * FROM "+ TABLE_EMP;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() >0) {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String emp_no= cursor.getString(cursor.getColumnIndex("Emp_no"));
                    String emp_name= cursor.getString(cursor.getColumnIndex("Emp_name"));
                    String EMP=emp_no + "|" + emp_name;
                    list.add(EMP);
                }
            }
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();

            // Close database
        }
        return list;

    }
    //GetSuppArray==================================================================================
    public ArrayList<String> getAllSupp(){

        ArrayList<String> list=new ArrayList<String>();
        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();

        try {

            String selectQuery = "SELECT * FROM "+ TABLE_SUPP;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() >0) {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String Supp_no= cursor.getString(cursor.getColumnIndex("Supp_no"));
                    String Supp_name= cursor.getString(cursor.getColumnIndex("Supp_name"));
                    String SUPP=Supp_no + "|" + Supp_name;
                    list.add(SUPP);
                }
            }
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();

            // Close database
        }
        return list;

    }
    //GetPdctArray==================================================================================
    public ArrayList<String> getAllPdct(){

        ArrayList<String> list=new ArrayList<String>();
        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();

        try {

            String selectQuery = "SELECT * FROM "+ TABLE_PDCT;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() >0) {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String pdct_no= cursor.getString(cursor.getColumnIndex("Pdct_no"));
                    String pdct_name= cursor.getString(cursor.getColumnIndex("Pdct_name"));
                    String PDCT=pdct_no + "|" + pdct_name;
                    list.add(PDCT);
                }
            }
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();

            // Close database
        }
        return list;

    }
    //GetCustArray==================================================================================
    public ArrayList<String> getAllCust(){

        ArrayList<String> list=new ArrayList<String>();
        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();

        try {

            String selectQuery = "SELECT * FROM "+ TABLE_CUST;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() >0) {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String Cust_no= cursor.getString(cursor.getColumnIndex("Cust_no"));
                    String Cust_name= cursor.getString(cursor.getColumnIndex("Cust_name"));
                    String Cust=Cust_no + "|" + Cust_name;
                    list.add(Cust);
                }
            }
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
        return list;
    }*/

    //GetA01(取得進貨匯總檔)==================================================================================
    public ArrayList<String> getAllA01(){
        ArrayList<String> list=new ArrayList<String>();
        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();
        try {
            String selectQuery="";

            selectQuery = "SELECT cust_no,mobile_no,sum(qty) as qty  FROM "+ TABLE_A01
                    + " group by cust_no,mobile_no "
                    + " order by cust_no " ;

            //values.put("cust_no", strCustNo);
            //values.put("mobile_no", strMobileNo);
            //values.put("sn_no", strSnNo);
            //values.put("qty", intQty);

            //selectQuery = "SELECT sum(qty)  FROM "+ TABLE_A01
            //        + " order by cust_no " ;

            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() >0) {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String strCust_no= cursor.getString(cursor.getColumnIndex("cust_no"));
                    String strMobile_no= cursor.getString(cursor.getColumnIndex("mobile_no"));
                    //String strSn_no= cursor.getString(cursor.getColumnIndex("sn_no"));
                    String strQty=Integer.toString(cursor.getInt(cursor.getColumnIndex("qty")));

                    //int intQty= cursor.getInt(cursor.getColumnIndex("qty"));
                    //String strRem01= cursor.getString(cursor.getColumnIndex("Rem01"));
                    //String strRem02= cursor.getString(cursor.getColumnIndex("Rem02"));

                    //String str = String.format("%04d", 1);  //數字前補0
                    //String str = String.format("%-10s", "A");  //字串後補空白

                    strCust_no= String.format("%-10s", strCust_no);  //字串後補空白
                    strMobile_no= String.format("%-20s", strMobile_no);  //字串後補空白
                    strQty= String.format("%-8s", strQty);  //字串後補空白

                    String A01=strCust_no + strMobile_no + strQty;
                    list.add(A01);
                }
            }
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();

            // Close database
        }
        return list;
    }
    //GetA01(取得進貨明細檔)==================================================================================
    public ArrayList<String> getAllA01List(){
        ArrayList<String> list=new ArrayList<String>();
        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();
        try {
            String selectQuery="";

            selectQuery = "SELECT *  FROM "+ TABLE_A01
                    + " order by cust_no,mobile_no,sn_no " ;

            //values.put("cust_no", strCustNo);
            //values.put("mobile_no", strMobileNo);
            //values.put("sn_no", strSnNo);
            //values.put("qty", intQty);

            //selectQuery = "SELECT sum(qty)  FROM "+ TABLE_A01
            //        + " order by cust_no " ;

            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() >0) {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String strCust_no= cursor.getString(cursor.getColumnIndex("cust_no"));
                    String strMobile_no= cursor.getString(cursor.getColumnIndex("mobile_no"));
                    String strSn_no= cursor.getString(cursor.getColumnIndex("sn_no"));
                    String strQty=Integer.toString(cursor.getInt(cursor.getColumnIndex("qty")));

                    //int intQty= cursor.getInt(cursor.getColumnIndex("qty"));
                    //String strRem01= cursor.getString(cursor.getColumnIndex("Rem01"));
                    //String strRem02= cursor.getString(cursor.getColumnIndex("Rem02"));

                    //String str = String.format("%04d", 1);  //數字前補0
                    //String str = String.format("%-10s", "A");  //字串後補空白

                    //strCust_no= String.format("%-10s", strCust_no);  //字串後補空白
                    //strMobile_no= String.format("%-20s", strMobile_no);  //字串後補空白
                    //strQty= String.format("%-8s", strQty);  //字串後補空白

                    String A01=strCust_no + "," + strMobile_no + "," + strSn_no + "," + strQty;
                    list.add(A01);
                }
            }
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();

            // Close database
        }
        return list;
    }
    //CheckA01==================================================================================
    public ArrayList<String> CheckA01(String strSnNo){
        ArrayList<String> list=new ArrayList<String>();
        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();
        try {
            String selectQuery = "SELECT Count(*) as cnt  FROM "+ TABLE_A01
                    +" where Sn_no = '"+strSnNo+"'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() >0) {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String strCnt= cursor.getString(cursor.getColumnIndex("cnt"));
                    String CheckA01=strCnt;
                    list.add(CheckA01);
                }
            }
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();

            // Close database
        }
        return list;
    }
    //CountA01==================================================================================
    public ArrayList<String> CountA01(){
        ArrayList<String> list=new ArrayList<String>();
        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();
        try {
            String selectQuery = "SELECT Count(*) as cnt,ifnull(sum(qty),0) as qty  FROM "+ TABLE_A01;
            //String selectQuery = "SELECT Count(*) as cnt , Sum(Weight) as SumWeight FROM "+ TABLE_A01+
            //" Where Cust_no like '%"+strCust_no+"%'" ;

            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() >0) {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String strCnt= cursor.getString(cursor.getColumnIndex("cnt"));
                    String strQty= cursor.getString(cursor.getColumnIndex("qty"));
                    //String CountA01=strCnt;
                    list.add(strCnt + ',' + strQty);
                    ///list.add(strQty);
                }
            }
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();

            // Close database
        }
        return list;
    }

    //GetA02(取得貨匯總檔)==================================================================================
    public ArrayList<String> getAllA02(){
        ArrayList<String> list=new ArrayList<String>();
        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();
        try {
            String selectQuery="";

            selectQuery = "SELECT cust_no,mobile_no,sum(qty) as qty  FROM "+ TABLE_A02
                    + " group by cust_no,mobile_no "
                    + " order by cust_no " ;

            //values.put("cust_no", strCustNo);
            //values.put("mobile_no", strMobileNo);
            //values.put("sn_no", strSnNo);
            //values.put("qty", intQty);

            //selectQuery = "SELECT sum(qty)  FROM "+ TABLE_A01
            //        + " order by cust_no " ;

            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() >0) {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String strCust_no= cursor.getString(cursor.getColumnIndex("cust_no"));
                    String strMobile_no= cursor.getString(cursor.getColumnIndex("mobile_no"));
                    //String strSn_no= cursor.getString(cursor.getColumnIndex("sn_no"));
                    String strQty=Integer.toString(cursor.getInt(cursor.getColumnIndex("qty")));

                    //int intQty= cursor.getInt(cursor.getColumnIndex("qty"));
                    //String strRem01= cursor.getString(cursor.getColumnIndex("Rem01"));
                    //String strRem02= cursor.getString(cursor.getColumnIndex("Rem02"));

                    //String str = String.format("%04d", 1);  //數字前補0
                    //String str = String.format("%-10s", "A");  //字串後補空白

                    strCust_no= String.format("%-10s", strCust_no);  //字串後補空白
                    strMobile_no= String.format("%-20s", strMobile_no);  //字串後補空白
                    strQty= String.format("%-8s", strQty);  //字串後補空白

                    String A02=strCust_no + strMobile_no + strQty;
                    list.add(A02);
                }
            }
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();

            // Close database
        }
        return list;
    }
    //GetA02(取得出貨明細檔)==================================================================================
    public ArrayList<String> getAllA02List(){
        ArrayList<String> list=new ArrayList<String>();
        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();
        try {
            String selectQuery="";

            selectQuery = "SELECT *  FROM "+ TABLE_A02
                    + " order by cust_no,mobile_no,sn_no " ;

            //values.put("cust_no", strCustNo);
            //values.put("mobile_no", strMobileNo);
            //values.put("sn_no", strSnNo);
            //values.put("qty", intQty);

            //selectQuery = "SELECT sum(qty)  FROM "+ TABLE_A01
            //        + " order by cust_no " ;

            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() >0) {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String strCust_no= cursor.getString(cursor.getColumnIndex("cust_no"));
                    String strMobile_no= cursor.getString(cursor.getColumnIndex("mobile_no"));
                    String strSn_no= cursor.getString(cursor.getColumnIndex("sn_no"));
                    String strQty=Integer.toString(cursor.getInt(cursor.getColumnIndex("qty")));

                    //int intQty= cursor.getInt(cursor.getColumnIndex("qty"));
                    //String strRem01= cursor.getString(cursor.getColumnIndex("Rem01"));
                    //String strRem02= cursor.getString(cursor.getColumnIndex("Rem02"));

                    //String str = String.format("%04d", 1);  //數字前補0
                    //String str = String.format("%-10s", "A");  //字串後補空白

                    //strCust_no= String.format("%-10s", strCust_no);  //字串後補空白
                    //strMobile_no= String.format("%-20s", strMobile_no);  //字串後補空白
                    //strQty= String.format("%-8s", strQty);  //字串後補空白

                    String A02=strCust_no + "," + strMobile_no + "," + strSn_no + "," + strQty;
                    list.add(A02);
                }
            }
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();

            // Close database
        }
        return list;
    }
    //CheckA02==================================================================================
    public ArrayList<String> CheckA02(String strSnNo){
        ArrayList<String> list=new ArrayList<String>();
        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();
        try {
            String selectQuery = "SELECT Count(*) as cnt  FROM "+ TABLE_A02
                    +" where Sn_no = '"+strSnNo+"'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() >0) {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String strCnt= cursor.getString(cursor.getColumnIndex("cnt"));
                    //String CheckA02=strCnt;
                    list.add(strCnt);
                }
            }
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();

            // Close database
        }
        return list;
    }
    //CountA02==================================================================================
    public ArrayList<String> CountA02(){
        ArrayList<String> list=new ArrayList<String>();
        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();
        try {
            String selectQuery = "SELECT Count(*) as cnt,ifnull(sum(qty),0) as qty  FROM "+ TABLE_A02;
            //String selectQuery = "SELECT Count(*) as cnt , Sum(Weight) as SumWeight FROM "+ TABLE_A01+
            //" Where Cust_no like '%"+strCust_no+"%'" ;

            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() >0) {
                while (cursor.moveToNext()) {
                    // Add province name to arraylist
                    String strCnt= cursor.getString(cursor.getColumnIndex("cnt"));
                    String strQty= cursor.getString(cursor.getColumnIndex("qty"));
                    //String CountA01=strCnt;
                    list.add(strCnt + ',' + strQty);
                    ///list.add(strQty);
                }
            }
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();

            // Close database
        }
        return list;
    }

    /* //GetA02==================================================================================
      public ArrayList<String> getAllA02(){
          ArrayList<String> list=new ArrayList<String>();
          // Open the database for reading
          SQLiteDatabase db = this.getReadableDatabase();
          // Start the transaction.
          db.beginTransaction();
          try {
              String selectQuery="";
              // Add province name to arraylist
              selectQuery = "SELECT A.Qty,B.Cust_name ,C.Pdct_name  FROM "+ TABLE_A02 +" as A"
                      +" left join "+TABLE_CUST+ " as B on A.Cust_no=B.Cust_no "
                      +" left join "+TABLE_PDCT+ " as C on A.Pdct_no=C.Pdct_no "
                      + " order by  A.Cust_no,A.Pdct_no " ;
              Cursor cursor = db.rawQuery(selectQuery, null);
              if(cursor.getCount() >0) {
                  while (cursor.moveToNext()) {
                      String strCust_no= cursor.getString(cursor.getColumnIndex("Cust_name"));
                      String strPdct_no= cursor.getString(cursor.getColumnIndex("Pdct_name"));
                      String strTotal= cursor.getString(cursor.getColumnIndex("Qty"));

                      String A02=strCust_no+","+strPdct_no+","+strTotal;
                      list.add(A02);
                  }
              }
              db.setTransactionSuccessful();
          }
          catch (SQLiteException e) {
              e.printStackTrace();
          }
          finally {
              db.endTransaction();
              // End the transaction.
              db.close();

              // Close database
          }
          return list;
      }

      //員工資料刪除===================================================================================
      public void  DeleteEMP() {
          // Open the database for writing
          SQLiteDatabase db = this.getWritableDatabase();
          // Start the transaction.
          db.beginTransaction();
          try {
              db.execSQL(DELETE_EMP);
              // Insert into database successfully.
              db.setTransactionSuccessful();
          }
          catch (Exception e) {
              e.printStackTrace();
          }
          finally {
              db.endTransaction();
              // End the transaction.
              db.close();
              // Close database
          }
      }
      //廠商資料刪除===================================================================================
      public void  DeleteSupp() {
          // Open the database for writing
          SQLiteDatabase db = this.getWritableDatabase();
          // Start the transaction.
          db.beginTransaction();

          try {
              db.execSQL(DELETE_SUPP);
              // Insert into database successfully.
              db.setTransactionSuccessful();
          }
          catch (Exception e) {
              e.printStackTrace();
          }
          finally {
              db.endTransaction();
              // End the transaction.
              db.close();
              // Close database
          }
      }
      //產品資料刪除===================================================================================
      public void  DeletePdct() {
          // Open the database for writing
          SQLiteDatabase db = this.getWritableDatabase();
          // Start the transaction.
          db.beginTransaction();

          try {
              db.execSQL(DELETE_PDCT);
              // Insert into database successfully.
              db.setTransactionSuccessful();
          }
          catch (Exception e) {
              e.printStackTrace();
          }
          finally {
              db.endTransaction();
              // End the transaction.
              db.close();
              // Close database
          }
      }
      //客戶資料刪除===================================================================================
      public void  DeleteCUST() {
          // Open the database for writing
          SQLiteDatabase db = this.getWritableDatabase();
          // Start the transaction.
          db.beginTransaction();
          try {
              db.execSQL(DELETE_CUST);
              // Insert into database successfully.
              db.setTransactionSuccessful();
          }
          catch (Exception e) {
              e.printStackTrace();
          }
          finally {
              db.endTransaction();
              // End the transaction.
              db.close();
              // Close database
          }
      }*/

    //A01資料刪除===================================================================================
    public void  DeleteA01() {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();

        try {
            db.execSQL(DELETE_A01);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
    }

    //A02資料刪除===================================================================================
    public void  DeleteA02() {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();

        try {
            db.execSQL(DELETE_A02);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
    }

    /*//A01資料刪除===================================================================================
    public void  DeleteA01_data(String strSupp_no,String strPdct_no,String strPack_qty) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        try {
            String selectQuery = "SELECT  Qty  FROM "+ TABLE_A01+" where Supp_no='"+strSupp_no+"' and Pdct_no='"+strPdct_no+"'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() >0) {
                while (cursor.moveToNext()) {
                    String strQty_old= cursor.getString(cursor.getColumnIndex("Qty"));
                    if (!strQty_old.equals("0")){
                        String strQty_new=String.valueOf(Integer.valueOf(strQty_old)-Integer.valueOf(strPack_qty));
                        if (!strQty_new.equals("0")){   //當刪除完後數量不等於0時，更新總數
                            String UpdateQuery="Update A01 set Qty='"+strQty_new+"' " +
                                    " where Supp_no='"+strSupp_no+"' and Pdct_no='"+strPdct_no+"'";
                            db.execSQL(UpdateQuery);
                        }else if(strQty_new.equals("0")){  //當刪除完後數量等於0時,刪除資料
                            String DeleteQuery=DELETE_A01+" where Supp_no='"+strSupp_no+"' and Pdct_no='"+strPdct_no+"'";
                            db.execSQL(DeleteQuery);
                        }
                    }
                }
            }
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();

            // Close database
        }
    }
    //A02資料刪除===================================================================================
    public void  DeleteA02() {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();

        try {
            db.execSQL(DELETE_A02);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
    }
    //A01資料刪除===================================================================================
    public void  DeleteA02_data(String strCust_no,String strPdct_no,String strPack_qty) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        try {
            String selectQuery = "SELECT  Qty  FROM "+ TABLE_A02+" where Cust_no='"+strCust_no+"' and Pdct_no='"+strPdct_no+"'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() >0) {
                while (cursor.moveToNext()) {
                    String strQty_old= cursor.getString(cursor.getColumnIndex("Qty"));
                    if (!strQty_old.equals("0")){
                        String strQty_new=String.valueOf(Integer.valueOf(strQty_old)-Integer.valueOf(strPack_qty));
                        if (!strQty_new.equals("0")){   //當刪除完後數量不等於0時，更新總數
                            String UpdateQuery="Update A02 set Qty='"+strQty_new+"' " +
                                    " where Cust_no='"+strCust_no+"' and Pdct_no='"+strPdct_no+"'";
                            db.execSQL(UpdateQuery);
                        }else if(strQty_new.equals("0")){  //當刪除完後數量等於0時,刪除資料
                            String DeleteQuery=DELETE_A02+" where Cust_no='"+strCust_no+"' and Pdct_no='"+strPdct_no+"'";
                            db.execSQL(DeleteQuery);
                        }
                    }
                }
            }
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            // End the transaction.
            db.close();

            // Close database
        }
    }*/

}
