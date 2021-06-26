package com.example.blood;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
public class CustomList_Adapter extends BaseAdapter{
    private LayoutInflater userInflater;
    private int which_list;
    private List<Class_ListDirectory> userList;
    private View lineView;
    private TextView tx_title,tx_message,tx_tarih;
    public CustomList_Adapter(Activity activity, List<Class_ListDirectory> userList, int deger) {
        userInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.userList = userList;
        which_list= deger;
    }
    @Override
    public int getCount() {
        return userList.size();
    }
    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(which_list == 1){
            lineView = userInflater.inflate(R.layout.list_mesaj, null);
            tx_title = (TextView) lineView.findViewById(R.id.text1);
            tx_message = (TextView) lineView.findViewById(R.id.text2);
            tx_tarih = (TextView) lineView.findViewById(R.id.tarih);
        }
        else if(which_list == 2){
            lineView = userInflater.inflate(R.layout.list_view_mesaj, null);
            tx_title = (TextView) lineView.findViewById(R.id.text1);
            tx_message = (TextView) lineView.findViewById(R.id.text2);
            tx_tarih = (TextView) lineView.findViewById(R.id.tarih);
        }
        else if(which_list == 3){
            lineView = userInflater.inflate(R.layout.listview_kan_ihtiyaci, null);
            tx_title = (TextView) lineView.findViewById(R.id.text1);
            tx_message = (TextView) lineView.findViewById(R.id.text2);
            tx_tarih = (TextView) lineView.findViewById(R.id.tarih);
        }
        else if(which_list == 4){
            lineView = userInflater.inflate(R.layout.extra_listview, null);
            tx_title = (TextView) lineView.findViewById(R.id.text1);
            tx_message = (TextView) lineView.findViewById(R.id.text2);
            tx_tarih = (TextView) lineView.findViewById(R.id.tarih);
        }
        Class_ListDirectory user = userList.get(i);
        tx_title.setText(user.getTitle());
        tx_message.setText(user.getMessagee());
        tx_tarih.setText(user.getTarih());
        return lineView;
    }
}
