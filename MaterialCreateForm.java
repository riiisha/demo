package org.glove.app.ui;

import org.glove.app.entity.MaterialEntity;
import org.glove.app.manager.MaterialEntityManager;
import org.glove.app.util.BaseForm;
import org.glove.app.util.CustomTableModel;
import org.glove.app.util.DialogUtil;

import javax.swing.*;
import java.sql.SQLException;

public class MaterialCreateForm extends BaseForm {
    private JTextField titleField;
    private JTextField typeMaterialField;
    private JTextField imagePathField;
    private JTextField costField;
    private JTextField countInStockField;
    private JTextField minCountField;
    private JSpinner countInPackSpinner;
    private JTextField unitField;
    private JPanel mainPanel;
    private JButton backButton;
    private JButton saveButton;

    CustomTableModel<MaterialEntity> model;

    public MaterialCreateForm() {
        super(1200, 800);

        setContentPane(mainPanel);

        initButton();
        setVisible(true);
    }

    private void initButton() {

        backButton.addActionListener(e -> {
            dispose();
            new MaterialTableForm();
        });

        saveButton.addActionListener(e -> {


            String title = titleField.getText();

            String typeMaterial = typeMaterialField.getText();

            String imagePath = imagePathField.getText();

            String unit = unitField.getText();

            double cost = -1;
            try {
                cost = Double.parseDouble(costField.getText());

            } catch (Exception ex){
                DialogUtil.showError(this, "Стоимость введена неверно");
                return;
            }
            if(cost< 0){
                DialogUtil.showError(this, "Стоимость введена неверно");
                return;
            }


            double countInStock = -1;
            try {
                countInStock = Double.parseDouble(countInStockField.getText());

            } catch (Exception ex){
                DialogUtil.showError(this, "Количество на складе введено неверно");
                return;
            }
            if(countInStock< 0){
                DialogUtil.showError(this, "Количество на складе введено неверно");
                return;
            }



            double minCount = -1;
            try {
                minCount = Double.parseDouble(minCountField.getText());

            } catch (Exception ex){
                DialogUtil.showError(this, "Количество на складе введено неверно");
                return;
            }
            if(minCount< 0){
                DialogUtil.showError(this, "Количество на складе введено неверно");
                return;
            }



            int countInPack = (int) countInPackSpinner.getValue();
            if(countInPack < 0){
                DialogUtil.showError(this, "Количество в упаковке введено неверно");
                return;
            }

            MaterialEntity material = new MaterialEntity(
                    title,
                    typeMaterial,
                    imagePath,
                    cost,
                    countInStock,
                    minCount,
                    countInPack,
                    unit
            );

            try {
                MaterialEntityManager.insert(material);
                DialogUtil.showInfo(this, "Запись успешно добавлена");

                dispose();
                new MaterialTableForm();
            } catch (SQLException ex) {
                ex.printStackTrace();
                DialogUtil.showError(this, "Ошибка сохранения данных");
            }


        });
    }
}


