package sample;

import gherkin.ast.Feature;
import gherkin.ast.ScenarioDefinition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import net.rcarz.jiraclient.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.controlsfx.dialog.*;
import org.json.simple.JSONObject;
import sample.com.app.cucumber.CucumberParser;
import sample.com.app.cucumber.GherkinParser;
import sample.com.app.jira.ZypherClient;
import sample.com.app.utils.JsonUtil;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Controller {

    @FXML
    public PasswordField password;
    @FXML
    public Button loginBtn;
    @FXML
    public TextField userName;
    @FXML
    public TextField url;
    @FXML
    public ComboBox projects;
    @FXML
    public Label message;
    @FXML
    public Button fileChooser;
    @FXML
    public ComboBox stories;

    private ZypherClient client;
    private String featureFilePath;
    private GherkinParser gherkinParser = new GherkinParser();

   @FXML
    public void login(ActionEvent event){
        try {
            if(password.getText().equals("")||userName.getText().equals("")||url.getText().equals("")){
                Dialogs.create()
                        .title("Log in")
                        .message("Please provide username and password for login")
                        .showError();
            }
            else {
                Dialogs.create()
                        .title("Log in")
                        .masthead("Log-in is in progress")
                        .showWorkerProgress(loginService);
                if (loginService.getState().equals(Worker.State.SUCCEEDED)) {
                    loginService.restart();
                } else if (loginService.getState().equals(Worker.State.READY)) {
                    loginService.start();
                } else if (loginService.getState().equals(Worker.State.FAILED) ||
                        loginService.getState().equals(Worker.State.CANCELLED)) {
                    loginService.reset();
                    loginService.start();
                }
            }
        }
        catch (Exception ex){
            Dialogs.create()
                    .title("Log in")
                    .message("Login Failed!")
                    .showExceptionInNewWindow(ex);
        }
    }

    @FXML
    public void chooseFeatureFIle(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        featureFilePath  =fileChooser.showOpenDialog(null).getPath();
        if(!featureFilePath.contains(".feature")){
            Dialogs.create()
                    .title("Feature File")
                    .message("Please choose feature file only")
                    .showError();
        }
        else {
            message.setText("Selected File Path:" + featureFilePath);
        }
    }

    @FXML
    public void importScenarios(ActionEvent event) throws JiraException {
        if ((projects.getItems().size() == 0) ||
                (featureFilePath == null || featureFilePath == "")) {
            Dialogs.create()
                    .title("Import Scenarios")
                    .message("Please load the projects into DropDown by logging in\n" +
                            "and Select the feature file")
                    .showWarning();
        } else {
            if(validFeatureFormat(featureFilePath)) {
                Dialogs.create()
                        .title("Progress Dialog")
                        .masthead("Importing Scenarios")
                        .showWorkerProgress(service);
                if (service.getState().equals(Worker.State.READY)) {
                    service.start();
                } else if (service.getState().equals(Worker.State.SUCCEEDED)) {
                    service.restart();
                } else if (service.getState().equals(Worker.State.CANCELLED) ||
                        service.getState().equals(Worker.State.FAILED)) {
                    service.reset();
                    service.start();
                } else if (service.getState().equals(Worker.State.RUNNING)) {
                    Dialogs.create()
                            .title("Import Service")
                            .masthead("Import Service is running in the background.")
                            .message("Cancelling the current running process")
                            .showInformation();
                    service.cancel();
                }
            }
        }
    }

    /*
     * validates the feature file and throws error if the file is incorrect
     */
    boolean validFeatureFormat(String filePath){
        try {
            Feature feature = gherkinParser.getParsedFeature(featureFilePath);
        }
        catch (Exception ex){
            Dialogs.create()
                    .title("Feature File Parsing Exception")
                    .message(ex.getMessage())
                    .showError();
            return false;
        }
        return true;
    }

    File writeToFile(String filePath, String feature){
        try{
            String[] lines = feature.split("\n");
            File file = new File(filePath);
            FileUtils.writeLines(file, Arrays.asList(lines));
            return file;
        }
        catch(Exception ex){
            return null;
        }
    }

    Service<Void> loginService = new Service<Void>(){
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    client = new ZypherClient(userName.getText(),
                            password.getText(), url.getText());
                    List<String> projectNames = client.getProjectNames(message);
                    ObservableList<String> options =
                            FXCollections.observableArrayList(projectNames);
                    projects.setItems(options);
                    return null;
                }
            };
        }
    };

    Service<Void> service = new Service<Void>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call()
                        throws Exception {
                    updateMessage("Tests Scenarios Import Started. . .");
                    updateMessage("Importing Tests Scenarios . . .");
                    String jsonFeature = gherkinParser.getFeatureJson(featureFilePath);
                    CucumberParser parser = new CucumberParser();
                    GherkinParser Gparser = new GherkinParser();
                    Feature feature = Gparser.getParsedFeature(featureFilePath);
                    String backGroundName = JSONObject.escape(parser.getFeatureBackGroundName(feature));
                    String backGroundDescription = JSONObject.escape(parser.getFeatureBackgroundDescription(feature));
                    String backGroundSteps = parser.getFeatureBackgroundSteps(feature);
                    List<ScenarioDefinition> definitions = feature.getScenarioDefinitions();
                    int starter = 0;
                    int defCount = definitions.size();
                    updateProgress(0, defCount);
                    for (ScenarioDefinition definition : definitions) {
                        String issueKey = "";
                        boolean testUpdated = false;
                        boolean testCreated = false;
                        String testSceKeyWord = definition.getKeyword();
                        String testScenarioName = parser.getTestScenarioName(definition);
                        String testScenarioDescription = parser.getTestScenarioDescription(definition);
                        String testScenarioSteps = parser.getTestScenarioSteps(definition);
                        String testScenarioExamples = parser.getTestScenarioOutLineExamples(definition);
                        List<String> labels = parser.getTags(definition);
                        String description = backGroundName + "\n"+ backGroundDescription + "\n"+ backGroundSteps+ "\n"+
                                testSceKeyWord + ": " +testScenarioName +"\n"
                                +testScenarioDescription+"\n" +testScenarioSteps+"\n" +testScenarioExamples;
                        if(labels.stream().anyMatch(x->x.contains("JIRA="))) {
                            issueKey = CollectionUtils.find(labels, item->item.toString()
                                    .contains("JIRA=")).toString().split("=")[1];
                            labels.remove("JIRA="+issueKey);
                            testUpdated = client.updateTest(issueKey, testScenarioName,
                                    description, labels);
                            testUpdated = true;
                        }
                        else {
                            issueKey = client.createTest(projects.getValue().toString(), testScenarioName,
                                    description, labels);
                            JsonUtil util = new JsonUtil();
                            util.load(jsonFeature);
                            jsonFeature = util.updateScenarioTagNode(issueKey, testScenarioName);
                            testCreated = true;
                        }
                        updateProgress(starter + 1, defCount);
                        updateMessage("Imported " + (starter + 1) + " Scenario!");
                        starter = starter+1;
                    }
                    String featureFile = gherkinParser.getFeatureFromJson(jsonFeature);
                    File file = writeToFile(featureFilePath, featureFile);
                    updateMessage("Imported all Scenarios.");
                    return null;
                }
            };
        }
    };
}