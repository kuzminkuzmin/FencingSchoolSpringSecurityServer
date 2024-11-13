package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.Admin;

import java.util.List;

public interface AdminService {
    Admin addAdmin(Admin admin);

    Admin get(String login, String password);

    //Admin get(String login);

    Admin get(long id);

    List<Admin> getAllAdmins();

    Admin updateAdmin(Admin admin);

    Admin deleteAdmin(long id);
}
