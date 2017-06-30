package sample.com.app.cucumber;

import gherkin.ast.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuvaraj Gunisetti on 17/01/16.
 * Purpose:
 */
public class CucumberParser {

    /*
    * gets Background Name
    */
    public String getFeatureBackGroundName(Feature feature){
        String name = "";
        if(feature.getBackground() != null) {
            name = feature.getBackground().getName();
            if (name != null) {

                return "Background: "+name;
            }
        }
        return "";
    }

    /*
     * gets Background Description
     */
    public String getFeatureBackgroundDescription(Feature feature){
        String description = "";
        if(feature.getBackground() != null) {
            description = feature.getBackground().getDescription();
            if (description != null) {
                return description;
            }
        }
        return description;
    }

    public String getFeatureBackgroundSteps(Feature feature){
        StringBuilder description = new StringBuilder();
        if(feature.getBackground()!=null) {
            List<Step> steps = feature.getBackground().getSteps();
            for (Step step : steps) {
                description.append(getStepName(step));
                description.append("\n");
                description.append(getStepArgumentData(step));
            }
        }
        return description.toString();
    }

    public String getFeatureBackgroundArgumentData(Feature feature){
        StringBuilder builder = new StringBuilder();
        if(feature.getBackground()!=null) {
            List<Step> backSteps = feature.getBackground().getSteps();
            for (Step step : backSteps) {
                builder.append(step.getKeyword() + " " + step.getText());
                builder.append("\n");
                builder.append(getStepArgumentData(step));
            }
        }
        return builder.toString();
    }



    /*
     * gets Test Scenario Name for a sceanrio / scenario out line
     */
    public String getTestScenarioName(ScenarioDefinition definition){
        String sceName = definition.getName();
        if(sceName == null) {
            return "";
        }
        return sceName;
    }

    public String getTestScenarioDescription(ScenarioDefinition definition){
        String sceName = definition.getDescription();
        if(sceName == null) {
            return "";
        }
        return sceName;
    }


    /*
   * gets Test Scenario Name for a sceanrio / scenario out line
   */
    public String getTestScenarioOutLineExamples(ScenarioDefinition definition){
        StringBuilder builder = new StringBuilder();
        if(definition.getKeyword().toLowerCase().startsWith("scenario outline")){
            List<Examples> examples = ((ScenarioOutline) definition).getExamples();
            builder.append("Examples:\n");
            builder.append(getExampleHeader(examples.get(0)));
            for(Examples example: examples){
                builder.append(getExampleData(example));
            }
        }
        return builder.toString();
    }

    /*
     * gets the Example Header
     */
    String getExampleHeader(Examples examples){
        StringBuilder builder = new StringBuilder();
        List<TableCell> cells = examples.getTableHeader().getCells();
        builder.append("|");
        for(TableCell cell: cells){
            builder.append(cell.getValue());
            builder.append("|");
        }
        builder.append("\n");
        return builder.toString();
    }

    /*
     * gets the Example Data
     */
    String getExampleData(Examples examples){
        StringBuilder builder = new StringBuilder();
        List<TableRow> rows = examples.getTableBody();
        for(TableRow row: rows){
            builder.append("|");
            for(TableCell cell: row.getCells()){
                builder.append(cell.getValue());
                builder.append("|");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    /*
    * gets Test Scenario Name for a sceanrio / scenario out line
    */
    public String getTestScenarioSteps(ScenarioDefinition definition){
        List<Step> steps = definition.getSteps();
        StringBuilder description = new StringBuilder();
        for(Step step: steps){
            description.append(getStepName(step));
            description.append("\n");
            description.append(getStepArgumentData(step));
        }
        return description.toString();
    }

    /*
     * gets Step Name
     */
    String getStepName(Step step){
        return step.getKeyword() +" "+ step.getText();
    }

    /*
     * gets Step Argument Data
     */
    String getStepArgumentData(Step step){
        StringBuilder stepArgument = new StringBuilder();
        if(step.getArgument() != null) {
            List<TableRow> rows = ((DataTable) step.getArgument()).getRows();
            if (rows != null && rows.size() > 0) {
                for (TableRow row : rows) {
                    stepArgument.append("|");
                    for (TableCell cell : row.getCells()) {
                        stepArgument.append(cell.getValue());
                        stepArgument.append("|");
                    }
                    stepArgument.append("\n");
                }
            }
        }
        return stepArgument.toString();
    }

    /*
     * gets Labels.
     */
    public List<String> getTags(ScenarioDefinition definition){
        List<String> labels = new ArrayList<>();
        try {
            List<Tag> tags = definition.getTags();
            for(Tag tag: tags){
                labels.add(tag.getName().replaceFirst("@",""));
            }
        }
        catch (Exception ex){

        }
        return labels;
    }

}
