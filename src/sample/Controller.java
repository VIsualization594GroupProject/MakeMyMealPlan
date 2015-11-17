package sample;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.ListView;

import java.util.Observable;
import java.util.Observer;

/*
 * the controller handles the events generated by the GUI- anything the GUI needs to do should happen in here.
 * Also, any time the model changes, information will need to be updated to the GUI- the controller handles that too
 * It's the layer handling all communication between the model and the GUI.
 *

 */
public class Controller implements Observer {
    Model model;
    ListView focus;
    public void setModel(Model model){
        this.model = model;
    }


    public void makeBreakfastFocus(Event event) {//This is a change
        System.err.println("Breakfast is focus (really)");
        focus = (ListView) Main.root.lookup("#BreakfastView");
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
}
