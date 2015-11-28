package kroam.tournamentmaker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class TournamentCreateActivity extends AppCompatActivity implements View.OnClickListener {

    StatsAdapter adapter;
    NumberPicker sizePicker;
    RadioGroup typeGroup;
    RadioButton roundRobin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tournament_creation);
        RecyclerView stats;
        Button add;
        Button confirm;
        Button cancel;

        stats = (RecyclerView) findViewById(R.id.stats_list);
        stats.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager(getBaseContext()));
        stats.setAdapter(adapter = new StatsAdapter());

        sizePicker = (NumberPicker) findViewById(R.id.numberPicker);
        sizePicker.setMinValue(1);
        sizePicker.setMaxValue(16);

        typeGroup = (RadioGroup) findViewById(R.id.tournament_type_group);

        roundRobin = (RadioButton) findViewById(R.id.radio_round_robin);
        roundRobin.setChecked(true);

        add = (Button) findViewById(R.id.btn_add);
        add.setOnClickListener(this);

        confirm = (Button) findViewById(R.id.btn_confirm);
        confirm.setOnClickListener(this);

        cancel = (Button) findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                adapter.addItem();
                break;
            case R.id.btn_confirm:
                StatsDataSource.getInstance().addStats(adapter.getItems());
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}
