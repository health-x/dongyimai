package com.health;

import org.junit.Test;

import java.util.*;

public class Demo {

    @Test
    public void test01(){
        Set set = new HashSet();
        int[] arr = {3,2,3};
        int n = arr.length;
        Map<Integer,Integer> map = new HashMap();
        for (int i = 0; i < arr.length; i++) {
            if (map.get(arr[i])==null){
                map.put(arr[i],1);
            }else {
                map.put(arr[i],map.get(arr[i])+1);
            }
        }
        Iterator<Map.Entry<Integer, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Integer key = iterator.next().getKey();
            Integer value = map.get(key);
            if (value>(n/2)){
                set.add(key);
                System.out.println(key);
            }
        }
        System.out.println(set);
    }


    @Test
    public void test02(){
        String str = "a1b1c1d1efg1";
        String str2 = "ghjk";
        System.out.println(str.concat(str2));

        System.out.println(str.replace("1","2"));

        System.out.println(str.contains("a"));

        System.out.println(str.indexOf("c"));
        System.out.println(Arrays.toString(str.split("1",2)));
    }
}