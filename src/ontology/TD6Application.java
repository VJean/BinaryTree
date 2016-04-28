package ontology;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by JeanV on 28/04/2016.
 */
public class TD6Application {
    public static void main(String[] args) {
        test();
    }

    public static void test() {
        String query = "file:res/lgd-countries-capitals.sparql"; // fichier contenant la requête
        Model model = ModelFactory.createDefaultModel();

        InputStream in = FileManager.get().open("file:res/foaf.n3");
        model.read(in, null, "TURTLE");
        runSelectQuery(query, model);

    }
    public static void runSelectQuery(String qfilename, Model model) {
        Query query = QueryFactory.read(qfilename);

        System.setProperty("http.proxyHost","proxyweb.utc.fr");
        System.setProperty("http.proxyPort","3128");

        QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://linkedgeodata.org/sparql", query);
        ResultSet r = queryExecution.execSelect();
        ResultSetFormatter.out(System.out,r);
        queryExecution.close();
    }
}
