package rdfMoleculesEvaluation;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.util.Convert;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dcollarana on 9/4/2016.
 */
public class SimilarityEvaluator {

    public List<RDFMolecule> getModelAsAList(double threshold)throws Exception {

        System.out.println("Starting to join the molecules from Similarity File with threshold: " + threshold);

        Model model0 = RDFDataMgr.loadModel("C://DIC/Temp/dump_830k/dump0.nt");
        Model model1 = RDFDataMgr.loadModel("C://DIC/Temp/dump_830k/dump1.nt");
        Model model2 = RDFDataMgr.loadModel("C://DIC/Temp/dump_830k/dump2.nt");

        Jaccard jc = new Jaccard();

        //double threshold = 0.6;
        //Files
        Map<String, RDFMolecule> molecules1 = joinMoleculesFromFile("C://DIC/Temp/MoleculesExperiment/TransE/TransE_results_20160901_0-1.txt", model0, model1, jc, threshold);
        Map<String, RDFMolecule> molecules2 = joinMoleculesFromFile("C://DIC/Temp/MoleculesExperiment/TransE/TransE_results_20160901_0-2.txt", model0, model2, jc, threshold, molecules1);
        Map<String, RDFMolecule> molecules3 = joinMoleculesFromFile("C://DIC/Temp/MoleculesExperiment/TransE/TransE_results_20160901_1-2.txt", model1, model2, jc, threshold, molecules2);

        System.out.println("Process finished");

        //Casting from Collection<T> to List<T>
        List<RDFMolecule> results= new ArrayList<RDFMolecule>();
        results.addAll(molecules3.values());

        System.out.println("Subjects Count: "+results.size());
        return results;
    }

    public List<RDFMolecule> getModelAsAList2(double threshold)throws Exception {

        System.out.println("Starting to join the molecules from Similarity File with threshold: " + threshold);

        Model model0 = RDFDataMgr.loadModel("C://DIC/Temp/dump_830k/dump0.nt");
        Model model1 = RDFDataMgr.loadModel("C://DIC/Temp/dump_830k/dump1.nt");
        Model model2 = RDFDataMgr.loadModel("C://DIC/Temp/dump_830k/dump2.nt");

        Jaccard jc = new Jaccard();

        //double threshold = 0.6;
        //Files
        List<Triplet> file1 = readFromFile("C://DIC/Temp/MoleculesExperiment/TransE/TransE_results_20160901_0-1.txt");
        List<Triplet> file2 = readFromFile("C://DIC/Temp/MoleculesExperiment/TransE/TransE_results_20160901_0-2.txt");
        List<Triplet> file3 = readFromFile("C://DIC/Temp/MoleculesExperiment/TransE/TransE_results_20160901_1-2.txt");

        Map<String, RDFMolecule> molecules1 = joinMoleculesFromList(file1, model0, model1, jc, threshold);
        Map<String, RDFMolecule> molecules2 = joinMoleculesFromList(file2, model0, model2, jc, threshold, molecules1);
        Map<String, RDFMolecule> molecules3 = joinMoleculesFromList(file3, model1, model2, jc, threshold, molecules2);

        System.out.println("Process finished");

        //Casting from Collection<T> to List<T>
        List<RDFMolecule> results= new ArrayList<RDFMolecule>();
        results.addAll(molecules3.values());

        System.out.println("Subjects Count: "+results.size());
        return results;
    }

    private Map<String, RDFMolecule> joinMoleculesFromFile(String file, Model modelA, Model modelB, Jaccard jc, double threshold) throws Exception {
        return joinMoleculesFromFile(file, modelA, modelB, jc, threshold, new HashMap<String, RDFMolecule>());
    }

    private Map<String, RDFMolecule> joinMoleculesFromFile(String file, Model modelA, Model modelB, Jaccard jc, double threshold, Map<String, RDFMolecule> map) throws Exception {

        Map<String, RDFMolecule> molecules = map;

        BufferedReader br = new BufferedReader(new FileReader(file));
        String uriA;
        String uriB;
        String moleculeUri;
        RDFMolecule molecule;
        double sm;
        RDFUtil util = new RDFUtil();
        int count = 0;
        String[] results;
        String line;
        while ((line = br.readLine()) != null) {

            results = line.split("\t");
            uriA = "<"+results[0]+">";
            uriB = "<"+results[1]+">";
            sm =  Double.parseDouble(results[2]);
            //process the line.
            List<Pair> dump0 = util.getPropertiesFromSubject(uriA,modelA);
            List<Pair> dump1 = util.getPropertiesFromSubject(uriB,modelB);

            if (sm > threshold) {
                count++;
                List<Pair> un = jc.union(dump0, dump1);
                //replacing dumps prefix
                moleculeUri = uriA.replace("/dump0","");
                moleculeUri = moleculeUri.replace("/dump1","");
                moleculeUri = moleculeUri.replace("/dump2","");

                if (molecules.containsKey(moleculeUri)) {
                    molecule = molecules.get(moleculeUri);
                } else {
                    molecule = new RDFMolecule(moleculeUri);
                    molecules.put(moleculeUri, molecule);
                }
                molecule.unionPairs(un);
            }
        }
        System.out.println("File: "+file);
        System.out.println("Count: "+count);
        br.close();
        return molecules;
    }

    private Map<String, RDFMolecule> joinMoleculesFromList(List<Triplet> triplets, Model modelA, Model modelB, Jaccard jc, double threshold) throws Exception {
        return joinMoleculesFromList(triplets, modelA, modelB, jc, threshold, new HashMap<String, RDFMolecule>());
    }

    private Map<String, RDFMolecule> joinMoleculesFromList(List<Triplet> triplets, Model modelA, Model modelB, Jaccard jc, double threshold, Map<String, RDFMolecule> map) throws Exception {

        Map<String, RDFMolecule> molecules = map;

        String uriA;
        String uriB;
        double sm;
        String moleculeUri;
        RDFMolecule molecule;
        RDFUtil util = new RDFUtil();
        int count = 0;

        for (Triplet item : triplets) {
            uriA = (String)item.getValue0();
            uriB = (String)item.getValue1();
            sm =  (Double)item.getValue2();
            //process the line.
            List<Pair> dump0 = util.getPropertiesFromSubject(uriA,modelA);
            List<Pair> dump1 = util.getPropertiesFromSubject(uriB,modelB);

            if (sm > threshold) {
                count++;
                List<Pair> un = jc.union(dump0, dump1);
                //replacing dumps prefix
                moleculeUri = uriA.replace("/dump0","");
                moleculeUri = moleculeUri.replace("/dump1","");
                moleculeUri = moleculeUri.replace("/dump2","");

                if (molecules.containsKey(moleculeUri)) {
                    molecule = molecules.get(moleculeUri);
                } else {
                    molecule = new RDFMolecule(moleculeUri);
                    molecules.put(moleculeUri, molecule);
                }
                molecule.unionPairs(un);
            }
        }
        System.out.println("Count: "+count);
        return molecules;
    }

    private List<Triplet> readFromFile(String file) throws Exception {

        List<Triplet> triplets = new ArrayList<Triplet>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String[] results;
        String line;
        while ((line = br.readLine()) != null) {
            results = line.split("\t");
            triplets.add(new Triplet("<" + results[0] + ">", "<" + results[1] + ">", Double.parseDouble(results[2])));
        }
        return triplets;
    }

    //private List<Triplet>

    public static void main (String[] args) {
        SimilarityEvaluator simEval = new SimilarityEvaluator();
        try{
            System.out.println("Size: "+simEval.getModelAsAList2(0.6).size());
        }catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
