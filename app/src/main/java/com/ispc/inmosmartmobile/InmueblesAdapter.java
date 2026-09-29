package com.ispc.inmosmartmobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class InmueblesAdapter extends RecyclerView.Adapter<InmueblesAdapter.ViewHolder> {

    private List<Inmueble> listaInmuebles;

    public InmueblesAdapter(List<Inmueble> listaInmuebles) {
        this.listaInmuebles = listaInmuebles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inmueble, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Inmueble inmueble = listaInmuebles.get(position);
        holder.tvTitulo.setText(inmueble.getTitulo());
        holder.tvDetalles.setText(inmueble.getTipoOperacion() + " - " + inmueble.getCiudad());
        holder.tvPrecio.setText("$ " + inmueble.getPrecio());
    }

    @Override
    public int getItemCount() {
        return listaInmuebles.size();
    }

    public void actualizarLista(List<Inmueble> nuevaLista) {
        this.listaInmuebles = nuevaLista;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvDetalles, tvPrecio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvDetalles = itemView.findViewById(R.id.tvDetalles);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
        }
    }
}
