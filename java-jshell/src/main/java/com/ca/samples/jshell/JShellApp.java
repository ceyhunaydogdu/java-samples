package com.ca.samples.jshell;


import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jdk.jshell.*;
import jdk.jshell.Snippet.Status;
import jdk.jshell.tool.JavaShellToolBuilder;

class JShellApp {

    public static void main(String[] args) throws Exception {

        JavaShellToolBuilder.builder().run();
        // Console console = System.console();
        // try (JShell js = JShell.create()) {
        //     loadDefaultPackages(js);
        //     do {
        //         System.out.print("Enter some Java code: ");
        //         String input = console.readLine();
        //         if (input == null) {
        //             break;
        //         }
        //         List<SnippetEvent> events = analyzeCode(js,input)   
        //                                         .stream().map(js::eval).flatMap(List::stream)
        //                                         .collect(Collectors.toList());
        //         for (SnippetEvent e : events) {
        //             StringBuilder sb = new StringBuilder();
        //             if (e.causeSnippet() == null) {
        //                 //  We have a snippet creation event
        //                 switch (e.status()) {
        //                     case VALID:
        //                         sb.append("Successful ");
        //                         break;
        //                     case RECOVERABLE_DEFINED:
        //                         sb.append("With unresolved references ");
        //                         break;
        //                     case RECOVERABLE_NOT_DEFINED:
        //                         sb.append("Possibly reparable, failed  ");
        //                         break;
        //                     case REJECTED:
        //                         sb.append("Failed ");
        //                         break;
        //                 }
        //                 if (e.previousStatus() == Status.NONEXISTENT) {
        //                     sb.append("addition");
        //                 } else {
        //                     sb.append("modification");
        //                 }
        //                 sb.append(" of ");
        //                 sb.append(e.snippet().source());
        //                 System.out.println(sb);
        //                 if (e.value() != null) {
        //                     System.out.printf("Value is: %s\n", e.value());
        //                 }
        //                 System.out.flush();
        //             }
        //         }
        //     } while (true);
        // }
        // System.out.println("\nGoodbye");
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

    public static void loadDefaultPackages(JShell js) {
        System.out.println("---> START : loading default packages");
        // String imports="import java.io.*; \n import java.math.*; \n import java.net.*; \n import java.nio.file.*; \n import java.util.*; \n import java.util.concurrent.*; import java.util.funtion.*; import java.util.prefs.*; import java.util.regex.*; import java.util.stream.*;";
        // String imports="import java.io.*; \n import java.math.*; \n import java.net.*; \n import java.nio.file.*; \n import java.util.*; \n import java.util.concurrent.*; \n import java.util.funtion.*; \n import java.util.prefs.*; \n import java.util.regex.*; \n import java.util.stream.*;";
        // analyzeCode(js,imports).stream().map(js::eval).flatMap(List::stream).forEach(e->System.out.println(e.value()));   
        // js.eval("import java.util.*;").stream().forEach(e->System.out.println(e.value()));
        js.eval("import java.util.stream.*;");
        js.imports().findAny().ifPresentOrElse(is-> {
                System.out.println(is.source()); 
            }, ()->System.out.println("No package imported"));
        System.out.println("---> END : loaded default packages");
            
    }
}
