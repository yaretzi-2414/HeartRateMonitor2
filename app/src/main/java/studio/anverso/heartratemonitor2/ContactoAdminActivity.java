package studio.anverso.heartratemonitor2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import studio.anverso.heartratemonitor2.Prevalent.Prevalent;

public class ContactoAdminActivity extends AppCompatActivity {

    EditText fullNameContacto, phoneContacto;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto_admin);

        fullNameContacto = findViewById(R.id.txt_full_name);
        phoneContacto = findViewById(R.id.txt_number_phone_em);
        update = findViewById(R.id.update_emergencia);


        //Esta parte indica qué pantalla se abrirá al presionar algún elemento del menú de navegación inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.contacto);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch(item.getItemId()){
                case R.id.contacto:
                    return true;
                case R.id.medida:
                    startActivity(new Intent(ContactoAdminActivity.this, AdminHome.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.historial:
                    startActivity(new Intent(ContactoAdminActivity.this, HistorialAdminActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.info:
                    startActivity(new Intent(ContactoAdminActivity.this, InfoAdminActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.ajustes:
                    startActivity(new Intent(ContactoAdminActivity.this, SettingsAdminActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
            }

            return false;
        });

        //Método para mostrar la información del contacto de emergencia registrado
        userInfoDisplay(fullNameContacto, phoneContacto);

        //Al presionar el botón de Actualizar se llama al método updateOnlyUserInfo()
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOnlyUserInfo();
            }
        });

    }

    //Método para mostrar la información del contacto de emergencia registrado
    private void userInfoDisplay(final EditText fullNameContacto, final EditText phoneContacto) {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Admin").child(Prevalent.currentOnlineUser.getId());

        //Método para obtener la información del contacto de emergencia registrado: nombre y télefono
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("name_contact").exists()){
                        String name = snapshot.child("name_contact").getValue().toString();
                        String phone = snapshot.child("phone_contact").getValue().toString();
                        fullNameContacto.setText(name);
                        phoneContacto.setText(phone);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    //Método para actualizar la información del contacto de emergencia registrado en la base de datos
    private void updateOnlyUserInfo(){
        //Referencia a la BD donde se actualizará la info
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Admin");

        //En estas líneas se pasa el valor escrito en los campos para guardar en la BD
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name_contact", fullNameContacto.getText().toString());
        userMap.put("phone_contact", phoneContacto.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getId()).updateChildren(userMap);

        //Mensaje que indica que se guardó correctamente la información en la BD
        Toast.makeText(ContactoAdminActivity.this, R.string.Info_guardar, Toast.LENGTH_SHORT).show();

    }


}