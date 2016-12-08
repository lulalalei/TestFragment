package com.example.administrator.day03testfragment.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.day03testfragment.R;
import com.example.administrator.day03testfragment.utils.MyUsersSqlite;
import com.example.administrator.day03testfragment.utils.ShareSPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistActivity extends AppCompatActivity {

    @BindView(R.id.toobar)
    Toolbar toolbar;
    @BindView(R.id.exits_iv)
    ImageView exits_iv;
    @BindView(R.id.nameTextInput)
    TextInputLayout nameTextInput;
    @BindView(R.id.checkPwdTextInput)
    TextInputLayout checkPwdTextInput;
    @BindView(R.id.pwdTextInput)
    TextInputLayout pwdTextInput;
    @BindView(R.id.name_et)
    EditText name_et;
    @BindView(R.id.pwd_et)
    EditText pwd_et;
    @BindView(R.id.checkPwd_et)
    EditText checkPwd_et;

    boolean userNameFinished,pwdFinished,checkPwdFinished;
   // String pwdBuff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        toolbar.setTitle("注册");
        setSupportActionBar(toolbar);

        //toolbar的返回图标
        exits_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence))
                {
                    nameTextInput.setError("用户名不能为空");
                    userNameFinished=false;
                }
                else
                {
                    nameTextInput.setError("");
                    userNameFinished=true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        pwd_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (userNameFinished)
                {
                   // pwdBuff=charSequence.toString();
                    if (TextUtils.isEmpty(charSequence))
                    {
                        pwdTextInput.setError("密码不能为空");
                        pwdFinished=false;
                    }
                    else
                    {
                        pwdTextInput.setError("");
                        pwdFinished=true;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        checkPwd_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (pwdFinished)
                {
                    if (!TextUtils.isEmpty(charSequence))
                    {
                        checkPwdTextInput.setError("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void registMethod(View view) {
        String userName = name_et.getText().toString();
        String pwd = pwd_et.getText().toString();
        String checkPwd = checkPwd_et.getText().toString();
        if (TextUtils.isEmpty(userName))
        {
            nameTextInput.setError("用户名不能为空");
        }
        else if (TextUtils.isEmpty(pwd))
        {
            pwdTextInput.setError("密码不能为空");

        }
        else if (!pwd.equals(checkPwd))
        {
            checkPwdTextInput.setError("密码输入有误,请重新输入密码");
            checkPwd_et.setText("");
        }
        else
        {
            Cursor cursor = MyUsersSqlite.db.rawQuery("select * from userstb where name = ?", new String[]{userName});
            if (cursor!=null)
            {
                if (cursor.moveToNext())//已存在用户,注册失败
                {
                    Toast.makeText(this,"该用户名已被注册,请重新输入用户名",Toast.LENGTH_SHORT).show();
                    nameTextInput.setError("用户名不能为空");
                    name_et.setText("");
                }
                else
                {
                    String usersIconPath=ShareSPUtils.writeShareSp(true,userName,pwd);
                    ContentValues values = new ContentValues();
                    values.put("name",userName);
                    values.put("pwd",pwd);
                    //在此设置头像图片为默认
                    values.put("usericon", usersIconPath);
                    long flag = MyUsersSqlite.db.insert("userstb", null, values);
                    values.clear();
                    Toast.makeText(this,"插入新用户成功:"+flag,Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}
