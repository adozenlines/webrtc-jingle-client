package com.tuenti.voice.core.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Used to lock in a network connection, cellular or wifi during a call.
 * A change in network, disconnect from xmpp server, results in a call
 * terminating, and this code helps mitigate that problem.
 *
 */
public class NetworkPreference {

    protected ConnectivityManager mConnManager;
    protected int networkPreference;
    protected boolean networkPreferenceEnabled;

    public NetworkPreference(Context context){
        networkPreferenceEnabled = false;
        mConnManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public ConnectivityManager getConnectivityManager(){
        return mConnManager;
    }

    public int getActiveNetwork(){
        NetworkInfo networkInfo = mConnManager.getActiveNetworkInfo();
        int returnVal = -1;
        if (networkInfo != null) {
            returnVal = networkInfo.getType();
        }
        return returnVal;
    }

    /**
     * Call when you start a call to lock your network preference to current network.
     */
    public synchronized void enableStickyNetworkPreference(){
        if (networkPreferenceEnabled == false && mConnManager != null) {
            networkPreference = mConnManager.getNetworkPreference();
            int activeNetwork = getActiveNetwork();
            if (activeNetwork > -1) {
                mConnManager.setNetworkPreference(getActiveNetwork());
                networkPreferenceEnabled = true;
            }
        }
    }

    /**
     * Call when you end a call to restore network preference.
     */
    public synchronized void unsetNetworkPreference(){
        if(networkPreferenceEnabled == true && mConnManager != null) {
            // Note: Do not set to DEFAULT_NETWORK_PREFERENCE.
            // On Nexus4 results in no data connection.
            mConnManager.setNetworkPreference(networkPreference);
            networkPreferenceEnabled = false;
        }
    }
}
