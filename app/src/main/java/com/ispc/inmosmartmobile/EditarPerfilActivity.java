package com.ispc.inmosmartmobile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class EditarPerfilActivity extends AppCompatActivity {

    private EditText etUsuario;
    private EditText etDni;
    private EditText etEmail;
    private EditText etTelefono;
    private AppCompatButton btnGuardar;
    private AppCompatButton btnCancelar;

    private SharedPreferences prefs;

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

        prefs = getSharedPreferences("perfil_prefs", MODE_PRIVATE);

        // Cargar los datos actuales
        String usuarioIntent = getIntent().getStringExtra("Extra_USUARIO");
        etUsuario.setText(prefs.getString("usuario", usuarioIntent));
        etDni.setText(prefs.getString("dni", ""));
        etEmail.setText(prefs.getString("email", ""));
        etTelefono.setText(prefs.getString("telefono", ""));

        // Guardar cambios
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
        if (dni.length() < 7 || dni.length() > 8) {
            Toast.makeText(this, "El DNI debe tener 7 u 8 dígitos", Toast.LENGTH_SHORT).show();
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

        // Guardar en SharedPreferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("usuario", usuario);
        editor.putString("dni", dni);
        editor.putString("email", email);
        editor.putString("telefono", telefono);
        editor.apply();

        Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
        finish(); // vuelve al perfil
    }
}