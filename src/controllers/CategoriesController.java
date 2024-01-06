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
import models.Categories;
import models.CategoriesDao;
import models.DynamicComboBox;
import static models.EmployeesDao.rol_user;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import views.SystemView;

/**
 *
 * @author akira
 */
public class CategoriesController implements ActionListener, MouseListener, KeyListener {

    private Categories category;
    private CategoriesDao categoryDao;
    private SystemView views;
    String rol = rol_user;

    DefaultTableModel model = new DefaultTableModel();

    public CategoriesController(Categories category, CategoriesDao categoryDao, SystemView views) {
        this.category = category;
        this.categoryDao = categoryDao;
        this.views = views;

        //button register category
        this.views.btn_register_category.addActionListener(this);

        //button modify category
        this.views.btn_update_category.addActionListener(this);

        //button delete category 
        this.views.btn_delete_category.addActionListener(this);
        
        //button cancel
        this.views.btn_cancel_category.addActionListener(this);

        //label listening
        this.views.categories_table.addMouseListener(this);
        this.views.txt_search_category.addKeyListener(this);
        this.views.jLabelCategories.addMouseListener(this);
        getCategoryName();
        AutoCompleteDecorator.decorate(views.cmb_product_category);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_category) {
            if (views.txt_category_name.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "All fields are required");
            } else {
                category.setName(views.txt_category_name.getText().trim());

                if (categoryDao.registerCategoryQuery(category)) {

                    cleanTable();
                    cleanFields();
                    listAllCategories();

                    JOptionPane.showMessageDialog(null, "successfully registered category");
                } else {
                    JOptionPane.showMessageDialog(null, "An error occurred while registering the category");
                }
            }
        } else if (e.getSource() == views.btn_update_category) {
            if (views.txt_category_id.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "select a row to continue");
            } else {
                if (views.txt_category_id.getText().equals("")
                        || views.txt_category_name.getText().equals("")) {

                    JOptionPane.showMessageDialog(null, "All fields are required");
                } else {
                    category.setId(Integer.parseInt(views.txt_category_id.getText()));
                    category.setName(views.txt_category_name.getText().trim());

                    if (categoryDao.updateCategoryQuery(category)) {

                        cleanTable();
                        cleanFields();
                        views.btn_register_category.setEnabled(true);
                        listAllCategories();
                        JOptionPane.showMessageDialog(null, "successfully updated category");
                    }
                }
            }
        } else if (e.getSource() == views.btn_delete_category) {
            int row = views.categories_table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "select an category to delete");
            } else {
                int id = Integer.parseInt(views.categories_table.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "Are you sure to eliminate the category?");

                if (question == 0 && categoryDao.deleteCategoryQuery(id) != false) {
                    cleanTable();
                    cleanFields();
                    views.btn_register_category.setEnabled(true);
                    listAllCategories();
                    JOptionPane.showMessageDialog(null, "category successfully deleted");

                }
            }
        }else if(e.getSource() == views.btn_cancel_category){
            cleanFields();
            views.btn_register_category.setEnabled(true);
        }
    }
    //list category

    public void listAllCategories() {
        if (rol.equals("Admin")) {
            List<Categories> list = categoryDao.listCategoriesQuery(views.txt_search_category.getText());
            model = (DefaultTableModel) views.categories_table.getModel();

            Object[] row = new Object[2];
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getName();
                model.addRow(row);
            }
            views.categories_table.setModel(model);
        }
    }

    //MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.categories_table) {
            int row = views.categories_table.rowAtPoint(e.getPoint());
            views.txt_category_id.setText(views.categories_table.getValueAt(row, 0).toString());
            views.txt_category_name.setText(views.categories_table.getValueAt(row, 1).toString());
            views.btn_register_category.setEnabled(false);
        }else if(e.getSource() == views.jLabelCategories){
            if(rol.equals("Admin")){
                views.jTabbedPane1.setSelectedIndex(6);
                cleanTable();
                cleanFields();
                listAllCategories();
            }else {
                views.jTabbedPane1.setEnabledAt(6, false);
                views.jLabelCategories.setEnabled(false);
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
        if (e.getSource() == views.txt_search_category) {
            //clean table
            cleanTable();
            //list category
            listAllCategories();
        }
    }

    //cleantable
    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    public void cleanFields() {
        views.txt_category_id.setText("");
        views.txt_category_name.setText("");

    }
    
    //method to display the name of the categories
    public void getCategoryName(){
        List<Categories> list = categoryDao.listCategoriesQuery(views.txt_search_category.getText());
        for(int i = 0; i < list.size(); i++){
            int id = list.get(i).getId();
            String name = list.get(i).getName();
            views.cmb_product_category.addItem(new DynamicComboBox(id, name));
        }
    }

}
