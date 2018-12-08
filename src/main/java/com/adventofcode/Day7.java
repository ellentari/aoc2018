package com.adventofcode;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.rangeClosed;

class Day7 {

    String solvePart1(List<String> input) {
        JobGraph remainingJobs = initJobGraph(input);
        JobQueue jobQueue = new JobQueue(remainingJobs.getNext());
        java.util.List<String> completedJobs = new ArrayList<>();

        while (jobQueue.hasMore()) {
            String done = jobQueue.remove();

            completedJobs.add(done);

            remainingJobs.remove(done);
            remainingJobs.getNext().forEach(jobQueue::add);
        }

        return String.join("", completedJobs);
    }

    int solvePart2(int workersCount, int baseJobTime, List<String> input) {
        PriorityQueue<Integer> workerPool = initWorkerPool(workersCount);

        JobGraph remainingJobs = initJobGraph(input);
        JobQueue jobQueue = new JobQueue(remainingJobs.getNext());

        PriorityQueue<JobInProgress> inProgress = new PriorityQueue<>(comparing(JobInProgress::completionTime));

        int[] time = {0};

        while (!remainingJobs.isEmpty()) {
            getFinished(inProgress).ifPresent(finished -> {

                remainingJobs.remove(finished.name);
                remainingJobs.getNext().forEach(jobQueue::add);

                workerPool.add(finished.workerId);

                time[0] = finished.completionTime;

                System.out.println(time[0] + ": worker " + finished.workerId + " finished processing job " + finished.name);
            });

            while (jobQueue.hasMore() && !workerPool.isEmpty()) {
                getAvailableWorker(workerPool).ifPresent(worker -> {
                    String jobToBeDone = jobQueue.remove();
                    int completionTime = time[0] + baseJobTime + getRequiredTimeFor(jobToBeDone);

                    inProgress.add(new JobInProgress(jobToBeDone, completionTime, worker));

                    System.out.println(time[0] + ": worker " + worker + " started processing job " + jobToBeDone);
                });
            }
        }

        return time[0];
    }

    private PriorityQueue<Integer> initWorkerPool(int workersCount) {
        return rangeClosed(1, workersCount).boxed().collect(toCollection(PriorityQueue::new));
    }

    private Optional<Integer> getAvailableWorker(PriorityQueue<Integer> pool) {
        return Optional.ofNullable(pool.poll());
    }

    private Optional<JobInProgress> getFinished(PriorityQueue<JobInProgress> inProgress) {
        return Optional.ofNullable(inProgress.poll());
    }

    private int getRequiredTimeFor(String job) {
        return (job.charAt(0) - 'A') + 1;
    }

    private JobGraph initJobGraph(List<String> input) {
        JobGraph graph = new JobGraph();
        input.map(this::parseJobNames).forEach(t -> graph.addEdge(t._2, t._1));
        return graph;
    }

    private Tuple2<String, String> parseJobNames(String input) {
        String[] split = input.split(" ");
        return Tuple.of(split[1], split[7]);
    }

    private static class JobInProgress {
        final String name;
        final int completionTime;
        final Integer workerId;

        JobInProgress(String name, int completionTime, Integer workerId) {
            this.name = name;
            this.completionTime = completionTime;
            this.workerId = workerId;
        }

        @Override
        public String toString() {
            return "JobInProgress{" +
                    "name='" + name + '\'' +
                    ", completionTime=" + completionTime +
                    '}';
        }

        int completionTime() {
            return completionTime;
        }
    }

    private static class JobGraph {
        private final Map<String, Set<String>> adjacencyList = new HashMap<>();

        void addEdge(String from, String to) {
            adjacencyList.computeIfAbsent(from, k -> new HashSet<>()).add(to);
            adjacencyList.computeIfAbsent(to, k -> new HashSet<>());
        }

        boolean isEmpty() {
            return adjacencyList.isEmpty();
        }

        java.util.List<String> getNext() {
            return adjacencyList.entrySet().stream()
                    .filter(e -> e.getValue().isEmpty())
                    .map(Map.Entry::getKey)
                    .collect(toList());
        }

        void remove(String node) {
            adjacencyList.remove(node);
            adjacencyList.values().forEach(l -> l.remove(node));
        }
    }

    private static class JobQueue {
        private final PriorityQueue<String> queue = new PriorityQueue<>();
        private final Set<String> processed = new HashSet<>();

        JobQueue(java.util.List<String> init) {
            queue.addAll(init);
            processed.addAll(init);
        }

        boolean hasMore() {
            return !queue.isEmpty();
        }

        String remove() {
            return queue.remove();
        }

        void add(String job) {
            if (!processed.contains(job)) {
                queue.add(job);
                processed.add(job);
            }
        }
    }
}
