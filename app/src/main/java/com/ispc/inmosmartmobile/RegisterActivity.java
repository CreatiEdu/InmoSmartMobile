package com.ispc.inmosmartmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
                    String dni = boxDni.toString();
                    String email = boxEmail.getText().toString().trim();
                    String password = boxPassword.getText().toString().trim();
                    String passwordAgain = boxPasswordAgain.getText().toString().trim();

                    // Validar que los campos no estén vacíos
                    if (!usuario.isEmpty() && !password.isEmpty() && !dni.isEmpty() && !email.isEmpty() && !passwordAgain.isEmpty()) {

                        // Crear Intent para ir a MainActivity
                        Intent intent = new Intent(com.ispc.inmosmartmobile.RegisterActivity.this, LoginActivity.class);

                        startActivity(intent);
                        Toast.makeText(
                        com.ispc.inmosmartmobile.RegisterActivity.this,
                        "Solicitud de Registro Enviada",
                        Toast.LENGTH_SHORT
                        ).show();
                        // Cierra la pantalla de Register al ingresar
                        finish();

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
    }