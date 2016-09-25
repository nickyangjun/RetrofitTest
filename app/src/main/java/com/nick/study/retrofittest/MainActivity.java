package com.nick.study.retrofittest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.nick.study.annotation.ViewBind;
import com.nick.study.retrofittest.annotation.ViewClick;


public class MainActivity extends AppCompatActivity {
    final static String TAG = "MainActivity";

    @ViewBind(R.id.user_tv)
    TextView mTextView;

    @ViewBind(R.id.user_tv2)
    TextView mTextView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        com.nick.study.viewinject.ViewBind.init(this);
//        mTextView = (TextView) findViewById(R.id.user_tv);

//        ViewBindUtil.initViewBind(this);
        mTextView.setText("hahaha!!!! annotation success!!!!");
        mTextView2.setText("hahaha!!!! annotation success!!!!");


//        Call<User>  userCall = ApiFactory.gitHubAPI().getUserInfo("nickyangjun");
//        userCall.enqueue(new Callback<User>() {
//
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                Log.i(TAG, "name:  " + response.body().login + " id: " + response.body().id);
//                mTextView.setText("name:  " + response.body().login + " id: " + response.body().id);
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//
//            }
//        });

    }

    @ViewClick(id = R.id.user_tv)
    public void onClick(){
        Toast.makeText(this,"hahaha!!!! annotation click success!!!!",Toast.LENGTH_SHORT).show();
    }
}
