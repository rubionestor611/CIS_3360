/*=============================================================================
| Assignment: HW 02 – Calculating the 8, 16, or 32 bit checksum for a
|             given input file
|
| Author: Nestor Rubio
| Language: Java
|
| To Compile: javac Hw02.java
|
| To Execute: java Hw02 textfile.txt checksum_size
|             where the files in the command line are in the current directory.
|
|             The text file contains text is mixed case with spaces, punctuation,
|             and is terminated by the hexadecimal ‘0A’, inclusive.
|             (The 0x’0A’ is included in the checksum calculation.)
|
|             The checksum_size contains digit(s) expressing the checksum size
|             of either 8, 16, or 32 bits
|
| Class: CIS3360 - Security in Computing - Fall 2020
| Instructor: McAlpin
| Due Date: per assignment
|
+=============================================================================*/
import java.io.File;
import java.util.ArrayList;
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
        String paddedinputtxt = padifNecessary(inputtxt, inputsize / 8);

        printFormat(paddedinputtxt);
        int checksum = doCheckSum(paddedinputtxt, inputsize);
        printFinal(inputsize, checksum, paddedinputtxt.length());

    }

    private static String padifNecessary(String inputtxt, int modvalue) {
        int need = inputtxt.length() % modvalue;
        if(modvalue == 4){
            need = modvalue - need;
            while(need != 0){
                inputtxt += 'X';
                need--;
            }
            return inputtxt;
        }
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
        return n == 8 || n == 16 || n == 32;
    }
    public static void printFormat(String s) {
        System.out.print("\n");
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
        System.out.print(x +" bit checksum is " + Integer.toHexString(y) + " for all " + z + " chars\n");
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
        long ret = 0L;
        int len = txt.length();
        long setA = 0L;
        long modval = Integer.MAX_VALUE;
        ArrayList<Long> al = new ArrayList<>();
        for(int i = 0; i <= len - 4; i += 4){
            setA = 0L;
            for(int j = 0; j < 4; j++){
                setA += asciiValue(txt.charAt(i + j));
                if(j != 3){
                    setA = setA << 8;
                }
                setA %= modval;
            }
            al.add(setA);
            ret %= (modval);
        }
        int need = len % 4;
        if(need != 0){
            need = 4 - need;
            for(int j = need; j > 0; j--){
                setA += asciiValue(txt.charAt(txt.length() - j));
                setA = setA << 8;
            }
            al.add(setA);
        }

        for(int i =0 ; i < al.size(); i++){
            ret += al.get(i);
        }
        return (int) ret;
    }
}
/*=============================================================================
| I Nestor Rubio (4834890) affirm that this program is
| entirely my own work and that I have neither developed my code together with
| any another person, nor copied any code from any other person, nor permitted
| my code to be copied or otherwise used by any other person, nor have I
| copied, modified, or otherwise used programs created by others. I acknowledge
| that any violation of the above terms will be treated as academic dishonesty.
+=============================================================================*/
