package org.glove.app.ui;

import org.glove.app.entity.MaterialEntity;
import org.glove.app.manager.MaterialEntityManager;
import org.glove.app.util.BaseForm;
import org.glove.app.util.DialogUtil;

import javax.swing.*;
import java.sql.SQLException;

public class MaterialEditForm extends BaseForm {
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
    private JTextField idField;
    private JButton deleteButton;

    private MaterialEntity material;

    public MaterialEditForm(MaterialEntity material) {
        super(1200, 800);
        this.material = material;
        setContentPane(mainPanel);
        initFields();
        initButton();
        setVisible(true);
    }


    private void initFields() {
        idField.setEditable(false);
        idField.setText(String.valueOf(material.getId()));
        titleField.setText(material.getTitle());
        typeMaterialField.setText(material.getMaterialType());

        imagePathField.setText(material.getImagePath());
        costField.setText(String.valueOf(material.getCost()));
        countInStockField.setText(String.valueOf(material.getCountInStock()));
        minCountField.setText(String.valueOf(material.getMinCount()));
        countInPackSpinner.setValue(material.getCountInPack());
        unitField.setText(material.getUnit());


    }

    private void initButton() {

        deleteButton.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Вы точно хотите удалить запись?", "Подтверждение", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    MaterialEntityManager.delete(material);
                    DialogUtil.showInfo(this, "Запись успешно удалена");
                    dispose();
                    new MaterialTableForm();
                } catch (SQLException ex) {
                    DialogUtil.showError(this, "Произошла ошибка удаления");
                }
            }
        });

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

            } catch (Exception ex) {
                DialogUtil.showError(this, "Стоимость введена неверно");
                return;
            }
            if (cost < 0) {
                DialogUtil.showError(this, "Стоимость введена неверно");
                return;
            }


            double countInStock = -1;
            try {
                countInStock = Double.parseDouble(countInStockField.getText());

            } catch (Exception ex) {
                DialogUtil.showError(this, "Количество на складе введено неверно");
                return;
            }
            if (countInStock < 0) {
                DialogUtil.showError(this, "Количество на складе введено неверно");
                return;
            }


            double minCount = -1;
            try {
                minCount = Double.parseDouble(minCountField.getText());

            } catch (Exception ex) {
                DialogUtil.showError(this, "Количество на складе введено неверно");
                return;
            }
            if (minCount < 0) {
                DialogUtil.showError(this, "Количество на складе введено неверно");
                return;
            }


            int countInPack = (int) countInPackSpinner.getValue();
            if (countInPack < 0) {
                DialogUtil.showError(this, "Количество в упаковке введено неверно");
                return;
            }

            material.setTitle(title);
            material.setMaterialType(typeMaterial);
            material.setImagePath(imagePath);
            material.setCost(cost);
            material.setCountInStock(countInStock);
            material.setMinCount(minCount);
            material.setCountInPack(countInPack);
            material.setUnit(unit);

            try {
                MaterialEntityManager.update(material);
                DialogUtil.showInfo(this, "Запись успешно отредактирована");

                dispose();
                new MaterialTableForm();
            } catch (SQLException ex) {
                ex.printStackTrace();
                DialogUtil.showError(this, "Ошибка сохранения данных");
            }



        });
    }
}


