package com.ispc.inmosmartmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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

        int propiedadId = getIntent().getIntExtra("propiedad_id", 1);

        List<Integer> imagenesPrueba = new ArrayList<>();
        imagenesPrueba.add(android.R.drawable.ic_menu_gallery);
        imagenesPrueba.add(android.R.drawable.ic_menu_gallery);
        imagenesPrueba.add(android.R.drawable.ic_menu_gallery);

        SliderAdapter adapter = new SliderAdapter(imagenesPrueba);
        rvCarrusel.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvCarrusel.setAdapter(adapter);
        new PagerSnapHelper().attachToRecyclerView(rvCarrusel);

        tvPrecio.setText("$ 150.000");
        tvDescripcion.setText("Descripción de ejemplo de la propiedad " + propiedadId + ".");
        tvSuperficie.setText("120 m²");
        tvRequisitos.setText("Garantía propietaria, recibo de sueldo x3.");

        // TODO: cuando esté el endpoint real, reemplazar los datos de prueba
        // de arriba por una llamada a ApiService, algo como:
        // ApiClient.getApiService().getPropiedad(propiedadId).enqueue(...)
    }
}