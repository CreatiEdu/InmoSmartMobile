package com.ispc.inmosmartmobile;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

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
    private TextView reqLongitud, reqMinuscula, reqMayuscula, reqNumero, reqSimbolo, reqCoinciden;

    // --- Pantalla de carga ---
    private FrameLayout loadingOverlay;
    private ImageView loadingImage;
    private ObjectAnimator spinner;

    // --- Control de visibilidad de contraseñas ---
    private boolean passwordVisible = false;
    private boolean passwordAgainVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loadingOverlay = findViewById(R.id.loadingOverlay);
        loadingImage = findViewById(R.id.loadingImage);

        spinner = ObjectAnimator.ofFloat(loadingImage, View.ROTATION, 0f, 360f);
        spinner.setDuration(1000);
        spinner.setRepeatCount(ObjectAnimator.INFINITE);
        spinner.setInterpolator(new LinearInterpolator());

        // 1. Enlazar componentes Java con su id XML
        boxUsuario = findViewById(R.id.boxUsuario);
        boxDni = findViewById(R.id.boxDni);
        boxEmail = findViewById(R.id.boxEmail);
        boxPassword = findViewById(R.id.boxPassword);
        boxPasswordAgain = findViewById(R.id.boxPasswordAgain);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        reqLongitud   = findViewById(R.id.reqLongitud);
        reqMinuscula  = findViewById(R.id.reqMinuscula);
        reqMayuscula  = findViewById(R.id.reqMayuscula);
        reqNumero     = findViewById(R.id.reqNumero);
        reqSimbolo    = findViewById(R.id.reqSimbolo);
        reqCoinciden  = findViewById(R.id.reqCoinciden);

        //  ojitos para mostrar/ocultar contraseñas
        configurarOjoPassword(boxPassword, true);
        configurarOjoPassword(boxPasswordAgain, false);

        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override public void afterTextChanged(Editable s) {
                actualizarRequisitos();
            }
        };
        boxPassword.addTextChangedListener(watcher);
        boxPasswordAgain.addTextChangedListener(watcher);

        // Estado inicial: todo en rojo y botón deshabilitado
        actualizarRequisitos();

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

                    if (!password.equals(passwordAgain)) {
                        Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ejecutarRegistro(usuario, dni, email, password);

                } else {
                    // Mostrar mensaje de advertencia
                    Toast.makeText(
                            RegisterActivity.this,
                            "Por favor complete todos los campos",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ir a la pantalla de Login
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // ---------- Pantalla de carga ----------

    private void showLoading() {
        if (loadingOverlay != null) {
            loadingOverlay.animate().cancel();
            loadingOverlay.setAlpha(0f);
            loadingOverlay.setVisibility(View.VISIBLE);
            loadingOverlay.animate().alpha(1f).setDuration(200).start();
        }
        if (spinner != null && !spinner.isStarted()) spinner.start();
    }

    private void hideLoading() {
        if (loadingOverlay == null || loadingOverlay.getVisibility() != View.VISIBLE) return;
        loadingOverlay.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        loadingOverlay.setVisibility(View.GONE);
                        if (spinner != null) spinner.cancel();
                    }
                })
                .start();
    }

    @Override
    public void onBackPressed() {
        if (loadingOverlay != null && loadingOverlay.getVisibility() == View.VISIBLE) return;
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (spinner != null) spinner.cancel();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void configurarOjoPassword(final EditText editText, final boolean isFirstField) {
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Drawable iconoFinal = editText.getCompoundDrawablesRelative()[2];
                    if (iconoFinal != null) {
                        int limite = editText.getWidth()
                                - editText.getPaddingEnd()
                                - iconoFinal.getBounds().width()
                                - editText.getCompoundDrawablePadding();

                        if (event.getX() >= limite) {
                            boolean currentVisible = isFirstField ? passwordVisible : passwordAgainVisible;

                            if (currentVisible) {
                                // Ocultar contraseña
                                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                editText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_visibility, 0);
                            } else {
                                // Mostrar contraseña
                                editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                editText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);
                            }

                            if (isFirstField) {
                                passwordVisible = !passwordVisible;
                            } else {
                                passwordAgainVisible = !passwordAgainVisible;
                            }

                            editText.setSelection(editText.getText().length()); // cursor al final
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    private void actualizarRequisitos() {
        String pass = boxPassword.getText().toString().trim();
        String again = boxPasswordAgain.getText().toString().trim();

        boolean longitud = pass.length() >= 12;
        boolean minuscula = false, mayuscula = false, numero = false, simbolo = false;

        for (int i = 0; i < pass.length(); i++) {
            char c = pass.charAt(i);
            if (Character.isLowerCase(c)) minuscula = true;
            else if (Character.isUpperCase(c)) mayuscula = true;
            else if (Character.isDigit(c)) numero = true;
            else if (!Character.isWhitespace(c)) simbolo = true;
        }

        boolean coinciden = !pass.isEmpty() && pass.equals(again);

        setRequisito(reqLongitud,  longitud,  "Mínimo 12 caracteres");
        setRequisito(reqMinuscula, minuscula, "Al menos 1 letra minúscula");
        setRequisito(reqMayuscula, mayuscula, "Al menos 1 letra mayúscula");
        setRequisito(reqNumero,    numero,    "Al menos 1 número");
        setRequisito(reqSimbolo,   simbolo,   "Al menos 1 símbolo (ej: ! @ # $ %)");
        setRequisito(reqCoinciden, coinciden, "Las contraseñas coinciden");

        boolean todoOk = longitud && minuscula && mayuscula && numero && simbolo && coinciden;
        btnRegister.setEnabled(todoOk);
        btnRegister.setAlpha(todoOk ? 1f : 0.5f);
    }

    private void setRequisito(TextView tv, boolean cumple, String texto) {
        tv.setText((cumple ? "✓ " : "✗ ") + texto);
        tv.setTextColor(ContextCompat.getColor(this,
                cumple ? R.color.req_ok : R.color.req_fail));
    }

    private void ejecutarRegistro(String usuario, String dni, String email, String password) {
        showLoading();
        ApiService apiService = ApiClient.getApiService();
        RegisterRequest registerRequest = new RegisterRequest(usuario, dni, email, password);

        Call<RegisterResponse> call = apiService.register(registerRequest);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    TokenStorage.clearSession(RegisterActivity.this);

                    LoginRequest loginRequest = new LoginRequest(email, password);
                    apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> callLogin, Response<LoginResponse> responseLogin){
                            hideLoading();
                            if (responseLogin.isSuccessful() && responseLogin.body() != null) {
                                LoginResponse loginResponse = responseLogin.body();

                                TokenStorage.saveSession(RegisterActivity.this, loginResponse.getToken(), loginResponse.getId());
                                Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.putExtra("Extra_USUARIO", loginResponse.getNombre() != null ? loginResponse.getNombre() : usuario);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registro exitoso. Por favor iniciá sesión.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> callLogin, Throwable tLogin) {
                            hideLoading();
                            Toast.makeText(RegisterActivity.this, "Registro exitoso. Iniciá sesión para continuar.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    hideLoading();
                    String mensaje = "Error al registrar usuario (" + response.code() + ")";
                    try {
                        if (response.errorBody() != null) {
                            String cuerpo = response.errorBody().string();
                            Log.e("REGISTRO", "URL=" + call.request().url()
                                    + " code=" + response.code() + " body=" + cuerpo);
                            ErrorResponse err = new Gson().fromJson(cuerpo, ErrorResponse.class);
                            if (err != null && err.getError() != null) {
                                mensaje = err.getError();
                            }
                        }
                    } catch (Exception e) {}

                    new androidx.appcompat.app.AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("No se pudo registrar")
                            .setMessage(mensaje)
                            .setPositiveButton("Entendido", null)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                hideLoading();
                Toast.makeText(RegisterActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}