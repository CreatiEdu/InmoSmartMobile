package com.ispc.inmosmartmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisTurnosActivity extends AppCompatActivity {

    private RecyclerView rvTurnos;
    private TurnosAdapter adapter;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_turnos);

        rvTurnos = findViewById(R.id.rvTurnos);
        rvTurnos.setLayoutManager(new LinearLayoutManager(this));

        bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_contacto) {
                Intent intent = new Intent(MisTurnosActivity.this, contact.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_buscar) {
                Intent intent = new Intent(MisTurnosActivity.this, ActivityPropiedades.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_menu) {
                return true;
            } else if (itemId == R.id.nav_mas) {
                View viewMas = bottomNavigation.findViewById(R.id.nav_mas);
                mostrarMenuDesplegable(viewMas != null ? viewMas : bottomNavigation);
                return true;
            }

            return false;
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        cargarTurnos();
    }

    private void cargarTurnos() {
        SharedPreferences prefs = getSharedPreferences("sesion", MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Inicia sesión para ver tus turnos", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ApiClient.getApiService().obtenerTurnos("Bearer " + token).enqueue(new Callback<List<TurnoModel>>() {
            @Override
            public void onResponse(Call<List<TurnoModel>> call, Response<List<TurnoModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TurnoModel> turnos = response.body();
                    adapter = new TurnosAdapter(MisTurnosActivity.this, turnos, idTurno -> cancelarTurnoApi(token, idTurno));
                    rvTurnos.setAdapter(adapter);
                } else {
                    Toast.makeText(MisTurnosActivity.this, "Error al cargar turnos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TurnoModel>> call, Throwable t) {
                Toast.makeText(MisTurnosActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelarTurnoApi(String token, int idTurno) {
        ApiClient.getApiService().cancelarTurno("Bearer " + token, idTurno).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MisTurnosActivity.this, "Turno cancelado con éxito", Toast.LENGTH_SHORT).show();
                    cargarTurnos(); // Recargar la lista
                } else {
                    Toast.makeText(MisTurnosActivity.this, "No se pudo cancelar el turno", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MisTurnosActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarMenuDesplegable(View anchorView) {
        PopupMenu popup = new PopupMenu(MisTurnosActivity.this, anchorView);
        popup.getMenuInflater().inflate(R.menu.menu_mas, popup.getMenu());

        popup.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();

            if (itemId == R.id.sub_nosotros) {
                Intent intent = new Intent(MisTurnosActivity.this, QuienesSomosActivity.class);
                startActivity(intent);
                return true;
            }
            if (itemId == R.id.sub_perfil) {
                Intent intent = new Intent(MisTurnosActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            }
            if (itemId == R.id.sub_favoritos) {
                Intent intent = new Intent(MisTurnosActivity.this, FavoritosActivity.class);
                startActivity(intent);
                return true;
            }

            return false;
        });
    }
}