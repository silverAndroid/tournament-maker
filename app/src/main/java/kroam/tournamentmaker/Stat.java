package kroam.tournamentmaker;

import java.util.ArrayList;

/**
 * Created by Rushil Perera on 11/23/2015.
 *
 * An instance of Stat represents one particular match statistic. Each instance of Stat is defined
 * when chosen to be "used" in a Tournament.
 */
public class Stat {

    private String key;
    private ArrayList<String> tournamentNames;
    private ArrayList<String> values;

    public Stat() {
        this("");
    }

    public Stat(String key) {
        this(key, new ArrayList<String>(), new ArrayList<String>());
    }

    public Stat(String key, ArrayList<String> tournamentNames, ArrayList<String> values) {
        this.key = key;
        this.tournamentNames = new ArrayList<>(tournamentNames.size());
        this.tournamentNames.addAll(tournamentNames);
        this.values = new ArrayList<>(values.size());
        this.values.addAll(values);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void addTournamentName(String name) {
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

    public void addValue(String value) {
        values.add(value);
    }

    public String getValue(int index) {
        return values.get(index);
    }

    public String getValue(String value) {
        for (String valueKey : values) {
            if (valueKey.equals(value))
                return value;
        }
        return null;
    }

    public ArrayList<String> getValues() {
        return values;
    }
}
