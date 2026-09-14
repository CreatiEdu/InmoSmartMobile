package com.ispc.inmosmartmobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        //temporizador para cambiar de pantalla automaticamente
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run(){
                //Crear el Intent explicito hacia LoginActivity
                Intent intent =new Intent(
                        SplashActivity.this,
                        LoginActivity.class
                );

                startActivity(intent);
                //Cerrar SplashActivity para que el usuario no vuelva a ella con el botón atras
                finish();
            }

        }, 2500); // Tiempo en milisegundos
    }
}