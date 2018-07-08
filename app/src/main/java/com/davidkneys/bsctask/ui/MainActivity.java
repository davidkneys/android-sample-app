package com.davidkneys.bsctask.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;

import com.davidkneys.bsctask.R;
import com.davidkneys.bsctask.ui.detail.NoteDetailFragment;
import com.davidkneys.bsctask.ui.notelist.NotesFragment;
import com.davidkneys.bsctask.utils.BaseActivity;

/**
 * Entry point of the whole Application.
 * Whole app is built with one Activity, many fragments pattern in mind.
 */
public class MainActivity extends BaseActivity {

    private Snackbar snackbar = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injector().inject(this);
        setContentView(R.layout.activity_main);

        subscribeNavigation();
        subscribe(commonState().observeOnlineState().subscribe(
                online -> {
                    if (!online) {
                        snackbar = Snackbar
                                .make(findViewById(android.R.id.content), R.string.check_internet, Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.check_internet_refresh, v -> {
                                    commonState().refreshData();
                                })
                                .setActionTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));

                        snackbar.show();
                    } else if (snackbar != null) {
                        snackbar.dismiss();
                    }
                }
        ));
    }

    private void subscribeNavigation() {
        //we don't wanna mess with fragment manager backstack, this way the navigator logic can
        // be pushed to ViewModel level, where it supposed to be, the implementation of navigation
        // is really trivial here, in future exchange it to stable version of Android Navigator library
        //TODO change it to official Android Navigator library when stable
        subscribe(commonState().currentScreen().subscribe(screen -> {
            switch (screen) {
                case MAIN:
                    replaceScreen(CommonState.Screen.MAIN, new NotesFragment());
                    break;
                case DETAIL:
                    replaceScreen(CommonState.Screen.DETAIL, new NoteDetailFragment());
                    break;
            }
        }));
    }

    /**
     * Helper function for replacing a screen, but only when the screen is not already there what
     * happens for example when Activity is recreated (orientation change) and Android Framework
     * itself recreate last Fragment
     */
    private void replaceScreen(CommonState.Screen screen, Fragment fragment) {
        if (getSupportFragmentManager().findFragmentByTag(screen.name()) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentPlaceholder, fragment, screen.name())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (!commonState().backPressed()) {
            super.onBackPressed();
        }
    }
}
