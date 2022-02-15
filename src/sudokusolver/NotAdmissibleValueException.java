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
public class NotAdmissibleValueException extends Exception {

    /**
     * Creates a new instance of <code>NotAdmissibleValueException</code>
     * without detail message.
     */
    public NotAdmissibleValueException() {
    }

    /**
     * Constructs an instance of <code>NotAdmissibleValueException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public NotAdmissibleValueException(String msg) {
        super(msg);
    }
}
