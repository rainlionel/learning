public class parentClass {
    static {
        System.out.println("parent static block");
    }

    public parentClass() {
    }

    public parentClass(int val){
        System.out.println("parent "+val);
    }
}
