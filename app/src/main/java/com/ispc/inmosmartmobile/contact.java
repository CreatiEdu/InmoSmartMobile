package com.ispc.inmosmartmobile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class contact extends AppCompatActivity {

    // Declaramos los componentes del formulario
    private EditText edtNombre;
    private EditText edtTelefono;
    private EditText edtEmail;
    private EditText edtAsunto;
    private EditText edtMensaje;
    private Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact); // Vincula con tu XML de contacto

        // 1. Enlazar componentes Java con sus ID en el XML
        edtNombre = findViewById(R.id.edtNombre);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtEmail = findViewById(R.id.edtEmail);
        edtAsunto = findViewById(R.id.edtAsunto);
        edtMensaje = findViewById(R.id.edtMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);

        // 2. Escuchar el evento de clic del botón Enviar
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Obtener el texto ingresado en cada campo y quitar espacios extras
                String nombre = edtNombre.getText().toString().trim();
                String telefono = edtTelefono.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String asunto = edtAsunto.getText().toString().trim();
                String mensaje = edtMensaje.getText().toString().trim();

                // Validar que NINGÚN campo esté vacío
                if (!nombre.isEmpty() && !telefono.isEmpty() && !email.isEmpty()
                        && !asunto.isEmpty() && !mensaje.isEmpty()) {

                    // Acción cuando la validación es correcta
                    Toast.makeText(
                            contact.this,
                            "¡Mensaje enviado con éxito!",
                            Toast.LENGTH_LONG
                    ).show();

                    // Limpiar las casillas de texto después de enviar
                    limpiarCampos();

                } else {

                    // Advertencia si falta llenar algún campo
                    Toast.makeText(
                            contact.this,
                            "Por favor complete todos los campos",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }

    // Método auxiliar para resetear los EditText
    private void limpiarCampos() {
        edtNombre.setText("");
        edtTelefono.setText("");
        edtEmail.setText("");
        edtAsunto.setText("");
        edtMensaje.setText("");
    }
}