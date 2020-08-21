package com.example.salesman;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;

public class profileframgent extends Fragment {


    TextView name;
    TextView email;
    TextView phone;
    TextView address;
    TextView gender;
    TextView city;
    ImageView profileimg;
    public  profileinterface profileinter;
    public interface profileinterface
    {
        void onprofile();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try
        {
          profileinter = (profileinterface) context;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_frag,container,false);
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
        name = getActivity().findViewById(R.id.txtnameprofile);
        email = getActivity().findViewById(R.id.txtemailprofile);
        address = getActivity().findViewById(R.id.txtaddressprofile);
        gender = getActivity().findViewById(R.id.txtgenderprofile);
        city = getActivity().findViewById(R.id.txtcityprofile);
        phone = getActivity().findViewById(R.id.txtphnprofile);

        profileimg = getActivity().findViewById(R.id.profile_image);

        SharedPreferences preferences = getContext().getSharedPreferences("Profile",MODE_PRIVATE);
        String image = preferences.getString("image", "Default");
        String name1 = preferences.getString("name", "Default");
        String mail1 = preferences.getString("mail", "Default");
        String phn1 = preferences.getString("phone", "Default");
        String address1 = preferences.getString("address", "Default");
        String gender1 = preferences.getString("gender", "Default");
        String city1 = preferences.getString("city", "Default");

        profileimg.setImageBitmap(decodeBase64(image));
        name.setText(name1);
        email.setText(mail1);
        address.setText(address1);
        gender.setText(gender1);
        city.setText(city1);
        phone.setText(phn1);



    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
