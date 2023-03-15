package moviereservation;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import javax.swing.JPasswordField;

/**
 *
 * @author umsang-uk
 */
public class PasswordText extends JPasswordField {
    
    private String defaultText;
    public String password;

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
        this.setText(defaultText);
    }
    
    private void checkEmptyValue() {
        setPassword(new String(this.getPassword()));
        if (password.trim().length() == 0){
            setText(defaultText);
        }
        return;
    }

    public void setPassword(String _password) {
        this.password = _password;
    }
    
    
}
