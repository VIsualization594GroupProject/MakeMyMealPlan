package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.*;

import java.util.*;

/*
 * the controller handles the events generated by the GUI- anything the GUI needs to do should happen in here.
 * Also, any time the model changes, information will need to be updated to the GUI- the controller handles that too
 * It's the layer handling all communication between the model and the GUI.
 *

 */
public class Controller implements Observer {
    static Model model;
    ListView focus;
    public void setModel(Model m){
        model = m;
        init();
    }


    ArrayList<String> categoryList;
    static ComboBox category, food, gender, activity;
    static ComboBox[] trackNutrient = new ComboBox[5];
    static Label [] nutrientLabels = new Label[5], warningLabels=new Label[5];
    static ProgressBar[] progressBars = new ProgressBar[5];
    static List<String> genderList, activityList;

    static void prepComboBox(ComboBox<String> c, List<String> strings) {//Sets a combobox to contain the labels
        ObservableList<String> list = FXCollections.observableList(strings);
        c.setItems(list);
        SelectionModel s = c.getSelectionModel();
        c.setValue(list.get(0));
        //c.setPromptText(null);
    }
    private void init() {
//data structures
        genderList = new ArrayList<String>(2);
        genderList.add("Male");
        genderList.add("Female");
        activityList = new ArrayList<String>(5);
        for(NutrientCalculator.ExerciseLevel x : NutrientCalculator.allLevels){
            activityList.add(x.toString());
        }

        for (int i = 0; i < 5 ; ++i) {
            nutrientLabels[i] = (Label) Main.root.lookup("#nutrientLabel"+(i+1));
            warningLabels[i] = (Label) Main.root.lookup("#warningLabel" + (i+1));
            progressBars[i] = (ProgressBar) Main.root.lookup("#nutrientProgress"+(i+1));
            warningLabels[i].setVisible(false);
            progressBars[i].setStyle("-fx-accent: pink");
            trackNutrient[i]= (ComboBox)Main.root.lookup("#trackNutrient"+(i+1));
        }

        initComboBoxes();

    }


    public void initComboBoxes(){
       Hashtable<String, ArrayList<String>> categories= model.getCategoryToItemsList();
        category = (ComboBox<String>) Main.root.lookup("#categoryCombo");
        food = (ComboBox)Main.root.lookup("#foodCombo");
        gender= (ComboBox)Main.root.lookup("#genderCombo");
        activity= (ComboBox)Main.root.lookup("#activityCombo");

        categoryList = new ArrayList<String>();
        for(String x : categories.keySet())categoryList.add(x);
        prepComboBox(category, categoryList);
        prepComboBox(food, categories.get(categoryList.get(0)));
        prepComboBox(gender, genderList);
        prepComboBox(activity, activityList);
        for (int i = 0; i < 5; i++) {
            prepComboBox(trackNutrient[i], model.getNutrients());
            trackNutrient[i].setValue(model.getNutrients().get(i));
        }

    }

    public void makeBreakfastFocus(Event event) {//This is a change
        System.err.println("Breakfast is focus (really)");
        focus = (ListView) Main.root.lookup("#BreakfastView");
        String SelectedItem = "Temp";
        model.addSelectedItem(SelectedItem);
    }

    public void makeLunchFocus(Event event) {
        System.err.println("Lunch is focus (not really)");

    }

    public void makeDinnerFocus(Event e) {
        System.err.println("Dinner is focus (not really)");

    }



    public void addToDinner(ActionEvent actionEvent) {
        System.err.println("Dinner Stuff");
    }

    public void addToLunch(ActionEvent actionEvent) {
        System.err.println("Lunch Stuff");
    }

    public void addToBreakfast(ActionEvent actionEvent) {//Official change by Anthony
        System.err.println("BreakfastStuff");
    }

    public void printLabels(ActionEvent actionEvent) {//Now Sofy added this change
        System.err.println("Print Stuff");
    }

    public void removeAllItems(ActionEvent actionEvent) {//This is another change
    }


    public void removeFocusedItem(ActionEvent actionEvent) {
    }

    @Override
    ///This is the method that updates the
    public void update(Observable o, Object arg) {

    }

    public void runAlgorithm(ActionEvent actionEvent) {

    }

    public void calculateStuff(ActionEvent actionEvent) {
        model.updateNutrientCalcBasedonPersonalDetails();
        System.err.println("Calculating things on click");
    }

    private void changeBoxAndAssigns(int i) {
        String newLabel = trackNutrient[i].getValue().toString();
        //TODO: Implement goal logic
        //warningLabels[i].setVisible(overGoal(i));
        nutrientLabels[i].setText(newLabel);
        progressBars[i].setProgress(.3*i);

    }


    public void updateBox1(ActionEvent actionEvent) {
        changeBoxAndAssigns(0);
    }


    public void updateBox2(ActionEvent actionEvent) {
        changeBoxAndAssigns(1);
    }
    public void updateBox3(ActionEvent actionEvent) {
        changeBoxAndAssigns(2);
    }
    public void updateBox4(ActionEvent actionEvent) {
     changeBoxAndAssigns(3);
    }
    public void updateBox5(ActionEvent actionEvent) {
    changeBoxAndAssigns(4);
    }
}
