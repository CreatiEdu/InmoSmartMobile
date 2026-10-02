package com.ispc.inmosmartmobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class QuienesSomosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quienes_somos);

        // Flecha de volver del encabezado
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Botón "Contáctanos": abre la pantalla de contacto
        Button btnContactanos = findViewById(R.id.btnContactanos);
        btnContactanos.setOnClickListener(v -> {
            Intent intent = new Intent(QuienesSomosActivity.this, contact.class);
            startActivity(intent);
        });
    }
}