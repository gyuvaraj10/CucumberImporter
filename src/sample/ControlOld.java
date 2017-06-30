package sample;

/**
 * Created by ctl-user on 25/1/2016.
 */
public class ControlOld {
//    @FXML
//    public PasswordField password;
//    @FXML
//    public Button loginBtn;
//    @FXML
//    public TextField userName;
//    @FXML
//    public ComboBox projects;
//    @FXML
//    public ProgressBar progressBar;
//    @FXML
//    public Label message;
//    @FXML
//    public Button fileChooser;
//
//    private ZypherClient client;
//
//    private String featureFilePath;
//
//    private GherkinParser gherkinParser = new GherkinParser();
//
//    @FXML
//    public void login(ActionEvent event){
//        try {
//            client = new ZypherClient(userName.getText(),
//                    password.getText(), "https://anatwine.atlassian.net/");
//            List<String> projectNames = client.getProjectNames(message);
//            ObservableList<String> options =
//                    FXCollections.observableArrayList(projectNames);
//            projects.setItems(options);
//            Dialogs.create()
//                    .title("Log in")
//                    .message("You have successfully logged in!")
//                    .showInformation();
//        }
//        catch (Exception ex){
//            Dialogs.create()
//                    .title("Log in")
//                    .message("Login Failed!")
//                    .showExceptionInNewWindow(ex);
//        }
//    }
//
//    @FXML
//    public void chooseFeatureFIle(ActionEvent event){
//        FileChooser fileChooser = new FileChooser();
//        featureFilePath  =fileChooser.showOpenDialog(null).getPath();
//        if(!featureFilePath.contains(".feature")){
//            Dialogs.create()
//                    .title("Feature File")
//                    .message("Please choose feature file only")
//                    .showError();
//        }
//        else {
//            message.setText("Selected File Path:" + featureFilePath);
//        }
//    }
//
//    @FXML
//    public void importScenarios(ActionEvent event) throws JiraException {
//        if((projects.getItems().size()==0) ||
//                (featureFilePath == null || featureFilePath == "")){
//            Dialogs.create()
//                    .title("Import Scenarios")
//                    .message("Please load the projects into DropDown by logging in\n" +
//                            "and Select the feature file")
//                    .showWarning();
//        }
//        else {
//            loginBtn.setDisable(true);
//            fileChooser.setDisable(true);
//            importTests();
//            Dialogs.create()
//                    .title("Import Sceanrios")
//                    .message("Scenarios are Imported to Zepher successfully!")
//                    .showInformation();
//        }
//        loginBtn.setDisable(false);
//        fileChooser.setDisable(false);
//    }
//
//    void importTests() throws JiraException{
//        String jsonFeature = gherkinParser.getFeatureJson(featureFilePath);
//        CucumberParser parser = new CucumberParser();
//        GherkinParser Gparser = new GherkinParser();
//        Feature feature = Gparser.getParsedFeature(featureFilePath);
//        String backGroundName = JSONObject.escape(parser.getFeatureBackGroundName(feature));
//        String backGroundDescription = JSONObject.escape(parser.getFeatureBackgroundDescription(feature));
//        String backGroundSteps = parser.getFeatureBackgroundSteps(feature);
//        List<ScenarioDefinition> definitions = feature.getScenarioDefinitions();
//        double progress = 0.0;
//        progressBar.progressProperty().unbind();
//        for (ScenarioDefinition definition : definitions) {
//            String issueKey = "";
//            boolean testUpdated = false;
//            boolean testCreated = false;
//            progressBar.setProgress(progress);
//            String testSceKeyWord = definition.getKeyword();
//            String testScenarioName = parser.getTestScenarioName(definition);
//            String testScenarioDescription = parser.getTestScenarioDescription(definition);
//            String testScenarioSteps = parser.getTestScenarioSteps(definition);
//            String testScenarioExamples = parser.getTestScenarioOutLineExamples(definition);
//            List<String> labels = parser.getTags(definition);
//            String description = backGroundName + "\n"+ backGroundDescription + "\n"+ backGroundSteps+ "\n"+
//                    testSceKeyWord + ": " +testScenarioName +"\n"
//                    +testScenarioDescription+"\n" +testScenarioSteps+"\n" +testScenarioExamples;
//            if(labels.stream().anyMatch(x->x.contains("JIRA="))) {
//                issueKey = CollectionUtils.find(labels, item->item.toString()
//                        .contains("JIRA=")).toString().split("=")[1];
//                labels.remove("JIRA="+issueKey);
//                testUpdated = client.updateTest(issueKey, testScenarioName,
//                        description, labels);
//                testUpdated = true;
//            }
//            else {
//                issueKey = client.createTest(projects.getValue().toString(), testScenarioName,
//                        description, labels);
//                JsonUtil util = new JsonUtil();
//                util.load(jsonFeature);
//                jsonFeature = util.updateScenarioTagNode(issueKey, testScenarioName);
//                testCreated = true;
//            }
//            progress = progress + 0.1;
//            System.out.println(issueKey);
//        }
//        String featureFile = gherkinParser.getFeatureFromJson(jsonFeature);
//        File file = writeToFile(featureFilePath, featureFile);
//        progressBar.setProgress(1.0);
//        System.out.println(file.getAbsolutePath());
//    }
//
//    File writeToFile(String filePath, String feature){
//        try{
//            String[] lines = feature.split("\n");
//            File file = new File(filePath);
//            FileUtils.writeLines(file, Arrays.asList(lines));
//            return file;
//        }
//        catch(Exception ex){
//            return null;
//        }
//    }
}
//        String jText = parser.getFeatureJson(featureFilePath);
//        parser.getFeatureFromJson(jText);