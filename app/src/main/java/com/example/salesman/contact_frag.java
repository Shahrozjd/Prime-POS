package com.example.salesman;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.salesman.mainsale_frag.PERMISSION_REQUEST_CODE;

public class contact_frag extends Fragment {

    public static final int PERMISSION_REQUEST_CODE = 200;
    ImageView imageView1,imageView2,imageView3,imageView4;
    ImageView msgimageView1,msgimageView2,msgimageView3,msgimageView4;
    Context contactinterface;
    public interface homeinterface
    {
        public void oncontact();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        context = contactinterface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contact_frag,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageView1=getActivity().findViewById(R.id.callOne);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(checkPermission())
               {
                   makecall("03216611062","Usman Shahid");
               }
               else
               {
                   requestPermission();
               }

            }
        });

        imageView2=getActivity().findViewById(R.id.callTwo);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission())
                {
                    makecall("03216006724","Faisal Tufail");
                }
                else
                {
                    requestPermission();
                }
            }
        });

        imageView3=getActivity().findViewById(R.id.callThree);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission())
                {
                    makecall("03341557686","Shahroz Javed");
                }
                else
                {
                    requestPermission();
                }
            }
        });
        imageView4=getActivity().findViewById(R.id.callFour);
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission())
                {
                    makecall("03235308033","Abdul Hannan Sabir");
                }
                else
                {
                    requestPermission();
                }
            }
        });


        msgimageView1=getActivity().findViewById(R.id.msgOne);
        msgimageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission())
                {
                    makemsg("03216611062","Usman Shahid");
                }
                else
                {
                    requestPermission();
                }
            }
        });

        msgimageView2=getActivity().findViewById(R.id.msgTwo);
        msgimageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission())
                {
                    makemsg("03216006724","Faisal Tufail");
                }
                else
                {
                    requestPermission();
                }
            }
        });

        msgimageView3=getActivity().findViewById(R.id.msgThree);
        msgimageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission())
                {
                    makemsg("03341557686","Shahroz Javed");
                }
                else
                {
                    requestPermission();
                }
            }
        });

        msgimageView4=getActivity().findViewById(R.id.msgFour);
        msgimageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission())
                {
                    makemsg("03235308033","Abdul Hannan Sabir");
                }
                else
                {
                    requestPermission();
                }
            }
        });
    }

    public void makemsg(final String dial,final String name){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Are You Sure?");
        alertDialog.setMessage("Send sms to: "+name);
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse("smsto:"+dial);
                        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                        it.putExtra("sms_body", "Hi, ");
                        startActivity(it);
                    }
                });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog.show();
    }
    public void makecall(final String dial,final String name){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Are You Sure?");
        alertDialog.setMessage("Call: "+name);
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Dialing", Toast.LENGTH_SHORT).show();
                        String dialcall=dial;
                        long result = Long.parseLong(dialcall);
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:+92"+result));
                        startActivity(callIntent);
                    }
                });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog.show();
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CALL_PHONE},
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
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
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
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}