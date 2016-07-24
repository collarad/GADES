package rdfMoleculesEvaluation;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.javatuples.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

/**
 * Created by dcollarana on 7/24/2016.
 */
public class GoldStandard {

    public static void main (String[] args) {

        Model modelGold = RDFDataMgr.loadModel("C://DIC/Temp/dump_830k/goldStandard.nt");

        String file = "C://DIC/Temp/dump_830k/list_gold";
        //Loading the file of subjects
        try{
            JoinMolecules jm = new JoinMolecules("C://DIC/Temp/Results/goaldTemp.nt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            RDFUtil util = new RDFUtil();
            int count = 0;
            while ((line = br.readLine()) != null) {
                //process the line.
                List<Pair> molecule = util.getPropertiesFromSubject(line,modelGold);
                count++;
                jm.addMolecule(line, molecule);
            }
            jm.close();
            System.out.println("Count: "+count);

        }catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
