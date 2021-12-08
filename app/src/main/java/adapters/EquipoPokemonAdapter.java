package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.pokedexd.R;

import java.util.ArrayList;

import models.Pokemon;

public class EquipoPokemonAdapter extends RecyclerView.Adapter<EquipoPokemonAdapter.ViewHolder> {

    private ArrayList<Pokemon> dataset;
    private Context context;

    public EquipoPokemonAdapter(Context context){
        this.context=context;
        dataset= new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon_equipo,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pokemon p=dataset.get(position);
        holder.nombrePokemon.setText(p.getName());
        holder.nombreObjeto.setText(p.getObjeto());
        Glide.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/"+p.getObjeto().toLowerCase().replace(" ","-")+".png")
                .centerCrop()
                .override(100, 100)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imagenObjeto);
        holder.nombreHabilidad.setText(p.getHabilidad());
        holder.nombreAtaque1.setText(p.getAtaques().get(0));
        holder.nombreAtaque2.setText(p.getAtaques().get(1));
        holder.nombreAtaque3.setText(p.getAtaques().get(2));
        holder.nombreAtaque4.setText(p.getAtaques().get(3));

        Glide.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-viii/icons/"+p.getId2()+".png")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.fotoImagePokemon);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void addListaPokemon(ArrayList<Pokemon> listaPokemon){
        dataset.addAll(listaPokemon);
        notifyDataSetChanged();
    }

    public void eliminarPokemon(){
        dataset.clear();
        notifyDataSetChanged();
    }

    public void eliminarPokemon(Pokemon p){
        dataset.remove(p);
        notifyDataSetChanged();
    }

    public void addPokemon(ArrayList<Pokemon> listaPokemon){
        dataset=new ArrayList<>();
        dataset.addAll(listaPokemon);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView fotoImagePokemon;
        private TextView nombrePokemon;
        private TextView nombreObjeto;
        private ImageView imagenObjeto;
        private TextView nombreHabilidad;
        private TextView nombreAtaque1;
        private TextView nombreAtaque2;
        private TextView nombreAtaque3;
        private TextView nombreAtaque4;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fotoImagePokemon = (ImageView)  itemView.findViewById(R.id.fotoImagePokemon);
            nombrePokemon = (TextView) itemView.findViewById(R.id.textViewNombrePokemon);
            nombreObjeto = (TextView) itemView.findViewById(R.id.textViewObjetoNombre);
            imagenObjeto = (ImageView)  itemView.findViewById(R.id.fotoImageViewObjeto);
            nombreHabilidad = (TextView) itemView.findViewById(R.id.textViewHabilidadNombre);
            nombreAtaque1 = (TextView) itemView.findViewById(R.id.textViewAtaques1);
            nombreAtaque2 = (TextView) itemView.findViewById(R.id.textViewAtaques2);
            nombreAtaque3 = (TextView) itemView.findViewById(R.id.textViewAtaques3);
            nombreAtaque4 = (TextView) itemView.findViewById(R.id.textViewAtaques4);

        }
    }
}
