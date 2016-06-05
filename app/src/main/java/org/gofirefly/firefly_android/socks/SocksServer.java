package org.gofirefly.firefly_android.socks;

import android.net.VpnService;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import de.measite.minidns.DNSClient;

public class SocksServer implements Runnable {
    private static final String TAG = "SocksServer";

    public VpnService vpn;

    public DNSClient dns;

    private String ip;

    private int port;

    private ServerSocketChannel ss;

    public SocksServer(String ip, int port, VpnService vpn, DNSClient dns) {
        this.ip = ip;
        this.port = port;
        this.vpn = vpn;
        this.dns = dns;
    }

    public void init() throws Exception {
        ss = ServerSocketChannel.open();
        ss.socket().setReuseAddress(true);
        ss.socket().bind(new InetSocketAddress(InetAddress.getByName(ip), port));
    }

    @Override
    public void run() {
        try {
            while (true) {
                SocketChannel client = ss.accept();
                Log.i(TAG, "new connection: " + client.toString());
                SocksConnection con = new SocksConnection(client, this);
                new Thread(con).start();
            }
        } catch (Exception e) {
        }
    }

    public void stop() {
        if (ss != null) {
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
