package application;

import java.util.ArrayList;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/*
 * The control class of the pizza order system
 * Yue Cai 14yc26 10168332
 */
public class PizzaOrderSystemController {
    
    @FXML
    private ChoiceBox<String> size = new ChoiceBox<>();
    
    @FXML
    private ChoiceBox<Integer> cheese = new ChoiceBox<>();
    
    @FXML
    private ChoiceBox<Integer> pepperoni = new ChoiceBox<>();
    
    @FXML
    private ChoiceBox<Integer> ham = new ChoiceBox<>();
    
    private ObservableList<String> sizeList = FXCollections.observableArrayList(
    		"small", "medium", "large");
    
    private ObservableList<Integer> cheeseList = FXCollections.observableArrayList(
    		1, 2, 3);

    private ObservableList<Integer> pepperoniList = FXCollections.observableArrayList(
    		0, 1, 2, 3);
    
    private ObservableList<Integer> hamList = FXCollections.observableArrayList(
    		0, 1, 2, 3);

    private int quantityNum = 1;
    
    @FXML
    private TextField quantity;
    
    @FXML
    private TextField perItemCost = new TextField("$ 8.5");
    
    @FXML
    private TextField totalItemCost = new TextField("$ 8.5");
    
    @FXML
    private TextArea lineOrder;
    
    private ArrayList<LineItem> pizzas = new ArrayList<>();
    
    // This method creates a warning alert that tells the user he has selected too much meat.
    @FXML
    void warningAction() {
    	Alert info = new Alert(AlertType.WARNING);
    	info.setTitle("Warning Alert");
    	info.setHeaderText("Too Much Meet!");
    	info.setContentText("You can't have more than three meat!");
    	info.showAndWait();
    }
    
    // The "Add to Order" button is used to save the current pizza configuration and quantity as a line item
    // and displays the current order to a textArea control.
    @FXML
    void myButtonAction(ActionEvent event) {
    	if (quantity.getText() != null) {
    		try {
    			Pizza pizza = new Pizza(size.getValue(), cheese.getValue(), ham.getValue(), pepperoni.getValue());
    			LineItem order = new LineItem(quantityNum, pizza);
    			pizzas.add(order);
    		} catch (IllegalPizza e) {
    			e.printStackTrace();
    		}
    		LineItem[] pizzaArray = pizzas.toArray(new LineItem[0]);
    		String out = "Your order: \r\n";
    		float cost = 0;
    		for (LineItem p : pizzaArray) {
    			out += p.toString();
    			cost += p.getCost();
    		}
    		out += "Total Cost: $ " + cost;
    		lineOrder.setText(out);
    	}
    }

    @FXML
    void initialize() {
    	quantity.setText("1");
    	size.setValue("small");
    	size.setItems(sizeList);
    	cheese.setValue(1);
    	cheese.setItems(cheeseList);
    	pepperoni.setValue(1);
    	pepperoni.setItems(pepperoniList);
    	ham.setValue(0);
    	ham.setItems(hamList);
    	// The listener for the size choicebox.
    	size.valueProperty().addListener((observableValue, oldVal, newVal) ->
    	{
			try {
				Pizza pizza = new Pizza(newVal, cheese.getValue(), ham.getValue(), pepperoni.getValue());
				LineItem order = new LineItem(quantityNum, pizza);
		    	// displays the per item cost and the total item cost to the textfield.
	    		perItemCost.setText("$ " + pizza.getCost());
	    		totalItemCost.setText("$ " + order.getCost());
			} catch (IllegalPizza e) {
				e.printStackTrace();
			}
    	});
    	// The listener for the cheese choicebox.
    	cheese.valueProperty().addListener((observableValue, oldVal, newVal) ->
    	{
			try {
				Pizza pizza = new Pizza(size.getValue(), newVal, ham.getValue(), pepperoni.getValue());
				LineItem order = new LineItem(quantityNum, pizza);
	    		perItemCost.setText("$ " + pizza.getCost());
	    		totalItemCost.setText("$ " + order.getCost());
			} catch (IllegalPizza e) {
				e.printStackTrace();
			}
    	});
    	// The listener for the pepperoni choicebox.
    	pepperoni.valueProperty().addListener((observableValue, oldVal, newVal) ->
    	{
    		// if the user selects too much meat
    		if (newVal + ham.getValue() > 3) {
    			warningAction();
    			pepperoni.setValue(oldVal);
    		}
    		else {
    			try {
    				Pizza pizza = new Pizza(size.getValue(), cheese.getValue(), ham.getValue(), newVal);
    				LineItem order = new LineItem(quantityNum, pizza);
    				perItemCost.setText("$ " + pizza.getCost());
    				totalItemCost.setText("$ " + order.getCost());
    			} catch (IllegalPizza e) {
    				e.printStackTrace();
    			}
    		}
    	});
    	// The listener for the size choicebox.
    	ham.valueProperty().addListener((observableValue, oldVal, newVal) ->
    	{
    		// if the user selects too much meat
    		if (newVal + pepperoni.getValue() > 3) {
    			warningAction();
    			ham.setValue(oldVal);
    		}
    		else {
    			try {
    				Pizza pizza = new Pizza(size.getValue(), cheese.getValue(), newVal, pepperoni.getValue());
    				LineItem order = new LineItem(quantityNum, pizza);
    	    		perItemCost.setText("$ " + pizza.getCost());
    	    		totalItemCost.setText("$ " + order.getCost());
    			} catch (IllegalPizza e) {
    				e.printStackTrace();
    			}
    		}
    	});
    	// change the totalItemCost when the user changes the quantity of the current pizza object.
    	quantity.textProperty().addListener((observableValue,oldText, newText) -> 
    	{
    		if (newText != null && !newText.isEmpty()) {
    			try {
    				// the input should be an integer.
    				int a = Integer.parseInt(newText);
    				// the inut integer should between 1 to 100.
    				if (a > 100) {
    					((StringProperty) observableValue).setValue("100");
    					quantityNum = 100;
    				}
    				else {
    					quantityNum = a;
    					Pizza pizza = new Pizza(size.getValue(), cheese.getValue(), ham.getValue(), pepperoni.getValue());
    					LineItem order = new LineItem(quantityNum, pizza);
    					totalItemCost.setText("$ " + order.getCost());
    				}
    			} catch (NumberFormatException e) {
    				((StringProperty) observableValue).setValue(oldText);
				} catch (IllegalPizza e) {
					e.printStackTrace();
				}
    		}
    	});
    }
}
