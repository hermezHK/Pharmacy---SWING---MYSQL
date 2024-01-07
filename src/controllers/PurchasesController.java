/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;
import static models.EmployeesDao.rol_user;
import models.Products;
import models.ProductsDao;
import models.Purchases;
import models.PurchasesDao;
import views.SystemView;

/**
 *
 * @author akira
 */
public class PurchasesController implements KeyListener{
    private Purchases purchase;
    private PurchasesDao purchaseDao;
    private SystemView views;
    
    //instance model product
    Products product = new Products();
    ProductsDao productDao = new ProductsDao();
    
    String rol = rol_user;

    public PurchasesController(Purchases purchase, PurchasesDao purchaseDao, SystemView views) {
        this.purchase = purchase;
        this.purchaseDao = purchaseDao;
        this.views = views;
        
        this.views.txt_purchase_product_code.addKeyListener(this);
        this.views.txt_purchase_price.addKeyListener(this);
    }
    
    //KeyListener
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
         if(e.getSource() == views.txt_purchase_product_code){
             if(e.getKeyCode() == KeyEvent.VK_ENTER){
                 if(views.txt_purchase_product_code.getText().equals("")){
                     JOptionPane.showMessageDialog(null, "Enter the code of the product to buy");
                 }else {
                     int id = Integer.parseInt(views.txt_purchase_product_code.getText());
                     product = productDao.searchCode(id);
                     views.txt_purchase_product_name.setText(product.getName());
                     views.txt_purchase_id.setText(""+ product.getId());
                     views.txt_purchase_amount.requestFocus();
                 }
             }
         }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getSource() == views.txt_purchase_price){
            int quantity;
            double price = Double.parseDouble(views.txt_purchase_price.getText());
            price = 0.0;
            
            if(views.txt_purchase_amount.getText().equals("")){
                quantity = 1;
                views.txt_purchase_price.setText(""+ price);
            }else {
                quantity = Integer.parseInt(views.txt_purchase_amount.getText());
                price = Double.parseDouble(views.txt_purchase_price.getText());
                views.txt_purchase_subtotal.setText("" + quantity * price);
            }
        }
    }
    
    
}
