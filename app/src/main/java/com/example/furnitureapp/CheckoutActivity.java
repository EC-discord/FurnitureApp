package com.example.furnitureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CheckoutActivity extends AppCompatActivity {
    TextView totalPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        final Object obj = getIntent().getSerializableExtra("total");
        totalPrice = findViewById(R.id.totalPrice);
        totalPrice.setText(obj.toString());
        OrderTask ot = new OrderTask(CheckoutActivity.this);
        ot.execute(obj.toString());
        totalPrice.setText(obj.toString());
    }

    public void toHome(View view) {
        startActivity(new Intent(CheckoutActivity.this, MainActivity.class));
    }
}
class OrderTask extends AsyncTask<String, Void, Integer> {
    Context ctx;
    FirebaseAuth auth;

    public OrderTask(Context ctx) {
        this.ctx = ctx;
        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected Integer doInBackground(String... strings) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2/furnitureapp", "root", "");
            PreparedStatement st = conn.prepareStatement("INSERT INTO `order`(uid, total) VALUES(?,?)");
            st.setString(1, auth.getCurrentUser().getUid());
            st.setInt(2, Integer.parseInt(strings[0]));
            st.executeUpdate();
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT id FROM `order` ORDER BY id DESC LIMIT 1");
            st = conn.prepareStatement("UPDATE cartItem SET orderid=? WHERE uid=? AND orderid=0");
            rs.next();
            st.setInt(1, Integer.parseInt(rs.getString(1)));
            st.setString(2, auth.getCurrentUser().getUid());
            st.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
        return 1;
    }

    protected void onPostExecute(Integer result) {
        if (result > 0) {
            Toast.makeText(ctx, "Order placed", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(ctx, "Could not place order", Toast.LENGTH_LONG).show();
        }
    }
}