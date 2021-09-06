package com.volvadvit.vaacation.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.volvadvit.vaacation.components.EmployeeEditor;
import com.volvadvit.vaacation.domain.Employee;
import com.volvadvit.vaacation.repo.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;

@Route
public class MainView extends VerticalLayout {

    private final EmployeeRepo employeeRepo;
    private final Grid<Employee> grid = new Grid<>(Employee.class);
    private final EmployeeEditor employeeEditor;

    private final TextField filter = new TextField("", "Type to filter");
    private final Button addNewEmployeeButton = new Button("Add new");
    private final HorizontalLayout toolBarLayout = new HorizontalLayout(filter, addNewEmployeeButton);

    @Autowired
    public MainView(EmployeeRepo employeeRepo, EmployeeEditor employeeEditor) {
        this.employeeEditor = employeeEditor;
        this.employeeRepo = employeeRepo;

        // Listeners
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showEmployee(e.getValue()));
        // Connect selected Employee to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            employeeEditor.editEmployee(e.getValue());
        });
        // Instantiate and edit new Employee the new button is clicked
        addNewEmployeeButton.addClickListener(e -> employeeEditor.editEmployee(new Employee()));
        // Listen changes made by the employeeEditor, refresh data from backend
        employeeEditor.setChangeHandler(() -> {
            employeeEditor.setVisible(false);
            showEmployee(filter.getValue());
        });


        add(toolBarLayout, grid, employeeEditor);
        showEmployee("");
    }

    private void showEmployee(String name) {
        if (name.isEmpty()) {
            grid.setItems(employeeRepo.findAll());
        } else {
            grid.setItems(employeeRepo.findByName(name));
        }
    }
}
