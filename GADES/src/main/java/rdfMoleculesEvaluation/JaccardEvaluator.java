package rdfMoleculesEvaluation;

import org.javatuples.Pair;

import java.util.*;

/**
 * Created by dcollarana on 7/23/2016.
 */

public class JaccardEvaluator {

    public static void main (String[] args) {

        Jaccard jc = new Jaccard();

        List<Pair> list1 = new ArrayList<Pair>(Arrays.asList(new Pair("A", "A1"), new Pair("B", "B1"), new Pair("C", "C1")));
        List<Pair> list2 = new ArrayList<Pair>(Arrays.asList(new Pair("A2", "A1"), new Pair("B", "B1"), new Pair("C", "C1")));

        ArrayList<Pair> intersect = (ArrayList<Pair>) jc.intersection(list1, list2);
        List<Pair> un = (List<Pair>) jc.union(list1, list2);
        System.out.println("Intersecion: "+intersect);
        System.out.println("Union: "+un);

        System.out.println("Jaccard similarity");
        System.out.println(jc.jaccard(list1, list2));

    }

}

class Jaccard {

    public <T> List<T> union(List<T> list1, List<T> list2) {
        Set<T> set = new HashSet<T>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<T>(set);
    }

    public <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();
        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }

    public <T> float jaccard(List<T> list1, List<T> list2) {
        ArrayList<Pair> intersect = (ArrayList<Pair>) this.intersection(list1, list2);
        List<Pair> un = (List<Pair>) this.union(list1, list2);
        float similarity = (float) intersect.size() / un.size();
        return similarity;
    }

}
