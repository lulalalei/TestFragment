package com.example.administrator.day03testfragment.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.day03testfragment.ContentFragment;
import com.example.administrator.day03testfragment.MyAdapter;
import com.example.administrator.day03testfragment.R;
import com.example.administrator.day03testfragment.cans.Cans;
import com.example.administrator.day03testfragment.utils.ShareSPUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    ViewPager viewpager;
    TabLayout tablayout;
    DrawerLayout drawerlayout;
    NavigationView navigation;
    Toolbar toobar;
    CircleImageView toolbar_civ;

    String[] titles = new String[]{
            "精选", "分类", "必玩", "游戏", "绑定"
    };
    ArrayList<Fragment> fragmentList=new ArrayList<>();
    MyAdapter myAdapter;

    private static final float MIN_SCALE=0.85f;
    private static final float MIN_ALPHA=0.5f;

    //策划菜单头布局中的控件
    CircleImageView userhead_civ;
    TextView userinfo_tv;

    //自定义弹出框,用于更换头像
    View changeUserIconView;
    TextView camera_tv;
    TextView mapstorage_tv;

    String picPath;

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.arg1)
            {
                case 100:
                    ShareSPUtils.edit.putString("userIcon", picPath);
                    ShareSPUtils.edit.commit();
                    if (userhead_civ!=null&&toolbar_civ!=null)
                    {
                        ShareSPUtils.readShareSP(userinfo_tv,userhead_civ,toolbar_civ,MainActivity.this);
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewpager= (ViewPager) findViewById(R.id.viewpager);
        tablayout= (TabLayout) findViewById(R.id.tablayout);
        drawerlayout= (DrawerLayout) findViewById(R.id.drawerlayout);
        navigation= (NavigationView) findViewById(R.id.navigation);
        toolbar_civ= (CircleImageView) findViewById(R.id.toolbar_civ);
        toobar= (Toolbar) findViewById(R.id.toobar);
        toobar.setTitle("");
        setSupportActionBar(toobar);

        //实例化策划菜单头布局的控件
        View navigationHeaderView = navigation.getHeaderView(0);
        userhead_civ = (CircleImageView) navigationHeaderView.findViewById(R.id.userhead_civ);
        userinfo_tv= (TextView) navigationHeaderView.findViewById(R.id.userinfo_tv);

        ShareSPUtils.readShareSP(userinfo_tv,userhead_civ,toolbar_civ,this);
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tablayout.setTabTextColors(Color.BLACK,Color.GRAY);
        //tablayout.setSelectedTabIndicatorHeight(30);
        for (int i = 0; i < titles.length; i++) {
            tablayout.addTab(tablayout.newTab().setText(titles[i]));
            ContentFragment contentFragment = new ContentFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position",i);
            contentFragment.setArguments(bundle);
            fragmentList.add(contentFragment);
        }

        myAdapter = new MyAdapter(getSupportFragmentManager(), fragmentList, titles);
        viewpager.setAdapter(myAdapter);
        turnViewpager(viewpager);

        //对Toolbar中头像的监听
        toolbar_civ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!drawerlayout.isDrawerOpen(Gravity.LEFT))
                {
                    drawerlayout.openDrawer(Gravity.LEFT);
                }
            }
        });

        //对侧滑菜单栏头布局中头像的监听
        userhead_civ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Cans.hasLogined)//此时跳转至登录页面
                {
                    hasNotLoginedMethod(drawerlayout);
                }
                else //此时可以更换头像
                {
                    //实例化自定义弹出框布局,用于更换头像
                    changeUserIconView = View.inflate(MainActivity.this, R.layout.change_usericon, null);
                    camera_tv= (TextView) changeUserIconView.findViewById(R.id.camera_tv);
                    mapstorage_tv= (TextView) changeUserIconView.findViewById(R.id.mapstorage_tv);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("请选择:");
                    builder.setView(changeUserIconView);
                    final AlertDialog dialog = builder.create();

                    dialog.show();
                    camera_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(MainActivity.this,"拍照",Toast.LENGTH_SHORT).show();

                            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                            {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                //以下为了获得原图
                                String cameraPath=Cans.getDefaultUsersIconFile();
                                File file = new File(cameraPath);
                                picPath=new File(file,System.currentTimeMillis()+".jpg").getAbsolutePath();
                                Uri uri = Uri.fromFile(new File(picPath));
                                //为拍摄的图片指定一个存储的路径
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                //intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
                                startActivityForResult(intent,101);
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,"请检查您的sdk",Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    });
                    mapstorage_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(MainActivity.this,"系统图库",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_PICK);
                            startActivityForResult(intent,102);
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        //对侧滑菜单栏菜单条目的监听
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.setting:
                        //Toast.makeText(MainActivity.this,"setting",Toast.LENGTH_SHORT).show();
                        item.setChecked(true);
                        break;
                    case R.id.collection:
                        item.setChecked(true);
                        break;
                    case R.id.track:
                        item.setChecked(true);
                        break;
                    case R.id.exits:
                        item.setChecked(true);
                        ShareSPUtils.resetShareSP();
                        ShareSPUtils.readShareSP(userinfo_tv,userhead_civ,toolbar_civ,MainActivity.this);
                        break;
                    default:
                        break;
                }
                drawerlayout.closeDrawer(Gravity.LEFT);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("==============","=======onActivityResult=========");
        if (requestCode==101)//拍照
        {
           /* Bundle bundle = data.getExtras();
            Bitmap bitmap11 = (Bitmap) data.getExtras().get("data");*/
            if (resultCode == RESULT_OK) {
                FileInputStream fis = null;
                FileOutputStream fos = null;
                //把图片转化为字节流
                try {
                    fos = new FileOutputStream(picPath);
                    fis = new FileInputStream(picPath);
                   /* Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,80,fos);
                    fos.close();*/
                   /* BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(picPath, options);
                    options.inSampleSize = 3;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    options.inJustDecodeBounds = false;
                    Bitmap bitmap = BitmapFactory.decodeFile(picPath, options);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);

                    ShareSPUtils.edit.putString("userIcon", picPath);
                    ShareSPUtils.edit.commit();*/
                    //Toast.makeText(this, "lalalalal", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (requestCode==102)//图库
            {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Uri uri = data.getData();
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                String mapPath = Cans.getDefaultUsersIconFile();
                                File file = new File(mapPath);
                                mapPath = new File(file, System.currentTimeMillis() + ".jpg").getAbsolutePath();
                                FileOutputStream fos = null;
                                BufferedOutputStream bos = null;
                                try {
                                    fos = new FileOutputStream(mapPath);
                                    bos = new BufferedOutputStream(fos);
                                    //compressBitmap(mapPath, fos);
                                bitmap.compress(Bitmap.CompressFormat.JPEG,80,bos);
                                bos.close();

                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                    options.inJustDecodeBounds=true;
                                    BitmapFactory.decodeFile(mapPath,options);
                                    options.inSampleSize=3;
                                    options.inPreferredConfig= Bitmap.Config.RGB_565;
                                    options.inJustDecodeBounds=false;
                                    Bitmap bitmap1 = BitmapFactory.decodeFile(mapPath, options);
                                    bitmap1.compress(Bitmap.CompressFormat.JPEG,80,fos);
                                    ShareSPUtils.edit.putString("userIcon", mapPath);
                                    ShareSPUtils.edit.commit();
                              /*  ShareSPUtils.edit.putString("userIcon", mapPath);
                                ShareSPUtils.edit.commit();
                                ShareSPUtils.readShareSP(userinfo_tv,userhead_civ,toolbar_civ,this);*/
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "请检查您的sdk", Toast.LENGTH_SHORT).show();
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

    }

    /*
        * 未登录时点击侧滑菜单头像时执行的方法*/
    public void hasNotLoginedMethod(DrawerLayout drawerlayout)
    {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        if (!drawerlayout.isDrawerOpen(Gravity.LEFT))
        {
            drawerlayout.openDrawer(Gravity.LEFT);
        }
    }
    /*
        * 頁卡切換时的动画*/
    public void turnViewpager(ViewPager viewpager)
    {

        viewpager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                Log.d("=========","======transformPage====="+"transformPage");
                int width = view.getWidth();
                int height = view.getHeight();
                if (position<-1)
                {
                    //移出屏幕时,隐藏该view
                    view.setAlpha(0);
                }
                else if (position<=1)
                {
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float horMargin = width * (1 - scaleFactor) / 2;
                    float verMargin = height * (1 - scaleFactor) / 2;
                    if (position<0)
                    {
                        view.setTranslationX(horMargin-verMargin/2);
                    }
                    else
                    {
                        view.setTranslationX(-horMargin+verMargin/2);
                    }
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);
                    view.setAlpha(MIN_ALPHA+(scaleFactor-MIN_SCALE)/(1-MIN_ALPHA)*(1+MIN_SCALE));
                }
                else
                {
                    view.setAlpha(0);
                }
            }
        });
    }

    /*
    * 二次采样图片,并写入文件*/
    public void compressBitmap(final String string,final FileOutputStream fos)
    {
        //final Bitmap bitmap;
        new Thread(new Runnable() {
            @Override
            public void run() {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds=true;
                BitmapFactory.decodeFile(string,options);
                options.inSampleSize=3;
                options.inPreferredConfig= Bitmap.Config.RGB_565;
                options.inJustDecodeBounds=false;
                Bitmap bitmap = BitmapFactory.decodeFile(string, options);
                bitmap.compress(Bitmap.CompressFormat.JPEG,80,fos);
                try {
                    fos.close();
                    Message message = Message.obtain();
                    message.arg1=100;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("==============","=======onResume=========");
        ShareSPUtils.readShareSP(userinfo_tv,userhead_civ,toolbar_civ,this);
    }
}
