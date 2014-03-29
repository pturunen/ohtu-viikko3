package ohtu.data_access;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import ohtu.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;


public class FileUserDao implements UserDao {

    private List<User> users;
    private File userfile;
    private String pathname;
    private Scanner tiedosto;
    private PrintWriter tiedostoK;
    
    public FileUserDao(String pathname) throws FileNotFoundException {
        users = new ArrayList<User>();
        if (!pathname.isEmpty()){
            this.pathname = pathname;
            this.userfile = new File(this.pathname);
            if (this.userfile.exists()){
                this.tiedosto = new Scanner(this.userfile);
                readFromFile();
            }
        }
    }        
    
    private void storeToFile(User user) throws FileNotFoundException {
            tiedostoK = new PrintWriter(new FileOutputStream(this.userfile,true));
            tiedostoK.println(user.getUsername()+" "+user.getPassword());
            tiedostoK.close();
    }

    private void readFromFile(){
        String tempName;
        String tempWord;
        if (this.tiedosto != null){
            while (tiedosto.hasNext()) {
                tempName = tiedosto.next();
                //System.out.println("tiedoston token="+tiedosto.next());
                if (this.tiedosto.hasNext()){
                   tempWord = tiedosto.next();
                   users.add(new User(tempName,tempWord));
               }
            }
        }
    }
    
    
    @Override
    public List<User> listAll() {
        return users;
    }

    @Override
    public User findByName(String name) {
        for (User user : users) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }
        return null;
    }

    /**
     *
     * @param user
     */
    @Override
    public void add(User user) {
        users.add(user);
        try {
            this.storeToFile(user);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
