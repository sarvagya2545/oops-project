package com.example.triggertracker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ShoppingItemsRecyclerAdapter extends RecyclerView.Adapter<ShoppingItemsRecyclerAdapter.ShoppingItemViewHolder> {
    private List<ShoppingItem> shoppingItems = new ArrayList<ShoppingItem>();
    private Context context;

    public ShoppingItemsRecyclerAdapter(Context context) { this.context = context; }

    @NonNull
    @Override
    public ShoppingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_item, parent,false);
        ShoppingItemViewHolder holder = new ShoppingItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingItemViewHolder holder, final int position) {
        ShoppingItem item = shoppingItems.get(position);
//        Log.d("TAG", "onBindViewHolder: holder: " + holder.shoppingItemQty.toString());
//        Log.d("TAG", "onBindViewHolder: item: " + String.valueOf(item.getQty()));

        holder.shoppingItemName.setText(item.getName());
        holder.shoppingItemQty.setText(String.valueOf(item.getQty()));
        if(!item.getHasReminder()) {
            holder.shoppingItemDate.setText("");
//            holder.shoppingItemNotification.setVisibility(View.GONE);
        } else {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            CharSequence dateCharSeq = df.format(item.getReminderTime().toDate());
            holder.shoppingItemDate.setText(dateCharSeq);
//            holder.shoppingItemNotification.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return shoppingItems.size();
    }

    public void setShoppingItems(List<ShoppingItem> shoppingItems) {
        this.shoppingItems = shoppingItems;
        notifyDataSetChanged();
    }

    class ShoppingItemViewHolder extends RecyclerView.ViewHolder{

        private ImageView shoppingItemImage, shoppingItemNotification;
        private TextView shoppingItemName, shoppingItemDate, shoppingItemQty;
        private Button btnPlus, btnMinus;

        public ShoppingItemViewHolder(@NonNull View itemView) {
            super(itemView);

            shoppingItemImage = itemView.findViewById(R.id.shopping_item_image);
            shoppingItemNotification = itemView.findViewById(R.id.shopping_item_notification);
            shoppingItemName = itemView.findViewById(R.id.shopping_item_name);
            shoppingItemDate = itemView.findViewById(R.id.shopping_item_date);
            shoppingItemQty = itemView.findViewById(R.id.shopping_item_qty);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
        }
    }
}
