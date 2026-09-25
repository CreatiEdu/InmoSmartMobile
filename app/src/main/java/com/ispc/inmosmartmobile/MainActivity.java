package com.ispc.inmosmartmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private TextView tvBannerSaludo;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enlaza componentes con los ID del XML
        tvBannerSaludo = findViewById(R.id.tvBannerSaludo);
        bottomNavigation = findViewById(R.id.bottomNavigation);

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