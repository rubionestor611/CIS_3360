import java.io.File;
import java.util.Scanner;

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
public class hw1 {
    public static void main(String[] args){
        //read in key matrix and print it
        int[][] matrix = readMatrix(args[0]);
        printMatrix(matrix, matrix.length);

        //read and print plaintext, stripped of whitespace
        //and numbers
        String plaintxt = readPlaintxt(args[1]);
        printPlaintxt(plaintxt);
        /*
        encrypt
        a b c  x
        d e f  y
        g h i  z
        a*x + b*y + c*z
        d*x + e*y + f*z
        g*x + h*y + i*z
        lmn%26
        opq%26
        rst%26
        u
        v
        w
         */

        //print ciphertext
    }
    public static int[][] readMatrix(String matrixfile){
        //int[row][col]
        File key = new File(matrixfile);
        int size =0;
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
        System.out.println("Matrix: ");
        for(int i = 0; i < len; i++){
            for(int j = 0; j < len; j++){
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("\n");
        }
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
            ret += s.toLowerCase();
        }
        int remainder = ret.length() % 5;
        if(remainder != 0){
            for(int i = 0; i < remainder; i++){
                ret += "x";
            }
        }
        return ret;
    }
    public static void printPlaintxt(String txt){
        System.out.println("Plaintext:");
        formattedPrint(txt);
    }
    public static void printCiphertxt(String txt){
        System.out.println("Ciphertext:");
        formattedPrint(txt);
    }
    public static void formattedPrint(String txt){
        for(int i = 0; i < txt.length(); i++){
            System.out.print(txt.charAt(i));
            if(i % 80 == 79){
                System.out.print("\n");
            }
        }
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