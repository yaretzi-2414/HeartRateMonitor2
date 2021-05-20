package studio.anverso.heartratemonitor2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import studio.anverso.heartratemonitor2.Model.Mediciones;
import studio.anverso.heartratemonitor2.Prevalent.Prevalent;

public class HistorialAdminActivity extends AppCompatActivity {

    //Declaración de variables
    private DatabaseReference MedicionesRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    LinearLayoutManager layoutManager2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_admin);

        //Declaración de variable de referencia para obtener las mediciones del usuario que cuenta con el chaleco guardadas en la base de datos
        MedicionesRef = FirebaseDatabase.getInstance().getReference().child("Admin").child(Prevalent.currentOnlineUser.getId()).child("mediciones");

        //Esta parte indica qué pantalla se abrirá al presionar algún elemento del menú de navegación inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.historial);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.contacto:
                        startActivity(new Intent(HistorialAdminActivity.this, ContactoAdminActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.medida:
                        startActivity(new Intent(HistorialAdminActivity.this, AdminHome.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.historial:

                        return true;
                    case R.id.info:
                        startActivity(new Intent(HistorialAdminActivity.this, InfoAdminActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.ajustes:
                        startActivity(new Intent(HistorialAdminActivity.this, SettingsAdminActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;
            }
        });

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        //Muestra las mediciones de la más antigua a la más reciente
        /*layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);*/

        //Mostrar medicion más reciente en la parte superior
        layoutManager2 = new LinearLayoutManager(HistorialAdminActivity.this);
        layoutManager2.setReverseLayout(true);
        layoutManager2.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager2);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Código para mostrar el historial de mediciones de la persona que cuenta con el chaleco
        //Este código muestra todas las mediciones registradas en la base de datos
        FirebaseRecyclerOptions<Mediciones> options = new FirebaseRecyclerOptions.Builder<Mediciones>().setQuery(MedicionesRef, Mediciones.class).build();


        //Parar mostrar todas las mediciones se crea una lista con todos los datos de cada medición: latidos, fecha y hora
        //y se colocan en el txt correspondiente para mostrar la información en la app
        FirebaseRecyclerAdapter<Mediciones, ListAdapter> adapter = new FirebaseRecyclerAdapter<Mediciones, ListAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull ListAdapter holder, int position, @NonNull @NotNull Mediciones model) {
                holder.txtLatidos.setText(model.getMedicion());
                holder.txtFecha.setText(model.getDate());
                holder.txtHora.setText(model.getTime());
            }

            @NonNull
            @Override
            public ListAdapter onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_element, parent, false);
                ListAdapter holder = new ListAdapter(view);
                
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}