import java.rmi.RemoteException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.xml.namespace.QName;


public class Test {

	private String ip = "127.0.0.1";
	private String port = "9231";

	
	/**
	 * @param args
	 * @throws Exception 
	 * @throws RemoteException 
	 */
	
	public static void main(String[] args) throws RemoteException, Exception {
		StringBuffer s1 = new StringBuffer();
		s1.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>").append("\r\n");
		s1.append("<SealDocRequest>").append("\r\n");
		s1.append("<FILE_LIST>").append("\r\n");
		s1.append("<TREE_NODE>").append("\r\n");
		s1.append("<FILE_NO>").append("\r\n");
		s1.append("test1");
		s1.append("</FILE_NO>").append("\r\n");
		s1.append("<FILE_TYPE>").append("\r\n");
		s1.append("word");
		s1.append("</FILE_TYPE>").append("\r\n");
		s1.append("<FILE_PATH>").append("\r\n");
		s1.append("http://127.0.0.1:9231/pdfconvert/test1.doc");
		s1.append("</FILE_PATH>").append("\r\n");
		s1.append("</TREE_NODE>").append("\r\n");
		s1.append("</FILE_LIST>").append("\r\n");
		s1.append("</SealDocRequest>").append("\r\n");
		Test obj=new Test();
		String resp=obj.docsToPdf(s1.toString());
		System.out.println(resp);
	}
	/**
	 * �ĵ�ת��
	 * 
	 * @param xmlStr
	 * @return
	 * @throws Exception
	 */
	public String docsToPdf(String xmlStr) throws Exception {
		try {
			String wsdlUrl = "http://"+ip+":"+port+"/pdfconvert/services/ConvertService?wsdl";
			String nameSpaceUri = "http://webs";

			Service service = new Service();
			Call call;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(wsdlUrl));
			call.setOperationName(new QName(nameSpaceUri, "docsToOfd"));
			String s = (String) call.invoke(new Object[] { xmlStr });
			System.out.println(s);
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	
}
