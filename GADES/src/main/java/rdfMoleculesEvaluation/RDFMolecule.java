package rdfMoleculesEvaluation;

import org.javatuples.Pair;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dcollarana on 7/24/2016.
 */
public class RDFMolecule {

    String subject;
    List<Pair> triples;
    Jaccard jc;

    public RDFMolecule(String subject) {
        this(subject, new ArrayList<Pair>());
    }

    public RDFMolecule(String subject, List<Pair> triples) {
        this.subject = subject;
        this.triples = triples;
        jc = new Jaccard();
    }

    public void unionPairs(List<Pair> triplets) {
        List<Pair> union = jc.union(this.triples, triplets);
        this.triples = union;
    }

    public void addPairs(List<Pair> triplets) {
        this.triples.addAll(triplets);
    }

    public void addPair(Pair triplet) {
        this.triples.add(triplet);
    }

    public void joinMolecule(RDFMolecule otherMolecule) {
        if (this.subject == otherMolecule.subject) {
            List<Pair> union = jc.union(this.triples, otherMolecule.triples);
            this.triples = union;
        }
        else
            System.out.println("Molecules are not from same subject: "+this.subject);
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof RDFMolecule))return false;
        RDFMolecule otherMolecule = (RDFMolecule)other;
        if (this.subject == otherMolecule.subject) {
            //if (jc.jaccard(this.triples, otherMolecule.triples) > 0.9)
            if (this.triples.equals(otherMolecule.triples))
                return true;
            else
                return false;
        }
        else
            return false;

    }

}
