package com.example.furnitureapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.example.furnitureapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        products = new ArrayList<>();
//        products.add(new Product("sofa", "sofa", 12000, 1));
//        products.add(new Product("sofa", "sofa", 12000, 1));
//        products.add(new Product("sofa", "sofa", 12000, 1));
//        products.add(new Product("sofa", "sofa", 12000, 1));
//        products.add(new Product("sofa", "sofa", 12000, 1));
        DBTask dbt = new DBTask(MainActivity.this, products, binding);
        dbt.execute();
//        pa = new ProductAdapter(this, products);
//        GridLayoutManager gl = new GridLayoutManager(this, 2);
//        binding.productList.setLayoutManager(gl);
//        binding.productList.setAdapter(pa);
    }

    public void logout(View view) {
        auth.signOut();
    }

    public void toCart(View view) {
        startActivity(new Intent(MainActivity.this, CartActivity.class));
    }
}

class DBTask extends AsyncTask<String, Void, ArrayList<Product>> {
    Context ctx;
    ArrayList<Product> products;
    ActivityMainBinding binding;

    public DBTask(Context ctx, ArrayList<Product> products, ActivityMainBinding binding) {
        this.ctx = ctx;
        this.products = products;
        this.binding = binding;
    }

    @Override
    protected ArrayList<Product> doInBackground(String... strings) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2/furnitureapp", "root", "");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM product");
            while (rs.next()) {
                Product p = new Product(rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getInt(1));
                products.add(p);
            }
        } catch (SQLException e) {
        }
        return products;
    }

    protected void onPostExecute(ArrayList<Product> result) {
        ProductAdapter pa = new ProductAdapter(ctx, products);
        GridLayoutManager gl = new GridLayoutManager(ctx, 2);
        binding.productList.setLayoutManager(gl);
        binding.productList.setAdapter(pa);
    }
}