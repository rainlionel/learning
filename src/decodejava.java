import com.datech.DaToSHCA.Base64;

import java.nio.charset.Charset;

public class decodejava {
    public static void main(String[] args) {

        String encodcert="MIIDFDCCArmgAwIBAgIQYHgauuCmO+vvOCBq45oimzAMBggqgRzPVQGDdQUAMIGM\n" +
                "MQswCQYDVQQGEwJDTjEQMA4GA1UECAwHQkVJSklORzEOMAwGA1UEBwwFQkVUREEx\n" +
                "DjAMBgNVBAoMBUNJRUNDMRowGAYDVQQLDBFHRkEgVFJVU1QgTkVUV09SSzEvMC0G\n" +
                "A1UEAwwmUk9PVCBDRVJUSUZJQ0FURSBGT1IgR0ZBIFRSVVNUIE5FVFdPUkswHhcN\n" +
                "MTkxMjIwMDYwMDUyWhcNMjAxMjE5MDYwMDUyWjCBuzEXMBUGA1UEAwwOMjMwMzAx\n" +
                "MDAwMDEwMDYxLTArBgNVBAsMJOm4oeilv+W4guiQpeWVhueOr+Wig+W7uuiuvueb\n" +
                "keedo+WxgDE/MD0GA1UECgw2MTEyMzAzMDBNQjE2NjIzOTkz6bih6KW/5biC6JCl\n" +
                "5ZWG546v5aKD5bu66K6+55uR552j5bGAMQ8wDQYDVQQHDAbpuKHopb8xEjAQBgNV\n" +
                "BAgMCem7kem+meaxnzELMAkGA1UEBhMCQ04wWTATBgcqhkjOPQIBBggqgRzPVQGC\n" +
                "LQNCAAQDaYi7GmfKjoGSYCG5xfLYvkrci2NBTBkmRh+zHyd2lMfIXrXiWxGAK3Ge\n" +
                "E8J2BMHcFTjFX5tc2MOa+z6ljTEWo4HJMIHGMHcGA1UdHwRwMG4wNaAzoDGGL2h0\n" +
                "dHA6Ly9nZmFjYS5nZmFwa2kuY29tLmNuL2dmYS9jcmwvY3JsMzBTTTIuY3JsMDWg\n" +
                "M6Axhi9odHRwOi8vZ2ZhY2EuZ2ZhcGtpLmNvbS5jbi9nZmEvY3JsL2NybDMwU00y\n" +
                "LmNybDAdBgNVHQ4EFgQUpuThv+SbFO6HnO6XCrU6xR463cQwHwYDVR0jBBgwFoAU\n" +
                "a4zCNOCMDOhQU2MEbURnHYkgCQowCwYDVR0PBAQDAgQwMAwGCCqBHM9VAYN1BQAD\n" +
                "RwAwRAIgdHv59ihRjT2t13arO3oOQjtHBu9IJ1cwOOgt9ztbtCkCIGZarR/wEzUx\n" +
                "lVfxeUn6liDpd8Qia0GB74zb9Pc4UQN0";

        byte []data1=encodcert.getBytes();
           byte [] decodata=  Base64.decode(data1);
           String mw=new String(decodata);
        String clpierpkey="BAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=";
              byte[] clip=Base64.decode(clpierpkey);
              String mw2=new String(clip);
        System.out.println("比对结果是"+(mw.equals(mw2)));

    }
}
