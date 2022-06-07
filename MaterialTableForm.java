package org.glove.app.ui;

import org.glove.app.entity.MaterialEntity;
import org.glove.app.manager.MaterialEntityManager;
import org.glove.app.util.BaseForm;
import org.glove.app.util.CustomTableModel;
import org.glove.app.util.DialogUtil;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MaterialTableForm extends BaseForm {
    private JTable table;
    private JButton costSortButton;
    private JComboBox costFilterBox;
    private JButton helpButton;
    private JButton connectButton;
    private JLabel rowCountLabel;
    private JPanel mainPanel;
    private JButton clearFilterButton;
    private JButton addButton;

    private CustomTableModel<MaterialEntity> model;

    private boolean costSort = false;

    public MaterialTableForm() {
        super(1200, 800);
        setContentPane(mainPanel);
        initTable();
        initBoxes();
        initButtons();
        setVisible(true);
    }


    public void initTable() {
        table.setRowHeight(70);

        try {
            model = new CustomTableModel<>(
                    MaterialEntity.class,
                    new String[]{
                            "id", "Наименование", "Тип", "путь до картинки", "цена", "количество на складе",
                            "минимальное количество","количество в упаковке", "единица измерения", "b"
                    },
                    MaterialEntityManager.selectAll()
            );
            table.setModel(model);

            TableColumn column1 = table.getColumn("id");
            column1.setMinWidth(0);
            column1.setMaxWidth(0);


            TableColumn column = table.getColumn("путь до картинки");
            column.setMinWidth(0);

            column.setMaxWidth(0);
            updateRowCountLabel(model.getRowCount(), model.getRowCount());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    int row = table.rowAtPoint(e.getPoint());
                    if(row!= -1){
                        dispose();
                        new MaterialEditForm(model.getRows().get(row));
                    }
                }
            }
        });

    }

    public void initBoxes() {
        costFilterBox.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED){
                applyFilter();
            }
        });
    }

    private void applyFilter() {
        try {
            List<MaterialEntity> list = MaterialEntityManager.selectAll();
            int max = list.size();

            switch (costFilterBox.getSelectedIndex()){
                case 1:
                    list.removeIf(s-> s.getCost()>= 5000);
                    break;
                case 2:
                    list.removeIf(s-> s.getCost()< 5000 || s.getCost() >= 15000);
                    break;
                case 3:
                    list.removeIf(s-> s.getCost()< 15000 || s.getCost() >= 50000);
                    break;
                case 4:
                    list.removeIf(s-> s.getCost()< 50000 || s.getCost() >= 100000);
                    break;
                case 5:
                    list.removeIf(s-> s.getCost()< 100000);
                    break;
            }
            model.setRows(list);
            model.fireTableDataChanged();
            updateRowCountLabel(list.size(),max);
        } catch (SQLException e) {
            DialogUtil.showError(null, "error");
        }
    }

    private void updateRowCountLabel(int actual, int max) {
        rowCountLabel.setText("Отображено записей: " + actual + "/" + max);
    }

    public void initButtons() {
        helpButton.addActionListener(e -> {
            DialogUtil.showInfo(null, "Вы можете просматривать, сортировать и фильтровать записи." +
                    "Также есть возможность сбросить все и посмотреть текущее и общее количество записей (:");
        });

        connectButton.addActionListener(e -> {
            DialogUtil.showInfo(null, "Связаться с разработчиком вы можете по почте");
        });

        clearFilterButton.addActionListener(e -> {
            costFilterBox.setSelectedIndex(0);

        });



        costSortButton.addActionListener(e -> {
            Collections.sort(model.getRows(), new Comparator<MaterialEntity>() {
                @Override
                public int compare(MaterialEntity o1, MaterialEntity o2) {
                    if(costSort){
                        return Double.compare(o2.getCost(), o1.getCost());
                    }else {
                        return Double.compare(o1.getCost(), o2.getCost());
                    }
                }
            });
            costSort = !costSort;
            model.fireTableDataChanged();
        });



        addButton.addActionListener(e -> {
            dispose();
            new MaterialCreateForm();
        });


    }
}
