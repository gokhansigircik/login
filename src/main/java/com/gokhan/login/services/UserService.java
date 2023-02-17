package com.gokhan.login.services;

import java.util.Optional;
    
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
    
import com.gokhan.login.models.LoginUser;
import com.gokhan.login.models.User;
import com.gokhan.login.repositories.UserRepository;
    
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepo;
    
    // TO-DO: Write register and login methods!
    public User register(User newUser, BindingResult result) {
        Optional<User> potentialUser = userRepo.findByEmail(newUser.getEmail());
        if(potentialUser.isPresent()){
            result.rejectValue("email", "emailExist", "The Email already exist!");
        }
        if(!newUser.getPassword().equals(newUser.getConfirm())) {
            result.rejectValue("confirm", "Matches", "The Confirm Password must match Password!");
        }
        if(result.hasErrors()){
            return null;
        }else{
            String hashed = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
            newUser.setPassword(hashed);
            return userRepo.save(newUser);
        }
    }
    public User login(LoginUser newLoginObject, BindingResult result) {
        Optional<User> potentialUser = userRepo.findByEmail(newLoginObject.getEmail());
        if(potentialUser.isEmpty()){
            result.rejectValue("email", "notFound", "User can not be found!");
        }else{
            User user = potentialUser.get();
            if(!BCrypt.checkpw(newLoginObject.getPassword(), user.getPassword())) {
                result.rejectValue("password", "Matches", "Invalid Password!");
            }
        }

        if(result.hasErrors()){
            return null;
        }else{
            User user = potentialUser.get();
            return user;
        }
    }
    public User getById(Long id){
        Optional<User> potentialUser = userRepo.findById(id);
        if(potentialUser.isEmpty()){
            return null;
        }else{
            return potentialUser.get();
        }
    }
}

