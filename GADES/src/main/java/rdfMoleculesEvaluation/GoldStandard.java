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
public class GoldStandard {

    public List<Triplet> getModelAsAList()throws Exception {

        Model modelGold = RDFDataMgr.loadModel("C://DIC/Temp/dump_830k/goldStandard.nt");

        String file = "C://DIC/Temp/dump_830k/list_gold";
        //Loading the file of subjects
            JoinTriples jt = new JoinTriples();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            RDFUtil util = new RDFUtil();
            int count = 0;
            while ((line = br.readLine()) != null) {
                //process the line.
                List<Pair> molecule = util.getPropertiesFromSubject(line, modelGold);
                count++;
                jt.addMolecule(line, molecule);
            }
            System.out.println("Count: "+count);
            return jt.get();
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
