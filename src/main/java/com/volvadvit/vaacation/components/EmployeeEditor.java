package com.volvadvit.vaacation.components;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.volvadvit.vaacation.domain.Employee;
import com.volvadvit.vaacation.repo.EmployeeRepo;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class EmployeeEditor extends VerticalLayout implements KeyNotifier {

    private final EmployeeRepo employeeRepo;
    private Employee employee;

    @Autowired
    public EmployeeEditor(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;

        // bind entity fields with textFields
        add(firstName, lastName, actions);
        binder.bindInstanceFields(this);

        // Configure and style components
        setSpacing(true);
        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");
        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editEmployee(employee));
        setVisible(false);
    }

    void delete() {
        employeeRepo.delete(employee);
        changeHandler.onChange();
    }

    void save() {
        employeeRepo.save(employee);
        changeHandler.onChange();
    }


    /* Fields to edit properties in Customer entity */
    private final TextField firstName = new TextField("First name");
    private final TextField lastName = new TextField("Last name");

    /* Action buttons */
    private final Button save = new Button("Save", VaadinIcon.CHECK.create());
    private final Button cancel = new Button("Cancel");
    private final Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Employee> binder = new Binder<>(Employee.class);

    @Setter
    private ChangeHandler changeHandler;
    public interface ChangeHandler {
        void onChange();
    }

    public void editEmployee(Employee newEmployee) {
        if (newEmployee == null) {
            setVisible(false);
            return;
        }
        if (newEmployee.getId() != null) {
            this.employee = employeeRepo.findById(newEmployee.getId()).orElse(newEmployee);
        } else {
            this.employee = newEmployee;
        }

        binder.setBean(employee);
        setVisible(true);
        lastName.focus();
    }
}
