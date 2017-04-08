package nyf.besza.cyk;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

public class Application {

    private static final String TEMPLATE = "index.vm";
    private static CYKAlgorithm cyk;
    private static String input;

    public static void main(String[] args) {
        Spark.staticFileLocation("/web");

        post("/", (req, res) -> {
            input = req.queryMap("input").value();
            String grammar = req.queryMap("grammar").value();
            cyk = new CYKAlgorithm(grammar);
            final boolean success = cyk.executeAlgorithm(input);
            Map<String, Object> model = new HashMap<>();
            model.put("table_body", generateRecognitionMatrix(input.length()));
            if (success) {
                model.put("can_generate", input + " szó eleme a nyelvtan által generált nyelvnek.");
            } else {
                model.put("can_generate", input + " a szó nem eleme a nyelvtan által generált nyelvnek.");
            }
            model.put("visibile", "block");
            return new VelocityTemplateEngine().render(new ModelAndView(model, TEMPLATE));
        });

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("visible", "hidden");
            return new VelocityTemplateEngine().render(new ModelAndView(model, TEMPLATE));
        });
    }

    private static String generateRecognitionMatrix(int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; ++i) {
            sb.append("<tr>");
            for (int j = 0; j < size; ++j) {
                sb.append("<td>");
                String rules = cyk.getSubsequenceRules(i, j);
                if (rules.isEmpty()) {
                    sb.append("-");
                } else {
                    sb.append(rules);
                }
                sb.append("</td>");
            }
            sb.append("</tr>");
        }
        return sb.toString();
    }

}
