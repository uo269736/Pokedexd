package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedexd.R;

import java.util.ArrayList;

import models.Ataque;

public class ListaAtaquesAdapter extends RecyclerView.Adapter<ListaAtaquesAdapter.ViewHolder>{

    private ArrayList<Ataque> dataset;
    private Context context;

    public ListaAtaquesAdapter(Context context) {
        this.context = context;
        dataset = new ArrayList<Ataque>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ataque, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaAtaquesAdapter.ViewHolder holder, int position) {
    Ataque a = dataset.get(position);
    holder.nombreTv.setText(a.getName());
    holder.descripcionTv.setText(a.getDescription());
    holder.tipoTv.setText(a.getType());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void addListaAtaques(ArrayList<Ataque> ataques) {
        dataset.addAll(ataques);
        notifyDataSetChanged();
    }

    public void eliminarAtaques() {
        dataset.clear();
        notifyDataSetChanged();
    }

    public void addAtaque(ArrayList<Ataque> ataques) {
        dataset = new ArrayList<>();
        dataset.addAll(ataques);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nombreTv;
        private TextView descripcionTv;
        private TextView tipoTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreTv = itemView.findViewById(R.id.nombreAtaque);
            descripcionTv = itemView.findViewById(R.id.descripcionAtaque);
            tipoTv = itemView.findViewById(R.id.tipoAtaque);
        }
    }
}
