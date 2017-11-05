package com.xiaofan.www.weather;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaofan.www.weather.common.Presenter;
import com.xiaofan.www.weather.common.RxBus;
import com.xiaofan.www.weather.fragment.ProvinceFragment;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {
    public Toolbar toolbar;
    public TextView title;
    public RxBus rxBus;
    public CompositeSubscription subscription;
    public Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init(){
        toolbar=(Toolbar)findViewById(R.id.main_toolbar);
        title=(TextView)findViewById(R.id.text_toolbar);

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
}
