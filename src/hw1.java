/*=============================================================================
| Assignment: HW 01 – Encrypting a plaintext file using the Hill cipher in the key file
|
| Author: Nestor Rubio
| Language: Java
|
| To Compile: javac Hw01.java
|
| To Execute: java Hw01 hillcipherkey.txt plaintextfile.txt
| where the files in the command line are in the current directory.
| The key text contains a single digit on the first line defining the size of the key
| while the remaining lines define the key, for example:
| 3
| 1 2 3
| 4 5 6
| 7 8 9
| The plain text file contains the plain text in mixed case with spaces & punctuation.
|
| Class: CIS3360 - Security in Computing - Fall 2020
| Instructor: McAlpin
| Due Date: per assignment
|
+=============================================================================*/
import java.io.File;
import java.util.Scanner;
//stu: 0- correct,1- correct,2 - correct,3- passed
public class hw1 {
    public static void main(String[] args){
        //read in key matrix and print it
        int[][] matrix = readMatrix(args[0]);
        printMatrix(matrix, matrix.length);


        //read and print plaintext, stripped of whitespace
        //and numbers
        String plaintxt = readPlaintxt(args[1]);
        plaintxt = checkPadding(plaintxt, matrix.length);
        printPlaintxt(plaintxt);
        System.out.print("\n");
        String ciphertext = hillCipher(plaintxt, matrix);
        printCiphertxt(ciphertext);
        System.out.print("\n");
    }
    public static int[][] readMatrix(String matrixfile){
        //int[row][col]
        File key = new File(matrixfile);
        int size;
        int[][] ret = new int[1][1];
        try {
            Scanner reader = new Scanner(key);
            String line = reader.nextLine();
            size = Integer.parseInt(line);
            ret = new int[size][size];
            while(reader.hasNextLine()){
                for (int i = 0; i < size; i++) {
                    String[] vals = reader.nextLine().trim().split(" ");
                    for (int j = 0; j < vals.length; j++) {
                        ret[i][j] = Integer.parseInt(vals[j]);
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return ret;
    }
    public static void printMatrix(int[][] matrix, int len){
        System.out.println("Key Matrix:\n");
        for(int i = 0; i < len; i++){
            for(int j = 0; j < len; j++){
                System.out.print(matrix[i][j] + "  ");
            }
            System.out.print("  \n");
        }
        System.out.println("\n");
    }
    public static String readPlaintxt(String plaintxtfile){
        File plaintext = new File(plaintxtfile);
        String str = "";
        try{
            Scanner scan = new Scanner(plaintext);
            while(scan.hasNextLine()){
                str += scan.nextLine();
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        String[] array = str.split("\\W+");
        String ret = new String();
        for(String s : array){
            if(!Character.isDigit(s.charAt(0))){
                ret += s.toLowerCase();
            }
        }
        return ret;
    }
    public static String checkPadding(String txt, int size){
        int remainder = txt.length() % size;

        if(remainder != 0){
            int pad = size - remainder;
            for(int i = 0; i < pad; i++){
                txt += "x";
            }
        }
        return txt;
    }
    public static void printPlaintxt(String txt){
        System.out.println("Plaintext:\n");
        formattedPrint(txt);
        System.out.print("\n");
    }
    public static void printCiphertxt(String txt){
        System.out.println("Ciphertext:\n");
        formattedPrint(txt);
    }
    public static void formattedPrint(String txt){
        for(int i = 0; i < txt.length(); i++){
            System.out.print(txt.charAt(i));
            if(i % 80 == 79){
                System.out.print("\n");
            }
        }
        //System.out.print("\n");
    }
    public static String hillCipher(String plaintxt, int[][] matrix){
        int[][] blockofplain = new int[matrix.length][1];
        String ciphertext = "";
        int len = plaintxt.length();
        for(int i = 0; i < len; i++){
            int val = intforLetter(plaintxt.charAt(i));
            blockofplain[i % matrix.length][0] = val;
            if(i % matrix.length == matrix.length - 1){
                int[][] cipherblock = encryptBlock(blockofplain, matrix);
                for(int k = 0; k < cipherblock.length; k++){
                    ciphertext += letterforInt(cipherblock[k][0]);
                }
            }
        }
        return ciphertext;
    }
    public static int[][] encryptBlock(int[][] plaintextblock, int[][] kmatrix){
        int[][] cipherblock = new int[kmatrix.length][1];
        for(int i = 0; i < kmatrix.length; i++){
            for(int a = 0; a < 1; a++){
                 cipherblock[i][a] = 0;
                 for(int j = 0; j < kmatrix.length; j++){
                     cipherblock[i][a] += (kmatrix[i][j] * plaintextblock[j][a]);
                 }
                 cipherblock[i][a] = cipherblock[i][a] % 26;
            }
        }
        return cipherblock;
    }
    public static String letterforInt(int a){
        char[] array = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        if(a < 0 || a >= array.length){
            return String.valueOf(a);
        }
        return String.valueOf(array[a]);
    }
    public static int intforLetter(char c){
        String array = "abcdefghijklmnopqrstuvwxyz";
        return array.indexOf(c);
    }

}
/*=============================================================================
| I Nestor Rubio (ne458269) affirm that this program is
| entirely my own work and that I have neither developed my code together with
| any another person, nor copied any code from any other person, nor permitted
| my code to be copied or otherwise used by any other person, nor have I
| copied, modified, or otherwise used programs created by others. I acknowledge
| that any violation of the above terms will be treated as academic dishonesty.
+=============================================================================*/