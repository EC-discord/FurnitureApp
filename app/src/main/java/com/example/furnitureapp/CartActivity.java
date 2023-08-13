package com.example.furnitureapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.furnitureapp.databinding.ActivityCartBinding;
import com.example.furnitureapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    TextView empty;
    ActivityCartBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        empty = findViewById(R.id.empty);
        LoadItemsTask lit = new LoadItemsTask(CartActivity.this, new ArrayList<CartItem>(), binding, empty);
        lit.execute();
    }

    public void toHome(View view) {
        startActivity(new Intent(CartActivity.this, MainActivity.class));
    }

    public void toCheckout(View view) {
        if (empty.getText().toString().equals("Cart is empty")) {
            Toast.makeText(CartActivity.this, "Cart is empty", Toast.LENGTH_LONG).show();
            return;
        }
        Intent i = new Intent(CartActivity.this, CheckoutActivity.class);
        i.putExtra("total", empty.getText().toString().substring(7));
        startActivity(i);
    }
}
class LoadItemsTask extends AsyncTask<String, Void, ArrayList<CartItem>> {
    Context ctx;
    TextView empty;
    ArrayList<CartItem> cartItems;
    ActivityCartBinding binding;

    public LoadItemsTask(Context ctx, ArrayList<CartItem> cartItems, ActivityCartBinding binding, TextView empty) {
        this.ctx = ctx;
        this.cartItems = cartItems;
        this.binding = binding;
        this.empty = empty;
    }

    @Override
    protected ArrayList<CartItem> doInBackground(String... strings) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2/furnitureapp", "root", "");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM cartItem WHERE uid='" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "' AND orderid = 0");
            while (rs.next()) {
                CartItem p = new CartItem(rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getInt(1), rs.getInt(5));
                cartItems.add(p);
            }
        } catch (SQLException e) {
        }
        return cartItems;
    }

    protected void onPostExecute(ArrayList<CartItem> result) {
        CartItemAdapter ca = new CartItemAdapter(ctx, cartItems);
        GridLayoutManager gl = new GridLayoutManager(ctx, 1);
        binding.cartItemList.setLayoutManager(gl);
        binding.cartItemList.setAdapter(ca);
        int total = 0;
        if (cartItems.size() == 0) {
            empty.setVisibility(View.VISIBLE);
            return;
        }
        for (CartItem ci: cartItems) {
            total += ci.getPrice() * ci.getQuantity();
        }
        empty.setText("Total: " + total);
        empty.setVisibility(View.VISIBLE);
    }
}