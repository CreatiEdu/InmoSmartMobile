package com.ispc.inmosmartmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallePropiedadActivity extends AppCompatActivity {

    private RecyclerView rvCarrusel;
    private TextView tvPrecio, tvDescripcion, tvSuperficie, tvRequisitos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_propiedad);

        rvCarrusel = findViewById(R.id.rvCarrusel);
        tvPrecio = findViewById(R.id.tvPrecio);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        tvSuperficie = findViewById(R.id.tvSuperficie);
        tvRequisitos = findViewById(R.id.tvRequisitos);

        int propiedadId = getIntent().getIntExtra("propiedad_id", -1);
        if (propiedadId == -1) {
            Toast.makeText(this, "Propiedad no válida", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        rvCarrusel.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        new PagerSnapHelper().attachToRecyclerView(rvCarrusel);

        cargarDetalle(propiedadId);
    }

    private void cargarDetalle(int id) {
        RetrofitClient.getApi().getDetalle(id).enqueue(new Callback<PropiedadDetalle>() {
            @Override
            public void onResponse(Call<PropiedadDetalle> call, Response<PropiedadDetalle> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(DetallePropiedadActivity.this,
                            "Error API: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                mostrar(response.body());
            }

            @Override
            public void onFailure(Call<PropiedadDetalle> call, Throwable t) {
                Toast.makeText(DetallePropiedadActivity.this,
                        "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
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

        List<String> imagenes = p.getImagenes() != null ? p.getImagenes() : new ArrayList<>();
        if (imagenes.isEmpty()) {
            rvCarrusel.setVisibility(View.GONE);
        } else {
            rvCarrusel.setVisibility(View.VISIBLE);
            rvCarrusel.setAdapter(new UrlSliderAdapter(imagenes));
        }
    }
}