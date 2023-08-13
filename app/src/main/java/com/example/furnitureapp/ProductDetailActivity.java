package com.example.furnitureapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.furnitureapp.databinding.ActivityMainBinding;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity {
    ImageView prodImg;
    TextView prodName, quantity, prodPrice;
    MaterialButton dec, inc, btnAddToCart;
    Product p;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        final Object obj = getIntent().getSerializableExtra("details");
        if (obj instanceof Product) {
            p = (Product) obj;
        }
        auth = FirebaseAuth.getInstance();
        prodImg = findViewById(R.id.prodImg);
        prodName = findViewById(R.id.prodName);
        prodPrice = findViewById(R.id.prodPrice);
        quantity = findViewById(R.id.quantity);
        dec = findViewById(R.id.dec);
        inc = findViewById(R.id.inc);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        if (p != null) {
            int resourceId = getApplicationContext().getResources().getIdentifier(p.getImage(), "drawable", getApplicationContext().getPackageName());
            prodImg.setImageResource(resourceId);
            prodName.setText(p.getName());
            prodPrice.setText("PKR " + p.getPrice());
        }
    }

    public void inc(View view) {
        if (Integer.parseInt(quantity.getText().toString()) < 10)
            quantity.setText((Integer.parseInt(quantity.getText().toString()) + 1) + "");
    }

    public void dec(View view) {
        if (Integer.parseInt(quantity.getText().toString()) > 1)
            quantity.setText((Integer.parseInt(quantity.getText().toString()) - 1) + "");
    }

    public void goHome(View view) {
        startActivity(new Intent(ProductDetailActivity.this, MainActivity.class));
        finish();
    }

    public void addToCart(View view) {
        AddToCartTask atc = new AddToCartTask(ProductDetailActivity.this,
                Integer.parseInt(quantity.getText().toString()));
        atc.execute(p);
    }
}
class AddToCartTask extends AsyncTask<Product, Void, Integer> {
    Context ctx;
    FirebaseAuth auth;
    int qty;

    public AddToCartTask(Context ctx, int qty) {
        this.ctx = ctx;
        auth = FirebaseAuth.getInstance();
        this.qty = qty;
    }

    @Override
    protected Integer doInBackground(Product... products) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2/furnitureapp", "root", "");
            PreparedStatement st = conn.prepareStatement("INSERT INTO cartItem(name, image, price, quantity, uid, orderid) VALUES(?,?,?,?,?,0)");
            st.setString(1, products[0].getName());
            st.setString(2, products[0].getImage());
            st.setInt(3, (int)products[0].getPrice());
            st.setInt(4, qty);
            st.setString(5, auth.getUid());
            st.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
        return 1;
    }

    protected void onPostExecute(Integer result) {
        if (result > 0) {
            Toast.makeText(ctx, "Added to Cart", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(ctx, "Could not add to Cart", Toast.LENGTH_LONG).show();
        }
    }
}