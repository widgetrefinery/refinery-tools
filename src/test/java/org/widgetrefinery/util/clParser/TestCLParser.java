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

import junit.framework.TestCase;

/**
 * Since: 3/4/12 6:29 PM
 */
public class TestCLParser extends TestCase {
    public void testDuplicateArgumentNames() {
        try {
            new CLParser(new String[0],
                         new Argument("f|foo", new BooleanArgumentType()),
                         new Argument("b|bar", new BooleanArgumentType()),
                         new Argument("foo", new StringArgumentType()));
            assertTrue("constructor did not throw exception", false);
        } catch (Exception e) {
            assertEquals("duplicate argument name (foo)", e.getMessage());
        }
    }

    public void testIllegalArgument() {
        try {
            String[] inputs = new String[]{"-ba"};
            new CLParser(inputs, new Argument("b", new BooleanArgumentType()));
            assertTrue("constructor did not throw exception", false);
        } catch (Exception e) {
            assertEquals("invalid argument (-a)", e.getMessage());
        }

        try {
            String[] inputs = new String[]{"--tasukete"};
            new CLParser(inputs, new Argument("help", new BooleanArgumentType()));
            assertTrue("constructor did not throw exception", false);
        } catch (Exception e) {
            assertEquals("invalid argument (--tasukete)", e.getMessage());
        }
    }

    public void testMissingArgumentValue() {
        try {
            String[] inputs = new String[]{"-ba"};
            new CLParser(inputs,
                         new Argument("a", new StringArgumentType()),
                         new Argument("b", new BooleanArgumentType()));
            assertTrue("constructor did not throw exception", false);
        } catch (Exception e) {
            assertEquals("missing value for argument -a", e.getMessage());
        }

        try {
            String[] inputs = new String[]{"--foo"};
            new CLParser(inputs, new Argument("foo", new StringArgumentType()));
            assertTrue("constructor did not throw exception", false);
        } catch (Exception e) {
            assertEquals("missing value for argument --foo", e.getMessage());
        }
    }

    public void testUnexpectedArgumentValue() {
        try {
            String[] inputs = new String[]{"--foo=bar"};
            new CLParser(inputs, new Argument("foo", new BooleanArgumentType()));
            assertTrue("constructor did not throw exception", false);
        } catch (Exception e) {
            assertEquals("unexpected value for argument --foo (bar)", e.getMessage());
        }
    }

    public void testHasArguments() {
        String[] inputs;
        Argument argument = new Argument("f|foo", new BooleanArgumentType());
        CLParser clParser;

        inputs = "hello world".split(" ");
        clParser = new CLParser(inputs, argument);
        assertEquals(false, clParser.hasArguments());

        inputs = "hello -f world".split(" ");
        clParser = new CLParser(inputs, argument);
        assertEquals(true, clParser.hasArguments());

        inputs = "hello --foo world".split(" ");
        clParser = new CLParser(inputs, argument);
        assertEquals(true, clParser.hasArguments());
    }

    public void testParser() {
        String[] inputs = "hello -f --bar=world".split(" ");
        CLParser clParser = new CLParser(inputs,
                                         new Argument("f|foo", new BooleanArgumentType()),
                                         new Argument("b|bar", new StringArgumentType()),
                                         new Argument("a|about", new BooleanArgumentType()));

        assertEquals(Boolean.TRUE, clParser.getValue("f"));
        assertEquals(Boolean.TRUE, clParser.getValue("foo"));
        assertEquals("world", clParser.getValue("b"));
        assertEquals("world", clParser.getValue("bar"));
        assertEquals(1, clParser.getLeftovers().size());
        assertEquals("hello", clParser.getLeftovers().get(0));

        try {
            clParser.getValue("fail");
            assertTrue("getValue() did not throw an exception", false);
        } catch (Exception e) {
            assertEquals("no such argument (fail)", e.getMessage());
        }
    }

    public void testUsageMessage() {
        CLParser clParser = new CLParser(new String[0],
                                         new Argument("h|help", new BooleanArgumentType(), "custom\nargument\n\tdescription"),
                                         new Argument("f|foo|foobar", new BooleanArgumentType()));

        String expectedUsage = "USAGE:\n" +
                               "  java java.lang.String [options] ...\n";
        String expectedDescription = "";
        String expectedOptions = "\nOPTIONS:\n" +
                                 "  -f\n" +
                                 "  --foo\n" +
                                 "  --foobar\n" +
                                 "    a boolean flag\n" +
                                 "  -h\n" +
                                 "  --help\n" +
                                 "    custom\n" +
                                 "    argument\n" +
                                 "        description\n";
        String msg = clParser.getHelpMessage(String.class, null);
        assertEquals(expectedUsage + expectedDescription + expectedOptions, msg);

        expectedDescription = "\nDESCRIPTION:\n" +
                              "  custom\n" +
                              "  application\n" +
                              "      description\n";
        msg = clParser.getHelpMessage(String.class, "custom\napplication\n\tdescription");
        assertEquals(expectedUsage + expectedDescription + expectedOptions, msg);
    }
}
