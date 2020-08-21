package com.example.salesman;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import static android.content.Context.MODE_PRIVATE;

public class frag_target extends Fragment {

    public String stock;
TextView getstock;
    public targetface targetface;
    public  String mail;
    TextView stockmsg;
    public interface targetface {
        public void onytarget();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        context = (Context) targetface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_target, container, false);

    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener



                    Fragment fragment = null;

                    fragment = new mainfragment();

                    if(fragment != null)
                    {
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_main,fragment);
                        ft.commit();


                    }
                    DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getstock = getActivity().findViewById(R.id.txtgetstock);
        stockmsg = getActivity().findViewById(R.id.txtmsgstock);
        SharedPreferences preferences = getActivity().getSharedPreferences("Profile",MODE_PRIVATE);
        mail = preferences.getString("mail", "Default");


        final ProgressDialog loading =
                ProgressDialog.show(getContext(), "Fetching Stock",
                        "Please wait...", true);
        loading.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbyLrkr9X08mcQQnBm-P4eWCItxm1BwYwRxpkp14gTqhT0sADkQY/exec?action=gettarget&email="+mail+"&stock=0",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        stock = response;
                    int len = 0;


                        if(Integer.parseInt(stock) == 0)
                        {
                            stockmsg.setText("No available stock,Please Contact Admin");
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("Target",MODE_PRIVATE).edit();
                            editor.putString("stock",Integer.toString(len));
                            editor.commit();
                        }
                        else if(Integer.parseInt(stock) < 0)
                        {
                            stockmsg.setText("No available stock,Please Contact Admin");
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("Target",MODE_PRIVATE).edit();
                            editor.putString("stock",Integer.toString(len));
                            editor.commit();

                        }
                        else
                        {
                            getstock.setText(stock);
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("Target",MODE_PRIVATE).edit();
                            editor.putString("stock",stock);
                            editor.commit();

                        }

                        loading.dismiss();



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("ALERT").setMessage("Oops ERROR!").setCancelable(false).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        loading.dismiss();
                    }
                }
        );

        int socketTimeOut = 50000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);




    }


}


