/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import static models.EmployeesDao.rol_user;
import models.Suppliers;
import models.SuppliersDao;
import views.SystemView;

/**
 *
 * @author akira
 */
public class SuppliersController implements ActionListener{
    private Suppliers supplier;
    private SuppliersDao supplierDao;
    private SystemView views;
    String rol = rol_user;

    public SuppliersController(Suppliers supplier, SuppliersDao supplierDao, SystemView views) {
        this.supplier = supplier;
        this.supplierDao = supplierDao;
        this.views = views;
        
        //button register supplier
        this.views.btn_register_supplier.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == views.btn_register_supplier){
            if(views.txt_supplier_name.getText().equals("")
                    || views.txt_supplier_description.getText().equals("")
                    || views.txt_supplier_address.getText().equals("")
                    || views.txt_supplier_telephone.getText().equals("")
                    || views.txt_supplier_email.getText().equals("")
                    || views.cmb_supplier_city.getSelectedItem().toString().equals("")){
                
                JOptionPane.showMessageDialog(null, "All fields are required");
                
            }else{
                //data insertion
                supplier.setName(views.txt_supplier_name.getText().trim());
                supplier.setDescription(views.txt_supplier_description.getText().trim());
                supplier.setAddress(views.txt_supplier_address.getText().trim());
                supplier.setTelephone(views.txt_supplier_telephone.getText().trim());
                supplier.setEmail(views.txt_supplier_email.getText().trim());
                supplier.setCity(views.cmb_supplier_city.getSelectedItem().toString());
                
                if(supplierDao.registerSupplierQuery(supplier)){
                    JOptionPane.showMessageDialog(null, "successfully registered supplier");
                }else {
                    JOptionPane.showMessageDialog(null, "An error occurred while registering supplier");
                }
                
            }
            
        }
    }
    
    
    
}
