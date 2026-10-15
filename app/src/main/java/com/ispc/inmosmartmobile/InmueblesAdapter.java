package com.ispc.inmosmartmobile;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

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

        String ciudad = inmueble.getCiudad();
        if (ciudad == null || ciudad.trim().isEmpty()) {
            holder.tvDetalles.setText(inmueble.getTipoOperacion());
        } else {
            holder.tvDetalles.setText(inmueble.getTipoOperacion() + " - " + ciudad);
        }

        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("es", "AR"));
        nf.setMaximumFractionDigits(0);
        holder.tvPrecio.setText("$ " + nf.format(inmueble.getPrecio()));

        Glide.with(holder.ivFoto.getContext())
                .load(inmueble.getImagen())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .centerCrop()
                .into(holder.ivFoto);

        holder.itemView.setOnClickListener(v -> {
            try {
                Intent i = new Intent(v.getContext(), DetallePropiedadActivity.class);
                i.putExtra("propiedad_id", Integer.parseInt(inmueble.getId()));
                v.getContext().startActivity(i);
            } catch (NumberFormatException ignored) { }
        });
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
        ImageView ivFoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvDetalles = itemView.findViewById(R.id.tvDetalles);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            ivFoto = itemView.findViewById(R.id.ivFoto);
        }
    }
}