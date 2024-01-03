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
import models.Customers;
import models.CustomersDao;
import views.SystemView;

/**
 *
 * @author akira
 */
public class CustomersController implements ActionListener, MouseListener, KeyListener {

    private Customers customer;
    private CustomersDao customerDao;
    private SystemView views;

    DefaultTableModel model = new DefaultTableModel();

    public CustomersController(Customers customer, CustomersDao customerDao, SystemView views) {
        this.customer = customer;
        this.customerDao = customerDao;
        this.views = views;

        //button register customer
        this.views.btn_register_customer.addActionListener(this);

        //button modify customer
        this.views.btn_update_customer.addActionListener(this);

        //button dellete customer
        this.views.btn_delete_customer.addActionListener(this);

        //button cancel customer
        this.views.btn_cancel_customer.addActionListener(this);

        //Search button customer
        this.views.txt_search_customer.addKeyListener(this);

        //label listening
        this.views.jLabelCustomers.addMouseListener(this);

        this.views.customers_table.addMouseListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_customer) {

            //check if the fields are empty
            if (views.txt_customer_id.getText().equals("")
                    || views.txt_customer_fullname.getText().equals("")
                    || views.txt_customer_address.getText().equals("")
                    || views.txt_customer_telephone.getText().equals("")
                    || views.txt_customer_email.getText().equals("")) {

                JOptionPane.showMessageDialog(null, "All fields are required");
            } else {
                customer.setId(Integer.parseInt(views.txt_customer_id.getText().trim()));
                customer.setFull_name(views.txt_customer_fullname.getText().trim());
                customer.setAddress(views.txt_customer_address.getText().trim());
                customer.setTelephone(views.txt_customer_telephone.getText().trim());
                customer.setEmail(views.txt_customer_email.getText().trim());

                if (customerDao.registerCustomerQuery(customer)) {

                    cleanTable();
                    listAllCustomers();

                    JOptionPane.showMessageDialog(null, "successfully registered customer");
                } else {
                    JOptionPane.showMessageDialog(null, "An error occurred while registering client");
                }
            }
        } else if (e.getSource() == views.btn_update_customer) {
            if (views.txt_customer_id.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "select a row to continue");

            } else {
                if (views.txt_customer_id.getText().equals("")
                        || views.txt_customer_fullname.getText().equals("")
                        || views.txt_customer_address.getText().equals("")
                        || views.txt_customer_telephone.getText().equals("")
                        || views.txt_customer_email.getText().equals("")) {

                    JOptionPane.showMessageDialog(null, "All fields are required");

                } else {
                    customer.setId(Integer.parseInt(views.txt_customer_id.getText().trim()));
                    customer.setFull_name(views.txt_customer_fullname.getText().trim());
                    customer.setAddress(views.txt_customer_address.getText().trim());
                    customer.setTelephone(views.txt_customer_telephone.getText().trim());
                    customer.setEmail(views.txt_customer_email.getText().trim());

                    if (customerDao.updateCustomerQuery(customer)) {

                        cleanTable();
                        cleanFields();
                        listAllCustomers();

                        views.btn_register_customer.setEnabled(true);

                        JOptionPane.showMessageDialog(null, "data modified successfully");
                    } else {
                        JOptionPane.showMessageDialog(null, "An error occurred while modifying the client");
                    }
                }
            }
        } else if (e.getSource() == views.btn_delete_customer) {
            int row = views.customers_table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "select a customer to delete");
            } else {
                int id = Integer.parseInt(views.customers_table.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "Are you sure to eliminate the customer?");

                if (question == 0 && customerDao.deleteCustomerQuery(id) != false) {
                    cleanTable();
                    cleanFields();

                    views.btn_register_customer.setEnabled(true);

                    listAllCustomers();

                    JOptionPane.showMessageDialog(null, "customer successfully deleted");

                }
            }
        } else if (e.getSource() == views.btn_cancel_customer) {
            views.btn_register_customer.setEnabled(true);

            cleanFields();
        }
    }

    //list customers
    public void listAllCustomers() {
        List<Customers> list = customerDao.listCustomerQuery(views.txt_search_customer.getText());
        model = (DefaultTableModel) views.customers_table.getModel();

        Object[] row = new Object[5];
        for (int i = 0; i < list.size(); i++) {

            row[0] = list.get(i).getId();
            row[1] = list.get(i).getFull_name();
            row[2] = list.get(i).getAddress();
            row[3] = list.get(i).getTelephone();
            row[4] = list.get(i).getEmail();
            model.addRow(row);

        }
        views.customers_table.setModel(model);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.customers_table) {
            int row = views.customers_table.rowAtPoint(e.getPoint());
            views.txt_customer_id.setText(views.customers_table.getValueAt(row, 0).toString());
            views.txt_customer_fullname.setText(views.customers_table.getValueAt(row, 1).toString());
            views.txt_customer_address.setText(views.customers_table.getValueAt(row, 2).toString());
            views.txt_customer_telephone.setText(views.customers_table.getValueAt(row, 3).toString());
            views.txt_customer_email.setText(views.customers_table.getValueAt(row, 4).toString());

            //disable buttons
            views.btn_register_customer.setEnabled(false);
            views.txt_customer_id.setEditable(false);
            
        }else if(e.getSource() == views.jLabelCustomers){
            views.jTabbedPane1.setSelectedIndex(3);
            
            //clean table
            cleanTable();
            
            //cleanfields
            cleanFields();
            
            //list customer
            listAllCustomers();
        }
    }

    //MouseListener
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
        if (e.getSource() == views.txt_search_customer) {
            //clean table - list customer 
            cleanTable();
            listAllCustomers();
        }
    }

    //cleanfields
    public void cleanFields() {
        views.txt_customer_id.setText("");
        views.txt_customer_id.setEditable(true);
        views.txt_customer_fullname.setText("");
        views.txt_customer_address.setText("");
        views.txt_customer_telephone.setText("");
        views.txt_customer_email.setText("");
    }

    //cleantable
    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }

    }

}
