package sample.com.app.cucumber;

import gherkin.formatter.Argument;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import java.util.Collections;
import java.util.List;

/**
 * Created by Yuvaraj Gunisetti on 23/01/16.
 * Purpose:
 */
public class JIRAMatchResultPair {
    public final Match match;
    public final Result result;

    public JIRAMatchResultPair(Match match, Result result) {
        this.match = match;
        this.result = result;
    }

    public List<Argument> getMatchArguments() {
        if(this.match!=null){
           return  this.match.getArguments();
        }
        else{
           return Collections.emptyList();
        }
//        return this.match != null?this.match.getArguments(): Collections.emptyList();
    }

    public String getMatchLocation() {
        return this.match != null?this.match.getLocation():null;
    }

    public String getResultStatus() {
        return this.result != null?this.result.getStatus():"skipped";
    }

    public boolean hasResultErrorMessage() {
        return this.result != null && this.result.getErrorMessage() != null;
    }

}
