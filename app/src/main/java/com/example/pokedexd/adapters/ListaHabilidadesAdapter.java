package com.example.pokedexd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedexd.R;

import java.util.ArrayList;

import models.Habilidad;

public class ListaHabilidadesAdapter extends RecyclerView.Adapter<ListaHabilidadesAdapter.ViewHolder> {

    private ArrayList<Habilidad> dataset;
    private Context context;

    public ListaHabilidadesAdapter(Context context){
        this.context=context;
        dataset= new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_habilidad,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Habilidad h=dataset.get(position);
        holder.nombreTextView.setText(h.getNombre());
        holder.descripcionTextView.setText(h.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void addListaHabilidades(ArrayList<Habilidad> listaHabilidades){
        dataset.addAll(listaHabilidades);
        notifyDataSetChanged();
    }

    public void eliminarHabilidades(){
        dataset.clear();
        notifyDataSetChanged();
    }

    public void addHabilidad(ArrayList<Habilidad> listaHabilidades){
        dataset=new ArrayList<>();
        dataset.addAll(listaHabilidades);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nombreTextView;
        private TextView descripcionTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreTextView = (TextView) itemView.findViewById(R.id.nombreHabilidad);
            descripcionTextView = (TextView) itemView.findViewById(R.id.descripcionHabilidad);

        }
    }
}
