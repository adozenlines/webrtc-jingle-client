import com.tuenti.voice.core.util.NetworkPreference;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowApplication;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

//import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.*;
import static org.hamcrest.CoreMatchers.*;

import android.content.Context;
import android.net.ConnectivityManager;

@RunWith(RobolectricTestRunner.class)
public class NetworkPreferenceTest {
    private Context context;
    private NetworkPreference networkPreference;
    private ConnectivityManager connectivityManager;
    private int originalPreference;

    @Before
    public void setUp() throws Exception {
        context = Robolectric.getShadowApplication().getApplicationContext();
        networkPreference = new NetworkPreference(context);
        connectivityManager = networkPreference.getConnectivityManager();
        connectivityManager.setNetworkPreference(ConnectivityManager.TYPE_WIFI);
        originalPreference = connectivityManager.getNetworkPreference();
    }

    @Test
    public void testEnablePreference() throws Exception {
        assertThat(ConnectivityManager.TYPE_WIFI, equalTo(originalPreference));
        NetworkPreference npSpy = spy(networkPreference);

        doReturn(ConnectivityManager.TYPE_MOBILE).when(npSpy).getActiveNetwork();
        // Enables MOBILE as preferred network.
        npSpy.enableStickyNetworkPreference();

        assertEquals(connectivityManager.getNetworkPreference(), npSpy.getActiveNetwork());
    }

    @Test
    public void testEnableUnsetPreference() throws Exception {
        int preferenceBefore = connectivityManager.getNetworkPreference();

        NetworkPreference npSpy = spy(networkPreference);
        // Simulate being on a mobile network.
        doReturn(ConnectivityManager.TYPE_MOBILE).when(npSpy).getActiveNetwork();
        npSpy.enableStickyNetworkPreference();

        // Verify we're now pref mobile.
        assertEquals(connectivityManager.getNetworkPreference(), ConnectivityManager.TYPE_MOBILE);

        // Back to original value.
        npSpy.unsetNetworkPreference();
        assertEquals(connectivityManager.getNetworkPreference(), preferenceBefore);
    }
}
