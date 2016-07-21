package similarity.matching;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import ontologyManagement.MyOWLLogicalEntity;
import ontologyManagement.OWLConcept;
import similarity.ComparableElement;

public class AnnSim {
	ComparableElement[] v1, v2;
	double[][] costMatrix;
	int[] assignment;
	Map<AnnotationComparison,Double> mapComparisons = null;
	
	public AnnSim()
	{
	}
	
	public AnnSim(Map<AnnotationComparison,Double> matrix)
	{
		mapComparisons = matrix;
	}
	
		
	public <T> double matching(Set<T> a, Set<T> b, OWLConcept orig, OWLConcept des) throws Exception
	{
		if (a.getClass() != b.getClass() && a != Collections.emptySet() && b != Collections.emptySet())// || !(a instanceof Set<ComparableElement>)))// || !(a instanceof Set<ComparableElement>))
			throw new Exception("Invalid comparison between " + a.getClass() + " " + b.getClass());
		else
		{
			if (a.equals(b))
				return 1.0;
			if (a.isEmpty() || b.isEmpty()) //Here we know that, almost one of the set is not empty
				return 0.0;
			costMatrix = new double [a.size()][b.size()];
			v1 = a.toArray(new ComparableElement[a.size()]);
			v2 = b.toArray(new ComparableElement[b.size()]);
			if (mapComparisons == null)
			{
				for (int i = 0; i< v1.length; i++)
				{
					ComparableElement s1 = v1[i];
					for (int j = 0; j < v2.length; j++)
					{
						ComparableElement s2 = v2[j];
						costMatrix[i][j] = 1 - s1.similarity(s2,orig,des); //The hungarian algorithm minimize. Therefore we convert the similarity in distance
					}
				}
			}
			else
			{
				for (int i = 0; i< v1.length; i++)
				{
					ComparableElement s1 = v1[i];
					for (int j = 0; j < v2.length; j++)
					{
						ComparableElement s2 = v2[j];
						AnnotationComparison comp = new AnnotationComparison(s1, s2);
						costMatrix[i][j] = 1 - mapComparisons.get(comp); //The hungarian algorithm minimize. Therefore we convert the similarity in distance
					}
				}
			}

			HungarianAlgorithm hungarn = new HungarianAlgorithm(costMatrix);
			assignment = hungarn.execute();
			
			double sim = 0;
			for (int i = 0; i < assignment.length; i++)
			{
				int aux = assignment[i];
				if (aux >=0) //If there is an assignment
				{
					sim += 1-costMatrix[i][aux];
				}
			}
			
			return 2*sim/(v1.length+v2.length);
		}
	}
	
	public <T> double maximumMatching(Set<T> a, Set<T> b, MyOWLLogicalEntity orig, MyOWLLogicalEntity des)
			throws Exception {
		if (a.getClass() != b.getClass() && a != Collections.emptySet() && b != Collections.emptySet())// || /eElement>))
			throw new Exception("Invalid comparison between " + a.getClass() + " " + b.getClass());
		else {
			if (a.equals(b))
				return 1.0;
			if (a.isEmpty() || b.isEmpty()) // Here we know that, almost one of the set is not empty
				return 0.0;
			costMatrix = new double[a.size()][b.size()];
			v1 = a.toArray(new ComparableElement[a.size()]);
			v2 = b.toArray(new ComparableElement[b.size()]);
			if (mapComparisons == null)
			{
				for (int i = 0; i < v1.length; i++) {
					ComparableElement s1 = v1[i];
					for (int j = 0; j < v2.length; j++) {
						ComparableElement s2 = v2[j];
						costMatrix[i][j] = s1.similarity(s2, orig, des);
					}
				}
			}
			else
			{
				for (int i = 0; i < v1.length; i++) {
					ComparableElement s1 = v1[i];
					for (int j = 0; j < v2.length; j++) {
						ComparableElement s2 = v2[j];
						Double value = mapComparisons.get(new AnnotationComparison(s1, s2));
						if (value == null)
							value = 0.0;
						costMatrix[i][j] = value;
					}
				}
			}
			double sim = 0;
			for (int i = 0; i < v1.length; i++) {
				double maxRow = 0;
				for (int j = 0; j < v2.length; j++) {
					if (maxRow < costMatrix[i][j])
						maxRow = costMatrix[i][j];
				}
				sim += maxRow;
			}
			
			for (int j = 0; j < v2.length; j++) {
				double maxCol = 0;
				for (int i = 0; i < v1.length; i++) {
					if (maxCol < costMatrix[i][j])
						maxCol = costMatrix[i][j];
				}
				sim += maxCol;
			}
			return sim / (a.size() + b.size());
		}
	}

}

