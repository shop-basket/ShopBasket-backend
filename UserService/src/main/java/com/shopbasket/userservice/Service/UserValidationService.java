package com.shopbasket.userservice.Service;


import com.shopbasket.userservice.DTO.AuthenticationRequest;
import com.shopbasket.userservice.DTO.CustomerRegisterRequest;
import com.shopbasket.userservice.DTO.EmployeeCreationResponse;
import org.springframework.stereotype.Service;


@Service
public class UserValidationService extends CredentialValidation{
    public boolean employeeCredentialValidation(EmployeeCreationResponse employeeCreationResponse){
        boolean validInput = false;
//       email validation
        boolean validEmail = emailValidation(employeeCreationResponse.getEmail());

//      Phone number validation
        boolean validPhoneNo = phoneNoValidation(employeeCreationResponse.getPhoneNo());

        if(validEmail && validPhoneNo){
            validInput = true;
        }
        return validInput;
    }

    public boolean customerCredentialValidation(CustomerRegisterRequest request){
        boolean validInput = false;
//       email validation
        boolean validEmail = emailValidation(request.getEmail());

//      Phone number validation
        boolean validPhoneNo = phoneNoValidation(request.getPhoneNo());

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
