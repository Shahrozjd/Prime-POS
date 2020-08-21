package com.example.salesman;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class forgotpass extends AppCompatActivity {


    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);
        editText = findViewById(R.id.txt_forgot_email);

    }

    public void reset(View view) {
        String Email = editText.getText().toString();
        if(Email.length()>0 && isValid(Email))
        {

            FirebaseAuth.getInstance().sendPasswordResetEmail(Email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(forgotpass.this);
                                builder.setTitle("Done.").setMessage("\n An email has sent to you.").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(forgotpass.this,login.class);
                                        startActivity(intent);

                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();


                            }
                            else
                            {
                                Toast.makeText(forgotpass.this, "Error sending email ,Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }
        else
        {
            Toast.makeText(this, "Please enter valid Email", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
