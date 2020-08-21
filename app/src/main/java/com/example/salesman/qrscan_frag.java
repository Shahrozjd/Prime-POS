package com.example.salesman;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PointF;
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
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

public class qrscan_frag extends Fragment implements QRCodeReaderView.OnQRCodeReadListener{


    public TextView resultxt;
    public qrscan qrscan;
    private QRCodeReaderView qrCodeReaderView;


    public interface qrscan
    {
        public void onqrscan();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        context = (Context) qrscan;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scanqr,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        resultxt = getActivity().findViewById(R.id.resulttxt);

        resultxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showalert();
            }
        });


        qrCodeReaderView = (QRCodeReaderView) getActivity().findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set front camera preview
        qrCodeReaderView.setFrontCamera();

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();
    }


    @Override
    public void onQRCodeRead(String text, PointF[] points) {


//
//
//        String[] value = text.split(",");

        if(text.length()>3)
        {
            String str = text.substring(0,3);


            if(str.equals("###"))
            {

                Fragment fragment = null;

                fragment = new saledata_frag();

                if(fragment != null)
                {



                    Bundle args = new Bundle();
                    args.putString("qrdata",text);

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

                resultxt.setText("Please use Company Certified QR"+"\nClick here to Continue.");

            }


        }
        else
        {

            resultxt.setText("Please use Company Certified QR"+"\nClick here to Continue.");

        }


    }



    public void showalert()
    {


                Fragment fragment1 = null;

                fragment1 = new mainfragment();

                if(fragment1 != null)
                {



                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_main,fragment1);
                    ft.commit();


                }
                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

    }
    @Override
    public void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();

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
    public void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }
}
