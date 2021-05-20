package studio.anverso.heartratemonitor2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class ForgotPassword extends AppCompatActivity {

    private EditText InputEmail;
    private Button RecoveryButton;
    String msj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        InputEmail = findViewById(R.id.recovery_email_input);
        RecoveryButton = findViewById(R.id.recovery_btn);

        //Al presionar sobre el botón de recuperar se inicia el método validate()
        RecoveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

    }

    //Método para tomar el email escrito en el campo y comenzar con el método de sendEmail
    public void validate() {
        String email = InputEmail.getText().toString().trim();

        msj = getString(R.string.correo_invalido);

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            InputEmail.setError(msj);
            return;
        }
        sendEmail(email);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ForgotPassword.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    //Método que envía al correo registrado en el campo las instrucciones a seguir para escribir la nueva contraseña
    public void sendEmail(String email) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String emailAddress = email;
        mAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, R.string.lbl_restablecer_msg, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ForgotPassword.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(ForgotPassword.this, R.string.correo_invalido, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}