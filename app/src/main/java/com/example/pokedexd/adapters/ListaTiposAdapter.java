package com.example.pokedexd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedexd.R;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class ListaTiposAdapter extends RecyclerView.Adapter<ListaTiposAdapter.ViewHolder>{
    
    private List<String> dataset;
    private Context context;

    public ListaTiposAdapter(Context context) {
        this.context = context;
        dataset = new ArrayList<>();
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tipo, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaTiposAdapter.ViewHolder holder, int position) {
        String nombreTipo = dataset.get(position);
        if(nombreTipo!=null){
            String tipoString= cleanString(nombreTipo);
            int drawableID = context.getResources().getIdentifier("tipo_"+tipoString.toLowerCase(), "drawable", context.getPackageName());
            holder.imagenTipo.setBackground(context.getResources().getDrawable(drawableID));
        }
        else
            holder.imagenTipo.setBackground(context.getResources().getDrawable(R.drawable.tipo_indefinido));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imagenTipo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imagenTipo = (ImageView) itemView.findViewById(R.id.fotoTipo);
        }
    }

    public void addListaTipos(List<String> nombresTipos) {
        dataset.clear();
        dataset.addAll(nombresTipos);
        notifyDataSetChanged();
    }

    public void removeListaTipos() {
        dataset.clear();
        notifyDataSetChanged();
    }

    public static String cleanString(String texto) {
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return texto;
    }
}
