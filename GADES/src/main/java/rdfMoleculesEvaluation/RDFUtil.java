package rdfMoleculesEvaluation;

import org.apache.jena.query.*;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dcollarana on 7/23/2016.
 */
public class RDFUtil {

    public List<Pair> getPropertiesFromSubject (String uri, String sparqlEndPoint){

        List<Pair> result = null;
        String sparqlQueryString1 = "PREFIX dbont: <http://dbpedia.org/ontology/> " +
                "SELECT ?predicate ?object " +
                "WHERE { " +
                uri + " ?predicate ?object "+
                "}";

        Query query = QueryFactory.create(sparqlQueryString1);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndPoint, query);

        ResultSet results = qexec.execSelect();
        if (results.hasNext())
            result =  new ArrayList<Pair>();
        while(results.hasNext()) {
            QuerySolution row = results.next();
            String predicate;
            String object;
            if (row.get("predicate").isLiteral())
                predicate = row.getLiteral("predicate").getString();
            else
                predicate = row.get("predicate").toString();
            if (row.get("object").isLiteral())
                object = row.getLiteral("object").getString();
            else
                object = row.get("object").toString();
            //System.out.println("p: "+predicate+" o:"+object);
            result.add(new Pair(predicate, object));
        }
        qexec.close() ;

        return result;
    }

}
