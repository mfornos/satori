package satori.i18n;

import java.text.CharacterIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Based on hirondelle.web4j.ui.translate.TextFlow
 * 
 */
public class TextFlow {

    private static Pattern TRIMMED_TEXT = Pattern.compile("((?:\\S(?:.)*\\S)|(?:\\S))");

    public static String emit(CharSequence body) {

        return emit(Translation.getDefaultBaseName(), body);

    }

    public static String emit(String baseName, CharSequence body) {

        if (StringUtils.isBlank(body)) {
            return (String) body;
        }

        final StringBuilder result = new StringBuilder();
        final StringBuilder snippet = new StringBuilder();
        boolean isInsideTag = false;

        final CharacterIterator iterator = new SequenceCharacterIterator(body);
        char character = iterator.current();

        while (character != CharacterIterator.DONE) {
            if (character == '<') {
                doStartTag(baseName, result, snippet, character);
                isInsideTag = true;
            } else if (character == '>') {
                doEndTag(result, character);
                isInsideTag = false;
            } else {
                doRegularCharacter(result, snippet, isInsideTag, character);
            }
            character = iterator.next();
        }

        if (isNotBlank(snippet.toString())) {
            appendTranslation(baseName, snippet, result);
        }

        return result.toString();

    }

    /**
     * The snippet may contain leading or trailing white space, or control chars
     * (new lines), which must be preserved.
     */
    private static void appendTranslation(String baseName, StringBuilder snippet, StringBuilder result) {

        if (isNotBlank(snippet.toString())) {
            StringBuffer translatedSnippet = new StringBuffer();

            Matcher matcher = TRIMMED_TEXT.matcher(snippet.toString());
            while (matcher.find()) {
                matcher.appendReplacement(translatedSnippet, getReplacement(baseName, matcher));
            }
            matcher.appendTail(translatedSnippet);

            result.append(translatedSnippet);
        } else {
            result.append(snippet.toString());
        }

    }

    private static void doEndTag(StringBuilder result, char character) {

        result.append(character);

    }

    private static void doRegularCharacter(StringBuilder result, StringBuilder snippet, boolean isInsideTag,
            char character) {

        if (isInsideTag) {
            result.append(character);
        } else {
            snippet.append(character);
        }

    }

    private static void doStartTag(String baseName, StringBuilder result, StringBuilder snippet, char character) {

        if (isNotBlank(snippet.toString())) {
            appendTranslation(baseName, snippet, result);
        } else {
            // often contains just spaces and/or new lines, which are
            // just appended
            result.append(snippet.toString());
        }
        snippet.setLength(0);
        result.append(character);

    }

    private static String getReplacement(String baseName, Matcher matcher) {

        String baseText = matcher.group(0);
        return isNotBlank(baseText) ? Translation.translate(baseName, baseText) : baseText;

    }

}
