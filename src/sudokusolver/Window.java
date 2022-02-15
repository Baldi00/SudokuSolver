/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokusolver;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author Andrea
 */
public class Window extends JFrame{
    
    private JTextField [][] cells;
    private JButton executeFast, executeSlow, rollback, reset, example;
    private JPanel primaryPanel, tablePanel, buttonsPanel;
    
    public Window(){
        super();
        cells = new JTextField[9][9];
        primaryPanel = new JPanel(new BorderLayout());
        tablePanel = new JPanel(new GridLayout(9,9));
        buttonsPanel = new JPanel(new GridLayout(5,1));
        executeFast = new JButton("Execute Fast");
        executeSlow = new JButton("Execute Slow");
        rollback = new JButton("Rollback");
        reset = new JButton("Reset");
        example = new JButton("Example");
        
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                cells[i][j] = new JTextField(1);
                cells[i][j].setFont(new Font(cells[i][j].getFont().getFamily(), Font.PLAIN, 30));
                cells[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
                cells[i][j].setHorizontalAlignment(JTextField.CENTER);
                cells[i][j].setForeground(Color.black);
                tablePanel.add(cells[i][j]);
            }
        }
        
        for(int i=3; i<9; i+=3){
            for(int j=0; j<9; j++){
                cells[i][j].setBorder(BorderFactory.createMatteBorder(3, 1, 1, 1, Color.black));
                cells[i-1][j].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, Color.black));
                cells[j][i].setBorder(BorderFactory.createMatteBorder(1, 3, 1, 1, Color.black));
                cells[j][i-1].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 3, Color.black));
            }
        }
        cells[2][2].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.black));
        cells[2][3].setBorder(BorderFactory.createMatteBorder(1, 3, 3, 1, Color.black));
        cells[3][2].setBorder(BorderFactory.createMatteBorder(3, 1, 1, 3, Color.black));
        cells[3][3].setBorder(BorderFactory.createMatteBorder(3, 3, 1, 1, Color.black));
        cells[2][5].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.black));
        cells[3][5].setBorder(BorderFactory.createMatteBorder(3, 1, 1, 3, Color.black));
        cells[2][6].setBorder(BorderFactory.createMatteBorder(1, 3, 3, 1, Color.black));
        cells[3][6].setBorder(BorderFactory.createMatteBorder(3, 3, 1, 1, Color.black));
        cells[5][2].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.black));
        cells[5][3].setBorder(BorderFactory.createMatteBorder(1, 3, 3, 1, Color.black));
        cells[6][2].setBorder(BorderFactory.createMatteBorder(3, 1, 1, 3, Color.black));
        cells[6][3].setBorder(BorderFactory.createMatteBorder(3, 3, 1, 1, Color.black));
        cells[5][5].setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.black));
        cells[5][6].setBorder(BorderFactory.createMatteBorder(1, 3, 3, 1, Color.black));
        cells[6][5].setBorder(BorderFactory.createMatteBorder(3, 1, 1, 3, Color.black));
        cells[6][6].setBorder(BorderFactory.createMatteBorder(3, 3, 1, 1, Color.black));
        
        cells[5][6].requestFocusInWindow();
        
        rollback.setEnabled(false);
        
        buttonsPanel.add(executeFast);
        buttonsPanel.add(executeSlow);
        buttonsPanel.add(rollback);
        buttonsPanel.add(reset);
        buttonsPanel.add(example);
        
        primaryPanel.add(buttonsPanel, BorderLayout.NORTH);
        primaryPanel.add(tablePanel, BorderLayout.CENTER);
        
        executeFast.addActionListener((ActionEvent ae) -> {
            Table actualTable = new Table();
            for(int i=0; i<9; i++){
                for(int j=0; j<9; j++){
                    if(!cells[i][j].getText().equals("")){
                        try {
                            actualTable.setCell(i, j, Integer.valueOf(cells[i][j].getText()));
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "There are some numbers in a wrong position", "Dialog", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
            }
            
            Solver s = new Solver(actualTable);
            try {
                s.solve(false);
                Table solution = s.getSolution();
                if(solution == null){
                    JOptionPane.showMessageDialog(null, "No solution found. Check if numbers are in correct positions", "Dialog", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                for(int i=0; i<9; i++){
                    for(int j=0; j<9; j++){
                        cells[i][j].setEditable(false);
                        if(solution.getCell(i, j) != null && cells[i][j].getText().equals("")){
                            cells[i][j].setText(solution.getCell(i, j).toString());
                            cells[i][j].setForeground(Color.blue);
                        }
                    }
                }
            } catch (NotAdmissibleValueException | AlreadySetValueException ex) {}
            
            rollback.setEnabled(true);
            executeFast.setEnabled(false);
            executeSlow.setEnabled(false);
        });
        
        executeSlow.addActionListener((ActionEvent ae) -> {
            Table actualTable = new Table();
            for(int i=0; i<9; i++){
                for(int j=0; j<9; j++){
                    if(!cells[i][j].getText().equals("")){
                        try {
                            actualTable.setCell(i, j, Integer.valueOf(cells[i][j].getText()));
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "There are some numbers in a wrong position", "Dialog", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
            }
            
            Solver s = new Solver(actualTable);
            try {
                s.solve(true);
                List<Table> path = s.getCompletePath();
                Table solution = s.getSolution();
                if(solution == null){
                    JOptionPane.showMessageDialog(null, "No solution found. Check if numbers are in correct positions", "Dialog", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                for(int i=0; i<9; i++){
                    for(int j=0; j<9; j++){
                        cells[i][j].setEditable(false);
                        if(solution.getCell(i, j) != null && cells[i][j].getText().equals("")){
                            cells[i][j].setForeground(Color.blue);
                        }
                    }
                }
                
                for(Table t : path){
                    for(int i=0; i<9; i++){
                        for(int j=0; j<9; j++){
                            if(t.getCell(i, j) == null){
                                cells[i][j].setText("");
                                cells[i][j].paintImmediately(0, 0, 200, 200);
                            }else{
                                cells[i][j].setText(t.getCell(i, j).toString());
                                cells[i][j].paintImmediately(0, 0, 200, 200);
                            }
                        }
                        try {
                            Thread.sleep(7);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                
                
                for(int i=0; i<9; i++){
                    for(int j=0; j<9; j++){
                        if(solution.getCell(i, j) != null && cells[i][j].getText().equals("")){
                            cells[i][j].setText(solution.getCell(i, j).toString());
                        }
                    }
                }
            } catch (NotAdmissibleValueException | AlreadySetValueException ex) {}
            
            rollback.setEnabled(true);
            executeFast.setEnabled(false);
            executeSlow.setEnabled(false);
        });
        
        rollback.addActionListener((ActionEvent ae) -> {
            for(int i=0; i<9; i++){
                for(int j=0; j<9; j++){
                    if(cells[i][j].getForeground().equals(Color.BLUE)){
                        cells[i][j].setText("");
                    }
                    cells[i][j].setEditable(true);
                    cells[i][j].setForeground(Color.black);
                }
            }
            rollback.setEnabled(false);
            executeFast.setEnabled(true);
            executeSlow.setEnabled(true);
        });
        
        reset.addActionListener((ActionEvent ae) -> {
            for(int i=0; i<9; i++){
                for(int j=0; j<9; j++){
                    cells[i][j].setText("");
                    cells[i][j].setForeground(Color.black);
                    cells[i][j].setEditable(true);
                }
            }
            rollback.setEnabled(false);
            executeFast.setEnabled(true);
            executeSlow.setEnabled(true);
        });
        
        example.addActionListener((ActionEvent ae) -> {
            for(int i=0; i<9; i++){
                for(int j=0; j<9; j++){
                    cells[i][j].setText("");
                    cells[i][j].setForeground(Color.black);
                    cells[i][j].setEditable(true);
                }
            }
            cells[0][2].setText(String.valueOf(6));
            cells[0][3].setText(String.valueOf(9));
            cells[0][5].setText(String.valueOf(1));
            cells[0][6].setText(String.valueOf(2));
            cells[1][1].setText(String.valueOf(2));
            cells[1][3].setText(String.valueOf(3));
            cells[1][5].setText(String.valueOf(4));
            cells[1][7].setText(String.valueOf(7));
            cells[2][0].setText(String.valueOf(1));
            cells[2][4].setText(String.valueOf(7));
            cells[2][8].setText(String.valueOf(8));
            cells[3][0].setText(String.valueOf(4));
            cells[3][1].setText(String.valueOf(6));
            cells[3][7].setText(String.valueOf(2));
            cells[3][8].setText(String.valueOf(5));
            cells[4][2].setText(String.valueOf(3));
            cells[4][6].setText(String.valueOf(7));
            cells[5][0].setText(String.valueOf(7));
            cells[5][1].setText(String.valueOf(9));
            cells[5][7].setText(String.valueOf(6));
            cells[5][8].setText(String.valueOf(4));
            cells[6][0].setText(String.valueOf(6));
            cells[6][4].setText(String.valueOf(3));
            cells[6][8].setText(String.valueOf(7));
            cells[7][1].setText(String.valueOf(4));
            cells[7][3].setText(String.valueOf(2));
            cells[7][5].setText(String.valueOf(9));
            cells[7][7].setText(String.valueOf(8));
            cells[8][2].setText(String.valueOf(8));
            cells[8][3].setText(String.valueOf(7));
            cells[8][5].setText(String.valueOf(6));
            cells[8][6].setText(String.valueOf(4));
            
            rollback.setEnabled(false);
            executeFast.setEnabled(true);
            executeSlow.setEnabled(true);
        });
        
        add(primaryPanel);
        setSize(500, 650);
        setTitle("Sudoku Solver");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
