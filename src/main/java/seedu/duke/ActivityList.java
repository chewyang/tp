package seedu.duke;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * List of activities for any day.
 */
public class ActivityList extends Duke {
    private ArrayList<Activity> activities;
    private int activityCounter;

    public ActivityList() {
        activities = new ArrayList<>();
    }

    /**
     * Returns the current number of activities in the list.
     *
     * @return current number of activities in the list
     */
    public int getNumberOfActivities() {
        return activities.size();
    }

    public ArrayList getArrayList() {
        return activities;
    }

    /**
     * Adds food with its respective calories to activity list.
     *
     //* @param userInput Food description.
     //* @param calories  Amount of calories.
     */
    //public void addFood(String userInput, int calories) {
    //Activity item = new Food(userInput, calories);
    //activities.add(item);
    //System.out.println("\t" + activities.get(activityCounter++).toString());
    //return activityCounter;
    //}

    /**
     * Adds exercise with its respective calories to activity list.
     //* @param userInput Exercise description.
     //* @param calories  Amount of calories.
     */
    //public void addExercise(String userInput, int calories) {
    //Activity item = new Exercise(userInput, calories);
    //activities.add(item);
    //System.out.println("\t" + activities.get(activityCounter++).toString());
    //return activityCounter;
    // }
    public void addActivity(Activity activity) {
        //Activity item = new Activity(userInput, calories);
        activities.add(activity);
        System.out.println(activities.get(activityCounter++).toString());
    }

    public Activity getActivity(int index) {
        return activities.get(index);
    }

    /**
     * Removes an activity from the list via index.
     *
     * @param index index of acitivty in list
     */
    public void removeActivity(int index) {
        if (isValidIndex(index)) {
            activities.remove(index);
            System.out.print("Activity removed!\n");
        } else {
            System.out.println("Please make sure index is within range");
        }
    }

    /**
     * Checks if the index is valid.
     *
     * @param index index of acitvity in list
     * @return true if index is within range, else false
     */
    public boolean isValidIndex(int index) {
        if ((index >= 0) && (index < activityCounter)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return (Arrays.toString(activities.toArray()));
    }
}
