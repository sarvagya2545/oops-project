package com.example.triggertracker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ShoppingItemsRecyclerAdapter extends RecyclerView.Adapter<ShoppingItemsRecyclerAdapter.ShoppingItemViewHolder> {
    private List<ShoppingItem> shoppingItems = new ArrayList<ShoppingItem>();
    private Context context;
    private String TAG = "TAG";

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
        final ShoppingItem item = shoppingItems.get(position);
        final String itemID = item.getDocumentId();
//        Log.d("TAG", "onBindViewHolder: holder: " + holder.shoppingItemQty.toString());
//        Log.d("TAG", "onBindViewHolder: item: " + String.valueOf(item.getQty()));

        holder.shoppingItemName.setText(item.getName());
        holder.shoppingItemQty.setText(String.valueOf(item.getQty()));
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItemQty(itemID, item.getQty() + 1);
            }
        });
        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItemQty(itemID, item.getQty() - 1);
            }
        });
    }

    private void updateItemQty(String itemID, int qty) {
        FirebaseFirestore.getInstance()
                .collection("ShopListItems")
                .document(itemID)
                .update("qty", qty)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Updated the doc");
                        } else {
                            Log.e(TAG, "onComplete: ", task.getException());
                        }
                    }
                });
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

        private ImageView shoppingItemImage;
        private TextView shoppingItemName,  shoppingItemQty;
        private Button btnPlus, btnMinus;

        public ShoppingItemViewHolder(@NonNull View itemView) {
            super(itemView);

            shoppingItemImage = itemView.findViewById(R.id.shopping_item_image);
            shoppingItemName = itemView.findViewById(R.id.shopping_item_name);
            shoppingItemQty = itemView.findViewById(R.id.shopping_item_qty);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
        }
    }
}

///package com.example.triggertracker;
//
//        import android.content.Context;
//        import android.util.Log;
//        import android.view.LayoutInflater;
//        import android.view.View;
//        import android.view.ViewGroup;
//        import android.widget.Button;
//        import android.widget.ImageView;
//        import android.widget.TextView;
//
//        import androidx.annotation.NonNull;
//        import androidx.recyclerview.widget.RecyclerView;
//
//        import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
//        import com.firebase.ui.firestore.FirestoreRecyclerOptions;
//
//        import java.text.DateFormat;
//        import java.text.SimpleDateFormat;
//        import java.util.ArrayList;
//        import java.util.List;
//
//public class ShoppingItemsRecyclerAdapter extends FirestoreRecyclerAdapter<ShoppingItem, ShoppingItemsRecyclerAdapter.ShoppingItemViewHolder> {
//    private List<ShoppingItem> shoppingItems = new ArrayList<ShoppingItem>();
//    private Context context;
//
//    public ShoppingItemsRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ShoppingItem> options) {
//        super(options);
////        this.noteListener = noteListener;
//    }
//
//    @NonNull
//    @Override
//    public ShoppingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_item, parent,false);
//        ShoppingItemViewHolder holder = new ShoppingItemViewHolder(view);
//        return holder;
//    }
//
//    @Override
//    public int getItemCount() {
//        return shoppingItems.size();
//    }
//
//    @Override
//    protected void onBindViewHolder(@NonNull ShoppingItemViewHolder holder, int position, @NonNull ShoppingItem item) {
//        holder.shoppingItemName.setText(item.getName());
//        holder.shoppingItemQty.setText(String.valueOf(item.getQty()));
//    }
//
//    public void setShoppingItems(List<ShoppingItem> shoppingItems) {
//        this.shoppingItems = shoppingItems;
//        notifyDataSetChanged();
//    }
//
//    class ShoppingItemViewHolder extends RecyclerView.ViewHolder{
//
//        private ImageView shoppingItemImage;
//        private TextView shoppingItemName,  shoppingItemQty;
//        private Button btnPlus, btnMinus;
//
//        public ShoppingItemViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            shoppingItemImage = itemView.findViewById(R.id.shopping_item_image);
//            shoppingItemName = itemView.findViewById(R.id.shopping_item_name);
//            shoppingItemQty = itemView.findViewById(R.id.shopping_item_qty);
//            btnPlus = itemView.findViewById(R.id.btnPlus);
//            btnMinus = itemView.findViewById(R.id.btnMinus);
//        }
//    }
//}
