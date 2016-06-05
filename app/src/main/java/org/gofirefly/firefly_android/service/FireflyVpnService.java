package org.gofirefly.firefly_android.service;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.VpnService;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import org.gofirefly.firefly_android.R;

import go.backend.Backend;

public class FireflyVpnService extends VpnService implements Handler.Callback, Runnable {
    private static final int VPN_MTU = 1500;
    private static final String TAG = "FireflyVpnService";

    private PendingIntent mConfigureIntent;

    private Handler mHandler;
    private Thread mThread;
    private VpnForwarder forwarder;

    private ParcelFileDescriptor mInterface;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The handler is only used to show messages.
        if (mHandler == null) {
            mHandler = new Handler(this);
        }

        // Stop the previous session by interrupting the thread.
        if (mThread != null) {
            mThread.interrupt();
            stop();
        }
        mThread = new Thread(this, "ToyVPN");
        mThread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mThread != null) {
            mThread.interrupt();
            stop();
        }
    }

    @Override
    public boolean handleMessage(Message message) {
        if (message != null) {
            Toast.makeText(this, message.what, Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public synchronized void run() {
        try {
            Log.i(TAG, "Starting");
            mHandler.sendEmptyMessage(R.string.connecting);
            _run();
        } catch (Exception e) {
            Log.e(TAG, "Got " + e.toString());
        } finally {
            stop();
            mInterface = null;
            mHandler.sendEmptyMessage(R.string.disconnected);
            Log.i(TAG, "Exiting");
        }
    }

    private void _run() throws Exception {
        Builder builder = new Builder();
        builder.setMtu(VPN_MTU);
        builder.addAddress("10.0.0.1", 28);
        builder.setSession("FireflyVPN");
        builder.addDnsServer("8.8.8.8");
        builder.addRoute("0.0.0.0", 0);

        Log.i(TAG, "start forwarder");
        forwarder = new VpnForwarder("127.0.0.1", 18080, this);
        forwarder.init();
        new Thread(forwarder).start();

        Log.i(TAG, "start socks");
        String tunnelAddr = "https://d288jep9bb0hx9.cloudfront.net,d288jep9bb0hx9.cloudfront.net";
        Backend.StartSocks("127.0.0.1:30800", tunnelAddr, "127.0.0.1:18080");

        // Create a new interface using the builder and save the parameters.
        mInterface = builder.setSession(TAG)
                .setConfigureIntent(mConfigureIntent)
                .establish();
        mHandler.sendEmptyMessage(R.string.connected);
        Log.i(TAG, "run tun2socks");
        Backend.RunTun2Socks("FireflyVPN", mInterface.detachFd(), "127.0.0.1:30800", "8.8.8.8");

    }

    private void stop() {
        try {
            Backend.StopSocks();
            Backend.StopTun2Socks();
            forwarder.stop();
            if (mInterface != null) {
                mInterface.close();
                mInterface = null;
            }
        } catch (Exception e) {
            // ignore
        }
    }
}
