package test;

import ontologyManagement.MyOWLLogicalEntity;
import ontologyManagement.MyOWLOntology;
import ontologyManagement.OWLConcept;
import ontologyManagement.OWLLink;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dcollarana on 7/22/2016.
 */
public class TestMain {

    public static void main (String[] args) {

        System.out.println("Starting loading the experiment");

        //String ontFile = "C://DIC/Temp/dump_830k/DBpedia_People_2016-07-22_18-11-04.nq";
        String ontFile = "C://DIC/Temp/dump_830k/DBPedia_People_2016-07-22_19-50-12.nq";
        //String ontFile = "C://DIC/Temp/dump_830k/go.owl";
        MyOWLOntology o = new MyOWLOntology(ontFile, "http://dbpedia.org/ontology/", "HermiT");

        OWLConcept a = o.getOWLConcept("http://dbpedia.org/resource/Airavt/dump0");
        //o.getOWLConcept()
        OWLNamedIndividual b = o.getOWLIndividual("http://dbpedia.org/resource/Airavt/dump1");

        /*Set<MyOWLLogicalEntity> anns = new HashSet<MyOWLLogicalEntity>();
        anns.add(a);
        anns.add(b);
        o.setOWLLinks(anns);
        Set<OWLLink> neighA = a.getNeighbors();
        Set<OWLLink> neighB = b.getNeighbors();*/

        System.out.println(a);//.taxonomicSimilarity(a));
        System.out.println(b);//.taxonomicSimilarity(a));

        System.out.println("Finishing the experiment");

    }

}
