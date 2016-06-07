package kroam.tournamentmaker;

import android.app.Application;

import com.bettervectordrawable.VectorDrawableCompat;

/**
 * Created by Rushil Perera on 12/8/2015.
 */
public class VectorApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VectorDrawableCompat.enableResourceInterceptionFor(true, getResources(), R.drawable.account_check,
                R.drawable.close, R.drawable.information_outline, R.drawable.plus_white);
    }
}
