package kroam.tournamentmaker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import java.util.ArrayList;

/**
 * Created by Rushil Perera on 12/3/2015.
 */
public class WinningStatAdapter extends RecyclerView.Adapter<WinningStatAdapter.StatsViewHolder> {

    private ArrayList<Stat> stats;
    private int statSelectedPosition;

    public WinningStatAdapter(ArrayList<Stat> stats) {
        this.stats = new ArrayList<>();
        this.stats.addAll(stats);
    }

    @Override
    public StatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.winning_stat_select_row, parent, false);
        return new StatsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StatsViewHolder holder, int position) {
        holder.stat.setText(stats.get(position).getKey());
        holder.stat.setChecked(position == statSelectedPosition);
    }

    @Override
    public int getItemCount() {
        return stats.size();
    }

    public Stat getWinningStat() {
        return stats.get(statSelectedPosition);
    }

    class StatsViewHolder extends RecyclerView.ViewHolder {
        RadioButton stat;

        public StatsViewHolder(View itemView) {
            super(itemView);
            stat = (RadioButton) itemView.findViewById(R.id.stat);
            stat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    statSelectedPosition = getAdapterPosition();
                    notifyItemRangeChanged(0, stats.size());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stat.setChecked(!stat.isChecked());
                }
            });
        }
    }
}
