package com.ispc.inmosmartmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TurnosAdapter extends RecyclerView.Adapter<TurnosAdapter.TurnoViewHolder> {

    private Context context;
    private List<TurnoModel> listaTurnos;
    private OnTurnoCancelListener cancelListener;

    public interface OnTurnoCancelListener {
        void onCancelar(int idTurno);
    }

    public TurnosAdapter(Context context, List<TurnoModel> listaTurnos, OnTurnoCancelListener cancelListener) {
        this.context = context;
        this.listaTurnos = listaTurnos;
        this.cancelListener = cancelListener;
    }

    @NonNull
    @Override
    public TurnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_turno, parent, false);
        return new TurnoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TurnoViewHolder holder, int position) {
        TurnoModel turno = listaTurnos.get(position);

        if (turno.getPropiedadDetalle() != null) {
            holder.tvPropiedad.setText(turno.getPropiedadDetalle().getTitulo());
        } else {
            holder.tvPropiedad.setText("Propiedad sin título");
        }

        holder.tvFechaHora.setText("Fecha y Hora: " + turno.getFechaHora());
        holder.tvEstado.setText("Estado: " + turno.getEstado());

        holder.btnCancelar.setOnClickListener(v -> {
            if (cancelListener != null) {
                cancelListener.onCancelar(turno.getIdTurno());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaTurnos != null ? listaTurnos.size() : 0;
    }

    public static class TurnoViewHolder extends RecyclerView.ViewHolder {
        TextView tvPropiedad, tvFechaHora, tvEstado;
        Button btnCancelar;

        public TurnoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPropiedad = itemView.findViewById(R.id.tvPropiedadTurno);
            tvFechaHora = itemView.findViewById(R.id.tvFechaHoraTurno);
            tvEstado = itemView.findViewById(R.id.tvEstadoTurno);
            btnCancelar = itemView.findViewById(R.id.btnCancelarTurno);
        }
    }
}