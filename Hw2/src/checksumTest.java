import java.util.Scanner;

class checksumTest {
    public static void main(String[] args){
        String[] arg = new String[2];
        Scanner s = new Scanner(System.in);
        System.out.print("text file: ");
        arg[0] = s.nextLine();
        System.out.print("checksum size: ");
        arg[1] = s.nextLine();
        checksum.main(arg);
    }
}