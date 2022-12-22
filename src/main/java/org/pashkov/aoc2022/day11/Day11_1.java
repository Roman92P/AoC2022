package org.pashkov.aoc2022.day11;

import org.pashkov.aoc2022.util.FileReaderImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class Day11_1 {

    public static int lcm(int number1, int number2) {
        if (number1 == 0 || number2 == 0) {
            return 0;
        }
        int absNumber1 = Math.abs(number1);
        int absNumber2 = Math.abs(number2);
        int absHigherNumber = Math.max(absNumber1, absNumber2);
        int absLowerNumber = Math.min(absNumber1, absNumber2);
        int lcm = absHigherNumber;
        while (lcm % absLowerNumber != 0) {
            lcm += absHigherNumber;
        }
        return lcm;
    }
    public static void main(String[] args) {
        System.out.println(lcm(23, 17));

        Set<Monkey> monkeys = mapInputToMonkeyObj(getFileInput());
        Map<Integer, Long> inspectingCounters = new HashMap<>();

        int roundCounter = 0;

        //get common multiply for all divisible checks
        List<Integer> allDivisibleChecks = monkeys.stream()
                .map(monkey -> monkey.getDivisibleTest())
                .mapToInt(Integer::intValue)
                .boxed()
                .collect(Collectors.toList());


        while (true) {
            if (roundCounter == 20) {
                break;
            }
            System.out.println(roundCounter);
            startRound(monkeys, inspectingCounters);
            roundCounter++;
        }

        for (Monkey m : monkeys) {
            System.out.println(m);
        }

        List<Long> result = new ArrayList<>();
        for (Map.Entry<Integer, Long> entry : inspectingCounters.entrySet()) {
//            System.out.printf("Monkey %d inspected items %d times.", entry.getKey(), entry.getValue());
//            System.out.println();
            result.add(entry.getValue());
        }
        List<Long> twoMostActiveMonkeys = result.stream()
                .sorted()
                .collect(Collectors.toList());

        Collections.reverse(twoMostActiveMonkeys);

        System.out.println(twoMostActiveMonkeys);
        long r = twoMostActiveMonkeys.get(0) * twoMostActiveMonkeys.get(1);
        System.out.println(r);
    }

    private static void startRound(Set<Monkey> monkeys, Map<Integer, Long> inspectingCounters) {
        for (Monkey monkey : monkeys) {
//            System.out.printf("Monkey %d: ", monkey.getId());
//            System.out.println();
            List<BigInteger> mItems = monkey.getStartingItems();
            String operation = monkey.getOperation();
            int divTestValue = monkey.getDivisibleTest();
            List<String> conditions = monkey.getConditions();
            for (BigInteger item : mItems) {
                Long c = Optional.ofNullable(inspectingCounters.get(monkey.id)).orElse(0L);
                inspectingCounters.put(monkey.id, c + 1);

                BigInteger toRemove = item;
//                System.out.printf("Monkey inspects an item with a worry level of %d: ", item);
//                System.out.println();
                item = applyOperationToItem(item, operation, divTestValue);
                //item = divideByThree(item);
                if (item.divideAndRemainder(BigInteger.valueOf(divTestValue))[1].equals(BigInteger.ZERO)) {
                    String trueCondition = conditions.get(0);
                    String monkeyId = trueCondition.substring(trueCondition.length() - 1);
//                    System.out.printf("Current worry level is divisible by %d", divTestValue);
//                    System.out.println();
                    throwItemToMonkey(item, Integer.parseInt(monkeyId), monkeys);
                } else {
                    String falseCondition = conditions.get(1);
                    String monkeyId = falseCondition.substring(falseCondition.length() - 1);
//                    System.out.printf("Current worry level is not divisible by %d", divTestValue);
//                    System.out.println();
                    throwItemToMonkey(item, Integer.parseInt(monkeyId), monkeys);
                }
                removeMonkeyItemAfterInspection(toRemove, monkey.getId(), monkeys);
                //System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
            }
        }
    }

    private static void removeMonkeyItemAfterInspection(BigInteger toRemove, int id, Set<Monkey> monkeys) {
        monkeys.stream()
                .map(monkey -> {
                    if (monkey.getId() == id) {
                        List<BigInteger> updatedItems = monkey.getStartingItems();
                        updatedItems = updatedItems.stream().filter(integer -> !integer.equals(toRemove))
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
//        System.out.printf("Monkey gets bored with item. Worry level is divided by 3 to %d.", a.intValue());
//        System.out.println();
        return a.intValue();
    }

    private static void throwItemToMonkey(BigInteger item, int id, Set<Monkey> monkeys) {
        monkeys.stream()
                .map(monkey -> {
                    if (monkey.getId() == id) {
                        List<BigInteger> updatedItems = monkey.getStartingItems();
                        updatedItems.add(item);
                        monkey.setStartingItems(updatedItems);
                    }
                    return monkey;
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));
//        System.out.printf("Item with worry level %d is thrown to monkey %d.", item, id);
//        System.out.println();
    }

    private static BigInteger applyOperationToItem(BigInteger item, String operation, int divTestValue) {
        String[] opArr = operation.split("\\s");
        String arg1 = opArr[0];
        String arg2 = opArr[2];

//        BigInteger a1 = arg1.equals("old") ? item : Integer.parseInt(arg1);
//        BigInteger b1 = arg2.equals("old") ? item : Integer.parseInt(arg2);
        BigInteger a1 = arg1.equals("old") ? item : new BigInteger(arg1);
        BigInteger b1 = arg2.equals("old") ? item : new BigInteger(arg2);

        switch (opArr[1].trim()) {
            case "+":
//                System.out.printf("Worry level is increased by %d to %d.", b1, a1.add(b1));
//                System.out.println();
//                return Math.abs(a1 + b1);
                BigInteger add = a1.add(b1);
                BigInteger[] bigIntegers = add.divideAndRemainder(BigInteger.valueOf(divTestValue));
                return bigIntegers[0];
            case "*":
//                System.out.printf("Worry level is multiplied by %d to %d.", b1, a1.multiply(b1));
//                System.out.println();
//                return Math.abs(a1 * b1);
                BigInteger mult = a1.multiply(b1);
                BigInteger[] bigIntegers1 = mult.divideAndRemainder(BigInteger.valueOf(divTestValue));
                return bigIntegers1[0];
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
                            .map(BigInteger::new)
//                            .mapToLong(Long::valueOf)
//                            .boxed()
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
        return FileReaderImpl.readEachLinesFromFile("day11-1e.txt");
    }

    private static class Monkey {
        private int id;
        private List<BigInteger> startingItems = new LinkedList<>();
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

        public List<BigInteger> getStartingItems() {
            return startingItems;
        }

        public void setStartingItems(List<BigInteger> startingItems) {
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
