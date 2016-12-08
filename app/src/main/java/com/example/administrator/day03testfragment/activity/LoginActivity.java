package com.example.administrator.day03testfragment.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.day03testfragment.R;
import com.example.administrator.day03testfragment.utils.MyUsersSqlite;
import com.example.administrator.day03testfragment.utils.ShareSPUtils;

public class LoginActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView exits_iv,qqLogin_iv;
    TextInputLayout nameTextInput,pwdTextInput;
    EditText name_et,pwd_et;
    TextView forget_tv,regist_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar= (Toolbar) findViewById(R.id.toobar);
        exits_iv= (ImageView) findViewById(R.id.exits_iv);
        qqLogin_iv= (ImageView) findViewById(R.id.qqLogin_iv);
        nameTextInput= (TextInputLayout) findViewById(R.id.nameTextInput);
        pwdTextInput= (TextInputLayout) findViewById(R.id.pwdTextInput);
        name_et= (EditText) findViewById(R.id.name_et);
        pwd_et= (EditText) findViewById(R.id.pwd_et);
        forget_tv= (TextView) findViewById(R.id.forget_tv);
        regist_tv= (TextView) findViewById(R.id.regist_tv);
        toolbar.setTitle("登录");
        setSupportActionBar(toolbar);

        //toolbar中 退出图标的监听
        exits_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        regist_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(intent);
            }
        });
        qqLogin_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this,"第三方登录",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loginMethod(View view) {
        String userName = name_et.getText().toString();
        String pwd = pwd_et.getText().toString();
        Cursor cursor = MyUsersSqlite.db.rawQuery("select * from userstb where name = ? and pwd = ?", new String[]{userName, pwd});
        if (cursor!=null)
        {
            if (cursor.moveToNext())//存在此用户
            {
                ShareSPUtils.writeShareSp(true,userName,pwd);
                Toast.makeText(this,"登录成功!",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"用户名或密码错误,请重新输入",Toast.LENGTH_SHORT).show();
                name_et.setText("");
                pwd_et.setText("");
            }
        }
    }
}
