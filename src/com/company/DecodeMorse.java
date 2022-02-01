package com.company;
import java.io.*;
import java.util.HashMap;
//import java.util.HashSet;
import java.util.Map;

import java.io.IOException;
import java.util.TreeSet;

public class DecodeMorse {

    Map<String, Character> morsecode = new HashMap<>();

    private final String fileName;

    //--------------------------------------------------
    public DecodeMorse(String fileName) {

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

                    this.morsecode.put(splitVal[1], splitVal[0].charAt(0));


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


    //-----------------------------------
    void decoding(StringBuilder str, String smorse, TreeSet<PairStat> freq) {


        Character getKode = this.morsecode.get(smorse); //букву на код морзе

       // System.out.printf(" GET ");
        //System.out.println(getKode);
        //System.out.printf(" smorse %s \n", smorse);*/
        PairStat pair = new PairStat(getKode, 1);
        if (getKode != null) {
            str.append(getKode);
            freq.add(pair);
        } else str.append(smorse);


    }  //end decodering




    //------------------------------
    void getTxtOrKode() {
        Reader reader = null;

        try {
            //System.out.println(this.type);
            reader = new InputStreamReader(new FileInputStream(this.fileName));
            StringBuilder encode = new StringBuilder();
            // HashSet<PairStat> freq = new HashSet<>() ;
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


            //------------------декодирование кода морзе в строку
            int c, flag = 0;
            StringBuilder str = new StringBuilder();


            while ((c = reader.read()) != -1) {
                //наращивать строку пока не встретится первый пробел и передача для декодировки
                // елси далее символы управляющие-пробеловЮ переводы строки, то нет декодирования,
                // а наращивание результирующей строки
                // System.out.println(c);
                if (c == ' ') {
                    if (flag > 0) {
                        encode.append(" ");
                        flag = 0;
                    } else {
                        flag = 1;
                        if (!str.isEmpty()) {
                            this.decoding(encode, str.toString(), freq);
                            str.delete(0, str.length());
                        }
                    }

                }  //не пробел
                else {

                    if (c == 13 || c == 10) {  //конец строки
                        if (!str.isEmpty()) {

                            this.decoding(encode, str.toString(), freq);
                            str.delete(0, str.length());
                        }

                        if (!delline)
                            encode.append('\n');
                        delline = !delline;
                        flag = 0;
                    } else {
                        flag = 0;
                        //System.out.printf("char- "+ (char) c);
                        str.append((char) c);
                    }

                }
            }


           System.out.println(encode.toString());
           try (FileWriter writer = new FileWriter("statistic.txt", false)) {
               for (PairStat p : freq) {
                    // System.out.printf(" %c - %d \n", p.getKey(), p.getValue());
                    // запись  строки
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
