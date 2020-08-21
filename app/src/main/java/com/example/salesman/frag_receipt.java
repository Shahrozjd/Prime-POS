package com.example.salesman;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class frag_receipt extends Fragment {

    Button btnsaveproceed;



    public String date;
    public String email;
    public String client;
    public String city;
    public String address;
    public String item;
    public String unitprice;
    public String quantity;
    public String discount;
    public String totalamount;
    public String clientphn;
    public String saleid;
    public String totalprice;

    TextView recsid;
    TextView rectime;
    TextView recclient;
    TextView recclientphn;
    TextView recsalesman;
    TextView recitem;
    TextView recqty;
    TextView recprice;
    TextView rectotalitem;
    TextView rectotalprice;
    TextView recdiscount;
    TextView recnettotal;

    private Bitmap bitmap;

    LinearLayout view;


    public Context frag_receipt;
    public interface fragreceipt{
        public void onreceiptfrag();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        context=frag_receipt;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        date = getArguments().getString("Dat");
        email = getArguments().getString("Email");
        client = getArguments().getString("Client");
        city = getArguments().getString("City");
        address = getArguments().getString("Address");
        item = getArguments().getString("item");
        unitprice = getArguments().getString("Unitprice");
        quantity = getArguments().getString("Quantity");
        discount = getArguments().getString("Discount");
        totalamount = getArguments().getString("Totalamount");
        clientphn = getArguments().getString("Clientphn");
        saleid = getArguments().getString("saleid");
        totalprice = getArguments().getString("totalprice");

        return inflater.inflate(R.layout.reciept_frag,container,false);

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

        btnsaveproceed = getActivity().findViewById(R.id.recsave);

        view = (LinearLayout)getActivity().findViewById(R.id.printlayout);

        recsid = getActivity().findViewById(R.id.recref);
        rectime = getActivity().findViewById(R.id.recTime);
        recclient = getActivity().findViewById(R.id.recclient);
        recclientphn = getActivity().findViewById(R.id.recclientphn);
        recsalesman = getActivity().findViewById(R.id.recsalesman);
        recitem = getActivity().findViewById(R.id.recitem);
        recprice = getActivity().findViewById(R.id.recprice);
        rectotalitem = getActivity().findViewById(R.id.rectotalitem);
        rectotalprice = getActivity().findViewById(R.id.recTotalprice);
        recdiscount = getActivity().findViewById(R.id.recDiscount);
        recnettotal = getActivity().findViewById(R.id.recNetTotal);

        recsid.setText(saleid);
        rectime.setText(date);
        recclient.setText(client);
        recclientphn.setText(clientphn);
        recitem.setText(item);
        recprice.setText(unitprice);
        rectotalitem.setText(quantity);
        recdiscount.setText(discount);
        rectotalprice.setText(totalprice);
        recnettotal.setText(totalamount);
        recsalesman.setText(email);



        btnsaveproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transfertoimg();
            }
        });

    }

    public void transfertoimg()
    {
        ImageView bmImage;


        // this is the important code :)
        // Without it the view will have a dimension of 0,0 and the bitmap will be null

//        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//
//        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//        view.setBackgroundColor(getResources().getColor(android.R.color.white));
//        view.setDrawingCacheEnabled(true);
//        view.buildDrawingCache(true);

        Bitmap b = getBitmapFromView(view);



        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Prime");
        myDir.mkdirs();


        String fname = "Sale-" + saleid + ".jpg";
        File file = new File(myDir, fname);

        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

                    Toast.makeText(getContext(), "Saved to Gallery", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }







}