import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpSocketServer {
    public static void main(String[] args)throws Exception {
        ServerSocket ss=new ServerSocket(9000);
        Socket s=ss.accept();
        InetAddress innet=s.getInetAddress();
        System.out.println("请求的客户端地址是："+innet);
        InputStream is= s.getInputStream();
        byte [] by=new byte[1024];
        int len=is.read(by);
        System.out.println(new String(by,0,len));
        OutputStream op=s.getOutputStream();
        op.write("你好，我是服务端".getBytes("utf-8"));

    }
}
