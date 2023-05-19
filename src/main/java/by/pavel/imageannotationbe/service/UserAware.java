package by.pavel.imageannotationbe.service;

import by.pavel.imageannotationbe.security.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;

public interface UserAware {

    default UserDetailsImpl getCurrentUser() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
