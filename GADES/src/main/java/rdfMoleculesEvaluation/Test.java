package rdfMoleculesEvaluation;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.javatuples.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dcollarana on 7/24/2016.
 */
public class Test {

    public static void main (String[] args) {

        // Create a model and read into it from file
        // "data.ttl" assumed to be Turtle.
        
        Model model = RDFDataMgr.loadModel("C://DIC/Temp/dump_830k/goldStandard.nt");

        //Test - getting the properties
        //RDFUtil util = new RDFUtil();
        //util.getPropertiesFromSubject("<http://dbpedia.org/resource/2015–16_KS_Cracovia_(football)_season/dump0>", "http://localhost:3030/dump0/query");

        //Test - Jaccard algorithm
        Jaccard jc = new Jaccard();
        List<Pair> list1 = new ArrayList<Pair>(Arrays.asList(new Pair("A", "A1"), new Pair("B", "B1"), new Pair("C", "C1")));
        List<Pair> list2 = new ArrayList<Pair>(Arrays.asList(new Pair("A2", "A1"), new Pair("B", "B1"), new Pair("C", "C1")));

        ArrayList<Pair> intersect = (ArrayList<Pair>) jc.intersection(list1, list2);
        List<Pair> un = (List<Pair>) jc.union(list1, list2);
        System.out.println("Intersecion: "+intersect);
        System.out.println("Union: "+un);

        System.out.println("Jaccard similarity");
        System.out.println(jc.jaccard(list1, list2));

        //Test - Jaccard algorithm
        //List<Pair> list1 = new ArrayList<Pair>(Arrays.asList(new Pair("A", "A1"), new Pair("B", "B1"), new Pair("C", "C1")));
        //List<Pair> list2 = new ArrayList<Pair>(Arrays.asList(new Pair("A2", "A1"), new Pair("B", "B1"), new Pair("C", "C1")));

        //ArrayList<Pair> intersect = (ArrayList<Pair>) jc.intersection(list1, list2);
        //List<Pair> un = (List<Pair>) jc.union(list1, list2);
        //System.out.println("Intersecion: "+intersect);
        //System.out.println("Union: "+un);

        //System.out.println("Jaccard similarity");
        //System.out.println(jc.jaccard(list1, list2));

        //Test - getting the properties
        //RDFUtil util = new RDFUtil();
        //util.getPropertiesFromSubject("<http://dbpedia.org/resource/2015–16_KS_Cracovia_(football)_season/dump0>", "http://localhost:3030/dump0/query");



    }

}
