package sample.com.app.cucumber;

import gherkin.*;
import gherkin.ast.Feature;
import gherkin.compiler.Compiler;
import gherkin.formatter.*;
import gherkin.util.FixJava;
import pickles.Pickle;

import java.io.*;
import java.util.List;

import java.io.FileInputStream;
import java.io.InputStreamReader;

import gherkin.formatter.JSONFormatter;
import gherkin.formatter.JSONPrettyFormatter;
/**
 * Created by Yuvaraj Gunisetti on 17/01/16.
 * Purpose:
 */
public class GherkinParser {

    Parser<Feature> parser = new Parser<>(new AstBuilder());
    private List<Pickle> pickles;
    private String gherkin;

    public Feature getParsedFeature(String featureFilePath) throws Exception{
        Feature feature = null;
        FileReader reader = new FileReader(featureFilePath);
        feature = parser.parse(reader);
        return feature;
    }

    public List<Pickle> returnScenarios(String featureFilePath){
        try{
            Feature feature = getParsedFeature(featureFilePath);
            pickles = new Compiler().compile(feature);
            return pickles;
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return pickles;
    }

    public String getFeatureFromJson(String json) throws Exception{
        String gherkin1;
        gherkin1 = FixJava.readReader(new InputStreamReader(new StringBufferInputStream(json),
                "UTF-8"));
        StringBuilder stringFe = new StringBuilder();
        PrettyFormatter formatter = new PrettyFormatter(stringFe, true, false);
        JSONParser jsonParser = new JSONParser(null,formatter);
        jsonParser.parse(gherkin1);
        formatter.done();
        formatter.close();
        System.out.println(stringFe);
        return stringFe.toString();
    }
    public String getFeatureJson(String featurePath) throws Exception{
        String format = "pretty";
        gherkin = FixJava.readReader(new InputStreamReader(
                new FileInputStream(featurePath), "UTF-8"));
        StringBuilder json = new StringBuilder();
        JSONFormatter formatter;
        if (format.equalsIgnoreCase("ugly")) {
            formatter = new JSONFormatter(json);// not pretty
        } else {
            formatter = new JSONPrettyFormatter(json);// pretty
        }
        gherkin.parser.Parser parser = new gherkin.parser.Parser(formatter);
        parser.parse(gherkin, featurePath, 0);
        formatter.done();
        formatter.close();
        System.out.println(json);
        return json.toString();
    }
}

//    public String generateGson(String featurePath){
//        try{
//            Gson gson = new Gson();
//            Parser<Feature> parser = new Parser<>(new AstBuilder());
//            TokenMatcher matcher = new TokenMatcher();
//            long startTime = System.currentTimeMillis();
//            Reader in = new InputStreamReader(new FileInputStream(featurePath), "UTF-8");
//            Feature feature = parser.parse(in, matcher);
//            System.out.print(gson.toJson(feature));
//            return gson.toJson(feature);
//        }
//        catch (Exception e) {
//            System.err.println(e.getMessage());
//            System.exit(1);
//        }
//        return "";
//    }