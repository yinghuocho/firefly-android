package org.gofirefly.firefly_android.service;

import android.net.VpnService;

import org.gofirefly.firefly_android.socks.SocksServer;

import de.measite.minidns.DNSClient;

public class VpnForwarder implements Runnable {
    // InetAddress ip;
    private String ip;

    private int port;

    private VpnService vpn;

    private DNSClient dns;

    private SocksServer server;

    public VpnForwarder(String ip, int port, VpnService vpn) {
        this.ip = ip;
        this.port = port;
        this.vpn = vpn;
        this.dns = new DNSClient(this.vpn);
    }

    public void init() throws Exception {
        this.server = new SocksServer(this.ip, this.port, this.vpn, this.dns);
        this.server.init();
    }

    @Override
    public void run() {
        server.run();
    }

    public void stop() {
        if (this.server != null) {
            this.server.stop();
        }
    }
}
