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

    private static final String STATIC_FILES_LOCATION = "/web";

    private static final String DEFAULT_PATH = "/";

    private static final String EMPTY_STRING = "";

    private static final String NO_BREAK_SPACE_ENTITY = "&nbsp";

    private static final VelocityTemplateEngine velocityTemplateEngine = new VelocityTemplateEngine();

    private static CYKAlgorithm cyk;

    public static void main(String[] args) {
        Spark.staticFileLocation(STATIC_FILES_LOCATION);

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
            StringBuilder exceptionHTML = new StringBuilder();
            exceptionHTML.append("<div class=\"alert alert-danger\">")
                    .append(exception.getMessage())
                    .append("</div>");

            Model exceptionModel = new Model(request.queryMap(GRAMMAR_PARAM).value(), exceptionHTML.toString(), EMPTY_STRING);
            response.status(500);
            response.body(velocityTemplateEngine.render(new ModelAndView(exceptionModel.createModelMap(), TEMPLATE)));
        });
    }

    private static String generateRecognitionMatrix(int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = size - 1; i >= 0; --i) {
            sb.append("<tr>");
            for (int j = 0; j < size; ++j) {
                sb.append("<td>");
                String rules = cyk.getSubsequenceRules(i, j);
                if (rules.isEmpty()) {
                    sb.append(NO_BREAK_SPACE_ENTITY);
                } else {
                    sb.append(rules);
                }
                sb.append("</td>");
            }
            sb.append("</tr>");
        }
        return sb.toString();
    }

    @Data
    @AllArgsConstructor
    private static final class Model {
        final String GRAMMAR = "grammar";
        final String ERROR = "error_message";
        final String RESULT = "result";
        String grammar;
        String errorMessage;
        String result;

        Map<String, Object> createModelMap() {
            Map<String, Object> modelMap = new HashMap<>();
            modelMap.put(ERROR, errorMessage);
            modelMap.put(GRAMMAR, grammar);
            modelMap.put(RESULT, result);
            return modelMap;
        }
    }
}
