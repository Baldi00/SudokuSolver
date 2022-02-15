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
public class Solver {
    private final List<Table> steps;
    private final List<Table> stepsComplete;
    private final List<List<Integer>> domains;
    private final List<Coordinates> coordinates;
    private Table solution;
    
    public Solver(Table initialTable){
        steps = new ArrayList<>();
        steps.add(new Table(initialTable));
        
        stepsComplete = new ArrayList<>();
        stepsComplete.add(new Table(initialTable));
        
        domains = new ArrayList<>();
        coordinates = new ArrayList<>();
    }
    
    public void solve(boolean backupCompletePath) throws NotAdmissibleValueException, AlreadySetValueException{
        boolean backtrack = false;
        while(true){
            try{
                Table actual = steps.get(steps.size()-1);
                if(backtrack == false){
                    Coordinates c = actual.minimumRemainingValueDomain();       //Get the next cell to assign with MRV heuristic
                    if(c.i == 100 || actual.isThereEmptyDomains()){
                        steps.remove(steps.size()-1);
                        backtrack = true;
                        continue;
                    }
                    coordinates.add(c);
                    domains.add(new ArrayList<>(actual.getDomain(c)));          //Add the possible assignments for that cell into a list

                    steps.add(new Table(actual));
                    if(backupCompletePath) stepsComplete.add(new Table(actual));
                    
                    int valueToAdd = domains.get(domains.size()-1).get(0);      //Get the first value from the possible ones
                    steps.get(steps.size()-1).setCell(c.i, c.j, valueToAdd);    //Add it into the new table
                    domains.get(domains.size()-1).remove(0);                    //Remove this value from the possible ones
//                    steps.get(steps.size()-1).print(1, c);
                    if(steps.get(steps.size()-1).isComplete())
                        break;
                } else {
                    if(domains.get(domains.size()-1).isEmpty() || actual.isThereEmptyDomains()){
                        domains.remove(domains.size()-1);
                        coordinates.remove(coordinates.size()-1);
                        if(backupCompletePath) stepsComplete.add(steps.get(steps.size()-1));
                        steps.remove(steps.size()-1);
                    } else {
                        steps.add(new Table(actual));
                        if(backupCompletePath) stepsComplete.add(new Table(actual));
                        
                        int valueToAdd = domains.get(domains.size()-1).get(0);      //Get the first value from the possible ones
                        Coordinates c = coordinates.get(coordinates.size()-1);      //Get the coordinates referred to the previous cell
                        steps.get(steps.size()-1).setCell(c.i, c.j, valueToAdd);    //Add it into the new table
                        domains.get(domains.size()-1).remove(0);                    //Remove this value from the possible ones
//                        steps.get(steps.size()-1).print(1, c);
                        if(steps.get(steps.size()-1).isComplete())
                            break;
                        backtrack = false;
                    }
                }
            }catch(Exception e){
                System.out.println("No solution found");
                return;
            }
        }
        solution = steps.get(steps.size()-1);
    }
    
    public Table getSolution(){
        return solution;
    }
    
    public List<Table> getCompletePath(){
        return stepsComplete;
    }
}
