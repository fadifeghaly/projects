package com.example.quizwhiz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Profil> mProfil;

    public RecyclerViewAdapter(Context mContext, List<Profil> mProfil) {
        this.mContext = mContext;
        this.mProfil = mProfil;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_profil, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.profil_nom.setText(mProfil.get(position).getNom());
        Picasso.get().load("http://10.0.2.2:8080/static/" + mProfil.get(position).getIdentifier() + ".png")
                .into(holder.profil_img);
        holder.cardView.setCardBackgroundColor(Color.TRANSPARENT);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ResidentHomePage.class);
                intent.putExtra("identifier", mProfil.get(position).getIdentifier());
                intent.putExtra("name", mProfil.get(position).getNom());
                intent.putStringArrayListExtra("accessibilities",mProfil.get(position).getAccessibilities());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mProfil.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView profil_nom;
        ImageView profil_img;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profil_nom = (TextView) itemView.findViewById(R.id.profil_nom_id);
            profil_img = (ImageView) itemView.findViewById(R.id.profil_img_id);
            cardView = (CardView) itemView.findViewById(R.id.profil_id);
            cardView.setCardElevation(0);
        }
    }

}
