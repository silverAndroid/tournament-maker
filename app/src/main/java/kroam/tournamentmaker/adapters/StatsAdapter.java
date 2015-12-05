package kroam.tournamentmaker.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

import kroam.tournamentmaker.R;
import kroam.tournamentmaker.Stat;

/**
 * Created by Rushil Perera on 11/26/2015.
 */
public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatsViewHolder> {

    private ArrayList<Stat> stats;
    private String tournamentName;

    public StatsAdapter() {
        stats = new ArrayList<>();
        stats.add(new Stat());
    }

    public StatsAdapter(String tournamentName) {
        stats = new ArrayList<>();
        Stat emptyStat = new Stat();
        emptyStat.addTournamentName(tournamentName);
        this.tournamentName = tournamentName;
        stats.add(emptyStat);
    }

    public StatsAdapter(ArrayList<Stat> stats) {
        this.stats = new ArrayList<>();
        this.stats.addAll(stats);
    }

    @Override
    public StatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_creation_row, parent, false);
        return new StatsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StatsViewHolder holder, final int position) {
        final Stat stat = stats.get(position);
        holder.stat.setText(stat.getKey());
    }

    public void setTournamentName(String name) {
        tournamentName = name;
        for (Stat stat : stats) {
            stat.addTournamentName(name);
        }
    }

    @Override
    public int getItemCount() {
        return stats.size();
    }

    public void addItem() {
        if (tournamentName == null)
            stats.add(new Stat());
        else {
            Stat stat = new Stat();
            stat.addTournamentName(tournamentName);
            stats.add(stat);
        }
        notifyItemInserted(stats.size());
    }

    public void deleteItem(int position) {
        stats.remove(position);
        notifyItemRemoved(position);
    }

    public ArrayList<Stat> getItems() {
        for (Stat stat : stats) {
            if (stat.getKey().equals(""))
                stats.remove(stat);
        }
        return stats;
    }

    class StatsViewHolder extends RecyclerView.ViewHolder {
        EditText stat;
        ImageButton delete;

        public StatsViewHolder(View view) {
            super(view);
            stat = (EditText) view.findViewById(R.id.stat);
            stat.requestFocus();
            delete = (ImageButton) view.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(getAdapterPosition());
                }
            });
            stat.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable newText) {
                    stats.set(getAdapterPosition(), new Stat(newText.toString()));
                }
            });
        }
    }
}
