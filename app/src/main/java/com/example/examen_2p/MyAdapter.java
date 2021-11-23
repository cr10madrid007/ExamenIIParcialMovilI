package com.example.examen_2p;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<usuarios> list;


    public MyAdapter(Context context, ArrayList<usuarios> list ) {
        this.context = context;
        this.list = list;

    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.iteam,parent, false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final usuarios item = list.get(position);
        usuarios user = list.get(position);

        holder.txtnombre.setText(user.getNombre());
        holder.txttelefono.setText(user.getTelefono());
        holder.txtlatitud.setText(user.getLatitud());
        holder.txtlongitud.setText(user.getLongitud());
        Glide.with(holder.img1.getContext()).load(user.getUrl()).into(holder.img1);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(),GPSActivity.class);
                intent.putExtra("itemDetalle",item);

                holder.itemView.getContext().startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {

        return list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView img1;
        TextView txtnombre,txttelefono,txtlongitud,txtlatitud;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            img1 = itemView.findViewById(R.id.img1);
            txtnombre = itemView.findViewById(R.id.txtnombrelist);
            txttelefono = itemView.findViewById(R.id.txttelefonolist);
            txtlongitud = itemView.findViewById(R.id.txtlongitudlist);
            txtlatitud = itemView.findViewById(R.id.txtlatitudlist);


        }
    }



}
