package co.kartoo.app.landing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EFragment;

import co.kartoo.app.R;

/**
 * Created by teresa on 10/15/2015.
 */

@EFragment
public class LandingSplashFragment extends Fragment{
    public static final String POSITION_KEY = "position";

    public LandingSplashFragment() {
    }

    int mPagePosition;

    //kalau ada yang bisa diklik2 di fragmennya masukin di sini

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mPagePosition = getArguments().getInt(POSITION_KEY);

        int layout_id = R.layout.fragment_landing_2;
        switch (mPagePosition) {
            case 0:
                layout_id = R.layout.fragment_landing_2;
                break;

            case 1:
                layout_id = R.layout.fragment_landing_3;
                break;

            case 2:
                layout_id = R.layout.fragment_landing_4;
                break;
            /*
            case 3:
                layout_id = R.layout.fragment_landing_4;
                break;
                */
        }
        return inflater.inflate(layout_id, container, false);
    }
}
