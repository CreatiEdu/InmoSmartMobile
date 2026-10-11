package com.ispc.inmosmartmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUsuario;
    private TextView tvDni;
    private TextView tvEmail;
    private TextView tvTelefono;
    private AppCompatButton btnEditarPerfil;
    private AppCompatButton btnVolver;

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

        // Ir a editar perfil
        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EditarPerfilActivity.class));
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
        cargarPerfil();
    }

    private void cargarPerfil() {
        SharedPreferences prefs = getSharedPreferences("sesion", MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token == null) {
            irALogin();
            return;
        }

        tvUsuario.setText("Cargando...");
        tvDni.setText("Cargando...");
        tvEmail.setText("Cargando...");
        tvTelefono.setText("Cargando...");

        ApiClient.getApiService().getPerfil("Bearer " + token).enqueue(new Callback<PerfilResponse>() {
            @Override
            public void onResponse(Call<PerfilResponse> call, Response<PerfilResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PerfilResponse perfil = response.body();
                    tvUsuario.setText(perfil.getNombre());
                    tvDni.setText(perfil.getDni());
                    tvEmail.setText(perfil.getEmail());

                    String telefono = perfil.getTelefono();
                    if (telefono == null || telefono.isEmpty()) {
                        tvTelefono.setText("No agregado");
                    } else {
                        tvTelefono.setText(telefono);
                    }

                } else if (response.code() == 401) {
                    Toast.makeText(ProfileActivity.this,
                            "La sesión expiró, iniciá sesión de nuevo", Toast.LENGTH_LONG).show();
                    irALogin();

                } else {
                    Toast.makeText(ProfileActivity.this,
                            "No se pudo cargar el perfil (" + response.code() + ")", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PerfilResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this,
                        "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void irALogin() {
        getSharedPreferences("sesion", MODE_PRIVATE).edit().remove("token").apply();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
