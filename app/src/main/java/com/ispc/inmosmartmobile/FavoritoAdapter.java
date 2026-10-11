package com.ispc.inmosmartmobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoritoAdapter extends RecyclerView.Adapter<FavoritoAdapter.FavoritoViewHolder> {

    public interface OnFavoritoListener {
        void onQuitar(Favorito favorito, int position);
        void onAbrir(Favorito favorito);
    }

    private final List<Favorito> favoritos;
    private final OnFavoritoListener listener;

    public FavoritoAdapter(List<Favorito> favoritos, OnFavoritoListener listener) {
        this.favoritos = favoritos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorito, parent, false);
        return new FavoritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritoViewHolder holder, int position) {
        Favorito favorito = favoritos.get(position);
        holder.imgFavorito.setImageResource(favorito.getImagen());
        holder.tvTitulo.setText(favorito.getTitulo());
        holder.tvPrecio.setText(favorito.getPrecio());

        holder.itemView.setOnClickListener(v -> listener.onAbrir(favorito));
        holder.btnQuitar.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                listener.onQuitar(favorito, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoritos.size();
    }

    public static class FavoritoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFavorito;
        TextView tvTitulo, tvPrecio;
        ImageButton btnQuitar;

        public FavoritoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFavorito = itemView.findViewById(R.id.imgFavorito);
            tvTitulo = itemView.findViewById(R.id.tvTituloFavorito);
            tvPrecio = itemView.findViewById(R.id.tvPrecioFavorito);
            btnQuitar = itemView.findViewById(R.id.btnQuitarFavorito);
        }
    }
}