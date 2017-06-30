package sample.com.app.jira;

import javafx.scene.control.Label;
import net.rcarz.jiraclient.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuvaraj Gunisetti on 17/01/16.
 * Purpose:
 */
public class ZypherClient {

    private BasicCredentials creds;

    public JiraClient client;

    public ZypherClient(String userName, String password, String url){
        creds = new BasicCredentials(userName, password);
        client = new JiraClient(url, creds);
    }

    public List<String> getProjectNames(Label label) throws Exception{
        List<String> projctes = new ArrayList<>();
//        try {
            List<Project> projects = client.getProjects();
            for(Project project: projects){
                projctes.add(project.getName());
            }
//        }
//        catch(Exception ex){
//            Dialogs.create()
//                    .title("Log in")
//                    .message(ex.getMessage())
//                    .showInformation();
//        }
        return projctes;
    }

    public JiraClient getClient() {
        return client;
    }

    /*
     * creates the Test Case
     */
    public String createTest(String projectName, String sceName, String desc, List<String> labels){
        try {
            Issue issue = this.getClient().createIssue(projectName, "Test")
                    .field(Field.SUMMARY, sceName)
                    .field(Field.DESCRIPTION, desc)
                    .field(Field.LABELS, labels)
                    .execute();
            return issue.getKey();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    /*
     * updates the Test Case
     */
    public boolean updateTest(String testKey, String sceName, String desc, List<String> labels){
        try {
            this.getClient().getIssue(testKey).update()
                    .field(Field.SUMMARY, sceName)
                    .field(Field.DESCRIPTION, desc)
                    .field(Field.LABELS, labels)
                    .execute();
            return true;
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
}
