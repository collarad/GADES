#GADES

Extension of the similarity measure ICOnSim with Information Content. A Similarity Measure for Determining Relatedness Between Ontology Terms.

[1] Ignacio Traverso-Ribón, Maria-Esther Vidal, Benedikt Kämpgen and York Sure-Vetter. GADES: A Graph-based Semantic Similarity Measure. Semantics 2016, Leipzig.

[2] Ignacio Traverso-Ribón, Maria-Esther Vidal and Guillermo Palma. A Similarity Measure for Determining Relatedness Between Ontology Terms. 11th  International Conference on Data Integration in the Life Sciences 2015 (DILS2015).

[3] Ignacio Traverso-Ribón, Maria-Esther Vidal. Exploiting Information Content and Semantics to Accurately Computing Similarity of GO-based Annotated Entities. 2015 IEEE Conference on Computational Intelligence in Bioinformatics and Computational Biology

[4] Palma, G.; Vidal, M. E.; Haag, E.; Raschid, L.; Thor, A. Measuring Relatedness Between Scientific Entities in Annotation Datasets. ACM International Conference on Bioinformatics, Computational Biology, and Biomedical Informatics (BCB), 2013


# Setting up GADES
Once you have downloaded the code from GitHub you only need to modify the file src/main/java/test/DatasetTestPhase.java to run GADES.

GADES need as inputs:
* A knowledge graph in OWL format.
* Two knowledge graph entities to be compared.

## Main example
In this example we compare the entities http://purl.org/obo/owl/GO#GO_0006355 and http://purl.org/obo/owl/GO#GO_0006342, which are two classes from the Gene Ontology.

    		String ontFile = prefix + "goProtein/go.owl";
		MyOWLOntology o = new MyOWLOntology(ontFile, ontPrefix.get(prefix), "HermiT");
		
		
		OWLConcept a = o.getOWLConcept("http://purl.org/obo/owl/GO#GO_0006355");
		OWLConcept b = o.getOWLConcept("http://purl.org/obo/owl/GO#GO_0006342");
		Set<MyOWLLogicalEntity> anns = new HashSet<MyOWLLogicalEntity>();
		anns.add(a);
		anns.add(b);
		o.setOWLLinks(anns);
		Set<OWLLink> neighA = a.getNeighbors();
		Set<OWLLink> neighB = b.getNeighbors();
		System.out.println(b.similarityIC(a));//.taxonomicSimilarity(a));
		
		OWLRelation r1 = o.getOWLRelation("http://purl.obolibrary.org/obo/RO_0002213");
		System.out.println(o.getPropertyChains(r1));
		OWLRelation r2 = o.getOWLRelation("http://purl.obolibrary.org/obo/RO_0002211");
		System.out.println(o.getPropertyChains(r2));
		System.out.println(r1.similarity(r2));

# License
This work is licensed under [GNU/GPL v2](https://www.gnu.org/licenses/gpl-2.0.html).
