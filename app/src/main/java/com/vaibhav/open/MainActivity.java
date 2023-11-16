package com.vaibhav.open;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.vaibhav.open.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    public Context mContext;
    DrAdb adb = new DrAdb(this);
    public String[] packages = null;
    public String unsuspendcmd = "";
    ActivityMainBinding binding;
    private Button engine;

    public static void sleep(long milisec) throws InterruptedException {//debug purpose only
        Thread.sleep(milisec);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mContext = this;
        PackageInfo package[] =getApplicationContext().getPackageManager().getInstalledPackages();
        for (PackageInfo x : this.getPackageManager().getInstalledPackages(PackageInfo.INSTALL_LOCATION_AUTO)) {
            x.
        }


        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .permitAll()
                .detectDiskWrites()
                .penaltyLog()
                .build()); //allows network operations on mainThread, without this it would give networkOnMainThreadException


    }


}