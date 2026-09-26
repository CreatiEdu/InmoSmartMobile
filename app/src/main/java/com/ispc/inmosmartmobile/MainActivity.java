package com.ispc.inmosmartmobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.viewpager2.widget.ViewPager2;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvBannerSaludo;
    private BottomNavigationView bottomNavigation;
    private ViewPager2 viewPagerCarrusel;
    private Handler carruselHandler;
    private Runnable carruselRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enlaza componentes con los ID del XML
        tvBannerSaludo = findViewById(R.id.tvBannerSaludo);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        viewPagerCarrusel = findViewById(R.id.viewPagerCarrusel);

        // Configuración del carrusel con imágenes
        List<Integer> imagenesCarrusel = new ArrayList<>();
        imagenesCarrusel.add(R.drawable.casa5);
        imagenesCarrusel.add(R.drawable.casa1);
        imagenesCarrusel.add(R.drawable.casa2);
        imagenesCarrusel.add(R.drawable.casa4);

        viewPagerCarrusel.setAdapter(new SliderAdapter(imagenesCarrusel));
        iniciarAutoScroll(imagenesCarrusel.size());

        // Recibimos los datos enviados desde LoginActivity
        String usuario = getIntent().getStringExtra("Extra_USUARIO");
        boolean mostrarBienvenida = getIntent().getBooleanExtra("MOSTRAR_BIENVENIDA", false);

        if (mostrarBienvenida && usuario != null) {
            // Mensaje rápido emergente (Toast)
            Toast.makeText(this, "¡Bienvenido/a, " + usuario + "!", Toast.LENGTH_LONG).show();
        }

        // Configuración de clics en el menú inferior
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_contacto) {
                // Abre la pantalla de Contacto
                Intent intent = new Intent(MainActivity.this, contact.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_buscar) {
                // Lógica de búsqueda
                Intent intent = new Intent(MainActivity.this, ActivityPropiedades.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_menu) {
                return true;
            } else if (itemId == R.id.nav_mas) {
                // Muestra el menú de los 3 puntos sobre el ícono "Más"
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
        // Evita que el Runnable siga ejecutándose si la Activity ya no existe
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
                // Prueba para verificar que entra al clic
                Toast.makeText(MainActivity.this, "Clic en Quiénes Somos", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, QuienesSomosActivity.class);
                startActivity(intent);
                return true;
            }

            return false;
        });

        popup.show();
    }
}