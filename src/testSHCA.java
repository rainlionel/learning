//package test;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import com.datech.DaToSHCA.*;

import java.math.BigInteger;

public class testSHCA {
	public static da_handle	tmpHandle = new da_handle();
	public static DaToSHCA		tmpTest = new DaToSHCA();

	public static byte[]		HashData;
	public static String		SignData;

	public static void mainHandle() {
		System.out.println("/*---------------------*/");
		System.out.println("/* 1. SM3??????		");
		System.out.println("/* 2. SM2???			");
		System.out.println("/* 3. SM2???			");
		System.out.println("/* 4. SM2?????		");
		System.out.println("/* 5. SM4?????		");
		System.out.println("/* 6. SM1?????		");
		System.out.println("/* 7. ???????SM4?????      ");
		System.out.println("/* 8. ???????SM1?????      ");
		System.out.println("/* 9. ????????		");
		System.out.println("/* 10. ???????????????	");
		System.out.println("/* 11. ?????SM2??????");
		System.out.println("/* 12. ?????SM2?????	");
		System.out.println("/* 13. ?????SM3	    ");
		System.out.println("/* 14. ???????	    ");
		System.out.println("/* 15. ??????	    ");
		System.out.println("/* 16. ?????????????ECC???????	    ");
		System.out.println("/* 17. ???Base64????????	    ");
		System.out.println("/* 0. exit			");
		System.out.println("/*---------------------*/");
	}
	
	 /** 
     * ?????????????intToBase64?????? 
     **/  
     private static int base64Toint(char c) {  
        int index = base64Toint[c];  
        if(index < 0) {  
            throw new IllegalArgumentException("Illegal character " + c);  
        }  
        return index;  
    }  
	
	 /**     
	    * byte???????????????????      
	     * */  
	      
	    private static final char[] intToBase64 = {  
	        'A','B','C','D','E','F','G','H','I','J',  
	        'K','L','M','N','O','P','Q','R','S','T',  
	        'U','V','W','X','Y','Z','a','b','c','d',  
	        'f','g','h','i','j','k','l','m','n','o',  
	        'p','q','r','s','t','u','v','w','x','y',  
	        'z','0','1','2','3','4','5','6','7','8',  
	        '9','+','/'  
	    };  
	      
	    /** 
	     * ???ASCII????intToBase64??????      
	     * */      
	     private static final int[] base64Toint = {  
	        -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  
	        -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  
	        -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  
	        -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  
	        -1,-1,-1,62,-1,-1,-1,63,52,53,  
	        54,55,56,57,58,59,60,61,-1,-1,  
	        -1,-1,-1,-1,-1,0 ,1 ,2 ,3 , 4,  
	        5 ,6 ,7 ,8 ,9 ,10,11,12,13,14,  
	        15,16,17,18,19,20,21,22,23,24,  
	        25,-1,-1,-1,-1,-1,-1,26,27,28,  
	        29,30,31,32,33,34,35,36,37,38,  
	        39,40,41,42,43,44,45,46,47,48,  
	        49,50,51      
	     };       
	     /**      
	     * base64????      
	     * 1????byte?????3????????????4???????????????????0      
	     * 2??????????????????intToBase64???????????????      
	     * 3?????????????3???????????????????????=????      
	     * */      
	     public static String encode(byte[] a) {          
	         int totalLen = a.length;         
	         int groupNum = a.length/3;       
	         int lastGroup = totalLen - groupNum*3;       
	         int index = 0;       
	         StringBuffer result = new StringBuffer();        
	         for (int i = 0; i < groupNum ; i++) {             
	            int first = a[index++] & 0xff;            
	            int second = a[index++] & 0xff;           
	            int third = a[index++] & 0xff;                        
	            result.append(intToBase64[first >> 2]);             
	            result.append(intToBase64[(first << 4) & 0x3f | second >> 4]);        
	            result.append(intToBase64[(second << 2) & 0x3f | third >> 6]);            
	            result.append(intToBase64[third & 0x3f]);                     
	         }                        
	         if(lastGroup != 0) {             
	            int first = a[index++] & 0xff;            
	            result.append(intToBase64[first >> 2]);         
	            if(lastGroup == 1) {              
	                result.append(intToBase64[(first << 4) & 0x3f]);                
	                result.append("==");              
	            }else{                
	                int second = a[index++] & 0xff;               
	                result.append(intToBase64[(first << 4) & 0x3f | second >> 4]);                
	                result.append(intToBase64[(second << 2) & 0x3f]);               
	                result.append("=");           
	            }                 
	         }        
	         return result.toString();    
	    }         
	    /** 
	     * base64????   
	     * 1??????????4???????????????????intToBase64?????????     
	     * 2????????????2?????????????3??????byte     
	     * 3????????????????????????????????????????????????????????byte      
	     * */     
	     public static byte[] decode(String s) {  
	        int strlen = s.length();          
	        int groupNum = strlen / 4;        
	        if(groupNum * 4 != strlen) {              
	            throw new IllegalArgumentException("String length must be a multiple of 4.");         
	        }                 
	        int lastMissingNum = 0;       
	        int numFullGroup = groupNum;          
	        if(strlen != 0) {             
	            if(s.charAt(strlen - 1) == '=') {                 
	                lastMissingNum ++;                
	                numFullGroup --;              
	            }             
	            if(s.charAt(strlen - 2) == '=') {                 
	                lastMissingNum ++;            
	            }         
	        }                 
	        byte[] result = new byte[groupNum*3 - lastMissingNum];  
	        int charIndex = 0;        
	        int resultIndex = 0;          
	        for (int i = 0; i < numFullGroup; i++) {           
	            int char0 = base64Toint(s.charAt(charIndex++));           
	            int char1 = base64Toint(s.charAt(charIndex++));           
	            int char2 = base64Toint(s.charAt(charIndex++));           
	            int char3 = base64Toint(s.charAt(charIndex++));           
	            result[resultIndex++] = (byte)(char0 << 2 | char1 >> 4);              
	            result[resultIndex++] = (byte)(char1 << 4 | char2 >> 2);              
	            result[resultIndex++] = (byte)(char2 << 6 | char3);         
	        }                 
	        if(lastMissingNum != 0) {             
	            int char0 = base64Toint(s.charAt(charIndex++));           
	            int char1 = base64Toint(s.charAt(charIndex++));           
	            result[resultIndex++] = (byte)(char0 << 2 | char1 >> 4);              
	            if(lastMissingNum == 1) {                 
	                int char2 = base64Toint(s.charAt(charIndex++));               
	                result[resultIndex++] = (byte)(char1 << 4 | char2 >> 2);              
	            }         
	        }         
	        return result;    
	     }  
	
	
	public  static byte[] hexStringToByte(String hex) {
	    int len = (hex.length() / 2);
		//int len = (hex.length());
	    byte[] result = new byte[len];
	    char[] achar = hex.toCharArray();
	    for (int i = 0; i < len; i++) {
	     int pos = i * 2;
	     result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
	    }
	    return result;
	} 
	private static byte toByte(char c) {
	    byte b = (byte) "0123456789abcdef".indexOf(c);
	    return b;
	} 

	 public static String Bytes2HexString(byte[] b)//Bytes2HexString??????16??????????????????,?????{0x11, 0x22} ---> "1122"
	   	{
	   		String hexs = "";
	   		for (int i=0; i<b.length; i++)
	   		{
	   			String hex = Integer.toHexString(b[i] & 0xFF);
	   			if (hex.length() == 1)
	   			{
	   				hex = '0' + hex;
	   			}
	   			hexs = hexs + hex.toUpperCase();
	   		}
	   		return hexs;
	   	}

	
	public static void main(String[] args) {


        //test start
		byte test[]={17, 62, -11, -100, -61, -64, 75, 84, -6, 42, -104, -104, -2, -78, 110, 41, -111, -127, 125, 28, -112, 61, 10, -99, 27, -107, -51, 4, 2, -39, -106, -89, -18, 114, 113, -38, 87, 7, 100, 104, 114, 72, 84, -59, -104, -93, -3, 71, 61, -108, 96, -73, -93, 24, 35, -32, 60, 41, 124, -51, 20, -74, -51, 64};
		try{    		 
	      	  OutputStream data = new FileOutputStream("d://data.dat");
	      	  data.write(test);
	      	  data.close();
		}
		catch (Exception e) {
			System.out.println("[ERROR] testSM3: " + e.getMessage());
			e.printStackTrace();
		}
		
		System.out.println("main test.length"+test.length);

		//test end
		// ==============????????=============
		for(int i=0;i<1;i++)
		{
			//			boolean OpenFlag = tmpTest.DA_OpenHsmServer(tmpHandle, "192.168.0.146",6006);
  //  	boolean OpenFlag = tmpTest.DA_OpenHsmServer(tmpHandle, "192.168.0.171",6006);
  //  	      	 	          boolean OpenFlag = tmpTest.DA_OpenHsmServer(tmpHandle, "124.207.214.229",6006);
//      	 	       	boolean OpenFlag = tmpTest.DA_OpenHsmServer(tmpHandle, "192.168.0.176",6006);
			boolean OpenFlag = tmpTest.DA_OpenHsmServer(tmpHandle, "192.150.254.225",6006);
	    
		if (!OpenFlag) {
			System.out.println("[ERROR] DA_OpenHsmServer error ...");
			return ;
		}
		else {
			System.out.println("[SUCCESS] DA_OpenHsmServer success ...");
		}
			/*try {
				testSM3Hash();
			} catch (Exception e) {
				System.out.println("[ERROR] testSM3: " + e.getMessage());
				e.printStackTrace();
			}
			boolean CloseFlag = tmpTest.DA_CloseHsmServer(tmpHandle);
			if (!CloseFlag) {
				System.out.println("[ERROR] DA_CloseHsmServer error ...");
				return ;
			}
			else {
				System.out.println("[SUCCESS] DA_CloseHsmServer success ...");
			}*/
			System.out.println("###################"+i);
		}
		
		while(true) {			
			mainHandle();
			Scanner scan = new Scanner(System.in);
			System.out.print("Please Input Number: ");
			int choice = scan.nextInt();

			switch(choice) {
			case 1:
				try {
					testSM3Hash();
				} catch (Exception e) {
					System.out.println("[ERROR] testSM3: " + e.getMessage());
					e.printStackTrace();
				}
				break;
			case 2:
				try { 
		 		   testDA_SM2Sgin();
				} catch (Exception e) {
					System.out.println("[ERROR] testDA_SM2Sgin: " + e.getMessage());
					e.printStackTrace();
				}
				break;
			case 3:
				try {
					testDA_SM2Verify();
				} catch (Exception e) {
					System.out.println("[ERROR] testDA_SM2Verify: " + e.getMessage());
					e.printStackTrace();
				}
				break;
			case 4:
				try {
    				testSM2EncDec(); 
				} catch (Exception e) {
					System.out.println("[ERROR] testSM2EncDec: " + e.getMessage());
					e.printStackTrace();
				}
				break;
			case 5:
				try {
					testSM4EncDec(); 
				} catch (Exception e) {
					System.out.println("[ERROR] testSM2EncDec: " + e.getMessage());
					e.printStackTrace();
				}
				break;	
			case 6:
				try {
					testSM1EncDec(); 
				} catch (Exception e) {
					System.out.println("[ERROR] testSM2EncDec: " + e.getMessage());
					e.printStackTrace();
				}
				break; 
			case 7:
				try {
					testSM4EncDec0(); 
				} catch (Exception e) {
					System.out.println("[ERROR] testSM2EncDec: " + e.getMessage());
					e.printStackTrace();
				}
				break;	
			case 8:
				try {
					testSM1EncDec0(); 
				} catch (Exception e) {
					System.out.println("[ERROR] testSM2EncDec: " + e.getMessage());
					e.printStackTrace();
				}
				break; 
			case 9:
				try {
					DeleteSession(); 
				} catch (Exception e) {
					System.out.println("[ERROR] DeleteSession: " + e.getMessage());
					e.printStackTrace();
				}
				break; 
			case 10:
				try {
					importSessionKey() ; 
				} catch (Exception e) {
					System.out.println("[ERROR] importSessionKey: " + e.getMessage());
					e.printStackTrace();
				}
				break; 
			case 11:
				try {
				testDA_SM2Sgin0();
				} catch (Exception e) {
					System.out.println("[ERROR] testDA_SM2Sgin0: " + e.getMessage());
					e.printStackTrace();
				}
				break;
			case 12:
				try {
					testSM2EncDec0();
				} catch (Exception e) {
					System.out.println("[ERROR] testDA_SM2Sgin0: " + e.getMessage());
					e.printStackTrace();
				}
				break;	
			case 13:
				try {
					testSM3Hash0();
				} catch (Exception e) {
					System.out.println("[ERROR] testDA_SM2Sgin0: " + e.getMessage());
					e.printStackTrace();
				}
				break;	
			case 14:
				try {
					readdevicecode();
				} catch (Exception e) {
					System.out.println("[ERROR] readdevicecode: " + e.getMessage());
					e.printStackTrace();
				}
				break;	
			case 15:
				try {
					writedevicecode();
				} catch (Exception e) {
					System.out.println("[ERROR] testDA_SM2Sgin0: " + e.getMessage());
					e.printStackTrace();
				}
				break;	
			case 16:
				try {
					testDA_ImportCipherSM2Key();
				} catch (Exception e) {
					System.out.println("[ERROR] testDA_ImportCipherSM2Key: " + e.getMessage());
					e.printStackTrace();
				}
				break;	
			case 17:
				try {
					testDA_GetBase64EccPublicKey();
				} catch (Exception e) {
					System.out.println("[ERROR] testDA_GetBase64EccPublicKey: " + e.getMessage());
					e.printStackTrace();
				}
				break;	
					
			case 0:
				scan.close();
				// ==============???????=============
				boolean CloseFlag = tmpTest.DA_CloseHsmServer(tmpHandle);
				if (!CloseFlag) {
					System.out.println("[ERROR] DA_CloseHsmServer error ...");
					return ;
				}
				else {
					System.out.println("[SUCCESS] DA_CloseHsmServer success ...");
				}
				return ;
			default:
				System.out.println("Number Error, Please Input Again ...");
			}
		}
	}
	private static int Bytetoint(byte[] lenBytes) {
		int rv = 0;
		int i = 0;
		int len = lenBytes.length;
		for (i = len; i > 0; i--) {
			rv = (rv * 256) + ((lenBytes[i-1] + 256) % 256);
		}
		return rv;
	}
	public static void  readdevicecode()throws Exception 
	{
		byte []readdata = tmpTest.DA_ReadDeviceCode(tmpHandle);
		System.out.println("main readdata:"+ByteToHexString(readdata));
	}
	public static void  writedevicecode()throws Exception 
	{
		byte []writedata =  "1212".getBytes() ;
		System.out.println("main writedata:"+ByteToHexString(writedata));
		int rv = tmpTest.DA_writeDeviceCode(tmpHandle,writedata);
		if(rv != 0)
		{
			System.out.println(" DA_writeDeviceCode error  :"+rv);
			return;
		}
		
		byte []readdata = tmpTest.DA_ReadDeviceCode(tmpHandle);
		System.out.println("main readdata:"+ByteToHexString(readdata));
		
		String swritedata = writedata.toString();
		String sreaddata = readdata.toString();
		if(swritedata.equals(sreaddata))
		{
			System.out.println("write read device code error !!!" );
		}
		else
		{
			System.out.println("write read device code success !!!" ); 
		}
	}
	public static void DeleteSession() throws Exception {
		byte[] inputData = "1112131121212121".getBytes() ;
		byte[] keydata = "1112131121212121".getBytes() ;
		Scanner scan = new Scanner(System.in);
		 
		System.out.print("Please Input key Number: ");
		int choice = scan.nextInt();
		System.out.println(" choice :"+choice);
		int  result  = -1;
//		for(int i = 0;i < 250;i++)
		{
//			result = tmpTest.DA_SDF_DestroyKey(tmpHandle,i);
			result = tmpTest.DA_SDF_DestroyKey(tmpHandle,choice);
		}
		System.out.println(" result :"+result);
		 
	}
	
	public static String ByteToHexString (byte[] value)
	{
		int len = value.length;
		int i = 0;
		String result = "";
		String tmpStr = "";
		for (i = 0;i<len;i++)
		{
			tmpStr = Integer.toHexString(value[i]   >=   0   ?   value[i]   :   256+value[i]);
			if (tmpStr.length() == 1)
			{
				tmpStr = "0"+tmpStr;
			}
			result = result + tmpStr;			
		}
		return result;
	}
	
//	typedef struct Struct_ECCCIPHERBLOB{
//		BYTE  XCoordinate[ECC_MAX_XCOORDINATE_BITS_LEN/8]; 
//		BYTE  YCoordinate[ECC_MAX_XCOORDINATE_BITS_LEN/8]; 
//		BYTE  HASH[32]; 
//		ULONG   CipherLen;  
//		BYTE     Cipher[1];      //????????
//		} ECCCIPHERBLOB, *PECCCIPHERBLOB;

	 private static int toLittleEndianint(int lenBytes) {
		 byte []b = new byte[4];
		 b[0] = (byte)(0xff & lenBytes);
		 b[1] = (byte)((0xff00 & lenBytes) >> 8);
		 b[2] = (byte)((0xff0000 & lenBytes) >> 16);
		 b[3] = (byte)((0xff000000 & lenBytes) >> 24);
		 return b[3] & 0xff |(b[2] & 0xff) << 8 | (b[1] & 0xff) <<16 |(b[0] & 0xff) << 24;
	}
	 private static int toBigEndianint(int lenBytes) {
		 byte []b = new byte[4];
		 b[3] = (byte)(0xff & lenBytes);
		 b[2] = (byte)((0xff00 & lenBytes) >> 8);
		 b[1] = (byte)((0xff0000 & lenBytes) >> 16);
		 b[0] = (byte)((0xff000000 & lenBytes) >> 24);
		 return b[0] & 0xff |(b[1] & 0xff) >> 8 | (b[2] & 0xff) >> 16 |(b[3] & 0xff) >> 24;
	 }
  private static short lowToShort(short lenBytes) {
 
		 return (short)(((lenBytes & 0xff) << 8)|((lenBytes << 8)&0xff));
	}
  private static short hignToShort(short lenBytes) {
	  
		 return (short)(((lenBytes & 0xff) >> 8)|((lenBytes >> 8)&0xff));
	} 
  private static int lowToInt(int lenBytes) {
	  return (lenBytes & 0xff) << 24 | ((lenBytes >> 8) & 0xff) <<16| ((lenBytes >> 16) & 0xff) << 8|  ((lenBytes  << 24) & 0xff);
	}
  
  private static int hignToInt(int lenBytes) {
	  return (lenBytes & 0xff) >> 24 | ((lenBytes << 8) & 0xff) >> 16| ((lenBytes << 16) & 0xff) >> 8|  ((lenBytes  >> 24) & 0xff);
	}
	public static void importSessionKey() throws Exception {
//		int i = 128;
//		int i1 = toLittleEndianint(i); 
//		System.out.println("toLittleEndianint(4):"+i1);
//		int i2 = toBigEndianint(i1);
//		System.out.println("toBigEndianint(i1):"+i2);
		
//		short i = 100;
//		short i1 = lowToShort(i); 
//		System.out.println("lowToShort(1):"+i1);
//		short i2 = hignToShort(i1);
//	 	System.out.println("hignToShort(i1):"+i2);
		
//		int i = 128;
//		int i1 = lowToInt(i); 
//		System.out.println("lowToInt(128):"+i1);
//		int i2 = hignToInt(i1);
//		System.out.println("hignToInt(i1):"+i2);		
		 
		da_pkeyhandle pKeyHandle = new da_pkeyhandle(); 
		ECCCipher eccCipher = new ECCCipher();
	  
		byte[] data = getInData("E:/DE/cn/2017/0602???????????????????/??????????/key(2).txt");
		 
		System.out.println("data.length:"+data.length);
		
		System.arraycopy(data , 32,eccCipher.x, 32,32);
		System.arraycopy(data, 64+32,eccCipher.y, 32,32);
		byte[] len1 = new byte[4];
		System.arraycopy(data, 64+64+32,len1, 0,4);
		int ilen1 = Bytetoint(len1);
		eccCipher.L = ilen1;
		
		System.out.println("ilen1:"+ilen1);
		
		System.arraycopy(data, 64+64,eccCipher.M, 0,32);
		System.arraycopy(data, 64+64+32+4,eccCipher.C, 0,eccCipher.L);
 	
 	    boolean result = tmpTest.DA_SDF_ImportKeyWithISK_ECC(tmpHandle,1, eccCipher ,pKeyHandle);
   	    System.out.println("pKeyHandle.HandleState:"+pKeyHandle.HandleState); 
//   	    int uiAlgID = 0x00000102; //SM1 CBC
 //       int uiAlgID = 0x00000101; //SM1 ECB
        int uiAlgID = 0x00000401;//SM4 ECB
   	    byte []iv = "1111111111111111".getBytes();
  	    byte[] encdata =  getInData("E:/DE/cn/2017/0602???????????????????/??????????/encdata(1).txt.bak");
  	    int indatalen = encdata.length;
//  	    System.out.println("main >>>> Hex Der iv[" + iv.length + "] = ");
//		System.out.println(ByteToHexString(iv));  
//  	    String encdataout = tmpTest.DA_EncKeyNum(tmpHandle,"SM4", pKeyHandle,iv);
  	    
  	    String decdatain = new String(Base64.encode(encdata));
  	    System.out.println("decdatain.length()"+decdatain.length());
  	    byte [] decdataout = tmpTest.DA_DncKeyNum(tmpHandle,"SM4", pKeyHandle,decdatain);
  	    System.out.println("main >>>> Hex Der decdata[" + decdataout.length + "] = ");
		System.out.println(ByteToHexString(decdataout));
		
	//	int result11 = tmpTest.DA_SDF_DestroyKey(tmpHandle, pKeyHandle);
//		System.out.println("main >>>> DA_SDF_DestroyKey result11 = " + result11);
		 
	}

	public static void testSM4EncDec0() throws Exception {
//		byte[] inputData = "1112121212121212".getBytes() ;  
		
		byte[] inputData = {0x11,0x22,0x33,0x44,0x11,0x22,0x33,0x44,0x11,0x22,0x33,0x44,0x11,0x22,0x33,0x44};
		byte[] keydata = "1112131121212121".getBytes() ;
		String encdata = tmpTest.DA_Enc(tmpHandle,"SM4",keydata, inputData);
		System.out.println("[SUCCESS] DA_EncKeyNum ok ...inputData.length"+inputData.length);
		System.out.println(bin2HexString(Base64.decode(encdata)));
		byte []  decdata = tmpTest.DA_Dnc(tmpHandle,"SM4",keydata,encdata);
		System.out.println("[SUCCESS] DA_DncKeyNum ok ...");
		
		// ???16???? DER????? SM2??????? 
		System.out.println(">>>> Hex Der inputData[" + inputData.length + "] = ");
		System.out.println(bin2HexString(inputData));

		// ???16????? SM2??????? 
		System.out.println(">>>> Hex decdata[" + decdata.length + "] = ");
		System.out.println(bin2HexString(decdata));
	}
	
	public static void testSM1EncDec0() throws Exception {
		byte[] inputData = "1112131121212121".getBytes() ;
		byte[] keydata = "1112131121212121".getBytes() ;
		String encdata = tmpTest.DA_Enc(tmpHandle,"SM1",keydata, inputData);
		System.out.println("[SUCCESS] DA_EncKeyNum ok ...");
		byte []  decdata = tmpTest.DA_Dnc(tmpHandle,"SM1",keydata,encdata);
		System.out.println("[SUCCESS] DA_DncKeyNum ok ...");
		
		// ???16???? DER????? SM2??????? 
		System.out.println(">>>> Hex Der inputData[" + inputData.length + "] = ");
		System.out.println(bin2HexString(inputData));

		// ???16????? SM2??????? 
		System.out.println(">>>> Hex decdata[" + decdata.length + "] = ");
		System.out.println(bin2HexString(decdata));
	}
	
	public static void testSM4EncDec() throws Exception {
		byte[] inputData = "1112131121212121".getBytes() ;
		String encdata = tmpTest.DA_EncKeyNum(tmpHandle,"SM4",1, inputData);
		System.out.println("[SUCCESS] DA_EncKeyNum ok ...");
		byte []  decdata = tmpTest.DA_DncKeyNum(tmpHandle,"SM4",1,encdata);
		System.out.println("[SUCCESS] DA_DncKeyNum ok ...");
		
		// ???16???? DER????? SM2??????? 
		System.out.println(">>>> Hex Der inputData[" + inputData.length + "] = ");
		System.out.println(bin2HexString(inputData));

		// ???16????? SM2??????? 
		System.out.println(">>>> Hex decdata[" + decdata.length + "] = ");
		System.out.println(bin2HexString(decdata));
	}
	
	public static void testSM1EncDec() throws Exception {
		byte[] inputData = "1112131121212121".getBytes() ;
		String encdata = tmpTest.DA_EncKeyNum(tmpHandle,"SM1",1, inputData);
		System.out.println("[SUCCESS] DA_EncKeyNum ok ...");
		byte []  decdata = tmpTest.DA_DncKeyNum(tmpHandle,"SM1",1,encdata);
		System.out.println("[SUCCESS] DA_DncKeyNum ok ...");
		
		// ???16???? DER????? SM2??????? 
		System.out.println(">>>> Hex Der inputData[" + inputData.length + "] = ");
		System.out.println(bin2HexString(inputData));

		// ???16????? SM2??????? 
		System.out.println(">>>> Hex decdata[" + decdata.length + "] = ");
		System.out.println(bin2HexString(decdata));
	}
	
	
	
	public static void testSM2EncDec() throws Exception {
		byte[] inputData = "11121311212121211".getBytes() ;
		byte[] pubKey = tmpTest.DA_GetSM2PublicKey(tmpHandle,1,2).getEncoded();
		System.out.println("[SUCCESS] DA_GetSM2PublicKey ok ..." + pubKey.length);
		System.out.println(" inputData.length ..."+inputData.length);
		String encdata = tmpTest.DA_SM2PublicEnc(tmpHandle,inputData, pubKey);
		System.out.println("[SUCCESS] DA_SM2PublicEnc ok ...");
		System.out.println(">>>> Hex Der inputData[" + inputData.length + "] = ");
		System.out.println(encdata);
		byte []  decdata = tmpTest.DA_SM2PrivateDec(tmpHandle,1,encdata);
		System.out.println("[SUCCESS] DA_SM2PrivateDec ok ...");
		
		// ???16???? DER????? SM2??????? 
		System.out.println(">>>> Hex Der inputData[" + inputData.length + "] = ");
		System.out.println(bin2HexString(inputData));

		// ???16????? SM2??????? 
		System.out.println(">>>> Hex decdata[" + decdata.length + "] = ");
		System.out.println(bin2HexString(decdata));
		
	}
	public static void testSM3Hash0() throws Exception { 
		byte[] InData = {0x71,(byte)0xaf,(byte)0xf2,0x64,(byte)0xd0,(byte)0xf2,0x48,0x41,(byte)0xd6,0x46,0x5f,0x09,(byte)0x96,(byte)0xff,(byte)0x84,(byte)0xe6};
		 
		byte[] xy = new byte[64];
 
		byte[] sm2Pub = {0x21,(byte)0xfc,0x5d,(byte)0xa2,(byte)0x9a,0x27,(byte)0xd3,0x32,(byte)0xc4,(byte)0x86,0x57,0x70,0x01,(byte)0xfa,(byte)0xfa,0x34,0x49,0x38,0x75,(byte)0x85,(byte)0xed,0x0e,0x05,0x1c,(byte)0xb8,0x66,0x68,
				0x1b,0x3c,(byte)0xa4,(byte)0xa2,(byte)0xf7,(byte)0xbb,(byte)0xc4,0x6d,(byte)0xc9,0x63,0x7a,0x05,(byte)0x84,0x07,(byte)0xe9,0x50,0x53,0x1f,0x76,(byte)0x9a,0x4c,0x69,(byte)0x8a,0x5d,(byte)0xf5,(byte)0x88,0x6b,(byte)0x89,0x05,
				0x68,(byte)0x88,(byte)0xde,0x7d,0x0b,(byte)0xf7,(byte)0x86,(byte)0x97};
		System.out.println("sm2Pub.length:" +sm2Pub.length); 
		System.arraycopy(sm2Pub, 0, xy, 0, 64);
		System.out.println("xy= "+bin2HexString(xy)+sm2Pub.length);
		SM3Digest digest = new SM3Digest();
		byte []x = new byte[32];
		byte []y = new byte[32];
		byte []id = {0x5f,(byte)0xc5,0x17,(byte)0xc5,0x3e,(byte)0xfc,0x33,0x63,(byte)0xc3,(byte)0x84,(byte)0x92,(byte)0xab,0x08,(byte)0xa3,(byte)0xaa,0x3f};
		System.arraycopy(sm2Pub, 0, x, 0, 32);
		System.arraycopy(sm2Pub, 32, y, 0, 32);
		digest.addId(x, y, id);
		
		
		digest.update(InData, 0, InData.length);
		byte[] out = new byte[32];
		digest.doFinal(out, 0);
		System.out.println("out= "+bin2HexString(out)+out.length);	
		 
		// SM3 ?????
		tmpTest.SM3_Init(tmpHandle, xy);
		
		// SM3 Updata 
		int p = InData.length/128;
		int q = InData.length%128;
		
		for(int i=0; i<p; i++) {
			byte[] Data = new byte[128];
			System.arraycopy(InData, 128*i, Data, 0, 128);
			tmpTest.SM3_Updata(tmpHandle, Data);
		}
		byte[] Data = new byte[q];
		System.arraycopy(InData, 128*p, Data, 0, q);
		tmpTest.SM3_Updata(tmpHandle, Data);
		
		//  SM3 ????
		HashData = tmpTest.SM3_Final(tmpHandle);		
		if (HashData == null) {
			System.out.println("[ERROR] DA_DigestTmp error ...");
			return ;
		}
		else {
			System.out.println("[SUCCESS] DA_DigestTmp success ...\n");

			// ???16???? SM3 Hash????
			System.out.println(">>>> Hex HashData[" + HashData.length + "] = ");
			System.out.println(bin2HexString(HashData));
		}	
	}
	
	public static void testSM3Hash() throws Exception {
//		byte[] InData = getInData("d:\\TestFile.txt");
		byte[] InData = getInData("E:/DE/cn/2017/0602???????????????????/??????????/ori.txt");
		// ??? SM3 Hash?????
		//byte[] sm2Pub = tmpTest.DA_GetSM2PublicKey(tmpHandle, 1).getEncoded();
		byte[] xy = new byte[64];
//		String aa="7a57266640f996491f6ee46918e26f7c6acdf1ac9e668ae969f881062ae237e6551b62c8c6fea0f83ee6deab07ac91e74f8c6b25d9ff8ce2dc72aa3d0706ffb8";
	//	byte[] sm2Pub=hexStringToByte(aa);
		
//		String aa="47a9289c15f27f5a47ddfacc24d8061e2ad27d6295555efc570e22898587be71d74f9ed4bebf286b36f7699e00e174ebe571c4134c0af91dd560e361c56ed145";
//		byte[] sm2Pub=hexStringToByte(aa);
		byte[] sm2Pub = getInData("E:/DE/cn/2017/0602???????????????????/??????????/pubkey.txt");
		//System.arraycopy(sm2Pub, sm2Pub.length - 64, xy, 0, 64);
		System.arraycopy(sm2Pub, 1, xy, 0, 64);
		System.out.println("xy= "+bin2HexString(xy)+sm2Pub.length);
		SM3Digest digest = new SM3Digest();
		digest.update(InData, 0, InData.length);
		byte[] out = new byte[32];
		digest.doFinal(out, 0);
		System.out.println("out= "+bin2HexString(out)+out.length);		
		
		// SM3 ?????
		tmpTest.SM3_Init(tmpHandle, xy);
		
		// SM3 Updata 
		int p = InData.length/128;
		int q = InData.length%128;
		
		for(int i=0; i<p; i++) {
			byte[] Data = new byte[128];
			System.arraycopy(InData, 128*i, Data, 0, 128);
			tmpTest.SM3_Updata(tmpHandle, Data);
		}
		byte[] Data = new byte[q];
		System.arraycopy(InData, 128*p, Data, 0, q);
		tmpTest.SM3_Updata(tmpHandle, Data);
		
		//  SM3 ????
		HashData = tmpTest.SM3_Final(tmpHandle);		
		if (HashData == null) {
			System.out.println("[ERROR] DA_DigestTmp error ...");
			return ;
		}
		else {
			System.out.println("[SUCCESS] DA_DigestTmp success ...\n");

			// ???16???? SM3 Hash????
			System.out.println(">>>> Hex HashData[" + HashData.length + "] = ");
			System.out.println(bin2HexString(HashData));
		}	
	}

	public static void testDA_SM2Sgin() throws Exception {
		// ????????? SM2???
		SignData = tmpTest.DA_SM2Sgin(tmpHandle,1, HashData);
		if (SignData == null) {
			System.out.println("[ERROR] DA_SM2Sgin error ...");
			return ;
		}
		else {
			System.out.println("[SUCCESS] DA_SM2Sgin success ...\n");

			// ??? Base64?????? DER????? SM2???????
			System.out.println(">>>> Base64 Der SignData[" + SignData.length() + "] = ");
			System.out.println(SignData);

			// ???16???? DER????? SM2???????
			byte[] encodeData = Base64.decode(SignData);
			System.out.println(">>>> Hex Der encodeData[" + encodeData.length + "] = ");
			System.out.println(bin2HexString(encodeData));

			// ???16????? SM2???????
			byte[] structData = KeyBase.signDerToStruct(encodeData);
			System.out.println(">>>> Hex structData[" + structData.length + "] = ");
			System.out.println(bin2HexString(structData));
		}
	}

	public static void testSM2EncDec0() throws Exception {
	//	byte[] prikey =  getInData("C:/Users/dean-user/Desktop/eccprikey1.dat");
		byte[] inputData = "11121311212121211".getBytes() ;
//		byte[] pubKey = tmpTest.DA_GetSM2PublicKey(tmpHandle,1,2).getEncoded();
 //????1
//		byte [] pubkey = {(byte)0xD0,(byte)0xCE,(byte)0xAD,(byte)0x0E,(byte)0x37,(byte)0x77,(byte)0xEF,(byte)0x5B,(byte)0x1D,(byte)0x63,(byte)0x11
//				,(byte)0xC1,(byte)0x9F,(byte)0x65,(byte)0x1E,(byte)0x5E,(byte)0x57,(byte)0xDB,(byte)0x5C,(byte)0x54,(byte)0xAA,(byte)0x60,(byte)0xF3
//				,(byte)0x01,(byte)0xE2,(byte)0xA9,(byte)0xF8,(byte)0x95,(byte)0x59,(byte)0xE2,(byte)0x71,(byte)0x01,(byte)0x41,(byte)0x30,(byte)0xE2
//				,(byte)0x82,(byte)0x0A,(byte)0x0E,(byte)0x89,(byte)0x28,(byte)0x91,(byte)0x02,(byte)0x2F,(byte)0xA5,(byte)0xD7,(byte)0xAB,(byte)0xC9
//				,(byte)0x7F,(byte)0x8C,(byte)0x1C,(byte)0x37,(byte)0x14,(byte)0xAC,(byte)0xB0,(byte)0x77,(byte)0x25,(byte)0x4C,(byte)0x75,(byte)0x7F
//				,(byte)0xEA,(byte)0x52,(byte)0xDF,(byte)0x52,(byte)0xB5};
//		byte [] prikey1 = {(byte)0x4E,(byte)0xBD,(byte)0x91,(byte)0x5A,(byte)0xDF,(byte)0x31,(byte)0xB1,(byte)0xD6,(byte)0x44,(byte)0xBB,(byte)0xEB
//				,(byte)0xA2,(byte)0xA9,(byte)0xD3,(byte)0x4B,(byte)0x78,(byte)0x29,(byte)0xB2,(byte)0x90,(byte)0x14,(byte)0x51,(byte)0x0E,(byte)0xB8
//				,(byte)0xA4,(byte)0x99,(byte)0xF8,(byte)0xAF,(byte)0x9E,(byte)0x6D,(byte)0x54,(byte)0x56,(byte)0xDF};
//????2
//		byte [] pubkey = {(byte)0xDC,(byte)0xB4,(byte)0x29,(byte)0x67,(byte)0xBE,(byte)0x30,(byte)0x8E,(byte)0x4A,(byte)0x23,(byte)0x72,(byte)0xCC,(byte)0x47
//				,(byte)0x27,(byte)0xCF,(byte)0xF8,(byte)0x3E,(byte)0x41,(byte)0x20,(byte)0x1D,(byte)0xC7,(byte)0x5E,(byte)0x11,(byte)0x0C,(byte)0xE7
//				,(byte)0x29,(byte)0xD4,(byte)0x47,(byte)0xA8,(byte)0x55,(byte)0x85,(byte)0x33,(byte)0xF2,(byte)0x68,(byte)0x37,(byte)0xEC,(byte)0xDB
//				,(byte)0xC5,(byte)0x29,(byte)0x66,(byte)0x42,(byte)0x9C,(byte)0x7A,(byte)0x23,(byte)0x18,(byte)0xED,(byte)0xB4,(byte)0x71,(byte)0xBA
//				,(byte)0x27,(byte)0x42,(byte)0x94,(byte)0x99,(byte)0x54,(byte)0x07,(byte)0x80,(byte)0xE8,(byte)0xE0,(byte)0x46,(byte)0x3A,(byte)0x42
//				,(byte)0x39,(byte)0xA1,(byte)0x45,(byte)0x52};
//		byte [] prikey1 = {(byte)0x2F,(byte)0xAA,(byte)0xEF,(byte)0x8A,(byte)0x43,(byte)0xCD,(byte)0x41,(byte)0xB9,(byte)0xD7,(byte)0x42,(byte)0x45,(byte)0xFA
//				,(byte)0xBD,(byte)0x2E,(byte)0x87,(byte)0x79,(byte)0x26,(byte)0xFC,(byte)0x65,(byte)0x16,(byte)0x81,(byte)0xDB,(byte)0x28,(byte)0x93
//				,(byte)0x74,(byte)0xCA,(byte)0x2B,(byte)0xFC,(byte)0x20,(byte)0x25,(byte)0x79,(byte)0x3E};
//????3
		byte [] pubkey = {(byte)0xBD,(byte)0x5B,(byte)0x89,(byte)0x42,(byte)0x73,(byte)0xB8,(byte)0x8A,(byte)0xB0,(byte)0xE1,(byte)0x7F,(byte)0xF7,(byte)0x70
				,(byte)0x25,(byte)0xAE,(byte)0xBC,(byte)0x0C,(byte)0x97,(byte)0xA4,(byte)0x4D,(byte)0x41,(byte)0x26,(byte)0xF3,(byte)0xBD,(byte)0x59
				,(byte)0x8B,(byte)0x9A,(byte)0x1F,(byte)0x3A,(byte)0xAD,(byte)0x5C,(byte)0x84,(byte)0x86,(byte)0x3A,(byte)0xEC,(byte)0xEB,(byte)0xC4
				,(byte)0xB5,(byte)0x0A,(byte)0xE1,(byte)0x82,(byte)0x54,(byte)0x2B,(byte)0x80,(byte)0x13,(byte)0x55,(byte)0xF7,(byte)0x2A,(byte)0x75
				,(byte)0x57,(byte)0x69,(byte)0x31,(byte)0xDE,(byte)0x5C,(byte)0x09,(byte)0x43,(byte)0xD4,(byte)0x6B,(byte)0xFF,(byte)0x3F,(byte)0xD3
				,(byte)0xCA,(byte)0xCD,(byte)0x3E,(byte)0x87};
		byte [] prikey1 = {(byte)0x75,(byte)0xB7,(byte)0xB1,(byte)0x07,(byte)0xFC,(byte)0x74,(byte)0x93,(byte)0x50,(byte)0xF0,(byte)0x0E,(byte)0x4C,(byte)0x26
				,(byte)0xD7,(byte)0x5B,(byte)0xA7,(byte)0x1B,(byte)0xEA,(byte)0x79,(byte)0x0A,(byte)0x21,(byte)0xE3,(byte)0xA6,(byte)0x82,(byte)0xDA
				,(byte)0x92,(byte)0x07,(byte)0x92,(byte)0x99,(byte)0xD7,(byte)0xFE,(byte)0xF8,(byte)0xB7};
		
		
		byte[] prikey = new byte[292];
		System.arraycopy(pubkey, 0, prikey, 292-96, 64);
		System.arraycopy(prikey1, 0, prikey, 292-32, 32);
		byte[] pubKey = new byte[260];
		System.arraycopy(prikey, 0, pubKey, 0, 260); 
		System.out.println(" inputData.length ..."+inputData.length);
		String encdata = tmpTest.DA_SM2PublicEnc(tmpHandle,inputData, pubKey);
		System.out.println("[SUCCESS] DA_SM2PublicEnc ok ...");
		System.out.println(">>>> Hex encdata[" + encdata.length() + "] = ");
		System.out.println(bin2HexString(Base64.decode(encdata)));
		 //????1
//		byte [] encdata1 = {0x20,0x00,(byte)0xD2,(byte)0x56,(byte)0xB6,(byte)0xE5,(byte)0x9F,(byte)0x68,(byte)0x2C,(byte)0x13,(byte)0xA6,(byte)0x3C,(byte)0x9D,
//				(byte)0x89,(byte)0x30,(byte)0x58,(byte)0xB1,(byte)0xEB,(byte)0xE5,(byte)0xD6,(byte)0x54,(byte)0xD0,(byte)0x78,(byte)0xBC,(byte)0x91,(byte)0xFF,
//				(byte)0x11 ,(byte)0xFB,(byte)0x9C,(byte)0x4D,(byte)0xEB,(byte)0x8B,(byte)0xF4,(byte)0xCD,(byte)0xA9,(byte)0x49,(byte)0x25,(byte)0x7C,(byte)0x3E,
//				(byte)0x8B ,(byte)0xF7,(byte)0x3F,(byte)0xDA,(byte)0xCF,(byte)0x54,(byte)0x85,(byte)0xDA,(byte)0x68,(byte)0x0D,(byte)0x05,(byte)0xB5,(byte)0xFE,
//				(byte)0x58 ,(byte)0x83,(byte)0x0E,(byte)0xF0,(byte)0x61,(byte)0x3D,(byte)0x3A,(byte)0x94,(byte)0x07,(byte)0x79,(byte)0x0A,(byte)0x3B,(byte)0x2E,
//				(byte)0x1F ,
//				(byte)0x40,(byte)0x57,(byte)0xA8,(byte)0x28,(byte)0xAE,(byte)0x52,(byte)0x8B,(byte)0xA3,(byte)0x33,(byte)0xBA,(byte)0xB0,(byte)0x98,
//				(byte)0x2A ,(byte)0x00,(byte)0x9F,(byte)0xD0,(byte)0x01,(byte)0xF8,(byte)0xBC,(byte)0xF1,(byte)0x2F,
//				(byte)0x1A,(byte)0x45,(byte)0x12,(byte)0xF5,(byte)0x22,(byte)0x11,(byte)0x56,(byte)0xA1,(byte)0xEF,(byte)0x3E,(byte)0x07,	
//				(byte)0x59,(byte)0x28,(byte)0x81,(byte)0xD1,(byte)0xD8,(byte)0xB1,(byte)0x38,(byte)0xAD,(byte)0x1E,(byte)0xFD,(byte)0xD5,(byte)0x1F,(byte)0xA6,
//				(byte)0x26,(byte)0x26,(byte)0xDF,(byte)0x59,(byte)0xEA,(byte)0x2A,(byte)0x79,(byte)0xA7,(byte)0xF2,(byte)0xE6,(byte)0x21,(byte)0x11,(byte)0x4C
//				,(byte)0x11,(byte)0x4A,(byte)0x17,(byte)0x44,(byte)0xC0,(byte)0x16};
		//????2
//		byte [] encdata1 = {0x20,0x00,(byte)0x17,(byte)0xD0,(byte)0x25,(byte)0x5E,(byte)0x5B,(byte)0x3F,(byte)0x49,(byte)0x8A,(byte)0xFC,(byte)0x39,(byte)0xCC,(byte)0xFB
//				,(byte)0xC6,(byte)0x45,(byte)0x67,(byte)0x8F,(byte)0x78,(byte)0xD8,(byte)0x1A,(byte)0xC7,(byte)0x7C,(byte)0x70,(byte)0x3D,(byte)0x90
//				,(byte)0xC7,(byte)0xF3,(byte)0xE5,(byte)0x99,(byte)0x7B,(byte)0x09,(byte)0xB2,(byte)0x78,(byte)0x88,(byte)0xE6,(byte)0x6D,(byte)0xE1
//				,(byte)0xB4,(byte)0xC7,(byte)0x73,(byte)0x66,(byte)0xA4,(byte)0x2B,(byte)0xD7,(byte)0x39,(byte)0x14,(byte)0xB5,(byte)0xD3,(byte)0x25
//				,(byte)0xE1,(byte)0x18,(byte)0x32,(byte)0x8B,(byte)0x61,(byte)0x3E,(byte)0x29,(byte)0x07,(byte)0x13,(byte)0x4B,(byte)0xBD,(byte)0xB0
//				,(byte)0x96,(byte)0x41,(byte)0x0E,(byte)0x90,(byte)0x49,(byte)0xC6,(byte)0x58,(byte)0x13,(byte)0x05,(byte)0x01,(byte)0x76,(byte)0x75
//				,(byte)0xB0,(byte)0x33,(byte)0x47,(byte)0xC7,(byte)0x34,(byte)0xD3,(byte)0x0A,(byte)0x1E,(byte)0x9D,(byte)0x8E,(byte)0xDD,(byte)0x46
//				,(byte)0x46,(byte)0xFC,(byte)0x2A,(byte)0x46,(byte)0x1D,(byte)0x07,(byte)0xE1,(byte)0x5A,(byte)0x02,(byte)0xDB,(byte)0x21,(byte)0xF1
//				,(byte)0x70,(byte)0xD7,(byte)0x65,(byte)0x9E,(byte)0x27,(byte)0xC0,(byte)0x87,(byte)0xE1,(byte)0xE4,(byte)0xB4,(byte)0x7D,(byte)0x5E
//				,(byte)0x55,(byte)0x74,(byte)0xE0,(byte)0xA7,(byte)0x4B,(byte)0x8A,(byte)0x3C,(byte)0x03,(byte)0xFF,(byte)0xD9,(byte)0x26,(byte)0xEF
//				,(byte)0xEF,(byte)0xAA,(byte)0x91,(byte)0xA2,(byte)0x65,(byte)0x15,(byte)0x95,(byte)0x84};
		//????3
		byte [] encdata1 = {(byte)0x20,0x00,(byte)0x57,(byte)0x11,(byte)0xEC,(byte)0x67,(byte)0x8E,(byte)0xC7,(byte)0x30,(byte)0xAC,(byte)0x69,(byte)0xAC,(byte)0xB8,(byte)0x9B
				,(byte)0xE2,(byte)0xBF,(byte)0x10,(byte)0xB4,(byte)0x59,(byte)0x9B,(byte)0xA5,(byte)0x7D,(byte)0x35,(byte)0x0D,(byte)0x14,(byte)0xCA
				,(byte)0xF7,(byte)0xC7,(byte)0x22,(byte)0x34,(byte)0x57,(byte)0x52,(byte)0x2E,(byte)0xC6,(byte)0x5D,(byte)0x4A,(byte)0x7B,(byte)0xA8
				,(byte)0xC3,(byte)0x77,(byte)0x8C,(byte)0xB0,(byte)0x1D,(byte)0x31,(byte)0x76,(byte)0x37,(byte)0x8B,(byte)0xFB,(byte)0x53,(byte)0xA3
				,(byte)0x5C,(byte)0x77,(byte)0xBE,(byte)0x39,(byte)0xFF,(byte)0x1C,(byte)0xD0,(byte)0x99,(byte)0x9A,(byte)0x75,(byte)0xC0,(byte)0xA3
				,(byte)0xC2,(byte)0xC1,(byte)0xB1,(byte)0x6B,(byte)0x94,(byte)0x19,(byte)0x62,(byte)0x9C,(byte)0xC4,(byte)0x52,(byte)0x71,(byte)0x5A
				,(byte)0x5B,(byte)0x79,(byte)0x50,(byte)0x59,(byte)0x4F,(byte)0xE2,(byte)0x05,(byte)0xBC,(byte)0x01,(byte)0x34,(byte)0xE5,(byte)0xC9
				,(byte)0xFF,(byte)0xA2,(byte)0x2A,(byte)0xF5,(byte)0x56,(byte)0x6B,(byte)0xB9,(byte)0xC1,(byte)0x14,(byte)0x13,(byte)0x0D,(byte)0x9D
				,(byte)0x6E,(byte)0x6A,(byte)0xC2,(byte)0x99,(byte)0x93,(byte)0x1D,(byte)0x66,(byte)0x0E,(byte)0x1E,(byte)0x66,(byte)0xA5,(byte)0x26
				,(byte)0x3F,(byte)0x56,(byte)0xA5,(byte)0xA7,(byte)0x92,(byte)0x4E,(byte)0x6C,(byte)0xAF,(byte)0xF9,(byte)0xC5,(byte)0x82,(byte)0x5B
				,(byte)0x13,(byte)0x47,(byte)0xDF,(byte)0xEF,(byte)0x6B,(byte)0x5A,(byte)0xD2,(byte)0x28};
		
		//2+64+ 32+ 32 = 130   c1  c3  c2  
		byte []encstruct = new byte[4194]; 
		System.arraycopy(encdata1, 0, encstruct, 0, 2);  
		System.arraycopy(encdata1, 2, encstruct, 2, 64); //c1
		System.arraycopy(encdata1, 66, encstruct, 4194-32, 32);   //c3
		System.arraycopy(encdata1, encdata1.length-32, encstruct, 66, 32);   //c2 
	    byte[] sm2Enc=KeyBase.encStructToDer(encstruct); 
	    encdata =  new String(Base64.encode(sm2Enc));
	    
	    System.out.println(">>>> Hex encdata[" + encdata.length() + "] = ");
		System.out.println(bin2HexString(Base64.decode(encdata)));
		byte []  decdata = tmpTest.DA_SM2PrivateDec0(tmpHandle,prikey,encdata);
		System.out.println("[SUCCESS] DA_SM2PrivateDec ok ...");
		
		// ???16???? DER????? SM2??????? 
		System.out.println(">>>> Hex Der inputData[" + inputData.length + "] = ");
		System.out.println(bin2HexString(inputData));

		// ???16????? SM2??????? 
		System.out.println(">>>> Hex decdata[" + decdata.length + "] = ");
		System.out.println(bin2HexString(decdata));
		
	}
	
	public static void testDA_SM2Sgin0() throws Exception {
		//????1
//		byte [] pubkey = {(byte)0x4C,(byte)0x60,(byte)0xFF,(byte)0xCC,(byte)0x57,(byte)0xBD,(byte)0x2B,(byte)0x47,(byte)0x7A,(byte)0x0C,(byte)0x26,(byte)0xAE,(byte)0xAA,(byte)0xC1,
//			      (byte)0xB8,(byte)0xD4,(byte)0xA2,(byte)0xB3,(byte)0xFF,(byte)0xE8,(byte)0x11,(byte)0x48,(byte)0xD8,(byte)0xC3,(byte)0xF5,(byte)0x3B,(byte)0x4E,(byte)0xD9,
//				  (byte)0x76,(byte)0x7F,(byte)0xB4,(byte)0x36,(byte)0x78,(byte)0x45,(byte)0x91,(byte)0x97,(byte)0x9C,(byte)0x99,(byte)0xE2,(byte)0x06,(byte)0x45,(byte)0xAE,
//				  (byte)0x40,(byte)0x49,(byte)0x92,(byte)0xC5,(byte)0xA0,(byte)0xC6,(byte)0xFA,(byte)0x8F,(byte)0x39,(byte)0x52,(byte)0x61,(byte)0xBB,(byte)0x2C,(byte)0x25,
//				  (byte)0x58,(byte)0x93,(byte)0x0F,(byte)0xC9,(byte)0x81,(byte)0x89,(byte)0xF4,(byte)0x0C};
//		byte[] prikey1 = {(byte)0x1E,(byte)0xF0,(byte)0x19,(byte)0x74,(byte)0xEB,(byte)0x13,(byte)0x2F,(byte)0xE0,(byte)0xC1,(byte)0x4D,(byte)0x0A,(byte)0x7F,(byte)0x9B,(byte)0x57,
//			      (byte)0xF0,(byte)0x64,(byte)0xA4,(byte)0x0A,(byte)0xA1,(byte)0xD0,(byte)0x8E,(byte)0x0E,(byte)0x77,(byte)0xB0,(byte)0x4B,(byte)0x1A,(byte)0x77,(byte)0x94,
//				  (byte)0xC9,(byte)0x8F,(byte)0x98,(byte)0x7F};

		//????2 		
//		byte [] pubkey = {(byte)0x88,(byte)0xA2,0x4C,(byte)0x99,0x6C,(byte)0xE1,0x43,0x48,(byte)0xBB,0x0F,0x40,(byte)0xBB,0x1C,0x7F,
//		          0x19,0x5B,(byte)0xCE,0x34,0x47,0x01,0x42,0x7F,0x4E,0x1C,0x76,0x27,0x3D,0x27,
//		          (byte)0xE5,0x29,0x5E,(byte)0x89,(byte)0xB2,0x26,(byte)0x83,0x7D,0x69,0x74,0x1B,(byte)0xB2,0x18,0x25,
//		          (byte)0x8F,(byte)0xA1,0x69,0x63,0x20,(byte)0xC1,0x6D,0x74,(byte)0xB2,0x41,0x7D,(byte)0xBD,0x78,(byte)0xB1,
//				  0x65,(byte)0xAE,(byte)0x93,(byte)0x91,0x1A,(byte)0xAE,0x7D,(byte)0x8B};
//		byte[] prikey1 = {0x2E,(byte)0xA6,(byte)0xA6,0x50,0x69,0x79,0x4E,(byte)0xFF,0x16,0x79,0x21,0x53,(byte)0xDE,0x30,
//				(byte)0xB1,(byte)0x94,(byte)0x88,0x39,(byte)0x94,0x46,0x7F,0x4D,0x6E,0x2F,(byte)0xF8,(byte)0xD0,(byte)0x9F,(byte)0xF6,
//				(byte)0xC1,0x2F,(byte)0x9D,0x7E};
		
		//????3
		byte [] pubkey = {(byte)0xCC,(byte)0xE7,(byte)0xAD,(byte)0x32,(byte)0xA3,(byte)0x28,(byte)0x96,(byte)0x0B,(byte)0xEA,(byte)0xBD,(byte)0x05,(byte)0x38,(byte)0x0E,(byte)0x88,
		          (byte)0xA9,(byte)0x9F,(byte)0x65,(byte)0x6E,(byte)0x13,(byte)0x6A,(byte)0x50,(byte)0x36,(byte)0x96,(byte)0xC5,(byte)0x1B,(byte)0xB4,(byte)0x3B,(byte)0xC9,
				  (byte)0xB6,(byte)0xB0,(byte)0xE3,(byte)0x5D,(byte)0xFE,(byte)0x6E,(byte)0xFC,(byte)0xB2,(byte)0x73,(byte)0x71,(byte)0x15,(byte)0xB5,(byte)0xE8,(byte)0x60,
				  (byte)0x65,(byte)0xEE,(byte)0x7F,(byte)0x68,(byte)0x7A,(byte)0x0D,(byte)0x6A,(byte)0x5A,(byte)0x0E,(byte)0x2B,(byte)0x9D,(byte)0xB2,(byte)0xD8,(byte)0x6F,
				  (byte)0x5F,(byte)0x19,(byte)0x3E,(byte)0xE3,(byte)0x15,(byte)0x38,(byte)0xD2,(byte)0x01};
		byte[] prikey1 = {(byte)0x4E,(byte)0x46,(byte)0x66,(byte)0x07,(byte)0xBA,(byte)0x66,(byte)0xA7,(byte)0x10,(byte)0x57,(byte)0x9C,(byte)0xE0,(byte)0x81,(byte)0x25,(byte)0xBB,
		          (byte)0x77,(byte)0x45,(byte)0x49,(byte)0x98,(byte)0x47,(byte)0xEB,(byte)0xF3,(byte)0x7D,(byte)0x7D,(byte)0x32,(byte)0x82,(byte)0x48,(byte)0x57,(byte)0xA9,
				  (byte)0xEF,(byte)0x22,(byte)0xEE,(byte)0xED};
		
		byte[] prikey = new byte[292];
//		byte []tmp = {0x00,0x01,0x07,0x00};
//		System.arraycopy(tmp, 0, prikey, 292-100, 4);
		System.arraycopy(pubkey, 0, prikey, 292-96, 64);
		System.arraycopy(prikey1, 0, prikey, 292-32, 32);
		 
//		byte[] prikey =  getInData("C:/Users/dean-user/Desktop/eccprikey1.dat");
		//????1
//		byte[] inputdata = {(byte)0x7A,(byte)0x76,(byte)0x9A,(byte)0x6F,(byte)0x02,(byte)0xB7,(byte)0x32,(byte)0x94,(byte)0xB9,(byte)0x75,(byte)0x3F,(byte)0x62,(byte)0x41,(byte)0x1F,
//		          (byte)0x26,(byte)0xA7,(byte)0x9D,(byte)0x1E,(byte)0xFB,(byte)0xDA,(byte)0x37,(byte)0xD9,(byte)0xF3,(byte)0x83,(byte)0xF1,(byte)0x55,(byte)0xBD,(byte)0x40,
//				  (byte)0x3F,(byte)0x9C,(byte)0xCF,(byte)0x7A};
		//????2
//		byte[] inputdata = {(byte)0xF5,0x5A,(byte)0xB4,0x31,0x6E,(byte)0xA9,0x6B,(byte)0xA4,(byte)0xF7,(byte)0x8E,0x29,(byte)0xA5,(byte)0xD9,(byte)0xBB,
//        0x5C,(byte)0xC2,(byte)0xAA,(byte)0xA8,(byte)0xD2,(byte)0xBB,(byte)0xBC,(byte)0xD8,(byte)0xDA,(byte)0xD3,(byte)0x9F,0x01,(byte)0x84,(byte)0xBB,
//		  0x19,0x5C,(byte)0xBA,0x7A};
		//????3
		byte[] inputdata = {(byte)0x1A,(byte)0xE7,(byte)0x41,(byte)0x75,(byte)0x1D,(byte)0xFF,(byte)0xDB,(byte)0x1F,(byte)0x2A,(byte)0xB0,(byte)0x23,(byte)0x9E,(byte)0x2C,(byte)0xA1,
		          (byte)0x68,(byte)0xF2,(byte)0xC7,(byte)0x79,(byte)0x2B,(byte)0x5D,(byte)0xF5,(byte)0x03,(byte)0x6C,(byte)0x76,(byte)0x9D,(byte)0xC3,(byte)0xEB,(byte)0x71,
				  (byte)0xE5,(byte)0x27,(byte)0x51,(byte)0x62};
		
		byte[] pubKey = new byte[260]; 
		System.arraycopy(prikey, 0, pubKey, 0, 260);
	 
		SignData = tmpTest.DA_SM2Sgin0(tmpHandle,inputdata,prikey);
		if (SignData == null) {
			System.out.println("[ERROR] DA_SM2Sgin error ...");
			return ;
		}
		else {
			System.out.println("[SUCCESS] DA_SM2Sgin success ...\n");

			// ??? Base64?????? DER????? SM2???????
			System.out.println(">>>> Base64 Der SignData[" + SignData.length() + "] = ");
			System.out.println(SignData);

			// ???16???? DER????? SM2???????
			byte[] encodeData = Base64.decode(SignData);
			System.out.println(">>>> Hex Der encodeData[" + encodeData.length + "] = ");
			System.out.println(bin2HexString(encodeData));

			// ???16????? SM2???????
			byte[] structData = KeyBase.signDerToStruct(encodeData);
			System.out.println(">>>> Hex structData[" + structData.length + "] = ");
			System.out.println(bin2HexString(structData));
		}
		//????1
//		byte [] sign = {0x54,0x0E,(byte)0xD5,0x5A,(byte)0xC7,0x17,0x7B,(byte)0xE8,0x40,0x69,(byte)0xAE,(byte)0xE7,(byte)0xC7,0x74,0x15,(byte)0xA1,(byte)0xBD,(byte)0xF9,(byte)0xA5,0x13,(byte)0xDA,0x19,
//		0x7D,0x0A,(byte)0xCE,(byte)0xC3,(byte)0xA8,0x7B,0x14,(byte)0x89,0x15,(byte)0x8D,0x18,0x66,0x50,(byte)0xCB,0x7E,(byte)0xFD,0x28,0x42,0x50,0x18,0x34,(byte)0xE8,0x1C,0x2E,
//		0x2E,(byte)0xF6,(byte)0x8B,0x62,0x10,0x4B,0x53,0x45,(byte)0x93,0x42,0x36,(byte)0x96,0x2B,0x3C,0x53,0x70,0x39,0x1C};
		//????2
//		byte [] sign = {(byte)0xFE,0x6E,0x3F,0x0A,(byte)0xA7,(byte)0xB9,0x68,0x4E,(byte)0xE4,0x22,(byte)0xE5,(byte)0xFA,(byte)0xD2,(byte)0xC0,
//		          0x68,0x40,(byte)0x8E,0x28,0x58,(byte)0xCC,(byte)0xFD,0x6A,0x1F,0x11,0x21,(byte)0x8A,(byte)0x83,(byte)0xC4,
//				  0x17,0x6E,0x08,0x14,0x5E,0x7E,0x2F,(byte)0xB7,(byte)0xB2,(byte)0x91,(byte)0xC2,0x57,0x46,(byte)0x98,
//				  0x0A,0x06,0x4A,(byte)0xA8,0x21,(byte)0xCC,0x51,0x4A,(byte)0xED,0x08,(byte)0xB0,(byte)0x98,0x11,0x43,
//				  0x02,0x79,0x2A,(byte)0xFC,(byte) 0xB4,0x46,(byte)0xB5,0x23};
		//????3
		byte [] sign = {(byte)0xF2,(byte)0xB7,(byte)0x41,(byte)0x94,(byte)0xAA,(byte)0x8F,(byte)0x41,(byte)0x2B,(byte)0xAE,(byte)0x06,(byte)0x73,(byte)0x8C,(byte)0x9F,(byte)0xC1,
		          (byte)0x59,(byte)0x97,(byte)0x3E,(byte)0xD7,(byte)0xE6,(byte)0xBD,(byte)0xB0,(byte)0x15,(byte)0xF6,(byte)0x26,(byte)0x7D,(byte)0xFC,(byte)0x7E,(byte)0x08,
				  (byte)0xA0,(byte)0xE1,(byte)0x7D,(byte)0xCA,(byte)0xF4,(byte)0x2B,(byte)0xAF,(byte)0x28,(byte)0xE5,(byte)0xE9,(byte)0x37,(byte)0x0F,(byte)0x29,(byte)0x81,
				  (byte)0x2D,(byte)0x75,(byte)0x9E,(byte)0x0E,(byte)0x15,(byte)0xA5,(byte)0x5E,(byte)0x11,(byte)0xF0,(byte)0xC7,(byte)0xC6,(byte)0xD3,(byte)0xF1,(byte)0xE5,
				  (byte)0xDC,(byte)0xCF,(byte)0xD0,(byte)0x99,(byte)0x63,(byte)0xDD,(byte)0x01,(byte)0x4A};
		 
	    byte[] sm2SignData=KeyBase.signStructToDer(sign);
		SignData = new String(Base64.encode(sm2SignData));
		
		boolean VerifyFlag = tmpTest.DA_SM2Verify(tmpHandle,inputdata,SignData, pubKey);
		if (!VerifyFlag) {
			System.out.println("[ERROR] DA_SM2Verify error ...");
			return ;
		}
		else {
			System.out.println("[SUCCESS] DA_SM2Verify success ...");
		}
	}
	 
	public static void testDA_SM2Verify() throws Exception {
		// ???SM2???
//		byte[] sm2Pub = tmpTest.DA_GetSM2PublicKey(tmpHandle, 2,1).getEncoded();
//		System.out.println("[SUCCESS] DA_GetSM2PublicKey ok ...");
		 
//		String aa="0047a9289c15f27f5a47ddfacc24d8061e2ad27d6295555efc570e22898587be71d74f9ed4bebf286b36f7699e00e174ebe571c4134c0af91dd560e361c56ed145";
//		byte[] sm2Pub=hexStringToByte(aa);
		 
		
//		byte [] data = "asqweqasasqweqasasqweqasasqweqasasqweqasasqweqasqweqasasqweqweqwewqasasqweqasasqweqasasqweqasqweqasqweqasqweqasqweqasqweqasqweqa".getBytes();
//		String  encodedata = new String(Base64.encode(data));
//		System.out.println("encodedata.length():"+encodedata.length());
//		System.out.println("encodedata:"+encodedata);
//		
//		byte [] signed1 = Base64.decode(encodedata.getBytes());
//		System.out.println("decodedata:"+bin2HexString(signed1));
		
		
 		byte[] data1 = getInData("E:/DE/cn/2017/0602???????????????????/??????????/signed(2).txt");
 		byte [] signed = Base64.decode(data1); 
 		System.out.println(">>>> Base64.decode(data1)[" + signed.length + "] = ");
		System.out.println(bin2HexString(signed));
    	byte[] signeddata = new byte[64];
    	System.arraycopy(signed, 64-32, signeddata, 0, 32);
    	System.arraycopy(signed, signed.length-33, signeddata, 32, 32);	 
    	signeddata[62] = 0x16;
    	signeddata[63] = (byte)0xe8;  	
    	byte[] sm2SignData=KeyBase.signStructToDer(signeddata);
 		SignData = new String(Base64.encode(sm2SignData));
 //		HashData = getInData("E:/DE/cn/2017/0602???????????????????/??????????/sm3(2).txt");
 		HashData = getInData("E:/DE/cn/2017/0602???????????????????/??????????/digest.der");
//		byte[] sm2Pub = getInData("E:/DE/cn/2017/0602???????????????????/??????????/pubkey(2).txt");	 
		byte[] sm2Pub = getInData("E:/DE/cn/2017/0602???????????????????/??????????/pubkey.der");	
 		String signdata = "MEQCIInsrvaJrFAuwzm+8UFCauHkti7l6oB1JOgJaK36fl7+AiCqEPeZKDmfP6ZLncL1QsA/JyEMcrZRI+AYwfFzIgAT8w==";
//		byte[] signeddata = {0x01,0x54,(byte)0xd4,0x76, (byte)0xc2,0x3f,0x4d,(byte)0x91,0x2c,0x58,0x1e,0x44,
//				0x15,0x2c,(byte)0xef,(byte)0xab,(byte)0x8e,0x31,(byte)0xaa,(byte)0xfc,
//				0x22,0x3e,0x5e,(byte)0xd0,(byte)0xcc,(byte)0xc0,(byte)0xfc,0x60,
//				(byte)0xbb,(byte)0xc7,(byte)0xf7,0x03,
//				(byte)0xc3,
//				0x53,(byte)0xf3,(byte)0xd5,0x17,(byte)0xd5,(byte)0x91,0x42,0x43,
//				0x76,0x34,0x3e,(byte)0xac,0x3d,(byte)0xd1,0x47,(byte)0x94,
//				0x3f,0x47,0x6b,0x40,0x1c,0x09,0x01,0x4e,
//				0x2a,0x73,(byte)0xbc,0x04,0x43,0x7f,0x50};
//    	System.out.println(">>>> signed11[" + signeddata.length + "] = ");
//		System.out.println(bin2HexString(signeddata));
//		byte[] sm2SignData=KeyBase.signStructToDer(signeddata);
// 		SignData = new String(Base64.encode(sm2SignData));
// 		HashData = getInData("C:/Users/dean-user/Desktop/2017/0602???????????????????/??????????/sm3(3).txt");
// 		byte[] sm2Pub = getInData("C:/Users/dean-user/Desktop/2017/0602???????????????????/??????????/pub_t3.txt");
 		
		// ??????????????? SM2???
		boolean VerifyFlag = tmpTest.DA_SM2Verify(tmpHandle, HashData, signdata, sm2Pub);
		if (!VerifyFlag) {
			
			System.out.println("[ERROR] DA_SM2Verify error ...");
			return ;
		}
		else {
			System.out.println("[SUCCESS] DA_SM2Verify success ...");
		}
	}

	// ??????????
	public static byte[] getInData(String fileName) throws Exception {
		FileInputStream tmpInput = new FileInputStream(fileName);
		int InDataLen = tmpInput.available();

		byte[] InData = new byte[InDataLen];

		tmpInput.read(InData);
		tmpInput.close();

		System.out.println("[Info] Read File:{" + fileName + "}, Read Length:{" + InDataLen + "} ...");

		return InData;	
	}
	
	// 2???????? ???? 16?????????
	public static String bin2HexString(byte[] tmpBin) {
		StringBuilder tmpStrB = new StringBuilder();

		for(int i=0; i<tmpBin.length; i++) {
			tmpStrB.append(String.format("%02x ", tmpBin[i]));
			if (i%16 == 15) {
				tmpStrB.append("\n");
			}
		}

		return new String(tmpStrB);
	}
	
	public static void testDA_ImportCipherSM2Key() throws Exception {
		int rv = -1;
		// ????????? SM2???
		// String inEncData = "AQAAAAEBAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADc90zT5/PImKSQ9nIgx1DDJRttU122SLWDrZPQb0IqLQABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAGuRslwa1IHxxs1tkyYsLh8zMjL+0rqUXbqMdB1eb2wwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA0pX2VH5QVxzAs/+YaNU+TCStQjOkFQ2nxN2ZBL0sycwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAUVLvLDjIBTgz1yiLBCGVO0BiBBApRCNZFJale3/QA/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAALbKMdvWgvuY2f0d3k268IpWFHvF3s1gBpeaG8+2jJsY8qoFaFKqeQ4ulvMbD1nGB8mvSUrrb/lx8iEaMqYyFzQQAAAAHgsAklpGFjRUcQeH658uBA==";
	//	String inEncData = "AQAAAAQBAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACBsDeQf4bzo0HAzG+8iuL7tr3fDTOCwpQl614G4sVQ3AABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACHsgQvuf9dvqXBgBuyiBHmkFMT3Yz1S3VsNMqtFbpfnAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABcz0y8wE8twzKdtPgrM+ZnGXxU1X9r6iQxqmaL0cZndgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAASis+sb5PC8oBWJR/j8Ao+2lh29LwQHzfy0XHLZTjxNIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAOgv1kjigumLp8t/Oroa8Nlu/AZO5j2JslKoxSLC4SQD7/fKB8El0L4iawbGvtnksnhbEnbGKBO04AMYa4FB96oQAAAAhGV1SFcQ7xuKLtJQZubGFw==";
//	    String inEncData="AQAAAAQBAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAC6j8tb9WGAq8J4cUkQ\n" +
//				"EPglu7gpgyUQfSbhziy/8pfajQABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANUO\n" +
//				"4Q+V4MfuNMkTV4caOSI0sb6+sYcL8T/D/s5MFDZ5AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n" +
//				"AAAAAACMH/t9I5oviQqNuqXxHRc0+oexMru+jWRX+8uPwzF0bQAAAAAAAAAAAAAAAAAAAAAAAAAA\n" +
//				"AAAAAAAAAAAAAAAA4C3/uOme2VGy5ThIXmDG/y15dGJyjon5MBvcIoPgWOwAAAAAAAAAAAAAAAAA\n" +
//				"AAAAAAAAAAAAAAAAAAAAAAAAAE0Mt1aVWzNyO591QYErSdlCv2ICIzd0geP17aB3faGiF+MQDe9k\n" +
//				"n43olOMvSWzceU0lGZRJ9vRL9vDmaTeXlPEQAAAA3IsI9ykQ3F+bGHEImQ8ndQ==";
		  String inEncData="AQAAAAQBAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACBsDeQf4bzo0HAzG+8iuL7tr3fDTOCwpQl614G4sVQ3AABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACHsgQvuf9dvqXBgBuyiBHmkFMT3Yz1S3VsNMqtFbpfnAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABcz0y8wE8twzKdtPgrM+ZnGXxU1X9r6iQxqmaL0cZndgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAASis+sb5PC8oBWJR/j8Ao+2lh29LwQHzfy0XHLZTjxNIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAOgv1kjigumLp8t/Oroa8Nlu/AZO5j2JslKoxSLC4SQD7/fKB8El0L4iawbGvtnksnhbEnbGKBO04AMYa4FB96oQAAAAhGV1SFcQ7xuKLtJQZubGFw==";
		rv  = tmpTest.DA_ImportCipherSM2Key(tmpHandle, 78, 79, inEncData);
		if (rv == 0) {
			System.out.println("[SUCCESS] DA_ImportCipherSM2Key rv = \n" + rv); 
		}
		else {
			System.out.println("[ERROR] DA_ImportCipherSM2Key error ...rv = " + rv);
			return ;
		}
	}

	public static void testDA_GetBase64EccPublicKey() throws Exception {
		String rv = null;
		// ????????? SM2???
		String inEncData = "";

		//byte[] pubKey = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x09, 0x10};
		//byte[] tmpPubKey = null;

		//System.arraycopy(pubKey, 2, tmpPubKey, 0, 2); 

		System.out.println("[SUCCESS] start\n" ); 
		
		rv  = tmpTest.DA_GetBase64EccPublicKey(tmpHandle, 1);
		if (rv == null)
		{
			System.out.println("[ERROR] DA_GetBase64EccPublicKey error ...rv = " + rv);
		}
		else 
		{
			System.out.println("[SUCCESS] DA_GetBase64EccPublicKey\n" ); 
			System.out.println("[Data] Base64 Public Key:" + rv); 
			return ;
		}
	}
}
