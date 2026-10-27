package com.ispc.inmosmartmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallePropiedadActivity extends AppCompatActivity {
    private Button btnFavoritoDetalle, btnAgendarTurno, btnEnviarMensaje;
    private RecyclerView rvCarrusel;
    private TextView tvPrecio, tvDescripcion, tvSuperficie, tvRequisitos;

    private boolean esFavorito = false;
    private int propiedadId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_propiedad);

        // Enlazar componentes
        btnFavoritoDetalle = findViewById(R.id.btnFavoritoDetalle);
        btnAgendarTurno = findViewById(R.id.btnAgendarTurno);
        btnEnviarMensaje = findViewById(R.id.btnEnviarMensaje);
        rvCarrusel = findViewById(R.id.rvCarrusel);
        tvPrecio = findViewById(R.id.tvPrecio);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        tvSuperficie = findViewById(R.id.tvSuperficie);
        tvRequisitos = findViewById(R.id.tvRequisitos);

        propiedadId = getIntent().getIntExtra("propiedad_id", -1);
        if (propiedadId == -1) {
            Toast.makeText(this, "Propiedad no válida", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnFavoritoDetalle.setOnClickListener(v -> {
            String token = TokenStorage.getToken(this);

            if (token == null || token.isEmpty()) {
                Toast.makeText(DetallePropiedadActivity.this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show();
                return;
            }

            if (esFavorito) {
                quitarFavoritoApi(token, propiedadId);
            } else {
                agregarFavoritoApi(token, propiedadId);
            }
        });

        btnEnviarMensaje.setOnClickListener(v -> {
            Intent intent = new Intent(DetallePropiedadActivity.this, contact.class);
            startActivity(intent);
        });

        // Listener del botón para Agendar Turno
        btnAgendarTurno.setOnClickListener(v -> {
            String token = TokenStorage.getToken(this);

            if (token == null || token.isEmpty()) {
                Toast.makeText(DetallePropiedadActivity.this, "Debes iniciar sesión para agendar un turno", Toast.LENGTH_SHORT).show();
                return;
            }

            mostrarSelectorFechaHora(token, propiedadId);
        });

        rvCarrusel.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        new PagerSnapHelper().attachToRecyclerView(rvCarrusel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDetalle(propiedadId);
    }

    private void cargarDetalle(int id) {
        String token = TokenStorage.getToken(this);
        String authHeader = (token != null && !token.isEmpty()) ? "Bearer " + token : null;

        RetrofitClient.getApi().getDetalle(authHeader, id).enqueue(new Callback<PropiedadDetalle>() {
            @Override
            public void onResponse(Call<PropiedadDetalle> call, Response<PropiedadDetalle> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(DetallePropiedadActivity.this, "Error API: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                mostrar(response.body());
            }

            @Override
            public void onFailure(Call<PropiedadDetalle> call, Throwable t) {
                Toast.makeText(DetallePropiedadActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void agregarFavoritoApi(String token, int idPropiedad) {
        AgregarFavoritoRequest request = new AgregarFavoritoRequest(idPropiedad);
        RetrofitClient.getApi().agregarFavorito("Bearer " + token, request).enqueue(new Callback<FavoritoResponse>() {
            @Override
            public void onResponse(Call<FavoritoResponse> call, Response<FavoritoResponse> response) {
                if (response.isSuccessful()) {
                    esFavorito = true;
                    actualizarEstadoBoton();
                    Toast.makeText(DetallePropiedadActivity.this, "¡Agregado a favoritos!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetallePropiedadActivity.this, "Error al guardar favorito", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FavoritoResponse> call, Throwable t) {
                Toast.makeText(DetallePropiedadActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void quitarFavoritoApi(String token, int idPropiedad) {
        RetrofitClient.getApi().eliminarFavorito("Bearer " + token, idPropiedad).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    esFavorito = false;
                    actualizarEstadoBoton();
                    Toast.makeText(DetallePropiedadActivity.this, "Quitado de favoritos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetallePropiedadActivity.this, "No se pudo quitar de favoritos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DetallePropiedadActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarSelectorFechaHora(String token, int idPropiedad) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_agendar_turno, null);

        CalendarView calendarView = dialogView.findViewById(R.id.calendarView);
        Spinner spinnerHoras = dialogView.findViewById(R.id.spinnerHoras);
        Button btnConfirmar = dialogView.findViewById(R.id.btnConfirmarDialogo);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelarDialogo);


        String[] horasDisponibles = {"10:00", "11:30", "14:00", "15:30", "17:00"};
        android.widget.ArrayAdapter<String> adapterHoras = new android.widget.ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, horasDisponibles);
        spinnerHoras.setAdapter(adapterHoras);

        final Calendar calendarSeleccionado = Calendar.getInstance();

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            calendarSeleccionado.set(year, month, dayOfMonth);
        });

        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        btnConfirmar.setOnClickListener(v -> {
            int year = calendarSeleccionado.get(Calendar.YEAR);
            int month = calendarSeleccionado.get(Calendar.MONTH) + 1;
            int day = calendarSeleccionado.get(Calendar.DAY_OF_MONTH);

            String horaSeleccionada = spinnerHoras.getSelectedItem().toString();

            String fechaHoraFormateada = String.format(Locale.getDefault(), "%d-%02d-%02d %s:00",
                    year, month, day, horaSeleccionada);

            dialog.dismiss();
            enviarTurnoApi(token, idPropiedad, fechaHoraFormateada);
        });

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void enviarTurnoApi(String token, int idPropiedad, String fechaHora) {
        AgendarTurnoRequest request = new AgendarTurnoRequest(idPropiedad, fechaHora);

        ApiClient.getApiService().agendarTurno("Bearer " + token, request).enqueue(new Callback<TurnoModel>() {
            @Override
            public void onResponse(Call<TurnoModel> call, Response<TurnoModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetallePropiedadActivity.this, "¡Turno agendado con éxito!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DetallePropiedadActivity.this, "Error al agendar el turno (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TurnoModel> call, Throwable t) {
                Toast.makeText(DetallePropiedadActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrar(PropiedadDetalle p) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("es", "AR"));
        nf.setMaximumFractionDigits(0);
        tvPrecio.setText("$ " + nf.format(p.getPrecio()));

        tvDescripcion.setText(p.getDescripcion());
        tvSuperficie.setText(p.getSuperficie() + " m²");
        tvRequisitos.setText(p.getHabitaciones() + " habitaciones, "
                + p.getBanios() + " baños. "
                + (p.isAceptaMascotas() ? "Acepta mascotas." : "No acepta mascotas."));

        esFavorito = p.isEsFavorito();
        actualizarEstadoBoton();

        List<String> imagenes = p.getImagenes() != null ? p.getImagenes() : new ArrayList<>();
        if (imagenes.isEmpty()) {
            rvCarrusel.setVisibility(View.GONE);
        } else {
            rvCarrusel.setVisibility(View.VISIBLE);
            rvCarrusel.setAdapter(new UrlSliderAdapter(imagenes));
        }
    }

    private void actualizarEstadoBoton() {
        if (esFavorito) {
            btnFavoritoDetalle.setText("Quitar de Favoritos");
            btnFavoritoDetalle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite_filled, 0, 0, 0);
        } else {
            btnFavoritoDetalle.setText("Agregar a Favoritos");
            btnFavoritoDetalle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorite, 0, 0, 0);
        }
    }
}