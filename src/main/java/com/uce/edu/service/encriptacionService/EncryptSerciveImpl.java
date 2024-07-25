package com.uce.edu.service.encriptacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptSerciveImpl implements IEncryptSercive{

@Autowired
private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String encriptar(String contraseniaSinEncriptar) {
        
        return bCryptPasswordEncoder.encode(contraseniaSinEncriptar);
    }

    @Override
    public boolean comparar(String contraseniaSinEncriptar, String contraseniaEncriptado) {
        // TODO Auto-generated method stub
        return bCryptPasswordEncoder.matches(contraseniaSinEncriptar, contraseniaEncriptado);
    }

 

}
