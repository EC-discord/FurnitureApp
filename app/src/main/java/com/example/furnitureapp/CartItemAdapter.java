package com.example.furnitureapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furnitureapp.databinding.ItemCartBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {
    Context context;
    ArrayList<CartItem> cartItems;
    public CartItemAdapter(Context ctx, ArrayList<CartItem> cartItems) {
        this.context = ctx;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem c = cartItems.get(position);
        int resourceId = context.getResources().getIdentifier(c.getImage(), "drawable", context.getPackageName());
        holder.binding.cImage.setImageResource(resourceId);
        holder.binding.cName.setText(c.getName());
        holder.binding.cPrice.setText("Price: Rp " + c.getPrice());
        holder.binding.cQuantity.setText("Quantity: " + c.getQuantity());
//        holder.binding.btnRemove.setOnClickListener(view -> {
//                try {
//                    Connection conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2/furnitureapp", "root", "");
//                    Statement st = conn.createStatement();
//                    int i = st.executeUpdate("DELETE FROM cartItem WHERE id=" + c.getId() + " AND uid=" + FirebaseAuth.getInstance().getCurrentUser().getUid());
//                    if (i > 0)
//                        Toast.makeText(context, "Item removed", Toast.LENGTH_LONG).show();
//                    else
//                        Toast.makeText(context, "Item could not be removed", Toast.LENGTH_LONG).show();
//                } catch (SQLException e) {
//                }
//        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class CartItemViewHolder extends RecyclerView.ViewHolder {
        ItemCartBinding binding;
        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCartBinding.bind(itemView);
        }
    }
}
