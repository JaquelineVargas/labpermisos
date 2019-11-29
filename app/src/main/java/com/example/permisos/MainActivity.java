package com.example.permisos;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermison();
    }
    public Boolean checkPermission(String permission) {
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermison(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CALL_PHONE},CODE_PERMISSION);
        }else{
            if(checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)&&
                    checkPermission(Manifest.permission.CALL_PHONE)){
                Toast.makeText(this,"los permisos fueron otorgados",Toast.LENGTH_LONG).show();
            }
        }
    }
    private final int CODE_PERMISSION = 1000;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        if (requestCode == CODE_PERMISSION){
            if (grantResults.length > 0){
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"permiso de almacenamiento otorgado",
                            Toast.LENGTH_SHORT).show();
                    initCall();
                }else{
                    //funcionalidad desactivada
                }
                if (grantResults[1]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Permiso de almacenamiento de llamadas otorgada",
                            Toast.LENGTH_SHORT).show();
                    initStore();
                }else{
                    //funcionalidad desactivada
                }
            }
        }
    }
    private void initCall(){
        ImageButton btn =findViewById(R.id.btncall);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btncall:{
                callPhone();
                break;
            }
            case R.id.btnstore:{
                storeData();
                break;
            }
        }
    }

    public void callPhone(){
        if (checkPermission(Manifest.permission.CALL_PHONE)) {
            TextView phone = findViewById(R.id.call);
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.getText()));
            startActivity(intent);
        }
    }

    private void initStore(){
        ImageButton btn =findViewById(R.id.btnstore);
        btn.setOnClickListener(this);
    }

    public void storeData(){
        TextView data =findViewById(R.id.content);
        try{
            FileOutputStream files = openFileOutput("android.txt",Activity.MODE_PRIVATE);
            OutputStreamWriter write = new OutputStreamWriter(files);
            write.write(data.getText().toString());
            write.flush();
            write.close();
            //OutputStreamWriter file - new OutputStreamWriter(openFileOutput());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

