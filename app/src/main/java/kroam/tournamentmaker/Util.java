package kroam.tournamentmaker;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import java.io.FileNotFoundException;

/**
 * Created by Rushil Perera on 11/24/2015.
 */
public class Util {

    public static final int TOURNAMENT_REQUEST_CODE = 0;
    public static final int TEAM_REQUEST_CODE = 1;
    public static final int IMAGE_LOCAL_CODE = 2;
    public static final int PERMISSION_MANAGE_DOCUMENTS = 3;
    private static final String TAG = "Util";

    public static boolean validateColumn(String column) {
        return !(column == null || column.equals(""));
    }

    public static Bitmap loadImage(ImageView imageView, Uri uri, ContentResolver contentResolver) {
        Bitmap bitmap;
        try {
            imageView.setImageBitmap(bitmap = decodeUri(uri, contentResolver));
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AlertDialog.Builder generateDialog(Context context, String title) {
        return generateDialog(context, title, "");
    }

    public static AlertDialog.Builder generateDialog(Context context, String title, String message) {
        return new AlertDialog.Builder(context/*, R.style.DialogTheme)*/) //Removed use of DialogTheme by Ocean 07/June/2016
                .setTitle(title)
                .setMessage(message);
    }

    //Copied directly from Android Developer website with a few modifications to fit required parameters
    private static Bitmap decodeUri(Uri selectedImage, ContentResolver contentResolver) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(contentResolver.openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 140;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(contentResolver.openInputStream(selectedImage), null, o2);
    }

    /*
    * Instantiates Matches with randomly matched teams that are in <code>tournament</code>.
    * Method currently only does Knockout Format(still requires Round Robin and Combinations)
    *
    */
    /*public static ArrayList<Match> generateMatches(Tournament tournament) {
        ArrayList<Team> teams = TournamentsDataSource.getInstance().getTeamsFromTournament(tournament.getName());
        Collections.shuffle(teams);
        Iterator<Team> teamIterator = teams.listIterator();
        ArrayList<Match> matches = new ArrayList<>();
        Match newMatch;

        switch (tournament.getType()) {
            case TournamentCreateActivity.KNOCKOUT:
                while (teamIterator.hasNext()) {
                    newMatch = new Match(teamIterator.next(), !teamIterator.hasNext() ? null : teamIterator.next(),
                            UUID.randomUUID().toString());
                    matches.add(newMatch);
                    MatchesDataSource.getInstance().createMatch(newMatch);
                }
                break;  //this is used to generate the first round of matches

            case TournamentCreateActivity.ROUND_ROBIN:
                for (int aTeam = 0; aTeam < teams.size() - 1; aTeam++) {
                    for (int otherTeam = aTeam + 1; otherTeam < teams.size(); otherTeam++) {
                        newMatch = new Match(teams.get(aTeam), teams.get(otherTeam), UUID.randomUUID().toString());
                        matches.add(newMatch);
                        MatchesDataSource.getInstance().createMatch(newMatch);
                    }
                }
                break;

            case TournamentCreateActivity.COMBINATION:
                for (int aTeam = 0; aTeam < teams.size() - 1; aTeam++) {
                    for (int otherTeam = aTeam + 1; otherTeam < teams.size(); otherTeam++) {
                        newMatch = new Match(teams.get(aTeam), teams.get(otherTeam), UUID.randomUUID().toString());
                        matches.add(newMatch);
                        MatchesDataSource.getInstance().createMatch(newMatch);
                    }
                }
                break;
            //generates the first round of Combination format, in Round Robin. Next rounds will be held
            //knockout format.
        }
        return matches;

    }

    *//*
    * Method utilized to generate 2nd+ round of a tournament with Knockout or Combination format
    * *//*
    public static ArrayList<Match> generateMatches(Tournament tournament, ArrayList<Team> qualifyingTeams) {
        Match newMatch;
        ArrayList<Match> matches = new ArrayList<>();

        switch (tournament.getType()) {
            case TournamentCreateActivity.ROUND_ROBIN:
                tournament.setCompleted(true);
                TournamentsDataSource.getInstance().updateTournament(tournament);
            default:
                if (qualifyingTeams.size() >= 2) {
                    Iterator<Team> teamIterator = qualifyingTeams.listIterator();
                    while (teamIterator.hasNext()) {
                        newMatch = new Match(teamIterator.next(), teamIterator.hasNext() ? teamIterator.next() :
                                null, UUID.randomUUID().toString());
                        matches.add(newMatch);
                        MatchesDataSource.getInstance().createMatch(newMatch);
                    }
                } else {
                    tournament.setCompleted(true);
                    TournamentsDataSource.getInstance().updateTournament(tournament);
                }
                return matches;
        }
    }

    public static ArrayList<Team> getListOfQualifiers(Tournament tournament, ArrayList<Match> matches, int
            currentRound) {
        ArrayList<Team> winners = new ArrayList<>();

        //qualifiers of the first round (RoundRobin format) of Combination
        if (tournament.getType().equals(TournamentCreateActivity.COMBINATION) && currentRound == 0) {
            ArrayList<Team> teams = tournament.getTeams();
            //positions in this arrays correspond to respective position in the ArrayList<Team> in tournament
            int[] amountOfWins = new int[teams.size()];
            Team[] teamWinInstances = new Team[matches.size()];
            MultiMap<Integer, Team> mapOfWinsAndTeams = new MultiMap<>();

            for (int matchPos = 0; matchPos < matches.size(); matchPos++) {
                teamWinInstances[matchPos] = matches.get(matchPos).getWinner();
            }

            for (Team teamWinInstance : teamWinInstances) {
                for (int i = 0; i < amountOfWins.length; i++) {
                    if (teams.get(i).getName().equals(teamWinInstance.getName())) {
                        amountOfWins[i]++;
                        break;
                    }
                }
            }

            for (int k = 0; k < amountOfWins.length; k++) {
                mapOfWinsAndTeams.put(amountOfWins[k], tournament.getTeams().get(k));
            }

            int swap;
            for (int c = 0; c < (amountOfWins.length - 1); c++) {
                for (int d = 0; d < amountOfWins.length - c - 1; d++) {
                    if (amountOfWins[d] > amountOfWins[d + 1]) *//* For descending order use < *//* {
                        swap = amountOfWins[d];
                        amountOfWins[d] = amountOfWins[d + 1];
                        amountOfWins[d + 1] = swap;
                    }
                }
            }

            //Array that contains the wins of the qualifying teams. Used to get teams that qualify from mapOfWinsAndTeams
            int[] qualifyingWins = new int[generateNumQualifiers(tournament.getTeams())];
            System.arraycopy(amountOfWins, 0, qualifyingWins, 0, qualifyingWins.length);

            for (int i = 0; i < qualifyingWins.length; i++) {
                if (i > 0 && qualifyingWins[i - 1] == qualifyingWins[i]) {
                    continue;
                }
                List<Team> qualifiers = mapOfWinsAndTeams.get(qualifyingWins[i]);
                for (int q = 0; q < qualifiers.size(); q++) {
                    winners.add(qualifiers.get(q));
                }
            }

            return winners;
        }

        for (int aWinner = 0; aWinner < matches.size(); aWinner++) {
            winners.add(matches.get(aWinner).getWinner());
        }
        return winners;
    }

    public static int generateNumQualifiers(ArrayList<Team> roundOfRR) {
        int sizeExponent = 0;
        while (Math.pow(2, sizeExponent) < roundOfRR.size()) {
            sizeExponent++;
        }
        sizeExponent--;
        return (int) Math.pow(2, sizeExponent);
    }

    public static int getRankingOf(Team team) {
        return 0;
    }*/
}
