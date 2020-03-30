import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class demo01 extends parentClass {
public demo01(){
    super(34);
    System.out.println("in demo01 construct");
}
public void printsate(){
    System.out.println("just in one line");
}
//    private Integer in;
//    private int na;
//    private String three;
//
//    public demo01() {
//        System.out.println("B");
//        three="111";
//    }

    public static void main(String[] args) {
//        demo01 d1=new demo01();
//        System.out.println(d1.in);
//        System.out.println(d1.na);
//        System.out.println(d1.three);
//        ArrayList<Integer> lsit=new ArrayList<>(4);
//        lsit.add(1);
//        lsit.add(5);
//        lsit.add(3);
//        Collections.sort(lsit);
//        lsit.set(3,2);
//        Collections.reverse(lsit);
//        System.out.println(lsit);
        new demo01();
    }
}
