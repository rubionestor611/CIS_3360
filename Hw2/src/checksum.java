public class checksum {
    public static void main(String[] args) {
        String textfile = args[0];
        int inputsize = Integer.parseInt(args[1]);
        System.out.println(textfile);
        System.out.println(inputsize);
        if(isValidChecksumSize(inputsize)){
            printFormat("continuing with checksum...");
        }
        else{
            System.err.println("Valid checksum sizes are 8, 16, or 32\\n");
        }

    }
    public static boolean isValidChecksumSize(int n){
        return switch (n) {
            case 8 -> true;
            case 16 -> true;
            case 32 -> true;
            default -> false;
        };
    }
    public static void printFormat(String s) {
        int len = s.length();
        for (int i = 0; i < len; i++) {
            System.out.print(s.charAt(i));
            if (i != 0 && i % 80 == 0) {
                System.out.print("\n");
            }
        }
    }
}
