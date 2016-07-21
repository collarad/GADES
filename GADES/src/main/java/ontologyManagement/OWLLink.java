package ontologyManagement;

import java.util.Set;

import similarity.ComparableElement;
import similarity.matching.BipartiteGraphMatching;
import test.ComparisonResult;





public class OWLLink implements ComparableElement {
	private OWLRelation relation;
	private MyOWLLogicalEntity destiny;
	private Set<OWLExplanation> explanations;
	

	public OWLLink( OWLRelation r, MyOWLLogicalEntity b, Set<OWLExplanation> exp) {
		relation = r;
		destiny = b;
		explanations = exp;
	}
	
	public OWLLink(OWLRelation r, MyOWLLogicalEntity b) {
		relation = r;
		destiny = b;
		explanations = null;
	}

	public Set<OWLExplanation> getExplanations() {
		return explanations;
	}

	public void setExplanations(Set<OWLExplanation> explanations) {
		this.explanations = explanations;
	}
	
	public String toString()
	{
		return relation.toString() + " " + destiny.toString();
	}
	
	public OWLRelation getRelation()
	{
		return relation;
	}
	
	public MyOWLLogicalEntity getDestiny()
	{
		return destiny;
	}
	
	
	public double similarity(OWLLink a, MyOWLLogicalEntity conceptA, MyOWLLogicalEntity conceptB)
	{
		BipartiteGraphMatching bpm = new BipartiteGraphMatching();
		
		try {
			double sim = 0;
			/*double simTaxRel = relation.similarity(a.relation);
			if (simTaxRel == 0)
				sim = 0;
			else
			{
				double simTaxDes = destiny.taxonomicSimilarity(a.destiny);
				double simExp = bpm.matching(explanations, a.explanations, conceptA, conceptB);
				sim = 0.1*simTaxRel + 0.5*simTaxDes + 0.4*simExp;
			}*/
			double simTaxRel = relation.similarity(a.relation);
			double simTaxDes = destiny.taxonomicSimilarity(a.destiny);
			double simExp = 1;//0;
			//if (simTaxRel != 0 && simTaxDes != 0)
			//	simExp = bpm.matching(explanations, a.explanations, conceptA, conceptB);
			sim = simTaxRel*simTaxDes*simExp;
			return sim;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0.0;
	}

	public double similarity(ComparableElement a, MyOWLLogicalEntity org, MyOWLLogicalEntity des) throws Exception {
		if (!(a instanceof OWLLink))
			throw new Exception("Invalid comparison");
		return similarity((OWLLink)a, org, des);
	}
	
	public boolean equals(Object o)
	{
		if (o instanceof OWLLink)
			return equals((OWLLink) o);
		return false;
	}
	
	public boolean equals(OWLLink b)
	{
		return relation == b.relation && destiny.getName().matches(b.destiny.getName());
	}
	
	public int hashCode(){
		
		return relation.hashCode() ^ destiny.hashCode();
	}

}
