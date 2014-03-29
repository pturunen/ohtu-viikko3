package ohtu.services;

import ohtu.domain.User;
import java.util.ArrayList;
import java.util.List;
import ohtu.data_access.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {

    private UserDao userDao;
    private int minLengthForName = 3;
    private int minLengthForWord = 8;
    
    @Autowired
    public AuthenticationService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean logIn(String username, String password) {
        for (User user : userDao.listAll()) {
            if (user.getUsername().equals(username)
                    && user.getPassword().equals(password)) {
                return true;
            }
        }

        return false;
    }

    public boolean createUser(String username, String password) {
        if (userDao.findByName(username) != null) {
            return false;
        }

        if (invalid(username, password)) {
            return false;
        }

        userDao.add(new User(username, password));

        return true;
    }

    private boolean invalid(String username, String password) {
        // validity check of username and password
        boolean specialFound = false;
        String aakkoset = "abcdefghijklmnopqrstuvxyz";
        for(int i=0;i<username.length();i++){
            if(!aakkoset.contains(Character.toString(username.charAt(i)))){
                return true;
            }
        }
        for(int j=0;j<password.length() && !specialFound;j++){
            if (!Character.isAlphabetic(password.charAt(j)) ){
                specialFound = true;
            }
        }
        
        if (username.length()< minLengthForName 
                || password.length() < minLengthForWord 
                || !specialFound){
            return true;
        }
        return false;
    }
}
