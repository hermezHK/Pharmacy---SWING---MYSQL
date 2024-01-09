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
import models.DynamicComboBox;
import static models.EmployeesDao.rol_user;
import models.Suppliers;
import models.SuppliersDao;
import views.SystemView;

/**
 *
 * @author akira
 */
public class SuppliersController implements ActionListener, MouseListener, KeyListener {

    private Suppliers supplier;
    private SuppliersDao supplierDao;
    private SystemView views;
    String rol = rol_user;

    DefaultTableModel model = new DefaultTableModel();

    public SuppliersController(Suppliers supplier, SuppliersDao supplierDao, SystemView views) {
        this.supplier = supplier;
        this.supplierDao = supplierDao;
        this.views = views;

        //button register supplier
        this.views.btn_register_supplier.addActionListener(this);

        //button modify supplier
        this.views.btn_update_supplier.addActionListener(this);

        //button delete supplier
        this.views.btn_delete_supplier.addActionListener(this);

        //button cancel supplier
        this.views.btn_cancel_supplier.addActionListener(this);

        //label listening
        this.views.suppliers_table.addMouseListener(this);
        this.views.txt_search_supplier.addKeyListener(this);
        this.views.jLabelSuppliers.addMouseListener(this);
        getSupplierName();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_supplier) {
            if (views.txt_supplier_name.getText().equals("")
                    || views.txt_supplier_description.getText().equals("")
                    || views.txt_supplier_address.getText().equals("")
                    || views.txt_supplier_telephone.getText().equals("")
                    || views.txt_supplier_email.getText().equals("")
                    || views.cmb_supplier_city.getSelectedItem().toString().equals("")) {

                JOptionPane.showMessageDialog(null, "All fields are required");

            } else {
                //data insertion
                supplier.setName(views.txt_supplier_name.getText().trim());
                supplier.setDescription(views.txt_supplier_description.getText().trim());
                supplier.setAddress(views.txt_supplier_address.getText().trim());
                supplier.setTelephone(views.txt_supplier_telephone.getText().trim());
                supplier.setEmail(views.txt_supplier_email.getText().trim());
                supplier.setCity(views.cmb_supplier_city.getSelectedItem().toString());

                if (supplierDao.registerSupplierQuery(supplier)) {

                    cleanTable();
                    cleanFields();
                    listAllSupplier();

                    JOptionPane.showMessageDialog(null, "successfully registered supplier");
                } else {
                    JOptionPane.showMessageDialog(null, "An error occurred while registering supplier");
                }

            }

        } else if (e.getSource() == views.btn_update_supplier) {
            if (views.txt_supplier_id.equals("")) {
                JOptionPane.showMessageDialog(null, "select a row to continue");
            } else {

                if (views.txt_supplier_name.getText().equals("")
                        || views.txt_supplier_address.getText().equals("")
                        || views.txt_supplier_telephone.getText().equals("")
                        || views.txt_supplier_email.getText().equals("")) {

                    JOptionPane.showMessageDialog(null, "All fields are required");
                } else {
                    supplier.setName(views.txt_supplier_name.getText().trim());
                    supplier.setDescription(views.txt_supplier_description.getText().trim());
                    supplier.setAddress(views.txt_supplier_address.getText().trim());
                    supplier.setTelephone(views.txt_supplier_telephone.getText().trim());
                    supplier.setEmail(views.txt_supplier_email.getText().trim());
                    supplier.setCity(views.cmb_supplier_city.getSelectedItem().toString());
                    supplier.setId(Integer.parseInt(views.txt_supplier_id.getText()));

                    if (supplierDao.updateSupplierQuery(supplier)) {

                        //clean table
                        cleanTable();
                        //clean fields
                        cleanFields();
                        //list supplier
                        listAllSupplier();
                        views.btn_register_supplier.setEnabled(true);

                        JOptionPane.showMessageDialog(null, "successfully registered supplier");
                    } else {
                        JOptionPane.showMessageDialog(null, "An error occurred while modifying the supplier");
                    }
                }
            }
        } else if (e.getSource() == views.btn_delete_supplier) {
            int row = views.suppliers_table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "select an supplier to delete");
            } else {
                int id = Integer.parseInt(views.suppliers_table.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "Are you sure to eliminate the supplier?");
                if (question == 0 && supplierDao.deleteSupplierQuery(id) != false) {

                    //clean table
                    cleanTable();
                    //clean fields
                    cleanFields();
                    //list supplier
                    listAllSupplier();

                    JOptionPane.showMessageDialog(null, "supplier successfully deleted");
                }
            }
        } else if (e.getSource() == views.btn_cancel_supplier) {
            cleanFields();
            views.btn_register_supplier.setEnabled(true);
        }
    }

    //list supplier
    public void listAllSupplier() {
        if (rol.equals("Admin")) {
            List<Suppliers> list = supplierDao.listSuppliersQuery(views.txt_search_supplier.getText());
            model = (DefaultTableModel) views.suppliers_table.getModel();
            Object[] row = new Object[7];
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getName();
                row[2] = list.get(i).getDescription();
                row[3] = list.get(i).getAddress();
                row[4] = list.get(i).getTelephone();
                row[5] = list.get(i).getEmail();
                row[6] = list.get(i).getCity();
                model.addRow(row);
            }
            views.suppliers_table.setModel(model);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.suppliers_table) {
            int row = views.suppliers_table.rowAtPoint(e.getPoint());
            views.txt_supplier_id.setText(views.suppliers_table.getValueAt(row, 0).toString());
            views.txt_supplier_name.setText(views.suppliers_table.getValueAt(row, 1).toString());
            views.txt_supplier_description.setText(views.suppliers_table.getValueAt(row, 2).toString());
            views.txt_supplier_address.setText(views.suppliers_table.getValueAt(row, 3).toString());
            views.txt_supplier_telephone.setText(views.suppliers_table.getValueAt(row, 4).toString());
            views.txt_supplier_email.setText(views.suppliers_table.getValueAt(row, 5).toString());
            views.cmb_supplier_city.setSelectedItem(views.suppliers_table.getValueAt(row, 6).toString());

            //disable button
            views.btn_register_supplier.setEnabled(false);
            views.txt_supplier_id.setEditable(false);

        } else if (e.getSource() == views.jLabelSuppliers) {
            if (rol.equals("Admin")) {
                views.jTabbedPane1.setSelectedIndex(5);

                //clean table
                cleanTable();
                //clean fields
                cleanFields();
                //list supplier
                listAllSupplier();

            } else {
                views.jTabbedPane1.setEnabledAt(5, false);
                views.jLabelSuppliers.setEnabled(false);
                JOptionPane.showMessageDialog(null, "You do not have permission to access this view");
            }
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
        if (e.getSource() == views.txt_search_supplier) {
            //clean table
            cleanTable();

            //list supplier
            listAllSupplier();
        }
    }

    //cleantable
    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }

    }

    //clean fields
    public void cleanFields() {
        views.txt_supplier_id.setText("");
        views.txt_supplier_id.setEditable(true);
        views.txt_supplier_name.setText("");
        views.txt_supplier_description.setText("");
        views.txt_supplier_address.setText("");
        views.txt_supplier_telephone.setText("");
        views.txt_supplier_email.setText("");
        views.cmb_supplier_city.setSelectedIndex(0);
    }

    //method to display the name of the supplier
    public void getSupplierName() {
        List<Suppliers> list = supplierDao.listSuppliersQuery(views.txt_search_supplier.getText());
        for (int i = 0; i < list.size(); i++) {
            int id = list.get(i).getId();
            String name = list.get(i).getName();
            views.cmb_purchase_supplier.addItem(new DynamicComboBox(id, name));
        } 
    }
}
