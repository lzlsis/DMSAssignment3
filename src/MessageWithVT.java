
import java.io.Serializable;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Andy Li
 */
public class MessageWithVT implements Serializable {

    private static final long serialVersionUID = 123456789L;
    private HashMap<String, Integer> vt;
    private String name;
    private String message;

    public MessageWithVT(String name, String message) {
        vt = new HashMap<String, Integer>();
        this.name = name;
        this.message = message;
        vt.put(name, 0);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        UpdateVT();
    }

    public void UpdateVT() {
        // increate its own time by 1
        vt.put(name, vt.get(name) + 1);
    }

    public HashMap<String, Integer> getVT() {
        return vt;
    }

    public void UpdateVT(HashMap<String, Integer> other) {
        if (vt.keySet() != null || !vt.keySet().isEmpty()) {
            for (String os : other.keySet()) {
                if (vt.keySet().contains(os)) {
                    // for the record which is already in this VectorTimestamp
                    if (vt.get(os).compareTo(other.get(os)) > 0) {
                        // update the time of the process if time is greater than the current record
                        vt.put(os, other.get(os));
                    }
                } else {
                    // add the record which is no in this VectorTimestamp
                    vt.put(os, other.get(os));
                }
            }
        } else {
            for (String os : other.keySet()) {
                vt.put(os, other.get(os));
            }
        }
    }
}
