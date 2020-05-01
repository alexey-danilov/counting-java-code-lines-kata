package counter.service;

import static java.util.Arrays.stream;

/**
 * Counts lines of code in source files, excluding comments and empty lines.
 */
public class LocCounter {
    public static final String QUOTE_CONTENT_REGEX = "(?<=([\"']\\b))(?:(?=(\\\\?))\\2.)*?(?=\\1)";
    public static final String BLOCK_COMMENT_REGEX = "\\/\\*[\\s\\S]*?\\*\\/|([^:]|^)\\/\\/.*$";
    public static final String LINE_COMMENT_REGEX = "(//.*)";
    public static final String SOURCE_CODE_COUNTER_REGEX = QUOTE_CONTENT_REGEX + '|' + BLOCK_COMMENT_REGEX + '|' + LINE_COMMENT_REGEX;

    long countSourceCodeLines(String sourceFile) { // package access solely for testing out
        /* IMPLEMENTATION DETAILS:
         1). First order of business is not make sure that something that looks like comment, but is in fact is a quoted text
         does not mess with resulting comment finding regex. This could have been achieved by
         making regex a bit more complex (i.e. looking for comments only in non-quotes), but instead I decided to take a
         simpler approach - purely for better readability. So, first we remove all contents of quotes -> this is _assumed_ not to affect
         resulting code lines (BUT as a side effect this method cannot be used just to strip code of comments (and show it to user, for
         example). Anyway, presentation of such a code was not an intention; if it is required,
         then we will have to resort to using more complex regex
         2). Just remove all the comments in a resulting file (which by this time will
         have empty quotes). It is important there to test that regex works robustly,
         for example without any catastrophic backtracking, which will result in
         stackoverflow errors.
         3). Remove empty lines, which were left after removing comments. This is
         done not via regex (although this is also an option)

         NOTE: I've started writing my own regexes for quote content and block comment lookup, but then decided to save time and googled
          for tried and tested solutions.
        */

        String[] sourceLinesWithoutComments = sourceFile.replaceAll(SOURCE_CODE_COUNTER_REGEX, "").split("\n");
        return stream(sourceLinesWithoutComments)
                .filter(line -> !line.trim().isEmpty()) // remove empty lines
                .count();
    }
}