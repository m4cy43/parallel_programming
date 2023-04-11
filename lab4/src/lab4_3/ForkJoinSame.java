package lab4_3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.RecursiveTask;

public class ForkJoinSame extends RecursiveTask<HashSet<String>> {
    private final Path filePath;
    private final int limit = 5;
    private final int startLine;
    private final int endLine;
    private final static String regSpacePunct = "[\\s\\p{Punct}]+";

    public ForkJoinSame(Path path) {
        this.filePath = path;
        this.startLine = 0;
        try (var lines = Files.lines(path)) {
            this.endLine = (int) lines.count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ForkJoinSame(Path path, int start, int end) {
        this.filePath = path;
        this.startLine = start;
        this.endLine = end;
    }

    private Collection<ForkJoinSame> createSubtasks() {
        int mid = (startLine + endLine) / 2;
        List<ForkJoinSame> subtasks = new ArrayList<>();
        var left = new ForkJoinSame(filePath, startLine, mid);
        var right = new ForkJoinSame(filePath, mid, endLine);
        subtasks.add(left);
        subtasks.add(right);
        return subtasks;
    }

    @Override
    protected HashSet<String> compute() {
        int lineNum = endLine - startLine;
        if (lineNum > limit) {
            Collection<ForkJoinSame> subTasks = createSubtasks();
            invokeAll(subTasks);
            HashSet<String> words = new HashSet<>();
            for (ForkJoinSame subTask : subTasks) {
                words.addAll(subTask.join());
            }
            return words;
        } else {
            HashSet<String> words = new HashSet<>();
            try {
                var lines = Files.readAllLines(filePath);
                for (int i = startLine; i < endLine && i < lines.size(); i++) {
                    var line = lines.get(i);
                    var wordsInLine = line.split(regSpacePunct);
                    words.addAll(Arrays.asList(wordsInLine));
                }
                return words;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
