package me.syes.kits.utils;

import java.util.TreeMap;

public class TextUtils {


    private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();

    static {

        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

    }

    public final static String toRoman(int number) {
        int l =  map.floorKey(number);
        if ( number == l ) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number-l);
    }

    public static String toUpperCamelCase(String str) {
        String[] split1 = str.split(" ");
        String itemname = "";
        for(int i = 0; i < split1.length; i++) {
            String[] split2 = split1[i].split("");
            for(int z = 0; z < split2.length; z++) {
                if(z == 0) {
                    itemname = itemname + split2[z].toUpperCase();
                    continue;
                }
                itemname = itemname + split2[z];
            }
            if(i < split1.length-1)
                itemname = itemname + " ";
        }
        return itemname;
    }

}
