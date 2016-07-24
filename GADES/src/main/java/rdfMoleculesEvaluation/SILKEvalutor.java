package rdfMoleculesEvaluation;

import org.javatuples.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

/**
 * Created by dcollarana on 7/24/2016.
 */
public class SILKEvalutor {

    public static void main (String[] args) {
        try{
            System.out.println("Starting to join the molecules");
            Jaccard jc = new Jaccard();
            JoinMolecules jm = new JoinMolecules("C://DIC/Temp/Results/silk_molecules.nt");

            String file = "C://DIC/Temp/Results/output-0-1.nt";
            //Loading the file of subjects

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String molecule0;
            String molecule1;
            String uri;
            float sm;
            RDFUtil util = new RDFUtil();
            while ((line = br.readLine()) != null) {

                String[] split = line.split(";");
                molecule0 = java.net.URLDecoder.decode(split[0], "UTF-8");
                molecule1 = java.net.URLDecoder.decode(split[1], "UTF-8");

                System.out.println("Join molecule: "+molecule0.replace("/dump0",""));

                //process the line.
                List<Pair> dump0 = util.getPropertiesFromSubject(molecule0, "http://localhost:3030/dump0/query");
                List<Pair> dump1 = util.getPropertiesFromSubject(molecule1, "http://localhost:3030/dump1/query");
                List<Pair> un = jc.union(dump0, dump1);
                jm.addMolecule(molecule0.replace("/dump0",""), un);

            }
            jm.close();
            System.out.println("Process finished");

        }catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
