package rdfMoleculesEvaluation;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dcollarana on 7/24/2016.
 */
public class SILKEvalutor {

    public List<RDFMolecule> getModelAsAList()throws Exception {

        System.out.println("Starting to join the molecules with SILK");

        Model model0 = RDFDataMgr.loadModel("C://DIC/Temp/dump_830k/dump0.nt");
        Model model1 = RDFDataMgr.loadModel("C://DIC/Temp/dump_830k/dump1.nt");
        Model model2 = RDFDataMgr.loadModel("C://DIC/Temp/dump_830k/dump2.nt");

        Jaccard jc = new Jaccard();

        //Files
        System.out.println("Starting file output-0-1.nt");
        Map<String, RDFMolecule> molecules1 = joinMoleculesFromFile("C://DIC/Temp/Results/SILK/output-0-1.nt", model0, model1, jc, "/dump0");
        System.out.println("Starting file output-0-2.nt");
        Map<String, RDFMolecule> molecules2 = joinMoleculesFromFile("C://DIC/Temp/Results/SILK/output-0-2.nt", model0, model2, jc, "/dump0", molecules1);
        System.out.println("Starting file output-1-2.nt");
        Map<String, RDFMolecule> molecules3 = joinMoleculesFromFile("C://DIC/Temp/Results/SILK/output-1-2.nt", model1, model2, jc, "/dump2", molecules2);

        System.out.println("Process finished");

        //Casting from Collection<T> to List<T>
        List<RDFMolecule> results= new ArrayList<RDFMolecule>();
        results.addAll(molecules3.values());

        System.out.println("Subjects Count: "+results.size());

        return results;

    }

    private Map<String, RDFMolecule> joinMoleculesFromFile(String file, Model modelA, Model modelB, Jaccard jc, String toReplace) throws Exception {

        return joinMoleculesFromFile(file, modelA, modelB, jc, toReplace, new HashMap<String, RDFMolecule>());

    }

    private Map<String, RDFMolecule> joinMoleculesFromFile(String file, Model modelA, Model modelB, Jaccard jc, String toReplace, Map<String, RDFMolecule> map) throws Exception {

        Map<String, RDFMolecule> molecules = map;

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        String molecule0;
        String molecule1;
        String moleculeUri;
        RDFMolecule molecule;
        RDFUtil util = new RDFUtil();
        while ((line = br.readLine()) != null) {
            String[] split = line.split(";");
            molecule0 = split[0];
            molecule1 = split[1];
            //process the line.
            List<Pair> dump0 = util.getPropertiesFromSubject(molecule0, modelA);
            //System.out.println("dump0 size: "+dump0.size());
            List<Pair> dump1 = util.getPropertiesFromSubject(molecule1, modelB);
            //System.out.println("dump1 size: "+dump1.size());
            List<Pair> un = jc.union(dump0, dump1);
            //System.out.println("union size: "+un.size());

            moleculeUri = molecule0.replace(toReplace,"");
            if (molecules.containsKey(moleculeUri)) {
                molecule = molecules.get(moleculeUri);
            } else {
                molecule = new RDFMolecule(moleculeUri);
                molecules.put(moleculeUri, molecule);
            }
            molecule.unionPairs(un);
        }
        System.out.println("File: "+file);
        System.out.println("Count: "+molecules.size());
        br.close();
        return molecules;

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
