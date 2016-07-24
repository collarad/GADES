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

    public List<RDFMolecule> getModelAsAList(double threshold)throws Exception {

        System.out.println("Starting to join the molecules from Jaccard");

        Model model0 = RDFDataMgr.loadModel("C://DIC/Temp/dump_830k/dump0.nt");
        Model model1 = RDFDataMgr.loadModel("C://DIC/Temp/dump_830k/dump1.nt");
        Model model2 = RDFDataMgr.loadModel("C://DIC/Temp/dump_830k/dump2.nt");

        Jaccard jc = new Jaccard();

        //double threshold = 0.6;
        //Files
        Map<String, RDFMolecule> molecules1 = joinMoleculesFromFile("C://DIC/Temp/dump_830k/list_dump0", model0, model1, jc, "/dump0", "/dump1", threshold);
        Map<String, RDFMolecule> molecules2 = joinMoleculesFromFile("C://DIC/Temp/dump_830k/list_dump0", model0, model2, jc, "/dump0", "/dump2", threshold, molecules1);
        Map<String, RDFMolecule> molecules3 = joinMoleculesFromFile("C://DIC/Temp/dump_830k/list_dump1", model1, model2, jc, "/dump1", "/dump2", threshold, molecules2);

        System.out.println("Process finished");

        //Casting from Collection<T> to List<T>
        List<RDFMolecule> results= new ArrayList<RDFMolecule>();
        results.addAll(molecules3.values());

        System.out.println("Subjects Count: "+results.size());
        return results;

    }

    private Map<String, RDFMolecule> joinMoleculesFromFile(String file, Model modelA, Model modelB, Jaccard jc, String toReplaceA, String toReplaceB, double threshold) throws Exception {

        return joinMoleculesFromFile(file, modelA, modelB, jc, toReplaceA, toReplaceB, threshold, new HashMap<String, RDFMolecule>());
    }

    private Map<String, RDFMolecule> joinMoleculesFromFile(String file, Model modelA, Model modelB, Jaccard jc, String toReplaceA, String toReplaceB, double threshold, Map<String, RDFMolecule> map) throws Exception {

        Map<String, RDFMolecule> molecules = map;

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        String line1;
        String moleculeUri;
        RDFMolecule molecule;
        double sm;
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

                moleculeUri = line.replace(toReplaceA,"");
                if (molecules.containsKey(moleculeUri)) {
                    molecule = molecules.get(moleculeUri);
                } else {
                    molecule = new RDFMolecule(moleculeUri);
                    molecules.put(moleculeUri, molecule);
                }
                molecule.addPairs(un);
            }
        }
        System.out.println("File: "+file);
        System.out.println("Count: "+count);

        return molecules;
    }

    public static void main (String[] args) {

        JaccardEvaluator jac = new JaccardEvaluator();

        try{

            System.out.println("Size: "+jac.getModelAsAList(0.2).size());

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
