package kroam.tournamentmaker;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rushil Perera on 11/23/2015.
 * <p/>
 * An instance of Stat represents one particular match statistic. Each instance of Stat is defined
 * when chosen to be "used" in a Tournament.
 */
public class Stat {

    private String key;
    private ArrayList<String> tournamentNames;
    private HashMap<String, StatValue> values;

    public Stat() {
        this("");
    }

    public Stat(String key) {
        this(key, new ArrayList<String>(), new HashMap<String, StatValue>());
    }

    public Stat(String key, ArrayList<String> tournamentNames, HashMap<String, StatValue> values) {
        this.key = key;
        this.tournamentNames = new ArrayList<>(tournamentNames.size());
        this.tournamentNames.addAll(tournamentNames);
        this.values = new HashMap<>(values.size());
        this.values.putAll(values);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void addTournamentName(String name) {
        if (!tournamentNames.contains(name))
            tournamentNames.add(name);
    }

    public String getTournament(int index) {
        return tournamentNames.get(index);
    }

    public String getTournament(String name) {
        for (String tName : tournamentNames) {
            if (tName.equals(name))
                return name;
        }
        return null;
    }

    public ArrayList<String> getTournamentNames() {
        return tournamentNames;
    }

    public void addValue(StatValue value) {
        values.put(value.toString(), value);
    }

    public StatValue getValue(String value) {
        return values.get(value);
    }

    public HashMap<String, StatValue> getValues() {
        return values;
    }
}
