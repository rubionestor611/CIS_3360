import java.io.File;
import java.util.Scanner;

public class checksum {
    public static void main(String[] args) {
        int inputsize = validateChecksumSize(args[1]);
        if(inputsize == -1){
            System.exit(-1);
        }
        String inputtxt = readInputFile(args[0]);
        if(inputtxt == null){
            System.exit(-1);
        }
        System.out.println("inputsize = " + inputsize + "\ntextlength = " + inputtxt.length());
        inputtxt = padifNecessary(inputtxt, inputsize / 8);
        printFormat(inputtxt);
        int checksum = doCheckSum(inputtxt, inputsize);
        printFinal(inputsize, checksum, inputtxt.length());

    }

    private static String padifNecessary(String inputtxt, int modvalue) {
        int need = inputtxt.length() % modvalue;
        while(need != 0){
            inputtxt += 'X';
            need--;
        }
        return inputtxt;

    }

    public static String readInputFile(String input){
        File inputfile = new File(input);
        String ret = "";
        try{
            Scanner scan = new Scanner(inputfile);
            while(scan.hasNextLine()){
                ret += scan.nextLine();
            }
            ret += '\n';
            return ret;
        } catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static int validateChecksumSize(String s){
        try{
            int ret = Integer.parseInt(s);
            if(isValidChecksumSize(ret))
                return ret;
            else{
                System.err.println("Valid checksum sizes are 8, 16, or 32\n");
                return -1;
            }
        } catch(Exception e){
            return -1;
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
            if (i % 80 == 79) {
                System.out.print("\n");
            }
        }
        System.out.println("");
    }
    public static void printFinal(int x, int y, int z){
        System.out.println(x +" bit checksum is " + Integer.toHexString(y) + " for all " +
                z + " chars\n");
    }
    public static int doCheckSum(String txt, int bitSize){
        int checksumvalue = -1;
        if(bitSize == 8) {//works perfectly
            checksumvalue =  do8bitChecksum(txt);
        }
        if(bitSize == 16){
            checksumvalue =  do16bitChecksum(txt);
        }
        if(bitSize == 32){
            checksumvalue =  do32bitChecksum(txt);
        }
        return checksumvalue;
    }
    public static int asciiValue(char c){
        return (int) c;
    }
    public static int do8bitChecksum(String txt){

        int ret = 0;
        int len = txt.length();
        for(int i = 0; i <= len - 2; i += 2){
            ret += (asciiValue(txt.charAt(i + 1)) + asciiValue(txt.charAt(i)));
            ret %= 256;
        }
        if(len % 2 != 0){
            ret += asciiValue(txt.charAt(len -1));
            ret %= 256;
        }
        return ret;
    }
    public static int do16bitChecksum(String txt){
        /*
        add every two letters modding by 65_535
        bit-shift by 8 as you read in the next one
         */
        int ret = 0;
        int len = txt.length();
        int pairA = 0;
        int pairB = 0;
        int pairsadded = 0;
        int modval = 65_536;

        for(int i = 0; i <= len - 4; i += 4){
            pairA = asciiValue(txt.charAt(i));
            pairA = pairA << 8;
            pairA += asciiValue(txt.charAt(i + 1));
            pairB = asciiValue(txt.charAt(i + 2));
            pairB = pairB << 8;
            pairB += asciiValue(txt.charAt(i + 3));
            pairsadded = pairA + pairB;
            pairsadded %= modval;
            ret += pairsadded;
            ret %= modval;
        }
        int need = len % 4;
        if(need == 1){
            pairA = asciiValue(txt.charAt(len - 1));
            pairA = pairA << 8;
            ret += pairA;
            ret %= modval;
        }else if(need == 2){
            pairA = asciiValue(txt.charAt(len - 2));
            pairA = pairA << 8;
            pairA += asciiValue(txt.charAt(len - 1));
            ret += pairA;
            ret %= modval;
        }else if(need == 3){
            pairA = asciiValue(txt.charAt(len - 3));
            pairA = pairA << 8;
            pairA += asciiValue(txt.charAt(len - 2));
            pairB = asciiValue(txt.charAt(len - 1));
            pairB = pairB << 8;
            ret += pairA + pairB;
            ret %= modval;
        }
        return ret;
    }
    public static int do32bitChecksum(String txt){
        /*
        add every 4 letter modding by 2_147_483_647
         */
        int ret = 0;
        int len = txt.length();
        for(int i = 0; i <= len - 2; i += 2){
            System.out.println(txt.charAt(i + 1) + "+" + txt.charAt(i) + '=' + (asciiValue(txt.charAt(i + 1)) + asciiValue(txt.charAt(i))));
            ret += (asciiValue(txt.charAt(i + 1)) + asciiValue(txt.charAt(i)));
            ret %= 2_147_483_647;
        }
        if(len % 2 != 0){
            ret += asciiValue(txt.charAt(len -1));
            ret %= 2_147_483_647;
        }
        return ret;
    }

}
