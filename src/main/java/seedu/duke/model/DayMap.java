package seedu.duke.model;

import seedu.duke.exception.KeywordNotFoundException;
import seedu.duke.exception.ListNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static seedu.duke.ui.Ui.displayEmptyActivityCounterMessage;


/**
 * Use hashmap of to store all the data.
 * The key of the hashmap would be the date and the value would be the activityList for that day.
 */
public class DayMap {

    private HashMap<LocalDate, ActivityList> dayMap;
    private ActivityList lastSeenList;

    public DayMap() {
        this.dayMap = new HashMap<>();
        this.lastSeenList = new ActivityList();
    }

    /**
     * Sets the last seen list.
     * @param activityList is the list to be passed into the lastSeenList
     */
    public void setLastSeenList(ActivityList activityList) {
        this.lastSeenList = new ActivityList();
        this.lastSeenList = activityList;
    }

    /**
     * Gets the date from the specified index of the lastSeenList.
     * @param index index of the lastSeenList
     * @return localDate
     */
    public LocalDate getDateFromLastSeenListAtIndex(int index) {
        return lastSeenList.getActivity(index).getActivityDate();
    }

    /**
     * Returns the last seen list.
     * @return the lastSeenList
     */
    public ActivityList getLastSeenList() {
        return this.lastSeenList;
    }

    /**
     * Displays the list using the drawer classes.
     * @param date is the date of the list to be drawn.
     */
    public void drawListAfterListCommand(LocalDate date) {
        ListDrawer listDrawer = new ListDrawer(date, lastSeenList);
        listDrawer.printList();
    }

    /**
     * Adds activity into activityList under the corresponding dateTime.
     * Creates a new activityList if there are none under the specified date.
     * @param dateTime Specified date to extract activitylist from the dayMap.
     * @param activity Description of the activity, the userinput
     */
    public void addActivity(LocalDateTime dateTime, Activity activity) {
        ActivityList alist = this.getActivityList(dateTime);
        if (alist == null) {
            dayMap.put(dateTime.toLocalDate(), new ActivityList());
            alist = this.getActivityList(dateTime);
        }
        alist.addActivity(activity);
        //System.out.println("Total calorie count for "
        // + dateTime.toLocalDate().toString() + " = " + alist.getNetCalorie());
    }


    /**
     * This function removes an activity and replaces it with a new one, to work with edit command.
     * @param index the index of the activity to be deleted
     * @param activity the activity to replace the old one.
     */
    public void insertActivity(int index, Activity activity) {
        lastSeenList.insertActivity(index, activity);

    }


    /**
     * Returns the activityList of the specified date.
     * @param dateTime is the date specified to extract the list from
     * @return activityList
     */
    public ActivityList getActivityList(LocalDateTime dateTime) {
        return dayMap.get(dateTime.toLocalDate());
    }

    /**
     * Returns the size of the specified activity list.
     * @param dateTime the date of which the activitylist should be extracted from
     * @return the size of the list
     */
    public int getSizeOfActivityList(LocalDateTime dateTime) {
        ActivityList alist = this.getActivityList(dateTime);
        if (alist == null) {
            return 0;
        } else {
            return alist.getNumberOfActivities();
        }
    }

    /**
     * Returns the net calorie.
     * @param date is the date of which to extract the activitylist from
     * @return the net calorie
     */
    public int getNetCalorieOfDay(LocalDate date) {
        ActivityList alist = getActivityList(date.atStartOfDay());
        return alist.getNetCalorie();
    }

    public HashMap<LocalDate, ActivityList> getHashMap() {
        return dayMap;
    }

    /**
     * Prints the activities for the given date.
     * @param date specified date to print the list
     */
    public void printActivityList(LocalDate date) {
        if (!dayMap.containsKey(date)) {
            displayEmptyActivityCounterMessage();
        } else {
            getActivityList(date.atStartOfDay()).printList();
        }
    }

    /**
     * Finds the activities containing a keyword.
     * @param description is the keyword where the activity should contain
     * @throws KeywordNotFoundException when the keyword is not found in any activity
     */
    public void listActivitiesContainingDescription(String description) throws KeywordNotFoundException {
        setLastSeenList(new ActivityList());
        Iterator it = dayMap.entrySet().iterator();
        int activityFindCounter = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String date = pair.getKey().toString();
            ActivityList activities = (ActivityList) pair.getValue();
            int activityCounter = activities.getNumberOfActivities();
            if (activityCounter > 0) {
                for (int i = 0; i < activityCounter; i++) {
                    String currentLine = activities.getActivity(i).toString();
                    String descriptionToCheck = currentLine.substring(currentLine.indexOf("|") + 1);
                    descriptionToCheck = descriptionToCheck.substring(0, descriptionToCheck.indexOf("|")).trim();
                    if (descriptionToCheck.contains(description)) {
                        //System.out.println((activityFindCounter + 1) + ". " + date + " " + currentLine);
                        lastSeenList.addActivity(activities.getActivity(i));
                        activityFindCounter++;
                    }
                }
            }
        }
        if (activityFindCounter == 0) {
            throw new KeywordNotFoundException();
        }
    }

    /**
     * Finds the activities containing a keyword.
     * @param calorie is the calorie to be matched
     * @throws KeywordNotFoundException when the keyword is not found in any activity
     */
    public void listActivitiesContainingCalorie(String calorie) throws KeywordNotFoundException {
        setLastSeenList(new ActivityList());
        Iterator it = dayMap.entrySet().iterator();
        int activityFindCounter = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String date = pair.getKey().toString();
            ActivityList activities = (ActivityList) pair.getValue();
            int activityCounter = activities.getNumberOfActivities();
            if (activityCounter > 0) {
                for (int i = 0; i < activityCounter; i++) {
                    String currentLine = activities.getActivity(i).toString();
                    int calorieStartIndex = currentLine.lastIndexOf(' ');
                    String calorieToCheck = currentLine.substring(calorieStartIndex).trim();
                    if (calorieToCheck.equals(calorie)) {
                        System.out.println((activityFindCounter + 1) + ". " + date + " " + currentLine);
                        lastSeenList.addActivity(activities.getActivity(i));
                        activityFindCounter++;
                    }
                }
            }
        }
        if (activityFindCounter == 0) {
            throw new KeywordNotFoundException();
        }
    }

    /**
     * Finds the activities containing all keywords.
     * @param userInput is the unparsed activity description
     * @throws KeywordNotFoundException when the keyword is not found in any activity
     */
    public void listActivitiesContainingAll(String userInput) throws KeywordNotFoundException {
        setLastSeenList(new ActivityList());
        Iterator it = dayMap.entrySet().iterator();
        int activityFindCounter = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String date = pair.getKey().toString();
            ActivityList activities = (ActivityList) pair.getValue();
            int activityCounter = activities.getNumberOfActivities();
            if (activityCounter > 0) {
                for (int i = 0; i < activityCounter; i++) {
                    boolean hasAllWords;
                    String currentLine = activities.getActivity(i).toString();
                    hasAllWords = checkAllWords(currentLine, userInput);
                    if (hasAllWords) {
                        lastSeenList.addActivity(activities.getActivity(i));
                        activityFindCounter++;
                    }
                }
            }
        }
        if (activityFindCounter == 0) {
            throw new KeywordNotFoundException();
        }
    }

    /**
     * Finds the activities containing at least one of the keywords inputted.
     * @param userInput is the unparsed activity description
     * @throws KeywordNotFoundException when the keyword is not found in any activity
     */
    public void listActivitiesContainingEither(String userInput) throws KeywordNotFoundException {
        setLastSeenList(new ActivityList());
        Iterator it = dayMap.entrySet().iterator();
        int activityFindCounter = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String date = pair.getKey().toString();
            ActivityList activities = (ActivityList) pair.getValue();
            int activityCounter = activities.getNumberOfActivities();
            if (activityCounter > 0) {
                for (int i = 0; i < activityCounter; i++) {
                    boolean hasOneWord;
                    String currentLine = activities.getActivity(i).toString();
                    hasOneWord = checkEitherWords(currentLine, userInput);
                    if (hasOneWord) {
                        lastSeenList.addActivity(activities.getActivity(i));
                        activityFindCounter++;
                    }
                }
            }
        }
        if (activityFindCounter == 0) {
            throw new KeywordNotFoundException();
        }
    }

    /**
     * Checks if all keywords inputted by user is present in entry.
     * @param currentLine current entry to be checked
     * @return true if all words are present, false otherwise
     */
    private boolean checkAllWords(String currentLine, String userInput) {
        ArrayList<String> wordsToCheck = new ArrayList<>();
        wordsToCheck = getAllTags(userInput);
        for (String word : wordsToCheck) {
            if (!currentLine.contains(word)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if one of the keywords inputted by user is present in entry.
     * @param currentLine current entry to be checked
     * @return  hasOneWord true if just one word is present, false otherwise
     */
    private boolean checkEitherWords(String currentLine, String userInput) {
        ArrayList<String> wordsToCheck = new ArrayList<>();
        wordsToCheck = getAllTags(userInput);
        for (String word : wordsToCheck) {
            if (currentLine.contains(word)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Parses all keywords inputted by user into an arraylist.
     * @param userInput String user typed into CLI
     */
    private ArrayList<String> getAllTags(String userInput) {
        ArrayList<String> tags = new ArrayList<>();
        while (userInput.indexOf("/") != userInput.lastIndexOf("/")) {
            int firstIndex = userInput.indexOf("/");
            int secondIndex = userInput.indexOf("/", userInput.indexOf("/") + 1);
            String firstWord = userInput.substring(firstIndex + 1, secondIndex).trim();
            userInput = userInput.substring(secondIndex);
            tags.add(firstWord);
        }
        userInput = userInput.substring(userInput.indexOf("/") + 1).trim();
        tags.add(userInput);
        return tags;
    }

    /**
     * Deletes the activity with a given index.
     * @param index is the index of the activity to be deleted
     * @throws IndexOutOfBoundsException if the index provided is out of range
     */
    public void deleteActivity(int index) throws IndexOutOfBoundsException {

        if (lastSeenList.isValidIndex(index)) {
            Activity activityToMatch = lastSeenList.getActivity(index);
            //if previous command was the list command then this will straight away delete the activity
            // from the list in the daymap
            lastSeenList.removeActivity(index);

            //if all the activities in a date is deleted, this is the key to be removed from the daymap
            LocalDate keyToDelete = null;

            //iterating through the entire daymap to find the activity to delete
            Iterator it = dayMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                ActivityList activities = (ActivityList) pair.getValue();
                int activityCounter = activities.getNumberOfActivities();

                if (activityCounter > 0) {
                    for (int i = 0; i < activityCounter; i++) {
                        if (activityToMatch.equals(activities.getActivity(i))) {
                            activities.removeActivity(i);
                            //if deleted the last item in the ActivityList then obtain the key to be deleted from daymap
                            if (activities.getNumberOfActivities() == 0) {
                                keyToDelete = (LocalDate) pair.getKey();
                            }
                            break;
                        }
                    }
                    //If encountered a activitylist with a count of 0,
                    // which will be resulted if deleted the last item of ActivityList from a list command
                } else if (activityCounter == 0) {
                    keyToDelete = (LocalDate)pair.getKey();
                }
            }
            //removes key from the daymap
            dayMap.remove(keyToDelete);
            //displaySavedMessage();
        } else {
            throw new IndexOutOfBoundsException();
        }

    }


    /**
     * Moves an activity from one index to another.
     * @param indexToBeMovedFrom the index to be moved from
     * @param indexToBeInsertedBelow the index to be moved to
     * @throws IndexOutOfBoundsException when index is not within the bounds
     * @throws ListNotFoundException when the list is not found
     */
    public void move(int indexToBeMovedFrom, int indexToBeInsertedBelow)
            throws IndexOutOfBoundsException, ListNotFoundException {
        if (lastSeenList.getNumberOfActivities() == 0) {
            throw new ListNotFoundException();
        } else {
            lastSeenList.moveActivity(indexToBeMovedFrom - 1, indexToBeInsertedBelow);
        }
    }

    /**
     * Sets the activities at a given date as a string.
     * For e.g, 2020-10-11: [F] | apple | 50, [F] | banana | 100, [E] | pushup | 10, [E] | jogging | 60.
     * @param dateTime is the specified date
     * @return activities as a string for the given date
     */
    public String toString(LocalDateTime dateTime) {
        ActivityList alist = this.getActivityList(dateTime);
        return dateTime.toLocalDate().toString() + ", " + alist.toString();
    }

    /**
     * Prints the primitive list of activities for the given date.
     * @param date is the date specified to extract the activitylist from
     */
    public void printList(LocalDate date) {
        System.out.println(date.toString());
        getActivityList(date.atStartOfDay()).printList();
    }
}
