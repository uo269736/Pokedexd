package com.example.pokedexd.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pokedexd.R;
import com.example.pokedexd.equipos.CrearEquipoActivity;

import java.util.ArrayList;
import java.util.List;

import models.Pokemon;
import models.database.Equipo;

public class MisEquiposAdapter extends RecyclerView.Adapter<MisEquiposAdapter.ViewHolder> {

    private List<Equipo> misEquipos;
    private Context context;

    public MisEquiposAdapter(Context context, ArrayList<Equipo> misEquipos) {
        this.context = context;
        this.misEquipos = misEquipos;
    }

    @NonNull
    @Override
    public MisEquiposAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_equipo, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MisEquiposAdapter.ViewHolder holder, int position) {
        String name = misEquipos.get(position).getNombre();
        List<Pokemon> pokemonList = misEquipos.get(position).getPokemonList();

        holder.nombreEquipo.setText(name);
        holder.btnEditEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CrearEquipoActivity.class);
                intent.putExtra("nombreEquipo", name);
                intent.putExtra("isEdit", true);
                context.startActivity(intent);
            }
        });

        int counter = 1;
        for (Pokemon pokemon: pokemonList) {
            holderImage(holder, pokemon, counter);
            counter++;
        }
    }

    private void holderImage(@NonNull MisEquiposAdapter.ViewHolder holder, Pokemon pokemon, int counter) {
        switch (counter) {
            case 1:
                Glide.with(context)
                        .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-viii/icons/"     + pokemon.getId2() + ".png")
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.pokemonView1);
                break;
            case 2:
                Glide.with(context)
                        .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-viii/icons/"     + pokemon.getId2() + ".png")
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.pokemonView2);
            break;
            case 3:
                Glide.with(context)
                        .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-viii/icons/" + pokemon.getId2() + ".png")
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.pokemonView3);
                break;
            case 4:
                Glide.with(context)
                        .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-viii/icons/" + pokemon.getId2() + ".png")
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.pokemonView4);
                break;
            case 5:
                Glide.with(context)
                        .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-viii/icons/" + pokemon.getId2() + ".png")
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.pokemonView5);
                break;
            case 6:
                Glide.with(context)
                        .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-viii/icons/" + pokemon.getId2() + ".png")
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.pokemonView6);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return misEquipos.size();
    }

    public void eliminarEquipo(Equipo equipo) {
        misEquipos.remove(equipo);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Button   btnEditEquipo;
        private TextView  nombreEquipo;
        private ImageView pokemonView1;
        private ImageView pokemonView2;
        private ImageView pokemonView3;
        private ImageView pokemonView4;
        private ImageView pokemonView5;
        private ImageView pokemonView6;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreEquipo  =  (TextView) itemView.findViewById(R.id.txtNombreEquipo);
            btnEditEquipo =    (Button) itemView.findViewById(R.id.btnEditEquipo);
            pokemonView1  = (ImageView) itemView.findViewById(R.id.pokemonView1);
            pokemonView2  = (ImageView) itemView.findViewById(R.id.pokemonView2);
            pokemonView3  = (ImageView) itemView.findViewById(R.id.pokemonView3);
            pokemonView4  = (ImageView) itemView.findViewById(R.id.pokemonView4);
            pokemonView5  = (ImageView) itemView.findViewById(R.id.pokemonView5);
            pokemonView6  = (ImageView) itemView.findViewById(R.id.pokemonView6);
        }
    }
}
