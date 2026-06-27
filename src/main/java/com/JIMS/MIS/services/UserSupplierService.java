package com.JIMS.MIS.services;

import com.JIMS.MIS.Repository.UserSupplierRepository;
import com.JIMS.MIS.model.UserSupplier;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserSupplierService {

    private final UserSupplierRepository repo;

    public UserSupplierService(UserSupplierRepository repo) {
        this.repo = repo;
    }

    public UserSupplier assignUserToSupplier(Long uid, Long supid, String euid) {
        UserSupplier us = new UserSupplier();
        us.setUid(uid);
        us.setSupid(supid);
        us.setEuid(euid);
        us.setEdt(LocalDateTime.now());
        return repo.save(us);
    }
}
