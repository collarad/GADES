package test;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ontologyManagement.MyOWLLogicalEntity;
import ontologyManagement.MyOWLOntology;
import ontologyManagement.OWLConcept;
import similarity.InformationContent;
import similarity.matching.AnnSim;
import similarity.matching.AnnotationComparison;

public class DatasetTestPhase {

	public static Set<MyOWLLogicalEntity> getFilteredConceptAnnotations(String conceptName, String folder, MyOWLOntology o)
	{
		Set<MyOWLLogicalEntity> concepts = DatasetTest.getConceptAnnotations(conceptName, folder, o, true);
		Set<MyOWLLogicalEntity> auxSet = new HashSet<MyOWLLogicalEntity>(concepts);
		for (MyOWLLogicalEntity c: auxSet)
		{
			for (MyOWLLogicalEntity d: auxSet)
			{
				if (!c.equals(d))
				{
					if (((OWLConcept) d).isSubConceptOf((OWLConcept) c))
						concepts.remove(c);
				}
			}
		}
		return concepts;
	}
	
	public static void main(String[] args) throws Exception {
		Map<String, String> ontPrefix = new HashMap<String,String>();
		ontPrefix.put("resources/dataset3/", "http://purl.org/obo/owl/GO#");
		ontPrefix.put("resources/dataset32014/", "http://purl.obolibrary.org/obo/");
		String[] p = {"resources/dataset3/"};
		for (String prefix: p)
		{
			String ontFile = prefix + "goProtein/goEL.owl";
			MyOWLOntology o = new MyOWLOntology(ontFile, ontPrefix.get(prefix));
			String comparisonFile = prefix + "proteinpairs.txt";
			List<ComparisonResult> comparisons = DatasetTest.readComparisonFile(comparisonFile);
			String[] files = {prefix + "bp"};//, prefix + "mf", prefix + "cc"};
			
			Set<String> entities = new HashSet<String>();
			File f = new File(files[0]);
			File[] pNames = f.listFiles();
			for (File x: pNames)
			{
				entities.add(x.getName());
			}
			
			
			InformationContent ic = new InformationContent(comparisons, files, o);
			ic.hashCode();
			
			//================= GETTING OWLCONCEPT NEIGHBORHOODS ==============
			double startRelTime = System.nanoTime();
			Set<MyOWLLogicalEntity> anns = DatasetTest.getOntologyTerms(comparisons, files, o);
			o.setOWLLinks(anns);
			double estimatedRelTime = (System.nanoTime() - startRelTime)/1000000;
			System.out.println(estimatedRelTime/1000/60);
			//================ END GETTING NEIGHBORHOOODS ====================
			//=================== GETTING INFORMATION CONTENT ======================
			
 			
			//=================== END GETTING INFORMATION CONTENT ======================
			//================ FINDING WHICH COMPARISONS HAVE TO BE PERFORMED ==========
			Set<AnnotationComparison> conceptComparisons = new HashSet<AnnotationComparison>();
			for (Iterator<ComparisonResult> i = comparisons.iterator(); i.hasNext();)
			{
				ComparisonResult comp = i.next();
				for (String file:files)
				{
					//Set<OWLConcept> a = /*DatasetTest.getConceptAnnotations*/getFilteredConceptAnnotations(comp.getConceptA(), file, o);
					//Set<OWLConcept> b = /*DatasetTest.getConceptAnnotations*/getFilteredConceptAnnotations(comp.getConceptB(), file, o);
					Set<MyOWLLogicalEntity> a = DatasetTest.getConceptAnnotations(comp.getConceptA(), file, o, false);
					Set<MyOWLLogicalEntity> b = DatasetTest.getConceptAnnotations(comp.getConceptB(), file, o, false);
					for (MyOWLLogicalEntity c1: a)
					{
						for (MyOWLLogicalEntity c2: b)
						{
							conceptComparisons.add(new AnnotationComparison(c1, c2));
						}
					}
				}
			}
			System.out.println("Concept Comparisons: " + conceptComparisons.size());
			//================ END FINDING COMPARISONS ================================
			//================ COMPUTING COMPARISONS ==================================
			startRelTime = System.nanoTime();
			Map<AnnotationComparison, Double> costMatrix = new HashMap<AnnotationComparison, Double>();
			for (AnnotationComparison comparison: conceptComparisons)
			{
				Double sim = ((MyOWLLogicalEntity) comparison.getConceptA()).similarity((MyOWLLogicalEntity) comparison.getConceptB());
 				costMatrix.put(comparison, sim);
			}
			estimatedRelTime = (System.nanoTime() - startRelTime)/1000000;
			System.out.println(estimatedRelTime/1000/60);
			//================ END COMPUTING COMPARISONS ===========================
			PrintWriter generalWriter = new PrintWriter(prefix + "results.txt", "UTF-8");
			Map<String, PrintWriter> writers = new HashMap<String, PrintWriter>();
			for (String file:files)
			{
				writers.put(file, new PrintWriter(prefix + file.replaceAll(prefix, "") + "results.txt"));
			}
			//generalWriter.println("Protein1\tProtein2\tSimilarity");
			int counter = 0, total = comparisons.size();
			//================ COMPUTING MATCHING ===============================
			startRelTime = System.nanoTime();
			AnnSim bpm = new AnnSim(costMatrix);
			//OnJaccard bpm = new OnJaccard(costMatrix);
			for (Iterator<ComparisonResult> i = comparisons.iterator(); i.hasNext();)
			{
				ComparisonResult comp = i.next();
				double sim = 0;
				double totalEstimatedTime = 0;
				for (String file:files)
				{
					Set<MyOWLLogicalEntity> a = DatasetTest.getConceptAnnotations(comp.getConceptA(), file, o, false);
					Set<MyOWLLogicalEntity> b = DatasetTest.getConceptAnnotations(comp.getConceptB(), file, o, false);
					Set<MyOWLLogicalEntity> intersection = new HashSet<MyOWLLogicalEntity>(a);
					intersection.retainAll(b);
					Set<MyOWLLogicalEntity> union = new HashSet<MyOWLLogicalEntity>(a);
					union.addAll(b);
					double startTime = System.nanoTime();  
					double aux = bpm.matching(a, b, null, null);
					double estimatedTime = System.nanoTime() - startTime;
					totalEstimatedTime += estimatedTime/1000000;
					sim = aux;
					comp.setSimilarity(sim);
					writers.get(file).println(comp);
					System.out.println(comp + "\t" + totalEstimatedTime + "\t" + counter++ + "/" + total + "\t" + a.size() + "\t" + b.size());
					generalWriter.println(comp + "\t" + totalEstimatedTime + "\t" + a.size() + "\t" + b.size());
				}
			}
			generalWriter.close();
			for (String file:files)
			{
				writers.get(file).close();
			}
			estimatedRelTime = (System.nanoTime() - startRelTime)/1000000;
			System.out.println(estimatedRelTime/1000/60);;
			o.disposeReasoner();
		}
		
	}

}
