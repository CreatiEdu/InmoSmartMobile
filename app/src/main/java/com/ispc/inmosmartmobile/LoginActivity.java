package com.ispc.inmosmartmobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsuario; // Representa el email en el backend
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;

    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario = findViewById(R.id.etUsuario); // Se usará como email
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Ojito para mostrar/ocultar la contraseña
        configurarOjoPassword();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etUsuario.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    realizarLogin(email, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void configurarOjoPassword() {
        etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Drawable iconoFinal = etPassword.getCompoundDrawablesRelative()[2];
                    if (iconoFinal != null) {
                        int limite = etPassword.getWidth()
                                - etPassword.getPaddingEnd()
                                - iconoFinal.getBounds().width()
                                - etPassword.getCompoundDrawablePadding();

                        if (event.getX() >= limite) {
                            if (passwordVisible) {
                                // Ocultar contraseña
                                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_visibility, 0);
                            } else {
                                // Mostrar contraseña
                                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                etPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);
                            }
                            passwordVisible = !passwordVisible;
                            etPassword.setSelection(etPassword.getText().length()); // cursor al final
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    private void realizarLogin(String email, String password) {
        ApiService apiService = ApiClient.getApiService();
        LoginRequest loginRequest = new LoginRequest(email, password);

        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("Extra_USUARIO", loginResponse.getNombre());
                    intent.putExtra("MOSTRAR_BIENVENIDA", true);
                    startActivity(intent);
                    finish();

                } else {
                    String cuerpo = "";
                    try {
                        if (response.errorBody() != null) cuerpo = response.errorBody().string();
                    } catch (Exception ignored) {}

                    android.util.Log.e("LOGIN", "HTTP " + response.code() + " -> " + cuerpo);

                    String mensaje = "Error del servidor (" + response.code() + ")";
                    try {
                        ErrorResponse err = new Gson().fromJson(cuerpo, ErrorResponse.class);
                        if (err != null && err.getError() != null) mensaje = err.getError();
                    } catch (Exception ignored) {}

                    Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
