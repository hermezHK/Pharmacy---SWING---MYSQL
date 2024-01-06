/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import models.DynamicComboBox;
import static models.EmployeesDao.rol_user;
import models.Products;
import models.ProductsDao;
import views.SystemView;

/**
 *
 * @author akira
 */
public class ProductsController implements ActionListener {

    private Products product;
    private ProductsDao productDao;
    private SystemView views;
    String rol = rol_user;

    public ProductsController(Products product, ProductsDao productDao, SystemView views) {
        this.product = product;
        this.productDao = productDao;
        this.views = views;

        //label listening
        this.views.btn_register_product.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_product) {
            if (views.txt_product_code.getText().equals("")
                    || views.txt_product_name.getText().equals("")
                    || views.txt_product_description.getText().equals("")
                    || views.txt_product_unit_price.getText().equals("")
                    || views.cmb_product_category.getSelectedItem().toString().equals("")) {

                JOptionPane.showMessageDialog(null, "All fields are required");
            } else {
                product.setCode(Integer.parseInt(views.txt_product_code.getText()));
                product.setName(views.txt_product_name.getText().trim());
                product.setDescription(views.txt_product_description.getText().trim());
                product.setUnit_price(Double.parseDouble(views.txt_product_unit_price.getText()));
                
                DynamicComboBox category_id = (DynamicComboBox) views.cmb_product_category.getSelectedItem();
                product.setCategory_id(category_id.getId());
                
                if(productDao.registerProductQuery(product)){
                    JOptionPane.showMessageDialog(null, "successfully registered product");
                }else{
                    JOptionPane.showMessageDialog(null, "An error occurred while registering the product");
                }
            }
        }
    }

}
