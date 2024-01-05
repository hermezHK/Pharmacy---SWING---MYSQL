/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import models.Categories;
import models.CategoriesDao;
import static models.EmployeesDao.rol_user;
import views.SystemView;

/**
 *
 * @author akira
 */
public class CategoriesController implements ActionListener{

    private Categories category;
    private CategoriesDao categoriesDao;
    private SystemView views;
    String rol = rol_user;

    public CategoriesController(Categories category, CategoriesDao categoriesDao, SystemView views) {
        this.category = category;
        this.categoriesDao = categoriesDao;
        this.views = views;
        
        //button register category
        this.views.btn_register_category.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == views.btn_register_category){
            if(views.txt_category_name.getText().equals("")){
                JOptionPane.showMessageDialog(null, "All fields are required");
            }else {
                category.setName(views.txt_category_name.getText().trim());
                
                if(categoriesDao.registerCategoryQuery(category)){
                    JOptionPane.showMessageDialog(null, "successfully registered category");
                }else {
                    JOptionPane.showMessageDialog(null, "An error occurred while registering the category");
                }
            }
        }
    }

}
