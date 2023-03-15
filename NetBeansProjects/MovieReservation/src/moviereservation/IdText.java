package moviereservation;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import javax.swing.JTextField;

/**
 *
 * @author umsang-uk
 */
public class IdText extends JTextField {
    
    private String defaultText;

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
        this.setText(defaultText);
    }
    
    private void checkEmptyValue() {
        if (getText().trim().length() == 0){
            setText(defaultText);
        }
        return;
    }
    
}
