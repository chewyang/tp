package seedu.duke.model;

import seedu.duke.command.GraphCommand;
import seedu.duke.model.DayMap;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class GraphProperty {
    public static final int ROW = 11;
    private static String DATE_FORMAT = "dd/MM";
    private static int TARGET_TYPE = 2;
    private static int LIMIT_TYPE = 4;
    private static int DIVISOR = 10;
    private static int EMPTY = 0;
    public int targetRow;
    public int column;
    public final DayMap dayMap;
    ArrayList<LocalDate> keys;
    public int targetCalories;
    public int maxCalories;
    public int minCalories;
    public int[][] table;

    /**
     * Constructor for the graph.
     *
     * @param dayMap hashmap containing dates
     * @param targetCalories tagret calories from userprofile
     */
    public GraphProperty(DayMap dayMap, int targetCalories) {
        assert dayMap != null;
        this.dayMap = dayMap;
        this.targetCalories = targetCalories;

    }

    /**
     * Maximum days in graph is 7 or lower.
     *
     * @return number of days to be shown in graph
     */
    private int checkSize() {
        int size = dayMap.getHashMap().size();
        assert size != 0;
        if (size < GraphCommand.MAXIMUM_DAYS) {
            return size;
        }
        return GraphCommand.MAXIMUM_DAYS;
    }

    /**
     * Set other properties by calculation.
     */
    public void setProperties() {
        this.column = checkSize();
        this.keys = sortKeys();
        ArrayList<Integer> calories = getCalories();
        this.table = initiateTable(calories);
    }

    /**
     * Initiates a 2 dimension table and fills the table with 0.
     */
    public int[][] setEmptyTable(int[][] table) {
        for (int[] row : table) {
            Arrays.fill(row, EMPTY);
        }
        return table;
    }

    /**
     * Get and sort keys from hashmap.
     *
     * @return sorted keys in arraylist
     */
    public ArrayList<LocalDate> sortKeys() {
        ArrayList<LocalDate> keys = new ArrayList<>();
        for (LocalDate key : dayMap.getHashMap().keySet()) {
            keys.add(key);
        }
        //sort the keys by date
        keys.sort(LocalDate::compareTo);
        ArrayList<LocalDate> newKeys = new ArrayList<>();
        for (int i = keys.size() - column; i < keys.size(); i++) {
            newKeys.add(keys.get(i));
        }
        return newKeys;
    }

    /**
     * Get calories from the dates.
     *
     * @return array of calories
     */
    public ArrayList<Integer> getCalories() {
        int currentCalories;
        int minCalories = targetCalories;
        int maxCalories = targetCalories;
        ArrayList<Integer> calories = new ArrayList<>();
        for (LocalDate date : keys) {
            currentCalories = dayMap.getNetCalorieOfDay(date);
            calories.add(currentCalories);
            maxCalories = findMaximum(maxCalories, currentCalories);
            minCalories = findMinimum(minCalories, currentCalories);
        }
        checkBoundaries(minCalories, maxCalories);
        return calories;
    }

    public void checkBoundaries(int minCalories, int maxCalories) {
        assert maxCalories != 0;
        if (maxCalories - minCalories < DIVISOR) {
            minCalories -= DIVISOR / 2;
            maxCalories += DIVISOR / 2;
        }
        this.minCalories = minCalories;
        this.maxCalories = maxCalories;

    }

    /**
     * Compares and finds the minimum between 2 numbers.
     *
     * @param firstNumber first number to compare
     * @param secondNumber second number to compare
     * @return the lesser number
     */
    public int findMinimum(int firstNumber, int secondNumber) {
        if (firstNumber < secondNumber) {
            return firstNumber;
        } else {
            return secondNumber;
        }
    }

    /**
     * Compares and finds the maximum between 2 numbers.
     *
     * @param firstNumber first number to compare
     * @param secondNumber second number to compare
     * @return the greater number
     */
    public int findMaximum(int firstNumber, int secondNumber) {
        if (firstNumber < secondNumber) {
            return secondNumber;
        }
        return firstNumber;
    }

    /**
     * Calculates interval of the graph.
     *
     * @return interval value
     */
    public int calculateInterval() {
        assert maxCalories - minCalories != 0;
        return (maxCalories - minCalories) / DIVISOR;
    }


    /**
     * Fills up the table with appropriate values.
     * 0 -> empty spaces.
     * 1 -> middle portion of the bar.
     * 2 -> target row.
     * 3 -> target row  + middle portion of the bar.
     * 4 -> top symbol of the bar.
     *
     * @param table representation of graph in 2d array.
     * @param calories calories list
     */
    public void fillTable(int[][] table, ArrayList<Integer> calories) {
        assert table != null;
        assert calories != null;
        this.targetRow = calculateRowNumber(targetCalories);
        int rowNumber;
        for (int i = ROW - 1; i >= 0; i--) {
            for (int j = 0; j < column; j++) {
                rowNumber = calculateRowNumber(calories.get(j));
                if (rowNumber == i) {
                    table[i][j] = LIMIT_TYPE;
                } else if (targetRow == i) {
                    table[i][j] = TARGET_TYPE;
                }
                if (rowNumber > i) {
                    table[i][j]++;
                }
            }
        }
    }

    /**
     * Parses the date into string.
     *
     * @return date in dd/MM format
     */
    public String parseDate(ArrayList<LocalDate> keys) {
        assert keys != null;
        String formattedDate = "";
        for (LocalDate key : keys) {
            formattedDate += key.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
            formattedDate += " ";
        }
        return formattedDate;
    }

    /**
     * Initiates a table.
     *
     * @return table
     */
    public int[][] initiateTable(ArrayList<Integer> calories) {
        assert calories != null;
        int [][]table = new int[ROW][column];
        setEmptyTable(table);
        fillTable(table, calories);
        return table;
    }

    /**
     * Find the row number corresponding to the calories.
     */
    public int calculateRowNumber(int calories) {
        assert calories >= minCalories;
        int interval = calculateInterval();
        assert interval != 0;
        return (calories - minCalories) / interval;
    }

}
