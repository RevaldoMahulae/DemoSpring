package com.example.demo.util;

public class Constants {
    public static final String API_USER = "/users";
    public static final String API_GET_USER_BY_ID = "/{id}";
    public static final String API_CREATE_USER = "/create";
    public static final String API_UPDATE_USER = "/update/{id}";
    public static final String API_DELETE_USER = "/delete/{id}";
    public static final String API_GET_USER_ROLES = "/{id}/roles";
    public static final String API_GET_USER_DIVISIONS = "/{id}/divisions";
    public static final String API_RESTORE_USER = "/restore/{id}";
	
    public static final String PARAM_ID = "id";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_NIK = "nik";
    public static final String PARAM_DOB = "dob";
    public static final String PARAM_USER_ID = "userId";
    public static final String PARAM_ROLE_ID = "roleId";
    public static final String PARAM_DIVISION_ID = "divisionId";
    public static final String PARAM_SORT_BY = "sortBy";
    public static final String PARAM_DIRECTION = "direction";

    public static final String YEAR_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_SORT_BY = "name";
    public static final String DEFAULT_DIRECTION = "ASC";

    public static final String ERROR_UPDATING_USER = "Error updating user";
    public static final String USER_NOT_FOUND = "User tidak ditemukan";
    public static final String DOB_FORMAT_ERROR = "Format tanggal lahir (dob) harus yyyy-MM-dd.";
    public static final String SAVE_USER_ERROR = "Gagal save User";
    public static final String EMAIL_SEND_ERROR = "User berhasil dibuat, tetapi email gagal dikirim.";
    public static final String EMAIL_HRD = "email@qualitas.co.id";
    public static final String EMAIL_SUBJECT_NEW_USER = "User Baru Telah Dibuat";

    public static final String QUERY_GET_ALL_USERS = "SELECT id, name, email, nik, dob FROM users WHERE is_deleted = false ORDER BY %s %s";
    public static final String QUERY_FIND_USER_BY_ID = "SELECT id, name, email, nik, dob FROM users WHERE id = :id";
    public static final String QUERY_INSERT_USER = "INSERT INTO users (name, email, nik, dob) VALUES (:name, :email, :nik, :dob) RETURNING id";
    public static final String QUERY_INSERT_USER_ROLE = "INSERT INTO user_roles (user_id, role_id) VALUES (:userId, :roleId)";
    public static final String QUERY_INSERT_USER_DIVISION = "INSERT INTO user_divisions (user_id, division_id) VALUES (:userId, :divisionId)";
    public static final String QUERY_DELETE_USER_ROLE = "DELETE FROM user_roles WHERE user_id = :id";
    public static final String QUERY_DELETE_USER_DIVISION = "DELETE FROM user_divisions WHERE user_id = :id";
    public static final String QUERY_UPDATE_USER = "UPDATE users SET name = :name, email = :email, nik = :nik, dob = :dob WHERE id = :id";
    public static final String QUERY_SOFT_DELETE_USER = "UPDATE User SET isDeleted = true WHERE id = :id";
    public static final String QUERY_RESTORE_USER = "UPDATE User SET isDeleted = false WHERE id = :id";
    public static final String QUERY_GET_USER_ROLES = 
            "SELECT r.role_name FROM roles r " +
            "JOIN user_roles ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = :userId";
    public static final String QUERY_GET_USER_DIVISIONS = 
            "SELECT d.division_name FROM divisions d " +
            "JOIN user_divisions ud ON d.id = ud.division_id " +
            "WHERE ud.user_id = :userId";
    
    
}
