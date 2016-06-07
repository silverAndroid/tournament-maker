package kroam.tournamentmaker.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import kroam.tournamentmaker.R;
import kroam.tournamentmaker.Stat;
import kroam.tournamentmaker.org.droidparts.widget.ClearableEditText;

/**
 * Created by Rushil Perera on 11/26/2015.
 */
public class StatsAdapter extends RecyclerView.Adapter {

    private ArrayList<Stat> stats;
    private String tournamentName;
    private int winningStatPosition;
    private boolean onBind;

    public StatsAdapter() {
        stats = new ArrayList<>();
    }

    public StatsAdapter(String tournamentName) {
        stats = new ArrayList<>();
        this.tournamentName = tournamentName;
    }

    public StatsAdapter(ArrayList<Stat> stats) {
        this.stats = new ArrayList<>();
        addItems(stats);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_new_stat, parent, false);
            return new NewStatViewHolder(v);
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_stat, parent, false);
        return new StatsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderParent, int position) {
        if (holderParent instanceof StatsViewHolder) {
            onBind = true;
            StatsViewHolder holder = (StatsViewHolder) holderParent;
            final Stat stat = stats.get(position);
            holder.stat.setText(stat.getKey());
            holder.winningStatSelect.setChecked(position == winningStatPosition);
            onBind = false;
        }
    }

    public void setTournamentName(String name) {
        tournamentName = name;
        for (Stat stat : stats) {
            stat.setTournamentName(name);
        }
    }

    public int getWinningStatPosition() {
        return winningStatPosition;
    }

    @Override
    public int getItemCount() {
        return stats.size() + 1;
    }

    public void addItem() {
        Stat stat = new Stat();
        if (tournamentName != null)
            stat.setTournamentName(tournamentName);
        stats.add(new Stat());
        notifyItemInserted(stats.size() - 1);
    }

    public void addItems(ArrayList<Stat> stats) {
        int initialSize = this.stats.size();
        if (tournamentName != null)
            for (Stat stat : stats) {
                stat.setTournamentName(tournamentName);
            }
        this.stats.addAll(stats);
        notifyItemRangeInserted(initialSize, stats.size());
    }

    public void deleteItem(int position) {
        stats.remove(position);
        notifyItemRemoved(position);
    }

    public ArrayList<Stat> getItems() {
        for (Stat stat : stats) {
            //removing empty keys
            if (stat.getKey().equals(""))
                stats.remove(stat);
        }
        return stats;
    }

    @Override
    public int getItemViewType(int position) {
        return position == stats.size() ? 1 : 0;
    }

    class StatsViewHolder extends RecyclerView.ViewHolder {

        ClearableEditText stat;
        RadioButton winningStatSelect;

        public StatsViewHolder(View view) {
            super(view);
            stat = (ClearableEditText) view.findViewById(R.id.stat);
            winningStatSelect = (RadioButton) view.findViewById(R.id.winning_stat_select);
            stat.requestFocus();
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
            winningStatSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && !onBind) {
                        winningStatPosition = getAdapterPosition();
                        notifyItemRangeChanged(0, stats.size());
                    }
                }
            });
        }
    }

    class NewStatViewHolder extends RecyclerView.ViewHolder {

        TextView stat;

        public NewStatViewHolder(View itemView) {
            super(itemView);
            stat = (TextView) itemView.findViewById(R.id.new_stat);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addItem();
                }
            });
        }
    }
}
