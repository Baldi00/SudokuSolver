/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokusolver;

/**
 *
 * @author Andrea
 */
public class AlreadySetValueException extends Exception {

    /**
     * Creates a new instance of <code>AlreadySetValueException</code> without
     * detail message.
     */
    public AlreadySetValueException() {
    }

    /**
     * Constructs an instance of <code>AlreadySetValueException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AlreadySetValueException(String msg) {
        super(msg);
    }
}
