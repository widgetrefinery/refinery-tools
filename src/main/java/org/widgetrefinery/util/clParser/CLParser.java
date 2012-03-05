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

import org.widgetrefinery.util.StringUtil;

import java.util.*;

/**
 * Since: 2/20/12 8:19 PM
 */
public class CLParser {
    private final Map<String, Argument> arguments;
    private final List<String>          leftovers;
    private       boolean               hasArguments;

    public CLParser(final String[] inputs, final Argument... definitions) {
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

    protected void parseLongArgument(final String longArgument) {
        String[] keyValuePair = longArgument.split("=", 2);
        String rawName = keyValuePair[0];
        String name = rawName.substring(2);
        Argument argument = this.arguments.get(name);
        if (null == argument) {
            throw new RuntimeException("invalid argument (" + rawName + ')');
        } else if (argument.isConsumesValue()) {
            if (2 == keyValuePair.length) {
                String value = keyValuePair[1];
                argument.parse(rawName, value);
            } else {
                throw new RuntimeException("missing value for argument " + rawName);
            }
        } else if (2 == keyValuePair.length) {
            throw new RuntimeException("unexpected value for argument " + rawName + " (" + keyValuePair[1] + ')');
        } else {
            argument.parse(rawName, "");
        }
        this.hasArguments = true;
    }

    protected void parseShortArgument(final String name, final Iterator<String> itr) {
        String rawName = "-" + name;
        Argument argument = this.arguments.get(name);
        if (null == argument) {
            throw new RuntimeException("invalid argument (" + rawName + ')');
        } else if (argument.isConsumesValue()) {
            if (itr.hasNext()) {
                String value = itr.next();
                argument.parse(rawName, value);
            } else {
                throw new RuntimeException("missing value for argument " + rawName);
            }
        } else {
            argument.parse(rawName, null);
        }
        this.hasArguments = true;
    }

    public boolean hasArguments() {
        return this.hasArguments;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(final String rawName) {
        Argument argument = this.arguments.get(rawName);
        if (null == argument) {
            throw new IllegalArgumentException("no such argument (" + rawName + ')');
        }
        return (T) argument.getValue();
    }

    public List<String> getLeftovers() {
        return this.leftovers;
    }

    public String getHelpMessage(final Class mainClass, final String description) {
        StringBuilder sb = new StringBuilder();

        sb.append("USAGE:\n  java ").append(mainClass.getName()).append(" [options] ...\n");

        if (StringUtil.isNotBlank(description)) {
            String formattedDescription = description.trim().replaceAll("\n", "\n  ").replaceAll("\t", "    ");
            sb.append("\nDESCRIPTION:\n  ").append(formattedDescription).append("\n");
        }

        sb.append("\nOPTIONS:\n");
        Set<Argument> arguments = new TreeSet<Argument>(this.arguments.values());
        for (Argument argument : arguments) {
            for (String argumentName : argument.getNames()) {
                sb.append("  ");
                if (1 < argumentName.length()) {
                    sb.append("--").append(argumentName);
                    if (argument.isConsumesValue()) {
                        sb.append("=[value]");
                    }
                } else {
                    sb.append('-').append(argumentName);
                    if (argument.isConsumesValue()) {
                        sb.append(" [value]");
                    }
                }
                sb.append("\n");
            }
            String argumentDescription = argument.getDescription().replaceAll("\n", "\n    ").replaceAll("\t", "    ");
            sb.append("    ").append(argumentDescription).append("\n");
        }

        return sb.toString();
    }
}
