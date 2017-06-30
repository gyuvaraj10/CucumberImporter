package sample.com.app.cucumber;

import gherkin.formatter.model.Tag;
import gherkin.util.Mapper;

/**
 * Created by ctl-user on 24/1/2016.
 */
public class MyMapper implements Mapper<Tag, String> {

    @Override
    public String map(Tag tag) {
        return tag.getName();
    }
}
