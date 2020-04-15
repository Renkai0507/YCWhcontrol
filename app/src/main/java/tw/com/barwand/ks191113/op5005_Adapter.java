package tw.com.barwand.ks191113;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class op5005_Adapter extends BaseAdapter {
    private String[] ElementsData ;   //資料
    private LayoutInflater inflater;

    public op5005_Adapter(String[] data, LayoutInflater inflater){
        this.ElementsData = data;
        this.inflater = inflater;
    }

    public static class ViewHolder{


        public TextView Pdct;
        public TextView Spec;
        public TextView Lot;
        public TextView Qty;


    }

    @Override
    public int getCount() {
        return ElementsData.length;
    }

    @Override
    public Object getItem(int position) {
        return ElementsData[position];

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){

            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.activity_op5005_header, null);

            viewHolder.Pdct = (TextView) convertView.findViewById(R.id.txv_pdct);
            viewHolder.Spec = (TextView) convertView.findViewById(R.id.txv_spec);
            viewHolder.Lot = (TextView) convertView.findViewById(R.id.txv_lot);
            viewHolder.Qty = (TextView) convertView.findViewById(R.id.txv_qty);




            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (!ElementsData[position].equals("")){
            //隔行變色
            if (position % 2 == 0){

                SetText(viewHolder.Pdct,ElementsData[position].split("\n")[0],13,Color.WHITE,Color.BLACK,View.VISIBLE);
                SetText(viewHolder.Spec,ElementsData[position].split("\n")[1],13,Color.WHITE,Color.BLACK,View.VISIBLE);
                SetText(viewHolder.Lot,ElementsData[position].split("\n")[2],13,Color.WHITE,Color.BLACK,View.VISIBLE);
                SetText(viewHolder.Qty,ElementsData[position].split("\n")[3],13,Color.WHITE,Color.BLACK,View.VISIBLE);


            }else
            {
                SetText(viewHolder.Pdct,ElementsData[position].split("\n")[0],13,Color.parseColor("#9AC0CD"),Color.BLACK,View.VISIBLE);
                SetText(viewHolder.Spec,ElementsData[position].split("\n")[1],13,Color.parseColor("#9AC0CD"),Color.BLACK,View.VISIBLE);
                SetText(viewHolder.Lot,ElementsData[position].split("\n")[2],13,Color.parseColor("#9AC0CD"),Color.BLACK,View.VISIBLE);
                SetText(viewHolder.Qty,ElementsData[position].split("\n")[3],13,Color.parseColor("#9AC0CD"),Color.BLACK,View.VISIBLE);

            }

        /*if (ElementsData[position].split("\n")[10].equals("1"))
        {
            SetText(viewHolder.Id,ElementsData[position].split("\n")[0],13,Color.WHITE,Color.BLACK,View.VISIBLE);
            SetText(viewHolder.Empno,ElementsData[position].split("\n")[1],13,Color.WHITE,Color.BLACK,View.VISIBLE);
            SetText(viewHolder.Sysno,ElementsData[position].split("\n")[2],13,Color.WHITE,Color.BLACK,View.VISIBLE);
            SetText(viewHolder.Barcode,ElementsData[position].split("\n")[3],13,Color.WHITE,Color.BLACK,View.VISIBLE);
            SetText(viewHolder.Pdctno,ElementsData[position].split("\n")[4],13,Color.WHITE,Color.BLACK,View.VISIBLE);
            SetText(viewHolder.Lotno,ElementsData[position].split("\n")[5],13,Color.WHITE,Color.BLACK,View.VISIBLE);
            SetText(viewHolder.Date,ElementsData[position].split("\n")[6],13,Color.WHITE,Color.BLACK,View.VISIBLE);
            SetText(viewHolder.Days,ElementsData[position].split("\n")[7],13,Color.WHITE,Color.BLACK,View.VISIBLE);
            SetText(viewHolder.Wsno,ElementsData[position].split("\n")[8],13,Color.WHITE,Color.BLACK,View.VISIBLE);
            SetText(viewHolder.UpdFlag,ElementsData[position].split("\n")[9],13,Color.WHITE,Color.BLACK,View.VISIBLE);
            SetText(viewHolder.DataFlag,ElementsData[position].split("\n")[10],13,Color.WHITE,Color.BLACK,View.VISIBLE);

        }else{
            SetText(viewHolder.Id,ElementsData[position].split("\n")[0],13,Color.RED,Color.WHITE,View.VISIBLE);
            SetText(viewHolder.Empno,ElementsData[position].split("\n")[1],13,Color.RED,Color.WHITE,View.VISIBLE);
            SetText(viewHolder.Sysno,ElementsData[position].split("\n")[2],13,Color.RED,Color.WHITE,View.VISIBLE);
            SetText(viewHolder.Barcode,ElementsData[position].split("\n")[3],13,Color.RED,Color.WHITE,View.VISIBLE);
            SetText(viewHolder.Pdctno,ElementsData[position].split("\n")[4],13,Color.RED,Color.WHITE,View.VISIBLE);
            SetText(viewHolder.Lotno,ElementsData[position].split("\n")[5],13,Color.RED,Color.WHITE,View.VISIBLE);
            SetText(viewHolder.Date,ElementsData[position].split("\n")[6],13,Color.RED,Color.WHITE,View.VISIBLE);
            SetText(viewHolder.Days,ElementsData[position].split("\n")[7],13,Color.RED,Color.WHITE,View.VISIBLE);
            SetText(viewHolder.Wsno,ElementsData[position].split("\n")[8],13,Color.RED,Color.WHITE,View.VISIBLE);
            SetText(viewHolder.UpdFlag,ElementsData[position].split("\n")[9],13,Color.RED,Color.WHITE,View.VISIBLE);
            SetText(viewHolder.DataFlag,ElementsData[position].split("\n")[10],13,Color.RED,Color.WHITE,View.VISIBLE);

        }*/

        }else{

            SetText(viewHolder.Pdct,"",13,Color.WHITE,Color.BLACK,View.VISIBLE);
            SetText(viewHolder.Spec,"",13,Color.WHITE,Color.BLACK,View.VISIBLE);
            SetText(viewHolder.Lot,"",13,Color.WHITE,Color.BLACK,View.VISIBLE);
            SetText(viewHolder.Qty,"",13,Color.WHITE,Color.BLACK,View.VISIBLE);
        }


        return convertView;
    }



    public void SetText(TextView TV,String txt,int txtsize,int backgroundcolor,int textcolor,int visable){
        TV.setText(txt);
        TV.setTextSize(txtsize);
        TV.setBackgroundColor(backgroundcolor);
        TV.setTextColor(textcolor);
        TV.setVisibility(visable);

    }

}
