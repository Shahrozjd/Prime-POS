package com.example.salesman;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class fetchsaelist_frag extends Fragment {




    public static final int PERMISSION_REQUEST_CODE = 101;
    public EditText searchlistitem;
    public ListView listView;
    public ListAdapter adapter = null;
    ProgressDialog loading;
    public String mail;
    public String scandata;
    public String[] values = null;

    public fetchsalelistface fetchsalelistface;
    public interface fetchsalelistface{
        public void onfetchlist();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        context= (Context) fetchsalelistface;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        scandata = getArguments().getString("qrdata");

        return inflater.inflate(R.layout.fetchsalelist,container,false);
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

                    fragment = new history_frag();

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

        SharedPreferences preferences = getActivity().getSharedPreferences("Profile",MODE_PRIVATE);
        mail = preferences.getString("mail", "Default");

        listView = getActivity().findViewById(R.id.salelist);

        searchlistitem = getActivity().findViewById(R.id.txtlistsearch);


        String str = scandata.substring(0,3);

        if(str.equals("###"))
        {
            values = scandata.split(",");
            getItemsforcustomer(values[1]);

        }
        else
        {
            getItems();

        }


        //transfer history to reciept
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("ALERT").setMessage("Do you want to save this history Receipt?").setCancelable(true).setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        HashMap<String, String> item = (HashMap<String, String>) parent.getItemAtPosition(position);

                        //transferin to reciept
                        if(checkPermission())
                        {

                            Fragment fragment = null;

                            fragment = new frag_receipt();

                            if(fragment != null)
                            {

                                Bundle args = new Bundle();

                                args.putString("Dat", item.get("Date"));
                                args.putString("Email", mail);
                                args.putString("Client", item.get("Client"));
                                args.putString("City", item.get("City"));
                                args.putString("Address",item.get("Address"));
                                args.putString("item", item.get("item"));
                                args.putString("Unitprice", item.get("Unitprice"));
                                args.putString("Quantity", item.get("Quantity"));
                                args.putString("Discount", item.get("Discount"));
                                args.putString("Totalamount", item.get("Totalamount"));
                                args.putString("Clientphn",item.get("Clientphn"));
                                args.putString("saleid",item.get("saleid"));
                                args.putString("totalprice",item.get("totalprice"));

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
            }
        });

        //filter search
        final EditText getfilter = getActivity().findViewById(R.id.txtlistsearch);
        Button search = getActivity().findViewById(R.id.btnlistsearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String filter = getfilter.getText().toString();
                getfilterItems(filter);
            }
        });

        //revert
         Button revert = getActivity().findViewById(R.id.btnlistrevert);
         revert.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 getItems();
             }
         });


    }

    //FILTERSEARCH

    private void getfilterItems(final String filteritems) {

        loading =  ProgressDialog.show(getContext(),"Loading","please wait",false,true);
        loading.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbyLrkr9X08mcQQnBm-P4eWCItxm1BwYwRxpkp14gTqhT0sADkQY/exec?action=getsale&email="+mail+"",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        filterparseItems(response,filteritems);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );

        int socketTimeOut = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }

    private void filterparseItems(String jsonResposnce,String filterresult) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("items");

            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);
                String Client = jo.getString("Client");
                String Total = jo.getString("Totalamount");
                String unitprice = jo.getString("Unitprice");
                String discount = jo.getString("Discount");
                String quantity = jo.getString("Quantity");
                String items = jo.getString("item");
                String address = jo.getString("Address");
                String City = jo.getString("City");
                String Date = jo.getString("Date");
                String Email = jo.getString("Email");
                String Clientphn = jo.getString("Clientphn");
                String saleid = jo.getString("saleid");
                String totalprice = jo.getString("totalprice");


                HashMap<String, String> item = new HashMap<>();

                item.put("Client", Client);
                item.put("Totalamount", Total);
                item.put("Unitprice", unitprice);
                item.put("Discount", discount);
                item.put("Quantity", quantity);
                item.put("item", items);
                item.put("Address", address);
                item.put("City", City);
                item.put("Date", Date);
                item.put("Email",Email);
                item.put("Clientphn",Clientphn);
                item.put("saleid",saleid);
                item.put("totalprice",totalprice);

                list.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        adapter = new SimpleAdapter(getContext(),list,R.layout.listbind,
                new String[]{"Client","Totalamount","Unitprice","Discount","Quantity","item","Address","City","Date","Email","Clientphn","saleid","totalprice"},
                new int[]{R.id.txtlistclient,R.id.txtlistnettotal,R.id.txtlistunitprice,R.id.txtlistdiscount,R.id.txtlistquantity
                        ,R.id.txtlistitemname,R.id.txtlistaddress,R.id.txtlistcity,R.id.txtlistdate,R.id.txtlistsalesman,R.id.txtlistClientphn,R.id.txtlistsaleid,R.id.txtlisttotalprice});
        //filtering search

        ((SimpleAdapter) adapter).getFilter().filter(filterresult);
        listView.setAdapter(adapter);
        loading.dismiss();
    }
//GET FILTER ITEMS ENDED






    private void getItems() {

        loading =  ProgressDialog.show(getContext(),"Loading","please wait",false,true);
        loading.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbyLrkr9X08mcQQnBm-P4eWCItxm1BwYwRxpkp14gTqhT0sADkQY/exec?action=getsale&email="+mail+"",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );

        int socketTimeOut = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }

    private void parseItems(String jsonResposnce) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("items");

            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);
                String Client = jo.getString("Client");
                String Total = jo.getString("Totalamount");
                String unitprice = jo.getString("Unitprice");
                String discount = jo.getString("Discount");
                String quantity = jo.getString("Quantity");
                String items = jo.getString("item");
                String address = jo.getString("Address");
                String City = jo.getString("City");
                String Date = jo.getString("Date");
                String Email = jo.getString("Email");
                String Clientphn = jo.getString("Clientphn");
                String saleid = jo.getString("saleid");
                String totalprice = jo.getString("totalprice");


                HashMap<String, String> item = new HashMap<>();

                item.put("Client", Client);
                item.put("Totalamount", Total);
                item.put("Unitprice", unitprice);
                item.put("Discount", discount);
                item.put("Quantity", quantity);
                item.put("item", items);
                item.put("Address", address);
                item.put("City", City);
                item.put("Date", Date);
                item.put("Email",Email);
                item.put("Clientphn",Clientphn);
                item.put("saleid",saleid);
                item.put("totalprice",totalprice);

                list.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        adapter = new SimpleAdapter(getContext(),list,R.layout.listbind,
                new String[]{"Client","Totalamount","Unitprice","Discount","Quantity","item","Address","City","Date","Email","Clientphn","saleid","totalprice"},
                new int[]{R.id.txtlistclient,R.id.txtlistnettotal,R.id.txtlistunitprice,R.id.txtlistdiscount,R.id.txtlistquantity
                        ,R.id.txtlistitemname,R.id.txtlistaddress,R.id.txtlistcity,R.id.txtlistdate,R.id.txtlistsalesman,R.id.txtlistClientphn,R.id.txtlistsaleid,R.id.txtlisttotalprice});
        listView.setAdapter(adapter);
        loading.dismiss();
    }


    //CUSTOMER QRSCAN
    private void getItemsforcustomer(String client) {

        loading =  ProgressDialog.show(getContext(),"Loading","please wait",false,true);
        loading.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbyLrkr9X08mcQQnBm-P4eWCItxm1BwYwRxpkp14gTqhT0sADkQY/exec?action=getcustsale&customer="+client+"",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItemsforcustomer(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );

        int socketTimeOut = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }

    private void parseItemsforcustomer(String jsonResposnce) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("items");

            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);
                String Client = jo.getString("Client");
                String Total = jo.getString("Totalamount");
                String unitprice = jo.getString("Unitprice");
                String discount = jo.getString("Discount");
                String quantity = jo.getString("Quantity");
                String items = jo.getString("item");
                String address = jo.getString("Address");
                String City = jo.getString("City");
                String Date = jo.getString("Date");
                String Email = jo.getString("Email");
                String Clientphn = jo.getString("Clientphn");
                String saleid = jo.getString("saleid");
                String totalprice = jo.getString("totalprice");


                HashMap<String, String> item = new HashMap<>();

                item.put("Client", Client);
                item.put("Totalamount", Total);
                item.put("Unitprice", unitprice);
                item.put("Discount", discount);
                item.put("Quantity", quantity);
                item.put("item", items);
                item.put("Address", address);
                item.put("City", City);
                item.put("Date", Date);
                item.put("Email",Email);
                item.put("Clientphn",Clientphn);
                item.put("saleid",saleid);
                item.put("totalprice",totalprice);

                list.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        adapter = new SimpleAdapter(getContext(),list,R.layout.listbind,
                new String[]{"Client","Totalamount","Unitprice","Discount","Quantity","item","Address","City","Date","Email","Clientphn","saleid","totalprice"},
                new int[]{R.id.txtlistclient,R.id.txtlistnettotal,R.id.txtlistunitprice,R.id.txtlistdiscount,R.id.txtlistquantity
                        ,R.id.txtlistitemname,R.id.txtlistaddress,R.id.txtlistcity,R.id.txtlistdate,R.id.txtlistsalesman,R.id.txtlistClientphn,R.id.txtlistsaleid,R.id.txtlisttotalprice});


        listView.setAdapter(adapter);
        loading.dismiss();
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
//        adapter = new SimpleAdapter(getContext(),list,R.layout.listbind,
//                new String[]{"Client","Totalamount","Unitprice","Discount","Quantity","item","Address","City","Date"},
//                new int[]{R.id.txtlistclient,R.id.txtlistnettotal,R.id.txtlistunitprice,R.id.txtlistdiscount,R.id.txtlistquantity
//                        ,R.id.txtlistitemname,R.id.txtlistaddress,R.id.txtlistcity,R.id.txtlistdate});
//
//
//        listView.setTextFilterEnabled(true);
//        listView.setAdapter(adapter);



//                HashMap<String, String> item = new HashMap<>();
//
//
//                item.put("Client", Client);
//                item.put("Totalamount", Total);
//                item.put("Unitprice", unitprice);
//                item.put("Discount", discount);
//                item.put("Quantity", quantity);
//                item.put("item", items);
//                item.put("Address", address);
//                item.put("City", City);
//                item.put("Date", Date);
//
//               list.add(item);