package com.ispc.inmosmartmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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

        adapter = new FavoritoAdapter(favoritos, new FavoritoAdapter.OnFavoritoListener() {
            @Override
            public void onQuitar(Favorito favorito, int position) {
                quitarFavoritoApi(favorito.getId(),position);
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
    }

    @Override
    protected void onResume(){
        super.onResume();
        cargarFavoritoDesdeApi();
    }

    private void cargarFavoritoDesdeApi(){
        SharedPreferences prefs = getSharedPreferences("sesion", MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token == null || token.isEmpty()){
            Toast.makeText(this,"Inicia sesión para ver tus favoritos", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitClient.getApi().obtenerFavoritos("Bearer " + token).enqueue(new Callback<List<FavoritoResponse>>() {
            @Override
            public void onResponse(Call<List<FavoritoResponse>> call, Response<List<FavoritoResponse>> response) {
                if (response.isSuccessful() && response.body() != null){
                    favoritos.clear();

                    for (FavoritoResponse item : response.body()){
                        int idPropiedad = item.getPropiedad();

                        // Obtenemos los detalles desde el objeto propiedad_detalle
                        PropiedadDetalleFavorito detalle = item.getPropiedadDetalle();

                        String titulo = "Propiedad #" + idPropiedad;
                        String precio = "$ 0";

                        if (detalle != null) {
                            titulo = detalle.getTitulo() != null ? detalle.getTitulo() : "Propiedad #" + idPropiedad;
                            precio = "$ " + detalle.getPrecio();
                        }

                        int foto = android.R.drawable.ic_menu_gallery;

                        favoritos.add(new Favorito(idPropiedad, titulo, precio, foto));
                    }

                    adapter.notifyDataSetChanged();
                    actualizarVacio();
                } else {
                    Toast.makeText(FavoritosActivity.this, "Error al cargar favoritos: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FavoritoResponse>> call, Throwable t) {
                Toast.makeText(FavoritosActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void quitarFavoritoApi(int idPropiedad, int position){
        SharedPreferences prefs = getSharedPreferences("sesion", MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token == null) return;

        RetrofitClient.getApi().eliminarFavorito("Bearer " + token, idPropiedad).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    if (position >= 0 && position < favoritos.size()) {
                        favoritos.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, favoritos.size());
                        actualizarVacio();
                    }
                    Toast.makeText(FavoritosActivity.this, "Quitado de favoritos", Toast.LENGTH_SHORT).show();
                } else {
                    // AQUÍ MOSTRAMOS EL CÓDIGO EXACTO QUE DEVUELVE DJANGO
                    int errorCode = response.code();
                    Toast.makeText(FavoritosActivity.this, "Error al quitar: HTTP " + errorCode, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(FavoritosActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void actualizarVacio() {
        tvSinFavoritos.setVisibility(favoritos.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
