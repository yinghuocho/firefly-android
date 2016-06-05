package org.gofirefly.firefly_android.socks;

import java.io.DataInputStream;

public class SocksInitialRequest {
    private int version;

    private int nmethod;

    private byte[] methods;

    static public SocksInitialRequest read(DataInputStream in) throws Exception {
        SocksInitialRequest init = new SocksInitialRequest();
        init.version = in.readUnsignedByte();
        if (init.version != SocksConstants.V5) {
            throw new Exception("unsupported version");
        }
        init.nmethod = in.readUnsignedByte();
        init.methods = new byte[init.nmethod];
        in.readFully(init.methods, 0, init.nmethod);
        return init;
    }
}
