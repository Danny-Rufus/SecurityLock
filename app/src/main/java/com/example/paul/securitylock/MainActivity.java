package com.example.paul.securitylock;

import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int LOCK_REQUEST_CODE = 221;
    private static final int SECURITY_SETTING_REQUEST_CODE = 223;

    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text_view);
        authenticateApp();


    }

    private void authenticateApp() {

    //GET INSTANCE OF KEYGUARD MANGER
        KeyguardManager keyguardManager = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);

        //check if the device version is greater of equal to lollipop(5.0)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            //Create an intent to open device screen lock screen to authenticate
            //Pass the Screen Lock screen Title and Description
            Intent i= keyguardManager.createConfirmDeviceCredentialIntent(getResources().getString(R.string.unlock),getResources().getString(R.string.confirm_Pattern));
            try {
            startActivityForResult(i,LOCK_REQUEST_CODE);
            }catch (Exception e){

                //If some exception occurs means Screen lock is not set up please set screen lock
                //Open Security screen directly to enable patter lock
                Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                try{
                    startActivityForResult(intent, SECURITY_SETTING_REQUEST_CODE);
                }catch(Exception ex){
                    textView.setText(getResources().getString(R.string.setting_label));

                }

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case LOCK_REQUEST_CODE:
                if(resultCode ==RESULT_OK){
                    textView.setText(getResources().getString(R.string.unlock_success));
                }else{
                    textView.setText(getResources().getString(R.string.unlock_failed));
                }
                break;
            case SECURITY_SETTING_REQUEST_CODE:
                if(isDeviceSecure()){
                    Toast.makeText(this, getResources().getString(R.string.device_is_secure), Toast.LENGTH_SHORT).show();
                    authenticateApp();
                }else{
                    textView.setText(getResources().getString(R.string.security_device_cancelled));
                }
                break;
        }
    }
    private boolean isDeviceSecure(){
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        return keyguardManager.isKeyguardSecure();

    }
    public void authenticate_again(View view) {
        authenticateApp();
    }
}
