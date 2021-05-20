package studio.anverso.heartratemonitor2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import studio.anverso.heartratemonitor2.Prevalent.Prevalent;

public class UserProfile extends AppCompatActivity {

    //Declaración de variables
    private FirebaseAuth mAuth;
    String name, email;
    TextView nombre, correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Esta parte indica qué pantalla se abrirá al presionar algún elemento del menú de navegación inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_user);
        bottomNavigationView.setSelectedItemId(R.id.info_user);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.info_user:

                        return true;
                    case R.id.home_user:
                        startActivity(new Intent(UserProfile.this, UserHome.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.ajustes:
                        startActivity(new Intent(UserProfile.this, UserSettings.class));
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;
            }
        });

        //Asignamos variables para TextView
        nombre = findViewById(R.id.name_user_info);
        correo = findViewById(R.id.email_user_info);

        //Método que muestra el correo y nombre dle usuario activo en los TextView declarados anteriormente
        userInfoDisplay(correo, nombre);
    }

    //Método para obtener la información del usuario desde la base de datos
    private void userInfoDisplay(TextView correo, TextView nombre) {
        //Referencia al nodo de la base de datos que contiene la información del usuario
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser2.getId());

        //Método para obtener la información del usuario y se actualiza en tiempo real
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    email = snapshot.child("email").getValue().toString();
                    name = snapshot.child("name").getValue().toString();
                    //Se pasa la información obtenida de la base de datos a los TextView
                    nombre.setText(name);
                    correo.setText(email);


                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}