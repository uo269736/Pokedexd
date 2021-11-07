package adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedexd.R;

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
        holder.nombreTipo.setText(nombreTipo);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nombreTipo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTipo = itemView.findViewById(R.id.nombreTipo);
        }
    }

    public void addListaTipos(List<String> nombresTipos) {
        dataset.addAll(nombresTipos);
        notifyDataSetChanged();
    }

    public void removeListaTipos() {
        dataset.clear();
        notifyDataSetChanged();
    }
}
