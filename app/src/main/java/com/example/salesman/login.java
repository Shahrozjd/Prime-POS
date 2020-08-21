package com.example.salesman;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText email;
    EditText password;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.txt_login_id);
        password = findViewById(R.id.txt_login_pass);
        imageView = findViewById(R.id.imagelogo);
        imageView.setImageResource(R.drawable.primelogo);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
    }

    public void login(View view) {

        final ProgressDialog loading = ProgressDialog.show(login.this, "Signing In", "Please wait");
        loading.setCancelable(false);
        // checkcred();
        email.setError(null);
        password.setError(null);
        final String logintxt = email.getText().toString();
        final String passtxt = password.getText().toString();

        if (logintxt.isEmpty() || passtxt.isEmpty()) {
            email.setError("Please enter email");
            password.setError("Please enter password");
loading.dismiss();


        } else {


            mAuth.signInWithEmailAndPassword(logintxt, passtxt)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {


                                checkpass checkpass = new checkpass(getApplicationContext());
                                checkpass.iffalse();
                                getemployee(logintxt);
//                                Intent intent = new Intent(com.example.salesman.login.this, MainActivity.class);
//                                startActivity(intent);
//                                finish();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(com.example.salesman.login.this);
                                builder.setTitle("ERROR!").setMessage("\n Check your Internet Connection.").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                loading.dismiss();
                            }
                        }
                    });

        }


    }



public void getemployee(String email)
{

//    loading =  ProgressDialog.show(this,"Loading","please wait",false,true);
    String url="https://script.google.com/macros/s/AKfycbyLrkr9X08mcQQnBm-P4eWCItxm1BwYwRxpkp14gTqhT0sADkQY/exec?action=search&Email="+email;
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    parseItems(response);

                Intent intent = new Intent(login.this,MainActivity.class);
                startActivity(intent);
                finish();

                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(login.this, "error,Try again", Toast.LENGTH_SHORT).show();
                }
            }
    );

    int socketTimeOut = 50000;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    stringRequest.setRetryPolicy(policy);
    RequestQueue queue = Volley.newRequestQueue(this);
    queue.add(stringRequest);
    
}
    private void parseItems(String jsonResposnce) {


        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("items");
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jo = jarray.getJSONObject(i);
                String Name = jo.getString("Name");
                String Email = jo.getString("Email");
                String Phone = jo.getString("Phone");
                String Address = jo.getString("Address");
                String Gender = jo.getString("Gender");
                String City = jo.getString("City");
                String Image = jo.getString("Image");

                sharedpreference(Name,Email,Phone,Address,Gender,City,Image);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void sharedpreference(String name ,String email , String phone , String Address , String gender ,String city, String image)
    {

        SharedPreferences.Editor editor = getSharedPreferences("Profile",MODE_PRIVATE).edit();
        editor.putString("name",name);
        editor.putString("mail",email);
        editor.putString("phone",phone);
        editor.putString("address",Address);
        editor.putString("gender",gender);
        editor.putString("city",city);
        editor.putString("image",image);
        editor.commit();

    }
    public void onForgot(View view) {
        Intent intent = new Intent(this,forgotpass.class);
        startActivity(intent);
        finish();

    }
}







//        adapter = new SimpleAdapter(this,list,R.layout.list_item_row,
//                new String[]{"Salesman","Client","Date","City","Items","Total"},new int[]{R.id.salsmanTxt,R.id.clientTxt,R.id.dateTxt,R.id.cityTxt,R.id.itemsTxt,R.id.totalTxt});
//        listView.setAdapter(adapter);
//        loading.dismiss();

//    public void checkcred()
//    {
//
//        //to check data
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbyLrkr9X08mcQQnBm-P4eWCItxm1BwYwRxpkp14gTqhT0sADkQY/exec",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response)
//                    {
//
//                        Toast.makeText(login.this, response, Toast.LENGTH_SHORT).show();
//

//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(com.example.salesman.login.this);
//                        builder.setTitle("ERROR!").setMessage("\n Check Internet Connection..").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//
//                            }
//                        });
//                        AlertDialog alertDialog = builder.create();
//                        alertDialog.show();
//
//
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> parmas = new HashMap<>();
//                parmas.put("action", "signin");
//                parmas.put("Email", logintxt);
//                parmas.put("Password", passtxt);
//                return parmas;
//            }
//        };
//
//        int socketTimeOut = 100000;
//        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        stringRequest.setRetryPolicy(retryPolicy);
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(stringRequest);
//
//    }









