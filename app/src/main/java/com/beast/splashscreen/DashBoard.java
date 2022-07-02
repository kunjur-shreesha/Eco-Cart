package com.beast.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beast.splashscreen.DRVinterface.LoadMore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class DashBoard extends AppCompatActivity implements PaymentResultListener {


    ImageView profile;
    Button payment_btn;
    private RecyclerView recyclerView;
    private StaticRvAdapter staticRvAdapter;
   static List<DynamicRvModel> items = new ArrayList<>();
    DynamicRVAdapter dynamicRVAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ActivityCompat.requestPermissions(DashBoard.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE},1);
        profile = findViewById(R.id.profile_icon);
        payment_btn = findViewById(R.id.payment_btn);
        registerForContextMenu(profile);
//        profile.setOnClickListener(view -> {
//            openContextMenu(view);
//        });

        //StaticRV
        ArrayList<StaticRvModel> item= new ArrayList<>();
        item.add(new StaticRvModel(R.drawable.axe,"Axe"));
        item.add(new StaticRvModel(R.drawable.sprite,"Sprite"));
        item.add(new StaticRvModel(R.drawable.pringles,"Pringles"));
        item.add(new StaticRvModel(R.drawable.lays,"Lays"));
        item.add(new StaticRvModel(R.drawable.corn,"Corn"));

        recyclerView = findViewById(R.id.rv_1);
        staticRvAdapter = new StaticRvAdapter(item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(staticRvAdapter);


//        Intent intent = getIntent();
//        price += intent.getIntExtra("price",0);


        //DynamicRV
//        items.add(new DynamicRvModel("Burger","Some Details about Product","$150",R.drawable.basket));
//        items.add(new DynamicRvModel("Basket","Some Details about Product","$75",R.drawable.potato_chips));
//        items.add(new DynamicRvModel("Aaaaa","Some Details about Product","$89",R.drawable.logo));
//        try {
//            items.add(new DynamicRvModel(data[0],data[1],data[2],R.drawable.basket));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        RecyclerView drv = findViewById(R.id.rv_2);
        drv.setLayoutManager(new LinearLayoutManager(this));
        if(items.size()==0)
        {
            payment_btn.setVisibility(View.INVISIBLE);
            drv.setBackgroundResource(R.drawable.empty_cart);

        }
        try {
//            payment_btn.setText(price);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dynamicRVAdapter = new DynamicRVAdapter(drv,this,items);
        drv.setAdapter(dynamicRVAdapter);


      /*  dynamicRVAdapter.setLoadMore(new LoadMore() {
            @Override
            public void onLoadMore() {
                if(items.size()<=3)
                {
                    items.add(null);
                    dynamicRVAdapter.notifyItemInserted(items.size()-1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            items.remove(items.size()-1);
                            dynamicRVAdapter.notifyItemRemoved(items.size());

                            int index = items.size();
                            int end = index+10;
                            for(int i=index;i<end;i++)
                            {
                                String name = UUID.randomUUID().toString();
                                DynamicRvModel item = new DynamicRvModel(name);
                                items.add(item);
                            }
                            dynamicRVAdapter.notifyDataSetChanged();
                            dynamicRVAdapter.setLoaded();
                        }
                    },2000);
                }
                else
                    Toast.makeText(DashBoard.this, "Data Completed", Toast.LENGTH_SHORT).show();
            }
        }); */
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoard.this,QrCodeScannerActivity.class));
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        //FIREBAse
//
//         FirebaseAuth mAuth;
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        String email = currentUser.getEmail();

        //RAZORPAY
//        String sAmount = "999";
//        int amount = Math.round(Float.parseFloat(sAmount)*100);
        payment_btn.setOnClickListener(view -> {
            int price=0;
            for(int i=0;i<items.size();i++)
            {
                View x=drv.getChildAt(i); // This will give you entire row(child) from RecyclerView
                if(x!=null)
                {
                    TextView textView= (TextView) x.findViewById(R.id.dynamic_item_price);
                     price+=Integer.parseInt(textView.getText().toString().substring(8));
//                    Log.i("ZZZ", "Price: "+price);
                }
                else
                {
                    Toast.makeText(this, "0 items in the Cart", Toast.LENGTH_SHORT).show();
                }
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Set the message show for the Alert time
            builder.setMessage("Continue to the Payment Portal ?");
            builder.setTitle("Total Cost : ₹"+price);
            builder.setCancelable(false);
            int finalPrice = price;
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    int amount = Math.round(Float.parseFloat(String.valueOf(finalPrice))*100);
                                    Checkout checkout = new Checkout();
                                    checkout.setKeyID("rzp_test_Ts9x6W4emtj28d");
                                    checkout.setImage(R.drawable.logo);
                                    JSONObject object = new JSONObject();
                                    try {
                                        object.put("name","User");
                                        object.put("description","E-Cart Payment");
                                        object.put("theme.color","#0da578");
                                        object.put("currency","INR");
                                        object.put("amount",amount);
                                        object.put("prefill.contact",null);
                                        object.put("prefill.email",null);
                                        checkout.open(DashBoard.this,object);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
            builder
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        });
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.m1,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.ac1:
            {
                Toast.makeText(this, "Construction Under Progress🌞", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.ac2:
            {
                Toast.makeText(this, "Construction Under Progress🌞", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.ac3:
            {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this,Login_Activity.class));
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onPaymentSuccess(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Payment ID");
        builder.setMessage(s);
        builder.show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}