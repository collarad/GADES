package rdfMoleculesEvaluation;

import org.apache.jena.vocabulary.RDF;
import org.javatuples.Triplet;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by dcollarana on 7/24/2016.
 */
public class PrecisionAndRecallCalculator {

    public static void main (String[] args) {

        try {

            calculateSimilarityFile();
            //calculateSilk();
            //calculateJaccard();

        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void calculateSilk() throws Exception {
        System.out.println("Process starting");

        GoldStandard gs = new GoldStandard();
        List<RDFMolecule> gsTriplets = gs.getModelAsAList();
        int true_set = gsTriplets.size();
        System.out.println("True Set: "+true_set);

        SILKEvalutor silk = new SILKEvalutor();
        List<RDFMolecule> silkTriplets = silk.getModelAsAList();
        int computed_set = silkTriplets.size();
        System.out.println("Computed Set: "+computed_set);

        Jaccard jc = new Jaccard();
        List<RDFMolecule> intersectionTriplets = jc.intersection(silkTriplets, gsTriplets);
        int intersection_set = intersectionTriplets.size();
        System.out.println("Intersection Set: "+intersection_set);

        double precision = (double) intersection_set / computed_set;
        System.out.println("Precision: "+precision);

        double recall = (double) intersection_set / true_set;
        System.out.println("Recall: "+recall);
    }

    private static void calculateJaccard() throws Exception {
        System.out.println("Process starting");

        GoldStandard gs = new GoldStandard();
        List<RDFMolecule> gsTriplets = gs.getModelAsAList();
        int true_set = gsTriplets.size();
        System.out.println("True Set: "+true_set);

        JaccardEvaluator jac = new JaccardEvaluator();
        List<RDFMolecule> jacTriplets = jac.getModelAsAList(0.2);
        int computed_set = jacTriplets.size();
        System.out.println("Computed Set: "+computed_set);

        Jaccard jc = new Jaccard();
        List<RDFMolecule> intersectionTriplets = jc.intersection(jacTriplets, gsTriplets);
        int intersection_set = intersectionTriplets.size();
        System.out.println("Intersection Set: "+intersection_set);

        double precision = (double) intersection_set / computed_set;
        System.out.println("Precision: "+precision);

        double recall = (double) intersection_set / true_set;
        System.out.println("Recall: "+recall);
    }

    private static void calculateSimilarityFile() throws Exception {
        System.out.println("Process starting TransE");

        GoldStandard gs = new GoldStandard();
        List<RDFMolecule> gsTriplets = gs.getModelAsAList();
        int true_set = gsTriplets.size();
        System.out.println("True Set: "+true_set);

        SimilarityEvaluator sim = new SimilarityEvaluator();
        List<RDFMolecule> simTriplets = sim.getModelAsAList2(0.6);
        int computed_set = simTriplets.size();
        System.out.println("Computed Set: "+computed_set);

        Jaccard jc = new Jaccard();
        List<RDFMolecule> intersectionTriplets = jc.intersection(simTriplets, gsTriplets);
        int intersection_set = intersectionTriplets.size();
        System.out.println("Intersection Set: "+intersection_set);

        double precision = (double) intersection_set / computed_set;
        System.out.println("Precision: "+precision);

        double recall = (double) intersection_set / true_set;
        System.out.println("Recall: "+recall);
    }

}
