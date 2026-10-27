package com.ispc.inmosmartmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilActivity extends AppCompatActivity {

    private EditText etUsuario;
    private EditText etDni;
    private EditText etEmail;
    private EditText etTelefono;
    private AppCompatButton btnGuardar;
    private AppCompatButton btnCancelar;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        // Enlazar componentes
        etUsuario = findViewById(R.id.etUsuario);
        etDni = findViewById(R.id.etDni);
        etEmail = findViewById(R.id.etEmail);
        etTelefono = findViewById(R.id.etTelefono);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        token =TokenStorage.getToken(this);

        if (token == null) {
            irALogin();
            return;
        }

        // Hasta que carguen los datos no se puede guardar
        btnGuardar.setEnabled(false);
        cargarDatosActuales();

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambios();
            }
        });

        // Cancelar: cierra sin guardar
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void cargarDatosActuales() {
        ApiClient.getApiService().getPerfil("Bearer " + token).enqueue(new Callback<PerfilResponse>() {
            @Override
            public void onResponse(Call<PerfilResponse> call, Response<PerfilResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PerfilResponse perfil = response.body();
                    etUsuario.setText(perfil.getNombre());
                    etDni.setText(perfil.getDni());
                    etEmail.setText(perfil.getEmail());
                    etTelefono.setText(perfil.getTelefono());
                    btnGuardar.setEnabled(true);

                } else if (response.code() == 401) {
                    Toast.makeText(EditarPerfilActivity.this,
                            "La sesión expiró, iniciá sesión de nuevo", Toast.LENGTH_LONG).show();
                    irALogin();

                } else {
                    Toast.makeText(EditarPerfilActivity.this,
                            "No se pudieron cargar los datos", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PerfilResponse> call, Throwable t) {
                Toast.makeText(EditarPerfilActivity.this,
                        "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void guardarCambios() {
        String usuario = etUsuario.getText().toString().trim();
        String dni = etDni.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();

        // Validaciones
        if (usuario.isEmpty() || dni.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Por favor complete usuario, DNI y correo", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dni.length() < 7 || dni.length() > 9) {
            Toast.makeText(this, "El DNI debe tener entre 7 y 9 dígitos", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "El correo electrónico no es válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // El teléfono es opcional: solo se valida si lo completó
        if (!telefono.isEmpty()) {
            String soloNumeros = telefono.replaceAll("[^0-9]", "");
            if (soloNumeros.length() < 8 || soloNumeros.length() > 15) {
                Toast.makeText(this, "El teléfono debe tener entre 8 y 15 números", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Evita tocar "Guardar" dos veces mientras se envía
        btnGuardar.setEnabled(false);

        PerfilRequest request = new PerfilRequest(usuario, email, dni, telefono);

        ApiClient.getApiService().actualizarPerfil("Bearer " + token, request)
                .enqueue(new Callback<PerfilResponse>() {
                    @Override
                    public void onResponse(Call<PerfilResponse> call, Response<PerfilResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(EditarPerfilActivity.this,
                                    "Perfil actualizado", Toast.LENGTH_SHORT).show();
                            finish(); // vuelve al perfil, que se recarga solo

                        } else if (response.code() == 401) {
                            Toast.makeText(EditarPerfilActivity.this,
                                    "La sesión expiró, iniciá sesión de nuevo", Toast.LENGTH_LONG).show();
                            irALogin();

                        } else {
                            btnGuardar.setEnabled(true);
                            Toast.makeText(EditarPerfilActivity.this,
                                    mensajeDeError(response), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PerfilResponse> call, Throwable t) {
                        btnGuardar.setEnabled(true);
                        Toast.makeText(EditarPerfilActivity.this,
                                "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Lee el mensaje {"error": "..."} que manda el backend
    private String mensajeDeError(Response<?> response) {
        String mensaje = "Error del servidor (" + response.code() + ")";
        try {
            if (response.errorBody() != null) {
                String cuerpo = response.errorBody().string();
                ErrorResponse err = new Gson().fromJson(cuerpo, ErrorResponse.class);
                if (err != null && err.getError() != null) mensaje = err.getError();
            }
        } catch (Exception ignored) {}
        return mensaje;
    }

    private void irALogin() {
        TokenStorage.clearSession(this);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
