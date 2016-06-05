package org.gofirefly.firefly_android.socks;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.measite.minidns.DNSMessage;
import de.measite.minidns.Record;

public class SocksConnection implements Runnable {
    private static final String TAG = "SocksConnection";

    private SocketChannel client;

    private SocketChannel remote;

    private Selector selector;

    private SocksServer svr;

    public SocksConnection(SocketChannel client, SocksServer svr) {
        this.client = client;
        this.svr = svr;
    }

    @Override
    public void run() {
        try {
            Socket sock = client.socket();
            sock.setSoTimeout(60000);
            DataInputStream in = new DataInputStream(sock.getInputStream());
            OutputStream out = sock.getOutputStream();
            SocksInitialRequest init = SocksInitialRequest.read(in);
            boolean succ = SocksAuth.auth(init, in, out);
            if (!succ) {
                sock.close();
                return;
            }
            SocksRequest req = SocksRequest.read(in);
            if (req.cmd() != SocksConstants.CONNECT) {
                sock.close();
                return;
            }

            InetSocketAddress dest = req.dest();
            if (dest.isUnresolved()) {
                remote = connectHost(dest);
            } else {
                remote = connectIP(dest, 30000);
            }
            if (remote == null) {
                client.close();
                return;
            }
            SocksResponse resp = new SocksResponse(SocksConstants.SUCC,
                    remote.socket().getLocalAddress(), remote.socket().getLocalPort());
            out.write(resp.encode());
            out.flush();

            selector = Selector.open();
            client.configureBlocking(false);
            ;
            remote.configureBlocking(false);

            client.register(selector, SelectionKey.OP_READ, remote);
            remote.register(selector, SelectionKey.OP_READ, client);
            ByteBuffer buffer = ByteBuffer.allocate(32768);
            long timestamp = System.currentTimeMillis();
            while (true) {
                int num = selector.select(60000);
                if (num == 0) {
                    if ((System.currentTimeMillis() - timestamp) > 60000) {
                        cleanUp();
                        return;
                    } else {
                        continue;
                    }
                }
                timestamp = System.currentTimeMillis();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = (SelectionKey) it.next();
                    SocketChannel r = (SocketChannel) key.channel();
                    SocketChannel w = (SocketChannel) key.attachment();

                    int n = r.read(buffer);
                    if (n == -1) {
                        cleanUp();
                        return;
                    } else if (n > 0) {
                        buffer.flip();
                        w.write(buffer);
                    }
                    buffer.clear();
                    it.remove();
                }
            }
        } catch (Exception e) {
            cleanUp();
        }
    }

    private void cleanUp() {
        try {
            client.close();
            if (remote != null) {
                remote.close();
            }
            if (selector != null) {
                selector.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SocketChannel connectHost(InetSocketAddress dest) throws IOException {
        DNSMessage msg = this.svr.dns.query(dest.getHostName(), Record.TYPE.A, Record.CLASS.IN);
        if (msg == null) {
            return null;
        }
        List<String> ips = new ArrayList<String>();
        for (Record ans : msg.answerSection) {
            ips.add(ans.payloadData.toString());
        }
        for (String ip : ips) {
            try {
                return connectIP(new InetSocketAddress(ip, dest.getPort()), 30000);
            } catch (Exception e) {
            }
        }
        return null;
    }

    private SocketChannel connectIP(InetSocketAddress dest, int timeout) throws IOException {
        SocketChannel remote = SocketChannel.open();
        svr.vpn.protect(remote.socket());
        remote.socket().connect(dest, timeout);
        return remote;
    }
}
