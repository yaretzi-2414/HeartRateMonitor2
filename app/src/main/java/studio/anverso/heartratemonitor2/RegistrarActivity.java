package studio.anverso.heartratemonitor2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrarActivity extends AppCompatActivity {

    //Declaración de variables
    private EditText InputEmail, InputPassword, InputConfPassword, InputName, InputEdad;
    String TipoUs, genero_usuario;
    private ProgressDialog loadingBar;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    Spinner genero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        //Variables FireBase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Variables de los elementos de la pantalla
        InputEmail = findViewById(R.id.btn_correo);
        InputPassword= findViewById(R.id.btn_contrasena);
        InputConfPassword = findViewById(R.id.btn_confirmar);
        InputName = findViewById(R.id.txtName);
        loadingBar = new ProgressDialog(this);

        genero = findViewById(R.id.SpinnerGender);
        InputEdad = findViewById(R.id.txt_edad);

        //Código para guardar la selección del género para registrar en la base de datos
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.op_genero, android.R.layout.simple_spinner_dropdown_item);
        genero.setAdapter(adapter);

        genero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genero_usuario = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    //Método para que al presionar sobre la opción se comience con el método de crear cuenta
    public void onClick(View view){
        switch (view.getId()){
            case R.id.button_crear:
                CreateAccount();
                break;
        }
    }

    //Método para crear la cuenta para poder ver la información del paciente o familiar
    private void CreateAccount() {
        //Se obtienen los textos de todos los campos y se pasan a variables String para poder trabajar con ellos
        String name = InputName.getText().toString();
        String email = InputEmail.getText().toString().trim();
        String password = InputPassword.getText().toString();
        String confpassword = InputConfPassword.getText().toString();
        String edad = InputEdad.getText().toString();

        //Se establece el tipo de usuario de la cuenta a crearse
        TipoUs = "Admin";

        //Mnesaje que aparece en la ventana que muestra el progreso para terminar de ejecutarse el método
        String msj2 = getResources().getString(R.string.msj_espera2);
        if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confpassword.isEmpty() && !edad.isEmpty()) {
            if (password.length() >= 6) {
                //Condición para comparar el texto de los dos campos donde se escribio la contraseña para validar que sean las mismas
                    if(password.equals(confpassword)){
                        loadingBar.setTitle(R.string.create_Account);
                        loadingBar.setMessage(msj2);
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        ValidateAccount(name, email, password, edad);
                    } else {
                        //Mensaje que se muestra si las contraseñas escritas en los dos campos no coinciden
                        Toast.makeText(this, R.string.pass_no_match, Toast.LENGTH_SHORT).show();
                    }

            } else {
                //Mensaje que se muestra si la contraseña escrita tiene menos de 6 carácteres
                Toast.makeText(RegistrarActivity.this, R.string.msj_password, Toast.LENGTH_SHORT).show();
            }
        } else {
            //Mensaje que se muestra si no se llenó la información solicitada en todos los campos
            Toast.makeText(RegistrarActivity.this, R.string.campos_complete, Toast.LENGTH_SHORT).show();
        }


    }

    //Método para validar la cuenta
    private void ValidateAccount(String name, String email, String password, String edad) {
        //Método para crear una cuenta Firebase
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Condición para comprobar si ya se termino de crear una cuenta en firebase
                if(task.isSuccessful()){
                    //Con las siguientes líneas se guarda la información escrita en los campos en una variable tipo HashMap
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("id", mAuth.getUid());
                    userdataMap.put("name", name);
                    userdataMap.put("email", email);
                    userdataMap.put("password", password);
                    userdataMap.put("gender", genero_usuario);
                    userdataMap.put("age", edad);

                    //Se obtiene el id del usuario recientemente creado
                    String id = mAuth.getCurrentUser().getUid();

                    //método para guardar la información de la variable tipo HashMap en la base de datos
                    mDatabase.child(TipoUs).child(id).setValue(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            //Verificar si registra los datos correctamente en la base de datos
                            if(task2.isSuccessful()){
                                //Enviar correo para verificar la cuenta creada
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //Se muestra un mensaje al completarse el envio del correo para verificar la cuenta
                                            Toast.makeText(RegistrarActivity.this, R.string.user_Registro,Toast.LENGTH_SHORT).show();
                                        }else{
                                            //Se muestra un mensaje de error si no se completo el envío del correo para verificar la cuenta
                                            Toast.makeText(RegistrarActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                                //Desaparece la barra de progreso y se inicia la pantalla de Login para ingresar con la cuenta creada
                                loadingBar.dismiss();
                                Intent intent = new Intent(RegistrarActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            else {
                                loadingBar.dismiss();
                                //Mensaje que muestra que la cuenta no se pudo crear
                                Toast.makeText(RegistrarActivity.this, R.string.datos_incorrectos, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    loadingBar.dismiss();
                    //Mensaje que muestra que la cuenta no se pudo crear y se abre la pantalla de si queremos registrarnos o entrar con una cuenta
                    Toast.makeText(RegistrarActivity.this, R.string.no_registro_user, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistrarActivity.this, RegisterLoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    //Método para dirigirnos a la pantalla de iniciar sesión
    public void irLogin(View view){
        Intent i = new Intent(RegistrarActivity.this, LoginActivity.class);
        startActivity(i);
    }

    //Método para dirigirnos a la pantalla de registrar cuenta de persona que quiere monitorear el ritmo cardiaco de otra persona
    public void irRegistrarUser(View view){
        Intent i = new Intent(RegistrarActivity.this, RegistrarUser.class);
        startActivity(i);
    }


}