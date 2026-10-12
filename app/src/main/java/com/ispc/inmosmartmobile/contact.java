package com.ispc.inmosmartmobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class contact extends AppCompatActivity {

    // Declaramos los componentes del formulario
    private EditText edtNombre;
    private EditText edtTelefono;
    private EditText edtEmail;
    private EditText edtAsunto;
    private EditText edtMensaje;
    private Button btnEnviar;
    private Button btnComoLlegar;
    private WebView webViewMapa;
    private ProgressBar progressMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_contact); // Vincula con tu XML de contacto
        super.onCreate(savedInstanceState);

        // 1. Enlazar componentes Java con sus ID en el XML
        edtNombre = findViewById(R.id.edtNombre);
        edtTelefono = findViewById(R.id.edtTelefono);
        edtEmail = findViewById(R.id.edtEmail);
        edtAsunto = findViewById(R.id.edtAsunto);
        edtMensaje = findViewById(R.id.edtMensaje);
        btnEnviar.setOnClickListener(v -> {
            if (!validarFormulario()) return;

            String nombre   = edtNombre.getText().toString().trim();
            String telefono = edtTelefono.getText().toString().replaceAll("[\\s().-]", ""); // normalizado
            String email    = edtEmail.getText().toString().trim().toLowerCase();
            String asunto   = edtAsunto.getText().toString().trim();
            String mensaje  = edtMensaje.getText().toString().trim();

            enviarContacto(new ContactoRequest(nombre, telefono, email, asunto, mensaje));
        });

// Borra el error apenas el usuario corrige el campo
        agregarLimpiadorDeError(edtNombre);
        agregarLimpiadorDeError(edtTelefono);
        agregarLimpiadorDeError(edtEmail);
        agregarLimpiadorDeError(edtAsunto);
        agregarLimpiadorDeError(edtMensaje);

        webViewMapa = findViewById(R.id.webViewMapa);
        btnComoLlegar = findViewById(R.id.btnComoLlegar);
        progressMapa = findViewById(R.id.progressMapa);

        // Configuración del mapa embebido de Google Maps (dentro de un iframe real)
        webViewMapa.getSettings().setJavaScriptEnabled(true);
        String htmlMapa = "<html><body style='margin:0;padding:0;'>" +
                "<iframe width='100%' height='100%' frameborder='0' style='border:0' " +
                "src='https://www.google.com/maps?q=Puesto+del+Marqu%C3%A9s+5550,+X5002AUD+C%C3%B3rdoba,+Argentina&output=embed'>" +
                "</iframe></body></html>";
        webViewMapa.loadDataWithBaseURL(null, htmlMapa, "text/html", "UTF-8", null);

        webViewMapa.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressMapa.setVisibility(View.GONE);
            }
        });

        // Botón "Cómo llegar" - abre la app nativa de Google Maps con la dirección
        btnComoLlegar.setOnClickListener(v -> {
            String uri = "geo:0,0?q=Puesto+del+Marqués+5550,+X5002AUD+Córdoba,+Argentina";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        });

        // 2. Enviar el formulario al backend
        btnEnviar.setOnClickListener(v -> {

            String nombre = edtNombre.getText().toString().trim();
            String telefono = edtTelefono.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String asunto = edtAsunto.getText().toString().trim();
            String mensaje = edtMensaje.getText().toString().trim();

            // Validar que NINGÚN campo esté vacío
            if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty()
                    || asunto.isEmpty() || mensaje.isEmpty()) {
                Toast.makeText(contact.this,
                        "Por favor complete todos los campos",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmail.setError("Email inválido");
                return;
            }

            enviarContacto(new ContactoRequest(nombre, telefono, email, asunto, mensaje));
        });
    }

    private boolean validarFormulario() {
        String nombre   = edtNombre.getText().toString().trim();
        String telefono = edtTelefono.getText().toString().trim();
        String email    = edtEmail.getText().toString().trim();
        String asunto   = edtAsunto.getText().toString().trim();
        String mensaje  = edtMensaje.getText().toString().trim();

        EditText primerError = null;

        // Nombre: solo letras, espacios, apóstrofes, puntos y guiones (2 a 60 caracteres)
        if (nombre.isEmpty()) {
            edtNombre.setError("Ingresá tu nombre");
            primerError = edtNombre;
        } else if (!nombre.matches("^\\p{L}[\\p{L} '.-]{1,59}$")) {
            edtNombre.setError("Nombre inválido (solo letras, entre 2 y 60 caracteres)");
            primerError = edtNombre;
        }

        // Teléfono: opcional "+", luego 8 a 15 dígitos (se ignoran espacios, guiones y paréntesis)
        String telLimpio = telefono.replaceAll("[\\s().-]", "");
        if (telefono.isEmpty()) {
            edtTelefono.setError("Ingresá tu teléfono");
            if (primerError == null) primerError = edtTelefono;
        } else if (!telLimpio.matches("^\\+?\\d{8,15}$")) {
            edtTelefono.setError("Teléfono inválido (entre 8 y 15 dígitos)");
            if (primerError == null) primerError = edtTelefono;
        }

        // Email
        if (email.isEmpty()) {
            edtEmail.setError("Ingresá tu email");
            if (primerError == null) primerError = edtEmail;
        } else if (email.length() > 254
                || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email inválido");
            if (primerError == null) primerError = edtEmail;
        }

        // Asunto: 3 a 100 caracteres
        if (asunto.length() < 3) {
            edtAsunto.setError("El asunto debe tener al menos 3 caracteres");
            if (primerError == null) primerError = edtAsunto;
        } else if (asunto.length() > 100) {
            edtAsunto.setError("El asunto no puede superar los 100 caracteres");
            if (primerError == null) primerError = edtAsunto;
        }

        // Mensaje: 10 a 1000 caracteres
        if (mensaje.length() < 10) {
            edtMensaje.setError("El mensaje debe tener al menos 10 caracteres");
            if (primerError == null) primerError = edtMensaje;
        } else if (mensaje.length() > 1000) {
            edtMensaje.setError("El mensaje no puede superar los 1000 caracteres");
            if (primerError == null) primerError = edtMensaje;
        }

        if (primerError != null) {
            primerError.requestFocus();
            return false;
        }
        return true;
    }

    private void agregarLimpiadorDeError(EditText campo) {
        campo.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {}
            @Override public void afterTextChanged(android.text.Editable s) { campo.setError(null); }
        });
    }

    // Envía el mensaje al backend (POST api/contacto/)
    private void enviarContacto(ContactoRequest body) {
        btnEnviar.setEnabled(false);

        ApiClient.getApiService().enviarContacto(body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        btnEnviar.setEnabled(true);
                        if (response.isSuccessful()) {
                            Toast.makeText(contact.this,
                                    "¡Mensaje enviado con éxito!",
                                    Toast.LENGTH_LONG).show();
                            limpiarCampos();
                        } else {
                            String cuerpo = "";
                            try {
                                if (response.errorBody() != null) cuerpo = response.errorBody().string();
                            } catch (Exception ignored) {}
                            android.util.Log.e("CONTACTO", "HTTP " + response.code() + " -> " + cuerpo);
                            Toast.makeText(contact.this,
                                    "Error al enviar (código " + response.code() + ")",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        btnEnviar.setEnabled(true);
                        Toast.makeText(contact.this,
                                "Error de conexión: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
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