package com.zy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class llsl {
    public static void main(String[] args) {
        List linked = new LinkedList();
        List array = new ArrayList();
        linked.add("a");
        linked.add("b");
        linked.add("c");
        linked.add("d");
        array.add("a");
        array.add("b");
        array.add("c");
        array.add("d");
        ((LinkedList) linked).addFirst("f");
        array.add("f");
        for (int i = 0;i<4;i++){
            System.out.println("l:"+linked.get(i));
            System.err.println("a:"+array.get(i));
        }

    }
}
