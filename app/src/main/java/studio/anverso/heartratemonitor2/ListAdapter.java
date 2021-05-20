package studio.anverso.heartratemonitor2;

import android.content.Context;
//import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
//Con esta clase asignamos los valores de las mediciones en la lista que mostrará toda la información
public class ListAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtLatidos, txtFecha, txtHora;

    public ListAdapter(@NonNull View itemView){
        super(itemView);

        txtLatidos = itemView.findViewById(R.id.latidosTextView);
        txtFecha = itemView.findViewById(R.id.dateTextView);
        txtHora = itemView.findViewById(R.id.timeTextView);

    }


    @Override
    public void onClick(View v) {

    }
}
