package org.pashkov.aoc2022.day11;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

//16429959433 to low
//16402325184
//16435163968 to low
public class Day11_1 {
    public static void main(String[] args) {
        BigInteger bg = new BigInteger("5743128071634326550");
        BigInteger r = bg.multiply(new BigInteger("2"));
        System.out.println(r);
        System.out.println(5743128071634326550L * 2);
//        Set<Monkey> monkeys = mapInputToMonkeyObj(getFileInput());
//        Map<Integer, Long> inspectingCounters = new HashMap<>();
//
//        int roundCounter = 0;
//
//        while (true) {
//            if (roundCounter == 10000) {
//                break;
//            }
//            startRound(monkeys, inspectingCounters);
//            roundCounter++;
//        }
//
//        for (Monkey m : monkeys) {
//            System.out.println(m);
//        }
//
//        List<Long> result = new ArrayList<>();
//        for (Map.Entry<Integer, Long> entry : inspectingCounters.entrySet()) {
//            System.out.printf("Monkey %d inspected items %d times.", entry.getKey(), entry.getValue());
//            System.out.println();
//            result.add(entry.getValue());
//        }
//        List<Long> twoMostActiveMonkeys = result.stream()
//                .sorted()
//                .collect(Collectors.toList());
//
//        Collections.reverse(twoMostActiveMonkeys);
//
//        System.out.println(twoMostActiveMonkeys);
//        long r = twoMostActiveMonkeys.get(0) * twoMostActiveMonkeys.get(1);
//        System.out.println(r);

    }

    private static void startRound(Set<Monkey> monkeys, Map<Integer, Long> inspectingCounters) {
        for (Monkey monkey : monkeys) {
            System.out.printf("Monkey %d: ", monkey.getId());
            System.out.println();
            List<Long> mItems = monkey.getStartingItems();
            String operation = monkey.getOperation();
            int divTestValue = monkey.getDivisibleTest();
            List<String> conditions = monkey.getConditions();
            for (long item : mItems) {
                //update monkey inspection counter
                long c = Optional.ofNullable(inspectingCounters.get(monkey.id)).orElse(0L);
                inspectingCounters.put(monkey.id, (c + 1));

                long toRemove = item;
                System.out.printf("Monkey inspects an item with a worry level of %d: ", item);
                System.out.println();
                item = applyOperationToItem(item, operation);
                //item = divideByThree(item);
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

    private static void removeMonkeyItemAfterInspection(long toRemove, int id, Set<Monkey> monkeys) {
        monkeys.stream()
                .map(monkey -> {
                    if (monkey.getId() == id) {
                        List<Long> updatedItems = monkey.getStartingItems();
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

    private static void throwItemToMonkey(long item, int id, Set<Monkey> monkeys) {
        monkeys.stream()
                .map(monkey -> {
                    if (monkey.getId() == id) {
                        List<Long> updatedItems = monkey.getStartingItems();
                        updatedItems.add(item);
                        monkey.setStartingItems(updatedItems);
                    }
                    return monkey;
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
        System.out.printf("Item with worry level %d is thrown to monkey %d.", item, id);
        System.out.println();
    }

    private static long applyOperationToItem(long item, String operation) {
        String[] opArr = operation.split("\\s");
        String arg1 = opArr[0];
        String arg2 = opArr[2];

        long a1 = arg1.equals("old") ? item : Integer.parseInt(arg1);
        long b1 = arg2.equals("old") ? item : Integer.parseInt(arg2);

        switch (opArr[1].trim()) {
            case "+":
                System.out.printf("Worry level is increased by %d to %d.", b1, a1 + b1);
                System.out.println();
                return Math.abs(a1 + b1);
            case "*":
                System.out.printf("Worry level is multiplied by %d to %d.", b1, a1 * b1);
                System.out.println();
                return Math.abs(a1 * b1);
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
                            .mapToLong(Long::valueOf)
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
        return FileReaderImpl.readEachLinesFromFile("AoC2022/day11_1.txt");
    }

    private static class Monkey {
        private int id;
        private List<Long> startingItems = new LinkedList<>();
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

        public List<Long> getStartingItems() {
            return startingItems;
        }

        public void setStartingItems(List<Long> startingItems) {
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
