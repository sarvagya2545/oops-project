package com.example.triggertracker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
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
    public void onBindViewHolder(@NonNull final ShoppingItemViewHolder holder, final int position) {
        final ShoppingItem item = shoppingItems.get(position);
        final String itemID = item.getDocumentId();

        holder.shoppingItemName.setText(item.getName());
        holder.shoppingItemQty.setText(String.valueOf(item.getQty()));
        holder.isBought.setChecked(item.isBought());

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

        holder.isBought.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    updateItemStatus(itemID,true);
                } else {
                    updateItemStatus(itemID,false);
                }
            }
        });

        item.getImageReference().getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        item.setShareUri(uri);

                        Glide.with(context)
                                .asBitmap()
                                .load(uri)
                                .placeholder(R.mipmap.ic_grocery)
                                .centerCrop()
                                .into(holder.shoppingItemImage);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Glide.with(context)
                                .asBitmap()
                                .load(R.mipmap.ic_grocery)
                                .centerCrop()
                                .into(holder.shoppingItemImage);

                        Log.d(TAG, "onFailure: Did not load");
                    }
                });

        holder.shoppingItemShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore.getInstance()
                        .collection("ShopListItem")
                        .document(itemID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if(user != null) {
                                        String name = user.getDisplayName() != null ? user.getDisplayName() : user.getPhoneNumber();
                                        String itemName = item.getName();
                                        String itemQuantity = Integer.toString(item.getQty());

                                        String msg = "Shopping List Item shared by: " + name +
                                                "\nItem: " + itemName +
                                                "\nQuantity: " + itemQuantity;

                                        final Intent myIntent=new Intent(Intent.ACTION_SEND);
                                        myIntent.setType("text/plain");

                                        String shareSub="Item";
                                        myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                                        myIntent.putExtra(Intent.EXTRA_TEXT,msg);

//                                        myIntent.putExtra(Intent.EXTRA_STREAM, item.getShareUri());

                                        context.startActivity(Intent.createChooser(myIntent, "Share via"));
                                    }

                                } else {
                                    Toast.makeText(context, "Some error occurred", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void updateItemStatus(String itemID ,boolean isBought) {
        FirebaseFirestore.getInstance()
                .collection("ShopListItems")
                .document(itemID)
                .update("bought", isBought)
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
        private ImageButton shoppingItemShare;
        private TextView shoppingItemName,  shoppingItemQty;
        private Button btnPlus, btnMinus;
        private CheckBox isBought;

        public ShoppingItemViewHolder(@NonNull View itemView) {
            super(itemView);

            shoppingItemShare = itemView.findViewById(R.id.shopping_share);
            isBought = itemView.findViewById(R.id.shopping_checkbox);
            shoppingItemImage = itemView.findViewById(R.id.shopping_item_image);
            shoppingItemName = itemView.findViewById(R.id.shopping_item_name);
            shoppingItemQty = itemView.findViewById(R.id.shopping_item_qty);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
        }
    }
}