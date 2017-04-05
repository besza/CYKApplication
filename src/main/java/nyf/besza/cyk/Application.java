package nyf.besza.cyk;

import spark.Spark;
import static spark.Spark.post;
import static j2html.TagCreator.body;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.link;
import static j2html.TagCreator.p;
import static j2html.TagCreator.title;
import j2html.tags.Tag;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author szabolcs
 */
public class Application {

    public static void main(String[] args) {
        Spark.staticFileLocation("/web");

        post("/", (req, res) -> {
            input = req.queryMap("input").value();
            String grammar = req.queryMap("grammar").value();
            cyk = new CYKAlgorithm(grammar);
            final boolean success = cyk.executeAlgorithm(input);

            return html().with(
                    head().with(title("Result"), link().withRel("stylesheet").withHref("styles/main.css")),
                    body().with(
                            showResult(success),
                            p(showRecognitionMatrix(cyk.getInputLength()))));
        });
    }
    
    private static CYKAlgorithm cyk;
    private static String input;

    private static Tag showResult(boolean success) {
        if (success) {
            return p(input + " eleme L(G)-nek.");
        } else {
            return p(input + " nem eleme L(G)-nek.");
        }
    }

    private static String showRecognitionMatrix(int size){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                String rules = cyk.getSubsequenceRules(i, j);
                if (rules.isEmpty())
                    sb.append("-");
                else sb.append(rules);
                sb.append("\t\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
