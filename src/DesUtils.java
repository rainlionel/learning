import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;

public class DesUtils {
    /** 字符串默认键值     */
    public static String strDefaultKey = "leemenz";

    /** 电子签章系统有效期加密密钥*/
    public static String strAvaliableDateKey="dianju111111";

    /** 电子签章系统用户数加密密钥*/
    //public static String strUserNumKey="dianju222222";
    public static String strUserNumKey="aadianju222222";

    /** 电子签章系统印章数加密密钥*/
    // public static String strSealNumKey="dianju333333";
    public static String strSealNumKey="bbdianju333333";

    /** 电子签章系统授权单位加密密钥*/
    public static String strUnitKey="dianju444444";

    /** 电子签章系统初始化菜单加密密钥*/
    public static String strMenuKey="dianju555555";

    /** 加密工具     */
    private Cipher encryptCipher = null;

    /** 解密工具     */
    private Cipher decryptCipher = null;
    /** key     */
    public String strKey="leemenz";

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
     * hexStr2ByteArr(String strIn) 互为可逆的转换过程
     *
     * @param arrB
     *            需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception
     *             本方法不处理任何异常，所有异常全部抛出
     */
    public static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
     * 互为可逆的转换过程
     *
     * @param strIn
     *            需要转换的字符串
     * @return 转换后的byte数组
     * @throws Exception
     *             本方法不处理任何异常，所有异常全部抛出
     * @author <a href="mailto:leo841001@163.com">LiGuoQing</a>
     */
    public static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

//	  /**
//	   * 默认构造方法，使用默认密钥
//	   *
//	   * @throws Exception
//	   */
//	  public DesUtils() throws Exception {
//	    this(strDefaultKey);
//	  }

    /**
     * 指定密钥构造方法
     *
     * @param strKey
     *            指定的密钥
     * @throws Exception
     */
    public DesUtils(String strKey) throws Exception {
        Key key = getKey(strKey.getBytes());
        encryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        decryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }
    public DesUtils() throws Exception {
        Key key = getKey(strKey.getBytes());
        encryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        decryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }

    /**
     * 加密字节数组
     *
     * @param arrB
     *            需加密的字节数组
     * @return 加密后的字节数组
     * @throws Exception
     */
    public byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);
    }

    /**
     * 加密字符串
     *
     * @param strIn
     *            需加密的字符串
     * @return 加密后的字符串
     * @throws Exception
     */
    public String encrypt(String strIn) throws Exception {
        return byteArr2HexStr(encrypt(strIn.getBytes("utf-8")));
    }

    /**
     * 解密字节数组
     *
     * @param arrB
     *            需解密的字节数组
     * @return 解密后的字节数组
     * @throws Exception
     */
    public byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }

    /**
     * 解密字符串
     *
     * @param strIn
     *            需解密的字符串
     * @return 解密后的字符串
     * @throws Exception
     */
    public String decrypt(String strIn) throws Exception {
        return new String(decrypt(hexStr2ByteArr(strIn)),"utf-8");
    }
    //解密有效期
    public String decryptDate(String strIn) throws Exception {
        return new String(decrypt(hexStr2ByteArr(strIn)));
    }
    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     *
     * @param arrBTmp
     *            构成该字符串的字节数组
     * @return 生成的密钥
     * @throws java.lang.Exception
     */
    private Key getKey(byte[] arrBTmp) throws Exception {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];
        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        // windows生成密钥
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        DESKeySpec keySpec = new DESKeySpec(arrB);
        keyFactory.generateSecret(keySpec);
        return keyFactory.generateSecret(keySpec);
        //防止linux下 随机生成key
//	    KeyGenerator _generator = KeyGenerator.getInstance("DES");
//        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
//        secureRandom.setSeed(arrB);
//        _generator.init(56,secureRandom);
//        return _generator.generateKey();
    }

    /**
     * main方法  。
     * @author
     * @param args
     */
    public static void main(String[] args) {
        try {

            String test = "2999-06-30";//加密原文
            DesUtils des=null;
            des = new DesUtils(strAvaliableDateKey);//有效期密钥
            des = new DesUtils(strUserNumKey);//用户密钥
            des = new DesUtils(strSealNumKey);//印章密钥
            //des=new DesUtils(strUnitKey);//单位授权
            System.out.println("加密前的字符：" + test);
            System.out.println("加密后的字符：" + des.encrypt(test));
            // String enc="21a6573f1fa013fa2cf103da2bbfae4d";
            ///System.out.println("加密后的字符长度：" + des.encrypt(test).length());
            // System.out.println("解密后的字符：" + des.decrypt(des.encrypt(test)));
            //  System.out.println("解密后的字符：" + des.decrypt("3b9aa8877a036cbe9fc7a099c9a61910"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
