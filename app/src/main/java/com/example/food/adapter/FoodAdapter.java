package com.example.food.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.food.FoodProfileActivity;
import com.example.food.R;
import com.example.food.firebase.DaoFood;
import com.example.food.model.Favorite;
import com.example.food.model.Food;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyViewHolder> {

    ArrayList<Food> categoryList;
    Context context;
    String pLikes;
    DatabaseReference databaseReference,fvrtref,fvrt_listRef;
    FirebaseUser user;
    DaoFood daoFood;
    Food food1 = null;
    Boolean mProcessLike =false;

    public FoodAdapter(ArrayList<Food> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.monan, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        Food categories = categoryList.get(position);
        holder.title.setText(categories.getNamefood());
        holder.txtdiachi.setText(categories.getDiachi());
        user = FirebaseAuth.getInstance().getCurrentUser();
        daoFood = new DaoFood(context);
        fvrtref = FirebaseDatabase.getInstance().getReference("favourites");
        fvrt_listRef = FirebaseDatabase.getInstance().getReference("favoriteList").child(user.getUid());


        holder.txtgia.setText(String.valueOf(decimalFormat.format(categories.getGia()) + " VNĐ"));
        Picasso.get()
                .load(categories.getImage())
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("Error : ", e.getMessage());
                    }
                });


      String  key = categories.getIdfood();
        favouriteChecker(key,holder);
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProcessLike = true;


//

                fvrtref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        if (mProcessLike.equals(true)){
                            if (snapshot.child(key).hasChild(user.getUid())){
                                fvrtref.child(key).child(user.getUid()).removeValue();
                                fvrt_listRef.child(key).removeValue();
//                                delete(time);
                                Toast.makeText(context, "Xóa khỏi yêu thích!", Toast.LENGTH_SHORT).show();
                                mProcessLike = false;
                            }else {


                                fvrtref.child(key).child(user.getUid()).setValue(true);
                                Favorite favorite = new Favorite(fvrt_listRef.push().getKey(), user.getUid(), categories,true);

//                                  String id = fvrt_listRef.push().getKey();
                                fvrt_listRef.child(key).setValue(favorite);
                                mProcessLike = false;

                                Toast.makeText(context, "Thêm vào yêu thích!", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                mProcessLike =true;
//                setLikes(holder, categories,true);
            }
        });



        holder.cardView1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FoodProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("img", categories.getImage());
                intent.putExtra("gia", decimalFormat.format(categories.getGia()) + "\t VNĐ");
                intent.putExtra("namefood", categories.getNamefood());
                intent.putExtra("idfood", "Id: " + categories.getIdfood());
                intent.putExtra("idstore", categories.getIdstore());
                intent.putExtra("diachi", categories.getDiachi());
                intent.putExtra("sl", categories.getSoluong() + "");
                intent.putExtra("matl", categories.getMatheloai());
                intent.putExtra("status", categories.getStatus());
                intent.putExtra("mota", categories.getMota());
                intent.putExtra("tokenstore", categories.getTokenstore());
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return categoryList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, likeBtn;
        TextView title, txtdiachi, txtgia;
        ProgressBar progressBar;
        CardView cardView, cardView1, card_view4;
        LinearLayout line1;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgfood);
            title = itemView.findViewById(R.id.txtnamefood);
            progressBar = itemView.findViewById(R.id.progressbar);
            txtdiachi = itemView.findViewById(R.id.txtdiachi);
            txtgia = itemView.findViewById(R.id.txtgia);
            line1 = itemView.findViewById(R.id.line1);
            likeBtn = itemView.findViewById(R.id.likeBtn);
            cardView1 = itemView.findViewById(R.id.cardview1);

        }
    }

    public void favouriteChecker(final String postkey, MyViewHolder holder) {




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        fvrtref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(postkey).hasChild(uid)){
                    holder.likeBtn.setImageResource(R.drawable.ic_heart_liked);
                }else {
                    holder.likeBtn.setImageResource(R.drawable.ic_heart);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
