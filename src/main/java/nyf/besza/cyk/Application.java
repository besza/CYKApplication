package nyf.besza.cyk;

import lombok.AllArgsConstructor;
import lombok.Data;
import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Application {

    private static final String TEMPLATE = "index.vm";

    private static final String INPUT_PARAM = "input";

    private static final String GRAMMAR_PARAM = "grammar";

    private static final String STATIC_PARAM_LOCATION = "/web";

    private static final String DEFAULT_PATH = "/";

    private static final String EMPTY_STRING = "";

    private static final VelocityTemplateEngine velocityTemplateEngine = new VelocityTemplateEngine();

    private static CYKAlgorithm cyk;

    @Data
    @AllArgsConstructor
    private static final class Model {
        String grammar;
        String errorMessage;
        String result;

        final String GRAMMAR = "grammar";
        final String ERROR = "error_message";
        final String RESULT = "result";


        Map<String, Object> createModelMap() {
            Map<String, Object> modelMap = new HashMap<>();
            modelMap.put(ERROR, errorMessage);
            modelMap.put(GRAMMAR, grammar);
            modelMap.put(RESULT, result);
            return modelMap;
        }
    }

    public static void main(String[] args) {
        Spark.staticFileLocation(STATIC_PARAM_LOCATION);

        get(DEFAULT_PATH, (request, response) -> {
            Model defaultModel = new Model(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING);
            return velocityTemplateEngine.render(new ModelAndView(defaultModel.createModelMap(), TEMPLATE));
        });

        post(DEFAULT_PATH, (request, response) -> {
            String input = request.queryMap(INPUT_PARAM).value();
            String text = request.queryMap(GRAMMAR_PARAM).value();

            Grammar grammar = GrammarParser.parseGrammar(text);

            cyk = new CYKAlgorithm(grammar);

            final boolean success = cyk.executeAlgorithm(input);

            StringBuilder resultHTML = new StringBuilder();
            resultHTML.append("<div class=\"col-sm-6\">")
                    .append("<h3>Felismerési mátrix</h3>")
                    .append("<table class=\"table\">")
                    .append(generateRecognitionMatrix(input.length()))
                    .append("</table>");

            if (success) {
                resultHTML.append("<p class=\"bg-success\">").append(input).append(" eleme a nyelvtan által generált nyelvnek");
            } else {
                resultHTML.append("<p class=\"bg-danger\">").append(input).append(" nem eleme a nyelvtan által generált nyelvnek");
            }
            resultHTML.append("</p>")
                    .append("</div>");

            Model postModel = new Model(request.queryMap(GRAMMAR_PARAM).value(), EMPTY_STRING, resultHTML.toString());

            return velocityTemplateEngine.render(new ModelAndView(postModel.createModelMap(), TEMPLATE));
        });

        exception(GrammarParserException.class, (exception, request, response) -> {
            Model exceptionModel = new Model(request.queryMap(GRAMMAR_PARAM).value(), exception.getMessage(), EMPTY_STRING);
            response.status(500);
            response.body(velocityTemplateEngine.render(new ModelAndView(exceptionModel.createModelMap(), TEMPLATE)));
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
