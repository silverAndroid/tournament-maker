package kroam.tournamentmaker.database;

/**
 * Created by silve on 2016-05-24.
 */

public class MissingColumnException extends RuntimeException {

    public MissingColumnException(String column) {
        super(column);
    }
}
