package com.ispc.inmosmartmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FavoritosActivity extends AppCompatActivity {

    private RecyclerView rvFavoritos;
    private TextView tvSinFavoritos;
    private final List<Favorito> favoritos = new ArrayList<>();
    private FavoritoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        rvFavoritos = findViewById(R.id.rvFavoritos);
        tvSinFavoritos = findViewById(R.id.tvSinFavoritos);

        // Datos de prueba (irian los reales cuando conectemos con la api)
        int foto = android.R.drawable.ic_menu_gallery;
        favoritos.add(new Favorito(1, "Casa en Río Cuarto", "$ 150.000", foto));
        favoritos.add(new Favorito(2, "Departamento centro", "$ 95.000", foto));
        favoritos.add(new Favorito(3, "Terreno en Córdoba", "$ 60.000", foto));

        adapter = new FavoritoAdapter(favoritos, new FavoritoAdapter.OnFavoritoListener() {
            @Override
            public void onQuitar(Favorito favorito, int position) {
                favoritos.remove(position);
                adapter.notifyItemRemoved(position);
                actualizarVacio();
                Toast.makeText(FavoritosActivity.this, "Quitado de favoritos", Toast.LENGTH_SHORT).show();
                // avisar a la api que se quito el favorito
            }

            @Override
            public void onAbrir(Favorito favorito) {
                // Abre el detalle pasándo el ID de la propiedad
                Intent intent = new Intent(FavoritosActivity.this, DetallePropiedadActivity.class);
                intent.putExtra("propiedad_id", favorito.getId());
                startActivity(intent);
            }
        });

        rvFavoritos.setLayoutManager(new LinearLayoutManager(this));
        rvFavoritos.setAdapter(adapter);
        actualizarVacio();

        // cuando este el endpoint real debemos cargar la lista desde la ApiService
    }

    private void actualizarVacio() {
        tvSinFavoritos.setVisibility(favoritos.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
