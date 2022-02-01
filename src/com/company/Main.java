package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.print("Введите команду (code/decode) и" +
                " имя текстового файла: ");
        String variant = in.next();
        String fileName = in.next();
        if (variant.equals("decode")) {  //decode
            System.out.println("decoding...");
            DecodeMorse dc = new DecodeMorse(fileName);
            dc.getTxtOrKode();
        } else {
            System.out.println("coding...");
            CodeMorse dc = new CodeMorse(fileName);
            dc.getTxtOrKode();


        }
    }
}

