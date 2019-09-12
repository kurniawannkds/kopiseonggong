package com.androiddevnkds.kopiseong.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentHelper {
    // For first fragment in every activity
    public static void fragmentInitializer(int resource,
                                           FragmentManager fragmentManager,
                                           Fragment fragment,
                                           Bundle bundle) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (bundle != null) fragment.setArguments(bundle);

        fragmentTransaction.add(resource, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void fragmentChanger(int resource,
                                       FragmentManager fragmentManager,
                                       Fragment fragment,
                                       Bundle bundle, boolean addToBackstack) {
        if (fragment != null) {
            if (bundle != null) fragment.setArguments(bundle);
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.replace(resource, fragment);
            if (addToBackstack)
                transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
