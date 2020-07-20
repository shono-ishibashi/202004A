package com.service;

import com.domain.Order;
import com.domain.User;
import com.repository.OrderItemRepository;
import com.repository.OrderRepository;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import static java.util.Objects.isNull;

@Service
public class LoginService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private HttpSession session;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (!isNull(user)) {
            if (isNull(session.getAttribute("userId"))) {
                session.setAttribute("userId", user.getId());
                return user;
            } else {
                Integer temporaryId = (Integer) session.getAttribute("userId");
                session.setAttribute("userId", user.getId());
                orderItemRepository.updateOrderId(temporaryId, user.getId());
                return user;
            }
        } else {
            throw new UsernameNotFoundException("そのemailは登録されていません");
        }
    }
}
