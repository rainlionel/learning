import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class socketClientdemo {
    public static void main(String[] args) throws Exception{
        //1.创建socket对象（主机目的地）
        Socket sc = new Socket("127.0.0.1", 9000);
        //2.获取传输通道
        OutputStream os=sc.getOutputStream();
        os.write("我们是爱学习的人".getBytes("utf-8"));
        byte [] s=new byte[1024];
        InputStream inp=sc.getInputStream();
        int len=inp.read(s);
        System.out.println(new String(s,0,len));

        sc.close();
}
}
