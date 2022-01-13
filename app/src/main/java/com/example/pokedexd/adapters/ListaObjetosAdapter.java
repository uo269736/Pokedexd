package com.example.pokedexd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pokedexd.R;

import java.util.ArrayList;

import models.Objeto;


public class ListaObjetosAdapter extends RecyclerView.Adapter<ListaObjetosAdapter.ViewHolder>{

    private ArrayList<Objeto> dataset;
    private Context context;

    public ListaObjetosAdapter(Context context){
        this.context=context;
        dataset= new ArrayList<>();
    }

    @NonNull
    @Override
    public ListaObjetosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_objeto,parent,false);
        return new ListaObjetosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaObjetosAdapter.ViewHolder holder, int position) {
        Objeto o=dataset.get(position);
        holder.nombreTextView.setText(o.getNombre());
        holder.descripcionTextView.setText(o.getDescripcion());
        Glide.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/"+o.getName().toLowerCase().replace(" ","-")+".png")
                .centerCrop()
                .override(300, 300)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.fotoObjeto);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void addListaObjetos(ArrayList<Objeto> listaObjetos){
        dataset.addAll(listaObjetos);
        notifyDataSetChanged();
    }

    public void eliminarObjetos(){
        dataset.clear();
        notifyDataSetChanged();
    }

    public void addObjeto(ArrayList<Objeto> listaObjetos){
        dataset=new ArrayList<>();
        dataset.addAll(listaObjetos);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nombreTextView;
        private TextView descripcionTextView;
        private ImageView fotoObjeto;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreTextView = (TextView) itemView.findViewById(R.id.nombreObjeto);
            descripcionTextView = (TextView) itemView.findViewById(R.id.descripcionObjeto);
            fotoObjeto = (ImageView)  itemView.findViewById(R.id.fotoObjeto);

        }
    }
}
