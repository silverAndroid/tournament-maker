package kroam.tournamentmaker;

/**
 * Created by Rushil Perera on 11/23/2015.
 */
public class StatsDataSource {
    private static StatsDataSource instance = new StatsDataSource();

    public static StatsDataSource getInstance() {
        return instance;
    }

    private StatsDataSource() {
    }


}
