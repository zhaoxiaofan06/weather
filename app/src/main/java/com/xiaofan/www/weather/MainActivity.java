package com.xiaofan.www.weather;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.xiaofan.www.weather.common.Presenter;
import com.xiaofan.www.weather.common.RxBus;
import com.xiaofan.www.weather.fragment.ProvinceFragment;
;
import java.util.Timer;

import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {
    public Toolbar toolbar;
    public TextView title;
    public RxBus rxBus;
    public CompositeSubscription subscription;
    public Presenter presenter;
    public Timer timer;
    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    public Menu menu;
    public Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init(){
        toolbar=(Toolbar)findViewById(R.id.main_toolbar);
        menu=toolbar.getMenu();
        getMenuInflater().inflate(R.menu.toolbar,menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id=item.getItemId();
                return true;
            }
        });

        title=(TextView)findViewById(R.id.text_toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView=(NavigationView)findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.nav_call:
                        if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.CALL_PHONE},1);
                        }else{
                            try{
                                Intent intent=new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:10086"));
                                startActivity(intent);
                            }catch (SecurityException e){
                                e.printStackTrace();
                            }
                        }
                        break;
                    case R.id.nav_camera:
                        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                        startActivity(intent);
                        break;
                    case R.id.nav_location:
                        Toast.makeText(MainActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_scan:
                        //Intent intent1=new Intent(MainActivity.this, CaptureActivity.class);
                        //startActivityForResult(intent1,SCAN_CODE);
                        IntentIntegrator intentIntegrator=new IntentIntegrator(MainActivity.this);
                        intentIntegrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
                        break;
                    case R.id.nav_task:
                        Toast.makeText(MainActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        rxBus=RxBus.getDefault();
        presenter=new Presenter(this);
        replaceFragmnet(new ProvinceFragment());
    }

    @Override
    protected void onResume(){
        super.onResume();
        presenter.setSubscription();
        subscription=new CompositeSubscription();
        subscription.add(presenter.getSubscription());
    }

    @Override
    protected void onPause() {
        super.onPause();
        subscription.unsubscribe();
        subscription.clear();
        timer.cancel();
    }

    public void replaceFragmnet(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_id,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onBackPressed(){
        int count=getSupportFragmentManager().getBackStackEntryCount();
        if(count > 0){
            Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.fragment_id);
            if(fragment instanceof ProvinceFragment){
                presenter.appExit();
            }else{
                super.onBackPressed();
            }
        }else{
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(MainActivity.this,"退出成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult != null) {
            if(intentResult.getContents() == null) {
                Toast.makeText(this,"内容为空",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,intentResult.getContents(),Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
}
