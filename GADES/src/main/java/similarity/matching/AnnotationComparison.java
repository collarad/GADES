package similarity.matching;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;


public class AnnotationComparison{

	private Object conceptA;
	private Object conceptB;
	private int hash;
	
	public AnnotationComparison(Object a, Object b)
	{
		conceptA = a;
		conceptB = b;
		if (a.toString().compareTo(b.toString()) < 0)
			hash = a.hashCode() ^ b.hashCode();
		else
			hash = b.hashCode() ^ a.hashCode();
	}

	public String toString()
	{
		Locale locale  = new Locale("en", "US");
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getNumberInstance(locale);
		formatter.applyPattern("#0.00000000");  
		return conceptA + "\t" + conceptB;
	}
	
	public Object getConceptA()
	{
		return conceptA;
	}
	
	public Object getConceptB()
	{
		return conceptB;
	}
	
	public boolean equals(Object o)
	{
		if (o instanceof AnnotationComparison)
			return equals((AnnotationComparison) o);
		return false;
	}
	
	public boolean equals(AnnotationComparison b)
	{
		return conceptA.equals(b.conceptA) && conceptB.equals(b.conceptB) || conceptA.equals(b.conceptB) && conceptB.equals(b.conceptA);
	}
	
	public int hashCode(){
		
		return hash;
	}
}
