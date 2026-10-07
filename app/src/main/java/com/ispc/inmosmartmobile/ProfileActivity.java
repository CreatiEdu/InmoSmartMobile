package com.ispc.inmosmartmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUsuario;
    private TextView tvDni;
    private TextView tvEmail;
    private TextView tvTelefono;
    private AppCompatButton btnEditarPerfil;
    private AppCompatButton btnVolver;

    private String usuarioIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Enlazar componentes
        tvUsuario = findViewById(R.id.tvUsuario);
        tvDni = findViewById(R.id.tvDni);
        tvEmail = findViewById(R.id.tvEmail);
        tvTelefono = findViewById(R.id.tvTelefono);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnVolver = findViewById(R.id.btnVolver);

        // Usuario que viene desde MainActivity
        usuarioIntent = getIntent().getStringExtra("Extra_USUARIO");
        if (usuarioIntent == null) {
            usuarioIntent = "Usuario";
        }

        // Ir a editar perfil
        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditarPerfilActivity.class);
                intent.putExtra("Extra_USUARIO", usuarioIntent);
                startActivity(intent);
            }
        });

        // Volver a la pantalla anterior
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Se ejecuta cada vez que la pantalla se muestra (incluso al volver de editar)
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("perfil_prefs", MODE_PRIVATE);
        tvUsuario.setText(prefs.getString("usuario", usuarioIntent));
        tvDni.setText(prefs.getString("dni", "No disponible"));
        tvEmail.setText(prefs.getString("email", "No disponible"));

        String telefono = prefs.getString("telefono", "");
        tvTelefono.setText(telefono.isEmpty() ? "No agregado" : telefono);
    }
}