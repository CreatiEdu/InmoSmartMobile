package com.ispc.inmosmartmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {


        // Declaramos los componentes
        private EditText boxUsuario;
        private EditText boxDni;
        private EditText boxEmail;
        private EditText boxPassword;
        private EditText boxPasswordAgain;
        private Button btnLogin;
        private Button btnRegister;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);

            // 1. Enlazar componentes Java con su id XML
            boxUsuario = findViewById(R.id.boxUsuario);
            boxDni = findViewById(R.id.boxDni);
            boxEmail = findViewById(R.id.boxEmail);
            boxPassword = findViewById(R.id.boxPassword);
            boxPasswordAgain = findViewById(R.id.boxPasswordAgain);

            btnLogin = findViewById(R.id.btnLogin);
            btnRegister = findViewById(R.id.btnRegister);

            // Escuchar el evento de click del botón
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String usuario = boxUsuario.getText().toString().trim();
                    String dni = boxDni.getText().toString().trim();
                    String email = boxEmail.getText().toString().trim();
                    String password = boxPassword.getText().toString().trim();
                    String passwordAgain = boxPasswordAgain.getText().toString().trim();

                    // Validar que los campos no estén vacíos
                    if (!usuario.isEmpty() && !password.isEmpty() && !dni.isEmpty() && !email.isEmpty() && !passwordAgain.isEmpty()) {

                        if (!password.equals(passwordAgain)){
                            Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        ejecutarRegistro(usuario, dni, email, password );

                    } else {
                        // Mostrar mensaje de advertencia
                        Toast.makeText(
                                com.ispc.inmosmartmobile.RegisterActivity.this,
                                "Por favor complete todos los campos",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            });
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Crear Intent para ir a RegisterActivity
                    Intent intent = new Intent(com.ispc.inmosmartmobile.RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    /* Cierra la pantalla de Login al ingresar */
                    finish();
                }
            });
        }

        private void ejecutarRegistro(String usuario, String dni, String email, String password){
            ApiService apiService = ApiClient.getApiService();
            RegisterRequest registerRequest = new RegisterRequest(usuario, dni, email, password);

            Call<RegisterResponse> call = apiService.register(registerRequest);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.isSuccessful() && response.body() != null){
                        Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.putExtra("Extra_USUARIO", usuario);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Error al registrar usuario:" + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
