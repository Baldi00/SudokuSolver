/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokusolver;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrea
 */
public class Table {
    private final List<List<List<Integer>>> domains;
    private final List<List<Integer>> variables;
    
    public Table(){
        List<Integer> defaultDomain = new ArrayList<>(9);
        defaultDomain.add(1);
        defaultDomain.add(2);
        defaultDomain.add(3);
        defaultDomain.add(4);
        defaultDomain.add(5);
        defaultDomain.add(6);
        defaultDomain.add(7);
        defaultDomain.add(8);
        defaultDomain.add(9);
        
        domains = new ArrayList<>(9);
        for(int i=0; i<9; i++){
            List<List<Integer>> temp = new ArrayList<>(9);
            for(int j=0; j<9; j++){
                temp.add(new ArrayList<>(defaultDomain));
            }
            domains.add(temp);
        }
        
        variables = new ArrayList<>(9);
        for(int i=0; i<9; i++){
            List<Integer> temp = new ArrayList<>(9);
            for(int j=0; j<9; j++){
                temp.add(null);
            }
            variables.add(temp);
        }
    }
    
    public Table(Table t){
        domains = new ArrayList<>(9);
        for(int i=0; i<9; i++){
            List<List<Integer>> temp = new ArrayList<>(9);
            for(int j=0; j<9; j++){
                temp.add(new ArrayList<>(t.domains.get(i).get(j)));
            }
            domains.add(temp);
        }
        
        variables = new ArrayList<>(9);
        for(int i=0; i<9; i++){
            List<Integer> temp = new ArrayList<>(9);
            for(int j=0; j<9; j++){
                temp.add(t.getCell(i,j));
            }
            variables.add(temp);
        }
    }
    
    public void setCell(int i, int j, int value) throws NotAdmissibleValueException, AlreadySetValueException{
        if(getCell(i,j) != null){
            throw new AlreadySetValueException();
        }
        
        if(!domains.get(i).get(j).contains(value)){
            throw new NotAdmissibleValueException();
        }
        
        variables.get(i).set(j, value);
        domains.get(i).get(j).clear();
        
        correctDomains(i, j, value);
    }
    
    private void correctDomains(int i, int j, int value){
        for(int k=0; k<9; k++){
            domains.get(i).get(k).remove(Integer.valueOf(value));
            domains.get(k).get(j).remove(Integer.valueOf(value));
        }
        
        int initialRow, initialCol;
        
        if(i<3)
            initialRow = 0;
        else if(i<6)
            initialRow = 3;
        else
            initialRow = 6;
        
        if(j<3)
            initialCol = 0;
        else if(j<6)
            initialCol = 3;
        else
            initialCol = 6;
        
        for(int k=initialRow; k<initialRow+3; k++){
            for(int l=initialCol; l<initialCol+3; l++){
                domains.get(k).get(l).remove(Integer.valueOf(value));
            }
        }
    }
    
    public void print(int mode, Coordinates c){
        System.out.println("-------------");
        for(int i=0; i<9; i+=3){
            for(int j=0; j<9; j+=3){
                System.out.print("|");
                printCell(getCell(i,j),i,j,c);
                printCell(getCell(i,j+1),i,j+1,c);
                printCell(getCell(i,j+2),i,j+2,c);
            }
            System.out.println("|");
            for(int j=0; j<9; j+=3){
                System.out.print("|");
                printCell(getCell(i+1,j),i+1,j,c);
                printCell(getCell(i+1,j+1),i+1,j+1,c);
                printCell(getCell(i+1,j+2),i+1,j+2,c);
            }
            System.out.println("|");
            for(int j=0; j<9; j+=3){
                System.out.print("|");
                printCell(getCell(i+2,j),i+2,j,c);
                printCell(getCell(i+2,j+1),i+2,j+1,c);
                printCell(getCell(i+2,j+2),i+2,j+2,c);
            }
            System.out.println("|");
            System.out.println("-------------");
        }
        
        if(mode == 1){
            for(int i=0; i<9; i++){
                for(int j=0; j<9; j++){
                    if(getCell(i,j) == null){
                        System.out.print("["+i+"]["+j+"]: ");
                        System.out.println(domains.get(i).get(j));
                    }
                }
            }
        }
    }
    
    private void printCell(Integer value, int i, int j, Coordinates c){
        if(value!=null){
            if(c!=null && c.i == i && c.j == j)
                System.out.print("\033[31;1m" + value + "\033[0m");
            else
                System.out.print(value);
        }else{
            System.out.print(" ");
        }
    }
    
    public boolean isComplete(){
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(getCell(i,j) == null)
                    return false;
            }
        }
        return true;
    }
    
    public Coordinates minimumRemainingValueDomain(){
        Coordinates c = new Coordinates();
        c.i = c.j = 100;
        int min = 100;
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(!domains.get(i).get(j).isEmpty() && domains.get(i).get(j).size() < min){
                    min = domains.get(i).get(j).size();
                    c.i = i;
                    c.j = j;
                    if(min==1){
                        return c;
                    }
                }
            }
        }
        return c;
    }
    
    public List<Integer> getDomain(Coordinates c){
        return domains.get(c.i).get(c.j);
    }
    
    public boolean isThereEmptyDomains(){
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(getCell(i,j) == null && domains.get(i).get(j).isEmpty())
                    return true;
            }
        }
        return false;
    }
    
    public Integer getCell(int i, int j){
        return variables.get(i).get(j);
    }
}
