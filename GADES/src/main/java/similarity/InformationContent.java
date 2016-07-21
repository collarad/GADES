package similarity;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ontologyManagement.MyOWLLogicalEntity;
import ontologyManagement.MyOWLOntology;
import ontologyManagement.OWLConcept;
import test.ComparisonResult;
import test.DatasetTest;

public class InformationContent {
	private static InformationContent instance = null;
	public static InformationContent getInstance() throws Exception
	{
		if (instance == null)
		{
			throw new Exception("No InformationContent instance created. You should call the constructor before calling this method.");
		}
		return instance;
	}
	
	//private Map<OWLConcept, Integer> occurrences;
	private Map<MyOWLLogicalEntity, Set<Entity>> occurrences;
	private Map<MyOWLLogicalEntity, Double> ics;
	private int totalAnnotations;
	private double maxIC;
	
	public InformationContent(List<ComparisonResult> comparisons, String[] files, MyOWLOntology o)
	{
		instance = this;
		ics = new HashMap<MyOWLLogicalEntity, Double>();
		//occurrences = new HashMap<OWLConcept, Integer>();
		occurrences = new HashMap<MyOWLLogicalEntity, Set<Entity>>();
		Set<String> proteins = new HashSet<String>();
		for (Iterator<ComparisonResult> i = comparisons.iterator(); i.hasNext();)
		{
			ComparisonResult cR = i.next();
			proteins.add(cR.getConceptA());
			proteins.add(cR.getConceptB());
		}
		totalAnnotations = proteins.size();//0;
		for (Iterator<String> i = proteins.iterator(); i.hasNext();)
		{
			String prot = i.next();
			for (String file:files)
			{
				Set<MyOWLLogicalEntity> anns = DatasetTest.getConceptAnnotations(prot, file, o, true);
				//System.out.println(prot + "\t" + anns.size());
				Set<OWLConcept> anc = new HashSet<OWLConcept>();
				for (MyOWLLogicalEntity a: anns)
				{
					Set<OWLConcept> ancestors = o.getAncestors(a.getOWLConcept());
					anc.addAll(ancestors);
				}
				anns.addAll(anc);
				//totalAnnotations += anns.size();
				for (Iterator<MyOWLLogicalEntity> j = anns.iterator(); j.hasNext();)
				{
					MyOWLLogicalEntity c = j.next();
					/*Integer v = occurrences.get(c);
					if (v == null)
						v = 0;
					occurrences.put(c, v + 1);*/
					Set<Entity> v = occurrences.get(c);
					if (v == null)
						v = new HashSet<Entity>();
					Entity protEnt = Entity.getEntity(prot);
					v.add(protEnt);
					occurrences.put(c, v);
				}
			}
		}
		for (Iterator<MyOWLLogicalEntity> i = occurrences.keySet().iterator(); i.hasNext();)
		{
			MyOWLLogicalEntity c = i.next();
			this.setIC(c);
		}
		maxIC = Collections.max(ics.values());
	}
	
	
	/*public InformationContent(Set<String> entities, String file, MyOWLOntology o)
	{
		instance = this;
		ics = new HashMap<OWLConcept, Double>();
		//occurrences = new HashMap<OWLConcept, Integer>();
		occurrences = new HashMap<OWLConcept, Set<Entity>>();

		totalAnnotations = entities.size();//0;
		for (Iterator<String> i = entities.iterator(); i.hasNext();)
		{
				String prot = i.next();
				Set<OWLConcept> anns = DatasetTest.getConceptAnnotations(prot, file, o, true);
				//totalAnnotations += anns.size();
				for (Iterator<OWLConcept> j = anns.iterator(); j.hasNext();)
				{
					OWLConcept c = j.next();
					Integer v = occurrences.get(c);
					if (v == null)
						v = 0;
					occurrences.put(c, v + 1);
					Set<Entity> v = occurrences.get(c);
					if (v == null)
						v = new HashSet<Entity>();
					Entity protEnt = Entity.getEntity(prot);
					v.add(protEnt);
					occurrences.put(c, v);
				}
		}
		for (Iterator<OWLConcept> i = occurrences.keySet().iterator(); i.hasNext();)
		{
			OWLConcept c = i.next();
			this.setIC(c);
		}
		occurrences.clear();
	}*/
	
	static class Entity{
		private String name;
		private static Map<String, Entity> entMap = new HashMap<String, Entity>();
		
		public Entity(String n)
		{
			name = n;
		}
		
		public static Entity getEntity(String n)
		{
			Entity e = entMap.get(n);
			if (e == null)
				e = new Entity(n);
			return e;
		}
		
		public String getName()
		{
			return name;
		}
		
		public boolean equals(Object o)
		{
			return equals((Entity) o);
		}
		
		public boolean equals(Entity a)
		{
			return name.matches(a.name);
		}
		
		public int hashCode()
		{
			return name.hashCode();
		}
	}
	
	private static Set<OWLConcept> getLeafNodes(Set<OWLConcept> conceptsOrig)
	{
		Set<OWLConcept> concepts = new HashSet<OWLConcept>(conceptsOrig);
		Set<OWLConcept> auxSet = new HashSet<OWLConcept>(concepts);
		for (OWLConcept c: auxSet)
		{
			for (OWLConcept d: auxSet)
			{
				if (!c.equals(d))
				{
					if (d.isSubConceptOf(c))
						concepts.remove(c);
				}
			}
		}
		return concepts;
	}
	
	/*public InformationContent(String fileCorpus, MyOWLOntology o)
	{
		instance = this;
		ics = new HashMap<OWLConcept, Double>();
		
		InputStream    fis;
		BufferedReader br;
		String         line;
		
		//occurrences = new HashMap<OWLConcept, Integer>();
		occurrences = new HashMap<OWLConcept, Set<Entity>>();
			
		File f = new File(fileCorpus);
		String targetFile = f.getParent() + "icCESSM.txt";
		File target = new File(targetFile);
		if (target.exists())
		{
			try {
				fis = new FileInputStream(f);
				br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
				line = br.readLine();
				while (line != null)
				{
					String term = line.split("\t")[0];
					String element = o.getOntologyPrefix() + term.replace(":", "_");
				    OWLConcept c = o.getOWLConcept(element);
					double ic = Double.parseDouble(line.split("\t")[1]);
					ics.put(c, ic);
				}					
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else
		{
			Set<OWLConcept> concepts = new HashSet<OWLConcept>();
			
			//Set<Entity> proteins = new HashSet<Entity>();
			Set<String> proteins = new HashSet<String>();
			
			try {
				fis = new FileInputStream(f);
				br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
				line = br.readLine();
				String protAux = line.split(" ")[0];
				while (line != null)
				{
					String prot = line.split(" ")[0];
					if (!prot.matches(protAux))
					{
						Set<OWLConcept> leaf = getLeafNodes(concepts);
						Set<OWLConcept> superConcepts = new HashSet<OWLConcept>();
						for (OWLConcept c: leaf)
						{
							superConcepts.addAll(c.getSuperConcepts());;
							Integer v = occurrences.get(c);
							if (v == null)
								v = 0;
							occurrences.put(c, v + 1);
						}
						for (OWLConcept c: superConcepts)
						{
							Integer v = occurrences.get(c);
							if (v == null)
								v = 0;
							occurrences.put(c, v + 1);
						}
						concepts.clear();
						protAux = prot;
					}
					String term = line.split(" ")[1];
					String element = o.getOntologyPrefix() + term.replace(":", "_");
					OWLConcept c = o.getOWLConcept(element);
					if (c != null)
					{
					   	//Set<Entity> v = occurrences.get(c);
					   	Integer v = occurrences.get(c);
						if (v == null)
						{
							//v = new HashSet<Entity>();
							v = 0;
						}
						//Entity protEnt = Entity.getEntity(prot);
						//proteins.add(protEnt);
						
						//v.add(protEnt);
						//occurrences.put(c, v + 1);
					   	proteins.add(prot);
					   	concepts.add(c);
					}
				    line = br.readLine();
				}
				br.close();
			}
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			totalAnnotations = proteins.size();
			proteins.clear();
			for (Iterator<OWLConcept> i = occurrences.keySet().iterator(); i.hasNext();)
			{
				OWLConcept c = i.next();
				this.setIC(c);
			}
	
			PrintWriter writer;
			try {
				writer = new PrintWriter(targetFile);
				for (Iterator<OWLConcept> i = occurrences.keySet().iterator(); i.hasNext();)
				{
					OWLConcept c = i.next();
					writer.println(c.getName() + "\t" + ics.get(c));
				}
				writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}*/
	
	protected void setIC(MyOWLLogicalEntity c)
	{
		double freq = 0;
		Set<OWLConcept> subConcepts = Collections.emptySet();
		if (c instanceof OWLConcept)
		{	
			subConcepts = ((OWLConcept) c).getSubConcepts();
			subConcepts.add((OWLConcept) c);
		}
			
		Set<Entity> union = new HashSet<Entity>();
		if (c instanceof OWLConcept)
		{
			for (Iterator<OWLConcept> i = subConcepts.iterator(); i.hasNext();)
			{
				OWLConcept a = i.next();
				//Integer aux = occurrences.get(a);
				Set<Entity> aux = occurrences.get(a);
				if (aux == null)
					aux = Collections.emptySet();//0;
				union.addAll(aux);
				//freq += aux;
			}
		}
		else
			union = occurrences.get(c);
		freq = union.size();
		if (freq/totalAnnotations > 1)
			System.out.println(c);
		ics.put(c, -Math.log(freq/totalAnnotations));
	}
	
	public double getIC(MyOWLLogicalEntity c)
	{
		Double ic = ics.get(c); 
		if ( ic == null)
		{
			System.out.println("ERROR");
			return 1;
			//setIC(c);
			//ic = ics.get(c);
		}
		//double maxIC = Collections.max(ics.values());
		return ic/maxIC;
	}

}
