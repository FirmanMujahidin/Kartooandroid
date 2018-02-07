package co.kartoo.app.drawer;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import co.kartoo.app.R;

/**
 * Created by MartinOenang on 10/11/2015.
 */

@EFragment(R.layout.fragment_social)
public class FragmentFeed extends Fragment{
    @ViewById
    RecyclerView mRVtimeline;
    @ViewById
    SwipeRefreshLayout mSRLrefresh;


    @AfterViews
    public void init() {
        mRVtimeline.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

}
