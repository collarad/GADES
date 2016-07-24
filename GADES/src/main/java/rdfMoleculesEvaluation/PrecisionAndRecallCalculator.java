package rdfMoleculesEvaluation;

import org.javatuples.Triplet;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by dcollarana on 7/24/2016.
 */
public class PrecisionAndRecallCalculator {

    public static void main (String[] args) {

        try {

            //calculateSilk();
            calculateJaccard();

        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void calculateSilk() throws Exception {
        System.out.println("Process starting");

        GoldStandard gs = new GoldStandard();
        List<Triplet> gsTriplets = null;//gs.getModelAsAList();
        int true_set = gsTriplets.size();
        System.out.println("True Set: "+true_set);

        SILKEvalutor silk = new SILKEvalutor();
        List<Triplet> silkTriplets = null;//silk.getModelAsAList();
        int computed_set = silkTriplets.size();
        System.out.println("Computed Set: "+computed_set);

        Jaccard jc = new Jaccard();
        List<Triplet> intersectionTriplets = jc.intersection(gsTriplets, silkTriplets);
        int intersection_set = intersectionTriplets.size();
        System.out.println("Intersection Set: "+intersection_set);

        float precision = intersection_set / computed_set;
        System.out.println("Precision: "+precision);

        float recall = intersection_set / true_set;
        System.out.println("Recall: "+recall);
    }

    private static void calculateJaccard() throws Exception {
        System.out.println("Process starting");

        GoldStandard gs = new GoldStandard();
        List<Triplet> gsTriplets = null;//gs.getModelAsAList();
        int true_set = gsTriplets.size();
        System.out.println("True Set: "+true_set);

        JaccardEvaluator jac = new JaccardEvaluator();
        List<Triplet> jacTriplets = jac.getModelAsAList();
        int computed_set = jacTriplets.size();
        System.out.println("Computed Set: "+computed_set);

        Jaccard jc = new Jaccard();
        List<Triplet> intersectionTriplets = jc.intersection(gsTriplets, jacTriplets);
        int intersection_set = intersectionTriplets.size();
        System.out.println("Intersection Set: "+intersection_set);

        double precision = (double) intersection_set / computed_set;
        System.out.println("Precision: "+precision);

        double recall = (double) intersection_set / true_set;
        System.out.println("Recall: "+recall);
    }

}
