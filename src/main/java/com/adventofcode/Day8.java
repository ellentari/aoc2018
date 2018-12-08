package com.adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;

class Day8 {

    int solvePart1(String input) {
        return buildTree(input).metadataSum();
    }

    int solvePart2(String input) {
        return buildTree(input).value();
    }

    private Node buildTree(String input) {
        return buildTree(toIntArray(input));
    }

    private Node buildTree(int[] array) {
        List<Node> children = new ArrayList<>();
        buildTree(children, array, 0);
        return children.get(0);
    }

    private int buildTree(List<Node> parentChildren, int[] array, int index) {
        NodeInfo nodeInfo = getNodeInfo(array, index);

        int lastProcessedIndex = index + 1;

        List<Node> children = new ArrayList<>();
        for (int child = 0; child < nodeInfo.childrenCount; child++) {
            lastProcessedIndex = buildTree(children, array, lastProcessedIndex + 1);
        }


        int[] metadata = getMetadata(array, lastProcessedIndex + 1, nodeInfo.metadataCount);

        parentChildren.add(new Node(metadata, children));

        return lastProcessedIndex + nodeInfo.metadataCount;
    }

    private NodeInfo getNodeInfo(int[] array, int index) {
        return new NodeInfo(array[index], array[index + 1]);
    }

    private int[] getMetadata(int[] array, int startIndex, int count) {
        return Arrays.copyOfRange(array, startIndex, startIndex + count);
    }

    private static int[] toIntArray(String input) {
        return stream(input.split(" ")).mapToInt(Integer::parseInt).toArray();
    }

    private static class NodeInfo {
        final int childrenCount;
        final int metadataCount;

        NodeInfo(int childrenCount, int metadataCount) {
            this.childrenCount = childrenCount;
            this.metadataCount = metadataCount;
        }
    }

    private static class Node {
        final List<Node> children;
        final int[] metadata;

        Node(int[] metadata, List<Node> children) {
            this.metadata = metadata;
            this.children = children;
        }

        int metadataSum() {
            return stream(metadata).sum() + children.stream().mapToInt(Node::metadataSum).sum();
        }

        int value() {
            if (children.isEmpty()) {
                return stream(metadata).sum();
            }

            return stream(metadata).map(this::getChildValue).sum();
        }

        private int getChildValue(int metadata) {
            return getChild(metadata - 1).map(Node::value).orElse(0);
        }

        private Optional<Node> getChild(int index) {
            return index < children.size() ? Optional.of(children.get(index)) : Optional.empty();
        }
    }

}
