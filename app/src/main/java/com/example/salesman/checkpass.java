package com.example.salesman;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class checkpass {




        login login;
        Context context;
        SharedPreferences preferences;
        SharedPreferences.Editor editor;

        public checkpass(Context context) {
            this.context = context;
            preferences = context.getSharedPreferences("Mylogin", MODE_PRIVATE);
            editor = preferences.edit();
            login = new login();
        }

        public void iffalse() {

            editor.putBoolean("flag", true);
            editor.commit();

        }
    public void dofalse() {

        editor.putBoolean("flag", false);
        editor.commit();

    }
        public void iftrue() {

            if (!this.login()) {
                Intent intent = new Intent(context, login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                context.startActivity(intent);

            }
        }

        public boolean login() {


            return preferences.getBoolean("flag", false);


        }



}
