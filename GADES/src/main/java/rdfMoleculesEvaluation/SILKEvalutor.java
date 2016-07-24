package rdfMoleculesEvaluation;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

/**
 * Created by dcollarana on 7/24/2016.
 */
public class SILKEvalutor {

    public List<Triplet> getModelAsAList()throws Exception {

        System.out.println("Starting to join the molecules from SILK");

        Model model0 = RDFDataMgr.loadModel("C://DIC/Temp/dump_830k/dump0.nt");
        Model model1 = RDFDataMgr.loadModel("C://DIC/Temp/dump_830k/dump1.nt");
        Model model2 = RDFDataMgr.loadModel("C://DIC/Temp/dump_830k/dump2.nt");

        Jaccard jc = new Jaccard();;

        //Files
        System.out.println("Starting file output-0-1.nt");
        List<Triplet> result1 = joinMoleculesFromFile("C://DIC/Temp/Results/SILK/output-0-1.nt", model0, model1, jc, "/dump0");
        System.out.println("Starting file output-0-2.nt");
        List<Triplet> result2 = joinMoleculesFromFile("C://DIC/Temp/Results/SILK/output-0-2.nt", model0, model2, jc, "/dump0");
        System.out.println("Starting file output-1-2.nt");
        List<Triplet> result3 = joinMoleculesFromFile("C://DIC/Temp/Results/SILK/output-1-2.nt", model1, model2, jc, "/dump1");
        System.out.println("Process finished");

        List<Triplet> merge1 = jc.union(result1, result2);
        List<Triplet> merge2 = jc.union(merge1, result3);

        return merge2;

    }

    private List<Triplet> joinMoleculesFromFile(String file, Model modelA, Model modelB, Jaccard jc, String toReplace) throws Exception {

        JoinTriples jt = new JoinTriples();

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        String molecule0;
        String molecule1;
        int count = 0;
        RDFUtil util = new RDFUtil();
        while ((line = br.readLine()) != null) {
            String[] split = line.split(";");
            //molecule0 = java.net.URLDecoder.decode(split[0], "UTF-8");
            //molecule1 = java.net.URLDecoder.decode(split[1], "UTF-8");
            molecule0 = split[0];
            molecule1 = split[1];

            //System.out.println("Join molecule: "+molecule0.replace("/dump0",""));
            count++;
            //process the line.
            List<Pair> dump0 = util.getPropertiesFromSubject(molecule0, modelA);
            //System.out.println("dump0 size: "+dump0.size());
            List<Pair> dump1 = util.getPropertiesFromSubject(molecule1, modelB);
            //System.out.println("dump1 size: "+dump1.size());
            List<Pair> un = jc.union(dump0, dump1);
            //System.out.println("union size: "+un.size());
            jt.addMolecule(molecule0.replace(toReplace,""), un);

        }
        System.out.println("File: "+file);
        System.out.println("Count: "+count);

        return jt.get();

    }

    public static void main (String[] args) {

        SILKEvalutor silk = new SILKEvalutor();

        try{

            System.out.println("Size: "+silk.getModelAsAList().size());

        }catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
