import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

public class redistest {
    public static void main(String[] args) {
        Jedis jd=new Jedis("192.168.119.14",6379);
        jd.set("addr","nnn");
        String value=jd.get("addr");
        System.out.println(value);
        jd.close();

        //JedisPoolConfig config=new JedisPoolConfig();




    }

}
