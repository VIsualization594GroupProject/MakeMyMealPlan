package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.*;

import java.text.DecimalFormat;
import java.util.*;

/*
 * the controller handles the events generated by the GUI- anything the GUI needs to do should happen in here.
 * Also, any time the model changes, information will need to be updated to the GUI- the controller handles that too
 * It's the layer handling all communication between the model and the GUI.
 *

 */
public class Controller implements Observer {
    static Model model;
    static ListView<String> focus, breakfastList, lunchList, dinnerList;
    static String selectedItem = "none", focusedItem = null;
    public void setModel(Model m){
        model = m;
        init();
    }


    ArrayList<String> categoryList;
    static ComboBox<String> category, food, gender, activity;
    static ComboBox[] trackNutrient = new ComboBox[5];
    static TextField [] goalBox = new TextField[5];
    static Label [] nutrientLabels = new Label[5], warningLabels=new Label[5], progressTexts = new Label[5];
    static ProgressBar[] progressBars = new ProgressBar[5];

    static List<String> genderList, activityList;
    static DecimalFormat percentageFormat = new DecimalFormat("    ##.#%"), unitFormat = new DecimalFormat("0.0");
    static TextField ageBox, heightBox, weightBox;

    static Label servingSize, caloriesTot, caloriesGoal, totalFatTot, totalFatGoal,
            saturatedFatTot, saturatedFatGoal, cholesterolTot, cholesterolGoal, sodiumTot,
            sodiumGoal, carbsTot, carbsGoal, sugarsGoal, sugarsTot, fiberTot, fiberGoal,
            proteinTot, proteinGoal, calciumGoal, potassiumGoal, ironGoal, zincGoal,
            vitAGoal, vitCGoal,vitB6Goal, vitB12Goal, vitDGoal, vitEGoal;
    static Label[] labelCollection = {servingSize, caloriesTot, caloriesGoal, totalFatTot, totalFatGoal,
            saturatedFatTot, saturatedFatGoal, cholesterolTot, cholesterolGoal, sodiumTot,
            sodiumGoal, carbsTot, carbsGoal, sugarsGoal, sugarsTot, fiberTot, fiberGoal,
            proteinTot, proteinGoal, calciumGoal, potassiumGoal, ironGoal, zincGoal,
            vitAGoal, vitCGoal,vitB6Goal, vitB12Goal, vitDGoal, vitEGoal};
    static Button removeItemButton, clearAllButton;

    static String[] styles = {"-fx-accent: pink","-fx-accent: cyan",
            "-fx-accent: gold","-fx-accent: sienna","-fx-accent: orange" };
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
        initLabels();
        removeItemButton = (Button)Main.root.lookup("#removeButton");
        clearAllButton = (Button) Main.root.lookup("#clearAllButton");
        for (int i = 0; i < 5 ; ++i) {
            nutrientLabels[i] = (Label) Main.root.lookup("#nutrientLabel"+(i+1));
            warningLabels[i] = (Label) Main.root.lookup("#warningLabel" + (i+1));
            progressBars[i] = (ProgressBar) Main.root.lookup("#nutrientProgress"+(i+1));
            warningLabels[i].setVisible(false);
            progressBars[i].setStyle(styles[i]);
            trackNutrient[i]= (ComboBox)Main.root.lookup("#trackNutrient"+(i+1));
            progressTexts[i] = (Label) Main.root.lookup("#progressText"+(i+1));
            goalBox[i] = (TextField) Main.root.lookup("#goalBox"+(i+1));
        }
        breakfastList = (ListView<String>) Main.root.lookup("#breakfastList");
        breakfastList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (focus == breakfastList) {
                    focusedItem = newValue;
                    updateNutritionLabel(newValue);
                }
            }
        });
        lunchList = (ListView<String>) Main.root.lookup("#lunchList");
        lunchList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (focus == lunchList) {
                    focusedItem = newValue;
                    updateNutritionLabel(newValue);
                }
            }
        });
        dinnerList = (ListView<String>) Main.root.lookup("#dinnerList");
        dinnerList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (focus == dinnerList) {
                    focusedItem = newValue;
                    updateNutritionLabel(newValue);
                }
            }
        });
        ageBox = (TextField) Main.root.lookup("#ageBox");
        ageBox.setText("25");
        weightBox = (TextField) Main.root.lookup("#weightBox");
        weightBox.setText("175");
        heightBox = (TextField) Main.root.lookup("#heightBox");
        heightBox.setText("70");
        initComboBoxes();
        calculateStuff(null);

    }


    private void initLabels() {
    /*servingSize, caloriesTot, caloriesGoal, totalFatTot, totalFatGoal,
            saturatedFatTot, saturatedFatGoal, cholesterolTot, cholesterolGoal, sodiumTot,
            sodiumGoal, carbsTot, carbsGoal, sugarsGoal, sugarsTot, fiberTot, fiberGoal,
            proteinTot, proteinGoal, calciumGoal, potassiumGoal, ironGoal, zincGoal,
            vitAGoal, vitCGoal,vitB6Goal, vitB12Goal, vitDGoal, vitEGoal;
    */
        servingSize = (Label) Main.root.lookup("#ServingSizeLabel");
        caloriesTot = (Label) Main.root.lookup("#CaloriesNumberLabel");
        caloriesGoal= (Label) Main.root.lookup("#CaloriesPercentLabel");
        totalFatTot = (Label) Main.root.lookup("#TotalFatGramsLabel");
        totalFatGoal = (Label) Main.root.lookup("#TotalFatPercentLabel");
        saturatedFatTot = (Label) Main.root.lookup("#SaturatedFatGramsLabel");
        saturatedFatGoal = (Label) Main.root.lookup("#SaturatedFatPercentLabel");
        cholesterolTot = (Label) Main.root.lookup("#CholesterolMilligramsLabel");
        cholesterolGoal = (Label) Main.root.lookup("#CholesterolPercentLabel");
        sodiumTot = (Label) Main.root.lookup("#SodiumMilligramsLabel");
        sodiumGoal = (Label) Main.root.lookup("#SodiumPercentLabel");
        carbsTot = (Label) Main.root.lookup("#TotalCarbohydratesGramsLabel");
        carbsGoal = (Label) Main.root.lookup("#TotalCarbohydratesPercentLabel");
        sugarsTot = (Label) Main.root.lookup("#SugarsGramsLabel");
        sugarsGoal = (Label) Main.root.lookup("#SugarsPercentLabel");
        fiberTot = (Label) Main.root.lookup("#DietaryFiberGramsLabel");
        fiberGoal = (Label) Main.root.lookup("#DietaryFiberPercentLabel");
        proteinTot = (Label) Main.root.lookup("#ProteinGramsLabel");
        proteinGoal = (Label) Main.root.lookup("#ProteinPercentLabel");
        calciumGoal= (Label) Main.root.lookup("#CalciumPercentLabel");
        potassiumGoal= (Label) Main.root.lookup("#PotassiumPercentLabel");
        ironGoal= (Label) Main.root.lookup("#IronPercentLabel");
        zincGoal= (Label) Main.root.lookup("#ZincPercentLabel");
        vitAGoal= (Label) Main.root.lookup("#VitaminAPercentLabel");
        vitB6Goal= (Label) Main.root.lookup("#VitaminB6PercentLabel");
        vitB12Goal= (Label) Main.root.lookup("#VitaminB12PercentLabel");
        vitCGoal= (Label) Main.root.lookup("#VitaminCPercentLabel");
        vitDGoal= (Label) Main.root.lookup("#VitaminDPercentLabel");
        vitEGoal= (Label) Main.root.lookup("#VitaminEPercentLabel");

    }

    private void updateNutritionLabel(String newFocus) {
        int rowIndex = model.nameToRowIndex.get(newFocus);
        ArrayList<Double> values = model.table.get(rowIndex);
        ArrayList<String> strings = model.stringTable.get(rowIndex);
        servingSize.setText(strings.get(3));

        double calories = values.get(0), totalFat=values.get(1), satFat = values.get(2),
                sodium = values.get(4), cholesterol = values.get(3),  carbs = values.get(5),
                sugars = values.get(6), fiber = values.get(7), protein = values.get(8),
                calcium = values.get(9), potassium = values.get(10), iron = values.get(11),
                spelter = values.get(12), vitA= values.get(13), vitB6 = values.get(14),
                vitB12 = values.get(15), vitC = values.get(16), vitD = values.get(17),
                vitE = values.get(18);

        caloriesTot.setText(unitFormat.format(calories));
        double calPercent = calories/model.getCalorieGoal();
        double tfPercent = totalFat/model.getTotalFatGoal();
        double tfGoal = model.getTotalFatGoal();
        caloriesGoal.setText(percentageFormat.format(calories/model.getCalorieGoal()));
        totalFatTot.setText(unitFormat.format(totalFat));
        totalFatGoal.setText(percentageFormat.format(totalFat/model.getTotalFatGoal()));
        saturatedFatTot.setText(unitFormat.format(satFat));
        saturatedFatGoal.setText(percentageFormat.format(satFat/model.getSaturatedFatGoal()));
        cholesterolTot.setText(unitFormat.format(cholesterol));
        cholesterolGoal.setText(percentageFormat.format(cholesterol/model.getCholesterolGoal()));
        sodiumTot.setText(unitFormat.format(sodium));
        sodiumGoal.setText(percentageFormat.format(sodium/model.getSodiumGoal()));
        carbsTot.setText(unitFormat.format(carbs));
        carbsGoal.setText(percentageFormat.format(carbs/model.getCarbohydratesGoal()));
        sugarsTot.setText(unitFormat.format(sugars));
        sugarsGoal.setText(percentageFormat.format(sugars/model.getSugarGoal()));
        fiberTot.setText(unitFormat.format(fiber));
        fiberGoal.setText(percentageFormat.format(fiber/model.getDietaryFiberGoal()));
        proteinTot.setText(unitFormat.format(protein));
        proteinGoal.setText(percentageFormat.format(protein/model.getProteinGoal()));
        calciumGoal.setText(percentageFormat.format(calcium/model.getCalciumGoal()));
        potassiumGoal.setText(percentageFormat.format(potassium/model.getPotassiumGoal()));
        ironGoal.setText(percentageFormat.format(iron/model.getIronGoal()));
        zincGoal.setText(percentageFormat.format(spelter/model.getZincGoal()));
        vitAGoal.setText(percentageFormat.format(vitA/model.getVitaminAGoal()));
        vitB6Goal.setText(percentageFormat.format(vitB6/model.getVitaminB6Goal()));
        vitB12Goal.setText(percentageFormat.format(vitB12/model.getVitaminB12Goal()));
        vitCGoal.setText(percentageFormat.format(vitC/model.getVitaminCGoal()));
        vitDGoal.setText(percentageFormat.format(vitD/model.getVitaminDGoal()));
        vitEGoal.setText(percentageFormat.format(vitE/model.getVitaminEGoal()));



    }


    public void initComboBoxes(){
       Hashtable<String, ArrayList<String>> categories= model.getCategoryToItemsList();
        category = (ComboBox<String>) Main.root.lookup("#categoryCombo");
        food = (ComboBox<String>)Main.root.lookup("#foodCombo");
        gender= (ComboBox<String>)Main.root.lookup("#genderCombo");
        activity= (ComboBox<String>)Main.root.lookup("#activityCombo");
            if (category == null || food == null || gender == null || activity == null){
                System.err.println("Error building scene.");
                System.exit(-1);
            }

        categoryList = new ArrayList<String>();
        for(String x : categories.keySet())categoryList.add(x);
        ArrayList<String> temp = model.getCategoryToItemsList().get(categoryList.get(0));
        prepComboBox(food, temp);
        prepComboBox(category, categoryList);
        prepComboBox(gender, genderList);
        prepComboBox(activity, activityList);
        new AutocompleteComboBoxListener(food);
        new AutocompleteComboBoxListener(category);
        for (int i = 0; i < 5; i++) {
            prepComboBox(trackNutrient[i], model.getNutrients());
            trackNutrient[i].setValue(model.getNutrients().get(i));
        }
        food.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.err.println("SelectedItemChanged from " +oldValue + " to " + newValue);
                selectedItem = newValue;
                updateNutritionLabel(selectedItem);
            }
        });
    }

    public void makeBreakfastFocus(Event event) {//This is a change
        focus = breakfastList;
        updateFocusedItem();
    }


    public void makeLunchFocus(Event event) {
        focus = lunchList;
        updateFocusedItem();
    }

    public void makeDinnerFocus(Event e) {
        focus = dinnerList;
        updateFocusedItem();
    }

        private void updateFocusedItem() {
            String tempItem = focus.getSelectionModel().getSelectedItem();
            if(tempItem != null){
                focusedItem = tempItem;
                clearAllButton.setDisable(false);
            }
            updateNutritionLabel(focusedItem);

            removeItemButton.setDisable(tempItem == null);

        }






    public void addToDinner(ActionEvent actionEvent) {
        System.err.println("Dinner Stuff");

        if(selectedItem.compareTo("none")!=0) {
            dinnerList.getItems().add(selectedItem);
            model.addSelectedItem(selectedItem, 2);
        }
    }

    public void addToLunch(ActionEvent actionEvent) {
        System.err.println("Lunch Stuff");
        if(selectedItem.compareTo("none")!=0) {
            lunchList.getItems().add(selectedItem);
            model.addSelectedItem(selectedItem, 1);
        }
    }

    public void addToBreakfast(ActionEvent actionEvent) {//Official change by Anthony
        System.err.println("BreakfastStuff");

        if(selectedItem.compareTo("none")!=0) {
            breakfastList.getItems().add(selectedItem);
            model.addSelectedItem(selectedItem, 0);
        }
    }

    public void printLabels(ActionEvent actionEvent) {//Now Sofy added this change
       model.print();
    }

    public void removeAllItems(ActionEvent actionEvent) {
        //Take the items out of the model:
        for (String x: breakfastList.getItems())
            model.deleteSelectedItem(x, 0);
        for(String x: lunchList.getItems()) model.deleteSelectedItem(x, 1);
        for(String x: dinnerList.getItems()) model.deleteSelectedItem(x, 2);
        //Take the items out of the controller
        breakfastList.getItems().clear();
        lunchList.getItems().clear();
        dinnerList.getItems().clear();


    }


    public void removeFocusedItem(ActionEvent actionEvent) {
        String itemToRemove = focus.getSelectionModel().getSelectedItem();
        int meal = 2;
        if(focus == breakfastList) meal = 0;
        else if(focus == lunchList) meal = 1;
        //else meal = 2)
        model.deleteSelectedItem(itemToRemove, meal);
        focus.getItems().remove(itemToRemove);


    }

    @Override
    ///This is the method that updates info based on the model
    public void update(Observable o, Object arg) {
        for (int i = 0; i < 5; i++) {
            changeBoxAndAssigns(i);
            }
        updateGoalBoxes();

    }
    ///This is the method that runs the nutrientCalculator in the model
    public void calculateStuff(ActionEvent actionEvent) {

        double age=0, height=0, weight=0;
        try {
            age = Double.parseDouble(ageBox.getText());
            height = Double.parseDouble(heightBox.getText());
            weight = Double.parseDouble(weightBox.getText());
        }
        catch(Exception ignored){}  String genderString, activityLevel;
        genderString = gender.getSelectionModel().getSelectedItem();
        activityLevel = activity.getSelectionModel().getSelectedItem();

        model.updateNutrientCalcBasedonPersonalDetails((int)age, (int)height, (int)weight, genderString, activityLevel);
        updateGoalBoxes();
        ageBox.setText(unitFormat.format(age));
        weightBox.setText(unitFormat.format(weight));
        heightBox.setText(unitFormat.format(height));
    }

    private void updateGoalBoxes() {
        for (int i = 0; i < 5; i++) {
            String nutrient = trackNutrient[i].getValue().toString();
            double goal = model.nutrients.getGoal(nutrient);
            goalBox[i].setText(unitFormat.format(goal));
            changeBoxAndAssigns(i);
        }
    }

    private void changeBoxAndAssigns(int i) {
        String nutrient = trackNutrient[i].getValue().toString();
        int column = model.labelToIndex.get(nutrient);
        double goal = model.nutrients.getGoal(nutrient);
        double total = model.totalNutrients.get(column);
        double percent = total/goal;
        warningLabels[i].setVisible(total>goal);
        nutrientLabels[i].setText(nutrient);
        progressBars[i].setProgress(percent);
        progressTexts[i].setText(percentageFormat.format(percent));
        goalBox[i].setText(unitFormat.format(goal));
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

    public void selectItemFromFood(ActionEvent actionEvent) {
    }

    public void changeCategory(ActionEvent actionEvent) {
        ArrayList<String> list = model.getCategoryToItemsList().get(category.getValue().toString());
        prepComboBox(food, list);
    }

    public void selectText(Event event) {
        ComboBox<String> box = (ComboBox<String>)event.getTarget();
        box.getSelectionModel().select(0);

    }

    public void updateGoalFromBox(int i){
        double newGoal = Double.parseDouble(goalBox[i].getText());
        String nutrient = trackNutrient[i].getValue().toString();
        model.nutrients.setGoal(nutrient, newGoal);
        changeBoxAndAssigns(i);

    }
    public void updateGoal1(ActionEvent actionEvent) {
        updateGoalFromBox(0);
    }
    public void updateGoal2(ActionEvent actionEvent) {
        updateGoalFromBox(1);
    }
    public void updateGoal3(ActionEvent actionEvent) {
        updateGoalFromBox(2);
    }
    public void updateGoal4(ActionEvent actionEvent) {
        updateGoalFromBox(3);
    }
    public void updateGoal5(ActionEvent actionEvent) {
        updateGoalFromBox(4);
    }
}
