package rdfMoleculesEvaluation;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * Created by dcollarana on 7/23/2016.
 */

public class JaccardEvaluator {

    public List<Triplet> getModelAsAList()throws Exception {
        System.out.println("Starting to join the molecules from Jaccard");

        Model model0 = RDFDataMgr.loadModel("C://DIC/Temp/dump_830k/dump0.nt");
        Model model1 = RDFDataMgr.loadModel("C://DIC/Temp/dump_830k/dump1.nt");
        Model model2 = RDFDataMgr.loadModel("C://DIC/Temp/dump_830k/dump2.nt");

        Jaccard jc = new Jaccard();
        JoinTriples jt = new JoinTriples();

        double threshold = 0.8;
        //Files
        joinMoleculesFromFile("C://DIC/Temp/dump_830k/list_dump0", model0, model1, jc, jt, "/dump0", "/dump1", threshold);
        joinMoleculesFromFile("C://DIC/Temp/dump_830k/list_dump0", model0, model2, jc, jt, "/dump0", "/dump2", threshold);
        joinMoleculesFromFile("C://DIC/Temp/dump_830k/list_dump1", model1, model2, jc, jt, "/dump1", "/dump2", threshold);
        System.out.println("Process finished");
        return jt.get();
    }

    private void joinMoleculesFromFile(String file, Model modelA, Model modelB, Jaccard jc, JoinTriples jt, String toReplaceA, String toReplaceB, double threshold) throws Exception {

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String line1;
            float sm;
            RDFUtil util = new RDFUtil();
            int count = 0;
            while ((line = br.readLine()) != null) {
                //process the line.
                List<Pair> dump0 = util.getPropertiesFromSubject(line,modelA);
                line1 = line.replace(toReplaceA,toReplaceB);
                List<Pair> dump1 = util.getPropertiesFromSubject(line1,modelB);
                sm = jc.jaccard(dump0, dump1);
                if (sm > threshold) {
                    count++;
                    List<Pair> un = jc.union(dump0, dump1);
                    jt.addMolecule(line.replace(toReplaceA,""), un);
                }
            }
            System.out.println("File: "+file);
            System.out.println("Count: "+count);
    }

    public static void main (String[] args) {

        JaccardEvaluator jac = new JaccardEvaluator();

        try{

            System.out.println("Size: "+jac.getModelAsAList().size());

        }catch (Exception ex) {
            ex.printStackTrace();
        }

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
