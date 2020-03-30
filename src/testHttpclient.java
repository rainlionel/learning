import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


public class testHttpclient {

public static void toSeal()throws Exception{
    //创建httpclient
   CloseableHttpClient httpClient=HttpClientBuilder.create().build();
   //拼装参数
    //String xmlStr=getfile();
    String xmlStr=getxml();
    StringBuffer param=new StringBuffer();
    try {
        param.append("xmlStr=" + URLEncoder.encode(xmlStr, "utf-8"));
    }catch (UnsupportedEncodingException ee){
        ee.printStackTrace();
    }
    //创建httpPost
    HttpPost httpPost = new HttpPost("http://127.0.0.1:9231/pdfconvert/general/docsToOfd.jsp?"+param);
    CloseableHttpResponse response=null;
    try {
        //执行httppost请求
        response=httpClient.execute(httpPost);
        System.out.println("请求响应状态："+response.getStatusLine());
        //获取响应的实体
        HttpEntity responseEntity=response.getEntity();
        //将响应实体转为String
        String backxml=EntityUtils.toString(responseEntity);
        System.out.println("请求响应报文："+backxml);
        Document doc=null;
        InputStream is=null;
        try {
            //解析xml报文
            doc= DocumentHelper.parseText(backxml);
            //获取根节点
            Element seal= doc.getRootElement();
            Element treenode=seal.element("TREE_NODE");
            String retcode=treenode.elementText("ret_code");
            System.out.println("返回的值为:"+retcode);
            //获取盖章返回信息
            String retCode=seal.elementText("RET_CODE");
            System.out.println("返回的值是："+retCode);
            if((Integer.parseInt(retCode))!=1){
                System.out.println(seal.elementText("RET_MSG"));
                return;
            }
            //获取文件下载地址
            String fileurl= seal.element("FILE_LIST").element("FILE").elementText("FILE_URL");
            //使用流的方式下载文件
             is=new URL(fileurl).openConnection().getInputStream();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
            //将输入流转为缓冲输入流
            BufferedInputStream bis=new BufferedInputStream(is);
           //创建文件夹及文件名称
            Date d=new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String da=sdf.format(d);
            String path="D://ofdwork/";
            File file=new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
            String filepath=path+da+".ofd";
            File file2=new File(filepath);
            if(!file2.exists()){
                file2.createNewFile();
            }
            byte [] buf=new byte[2048];
            //创建输出流
            FileOutputStream fos=new FileOutputStream(filepath);
            BufferedOutputStream bos=new BufferedOutputStream(fos);
            int len = 0;
            //保存文件
            while((len=bis.read(buf))!=-1)
            {
                fos.write(buf, 0, len);
            }
            bis.close();
            is.close();
            bos.close();
            fos.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    /**
     * 读取本地的报文文件
     * @return
     */
    public static   String getfile() {
        int length = 0;
        FileReader fir=null;
        try {
            fir = new FileReader("D://1.txt");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        char zh [] =null;
        zh=new char[2048];
        try {
            length=fir.read(zh);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String xmlStr=new String(zh,0,length);
       // System.out.println(xmlStr);

        return xmlStr;
    }

    /**
     * 获取文件的base64
     * @param filepath
     * @return
     */
    public static String getFilebase(String filepath){
    File file=new File(filepath);
        InputStream  ins=null;
        try {
            ins=new FileInputStream(file);
            byte [] filedata=new byte[(int)file.length()];
            ins.read(filedata);
            ins.close();

            return new BASE64Encoder().encode(filedata);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return  null;
    }

    /**
     * 创建盖章请求的xml
     * @return
     */
    public static String getxml(){
        String StringBase=getFilebase("D://test.pdf");
        StringBuffer  sb=new StringBuffer();
        sb.append("<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\" ?>");
        sb.append("<SEAL_DOC_REQUEST>");
//        //sb.append("<BASE_DATA>");
//        sb.append("<SYS_ID>").append("sysId").append("</SYS_ID>");
//        sb.append("<SYS_PWD>").append(123456).append("</SYS_PWD>");
//        sb.append("</BASE_DATA>");
//        sb.append("<META_DATA>");
//        sb.append("<IS_MERGER>").append(false).append("</IS_MERGER>");
//        sb.append("<FILE_NO>").append("test.ofd").append("</FILE_NO>");
//        sb.append("<IS_CODEBAR>").append(false).append("</IS_CODEBAR>");
//        sb.append("<RULE_TYPE>").append(0).append("</RULE_TYPE>");
//        sb.append("<RULE_NO>").append("test2").append("</RULE_NO>");
//        sb.append("</META_DATA>");
        sb.append("<FILE_LIST>");
        sb.append("<TREE_NODE>");
//        sb.append("<RULE_TYPE>").append(0).append("</RULE_TYPE>");
//        sb.append("<RULE_NO>").append("test2").append("</RULE_NO>");
       // sb.append("<CJ_TYPE>").append("base64").append("</CJ_TYPE>");
        sb.append("<FILE_TYPE>").append("word").append("</FILE_TYPE>");
        sb.append("<CJ_TYPE>").append("file").append("</CJ_TYPE>");
       // sb.append("<MODEL_NAME>").append(StringBase).append("</MODEL_NAME>");
        sb.append("<FILE_PATH>").append("http://127.0.0.1:9231/pdfconvert/test1.doc").append("</FILE_PATH>");
      //  sb.append("<FILE_NO>").append("des.ofd").append("</FILE_NO>");
        sb.append("<FILE_NO>").append("des.ofd").append("</FILE_NO>");
        sb.append("</TREE_NODE>");
        sb.append("</FILE_LIST>");
        sb.append("</SEAL_DOC_REQUEST>");
        return sb.toString();
    }

    public static void main(String[] args)throws Exception {
      toSeal();
       /* String data=getFilebase("D://test.pdf");
        InputStream ins=new ByteArrayInputStream(data.getBytes("utf-8"));
        byte [] in=new byte[data.length()];
        FileOutputStream fos=new FileOutputStream("D:/2.txt");
        BufferedOutputStream bos=new BufferedOutputStream(fos);
        int len=0;
        while((len=ins.read(in))!=-1){
            bos.write(in,0,len);
        }
        ins.close();
        bos.close();
        fos.close();*/
    }

}
