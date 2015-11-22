package com.dalong.imageviewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageCycleView ad_view,ad_view2;

    private ArrayList<String> urls=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        urls.add("http://s.xnimg.cn/rrinvest/wap/events/2015/recruitment/img/banner.jpg");
        urls.add("http://a.xnimg.cn/rrinvest/img/bannerwap/2015-11-11.jpg");
        urls.add("http://s.xnimg.cn/rrinvest/wap/events/2015/fall/img/appbanner.png");
        urls.add("http://fmn.rrfmn.com/fmn074/20150413/1720/original_WyLG_60c800005a561e83.jpg");
        ad_view=(ImageCycleView)findViewById(R.id.ad_view);
        ad_view2=(ImageCycleView)findViewById(R.id.ad_view2);
        //处理适配 设置比例
        ViewGroup.LayoutParams params = ad_view.getLayoutParams();
        ViewGroup.LayoutParams params2 = ad_view2.getLayoutParams();
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        params.height = (int) (dm.widthPixels * 0.421875);
        params2.height = (int) (dm.widthPixels * 0.421875);

        ad_view.setLayoutParams(params);
        ad_view2.setLayoutParams(params2);
        ad_view.setImageResources(urls, new ImageCycleView.ImageCycleViewListener() {
            @Override
            public void onImageClick(int position, View imageView) {
                Toast.makeText(MainActivity.this,""+position,Toast.LENGTH_LONG).show();
            }
        },0);
        ad_view2.setImageResources(urls, new ImageCycleView.ImageCycleViewListener() {
            @Override
            public void onImageClick(int position, View imageView) {
                Toast.makeText(MainActivity.this,""+position,Toast.LENGTH_LONG).show();
            }
        },1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ad_view.startImageCycle();
        ad_view2.startImageCycle();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ad_view.stopImageCycle();
        ad_view2.stopImageCycle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
