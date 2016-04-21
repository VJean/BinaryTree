package ontology;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

import java.io.InputStream;

/**
 * Created by JeanV on 21/04/2016.
 */
public class TD5Application {
    public static void main(String[] args) {
        Model model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open("file:res/td5.n3");
        model.read(in, null, "TURTLE");

    }
}
