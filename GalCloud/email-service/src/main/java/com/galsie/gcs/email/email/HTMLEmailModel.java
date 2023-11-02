package com.galsie.gcs.email.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * An HtmlEmail is a Model for an email that would be sent recurrently to the same/different address wuith different variables.
 *
 * The HtmlEmail#htmlText has variables in the form %variable_name_...%, these are stored as without the % in HtmlEmail#variables
 *
 *
 */
@AllArgsConstructor
@Getter
public class HTMLEmailModel {
    private static String BASE_PATH = "html";
    private static Pattern VARIABLE_PATTERN = Pattern.compile("%.*%");
    private String htmlText;
    private List<String> variables;


    public String getReplacedHTML(HashMap<String, String> variableReplacement){
        String htmlText = this.htmlText;
        for (String var: variableReplacement.keySet()){
            htmlText = htmlText.replaceAll("%" + var + "%", variableReplacement.get(var));
        }
        return htmlText;
    }


    /**
     *
     * @param filePath the path in HtmlEmail.BASE_PATH
     * @return
     * @throws NullPointerException if file couldn't open
     */
    public static HTMLEmailModel fromHtmlFile(String filePath) throws NullPointerException, IOException {
        var resourceUrl = HTMLEmailModel.class.getClassLoader().getResource(Paths.get(BASE_PATH, filePath).toString());
        assert resourceUrl != null;
        File file = new File(resourceUrl.getFile());
        assert file.exists();
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        return HTMLEmailModel.fromHtmlText(new String(data, StandardCharsets.UTF_8));
    }

    public static HTMLEmailModel fromHtmlText(String htmlText){
        List<String> variables = VARIABLE_PATTERN.matcher(htmlText).results().map(MatchResult::group).map((dt) -> dt.replaceAll("%", "")).collect(Collectors.toList());
        return new HTMLEmailModel(htmlText, variables);
    }
}
