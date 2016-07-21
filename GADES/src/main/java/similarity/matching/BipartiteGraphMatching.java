package similarity.matching;

import java.util.Collections;
import java.util.Set;

import ontologyManagement.MyOWLLogicalEntity;
import ontologyManagement.OWLConcept;
import ontologyManagement.OWLLink;
import similarity.ComparableElement;

public class BipartiteGraphMatching {
	ComparableElement[] v1, v2;
	double[][] costMatrix;
	int[] assignment;
	
	public BipartiteGraphMatching()
	{
	}
	
	public <T> double matching(Set<T> a, Set<T> b, MyOWLLogicalEntity orig, MyOWLLogicalEntity des) throws Exception
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
			for (int i = 0; i< v1.length; i++)
			{
				ComparableElement s1 = v1[i];
				for (int j = 0; j < v2.length; j++)
				{
					ComparableElement s2 = v2[j];
					//System.out.println(s1 + " " + s2);
					costMatrix[i][j] = 1 - s1.similarity(s2,orig,des); //The hungarian algorithm minimize. Therefore we convert the similarity in distance
				}
			}
			HungarianAlgorithm hungarn = new HungarianAlgorithm(costMatrix);
			assignment = hungarn.execute();
			//SSP ssp = new SSP(costMatrix);
			//assignment = ssp.execute();
			
			double sim = 0;
			for (int i = 0; i < assignment.length; i++)
			{
				int aux = assignment[i];
				//System.out.println(v1[i]);
				if (aux >=0) //If there is an assignment
				{
					//System.out.println(v1[i] + "\t" + v2[aux] + "\t" + (1-costMatrix[i][aux]));
					//map.put(v1[i], v2[aux]);
					sim += 1-costMatrix[i][aux];
				}
			}
			return 2*sim/(2*Math.max(v1.length, v2.length));  //(v1.length+v2.length);//Maybe it should not be divided by sum of sizes, but by the maximum size of the two parts of the graph. So we penalize the not matched nodes
		}
	}
	
	/*public Map<ComparableElement, ComparableElement> getAssignment()
	{
		return map;
	}*/
}
