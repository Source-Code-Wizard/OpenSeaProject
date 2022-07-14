package com.example.webapplication.User;

public class UserNotFoundException extends RuntimeException{
    private String userName;

    public UserNotFoundException(String _userName){
        super(String.format("user: %s is not registered in the data base!",_userName));
        this.userName=_userName;
    }
    public String getUserName(){
        return userName;
    }

}
