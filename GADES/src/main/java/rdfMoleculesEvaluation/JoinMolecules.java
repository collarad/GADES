package rdfMoleculesEvaluation;


import org.apache.jena.rdf.model.Model;
import org.javatuples.Pair;

import java.io.*;
import java.util.List;

/**
 * Created by dcollarana on 7/23/2016.
 */
public class JoinMolecules {

    private BufferedWriter bw;

    public JoinMolecules(String fileName) throws Exception {

        File fout = new File(fileName);
        FileOutputStream fos = new FileOutputStream(fout);
        bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
    }

    public void addMolecule(String uri, List<Pair> properties) throws Exception {
        for (Pair elem: properties) {
            bw.write(uri+" "+elem.getValue0().toString()+" "+elem.getValue1().toString()+" .");
            bw.newLine();
        }
    }

    public void close()throws Exception {
        bw.close();
    }

}
