/*
 * Copyright (C) 2012  Widget Refinery
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.widgetrefinery.util.clParser;

import org.widgetrefinery.util.BadUserInputException;
import org.widgetrefinery.util.StringUtil;
import org.widgetrefinery.util.lang.Translator;
import org.widgetrefinery.util.lang.UtilTranslationKey;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.*;

/**
 * Utility class for parsing GNU-style command line arguments. It supports
 * short arguments like <code>"-a -b hello -cd"</code> as well as long
 * arguments like <code>"--help --input=file"</code>. It also provides support
 * for displaying usage info that is usually shown with -h or --help.
 *
 * @since 2/20/12 8:19 PM
 */
public class CLParser {
    private static final int CONSOLE_WIDTH = 80;

    private final Map<String, Argument> arguments;
    private final List<String>          leftovers;
    private       boolean               hasArguments;

    /**
     * Creates a new instance of CLParser and immediately parses the input
     * arguments.
     *
     * @param inputs      input arguments to parse
     * @param definitions list of arguments to parse with
     * @throws IllegalArgumentException if the constructor was called with invalid values
     * @throws BadUserInputException    if there is a problem with the input arguments
     */
    public CLParser(final String[] inputs, final Argument... definitions) throws IllegalArgumentException, BadUserInputException {
        this.arguments = new HashMap<String, Argument>();
        for (Argument definition : definitions) {
            for (String name : definition.getNames()) {
                if (this.arguments.containsKey(name)) {
                    throw new IllegalArgumentException("duplicate argument name (" + name + ')');
                }
                this.arguments.put(name, definition);
            }
        }
        this.leftovers = new ArrayList<String>();

        Iterator<String> itr = Arrays.asList(inputs).iterator();
        while (itr.hasNext()) {
            String input = itr.next();
            if (input.startsWith("--")) {
                if (2 < input.length()) {
                    parseLongArgument(input);
                } else {
                    this.leftovers.add(input);
                }
            } else if (input.startsWith("-") && 1 < input.length()) {
                for (int ndx = 1; ndx < input.length(); ndx++) {
                    String name = input.substring(ndx, ndx + 1);
                    parseShortArgument(name, itr);
                }
            } else {
                this.leftovers.add(input);
            }
        }
    }

    protected void parseLongArgument(final String longArgument) throws BadUserInputException {
        String[] keyValuePair = longArgument.split("=", 2);
        String rawName = keyValuePair[0];
        String name = rawName.substring(2);
        Argument argument = this.arguments.get(name);
        if (null == argument) {
            throw new BadUserInputException(Translator.get(UtilTranslationKey.CL_ERROR_NO_SUCH_SWITCH, rawName), rawName);
        } else if (argument.isConsumesValue()) {
            if (2 == keyValuePair.length) {
                String value = keyValuePair[1];
                argument.parse(rawName, value);
            } else {
                throw new BadUserInputException(Translator.get(UtilTranslationKey.CL_ERROR_SWITCH_MISSING_VALUE, rawName));
            }
        } else if (2 == keyValuePair.length) {
            throw new BadUserInputException(Translator.get(UtilTranslationKey.CL_ERROR_UNEXPECTED_SWITCH_VALUE, rawName, keyValuePair[1]), keyValuePair[1]);
        } else {
            argument.parse(rawName, null);
        }
        this.hasArguments = true;
    }

    protected void parseShortArgument(final String name, final Iterator<String> itr) throws BadUserInputException {
        String rawName = "-" + name;
        Argument argument = this.arguments.get(name);
        if (null == argument) {
            throw new BadUserInputException(Translator.get(UtilTranslationKey.CL_ERROR_NO_SUCH_SWITCH, rawName), rawName);
        } else if (argument.isConsumesValue()) {
            if (itr.hasNext()) {
                String value = itr.next();
                argument.parse(rawName, value);
            } else {
                throw new BadUserInputException(Translator.get(UtilTranslationKey.CL_ERROR_SWITCH_MISSING_VALUE, rawName));
            }
        } else {
            argument.parse(rawName, null);
        }
        this.hasArguments = true;
    }

    /**
     * Returns true if the input contained at least one argument.
     *
     * @return true if the input contained at least one argument.
     */
    public boolean hasArguments() {
        return this.hasArguments;
    }

    /**
     * Returns the parsed value for the given argument. The argument name can
     * be any valid name for the argument.
     *
     * @param name name of argument to look up
     * @param <T>  parsed value type
     * @return parsed value
     * @throws IllegalArgumentException if the argument name is invalid
     */
    @SuppressWarnings("unchecked")
    public <T> T getValue(final String name) throws IllegalArgumentException {
        Argument argument = this.arguments.get(name);
        if (null == argument) {
            throw new IllegalArgumentException("no such argument (" + name + ')');
        }
        return (T) argument.getValue();
    }

    /**
     * Returns any arguments that were not parsed.
     *
     * @return leftover input data
     */
    public List<String> getLeftovers() {
        return this.leftovers;
    }

    /**
     * Constructs a help message describing the application. The application
     * description comes from {@link org.widgetrefinery.util.lang.Translator}
     * while the argument descriptions come from the arguments.
     *
     * @param mainClass entry point into your application
     * @return help message
     */
    public String getHelpMessage(final Class mainClass) {
        StringBuilder sb = new StringBuilder();

        String command = getCommand(mainClass);
        sb.append(StringUtil.wordWrap(Translator.get(UtilTranslationKey.CL_HELP_USAGE, command), CONSOLE_WIDTH, "", "  "));
        sb.append("\n\n");

        String description = Translator.get(UtilTranslationKey.CL_HELP_DESCRIPTION);
        if (StringUtil.isNotBlank(description)) {
            sb.append(StringUtil.wordWrap(description, CONSOLE_WIDTH, "", "  "));
            sb.append("\n\n");
        }

        sb.append(StringUtil.wordWrap(Translator.get(UtilTranslationKey.CL_HELP_OPTIONS), CONSOLE_WIDTH));
        sb.append('\n');
        String valueText = Translator.get(UtilTranslationKey.CL_HELP_OPTIONS_SWITCH_VALUE);
        String longSwitchValueText = "=[" + valueText + "]";
        String shortSwitchValueText = " [" + valueText + "]";
        Set<Argument> arguments = new TreeSet<Argument>(this.arguments.values());
        for (Argument argument : arguments) {
            for (String argumentName : argument.getNames()) {
                sb.append("  ");
                if (1 < argumentName.length()) {
                    sb.append("--").append(argumentName);
                    if (argument.isConsumesValue()) {
                        sb.append(longSwitchValueText);
                    }
                } else {
                    sb.append('-').append(argumentName);
                    if (argument.isConsumesValue()) {
                        sb.append(shortSwitchValueText);
                    }
                }
                sb.append("\n");
            }
            String argumentDescription = StringUtil.wordWrap(argument.getDescription().trim(), CONSOLE_WIDTH, "    ", "    ");
            sb.append(argumentDescription).append("\n");
        }

        return sb.toString();
    }

    protected String getCommand(final Class mainClass) {
        try {
            URI mainURI = mainClass.getProtectionDomain().getCodeSource().getLocation().toURI();
            File file = new File(mainURI);
            return "java -jar " + file.getName();
        } catch (Exception e) {
            return "java " + mainClass.getName();
        }
    }

    /**
     * Loads the license file and writes it to the given output stream. It
     * expects to find the license file under the "COPYING" filename somewhere
     * in the classpath. If the license file is not found, an error message is
     * written to the output stream indicating this.
     *
     * @param outputStream output stream to write the license to
     * @throws IOException if an IO error occurs
     */
    public void getLicense(final OutputStream outputStream) throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream("COPYING");
        if (null != input) {
            try {
                byte[] buffer = new byte[1024];
                for (int bytesRead = input.read(buffer); 0 < bytesRead; bytesRead = input.read(buffer)) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            } finally {
                input.close();
            }
        } else {
            byte[] msg = StringUtil.wordWrap(Translator.get(UtilTranslationKey.CL_ERROR_MISSING_LICENSE), CONSOLE_WIDTH).getBytes();
            outputStream.write(msg);
            outputStream.flush();
        }
    }
}
