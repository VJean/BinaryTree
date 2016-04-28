package ontology;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.FileManager;

import java.io.*;

/**
 * Created by JeanV on 21/04/2016.
 */
public class TD5Application {
    public static void main(String[] args) {
        Model model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open("file:res/td5.n3");
        model.read(in, null, "TURTLE");

        String nstd5 = model.getNsPrefixURI("td5");
        String foaf = model.getNsPrefixURI("foaf");

        Property name = model.getProperty(foaf + "name");

        Selector selectTypes = new SimpleSelector((Resource)null,name,readInput());
        StmtIterator iterator = model.listStatements(selectTypes);
        while (iterator.hasNext()) {
            Statement st = iterator.nextStatement();
            RDFNode obj = st.getSubject();
            System.out.println(obj.toString());
        }
    }

    public static String readInput() {
        BufferedReader br=null;
        String chaine;
        try {
            try {
                br = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("saisie : ");
                chaine=br.readLine();
                return chaine;
            }
            catch(EOFException e) {
                br.close();
            }
        }
        catch(IOException e) {
            System.out.println("IO Exception");
        }

        return null;
    }
}
