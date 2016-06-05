package org.gofirefly.firefly_android.socks;

import java.net.InetAddress;

public class SocksResponse {
    private int status;

    private InetAddress addr;

    private int port;

    public SocksResponse(int status, InetAddress addr, int port) {
        this.status = status;
        this.addr = addr;
        this.port = port;
    }

    public byte[] encode() {
        byte[] addrBytes = addr.getAddress();
        byte[] ret = new byte[6 + addrBytes.length];

        ret[0] = (byte) SocksConstants.V5;
        ret[1] = (byte) status;
        ret[2] = (byte) 0;
        if (addrBytes.length == 4) {
            ret[3] = (byte) SocksConstants.IPv4;
        } else {
            ret[3] = (byte) SocksConstants.IPv6;
        }
        System.arraycopy(addrBytes, 0, ret, 4, addrBytes.length);
        ret[4+addrBytes.length] = (byte)((port>>8)&0xFF);
        ret[4+addrBytes.length+1] = (byte)(port&0xFF);
        return ret;
    }
}
