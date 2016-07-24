package rdfMoleculesEvaluation;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dcollarana on 7/24/2016.
 */
public class JoinTriples {

    List<Triplet> results;

    public JoinTriples() {
        results =  new ArrayList<Triplet>();
    }

    public void addMolecule(String uri, List<Pair> properties) throws Exception {
        for (Pair elem: properties) {
            results.add(new Triplet(uri, elem.getValue0(), elem.getValue1()));
        }
    }

    public List<Triplet> get() {
        return  results;
    }

}
