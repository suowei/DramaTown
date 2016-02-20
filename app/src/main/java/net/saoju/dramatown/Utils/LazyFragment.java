package net.saoju.dramatown.Utils;

import android.support.v4.app.Fragment;

public abstract class LazyFragment extends Fragment {

    protected boolean isPrepared;
    protected boolean hasLoadedOnce;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isPrepared && !hasLoadedOnce) {
            load();
            hasLoadedOnce = true;
        }
    }

    protected abstract void load();

    public void refresh() {
        if(getUserVisibleHint() && isPrepared) {
            load();
        } else {
            hasLoadedOnce = false;
        }
    }
}
