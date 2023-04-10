package lab4_3;

import lab4_1.ForkJoinCounter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinSame extends RecursiveTask<HashSet<String>> {
    private final HashSet<String> words;
    private final Path filePath;
    private final int limit = 100;
    private final int startLine;
    private final int endLine;
    private final static String regSpacePunct = "[\\s\\p{Punct}]+";

    public ForkJoinSame(Path path) {
        this.words = new HashSet<>();
        this.filePath = path;
        this.startLine = 0;
        try (var lines = Files.lines(path)) {
            this.endLine = (int) lines.count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ForkJoinSame(Path path, HashSet<String> wordSet, int start, int end) {
        this.words = wordSet;
        this.filePath = path;
        this.startLine = start;
        this.endLine = end;
    }

    private Collection<ForkJoinSame> createSubtasks() {
        int mid = (startLine + endLine) / 2;
        List<ForkJoinSame> subtasks = new ArrayList<>();
        subtasks.add(new ForkJoinSame(filePath, words, startLine, mid));
        subtasks.add(new ForkJoinSame(filePath, words, mid, endLine));
        return subtasks;
    }

    @Override
    protected HashSet<String> compute() {
        int lineNum = endLine - startLine;
        if (lineNum > limit) {
            Collection<ForkJoinSame> subTasks = createSubtasks();
            ForkJoinTask.invokeAll(subTasks);
        } else {
            try {
                var lines = Files.readAllLines(filePath);
                for (int i = startLine; i < endLine && i < lines.size(); i++) {
                    var line = lines.get(i);
                    var wordsInLine = line.split(regSpacePunct);
                    words.addAll(Arrays.asList(wordsInLine));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return this.words;
    }
}
