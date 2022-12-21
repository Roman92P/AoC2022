package org.pashkov.aoc2022.day11;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Day11_1 {
    public static void main(String[] args) {
        Set<Monkey> monkeys = mapInputToMonkeyObj(getFileInput());

        int roundCounter = 0;

        while (true) {
            startRound(monkeys);
            roundCounter++;
            if (roundCounter == 1) {
                break;
            }
        }

        for (Monkey m : monkeys) {
            System.out.println(m);
        }
    }

    private static void startRound(Set<Monkey> monkeys) {
        for (Monkey monkey : monkeys) {
            System.out.printf("Monkey %d: ", monkey.getId());
            System.out.println();
            List<Integer> mItems = monkey.getStartingItems();
            String operation = monkey.getOperation();
            int divTestValue = monkey.getDivisibleTest();
            List<String> conditions = monkey.getConditions();
            for (int item : mItems) {
                int toRemove = item;
                System.out.printf("Monkey inspects an item with a worry level of %d: ", item);
                System.out.println();
                item = applyOperationToItem(item, operation);
                item = divideByThree(item);
                if (item % divTestValue == 0) {
                    String trueCondition = conditions.get(0);
                    String monkeyId = trueCondition.substring(trueCondition.length() - 1);
                    System.out.printf("Current worry level is divisible by %d", divTestValue);
                    System.out.println();
                    throwItemToMonkey(item, Integer.parseInt(monkeyId), monkeys);
                } else {
                    String falseCondition = conditions.get(1);
                    String monkeyId = falseCondition.substring(falseCondition.length() - 1);
                    System.out.printf("Current worry level is not divisible by %d", divTestValue);
                    System.out.println();
                    throwItemToMonkey(item, Integer.parseInt(monkeyId), monkeys);
                }
                removeMonkeyItemAfterInspection(toRemove, monkey.getId(), monkeys);
                System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
            }
        }
    }

    private static void removeMonkeyItemAfterInspection(int toRemove, int id, Set<Monkey> monkeys) {
        monkeys.stream()
                .map(monkey -> {
                    if (monkey.getId() == id) {
                        List<Integer> updatedItems = monkey.getStartingItems();
                        updatedItems = updatedItems.stream().filter(integer -> integer != toRemove)
                                .collect(Collectors.toList());
                        monkey.setStartingItems(updatedItems);
                    }
                    return monkey;
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private static int divideByThree(int item) {
        double result = 0;
        result = item / 3.0;
        BigDecimal a = BigDecimal.valueOf(result).setScale(0, BigDecimal.ROUND_DOWN);
        System.out.printf("Monkey gets bored with item. Worry level is divided by 3 to %d.", a.intValue());
        System.out.println();
        return a.intValue();
    }

    private static void throwItemToMonkey(int item, int id, Set<Monkey> monkeys) {
        monkeys.stream()
                .map(monkey -> {
                    if (monkey.getId() == id) {
                        List<Integer> updatedItems = monkey.getStartingItems();
                        updatedItems.add(item);
                        monkey.setStartingItems(updatedItems);
                    }
                    return monkey;
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
        System.out.printf("Item with worry level %d is thrown to monkey %d.", item, id);
        System.out.println();
    }

    private static int applyOperationToItem(int item, String operation) {
        String[] opArr = operation.split("\\s");
        String arg1 = opArr[0];
        String arg2 = opArr[2];

        int a1 = arg1.equals("old") ? item : Integer.parseInt(arg1);
        int b1 = arg2.equals("old") ? item : Integer.parseInt(arg2);

        switch (opArr[1].trim()) {
            case "+":
                System.out.printf("Worry level is increased by %d to %d.", b1, a1 + b1);
                System.out.println();
                return a1 + b1;
            case "*":
                System.out.printf("Worry level is multiplied by %d to %d.", b1, a1 * b1);
                System.out.println();
                return a1 * b1;
        }
        return item;
    }

    private static Set<Monkey> mapInputToMonkeyObj(List<String> fileInput) {
        Set<Monkey> resultList = new LinkedHashSet<>();

        String[] inputArr = fileInput.stream()
                .collect(Collectors.joining())
                .split("Monkey\\s\\d+:");
        String[] correctArr = Arrays.copyOfRange(inputArr, 1, inputArr.length);
        int count = 0;
        for (String monkeyStr : correctArr) {
            String[] monkeyStrArr = monkeyStr.trim().split("\\s{2}");
            Monkey monkey = new Monkey();
            monkey.setId(count);
            List<String> conditions = new LinkedList<>();
            for (int i = 0; i < monkeyStrArr.length; i++) {
                if (monkeyStrArr[i].startsWith("Starting")) {
                    String[] monkeyItemsArr = monkeyStrArr[i].replaceAll("[a-zA-Z:\\s]", "")
                            .split(",");
                    monkey.setStartingItems(Arrays.stream(monkeyItemsArr)
                            .mapToInt(Integer::valueOf)
                            .boxed()
                            .collect(Collectors.toList()));
                } else if (monkeyStrArr[i].startsWith("Operation")) {
                    String[] operationArr = monkeyStrArr[i].split("\\s=\\s");
                    monkey.setOperation(operationArr[1].trim());
                } else if (monkeyStrArr[i].startsWith("Test")) {
                    String divisibleValue = monkeyStrArr[i].replaceAll("\\D", "").trim();
                    monkey.setDivisibleTest(Integer.parseInt(divisibleValue));
                } else if (monkeyStrArr[i].startsWith("If")) {
                    conditions.add(monkeyStrArr[i].trim());
                }
            }
            monkey.setConditions(conditions);
            count++;
            resultList.add(monkey);
        }

        return resultList;
    }

    private static List<String> getFileInput() {
        return FileReaderImpl.readEachLinesFromFile("AoC2022/day11_1e.txt");
    }

    private static class Monkey {
        private int id;
        private List<Integer> startingItems = new LinkedList<>();
        private int divisibleTest;
        private List<String> conditions = new LinkedList<>();
        private String operation;

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<Integer> getStartingItems() {
            return startingItems;
        }

        public void setStartingItems(List<Integer> startingItems) {
            this.startingItems = startingItems;
        }

        public int getDivisibleTest() {
            return divisibleTest;
        }

        public void setDivisibleTest(int divisibleTest) {
            this.divisibleTest = divisibleTest;
        }

        public List<String> getConditions() {
            return conditions;
        }

        public void setConditions(List<String> conditions) {
            this.conditions = conditions;
        }

        @Override
        public String toString() {
            return "Monkey{" +
                    "id=" + id +
                    ", startingItems=" + startingItems +
                    ", divisibleTest=" + divisibleTest +
                    ", conditions=" + conditions +
                    ", operation='" + operation + '\'' +
                    '}';
        }
    }

}
