package ontologyManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLLogicalEntity;

import similarity.InformationContent;
import similarity.matching.AnnSim;

public class OWLConcept extends MyOWLLogicalEntity{
	private boolean satisfiable;
	private OWLClass cl;
	private String name;
	
	public OWLConcept(OWLClass a, MyOWLOntology onto)
	{
		o = onto;
		uri = a.getIRI().toString();//.toURI().toString();
		neighbors = null;
		cl = a;
		name = uri.replaceAll(o.getOntologyPrefix(),"").replace("_",":");
		satisfiable = isSatisfiable();
	}
	
	public OWLClass getOWLClass()
	{
		return cl;
	}
	
	public void setNeighbors(Set<OWLLink> n)
	{
		neighbors = n;
	}
	
	public Set<OWLLink> getNeighbors()
	{
		if (neighbors == null)
			neighbors = o.getConceptOWLLink(this);
		return neighbors;
	}
	
	public void dispose()
	{
		neighbors.clear();
	}
	
	public Set<OWLConcept> getSubConcepts()
	{
		return o.getSubConcepts(this);
	}
	
	public Set<OWLConcept> getSuperConcepts()
	{
		return o.getAncestors(this);
	}
	
	public String getURI()
	{
		return uri;
	}
	
	public String toString()
	{
		return uri;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean isSatisfiable ()
	{
		return o.isSatisfiable(getOWLClass());
	}
	
	@Override
	protected double similarityNeighbors(MyOWLLogicalEntity c) throws Exception {
		if (c instanceof OWLConcept)
			return similarityNeighbors((OWLConcept)c);
		if (c instanceof MyOWLIndividual)
			return similarityNeighbors((MyOWLIndividual)c);
		else
			throw new Exception("Invalid comparison between " + this + " and " + c);
	}
	
	public double similarityNeighbors(MyOWLIndividual c)
	{
		//BipartiteGraphMatching bpm = new BipartiteGraphMatching();
		AnnSim bpm = new AnnSim();
		if (neighbors == null)
			neighbors = o.getConceptOWLLink(this);
		if (c.neighbors == null)
			c.neighbors = o.getIndividualOWLLink(c);
		try {
			double sim = bpm.maximumMatching(neighbors, c.neighbors, this, c);
			return sim;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0.0;
	}

	public double similarityNeighbors(OWLConcept c)
	{
		//BipartiteGraphMatching bpm = new BipartiteGraphMatching();
		AnnSim bpm = new AnnSim();
		if (neighbors == null)
			neighbors = o.getConceptOWLLink(this);
		if (c.neighbors == null)
			c.neighbors = o.getConceptOWLLink(c);
		try {
			double sim = bpm.maximumMatching(neighbors, c.neighbors, this, c);
			return sim;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0.0;
	}
	
	public double similarityNeighborsAchim(OWLConcept c, int expRadius)
	{
		List<Map<OWLConcept, Integer>> nA = new ArrayList<Map<OWLConcept, Integer>>();
		List<Map<OWLConcept, Integer>> nB = new ArrayList<Map<OWLConcept, Integer>>();
		Map<OWLConcept, Integer> tA = new HashMap<OWLConcept, Integer>();
		tA.put(this, 1);
		Map<OWLConcept, Integer> tB = new HashMap<OWLConcept, Integer>();
		tB.put(c, 1);
		nA.add(tA);
		nB.add(tB);
		
		for (int i = 1; i <= expRadius; i++)
		{
			Map<OWLConcept, Integer> auxA = nA.get(i - 1);
			Map<OWLConcept, Integer> auxANext = new HashMap<OWLConcept, Integer>();
			Map<OWLConcept, Integer> auxB = nB.get(i - 1);
			Map<OWLConcept, Integer> auxBNext = new HashMap<OWLConcept, Integer>();
			
			for (OWLConcept con: auxA.keySet())
			{
				Set<OWLLink> lA = o.getDirectNeighbors(con);
				
				for (OWLLink l: lA)
				{
					OWLConcept neigh = (OWLConcept) l.getDestiny();
					Integer n = auxANext.get(l.getDestiny());
					if (n == null)
						n = 0;
					auxANext.put(neigh, n + 1);
				}
			}
			nA.add(auxANext);
			
			for (OWLConcept con: auxB.keySet())
			{
				Set<OWLLink> lA = o.getDirectNeighbors(con);
				
				for (OWLLink l: lA)
				{
					OWLConcept neigh = (OWLConcept) l.getDestiny();
					Integer n = auxBNext.get(l.getDestiny());
					if (n == null)
						n = 0;
					auxBNext.put(neigh, n + 1);
				}
			}
			nB.add(auxBNext);
		}
		
		
		Set<OWLConcept> elA = new HashSet<OWLConcept>();
		Set<OWLConcept> intersection = new HashSet<OWLConcept>();
		for (Map<OWLConcept, Integer> m: nA)
		{
			elA.addAll(m.keySet());
		}
		intersection.addAll(elA);
		Set<OWLConcept> elB = new HashSet<OWLConcept>();
		for (Map<OWLConcept, Integer> m: nB)
		{
			elB.addAll(m.keySet());
		}
		intersection.retainAll(elB);
		
		double total = 0;
		
		for (OWLConcept con: intersection)
		{
			double sumA = 0, sumB = 0;
			for (int i = 0; i <= expRadius; i++)
			{
				Map<OWLConcept, Integer> neighsA = nA.get(i);
				
				Integer x = neighsA.get(con);
				if (x == null)
					x = 0;
				sumA += Math.pow(0.5, i) * x;
			}
			
			for (int i = 0; i <= expRadius; i++)
			{
				Map<OWLConcept, Integer> neighsB = nB.get(i);
				
				Integer x = neighsB.get(con);
				if (x == null)
					x = 0;
				sumB += Math.pow(0.5, i) * x;
			}
			total += sumA * sumB;
		}
		
		
		double maxA = 0, maxB = 0;
		for (OWLConcept con: elA)
		{
			double sumA = 0;
			for (int i = 0; i <= expRadius; i++)
			{
				Map<OWLConcept, Integer> neighsA = nA.get(i);
				
				Integer x = neighsA.get(con);
				if (x == null)
					x = 0;
				sumA += Math.pow(0.5, i) * x;
			}
			maxA += sumA * sumA;
		}
		
		for (OWLConcept con: elB)
		{
			double sumB = 0;
			for (int i = 0; i <= expRadius; i++)
			{
				Map<OWLConcept, Integer> neighsB = nB.get(i);
				
				Integer x = neighsB.get(con);
				if (x == null)
					x = 0;
				sumB += Math.pow(0.5, i) * x;
			}
			maxB += sumB * sumB;
		}
		
		double max = Math.max(maxA, maxB);
		if (total > max)
			System.out.println("ERROR: Total and max incorrectly computed");
		if (max == 0)
			max = 1;
		return total / max;
	}
	
	public double taxonomicSimilarity(OWLConcept c)
	{
		return o.taxonomicClassSimilarity(this, c);
	}
	
	public double taxonomicSimilarity(MyOWLIndividual c)
	{
		Set<OWLConcept> concepts = c.getTypes();
		double max = 0;
		for (OWLConcept cn: concepts)
		{
			double sim = this.taxonomicSimilarity(cn);
			if (sim > max)
				max = sim;
		}
		return max;
	}
	
	public double taxonomicSimilarity(MyOWLLogicalEntity c) throws Exception
	{
		if (c instanceof OWLConcept)
			return taxonomicSimilarity((OWLConcept)c);
		if (c instanceof MyOWLIndividual)
			return taxonomicSimilarity((MyOWLIndividual) c);
		else
			throw new Exception("Invalid comparison between " + this + " and " + c);
	}
	
	public boolean isSubConceptOf(OWLConcept c)
	{
		return o.isSubClassOf(getOWLClass(), c.getOWLClass());
	}
	
	public double getIC()
	{
		InformationContent ic;
		Double res = null;
		try {
			ic = InformationContent.getInstance();
			res = ic.getIC(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	public double similarityIC(OWLConcept c)
	{
		double informC = 0;
		try {
			OWLConcept lca = this.getLCA(c);
			InformationContent ic = InformationContent.getInstance();
			informC = ic.getIC(lca);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return informC;
	}
	
	
	public double similarityMICA(OWLConcept c)
	{
		double informC = 0;
		try {
			Set<OWLConcept> cA = o.getAncestors(this);
			Set<OWLConcept> cB = o.getAncestors(c);
			cA.retainAll(cB);
			InformationContent ic = InformationContent.getInstance();
			for (OWLConcept con: cA)
			{
				if (ic.getIC(con) > informC)
					informC = ic.getIC(con);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return informC;
	}
	
	public double getDepth()
	{
		getOWLClass();
		return o.prof(this.cl);
	}
	
	public double similarity(OWLConcept c) throws Exception
	{
		if (!satisfiable || !c.satisfiable)
			return 0;		
		
		if (this == c)
			return 1.0;
		
		double sim = OnSim(c);
		
		return sim;
	}
	
	
	
	
	public boolean similarityByPass(OWLConcept c)
	{
		double informC = 0;
		try {
			OWLConcept lca = this.getLCA(c);
			InformationContent ic = InformationContent.getInstance();
			informC = ic.getIC(lca);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double taxSim = taxonomicSimilarity(c);
		return !(taxSim > 0 && informC > 0);

	}
	
	public OWLConcept getLCA(OWLConcept b)
	{
		return o.getLCS(this, b);
	}

	

	@Override
	public double similarity(MyOWLLogicalEntity a) throws Exception {
		if (a instanceof OWLConcept)
			return similarity((OWLConcept)a);
		if (a instanceof MyOWLIndividual)
			return ((MyOWLIndividual) a).similarity(this);
		else
			throw new Exception("Invalid comparison between " + this + " and " + a);
		
	}

	@Override
	public OWLLogicalEntity getOWLLogicalEntity() {
		return cl;
	}
}
