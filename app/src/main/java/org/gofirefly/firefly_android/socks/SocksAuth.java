package org.gofirefly.firefly_android.socks;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SocksAuth {
    public static boolean auth(SocksInitialRequest init, DataInputStream in, OutputStream out) throws IOException {
        byte resp[] = new byte[2];
        resp[0] = (byte)SocksConstants.V5;
        resp[1] = (byte)SocksConstants.NONE_AUTH;
        out.write(resp);
        out.flush();
        return true;
    }
}
