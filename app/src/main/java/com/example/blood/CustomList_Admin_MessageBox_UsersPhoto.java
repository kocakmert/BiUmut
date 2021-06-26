package com.example.blood;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
public class CustomList_Admin_MessageBox_UsersPhoto extends BaseAdapter {
    private LayoutInflater userInflater;
    private List<Class_Admin_MessageBox_Users_Photo> userList;
    public CustomList_Admin_MessageBox_UsersPhoto(Activity activity, List<Class_Admin_MessageBox_Users_Photo> userList) {
        userInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.userList = userList;
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
        View lineView;
        lineView = userInflater.inflate(R.layout.list_mesaj_isim, null);
        TextView textViewUserName = (TextView) lineView.findViewById(R.id.user_name);
        ImageView imageViewUserPicture = (ImageView) lineView.findViewById(R.id.user_image);
        Class_Admin_MessageBox_Users_Photo user = userList.get(i);
        textViewUserName.setText(user.getUserName());
        Picasso.get()
                .load(user.getUserImage())
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(imageViewUserPicture, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError(Exception e) {
                    }
                });
        return lineView;
    }
}
