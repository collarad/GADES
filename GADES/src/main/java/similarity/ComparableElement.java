package similarity;

import ontologyManagement.MyOWLLogicalEntity;
import ontologyManagement.OWLConcept;

public interface ComparableElement {
	double similarity(ComparableElement a, MyOWLLogicalEntity org, MyOWLLogicalEntity des) throws Exception;
	//String getId();
}
