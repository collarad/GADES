package test;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ComparisonResult {
	private String conceptA;
	private String conceptB;
	private double similarity;
	private int hash;
	
	public ComparisonResult(String a, String b)
	{
		conceptA = a;
		conceptB = b;
		similarity = -1.0;
		if (a.compareTo(b) < 0)
			hash = a.hashCode() ^ b.hashCode();
		else
			hash = b.hashCode() ^ a.hashCode();
	}
	
	public String toString()
	{
		Locale locale  = new Locale("en", "US");
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getNumberInstance(locale);
		formatter.applyPattern("#0.00000000");  
		return conceptA + "\t" + conceptB + "\t" + formatter.format(similarity);
	}
	
	public String getConceptA()
	{
		return conceptA;
	}
	
	public String getConceptB()
	{
		return conceptB;
	}
	
	public void setSimilarity (double s)
	{
		similarity = s;
	}
	public double getSimilarity ()
	{
		return similarity;
	}
	public boolean equals(Object o)
	{
		if (o instanceof ComparisonResult)
			return equals((ComparisonResult) o);
		return false;
	}
	
	public boolean equals(ComparisonResult b)
	{
		return conceptA.matches(b.conceptA) && conceptB.matches(b.conceptB) || conceptA.matches(b.conceptB) && conceptB.matches(b.conceptA);
	}
	
	public int hashCode(){
		
		return hash;
	}

}
