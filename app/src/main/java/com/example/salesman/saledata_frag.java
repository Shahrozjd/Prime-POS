package com.example.salesman;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Contacts;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;

public class saledata_frag extends Fragment {

    public static final int PERMISSION_REQUEST_CODE = 101;
LinearLayout linearLayout;
    public String mail;
    EditText unitprice;
    EditText discount;
    EditText totalamount;
    EditText city;
    EditText client;
    EditText address;
    EditText clientphn;
    TextView showstock;


    public String stock1;
    public String saleid;
    public String primeitem;
    public Button Savebtn;
    public Button Printbtn;
    public String totalprice;


    Button inc;
    Button dec;
    TextView itemno;
    public int minteger = 0;

    public ProgressDialog loading;

    String Time;

    public String scandata;
    Button scanbtn;
    public saledataface saledataface;

    public String[] values = null;
    public interface saledataface {
        public void onsaledata();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        context = (Context) saledataface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       scandata = getArguments().getString("qrdata");
        return inflater.inflate(R.layout.frag_saledata, container, false);

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

                    fragment = new mainsale_frag();

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






        SharedPreferences preferences1 = getActivity().getSharedPreferences("Target",MODE_PRIVATE);
        stock1 = preferences1.getString("stock", "0");



        SharedPreferences preferences = getActivity().getSharedPreferences("Profile",MODE_PRIVATE);
        mail = preferences.getString("mail", "Default");

        client = getActivity().findViewById(R.id.txtclient);
        city = getActivity().findViewById(R.id.txtcity);
        address = getActivity().findViewById(R.id.txtaddress);
        clientphn = getActivity().findViewById(R.id.txtclientphn);

        String str = scandata.substring(0,3);


        // zero stock exception
        if(Integer.parseInt(stock1) == 0 || stock1.equals("0") )
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Dear Salesman,").setMessage("Target completed , Press ok to see more assigned ").setCancelable(false).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Fragment fragment = null;

                    fragment = new frag_target();

                    if(fragment != null)
                    {


                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_main,fragment);
                        ft.commit();


                    }
                    DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();


        }

        //qr  exception
        if(str.equals("###"))
{
    values = scandata.split(",");
    client.setText(values[1]);
    clientphn.setText(values[2]);
    city.setText(values[3]);
    address.setText(values[4]);
    client.setEnabled(false);
//            client.setFocusable(false);
//            client.setClickable(false);

    city.setEnabled(false);
//            city.setFocusable(false);
//            city.setClickable(false);

    address.setEnabled(false);
//            address.setFocusable(false);
//            address.setClickable(false);
    clientphn.setEnabled(false);

}


        //spinnerITEMS
        Spinner spinnercity = getActivity().findViewById(R.id.spinneritem);
        ArrayAdapter<CharSequence> adaptercity = ArrayAdapter.createFromResource(getContext(),R.array.primeitem,android.R.layout.simple_spinner_dropdown_item);
        adaptercity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnercity.setAdapter(adaptercity);
        spinnercity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                primeitem = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//elegantdesign
        inc = getActivity().findViewById(R.id.increase);
        inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger = minteger +1;
                if(minteger > Integer.parseInt(stock1))
                {
                    minteger = minteger -1;
                    Toast.makeText(getContext(), "Enter amount under available stock", Toast.LENGTH_SHORT).show();

                }
                else
                {
                display(minteger);
                }
            }
        });

        dec = getActivity().findViewById(R.id.decrease);
        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger = minteger - 1;

                if(minteger < 0)
                {
                    minteger = minteger + 1;
                    Toast.makeText(getContext(), "Enter amount in positive", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    display(minteger);
                }

            }
        });





        discount = getActivity().findViewById(R.id.txtdiscount);


            discount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                }

                @Override
                public void afterTextChanged(Editable s) {


                    if(discount.getText().length()>0)
                    {
                     if(discount.getText().equals(null))
                     {
                            totalamount.setText(""+Totalamount+"");
                     }
                     else
                     {
                         int sumtotal = Totalamount - Integer.parseInt(discount.getText().toString());
                         totalamount.setText(""+sumtotal+"");

                     }
                    }
                    else
                    {
                        int sumtotal = Totalamount - 0;
                        totalamount.setText(""+sumtotal+"");

                    }

                }
            });



//SAVEBUTTON

        Savebtn = getActivity().findViewById(R.id.btnsave);
        Savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                additemtosheet();




            }
        });



    }

    int Totalamount = 0;

    public  void display(int number)
    {

        itemno = getActivity().findViewById(R.id.integer_number);
        itemno.setText(""+number+"");

        unitprice = getActivity().findViewById(R.id.txtunitprice);

        if(unitprice.getText().length() > 0)
        {

            if(number != 0)
            {

                Totalamount = Integer.parseInt(unitprice.getText().toString()) * number;
                totalamount = getActivity().findViewById(R.id.txtTotalamnt);
                totalamount.setText(""+Totalamount+"");
                totalprice = Integer.toString(Totalamount);

            }

        }

    }

public void additemtosheet()
{

    final ProgressDialog loading =
            ProgressDialog.show(getContext(), "Adding Sale",
                    "Please wait...", true);
    loading.setCancelable(false);

    SimpleDateFormat sdf = new SimpleDateFormat( "yyyy/MM/dd_(hh:mm a)");
    Time = sdf.format(new Date());


    if(    discount.getText().toString().isEmpty()  || address.getText().toString().isEmpty()|| city.getText().toString().isEmpty() || unitprice.getText().toString().isEmpty() || client.getText().toString().isEmpty())
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("ALERT").setMessage("Please fill the complete form").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        loading.dismiss();

    }
    else
    {


        saleid = UUID.randomUUID().toString();
        saleid = saleid.substring(0,5);



        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbyLrkr9X08mcQQnBm-P4eWCItxm1BwYwRxpkp14gTqhT0sADkQY/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Success!").setMessage("Sale added Successfully.").setCancelable(false).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                if(checkPermission())
                                {

                                    Fragment fragment = null;


                                    fragment = new frag_receipt();

                                    if(fragment != null)
                                    {

                                        Bundle args = new Bundle();

                                        args.putString("Dat", Time);
                                        args.putString("Email", mail);
                                        args.putString("Client", client.getText().toString());
                                        args.putString("City", city.getText().toString());
                                        args.putString("Address",address.getText().toString());
                                        args.putString("item", primeitem);
                                        args.putString("Unitprice", unitprice.getText().toString());
                                        args.putString("Quantity", itemno.getText().toString());
                                        args.putString("Discount", discount.getText().toString());
                                        args.putString("Totalamount", totalamount.getText().toString());
                                        args.putString("Clientphn",clientphn.getText().toString());
                                        args.putString("saleid",saleid.toString());
                                        args.putString("totalprice",totalprice);

                                        fragment.setArguments(args);
                                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                        ft.replace(R.id.content_main,fragment);
                                        ft.commit();



                                    }
                                    DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                                    drawer.closeDrawer(GravityCompat.START);

                                }
                                else
                                {
                                    requestPermission();
                                }

                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        loading.dismiss();
                        //have to replace

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
        )
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();
                parmas.put("action", "addsale");
                parmas.put("Dat", Time);
                parmas.put("Email", mail);
                parmas.put("Client", client.getText().toString());
                parmas.put("City", city.getText().toString());
                parmas.put("Address",address.getText().toString());
                parmas.put("item", primeitem);
                parmas.put("Unitprice", unitprice.getText().toString());
                parmas.put("Quantity", itemno.getText().toString());
                parmas.put("Discount", discount.getText().toString());
                parmas.put("Totalamount", totalamount.getText().toString());
                parmas.put("Clientphn",clientphn.getText().toString());
                parmas.put("saleid",saleid.toString());
                parmas.put("totalprice",totalprice);

                return parmas;
            }
        };


        int socketTimeOut = 50000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);

        SharedPreferences preferences = getActivity().getSharedPreferences("Target",MODE_PRIVATE);
        String stock = preferences.getString("stock", "Default");

        int sum = Integer.parseInt(stock) - Integer.parseInt(itemno.getText().toString());

        SharedPreferences.Editor editor = getActivity().getSharedPreferences("Target",MODE_PRIVATE).edit();
        editor.putString("stock",Integer.toString(sum));
        editor.commit();

        addtargettosheet(Integer.toString(sum));

    }





}

public void addtargettosheet(String sum )
{

    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbyLrkr9X08mcQQnBm-P4eWCItxm1BwYwRxpkp14gTqhT0sADkQY/exec?action=settarget&email="+mail+"&stock="+sum+"",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {




                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
    );

    int socketTimeOut = 50000;
    RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    stringRequest.setRetryPolicy(retryPolicy);
    RequestQueue queue = Volley.newRequestQueue(getContext());
    queue.add(stringRequest);

}




    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }


    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


}