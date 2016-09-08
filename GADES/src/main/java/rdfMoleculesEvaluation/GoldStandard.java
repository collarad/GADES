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
public class GoldStandard {

    public List<RDFMolecule> getModelAsAList()throws Exception {

        System.out.println("Starting to build GoldStandard");
        Model modelGold = RDFDataMgr.loadModel("C://DIC/Temp/MoleculesExperiment/goldStandard.nt");
        String file = "C://DIC/Temp/MoleculesExperiment/list_gold";
        List<RDFMolecule> molecules = new ArrayList<RDFMolecule>();

        //Loading the file of subjects
        //JoinTriples jt = new JoinTriples();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        RDFUtil util = new RDFUtil();
        int count = 0;
        RDFMolecule molecule;
        while ((line = br.readLine()) != null) {
            molecule = new RDFMolecule(line);
            molecule.addPairs(util.getPropertiesFromSubject(line, modelGold));
            molecules.add(molecule);
        }
        System.out.println("Subjects Count: "+molecules.size());
        br.close();
        return molecules;
    }


    public static void main (String[] args) {

        GoldStandard gs = new GoldStandard();
        try {

            System.out.println("Size: "+gs.getModelAsAList().size());

        }catch(Exception ex) {
            ex.printStackTrace();
        }

    }

}
