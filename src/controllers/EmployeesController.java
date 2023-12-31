/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.Employees;
import models.EmployeesDao;
import static models.EmployeesDao.id_user;
import static models.EmployeesDao.rol_user;
import views.SystemView;

/**
 *
 * @author akira
 */
public class EmployeesController implements ActionListener, MouseListener, KeyListener {

    private Employees employee;
    private EmployeesDao employeeDao;
    private SystemView views;

    //role
    String rol = rol_user;
    DefaultTableModel model = new DefaultTableModel();

    public EmployeesController(Employees employee, EmployeesDao employeeDao, SystemView views) {
        this.employee = employee;
        this.employeeDao = employeeDao;
        this.views = views;

        //buttom register employee
        this.views.btn_register_employee.addActionListener(this);

        //button modify employee
        this.views.btn_update_employee.addActionListener(this);

        //button detele employee
        this.views.btn_delete_employee.addActionListener(this);

        //button cancel employee
        this.views.btn_cancel_employee.addActionListener(this);
        
        //change password button employee
        this.views.btn_modify_data.addActionListener(this);
        
        //label listening
        this.views.jLabelEmployees.addMouseListener(this);

        this.views.employees_table.addMouseListener(this);
        this.views.txt_search_employee.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_employee) {
            //check if the fields are empty
            if (views.txt_employee_id.getText().equals("")
                    || views.txt_employee_fullname.getText().equals("")
                    || views.txt_employee_username.getText().equals("")
                    || views.txt_employee_address.getText().equals("")
                    || views.txt_employee_telephone.getText().equals("")
                    || views.txt_employee_email.getText().equals("")
                    || views.cmb_rol.getSelectedItem().toString().equals("")
                    || String.valueOf(views.txt_employee_password.getPassword()).equals("")) {

                JOptionPane.showMessageDialog(null, "All fields are required");
            } else {
                //perform the insertion
                employee.setId(Integer.parseInt(views.txt_employee_id.getText().trim()));
                employee.setFull_name(views.txt_employee_fullname.getText().trim());
                employee.setUsername(views.txt_employee_username.getText().trim());
                employee.setAddress(views.txt_employee_address.getText().trim());
                employee.setTelephone(views.txt_employee_telephone.getText().trim());
                employee.setEmail(views.txt_employee_email.getText().trim());
                employee.setPassword(String.valueOf(views.txt_employee_password.getPassword()));
                employee.setRol(views.cmb_rol.getSelectedItem().toString());

                if (employeeDao.registerEmployeeQuery(employee)) {

                    cleanTable();
                    cleanFields();
                    listAllEmployees();

                    JOptionPane.showMessageDialog(null, "successfully registered employee");
                } else {
                    JOptionPane.showMessageDialog(null, "An error occurred while registering the employee");
                }
            }
        } else if (e.getSource() == views.btn_update_employee) {
            if (views.txt_employee_id.equals("")) {
                JOptionPane.showMessageDialog(null, "select a row to continue");
            } else {
                //check if the fields are empty
                if (views.txt_employee_id.getText().equals("")
                        || views.txt_employee_fullname.getText().equals("")
                        || views.cmb_rol.getSelectedItem().toString().equals("")) {

                    JOptionPane.showMessageDialog(null, "All fields are required");

                } else {
                    employee.setId(Integer.parseInt(views.txt_employee_id.getText().trim()));
                    employee.setFull_name(views.txt_employee_fullname.getText().trim());
                    employee.setUsername(views.txt_employee_username.getText().trim());
                    employee.setAddress(views.txt_employee_address.getText().trim());
                    employee.setTelephone(views.txt_employee_telephone.getText().trim());
                    employee.setEmail(views.txt_employee_email.getText().trim());
                    employee.setPassword(String.valueOf(views.txt_employee_password.getPassword()));
                    employee.setRol(views.cmb_rol.getSelectedItem().toString());

                    if (employeeDao.updateEmployeeQuery(employee)) {

                        cleanTable();
                        cleanFields();
                        listAllEmployees();

                        views.btn_register_employee.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "employee data has been successfully modified");
                    } else {
                        JOptionPane.showMessageDialog(null, "An error occurred while modifying the employee");
                    }
                }
            }
        } else if (e.getSource() == views.btn_delete_employee) {
            int row = views.employees_table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(null, "select an employee to delete");
            } else if (views.employees_table.getValueAt(row, 0).equals(id_user)) {
                JOptionPane.showMessageDialog(null, "cannot delete authenticated user");
            } else {
                int id = Integer.parseInt(views.employees_table.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "Are you sure to eliminate the employee?");

                if (question == 0 && employeeDao.deleteEmployeeQuery(id) != false) {
                    cleanTable();
                    cleanFields();
                    views.btn_register_employee.setEnabled(true);
                    views.txt_employee_password.setEnabled(true);
                    listAllEmployees();
                    JOptionPane.showMessageDialog(null, "employee successfully deleted");

                }
            }
        }else if(e.getSource() == views.btn_cancel_employee){
            cleanFields();
            views.btn_register_employee.setEnabled(true);
            views.txt_employee_password.setEnabled(true);
            views.txt_employee_id.setEnabled(true);
            
        }else if(e.getSource() == views.btn_modify_data){
            //obtain information about password boxes
            String password = String.valueOf(views.txt_password_modify.getPassword());
            String confirm_password = String.valueOf(views.txt_password_modify_confirm.getPassword());
            
            //check if the text boxes are empty
            if(!password.equals("") && !confirm_password.equals("")){
                
                //check if passwords are the same
                if(password.equals(confirm_password)){
                    employee.setPassword(String.valueOf(views.txt_password_modify.getPassword()));
                    
                    if(employeeDao.updateEmployeePassword(employee) != false){
                        JOptionPane.showMessageDialog(null, "password modified successfully");
                    }else{
                        JOptionPane.showMessageDialog(null, "An error occurred while changing the password");
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "Passwords do not match");
                }
            }else {
                JOptionPane.showMessageDialog(null, "All fields are required");
            }
        }
    }

    //list all employees
    public void listAllEmployees() {
        if (rol.equals("Admin")) {
            List<Employees> list = employeeDao.listEmployeesQuery(views.txt_search_employee.getText());
            model = (DefaultTableModel) views.employees_table.getModel();
            Object[] row = new Object[7];
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getFull_name();
                row[2] = list.get(i).getUsername();
                row[3] = list.get(i).getAddress();
                row[4] = list.get(i).getTelephone();
                row[5] = list.get(i).getEmail();
                row[6] = list.get(i).getRol();
                model.addRow(row);
            }
        }
    }

    //MouseListener 
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.employees_table) {
            int row = views.employees_table.rowAtPoint(e.getPoint());

            views.txt_employee_id.setText(views.employees_table.getValueAt(row, 0).toString());
            views.txt_employee_fullname.setText(views.employees_table.getValueAt(row, 1).toString());
            views.txt_employee_username.setText(views.employees_table.getValueAt(row, 2).toString());
            views.txt_employee_address.setText(views.employees_table.getValueAt(row, 3).toString());
            views.txt_employee_telephone.setText(views.employees_table.getValueAt(row, 4).toString());
            views.txt_employee_email.setText(views.employees_table.getValueAt(row, 5).toString());
            views.cmb_rol.setSelectedItem(views.employees_table.getValueAt(row, 6).toString());

            //disable
            views.txt_employee_id.setEditable(false);
            views.txt_employee_password.setEnabled(false);
            views.btn_register_employee.setEnabled(false);
            
        }else if(e.getSource() == views.jLabelEmployees){
            if(rol.equals("Admin")){
                views.jTabbedPane1.setSelectedIndex(4);
                //clean table - fields - list employee
                cleanTable();
                cleanFields();
                listAllEmployees();
                
            }else {
                views.jTabbedPane1.setEnabledAt(4, false);
                views.jLabelEmployees.setEnabled(false);
                JOptionPane.showMessageDialog(null, "You do not have permission to access this view");
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    //KeyListener
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == views.txt_search_employee) {
            cleanTable();
            listAllEmployees();
        }
    }

    //cleanfields
    public void cleanFields() {
        views.txt_employee_id.setText("");
        views.txt_employee_id.setEditable(true);
        views.txt_employee_fullname.setText("");
        views.txt_employee_username.setText("");
        views.txt_employee_address.setText("");
        views.txt_employee_telephone.setText("");
        views.txt_employee_email.setText("");
        views.txt_employee_password.setText("");
        views.cmb_rol.setSelectedIndex(0);

    }

    //cleantable
    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

}
