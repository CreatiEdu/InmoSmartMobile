package com.ispc.inmosmartmobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvBannerSaludo;
    private TextView tvUsuarioBienvenido;
    private BottomNavigationView bottomNavigation;
    private ViewPager2 viewPagerCarrusel;
    private TabLayout tabIndicator;
    private Handler carruselHandler;
    private Runnable carruselRunnable;

    // Vistas de Búsqueda y Categorías
    private CardView cardSearch;
    private CardView cardCasas;
    private CardView cardLotes;
    private CardView cardAlquileres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enlaza componentes con los ID del XML
        tvBannerSaludo = findViewById(R.id.tvBannerSaludo);
        tvUsuarioBienvenido = findViewById(R.id.tvUsuarioBienvenido);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        viewPagerCarrusel = findViewById(R.id.viewPagerCarrusel);
        tabIndicator = findViewById(R.id.tabIndicator);

        // Enlazar Tarjetas
        cardSearch = findViewById(R.id.cardSearch);
        cardCasas = findViewById(R.id.cardCasas);
        cardLotes = findViewById(R.id.cardLotes);
        cardAlquileres = findViewById(R.id.cardAlquileres);

        // Configuración del carrusel con imágenes
        List<Integer> imagenesCarrusel = new ArrayList<>();
        imagenesCarrusel.add(R.drawable.casa5);
        imagenesCarrusel.add(R.drawable.casa1);
        imagenesCarrusel.add(R.drawable.casa2);
        imagenesCarrusel.add(R.drawable.casa4);

        viewPagerCarrusel.setAdapter(new SliderAdapter(imagenesCarrusel));

        // Conecta el indicador de puntos con el carrusel
        new TabLayoutMediator(tabIndicator, viewPagerCarrusel, (tab, position) -> {
        }).attach();

        iniciarAutoScroll(imagenesCarrusel.size());

        // Recibimos los datos enviados desde LoginActivity
        String usuario = getIntent().getStringExtra("Extra_USUARIO");

        if (usuario != null && !usuario.isEmpty()) {
            tvUsuarioBienvenido.setText("¡Hola, " + usuario + "!");
        } else {
            tvUsuarioBienvenido.setText("¡Bienvenido/a!");
        }

        // --- CLICS DE BÚSQUEDA Y OPCIONES ---

        // Clic en la barra de búsqueda rápida
        if (cardSearch != null) {
            cardSearch.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ActivityPropiedades.class);
                startActivity(intent);
            });
        }

        // Clic en tarjeta "Casas en venta"
        if (cardCasas != null) {
            cardCasas.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ActivityPropiedades.class);
                intent.putExtra("CATEGORIA", "Casas");
                startActivity(intent);
            });
        }

        // Clic en tarjeta "Lotes y terrenos"
        if (cardLotes != null) {
            cardLotes.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ActivityPropiedades.class);
                intent.putExtra("CATEGORIA", "Lotes");
                startActivity(intent);
            });
        }

        // Clic en tarjeta "Alquileres"
        if (cardAlquileres != null) {
            cardAlquileres.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ActivityPropiedades.class);
                intent.putExtra("CATEGORIA", "Alquileres");
                startActivity(intent);
            });
        }

        // Configuración de clics en el menú inferior
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_contacto) {
                Intent intent = new Intent(MainActivity.this, contact.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_buscar) {
                Intent intent = new Intent(MainActivity.this, ActivityPropiedades.class);
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

    // Hace que el carrusel avance solo cada 3 segundos
    private void iniciarAutoScroll(int cantidadImagenes) {
        carruselHandler = new Handler();
        carruselRunnable = new Runnable() {
            @Override
            public void run() {
                int siguiente = (viewPagerCarrusel.getCurrentItem() + 1) % cantidadImagenes;
                viewPagerCarrusel.setCurrentItem(siguiente, true);
                carruselHandler.postDelayed(this, 3000);
            }
        };
        carruselHandler.postDelayed(carruselRunnable, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (carruselHandler != null && carruselRunnable != null) {
            carruselHandler.removeCallbacks(carruselRunnable);
        }
    }

    // Método que despliega las opciones del menú emergente
    private void mostrarMenuDesplegable(View anchorView) {
        PopupMenu popup = new PopupMenu(MainActivity.this, anchorView);
        popup.getMenuInflater().inflate(R.menu.menu_mas, popup.getMenu());

        popup.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();

            if (itemId == R.id.sub_nosotros) {
                Intent intent = new Intent(MainActivity.this, QuienesSomosActivity.class);
                startActivity(intent);
                return true;
            }
            if (itemId == R.id.sub_perfil) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            }

            return false;
        });

        popup.show();
    }
}