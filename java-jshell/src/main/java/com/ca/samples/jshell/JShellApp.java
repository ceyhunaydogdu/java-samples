package com.ca.samples.jshell;


import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jdk.jshell.*;
import jdk.jshell.Snippet.Status;

class JShellApp {

    public static void main(String[] args) {
        Console console = System.console();
        try (JShell js = JShell.create()) {
            do {
                System.out.print("Enter some Java code: ");
                String input = console.readLine();
                if (input == null) {
                    break;
                }
                List<SnippetEvent> events = analyzeCode(js,input)   
                                                .stream().map(js::eval).flatMap(List::stream)
                                                .collect(Collectors.toList());
                for (SnippetEvent e : events) {
                    StringBuilder sb = new StringBuilder();
                    if (e.causeSnippet() == null) {
                        //  We have a snippet creation event
                        switch (e.status()) {
                            case VALID:
                                sb.append("Successful ");
                                break;
                            case RECOVERABLE_DEFINED:
                                sb.append("With unresolved references ");
                                break;
                            case RECOVERABLE_NOT_DEFINED:
                                sb.append("Possibly reparable, failed  ");
                                break;
                            case REJECTED:
                                sb.append("Failed ");
                                break;
                        }
                        if (e.previousStatus() == Status.NONEXISTENT) {
                            sb.append("addition");
                        } else {
                            sb.append("modification");
                        }
                        sb.append(" of ");
                        sb.append(e.snippet().source());
                        System.out.println(sb);
                        if (e.value() != null) {
                            System.out.printf("Value is: %s\n", e.value());
                        }
                        System.out.flush();
                    }
                }
            } while (true);
        }
        System.out.println("\nGoodbye");
    }

    public static List<String> analyzeCode(JShell js, String code) {
        SourceCodeAnalysis sca = js.sourceCodeAnalysis();
        List<String> snippets = new ArrayList<>();
        do {
            SourceCodeAnalysis.CompletionInfo info = sca.analyzeCompletion(code);
            snippets.add(info.source());
            code = info.remaining();
        } while (code.length() > 0);
        return snippets;
    }
}
