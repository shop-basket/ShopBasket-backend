package com.shopbasket.userservice.Service;


import com.shopbasket.userservice.DTO.AuthenticationRequest;
import com.shopbasket.userservice.DTO.EmployeeCreationResponse;
import com.shopbasket.userservice.DTO.RegisterRequest;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserValidationService extends CredentialValidation{
    public boolean employeeCredentialValidation(EmployeeCreationResponse employeeCreationResponse){
        boolean validInput = false;
//       email validation
        boolean validEmail = emailValidation(employeeCreationResponse.getEmail());

//      Phone number validation
        boolean validPhoneNo = phoneNoValidation(employeeCreationResponse.getPhoneNo());
        System.out.println("validEmail:  "+validEmail+" Valid phoneNo: "+validPhoneNo);
        if(validEmail && validPhoneNo){
            validInput = true;
        }
        return validInput;
    }

    public boolean customerCredentialValidation(RegisterRequest request){
        boolean validInput = false;
//       email validation
        boolean validEmail = emailValidation(request.getEmail());

//      Phone number validation
        boolean validPhoneNo = phoneNoValidation(request.getPhoneNo());
        System.out.println("validEmail:  "+validEmail+" Valid phoneNo: "+validPhoneNo);

        if(validEmail && validPhoneNo){
            validInput = true;
        }
        return validInput;
    }

    public boolean credentialsValidationRegister(AuthenticationRequest authenticationRequest){
        boolean validCredential = false;
//        email Validation
        boolean validEmail = emailValidation(authenticationRequest.getEmail());
//        passwordValidation
        boolean validPassword = passwordValidation(authenticationRequest.getPassword());

        if(validEmail && validPassword){
            validCredential = true;
        }
        return validCredential;
    }
}
