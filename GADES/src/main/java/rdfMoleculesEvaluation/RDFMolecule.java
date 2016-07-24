package rdfMoleculesEvaluation;

import org.javatuples.Triplet;

import java.util.List;

/**
 * Created by dcollarana on 7/24/2016.
 */
public class RDFMolecule {

    String subject;
    List<Triplet> triples;
    Jaccard jc;

    public RDFMolecule(String subject, List<Triplet> triples) {
        this.subject = subject;
        this.triples = triples;
        jc = new Jaccard();
    }

    public void addTriplet(Triplet triplet) {
        this.triples.add(triplet);
    }

    public void joinMolecule(RDFMolecule otherMolecule) {
        if (this.subject == otherMolecule.subject) {
            List<Triplet> union = jc.union(this.triples, otherMolecule.triples);
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
            if (jc.jaccard(this.triples, otherMolecule.triples) > 0.9)
                return true;
            else
                return false;
        }
        else
            return false;

    }

}
