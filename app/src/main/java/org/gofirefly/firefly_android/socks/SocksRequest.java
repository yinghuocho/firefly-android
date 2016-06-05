package org.gofirefly.firefly_android.socks;

import java.io.DataInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class SocksRequest {
    private int cmd;

    private int addrType;

    private byte[] addr;

    private int port;

    public static SocksRequest read(DataInputStream in) throws Exception {
        int version = in.readUnsignedByte();
        if (version != SocksConstants.V5) {
            throw new Exception("unsupported version");
        }
        int cmd = in.readUnsignedByte();
        in.readUnsignedByte();
        int addrType = in.readUnsignedByte();
        SocksRequest req = new SocksRequest();
        req.cmd = cmd;
        req.addrType = addrType;
        switch (addrType) {
            case SocksConstants.IPv4:
                req.addr = new byte[4];
                in.readFully(req.addr, 0, 4);
                break;
            case SocksConstants.IPv6:
                req.addr = new byte[16];// I believe it is 16 bytes,huge!
                in.readFully(req.addr, 0, 16);
                break;
            case SocksConstants.NAME:
                int len = in.readUnsignedByte();
                req.addr = new byte[len];// Next byte shows the length
                in.readFully(req.addr, 0, len);
                break;
            default:
                throw new Exception("unknown address type");
        }
        req.port = in.readUnsignedShort();
        return req;
    }

    public int cmd() {
        return cmd;
    }

    public InetSocketAddress dest() throws UnknownHostException {
        switch (addrType) {
            case SocksConstants.IPv4:
                return new InetSocketAddress(InetAddress.getByAddress(this.addr), this.port);
            case SocksConstants.IPv6:
                return new InetSocketAddress(InetAddress.getByAddress(this.addr), this.port);
            case SocksConstants.NAME:
                return InetSocketAddress.createUnresolved(new String(this.addr), this.port);
        }
        return null;
    }
}
