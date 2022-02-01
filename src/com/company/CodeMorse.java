package com.company;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import java.io.IOException;
import java.util.TreeSet;

public class CodeMorse {

    Map<Character, String> morsecode = new HashMap<>();
    private final String fileName;
    public CodeMorse(String fileName) {
        this.fileName = fileName;
        {
            //откроем файл текстовый с азбукой и загоним в map
            BufferedReader reader = null;

            try {
                File file = new File("morse.txt");
                //создаем объект FileReader для объекта File
                FileReader fr = new FileReader(file);
                //создаем BufferedReader с существующего FileReader для построчного считывания
                reader = new BufferedReader(fr);
                // считаем сначала первую строку
                String line = reader.readLine();
                while (line != null) {
                    //==файл в формате символ пробел код морзе
                    String[] splitVal = line.split(" ");

                    this.morsecode.put(splitVal[0].charAt(0), splitVal[1]);

                    // считываем остальные строки в цикле
                    line = reader.readLine();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != reader) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace(System.err);
                    }
                }
            }
        }


    } //end constructor

    void coding(StringBuilder str, Character ch, TreeSet<PairStat> freq) {

        if (Character.isDigit(ch) || Character.isLetter(ch)) {

            String get = this.morsecode.get(ch);  //код морзе на букву
            //System.out.println(get);
            if (get != null) str.append(get);
            PairStat pair = new PairStat(ch, 1);  //считаем частоту буквы
            freq.add(pair);

        } else str.append(ch);
        str.append(" ");


    }  //end coding

    //------------------------------
    void getTxtOrKode() {
        Reader reader = null;

        try {
            //System.out.println(this.type);
            reader = new InputStreamReader(new FileInputStream(this.fileName));
            StringBuilder encode = new StringBuilder();

            TreeSet<PairStat> freq = new TreeSet<>() {
                @Override   //переопределение добавления
                public boolean add(PairStat o) {
                    boolean AllOk = super.add(o);  //вызов родительского метода
                    if (!AllOk) {   //уже есть в наборе сочетание такое
                        for (PairStat p : this) {  //итератор
                            if (o.getKey().equals(p.getKey())) {
                                p.setValue(p.getValue() + 1);
                                AllOk = true;
                            }
                        }
                    }
                    return AllOk;
                }

            };


            boolean delline = false;

            //кодирование строку в код морзе
            // читаем посимвольно
            int c;
            char ch;

            while ((c = reader.read()) != -1) {
                if (c == 13 || c == 10) { //конец строки
                    if (!delline)

                        encode.append('\n');
                    delline = !delline;
                } else {
                    ch = Character.toUpperCase((char) c); //в верхн регистр


                    this.coding(encode, ch, freq);

                }
            }

            System.out.println(encode.toString());

            try (FileWriter writer = new FileWriter("statistic.txt", false)) {
                for (PairStat p : freq) {

                    Integer i = p.getValue();
                    String text = i.toString();
                    writer.append(p.getKey());
                    writer.append("->");
                    writer.append(text);
                    writer.append("\n");

                }
                writer.flush();
            } catch (IOException ex) {

                System.out.println(ex.getMessage());
            }


        }  //end try

        catch (IOException e) {
            System.err.println("Error while reading file: " + e.getLocalizedMessage());
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }


    }// end getTxtOrKode()




}
