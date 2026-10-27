package com.ispc.inmosmartmobile;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

    // --- Pantalla de carga ---
    private FrameLayout loadingOverlay;
    private ImageView loadingImage;
    private ObjectAnimator spinner;

    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario = findViewById(R.id.etUsuario); // Se usará como email
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // --- Pantalla de carga ---
        loadingOverlay = findViewById(R.id.loadingOverlay);
        loadingImage = findViewById(R.id.loadingImage);

        spinner = ObjectAnimator.ofFloat(loadingImage, View.ROTATION, 0f, 360f);
        spinner.setDuration(1000);
        spinner.setRepeatCount(ObjectAnimator.INFINITE);
        spinner.setInterpolator(new LinearInterpolator());

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

    // ---------- Pantalla de carga ----------

    private void showLoading() {
        loadingOverlay.setAlpha(0f);
        loadingOverlay.setVisibility(View.VISIBLE);
        loadingOverlay.animate().alpha(1f).setDuration(200).start();
        spinner.start();
    }

    private void hideLoading() {
        loadingOverlay.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        loadingOverlay.setVisibility(View.GONE);
                        spinner.cancel();
                    }
                })
                .start();
    }

    @Override
    public void onBackPressed() {
        // Ignora el botón atrás mientras se está cargando
        if (loadingOverlay.getVisibility() == View.VISIBLE) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (spinner != null) spinner.cancel();
    }

    // ---------- Ojito de la contraseña ----------

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

    // ---------- Login ----------

    private void realizarLogin(String email, String password) {
        showLoading(); // <-- NUEVO: muestra la pantalla de carga

        ApiService apiService = ApiClient.getApiService();
        LoginRequest loginRequest = new LoginRequest(email, password);

        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                hideLoading(); // <-- NUEVO: oculta la carga al recibir respuesta

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    // Guardar el token para usarlo en el perfil
                    TokenStorage.saveSession(LoginActivity.this, loginResponse.getToken(), loginResponse.getId());

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
                hideLoading(); // <-- NUEVO: oculta la carga también si falla

                Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}