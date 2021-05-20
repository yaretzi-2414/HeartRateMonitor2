package studio.anverso.heartratemonitor2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

public class InfoAdminActivity extends AppCompatActivity {

    //Declaración de variables
    String name, email, id, age, gender, msj, msj_string, msj_share;
    TextView nombre, correo, id_admin, edad, genero;
    private FirebaseAuth mAuth;
    ImageView share_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_admin);

        //Esta parte indica qué pantalla se abrirá al presionar algún elemento del menú de navegación inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.info);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.contacto:
                        startActivity(new Intent(InfoAdminActivity.this, ContactoAdminActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.medida:
                        startActivity(new Intent(InfoAdminActivity.this, AdminHome.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.historial:
                        startActivity(new Intent(InfoAdminActivity.this, HistorialAdminActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.info:

                        return true;
                    case R.id.ajustes:
                        startActivity(new Intent(InfoAdminActivity.this, SettingsAdminActivity.class));
                        overridePendingTransition(0, 0);
                        return true;


                }

                return false;
            }
        });


        //Asignamos variables para TextView
        nombre = findViewById(R.id.name_admin_info);
        id_admin = findViewById(R.id.id_admin_info);
        correo = findViewById(R.id.email_admin_info);
        genero = findViewById(R.id.genero_admin_info);
        edad = findViewById(R.id.edad_admin_info);
        share_icon = findViewById(R.id.share_icon);

        //Método que muestra la información del usuario activo en los TextView declarados anteriormente
        userInfoDisplay(id_admin, correo, nombre, genero, edad);

        //Compartir el ID del chaleco en diferentes redes sociales
        share_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent compartir = new Intent(android.content.Intent.ACTION_SEND);
                compartir.setType("text/plain");
                msj_string = getString(R.string.share_msj);
                msj = msj_string + "\n"+ "\n" + id_admin.getText().toString();
                msj_share = getString(R.string.share_msj_2);
                compartir.putExtra(Intent.EXTRA_SUBJECT, "Heart Rate Monitor");
                compartir.putExtra(Intent.EXTRA_TEXT, msj);
                startActivity(Intent.createChooser(compartir, msj_share));

            }
        });

        
    }

    //Método para obtener la información del usuario desde la base de datos
    private void userInfoDisplay(TextView id_admin, TextView correo, TextView nombre, TextView genero, TextView edad) {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Admin").child(Prevalent.currentOnlineUser.getId());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                        id = snapshot.child("id").getValue().toString();
                        email = snapshot.child("email").getValue().toString();
                        name = snapshot.child("name").getValue().toString();
                        age = snapshot.child("age").getValue().toString();
                        gender = snapshot.child("gender").getValue().toString();
                        nombre.setText(name);
                        correo.setText(email);
                        id_admin.setText(id);
                        edad.setText(age);
                        genero.setText(gender);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}