package com.ispc.inmosmartmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPropiedades extends AppCompatActivity {

    // Declaración de los elementos visuales del XML
    private Spinner spinnerTipoOperacion;
    private EditText etCiudad;
    private EditText etPrecioMax;
    private Button btnBuscar;
    private TextView tvSinResultados;
    private RecyclerView rvInmuebles;

    private InmueblesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propiedades);

        // 1. Vincular componentes Java con los IDs del layout XML
        spinnerTipoOperacion = findViewById(R.id.spinnerTipo);
        etCiudad = findViewById(R.id.etCiudad);
        etPrecioMax = findViewById(R.id.editPrecio);
        btnBuscar = findViewById(R.id.btnBuscar);
        tvSinResultados = findViewById(R.id.tvSinResultados);
        rvInmuebles = findViewById(R.id.rvInmuebles);

        // 2. Configurar las opciones del Spinner (Tipo de operación)
        String[] opcionesOperacion = {"Todos", "Alquiler", "Venta"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                opcionesOperacion
        );
        spinnerTipoOperacion.setAdapter(spinnerAdapter);

        // 3. Configurar el RecyclerView
        rvInmuebles.setLayoutManager(new LinearLayoutManager(this));
        adapter = new InmueblesAdapter(new ArrayList<>());
        rvInmuebles.setAdapter(adapter);

        // 4. Configurar el evento del botón Buscar
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarBusquedaConFiltros();
            }
        });

        // Carga inicial al abrir la pantalla
        ejecutarBusquedaConFiltros();
    }

    private void ejecutarBusquedaConFiltros() {
        // Obtener valores de los campos de texto y selector
        String tipoSeleccionado = spinnerTipoOperacion.getSelectedItem().toString();
        String tipoParam = tipoSeleccionado.equals("Todos") ? null : tipoSeleccionado;

        String ciudadInput = etCiudad.getText().toString().trim();
        String ciudadParam = ciudadInput.isEmpty() ? null : ciudadInput;

        String precioInput = etPrecioMax.getText().toString().trim();
        Double precioMaxParam = null;
        if (!precioInput.isEmpty()) {
            try {
                precioMaxParam = Double.parseDouble(precioInput);
            } catch (NumberFormatException e) {
                precioMaxParam = null;
            }
        }

        // Llamada a la API mediante Retrofit
        RetrofitClient.getApi().getInmueblesFiltrados(tipoParam, ciudadParam, precioMaxParam)
                .enqueue(new Callback<List<Inmueble>>() {
                    @Override
                    public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Inmueble> resultados = response.body();

                            if (resultados.isEmpty()) {
                                // Criterio de Aceptación: Si la lista viene vacía, muestra el mensaje
                                rvInmuebles.setVisibility(View.GONE);
                                tvSinResultados.setVisibility(View.VISIBLE);
                            } else {
                                // Si hay resultados, muestra la lista
                                rvInmuebles.setVisibility(View.VISIBLE);
                                tvSinResultados.setVisibility(View.GONE);
                                adapter.actualizarLista(resultados);
                            }
                        } else {
                            Toast.makeText(ActivityPropiedades.this, "Error al obtener respuesta de la API", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                        Toast.makeText(ActivityPropiedades.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}